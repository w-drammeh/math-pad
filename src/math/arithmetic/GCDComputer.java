package math.arithmetic;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import utg.Arithmetic;
import utg.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GCDComputer extends JDialog {
    private static GCDComputer runtimeInstance;


    public GCDComputer(){
        this.setTitle("GCD Computer");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final MTextField field1 = MTextField.digitPlusRangeControlledField();
        field1.setPreferredSize(new Dimension(175, 30));
        final MTextField field2 = MTextField.digitPlusRangeControlledField();
        field2.setPreferredSize(new Dimension(175, 30));

        final MPanel fieldsPanel = new MPanel(new FlowLayout(FlowLayout.CENTER));
        fieldsPanel.addAll(field1, Box.createHorizontalStrut(25), field2);

        final MTextPane gcdPane = MTextPane.wantHtmlFormattedPane("", 12);
        gcdPane.setPreferredSize(new Dimension(350, 100));

        final JButton generatorButton = new JButton("Compute Gcd");
        generatorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generatorButton.addActionListener(e-> {
            gcdPane.setText("");
            if (!(field1.hasText() && field2.hasText())) {
                return;
            }

            try {
                final int a = Integer.parseInt(field1.getText());
                final int b = Integer.parseInt(field2.getText());
                gcdPane.setText("<p style='text-align: center;'>Gcd("+a+", "+b+") = "+gcdOf(a, b)+"</p>");
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(GCDComputer.this.getRootPane());
            }
        });

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(generatorButton), gcdPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static int gcdOf(int n1, int n2){
        if (n1 == 0) {
            return n2;
        } else if (n2 == 0) {
            return n1;
        } else {
            final List<Integer> allMinimunFactors = FactorsGenerator.allFactorsOf(Math.min(n1,n2));
            int gcd = 1;
            for (int i = allMinimunFactors.size() - 1; i >= 0; i--) {
                if (Arithmetic.isDivisible(Math.max(n1, n2), allMinimunFactors.get(i))) {
                    gcd = allMinimunFactors.get(i);
                    break;
                }
            }
            return gcd;
        }
    }

    public static GCDComputer getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new GCDComputer();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
