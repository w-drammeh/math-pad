package math.arithmetic;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import maha.Arithmetic;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class LCMComputer extends JDialog {
    private static LCMComputer runtimeInstance;


    public LCMComputer(){
        this.setTitle("LCM Computer");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final MTextField field1 = MTextField.digitPlusRangeControlledField();
        field1.setPreferredSize(new Dimension(175, 30));
        final MTextField field2 = MTextField.digitPlusRangeControlledField();
        field2.setPreferredSize(new Dimension(175, 30));

        final MPanel fieldsPanel = new MPanel(new FlowLayout(FlowLayout.CENTER));
        fieldsPanel.addAll(field1, Box.createHorizontalStrut(25), field2);


        final MTextPane lcmPane = MTextPane.wantHtmlFormattedPane("", 12);
        lcmPane.setPreferredSize(new Dimension(350, 100));

        final JButton generatorButton = new JButton("Compute Lcm");
        generatorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generatorButton.addActionListener(e-> {
            lcmPane.setText("");
            if (!(field1.hasText() && field2.hasText())) {
                return;
            }

            try {
                final int a = Integer.parseInt(field1.getText());
                final int b = Integer.parseInt(field2.getText());
                if (a == 0 || b == 0) {
                    lcmPane.setText("<p>Error: Lcm of a zero.</p>");
                } else {
                    lcmPane.setText("<p style='text-align: center;'>Lcm("+a+", "+b+") = "+lcmOf(a, b)+"</p>");
                }
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(LCMComputer.this.getRootPane());
            }
        });

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(generatorButton), lcmPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static int lcmOf(int num1, int num2){
        if (num1 == 0 || num2 == 0) {
            throw new IllegalArgumentException("Lcm of zero and a number");
        }

        int a = Math.min(num1, num2);
        int b = Math.max(num1, num2);
        int t = b;
        while (!Arithmetic.isDivisible(t, a)) {
            t += b;
        }

        return t;
    }

    public static LCMComputer getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new LCMComputer();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
