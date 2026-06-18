import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import renderer.utils.Bresenham;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Rasterizer class.
 *
 */
public class TestRasterizer {

    /**
     * Distance used for test points.
     */
    private static final int DISTANCE = 100;

    /**
     * Origin point for tests.
     */
    private static final int ORIGIN_X = 100;

    /**
     * Origin point for tests.
     */
    private static final int ORIGIN_Y = 100;

    /**
     * Helper method to call Rasterizer.getLine and collect points into a list.
     *
     * @param x0 starting x coordinate
     * @param y0 starting y coordinate
     * @param x1 ending x coordinate
     * @param y1 ending y coordinate
     * @return list of points returned by Rasterizer.getLine
     */
    private List<Point> getLine(int x0, int y0, int x1, int y1) {
        List<Point> callbackResult = new ArrayList<>();
        Bresenham.getLine(x0, y0, x1, y1, (x, y) -> callbackResult.add(new Point(x, y)));
        return callbackResult;

        // return Rasterizer.getLine(x0, y0, x1, y1);
    }

    /**
     * Asserts that a line result has the expected basic properties.
     *
     * @param result the result to check
     * @param expectedSize the expected number of points
     */
    private void assertLineBasics(List<Point> result, int expectedSize) {
        assertNotNull("Result should not be null", result);
        assertEquals("Should return correct number of points",
                expectedSize, result.size());
    }

    /**
     * Asserts that the first point of a line matches expected coordinates.
     *
     * @param result the line result
     * @param expectedX the expected x coordinate
     * @param expectedY the expected y coordinate
     */
    private void assertFirstPoint(List<Point> result,
            int expectedX, int expectedY) {
        assertEquals("First point x should match", expectedX,
                result.get(0).x);
        assertEquals("First point y should match", expectedY,
                result.get(0).y);
    }

    /**
     * Asserts that the last point of a line matches expected coordinates.
     *
     * @param result the line result
     * @param expectedX the expected x coordinate
     * @param expectedY the expected y coordinate
     */
    private void assertLastPoint(List<Point> result,
            int expectedX, int expectedY) {
        Point lastPoint = result.get(result.size() - 1);
        assertEquals("Last point x should match", expectedX, lastPoint.x);
        assertEquals("Last point y should match", expectedY, lastPoint.y);
    }

    /**
     * Asserts that all points in a line have the same x coordinate.
     *
     * @param result the line result
     * @param expectedX the expected x coordinate
     */
    private void assertConstantX(List<Point> result, int expectedX) {
        for (Point p : result) {
            assertEquals("All points should have same x coordinate",
                    expectedX, p.x);
        }
    }

    /**
     * Asserts that all points in a line have the same y coordinate.
     *
     * @param result the line result
     * @param expectedY the expected y coordinate
     */
    private void assertConstantY(List<Point> result, int expectedY) {
        for (Point p : result) {
            assertEquals("All points should have same y coordinate",
                    expectedY, p.y);
        }
    }

    /**
     * Asserts that consecutive points form a continuous line.
     *
     * @param result the line result
     */
    private void assertLineContinuity(List<Point> result) {
        assertTrue("Should have at least 2 points", result.size() >= 2);
        for (int i = 0; i < result.size() - 1; i++) {
            Point current = result.get(i);
            Point next = result.get(i + 1);
            int dx = Math.abs(next.x - current.x);
            int dy = Math.abs(next.y - current.y);

            assertTrue("Points adjacent in x (dx=" + dx + ")", dx <= 1);
            assertTrue("Points adjacent in y (dy=" + dy + ")", dy <= 1);
            assertTrue("Points not same (distance > 0)", dx + dy > 0);
            assertTrue("Points connected (Manhattan <= 2)", dx + dy <= 2);
        }
    }

    /**
     * Asserts that two line results are identical.
     *
     * @param result1 first line result
     * @param result2 second line result
     */
    private void assertLinesEqual(List<Point> result1,
            List<Point> result2) {
        assertNotNull("First result should not be null", result1);
        assertNotNull("Second result should not be null", result2);
        assertEquals("Both results should have same number of points",
                result1.size(), result2.size());

        for (int i = 0; i < result1.size(); i++) {
            assertEquals("Point " + i + " x coordinate should match",
                    result1.get(i).x, result2.get(i).x);
            assertEquals("Point " + i + " y coordinate should match",
                    result1.get(i).y, result2.get(i).y);
        }
    }

