package math.arithmetic;

import ui.FontFactory;
import ui.MPanel;
import ui.MTextField;
import ui.MathPad;
import waks.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class EuclidAlgorithm extends JDialog {
    private MTextField field1, field2;
    private JButton computeButton, clearButton;
    private JCheckBox stepBox;
    private MPanel solutionPanel;
    private JScrollPane solutionScroll;
    private static EuclidAlgorithm instance;


    private EuclidAlgorithm(){
        setTitle("Euclidean Algorithm");
        setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        field1 = newNumberField();
        field2 = newNumberField();

        final MPanel fieldsPanel = new MPanel(new FlowLayout(FlowLayout.CENTER));
        fieldsPanel.addAll(field1, Box.createHorizontalStrut(15), field2);

        computeButton = new JButton("Compute"){
            @Override
            public void setEnabled(boolean b) {
                super.setEnabled(b);
                setCursor(b ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : null);
            }
        };
        computeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        computeButton.addActionListener(e-> {
            solutionPanel.removeAll();
            if (!(field1.hasText() && field2.hasText())) {
                solutionPanel.repaint();
                solutionPanel.revalidate();
                return;
            }
            try {
                final int a = Integer.parseInt(field1.getText());
                final int b = Integer.parseInt(field2.getText());
                new Thread(() -> launchAlgorithm(a, b)).start();
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(EuclidAlgorithm.this.getRootPane());
            }
        });

        stepBox = new JCheckBox("Compute with breaks");
        stepBox.setFont(new Font(FontFactory.FONT_NAME, Font.PLAIN, 15));

        clearButton = new JButton("Clear");
        clearButton.setFocusable(false);
        clearButton.addActionListener(e->{
            solutionPanel.removeAll();
            solutionPanel.repaint();
            solutionPanel.revalidate();
            field1.setText(null);
            field2.setText(null);
        });

        final MPanel buttonsPanel = new MPanel(new BorderLayout());
        buttonsPanel.add(newPanel(new FlowLayout(), computeButton), BorderLayout.WEST);
        buttonsPanel.add(newPanel(new FlowLayout(), stepBox), BorderLayout.CENTER);
        buttonsPanel.add(newPanel(new FlowLayout(), clearButton), BorderLayout.EAST);

        solutionPanel = new MPanel();
        solutionPanel.setBackground(Color.WHITE);
        solutionPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 10, 0));
        solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));

        solutionScroll = new JScrollPane(solutionPanel);
        solutionScroll.setPreferredSize(new Dimension(600, 425));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, buttonsPanel, Box.createVerticalStrut(5), solutionScroll);
        setContentPane(contentPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    private MTextField newNumberField(){
        final MTextField numberField = MTextField.digitPlusRangeControlledField();
        numberField.setPreferredSize(new Dimension(175, 30));
        numberField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));

        return numberField;
    }

    private MPanel newPanel(LayoutManager layoutManager, Component c){
        final MPanel mPanel = new MPanel(layoutManager);
        mPanel.add(c);
        return mPanel;
    }

    private void append(String text){
        final boolean isSolution = text.contains("\u2234");
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FontFactory.FONT_NAME, Font.PLAIN, 16));
        label.setForeground(isSolution ? Color.BLUE : null);
        solutionPanel.add(label);
        solutionPanel.repaint();
        solutionPanel.revalidate();
        if (stepBox.isSelected()) {
            try {
                Thread.sleep(Constants.SLEEP_LENGTH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(()-> solutionScroll.getVerticalScrollBar().setValue(solutionScroll.getVerticalScrollBar().getMaximum()));
    }

    private void setButtonsState(boolean newState){
        computeButton.setEnabled(newState);
        clearButton.setEnabled(newState);
        stepBox.setEnabled(newState);
    }

    private void launchAlgorithm(int a, int b){
        if (a == 0 || b == 0) {
            append("0 is not allowed in this computation.");
            setButtonsState(true);
            return;
        }
        setButtonsState(false);
        append("Computing the gcd of "+a+" and "+b+" using Euclidean Algorithm.");

        class CombinationEquation {//n = d(q) + r
            int number, divisor, quot, rem;

            private CombinationEquation(int n, int d, int q, int r){
                number = n;
                divisor = d;
                quot = q;
                rem = r;
            }

            private String getValue(){//n - d(q)
                return number+" - "+divisor+"("+quot+")";
            }
        }

        class ExtendedCombination{//n(#) - d(#)
            int val1, arg1, val2, arg2;

            private ExtendedCombination(int v1, int a1, int v2, int a2){
                val1 = v1;
                arg1 = a1;
                val2 = v2;
                arg2 = a2;
            }

            private String rawFormat(){
                return val1+"("+arg1+")"+" - "+val2+"("+arg2+")";
            }

            private String substitutedFormat(CombinationEquation e){
                if (e.rem == val1) {
                    return "["+e.getValue()+"]"+"("+arg1+") - "+val2+"("+arg2+")";
                } else {
                    return val1+"("+arg1+")"+" - ["+e.getValue()+"]("+arg2+")";
                }
            }

            private String expandedFormat(CombinationEquation e){//To follow substitutedFormat()
                if (e.rem == val1) {
                    return e.number+"("+arg1+")"+" - "+e.divisor+"("+e.quot+" x "+arg1+") - "+val2+"("+arg2+")";
                } else {
                    return val1+"("+arg1+") - "+e.number+"("+arg2+") + "+e.divisor+"("+e.quot+" x "+arg2+")";
                }
            }

            private ExtendedCombination extend(CombinationEquation e){
                if (e.rem == val1) {
                    int v1 = e.number;
                    int a1 = arg1;
                    int v2 = val2;
                    int a2 = (e.quot * arg1) + arg2;
                    return new ExtendedCombination(v1, a1, v2, a2);
                } else {
                    int v1 = val1;
                    int a1 = (e.quot * arg2) + arg1;
                    int v2 = e.number;
                    int a2 = arg2;
                    return new ExtendedCombination(v1, a1, v2, a2);
                }
            }
        }

        int number = Math.max(a, b);
        int divisor = Math.min(a, b);
        int quot = number / divisor;
        int rem = number - (divisor * quot);
        final ArrayList<CombinationEquation> list = new ArrayList<>();
        list.add(new CombinationEquation(number, divisor, quot, rem));
        while (rem != 0) {
            append(number+" = "+divisor+"("+quot+") + "+rem);
            list.add(new CombinationEquation(number, divisor, quot, rem));
            number = divisor;
            divisor = rem;
            quot = number / divisor;
            rem = number - (divisor * quot);
        }
        append(number+" = "+divisor+"("+quot+") + "+rem);
        final int GCD = divisor;
        append("\u2234 gcd("+a+", "+b+") = "+GCD);

        append("Now reversing to find the linear propagation of the gcd.");
        if (list.size() == 1) {
            append(GCD+" = "+number+"(1) - "+divisor+"("+(--quot)+")");
            setButtonsState(true);
            return;
        }
        Collections.reverse(list);
        list.remove(list.size() - 1);
        Iterator<CombinationEquation> listIterator = list.iterator();
        CombinationEquation equation = listIterator.next();
        ExtendedCombination combination = new ExtendedCombination(equation.number, 1, equation.divisor, equation.quot);
        append(GCD+" = "+combination.rawFormat());
        while (listIterator.hasNext() && combination.val1 != Math.max(a, b) && combination.val2 != Math.min(a, b)) {
            equation = listIterator.next();
            final ExtendedCombination nextCombination = combination.extend(equation);
            append(GCD+" = "+combination.substitutedFormat(equation));
            append(GCD+" = "+combination.expandedFormat(equation));
            append(GCD+" = "+nextCombination.rawFormat());
            combination = nextCombination;
        }
        append("\u2234 "+GCD+" = "+combination.rawFormat());
        setButtonsState(true);
    }

    public static EuclidAlgorithm getRuntimeInstance(){
        if (instance == null) {
            instance = new EuclidAlgorithm();
            MathPad.ACTIVITY_DIALOGS.add(instance);
        }
        return instance;
    }

}
