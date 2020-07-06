package math.finance;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import utg.Constants;

import javax.swing.*;
import java.awt.*;

public class CompoundInterest extends JDialog {
    private static CompoundInterest runtimeInstance;
    private MTextField principalField, rateField, timeField;
    private JButton solutionButton;
    private MTextPane outputPane;


    private CompoundInterest(){
        setTitle("Compound Interest Calculator");
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
                new Thread(()-> CompoundInterest.this.launchComputation(p, r, t)).start();
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(CompoundInterest.this.getRootPane());
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

    private void launchComputation(double initialPrinciple, double givenRate, int givenDuration){
        setInputState(false);
        double principle = initialPrinciple;
        double interest = (principle * givenRate)/100D;
        double amount = interest + principle;
        int y = 1;
        while (y < givenDuration){
            principle = amount;
            interest = (principle * givenRate)/100D;
            amount = interest + principle;
            y++;
        }
        final double compoundInterest = amount - initialPrinciple;
        outputPane.setText("P = D"+initialPrinciple+"<br>R = "+givenRate+"%<br>T = "+givenDuration+" year(s)" +
                "<p>\u2234 Interest (I) = D"+compoundInterest);
        setInputState(true);
    }

    public static CompoundInterest getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new CompoundInterest();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
