package math.arithmetic;

import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import ui.MathPad;
import waks.Constants;

import javax.swing.*;
import java.awt.*;

public class FactorialComputer extends JDialog {
    private static FactorialComputer runtimeInstance;


    private FactorialComputer(){
        this.setTitle("Factorial Computer");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        final MTextPane textPane = MTextPane.wantHtmlFormattedPane("", 12);
        textPane.setPreferredSize(new Dimension(425, 75));

        final JButton testerButton = new JButton("Compute Factorial");
        final MTextField field = MTextField.digitPlusRangeControlledField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        field.addActionListener(e-> testerButton.doClick());

        testerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        testerButton.addActionListener(e->{
            textPane.setText("");
            if (!field.hasText()) {
                return;
            }
            try {
                final long n = Long.parseLong(field.getText());
                if (n > 20) {
                    textPane.setText("<p>Number Format Error: cannot compute factorial<p>");
                } else {
                    new Thread(()->{
                        field.setEditable(false);
                        testerButton.setEnabled(false);
                        textPane.setText("<p style='text-align: center;'>"+factorialOf(n)+"</p>");
                        field.setEditable(true);
                        testerButton.setEnabled(true);
                    }).start();
                }
            } catch (NumberFormatException f){
                Constants.reportOverflowError(FactorialComputer.this.getRootPane());
            }
        });

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(new MPanel(field), new MPanel(testerButton), textPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static long factorialOf(long n){
        if (n < 0) {
            throw new IllegalArgumentException("Factorial of a negative integer "+n);
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorialOf(n - 1);
    }

    public static FactorialComputer getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new FactorialComputer();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
