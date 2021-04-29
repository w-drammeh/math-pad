package math.arithmetic;

import run.MathPad;
import ui.FontFactory;
import ui.MPanel;
import ui.MTextField;
import maha.Arithmetic;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class PrimeTester extends JDialog {
    private static PrimeTester runtimeInstance;


    private PrimeTester(){
        this.setTitle("Prime Tester");
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
                final boolean isPrime = isPrime(Integer.parseInt(field.getText()));
                if (isPrime) {
                    resultLabel.setText("True");
                    resultLabel.setForeground(Color.BLUE);
                } else {
                    resultLabel.setText("False");
                    resultLabel.setForeground(Color.RED);
                }
            } catch (NumberFormatException f){
                Constants.reportOverflowError(PrimeTester.this.getRootPane());
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

    public static boolean isPrime(int p){
        if (p == 2) {
            return true;
        } else if (p == 1 || Arithmetic.isEven(p)) {
            return false;
        }

        final int floorRoot = (int) Math.floor(Math.sqrt(p));
        for (int i = 3; i <= floorRoot; i++) {
            if (Arithmetic.isDivisible(p, i)) {
                return false;
            }
        }

        return true;
    }

    public static PrimeTester getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new PrimeTester();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
