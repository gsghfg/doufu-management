package com.zf.product.doufu.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends BaseController implements Initializable {




    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        //新建一个菜单
//        Menu about = new Menu();
//        //新建一个标签
//        Label label = new Label("关于");
//        //给标签绑定鼠标点击事件
//        label.setOnMouseClicked(event -> {
//            String msg = "您点击了‘关于'菜单";
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("提示");
//            alert.setContentText(msg);
//            alert.show();
//        });
//        //将标签设置到menu的graphic属性当中
//        about.setGraphic(label);
//        menuBar.getMenus().add(about);
    }

    @FXML
    protected void action(Event event) {
        MenuItem item = (MenuItem) event.getTarget();
        String msg = "text:" + item.getText() + ", id:" + item.getId();
        try {
            skipView(item.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void exit() {

    }
}