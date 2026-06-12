package core.shader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import renderer.core.shader.DepthBuffer;
import renderer.core.shader.fragmentshaders.Fragment;

/**
 * Unit tests for the DepthBuffer class.
 */
public class DepthBufferTest {

    /** The buffer width used for testing. */
    private static final int BUFFER_WIDTH = 10;
    /** The buffer height used for testing. */
    private static final int BUFFER_HEIGHT = 8;
    /** The new buffer width used for testing resize. */
    private static final int NEW_BUFFER_WIDTH = 15;
    /** The new buffer height used for testing resize. */
    private static final int NEW_BUFFER_HEIGHT = 12;
    /** The X coordinate used for testing. */
    private static final int X_COORD = 5;
    /** The Y coordinate used for testing. */
    private static final int Y_COORD = 4;
    /** The depth value close to camera. */
    private static final double DEPTH_CLOSE = 0.3;
    /** The depth value middle distance. */
    private static final double DEPTH_MID = 0.5;
    /** The depth value far from camera. */
    private static final double DEPTH_FAR = 0.8;
    /** Very small positive depth value. */
    private static final double DEPTH_VERY_SMALL = 1e-10;
    /** Slightly larger small depth value. */
    private static final double DEPTH_SLIGHTLY_LARGER = 2e-10;
    /** Negative depth value. */
    private static final double DEPTH_NEGATIVE = -0.5;

    /** The depth buffer instance used for testing. */
    private DepthBuffer depthBuffer;

    /**
     * Set up the test fixture before each test.
     */
    @Before
    public void setUp() {
        depthBuffer = new DepthBuffer(BUFFER_WIDTH, BUFFER_HEIGHT);
    }

    // ==================== Constructor Tests ====================

    /**
     * Test creating a DepthBuffer with valid dimensions.
     */
    @Test
    public void testDepthBufferCreation() {
        assertNotNull(depthBuffer);
        assertEquals(BUFFER_WIDTH, depthBuffer.width());
        assertEquals(BUFFER_HEIGHT, depthBuffer.height());
    }

    /**
     * Test creating a DepthBuffer with minimum dimensions.
     */
    @Test
    public void testDepthBufferCreationMinimumDimensions() {
        final DepthBuffer smallBuffer = new DepthBuffer(1, 1);
        assertNotNull(smallBuffer);
        assertEquals(1, smallBuffer.width());
        assertEquals(1, smallBuffer.height());
    }

    // ==================== Clear Tests ====================

    /**
     * Test that clear sets all depths to infinity.
     */
    @Test
    public void testClearSetsAllDepthsToInfinity() {
        // Explicitly clear the buffer
        depthBuffer.clear();

        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);

