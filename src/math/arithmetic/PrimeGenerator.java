package math.arithmetic;

import run.MathPad;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import maha.Arithmetic;
import maha.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator extends JDialog {
    private static PrimeGenerator runtimeInstance;
    

    private PrimeGenerator(){
        this.setTitle("Prime Generator");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        final MTextField fromField = MTextField.digitPlusRangeControlledField();
        fromField.setPreferredSize(new Dimension(200, 30));
        final MTextField toField = MTextField.digitPlusRangeControlledField();
        toField.setPreferredSize(new Dimension(200, 30));

        final MPanel fieldsPanel = new MPanel(new FlowLayout(FlowLayout.CENTER));
        fieldsPanel.addAll(new JLabel("From"),fromField, Box.createHorizontalStrut(25),
                new JLabel("To"), toField);
        
        final MTextPane generatorPane = MTextPane.wantHtmlFormattedPane("", 12);

        final JButton generatorButton = new JButton("Generate Primes");
        generatorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generatorButton.addActionListener(e-> {
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
                    generatorPane.setText("Unordered interval [" + a + ", " + b + "]");
                } else {
                    generatorPane.setText(getPrimes(a, b).toString());
                }
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(PrimeGenerator.this.getRootPane());
            }
        });
        
        final JScrollPane solutionScroll = new JScrollPane(generatorPane);
        solutionScroll.setPreferredSize(new Dimension(450, 300));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(generatorButton), Box.createVerticalStrut(10), solutionScroll);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static List<Integer> getPrimes(int from, int to){
        if (from > to) {
            throw new IllegalArgumentException("Unordered interval (" + from + ", " + to + ")");
        }

        final List<Integer> list = new ArrayList<>();
        if (from <= 2 && to >= 2) {
            list.add(2);
        }
        for (int i = Arithmetic.isOdd(from) ? from : from + 1; i <= to; i += 2) {
            if (PrimeTester.isPrime(i)) {
                list.add(i);
            }
        }

        return list;
    }

    public static PrimeGenerator getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new PrimeGenerator();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
