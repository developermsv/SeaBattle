package View;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by developermsv on 05.06.2015.
 */
class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(createIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    protected static ImageIcon createIcon(String path) {
        URL imgURL = GameWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("File not found " + path);
            return null;
        }
    }

}



