package math.algebra;

import run.MathPad;
import ui.FontFactory;
import ui.MPanel;
import ui.MTextField;
import ui.MTextPane;
import maha.Constants;

import javax.swing.*;
import java.awt.*;

public class QuadraticEquation extends JDialog {
    private static QuadraticEquation instance;
    private MTextField aField, bField, cField;
    private JButton solutionButton;
    private MTextPane outputPane;


    private QuadraticEquation(){
        setTitle("Quadratic Equation");
        setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        aField = newInputField();
        bField = newInputField();
        cField = newInputField();
        final MPanel fieldsPanel = new MPanel(){
            @Override
            public Component add(Component comp) {
                if (comp instanceof JLabel) {
                    comp.setFont(FontFactory.createBoldFont(15));
                }
                return super.add(comp);
            }
        };
        fieldsPanel.addAll(new JLabel("a"), aField, Box.createHorizontalStrut(10),
                new JLabel("b"), bField, Box.createHorizontalStrut(10),
                new JLabel("c"), cField);

        solutionButton = new JButton("Evaluate");
        solutionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        solutionButton.addActionListener(e->{
            outputPane.setText("");
            if (!(aField.hasText() && bField.hasText() && cField.hasText())) {
                return;
            }
            try {
                Integer.parseInt(aField.getText());
                Integer.parseInt(bField.getText());
                Integer.parseInt(bField.getText());
                new Thread(this::launchSolution).start();
            } catch (NumberFormatException f) {
                Constants.reportOverflowError(QuadraticEquation.this.getRootPane());
            }
        });

        outputPane = MTextPane.wantHtmlFormattedPane("", 12);
        outputPane.setPreferredSize(new Dimension(350, 150));

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(fieldsPanel, new MPanel(solutionButton), outputPane);
        setContentPane(contentPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(MathPad.getRuntimeInstance().getRootPane());
    }

    private MTextField newInputField(){
        final MTextField inputField = MTextField.digitPlusRangeControlledField(Constants.INPUT_LENGTH, "-");
        inputField.setPreferredSize(new Dimension(100, 30));
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));

        return inputField;
    }

    private void setInputState(boolean interactive){
        aField.setEnabled(interactive);
        bField.setEnabled(interactive);
        cField.setEnabled(interactive);
        solutionButton.setEnabled(interactive);
    }

    private String getEquation(){
        final int a = Integer.parseInt(aField.getText());
        final String aString = a == 1 ? "" : aField.getText();
        final int b = Integer.parseInt(bField.getText());
        final String bString = (b >= 0 ? "+ " : "- ")+Math.abs(b);
        final int c = Integer.parseInt(cField.getText());
        final String cString = (c >= 0 ? "+ " : "- ")+Math.abs(c);

        return "f(x) = "+aString+"x<sup>2</sup> "+(Math.abs(b) == 1 ? "+ " : bString)+"x "+cString;
    }

    private void launchSolution(){
        setInputState(false);
        final int a = Integer.parseInt(aField.getText());
        if (a == 0) {
            outputPane.setText(getEquation() +
                    "<p>This equation is not quadratic</p>");
            setInputState(true);
            return;
        }
        final int b = Integer.parseInt(bField.getText());
        final int c = Integer.parseInt(cField.getText());
        final int discriminant = b*b - 4*a*c;
        final double denominator = 2*a;
        if (discriminant < 0) {
            outputPane.setText(getEquation() +
                    "<p>The quadratic equation has no real roots</p>");
        } else if (discriminant == 0) {
            final double repeatedRoot = -b / denominator;
            outputPane.setText(getEquation()+"" +
                    "<p>x<sub>1</sub> = "+repeatedRoot+"<br>" +
                    "x<sub>2</sub> = "+repeatedRoot+"</p>" +
                            "<p>Repeated roots</p>");
        } else {
            final double root1 = ((-b + Math.sqrt(discriminant)) / denominator);
            final double root2 = ((-b - Math.sqrt(discriminant)) / denominator);
            outputPane.setText(getEquation()+"" +
                    "<p>x<sub>1</sub> = "+root1+"<br>" +
                    "x<sub>2</sub> = "+root2+"</p>");
        }
        setInputState(true);
    }

    public static QuadraticEquation getRuntimeInstance(){
        if (instance == null) {
            instance = new QuadraticEquation();
            MathPad.ACTIVITY_DIALOGS.add(instance);
        }

        return instance;
    }

}
