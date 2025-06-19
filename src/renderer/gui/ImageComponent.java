package renderer.gui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageComponent extends Component {

    BufferedImage renderedImage = null;

    public ImageComponent(BufferedImage init) {
        renderedImage = init;
    }

    public BufferedImage swapImage(BufferedImage bi) {
        BufferedImage ret = renderedImage;
        renderedImage = bi;
        return ret;
    }

    public void paint(Graphics2D g) {

        if (renderedImage != null) {
            g.drawImage(renderedImage, new AffineTransform(1f, 0f, 0f, 1f, 0, 0), null);
        }
    }

}
