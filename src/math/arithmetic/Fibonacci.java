package math.arithmetic;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class Fibonacci extends JDialog {
    private static Fibonacci runtimeInstance;


    private Fibonacci(){
        this.setTitle("Fibonacci Sequence");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        final MTextPane textPane = MTextPane.wantHtmlFormattedPane("", 12);
        textPane.setPreferredSize(new Dimension(425, 75));

        final JButton computeButton = new JButton("Compute Value");
        final MTextField field = MTextField.digitPlusRangeControlledField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        field.addActionListener(e-> computeButton.doClick());

        computeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        computeButton.addActionListener(e->{
            textPane.setText("");
            if (!field.hasText()) {
                return;
            }
            try {
                final long n = Long.parseLong(field.getText());
                new Thread(()-> {
                    field.setEditable(false);
                    computeButton.setEnabled(false);
                    textPane.setText("<p style='text-align: center;'>f("+n+") = "+fibonacciOf(n)+"</p>");
                    field.setEditable(true);
                    computeButton.setEnabled(true);
                }).start();
            } catch (NumberFormatException f){
                Constants.reportOverflowError(Fibonacci.this.getRootPane());
            }
        });

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(new MPanel(field), new MPanel(computeButton), textPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static long fibonacciOf(long n){
        if (n < 0) {
            throw new IllegalArgumentException(n+" is not in the domain of the Fibonacci Sequence");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return fibonacciOf(n - 1) + fibonacciOf(n - 2);
    }

    public static Fibonacci getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new Fibonacci();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
