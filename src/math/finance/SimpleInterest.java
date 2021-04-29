package math.finance;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class SimpleInterest extends JDialog {
    private static SimpleInterest runtimeInstance;
    private MTextField principalField, rateField, timeField;
    private JButton solutionButton;
    private MTextPane outputPane;


    private SimpleInterest(){
        setTitle("Simple Interest Calculator");
        setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        principalField = newInputField(".");
        rateField = newInputField(".");
        timeField = newInputField();
        final MPanel fieldsPanel = new MPanel();
        fieldsPanel.addAll(new JLabel("Principal (D)"), principalField, Box.createHorizontalStrut(10),
                new JLabel("Rate (%)"), rateField, Box.createHorizontalStrut(10),
                new JLabel("Time (years)"), timeField);

        solutionButton = new JButton("Calculate Interest");
        solutionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        solutionButton.addActionListener(e->{
            outputPane.setText("");
            if (!(principalField.hasText() && rateField.hasText() && timeField.hasText())) {
                return;
            }
            try {
                final double p = Double.parseDouble(principalField.getText());
                final double r = Double.parseDouble(rateField.getText());
                final int t = Integer.parseInt(timeField.getText());
                new Thread(()-> SimpleInterest.this.launchComputation(p, r, t)).start();
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(SimpleInterest.this.getRootPane());
            }
        });

        outputPane = MTextPane.wantHtmlFormattedPane("", 12);
        outputPane.setPreferredSize(new Dimension(400, 165));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(solutionButton), outputPane);
        setContentPane(contentPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    private MTextField newInputField(String... allowed){
        final MTextField inputField = MTextField.digitPlusRangeControlledField(Constants.INPUT_LENGTH, allowed);
        inputField.setPreferredSize(new Dimension(100, 30));
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        return inputField;
    }

    private void setInputState(boolean interactive){
        principalField.setEnabled(interactive);
        rateField.setEnabled(interactive);
        timeField.setEnabled(interactive);
        solutionButton.setEnabled(interactive);
    }

    private void launchComputation(double p, double r, int t){
        setInputState(false);
        final double i = (p * r * t)/100D;
        final double amount = i + p;
        outputPane.setText("P = D"+p+"<br>R = "+r+"%<br>T = "+t+" year(s)" +
                "<p>\u2234 Interest (I) = D"+i+"<br>" +
                "Amount (A) = D"+amount+"</p>");
        setInputState(true);
    }

    public static SimpleInterest getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new SimpleInterest();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
