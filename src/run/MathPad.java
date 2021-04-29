package run;

import math.algebra.QuadraticEquation;
import math.arithmetic.*;
import math.finance.CompoundInterest;
import math.finance.SimpleInterest;
import math.others.LeapYearTester;
import ui.About;
import ui.FontFactory;
import ui.MPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MathPad {
    private static JFrame mainFrame;
    private static UIManager.LookAndFeelInfo[] LOOKS_INFO;
    private static final ArrayList<JButton> EXPANSION_HANDLERS = new ArrayList<>();
    public static final ArrayList<JDialog> ACTIVITY_DIALOGS = new ArrayList<>();
    private static final String SHOWING = "Showing", HIDDEN = "Hidden";


    public static void main(String[] args) {
        LOOKS_INFO = UIManager.getInstalledLookAndFeels();

        final MPanel contentPanel = new MPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.addAll(newMasterLayer("Simple Arithmetic", getArithmeticLayer()),
                newMasterLayer("Financial Mathematics", getMonetaryLayer()),
                newMasterLayer("Algebra", getAlgebraicLayer()),
                newMasterLayer("Plain Mensuration", getMensurationLayer()),
                newMasterLayer("Conversion Tools", getConvertersLayer()),
                newMasterLayer("Others", getOthersLayer()),
                Box.createRigidArea(new Dimension(475, 145)));//

        final JScrollPane contentScroll = new JScrollPane(contentPanel);
        contentScroll.setPreferredSize(new Dimension(500, contentPanel.getPreferredSize().height));

        mainFrame = new JFrame("MathPad");
        attachMenuBar();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setContentPane(contentScroll);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(()-> mainFrame.setVisible(true));
    }

    private static void attachMenuBar(){
        final JMenuItem expandMenuItem = new JMenuItem("Expand All");
        expandMenuItem.addActionListener(e-> setExpansionState(true));
        final JMenuItem collapseMenuItem = new JMenuItem("Collapse All");
        collapseMenuItem.addActionListener(e-> setExpansionState(false));
        final JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e-> SwingUtilities.invokeLater(()-> new About(mainFrame.getRootPane()).setVisible(true)));
        final JMenuItem exitMenuItem = new JMenuItem("Quit");
        exitMenuItem.setMnemonic(KeyEvent.VK_Q);
        exitMenuItem.addActionListener(e->{
            mainFrame.dispose();
            EventQueue.invokeLater(()-> System.exit(0));
        });
        final JMenu mathPadMenu = new JMenu("MathPad");
        mathPadMenu.setMnemonic(KeyEvent.VK_M);
        mathPadMenu.add(expandMenuItem);
        mathPadMenu.add(collapseMenuItem);
        mathPadMenu.add(aboutMenuItem);
        mathPadMenu.add(exitMenuItem);

        final JMenu lookMenuItem = new JMenu("Set Look & Feel");
        for (UIManager.LookAndFeelInfo lookInfo : LOOKS_INFO) {
            final JMenuItem lookItem = new JMenuItem(lookInfo.getName());
            lookItem.addActionListener(e->{
                try {
                    UIManager.setLookAndFeel(lookInfo.getClassName());
                    SwingUtilities.updateComponentTreeUI(mainFrame);
                    mainFrame.pack();
                    SwingUtilities.invokeLater(()->{
                        for (JDialog dialog : ACTIVITY_DIALOGS) {
                            SwingUtilities.updateComponentTreeUI(dialog);
                            dialog.pack();
                        }
                    });
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainFrame.getRootPane(), "Error occurred while setting the Look anf Feel to "+lookInfo.getName(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            lookMenuItem.add(lookItem);
        }
        final JMenu preferencesMenu = new JMenu("Preferences");
        preferencesMenu.setMnemonic(KeyEvent.VK_P);
        preferencesMenu.add(lookMenuItem);

        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(mathPadMenu);
        menuBar.add(preferencesMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    private static JButton newActionButton(String buttonText, ActionListener buttonAction){
        final JButton jButton = new JButton(buttonText);
        jButton.setFont(FontFactory.preferredButtonsFont());
        jButton.setFocusable(false);
        jButton.setContentAreaFilled(false);
        jButton.setBorderPainted(false);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.addActionListener(buttonAction);
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton.setContentAreaFilled(true);
                jButton.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton.setContentAreaFilled(false);
                jButton.setBorderPainted(false);
            }
        });

        return jButton;
    }

    private static MPanel newMasterLayer(String headerText, JComponent toggleComponent){
        final JButton toggleButton = new JButton(hiddenIcon());
        toggleButton.setFocusable(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleButton.addActionListener(e-> {
            if (toggleComponent.isShowing()) {
                toggleComponent.setVisible(false);
                toggleButton.setIcon(hiddenIcon());
                toggleButton.setToolTipText("Expand");
            } else {
                toggleComponent.setVisible(true);
                toggleButton.setIcon(showingIcon());
                toggleButton.setToolTipText("Collapse");
            }
        });
        EXPANSION_HANDLERS.add(toggleButton);

        final JLabel headerLabel = new JLabel(headerText);
        headerLabel.setFont(FontFactory.createBoldFont(20));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButton.doClick();
            }
        });

        final MPanel headerPanel = new MPanel(new BorderLayout());
        headerPanel.add(new MPanel(headerLabel), BorderLayout.WEST);
        headerPanel.add(new MPanel(new BorderLayout(), toggleButton), BorderLayout.EAST);

        final MPanel masterLayer = new MPanel(new BorderLayout());
        masterLayer.add(headerPanel, BorderLayout.NORTH);
        masterLayer.add(toggleComponent, BorderLayout.CENTER);

        toggleComponent.setVisible(false);

        return masterLayer;
    }

    private static Icon showingIcon(){
        try {
            final BufferedImage bufferedImage = ImageIO.read(MathPad.class.getResource("/ui/gryup.gif"));
            final ImageIcon imageIcon = new ImageIcon(bufferedImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            imageIcon.setDescription(SHOWING);
            return imageIcon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Icon hiddenIcon(){
        try {
            final BufferedImage bufferedImage = ImageIO.read(MathPad.class.getResource("/ui/grydown.gif"));
            final ImageIcon imageIcon = new ImageIcon(bufferedImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            imageIcon.setDescription(HIDDEN);
            return imageIcon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static MPanel getArithmeticLayer(){
        final ActionListener primeTesterAction = e-> SwingUtilities.invokeLater(()-> PrimeTester.getRuntimeInstance().setVisible(true));
        final ActionListener primeGeneratorAction = e-> SwingUtilities.invokeLater(()-> PrimeGenerator.getRuntimeInstance().setVisible(true));
        final ActionListener lcmComputerAction = e-> SwingUtilities.invokeLater(()-> LCMComputer.getRuntimeInstance().setVisible(true));
        final ActionListener gcdComputerAction = e-> SwingUtilities.invokeLater(()-> GCDComputer.getRuntimeInstance().setVisible(true));
        final ActionListener euclidAlgorithmAction = e-> SwingUtilities.invokeLater(()-> EuclidAlgorithm.getRuntimeInstance().setVisible(true));
        final ActionListener factorsGeneratorAction = e-> SwingUtilities.invokeLater(()-> FactorsGenerator.getRuntimeInstance().setVisible(true));
        final ActionListener firstSumTermsAction = e-> SwingUtilities.invokeLater(()-> SumTerms.getRuntimeInstance().setVisible(true));
        final ActionListener factorialComputerAction = e-> SwingUtilities.invokeLater(()-> FactorialComputer.getRuntimeInstance().setVisible(true));
        final ActionListener fibonacciSeqAction = e-> SwingUtilities.invokeLater(()-> Fibonacci.getRuntimeInstance().setVisible(true));
        final ActionListener eulerPhiAction = e-> SwingUtilities.invokeLater(()-> EulerPhi.getRuntimeInstance().setVisible(true));

        return newTogglePanel(newActionButton("Prime Number Tester", primeTesterAction),
                newActionButton("Prime Number Generator", primeGeneratorAction),
                newActionButton("Lcm Computer", lcmComputerAction),
                newActionButton("Gcd / Hcf computer", gcdComputerAction),
                newActionButton("Euclidean Algorithm for Gcd & Linear Propagation", euclidAlgorithmAction),
                newActionButton("Factors Generator", factorsGeneratorAction),
                newActionButton("Sum of first N terms", firstSumTermsAction),
                newActionButton("Factorial Computer", factorialComputerAction),
                newActionButton("Fibonacci Sequence", fibonacciSeqAction),
                newActionButton("The Euler-Phi Function", eulerPhiAction));
    }

    private static MPanel getMonetaryLayer(){
        final ActionListener simpleInterestCalculatorAction = e-> SwingUtilities.invokeLater(()-> SimpleInterest.getRuntimeInstance().setVisible(true));
        final ActionListener compundInterestCalculatorAction = e-> SwingUtilities.invokeLater(()-> CompoundInterest.getRuntimeInstance().setVisible(true));

        return newTogglePanel(newActionButton("Simple Interest Calculator", simpleInterestCalculatorAction),
                newActionButton("Compound Interest Calculator", compundInterestCalculatorAction));
    }

    private static MPanel getAlgebraicLayer(){
        final ActionListener quadraticSolutionAction = e-> SwingUtilities.invokeLater(()-> QuadraticEquation.getRuntimeInstance().setVisible(true));

        return newTogglePanel(newActionButton("Quadratic Equation", quadraticSolutionAction));
    }

    private static MPanel getMensurationLayer(){
        final ActionListener rectangleAction = null;
        final ActionListener rightTriangleAction = null;
        final ActionListener parallelogramAction = null;
        final ActionListener rhombusAction = null;
        final ActionListener trapeziumAction = null;
        final ActionListener circleAction = null;

        return newTogglePanel(newActionButton("Rectangle", rectangleAction),
                newActionButton("Right Triangle", rightTriangleAction),
                newActionButton("Parallelogram", parallelogramAction),
                newActionButton("Rhombus", rhombusAction),
                newActionButton("Trapezium", trapeziumAction),
                newActionButton("Circle", circleAction));
    }

    private static MPanel getConvertersLayer(){
        final ActionListener lengthAction = null;
        final ActionListener temperatureAction = null;

        return newTogglePanel(newActionButton("Length", lengthAction),
                newActionButton("Temperature", temperatureAction));
    }

    private static MPanel getOthersLayer(){
        final ActionListener leapDeterminerAction = e-> SwingUtilities.invokeLater(()-> LeapYearTester.getRuntimeInstance().setVisible(true));

        return newTogglePanel(newActionButton("Leap Year Tester", leapDeterminerAction));
    }

    private static MPanel newTogglePanel(JComponent... components){
        final MPanel panel = new MPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.addAll(components);

        return panel;
    }

    private static void setExpansionState(boolean expand){
        if (expand) {
            for (JButton button : EXPANSION_HANDLERS) {
                final ImageIcon imageIcon = (ImageIcon)button.getIcon();
                if (!imageIcon.getDescription().equals(SHOWING)) {
                    button.doClick();
                }
            }
        } else {
            for (JButton button : EXPANSION_HANDLERS) {
                final ImageIcon imageIcon = (ImageIcon)button.getIcon();
                if (!imageIcon.getDescription().equals(HIDDEN)) {
                    button.doClick();
                }
            }
        }
    }

    public static JFrame getRuntimeInstance(){
        return mainFrame;
    }

}
