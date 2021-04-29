package math.others;

import run.MathPad;
import ui.FontFactory;
import ui.MPanel;
import ui.MTextField;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class LeapYearTester extends JDialog {
    private static LeapYearTester runtimeInstance;


    private LeapYearTester(){
        this.setTitle("Leap Year Tester");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final JLabel resultLabel = new JLabel();
        resultLabel.setFont(FontFactory.createBoldFont(20));

        final JButton testerButton = new JButton("Check");
        final MTextField field = MTextField.digitPlusRangeControlledField();
        field.setPreferredSize(new Dimension(225, 30));
        field.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        field.addActionListener(e-> testerButton.doClick());

        testerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        testerButton.addActionListener(e->{
            resultLabel.setText("");
            if (!field.hasText()) {
                return;
            }
            try {
                final boolean isLeap = isLeapYear(Integer.parseInt(field.getText()));
                if (isLeap) {
                    resultLabel.setText("True");
                    resultLabel.setForeground(Color.BLUE);
                } else {
                    resultLabel.setText("False");
                    resultLabel.setForeground(Color.RED);
                }
            } catch (NumberFormatException f){
                Constants.reportOverflowError(LeapYearTester.this.getRootPane());
            }
        });

        final MPanel presentPanel = new MPanel(resultLabel);
        presentPanel.setPreferredSize(new Dimension(300, 75));
        presentPanel.setBackground(Color.WHITE);
        presentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(new MPanel(field), new MPanel(testerButton), presentPanel);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static boolean isLeapYear(int year){
        return year % 4 == 0;
    }

    public static LeapYearTester getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new LeapYearTester();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
