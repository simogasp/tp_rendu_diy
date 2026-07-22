package renderer.utils;

import java.util.function.BiConsumer;

public final class Bresenham {

    private Bresenham() {
        // Prevent instantiation
    }

    /**
      * Rasterizes the edge between the projected points p0 and p1.
      * Calls the provided callback for each point on the line.
      *
      * @param x0 the x coordinate of the first point of the edge
      * @param y0 the y coordinate of the first point of the edge
      * @param x1 the x coordinate of the second point of the edge
      * @param y1 the y coordinate of the second point of the edge
      * @param callback callback invoked for each pixel (x, y)
      */
    public static void getLine(int x0, int y0, int x1, int y1,
                               BiConsumer<Integer, Integer> callback) {
        // Easy case: the two points are the same
        if (x0 == x1 && y0 == y1) {
            callback.accept(x0, y0);
            return;
        }
        // Normalize input so we always draw from left to right
        if (x0 <= x1) {
            bresenhamLine(x0, y0, x1, y1, callback);
        } else {
            bresenhamLine(x1, y1, x0, y0, callback);
        }
    }

    private static void bresenhamLine(int x0, int y0, int x1, int y1,
                                      BiConsumer<Integer, Integer> cb) {
        int dx = x1 - x0;
        int dy = Math.abs(y1 - y0);

        final int sx = 1;
        final int sy = (y0 < y1) ? 1 : -1;

        final boolean steep = dy > dx;

        if (steep) {
            int temp = dx;
            dx = dy;
            dy = temp;
        }

        int err = 2 * dy - dx;
        int x = x0;
        int y = y0;

        for (int i = 0; i <= dx; i++) {
            cb.accept(x, y);

            while (err >= 0) {
                if (steep) {
                    x += sx;
                } else {
                    y += sy;
                }
                err -= 2 * dx;
            }

            if (steep) {
                y += sy;
            } else {
                x += sx;
            }

            err += 2 * dy;
        }
    }
}


