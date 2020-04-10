package ui;

import javax.swing.*;
import java.awt.*;

public class MPanel extends JPanel {


    public MPanel(){
        super();
    }

    public MPanel(LayoutManager layout){
        super(layout);
    }

    public MPanel(LayoutManager layout, JComponent... components){
        this(layout);
        for (JComponent c : components) {
            this.add(c);
        }
    }

    public MPanel(JComponent... components){
        this();
        this.addAll(components);
    }

    public void addAll(Component... components){
        for (Component c : components) {
            this.add(c);
        }
    }

}
