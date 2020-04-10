package math.arithmetic;

import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import ui.MathPad;
import waks.Arithmetic;
import waks.Constants;

import javax.swing.*;
import java.awt.*;

public class SumTerms extends JDialog {
    private static SumTerms runtimeInstance;


    private SumTerms(){
        this.setTitle("Summation Calculator");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final MTextField fromField = MTextField.digitPlusRangeControlledField();
        fromField.setPreferredSize(new Dimension(175, 30));
        final MTextField toField = MTextField.digitPlusRangeControlledField();
        toField.setPreferredSize(new Dimension(175, 30));

        final MPanel fieldsPanel = new MPanel(new FlowLayout(FlowLayout.CENTER));
        fieldsPanel.addAll(new JLabel("From"),fromField, Box.createHorizontalStrut(25),
                new JLabel("To"), toField);

        final MTextPane generatorPane = MTextPane.wantHtmlFormattedPane("", 12);
        generatorPane.setPreferredSize(new Dimension(350, 75));

        final JButton sumButton = new JButton("Compute Sum");
        sumButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sumButton.addActionListener(e-> {
            generatorPane.setText("");
            if (!toField.hasText()) {
                return;
            }
            try {
                final int a;
                if (fromField.hasText()) {
                    a = Integer.parseInt(fromField.getText());
                } else {
                    a = 0;
                }
                final int b = Integer.parseInt(toField.getText());
                if (a > b) {
                    generatorPane.setText("Illegal summation interval: from "+a+" to "+b);
                } else {
                    generatorPane.setText("<p style='text-align: center;'>"+Arithmetic.sumOfTerms(a, b)+"</p>");
                }
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(SumTerms.this.getRootPane());
            }
        });

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(sumButton), Box.createVerticalStrut(5), generatorPane);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static SumTerms getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new SumTerms();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
