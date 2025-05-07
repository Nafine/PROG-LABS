package se.ifmo.client.ui.util;

import javafx.scene.control.TextField;

public class TextFieldManager {
    public static void restrictToInteger(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) field.setText(oldVal);
        });
    }

    public static void restrictToFloat(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) field.setText(oldVal);
        });
    }

    public static Long parseLongOrNull(String s) {
        return s == null || s.isEmpty() ? null : Long.parseLong(s);
    }

    public static Integer parseIntOrNull(String s) {
        return s == null || s.isEmpty() ? null : Integer.parseInt(s);
    }

    public static Double parseDoubleOrNull(String s) {
        return s == null || s.isEmpty() ? null : Double.parseDouble(s);
    }

    public static Float parseFloatOrNull(String s) {
        return s == null || s.isEmpty() ? null : Float.parseFloat(s);
    }

}
