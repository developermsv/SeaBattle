package View;

import javax.swing.*;

/**
 * Created by developermsv on 05.06.2015.
 */
public class MyJLabel extends JLabel {
    public ImageIcon getStateIcon() {
        return stateIcon;
    }

    public void setStateIcon(ImageIcon stateIcon) {
        this.stateIcon = stateIcon;
    }

    ImageIcon stateIcon;
}
