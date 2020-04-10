package math.arithmetic;

import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import ui.MathPad;
import waks.Arithmetic;
import waks.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EulerPhi extends JDialog {
    private static EulerPhi runtimeInstance;


    private EulerPhi(){
        this.setTitle("Euler Phi Function");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        final MTextPane textPane = MTextPane.wantHtmlFormattedPane("", 12);

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
                final int n = Integer.parseInt(field.getText());
                if (n <= 1) {
                    textPane.setText("<p>This function is defined on natural numbers greater than 1</p>");
                } else {
                    new Thread(()-> {
                        field.setEditable(false);
                        computeButton.setEnabled(false);
                        final List<Integer> phiList = eulerPhiOf(n);
                        textPane.setText("<p style='text-align: center;'>e("+n+") = "+phiList.size()+"</p>" +
                                "<p>"+phiList+"</p>");
                        field.setEditable(true);
                        computeButton.setEnabled(true);
                    }).start();
                }
            } catch (NumberFormatException f){
                Constants.reportOverflowError(EulerPhi.this.getRootPane());
            }
        });

        final JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(new MPanel(field), new MPanel(computeButton), scrollPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static  List<Integer> eulerPhiOf(int n){
        if (n <= 1) {
            throw new IllegalArgumentException(n+" The Euler-Phi function is defined on natural numbers greater than 1");
        }

        final List<Integer> resultList = new ArrayList<>();
        if (PrimeTester.isPrime(n)) {
            for (int i = 1; i < n; i++) {
                resultList.add(i);
            }
        } else {
            for (int i = 1; i < n; i++) {
                if (Arithmetic.areCoPrimes(i, n)) {
                    resultList.add(i);
                }
            }
        }
        return resultList;
    }

    public static EulerPhi getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new EulerPhi();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