    /**
     * Asserts two line results have same points (possibly reversed).
     *
     * @param result1 first line result
     * @param result2 second line result
     */
    private void assertLinesEqualOrReversed(List<Point> result1,
            List<Point> result2) {
        assertNotNull("First result should not be null", result1);
        assertNotNull("Second result should not be null", result2);
        assertEquals("Both results should have same number of points",
                result1.size(), result2.size());

        // Check if they're equal in normal order
        boolean normalOrder = true;
        for (int i = 0; i < result1.size(); i++) {
            if (result1.get(i).x != result2.get(i).x
                    || result1.get(i).y != result2.get(i).y) {
                normalOrder = false;
                break;
            }
        }

        if (normalOrder) {
            return; // Lines are equal in normal order
        }

        // Check if they're equal in reversed order
        for (int i = 0; i < result1.size(); i++) {
            int reverseIdx = result1.size() - 1 - i;
            assertEquals("Point " + i + " x should match reversed",
                    result1.get(i).x, result2.get(reverseIdx).x);
            assertEquals("Point " + i + " y should match reversed",
                    result1.get(i).y, result2.get(reverseIdx).y);
        }
    }

    /**
     * Tests getLine with the same point as input.
     * Expected: A single point is returned.
     */
    @Test
    public void getLineSamePointReturnsSinglePoint() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y);

        assertLineBasics(result, 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
    }

    /**
     * Tests getLine with horizontal line going right (positive x).
     * Expected: A line of points with increasing x and constant y.
     */
    @Test
    public void getLineHorizontalLineRightReturnsCorrectPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X + DISTANCE, ORIGIN_Y);

        assertLineBasics(result, DISTANCE + 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X + DISTANCE, ORIGIN_Y);
        assertConstantY(result, ORIGIN_Y);

        // Check x coordinates are sequential
        for (int i = 0; i < result.size(); i++) {
            assertEquals("X coordinates should be sequential",
                    ORIGIN_X + i, result.get(i).x);
        }
    }

    /**
     * Tests getLine with horizontal line going left (negative x).
     * Expected: The line is normalized and drawn from right to left.
     */
    @Test
    public void getLineHorizontalLineLeftReturnsCorrectPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X - DISTANCE, ORIGIN_Y);

        assertLineBasics(result, DISTANCE + 1);
        // Normalized to draw left to right
        assertFirstPoint(result, ORIGIN_X - DISTANCE, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y);
        assertConstantY(result, ORIGIN_Y);
    }

    /**
     * Tests getLine with vertical line going up (positive y).
     * Expected: A line of points with constant x and increasing y.
     */
    @Test
    public void getLineVerticalLineUpReturnsCorrectPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y + DISTANCE);

        assertLineBasics(result, DISTANCE + 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y + DISTANCE);
        assertConstantX(result, ORIGIN_X);
    }

    /**
     * Tests getLine with vertical line going down (negative y).
     * Expected: A line of points with constant x and varying y.
     */
    @Test
    public void getLineVerticalLineDownReturnsCorrectPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y - DISTANCE);

        assertLineBasics(result, DISTANCE + 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y - DISTANCE);
        assertConstantX(result, ORIGIN_X);
    }

    /**
     * Tests getLine with diagonal northeast (positive x, positive y).
     * Expected: A line with both x and y increasing at 45 degrees.
     */
    @Test
    public void getLineDiagonalNortheastReturnsCorrectPoints() {
        final int endX = ORIGIN_X + DISTANCE;
        final int endY = ORIGIN_Y + DISTANCE;
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                endX, endY);

        assertLineBasics(result, DISTANCE + 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, endX, endY);

        // Check points form a diagonal (x and y increase together)
        for (int i = 0; i < result.size(); i++) {
            Point p = result.get(i);
            assertEquals("X should increase steadily",
                    ORIGIN_X + i, p.x);
            assertEquals("Y should increase steadily",
                    ORIGIN_Y + i, p.y);
        }
    }

    /**
     * Tests getLine with diagonal southeast (positive x, negative y).
     * Expected: A line with x increasing and y decreasing at 45 degrees.
     */
    @Test
    public void getLineDiagonalSoutheastReturnsCorrectPoints() {
        final int endX = ORIGIN_X + DISTANCE;
        final int endY = ORIGIN_Y - DISTANCE;
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                endX, endY);

        assertLineBasics(result, DISTANCE + 1);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, endX, endY);

        // Check points form a diagonal (x increases, y decreases)
        for (int i = 0; i < result.size(); i++) {
            Point p = result.get(i);
            assertEquals("X should increase steadily",
                    ORIGIN_X + i, p.x);
            assertEquals("Y should decrease steadily",
                    ORIGIN_Y - i, p.y);
        }
    }

    /**
     * Tests getLine with diagonal southwest (negative x, negative y).
     * Expected: Both x and y decreasing, normalized left to right.
     */
    @Test
    public void getLineDiagonalSouthwestReturnsCorrectPoints() {
        final int endX = ORIGIN_X - DISTANCE;
        final int endY = ORIGIN_Y - DISTANCE;
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                endX, endY);

        assertLineBasics(result, DISTANCE + 1);
        // Normalized to draw left to right
        assertFirstPoint(result, endX, endY);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y);

        // Check points form a diagonal line
        for (int i = 0; i < result.size(); i++) {
            Point p = result.get(i);
            assertEquals("X should increase from left", endX + i, p.x);
            assertEquals("Y should increase from bottom", endY + i, p.y);
        }
    }

    /**
     * Tests getLine with diagonal northwest (negative x, positive y).
     * Expected: x decreasing and y increasing, normalized left to right.
     */
    @Test
    public void getLineDiagonalNorthwestReturnsCorrectPoints() {
        final int endX = ORIGIN_X - DISTANCE;
        final int endY = ORIGIN_Y + DISTANCE;
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                endX, endY);

        assertLineBasics(result, DISTANCE + 1);
        // Normalized to draw left to right
        assertFirstPoint(result, endX, endY);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y);

        // Check points form a diagonal (x increases, y decreases)
        for (int i = 0; i < result.size(); i++) {
            Point p = result.get(i);
            assertEquals("X should increase from left", endX + i, p.x);
            assertEquals("Y should decrease from top", endY - i, p.y);
        }
    }

    /**
     * Tests getLine with inverted points produces consistent results.
     * Expected: Points should define same line regardless of order.
     */
    @Test
    public void getLineInvertedPointsProducesConsistentLine() {
        final int endX = ORIGIN_X + DISTANCE;
        final int endY = ORIGIN_Y + DISTANCE;

        List<Point> result1 = getLine(ORIGIN_X, ORIGIN_Y,
                endX, endY);
        List<Point> result2 = getLine(endX, endY,
                ORIGIN_X, ORIGIN_Y);

        assertLinesEqual(result1, result2);
    }

    /**
     * Tests getLine with inverted vertical produces consistent results.
     * Expected: Points define same line (possibly in reversed order).
     */
    @Test
    public void getLineInvertedVerticalLineProducesConsistentLine() {
        final int endY = ORIGIN_Y + DISTANCE;

        List<Point> result1 = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X, endY);
        List<Point> result2 = getLine(ORIGIN_X, endY,
                ORIGIN_X, ORIGIN_Y);

        assertLinesEqualOrReversed(result1, result2);
    }

    /**
     * Tests getLine ensures the line is continuous.
     * Expected: Each point adjacent to next (Manhattan distance <= 2).
     */
    @Test
    public void getLineArbitraryLineProducesContinuousLine() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X + DISTANCE, ORIGIN_Y + (DISTANCE / 2));

        assertNotNull("Result should not be null", result);
        assertLineContinuity(result);
    }

    /**
     * Tests getLine with single pixel displacement in x.
     * Expected: A line with two points.
     */
    @Test
    public void getLineSinglePixelHorizontalReturnsTwoPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X + 1, ORIGIN_Y);

        assertLineBasics(result, 2);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X + 1, ORIGIN_Y);
    }

    /**
     * Tests getLine with single pixel displacement in y.
     * Expected: A line with two points.
     */
    @Test
    public void getLineSinglePixelVerticalReturnsTwoPoints() {
        List<Point> result = getLine(ORIGIN_X, ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y + 1);

        assertLineBasics(result, 2);
        assertFirstPoint(result, ORIGIN_X, ORIGIN_Y);
        assertLastPoint(result, ORIGIN_X, ORIGIN_Y + 1);
    }
}
