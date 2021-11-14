package com.zf.product.doufu.utils;

import javafx.scene.control.Alert;

public class ViewUtil {

    public static void showDialog(String tip) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("dialog");
        alert.setHeaderText(null);
        alert.setContentText(tip);
        alert.showAndWait();
    }
}
