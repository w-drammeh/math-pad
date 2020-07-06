package utg;

import javax.swing.*;

public class Constants {
    public static final int MAX_INPUT = Integer.MAX_VALUE;
    public static final int INPUT_LENGTH = String.valueOf(MAX_INPUT).length();
    public static final int SLEEP_LENGTH = 500;

    public static void reportOverflowError(JComponent parent){
        JOptionPane.showMessageDialog(parent, "Sorry, input cannot be greater than "+MAX_INPUT,
                "Overflow Error", JOptionPane.ERROR_MESSAGE);
    }

}
