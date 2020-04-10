package ui;

import javax.swing.*;
import java.awt.*;

public class About extends JDialog {


    public About(JRootPane rootPane){
        this.setTitle("About MathPad");
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
        this.setResizable(false);

        final JTabbedPane tabPane = new JTabbedPane();
        tabPane.setPreferredSize(new Dimension(550, 250));
        tabPane.addTab("Credits", getCreditsComponent());
        tabPane.addTab("Terms", getTermsComponent());
        tabPane.addTab("Documentation", getDocumentationComponent());
        ((JComponent)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(tabPane);
        this.pack();
        this.setLocationRelativeTo(rootPane);
    }

    private static JComponent getCreditsComponent(){
        final String creditsText = "This jar file was compiled and distributed by <b>Muhammed W. Drammeh</b><br>" +
                "The Univesity of The Gambia<br>" +
                "School of Arts & Sciences<br>" +
                "Department of Physical & Natural Sciences<br>" +
                "Mathematics Program (2016 - 2020)" +
                "<p>You must had received a copy of this product <i>free of charge</i>. And you are hereby permitted to " +
                "redistribute it, partially or as a whole, under the terms of the UTG-DSC Licence.";

        return MTextPane.wantHtmlFormattedPane(creditsText, 11);
    }

    private static JComponent getTermsComponent(){
        final String termsText = "<h3>Terms and Conditions</h3>" +
                "<p></p>";

        return MTextPane.wantHtmlFormattedPane(termsText, 11);
    }

    private static JComponent getDocumentationComponent(){
        final String docsText = "<h3>Documentation</h3>" +
                "<p></p>";

        return MTextPane.wantHtmlFormattedPane(docsText, 11);
    }

}
