package ui;

import javax.swing.*;

public class MTextPane extends JTextPane {


    public MTextPane(String type, String text){
        super();
        this.setContentType(type);
        this.setText(text);
        this.setEditable(false);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setFont(FontFactory.preferredTextPanesFont());
    }

    public static MTextPane wantHtmlFormattedPane(String htmlText, int fontSize){
        final String formattedText = "<!DOCTYPE html><html><head><style> body {font-size: "+fontSize+"px; font-family: Tahoma;} </style></head><body>" +
                htmlText +
                "</body></html>";

        return new MTextPane("text/html", formattedText);
    }

}