        // Any fragment should pass test after clear
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that clear resets buffer after writing fragments.
     */
    @Test
    public void testClearResetsBufferAfterWriting() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);

        // Write a fragment
        depthBuffer.writeFragment(fragment);

        // Same fragment should not pass test (equal depth)
        assertFalse(depthBuffer.testFragment(fragment));

        // Clear the buffer
        depthBuffer.clear();

        // Now the fragment should pass test again
        assertTrue(depthBuffer.testFragment(fragment));
    }

    // ==================== TestFragment Tests ====================

    /**
     * Test that a fragment passes when buffer is cleared (infinite depth).
     */
    @Test
    public void testFragmentPassesOnClearedBuffer() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that a closer fragment passes the depth test.
     */
    @Test
    public void testCloserFragmentPasses() {
        final Fragment farFragment = new Fragment(X_COORD, Y_COORD);
        farFragment.setDepth(DEPTH_FAR);
        depthBuffer.writeFragment(farFragment);

        final Fragment closeFragment = new Fragment(X_COORD, Y_COORD);
        closeFragment.setDepth(DEPTH_CLOSE);
        assertTrue(depthBuffer.testFragment(closeFragment));
    }

    /**
     * Test that a farther fragment fails the depth test.
     */
    @Test
    public void testFartherFragmentFails() {
        final Fragment closeFragment = new Fragment(X_COORD, Y_COORD);
        closeFragment.setDepth(DEPTH_CLOSE);
        depthBuffer.writeFragment(closeFragment);

        final Fragment farFragment = new Fragment(X_COORD, Y_COORD);
        farFragment.setDepth(DEPTH_FAR);
        assertFalse(depthBuffer.testFragment(farFragment));
    }

    /**
     * Test that a fragment with equal depth fails the depth test.
     */
    @Test
    public void testEqualDepthFragmentFails() {
        final Fragment fragment1 = new Fragment(X_COORD, Y_COORD);
        fragment1.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment1);

        final Fragment fragment2 = new Fragment(X_COORD, Y_COORD);
        fragment2.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment2));
    }

    /**
     * Test that fragments at different positions are independent.
     */
    @Test
    public void testFragmentsAtDifferentPositionsIndependent() {
        final Fragment fragment1 = new Fragment(X_COORD, Y_COORD);
        fragment1.setDepth(DEPTH_CLOSE);
        depthBuffer.writeFragment(fragment1);

        final Fragment fragment2 = new Fragment(X_COORD + 1, Y_COORD);
        fragment2.setDepth(DEPTH_FAR);
        assertTrue(depthBuffer.testFragment(fragment2));
    }

    /**
     * Test that fragment at origin position works correctly.
     */
    @Test
    public void testFragmentAtOrigin() {
        final Fragment fragment = new Fragment(0, 0);
        fragment.setDepth(DEPTH_MID);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that fragment at maximum valid position works correctly.
     */
    @Test
    public void testFragmentAtMaximumPosition() {
        final Fragment fragment =
            new Fragment(BUFFER_WIDTH - 1, BUFFER_HEIGHT - 1);
        fragment.setDepth(DEPTH_MID);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    // ==================== Out of Bounds Tests ====================

    /**
     * Test that fragment with negative X coordinate is out of bounds.
     */
    @Test
    public void testFragmentWithNegativeXOutOfBounds() {
        final Fragment fragment = new Fragment(-1, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that fragment with negative Y coordinate is out of bounds.
     */
    @Test
    public void testFragmentWithNegativeYOutOfBounds() {
        final Fragment fragment = new Fragment(X_COORD, -1);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that fragment with X beyond width is out of bounds.
     */
    @Test
    public void testFragmentWithXBeyondWidthOutOfBounds() {
        final Fragment fragment = new Fragment(BUFFER_WIDTH, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that fragment with Y beyond height is out of bounds.
     */
    @Test
    public void testFragmentWithYBeyondHeightOutOfBounds() {
        final Fragment fragment = new Fragment(X_COORD, BUFFER_HEIGHT);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that fragment with both coordinates out of bounds fails.
     */
    @Test
    public void testFragmentWithBothCoordinatesOutOfBounds() {
        final Fragment fragment = new Fragment(-1, -1);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    // ==================== WriteFragment Tests ====================

    /**
     * Test writing a fragment updates the buffer.
     */
    @Test
    public void testWriteFragmentUpdatesBuffer() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);

        // Fragment passes before writing
        assertTrue(depthBuffer.testFragment(fragment));

        // Write the fragment
        depthBuffer.writeFragment(fragment);

        // Fragment no longer passes (equal depth)
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test writing an out of bounds fragment does not crash.
     */
    @Test
    public void testWriteOutOfBoundsFragmentDoesNotCrash() {
        final Fragment fragment = new Fragment(-1, -1);
        fragment.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment); // Should not throw exception
    }

    /**
     * Test writing multiple fragments to same position keeps closest.
     */
    @Test
    public void testWriteMultipleFragmentsSamePositionKeepsClosest() {
        final Fragment farFragment = new Fragment(X_COORD, Y_COORD);
        farFragment.setDepth(DEPTH_FAR);
        depthBuffer.writeFragment(farFragment);

        final Fragment closeFragment = new Fragment(X_COORD, Y_COORD);
        closeFragment.setDepth(DEPTH_CLOSE);
        depthBuffer.writeFragment(closeFragment);

        // A fragment with middle depth should fail
        final Fragment midFragment = new Fragment(X_COORD, Y_COORD);
        midFragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(midFragment));

        // But an even closer fragment should pass
        final Fragment closerFragment = new Fragment(X_COORD, Y_COORD);
        closerFragment.setDepth(DEPTH_CLOSE / 2);
        assertTrue(depthBuffer.testFragment(closerFragment));
    }

    // ==================== Resize Tests ====================

    /**
     * Test resizing buffer to larger dimensions.
     */
    @Test
    public void testResizeBufferToLargerDimensions() {
        depthBuffer.resize(NEW_BUFFER_WIDTH, NEW_BUFFER_HEIGHT);
        assertEquals(NEW_BUFFER_WIDTH, depthBuffer.width());
        assertEquals(NEW_BUFFER_HEIGHT, depthBuffer.height());
    }

    /**
     * Test resizing buffer to smaller dimensions.
     */
    @Test
    public void testResizeBufferToSmallerDimensions() {
        final int smallWidth = 5;
        final int smallHeight = 4;
        depthBuffer.resize(smallWidth, smallHeight);
        assertEquals(smallWidth, depthBuffer.width());
        assertEquals(smallHeight, depthBuffer.height());
    }

    /**
     * Test resizing buffer clears all depths.
     */
    @Test
    public void testResizeClearsBuffer() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment);

        // Resize to same dimensions
        depthBuffer.resize(BUFFER_WIDTH, BUFFER_HEIGHT);

        // Fragment should pass after resize (buffer cleared)
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test resizing buffer to same dimensions still clears.
     */
    @Test
    public void testResizeSameDimensionsStillClears() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment);

        // Resize to same dimensions
        depthBuffer.resize(BUFFER_WIDTH, BUFFER_HEIGHT);

        // Buffer should be cleared
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that positions valid before resize become invalid after shrinking.
     */
    @Test
    public void testPositionsInvalidAfterShrinking() {
        final int smallWidth = 5;
        final int smallHeight = 4;
        depthBuffer.resize(smallWidth, smallHeight);

        final Fragment fragment = new Fragment(BUFFER_WIDTH - 1, BUFFER_HEIGHT - 1);
        fragment.setDepth(DEPTH_MID);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test that new positions become valid after expanding.
     */
    @Test
    public void testNewPositionsValidAfterExpanding() {
        depthBuffer.resize(NEW_BUFFER_WIDTH, NEW_BUFFER_HEIGHT);

        final Fragment fragment =
            new Fragment(NEW_BUFFER_WIDTH - 1, NEW_BUFFER_HEIGHT - 1);
        fragment.setDepth(DEPTH_MID);
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test resizing only width dimension.
     */
    @Test
    public void testResizeWidthOnly() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment);

        // Resize only width
        depthBuffer.resize(NEW_BUFFER_WIDTH, BUFFER_HEIGHT);

        assertEquals(NEW_BUFFER_WIDTH, depthBuffer.width());
        assertEquals(BUFFER_HEIGHT, depthBuffer.height());

        // Buffer should be cleared
        assertTrue(depthBuffer.testFragment(fragment));
    }

    /**
     * Test resizing only height dimension.
     */
    @Test
    public void testResizeHeightOnly() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_MID);
        depthBuffer.writeFragment(fragment);

        // Resize only height
        depthBuffer.resize(BUFFER_WIDTH, NEW_BUFFER_HEIGHT);

        assertEquals(BUFFER_WIDTH, depthBuffer.width());
        assertEquals(NEW_BUFFER_HEIGHT, depthBuffer.height());

        // Buffer should be cleared
        assertTrue(depthBuffer.testFragment(fragment));
    }

    // ==================== Width and Height Tests ====================

    /**
     * Test width returns correct value.
     */
    @Test
    public void testWidthReturnsCorrectValue() {
        assertEquals(BUFFER_WIDTH, depthBuffer.width());
    }

    /**
     * Test height returns correct value.
     */
    @Test
    public void testHeightReturnsCorrectValue() {
        assertEquals(BUFFER_HEIGHT, depthBuffer.height());
    }

    /**
     * Test width and height update after resize.
     */
    @Test
    public void testWidthHeightUpdateAfterResize() {
        depthBuffer.resize(NEW_BUFFER_WIDTH, NEW_BUFFER_HEIGHT);
        assertEquals(NEW_BUFFER_WIDTH, depthBuffer.width());
        assertEquals(NEW_BUFFER_HEIGHT, depthBuffer.height());
    }

    // ==================== Special Depth Values Tests ====================

    /**
     * Test fragment with depth zero.
     */
    @Test
    public void testFragmentWithDepthZero() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(0.0);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test fragment with depth one.
     */
    @Test
    public void testFragmentWithDepthOne() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(1.0);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    /**
     * Test fragment with very small positive depth.
     */
    @Test
    public void testFragmentWithVerySmallDepth() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_VERY_SMALL);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);

        final Fragment slightlyFarther = new Fragment(X_COORD, Y_COORD);
        slightlyFarther.setDepth(DEPTH_SLIGHTLY_LARGER);
        assertFalse(depthBuffer.testFragment(slightlyFarther));
    }

    /**
     * Test fragment with negative depth.
     */
    @Test
    public void testFragmentWithNegativeDepth() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_NEGATIVE);
        assertTrue(depthBuffer.testFragment(fragment));
        depthBuffer.writeFragment(fragment);
        assertFalse(depthBuffer.testFragment(fragment));
    }

    // ==================== Sequential Operations Tests ====================

    /**
     * Test sequence of write and test operations.
     */
    @Test
    public void testSequentialWriteAndTestOperations() {
        final Fragment frag1 = new Fragment(X_COORD, Y_COORD);
        frag1.setDepth(DEPTH_FAR);

        final Fragment frag2 = new Fragment(X_COORD, Y_COORD);
        frag2.setDepth(DEPTH_MID);

        final Fragment frag3 = new Fragment(X_COORD, Y_COORD);
        frag3.setDepth(DEPTH_CLOSE);

        // All should pass initially
        assertTrue(depthBuffer.testFragment(frag1));
        assertTrue(depthBuffer.testFragment(frag2));
        assertTrue(depthBuffer.testFragment(frag3));

        // Write far fragment
        depthBuffer.writeFragment(frag1);

        // Mid and close should pass, far should fail
        assertTrue(depthBuffer.testFragment(frag2));
        assertTrue(depthBuffer.testFragment(frag3));
        assertFalse(depthBuffer.testFragment(frag1));

        // Write mid fragment
        depthBuffer.writeFragment(frag2);

        // Only close should pass
        assertTrue(depthBuffer.testFragment(frag3));
        assertFalse(depthBuffer.testFragment(frag1));
        assertFalse(depthBuffer.testFragment(frag2));

        // Write close fragment
        depthBuffer.writeFragment(frag3);

        // None should pass
        assertFalse(depthBuffer.testFragment(frag1));
        assertFalse(depthBuffer.testFragment(frag2));
        assertFalse(depthBuffer.testFragment(frag3));
    }

    /**
     * Test writing fragments to all buffer positions.
     */
    @Test
    public void testWriteFragmentsToAllPositions() {
        // Write a fragment to every position
        for (int y = 0; y < BUFFER_HEIGHT; y++) {
            for (int x = 0; x < BUFFER_WIDTH; x++) {
                final Fragment fragment = new Fragment(x, y);
                fragment.setDepth(DEPTH_MID);
                assertTrue(depthBuffer.testFragment(fragment));
                depthBuffer.writeFragment(fragment);
                assertFalse(depthBuffer.testFragment(fragment));
            }
        }
    }
}
