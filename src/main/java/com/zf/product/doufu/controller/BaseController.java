package com.zf.product.doufu.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BaseController {
    @FXML
    private MenuBar menuBar;

    @FXML
    private AnchorPane mainPane;

    protected void skipView(String pagePath) throws IOException {
        ObservableList<Node> children = mainPane.getChildren();
        children.clear();
        children.add(FXMLLoader.load(getClass().getResource(pagePath)));
    }
}
