package ui;

import javax.swing.*;
import java.awt.*;

public class MScrollPane extends JScrollPane {


    public MScrollPane(Component insider){
        super(insider);
    }

    public void toTop(){
        this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getMinimum());
    }

    public void toBottom(){
        this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getMaximum());
    }

    public static MScrollPane newTextAreaScroller(JTextArea textArea){
        final MScrollPane scrollPane = new MScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(MScrollPane.VERTICAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

}
