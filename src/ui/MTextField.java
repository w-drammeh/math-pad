package ui;

import maha.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MTextField extends JTextField{


	public MTextField(){
        super();
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    @Override
    public void paste() {
	    //No nothing
    }

    public static MTextField digitOnlyField(){
        final MTextField digitField = new MTextField();
        digitField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) && !(e.getKeyChar() == KeyEvent.VK_DELETE || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        return digitField;
    }

    public static MTextField rangeControlledField(int range) {
        final MTextField rangeField = new MTextField();
        rangeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (rangeField.getText().length() >= range) {
                    e.consume();
                }
            }
        });

        return rangeField;
    }

    public static MTextField rangeControlledField(){
	    return rangeControlledField(Constants.INPUT_LENGTH);
    }

    public static MTextField digitPlusRangeControlledField(int range, String... allowedStrings) {
        final MTextField textField = new MTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField.getText().length() >= range || !Character.isDigit(e.getKeyChar()) &&
                        !(e.getKeyChar() == KeyEvent.VK_DELETE || e.getKeyChar() == KeyEvent.VK_BACK_SPACE || isIn(String.valueOf(e.getKeyChar()), allowedStrings))) {
                    e.consume();
                }
            }
        });

        return textField;
    }

    public static MTextField digitPlusRangeControlledField(){
	    return digitPlusRangeControlledField(Constants.INPUT_LENGTH);
    }

    private static boolean isIn(String checkString, String... strings){
        for (String s : strings) {
            if (s.equalsIgnoreCase(checkString)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasText(){
        return hasText(this.getText());
    }

    public static boolean hasText(String t){
        return !(t == null || t.length() == 0);
    }

    public void setText(int t) {
        super.setText(String.valueOf(t));
    }

}
