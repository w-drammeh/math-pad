package ui;

import java.awt.*;

public class FontFactory {
    public static final String FONT_NAME = "Tahoma";


    public static Font createBoldFont(int size){
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    public static Font createPlainFont(int size){
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

    public static Font createItalicFont(int size){
        return new Font(FONT_NAME, Font.ITALIC, size);
    }

    public static Font createBoldItalic(int size){
        return new Font(FONT_NAME,Font.BOLD + Font.ITALIC,size);
    }

    public static Font preferredButtonsFont(){
        return createPlainFont(15);
    }

    public static Font preferredFieldsFont(){
        return createPlainFont(15);
    }

    public static Font preferredTextPanesFont() {
        return createPlainFont(15);
    }
}
