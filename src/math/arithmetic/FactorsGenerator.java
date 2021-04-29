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
import java.util.Iterator;
import java.util.List;

public class FactorsGenerator extends JDialog {
    private static FactorsGenerator runtimeInstance;


    private FactorsGenerator(){
        this.setTitle("Factors Generator");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        final MTextField field = MTextField.digitPlusRangeControlledField();
        field.setPreferredSize(new Dimension(175, 30));

        final MTextPane generatorPane = MTextPane.wantHtmlFormattedPane("", 12);

        final JButton allGeneratorButton = new JButton("Generate all Factors");
        allGeneratorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        allGeneratorButton.addActionListener(e-> {
            generatorPane.setText("");
            if (!field.hasText()) {
                return;
            }
            try {
                final int n = Integer.parseInt(field.getText());
                if (n == 0) {
                    generatorPane.setText("0 is not allowed in this computation");
                } else {
                    generatorPane.setText(allFactorsOf(n).toString());
                }
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(FactorsGenerator.this.getRootPane());
            }
        });

        final JButton primesOnlyGeneratorButton = new JButton("Generate Primes Only");
        primesOnlyGeneratorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        primesOnlyGeneratorButton.addActionListener(e-> {
            generatorPane.setText("");
            if (!field.hasText()) {
                return;
            }

            try {
                final int n = Integer.parseInt(field.getText());
                if (n == 0) {
                    generatorPane.setText("0 is not allowed in this computation");
                } else {
                    final List<Integer> primeFactors = primeFactorsOf(n);
                    if (primeFactors.size() >= 2) {
                        generatorPane.setText(primeFactors.toString() +
                                "<p>\u2234 "+ presentedPrimeFactors(n, primeFactors)+"</p>");
                    } else {
                        generatorPane.setText(primeFactors.toString());
                    }
                }
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(FactorsGenerator.this.getRootPane());
            }
        });

        final MPanel buttonsPanel = new MPanel(new BorderLayout());
        buttonsPanel.add(new MPanel(allGeneratorButton), BorderLayout.WEST);
        buttonsPanel.add(new MPanel(primesOnlyGeneratorButton), BorderLayout.EAST);

        final JScrollPane solutionScroll = new JScrollPane(generatorPane);
        solutionScroll.setPreferredSize(new Dimension(450, 175));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(new MPanel(field), buttonsPanel, Box.createVerticalStrut(5), solutionScroll);
        this.setContentPane(contentPanel);
        this.pack();
        this.setMinimumSize(getPreferredSize());
        this.setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    public static List<Integer> allFactorsOf(int n){
        n = Math.abs(n);
        final java.util.List<Integer> factors = new ArrayList<>();

        if (PrimeTester.isPrime(n)) {
            factors.add(1);
            factors.add(n);
        } else {
            for (int i = 1; i <= n; i++) {
                if (Arithmetic.isDivisible(n, i)) {
                    factors.add(i);
                }
            }
        }
        return factors;
    }

    public static List<Integer> primeFactorsOf(int n){
        if (n <= 0) {
            throw new IllegalArgumentException("Prime factorization on a non-natural integer "+n);
        }
        final java.util.List<Integer> primeFactors = new ArrayList<>();
        if (PrimeTester.isPrime(n)) {
            primeFactors.add(n);
            return primeFactors;
        }

        final List<Integer> possiblePrimes = PrimeGenerator.getPrimes(0, n);
        int flexing = n;
        for (int i = 0; i < possiblePrimes.size(); i++) {
            if (Arithmetic.isDivisible(flexing, possiblePrimes.get(i))) {
                primeFactors.add(possiblePrimes.get(i));
                flexing /= possiblePrimes.get(i);
                i--;
            }
        }
        return primeFactors;
    }

    private static String presentedPrimeFactors(int n, List<Integer> primes){
        if (primes.size() == 0) {
            return "";
        }

        final Iterator<Integer> primesIterator = primes.iterator();
        final StringBuilder builder = new StringBuilder(n+" = "+primesIterator.next());
        while (primesIterator.hasNext()){
            builder.append(" x ").append(primesIterator.next());
        }
        return builder.toString();
    }

    public static FactorsGenerator getRuntimeInstance(){
        if (runtimeInstance == null) {
            runtimeInstance = new FactorsGenerator();
            MathPad.ACTIVITY_DIALOGS.add(runtimeInstance);
        }
        return runtimeInstance;
    }

}
