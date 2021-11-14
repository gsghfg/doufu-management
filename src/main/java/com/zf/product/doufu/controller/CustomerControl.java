package com.zf.product.doufu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.excel.ReadSheet;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.utils.ViewUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerControl implements Initializable {
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> address;

    @FXML
    private TableColumn<Customer, String> edit;
    @FXML
    private TableColumn<Customer, String> delete;

    @FXML
    private Button addCustomerBt;
    @FXML
    private Label labelText;
    @FXML
    private TableView<Customer> customerTable;

    public ObservableList<Customer> tableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableList.clear();
        List<Customer> customerList = ReadSheet.readCustomer();
        ObservableList<TableColumn<Customer, ?>> observableList = customerTable
                .getColumns();
        // name
        observableList.get(0).setCellValueFactory(
                new PropertyValueFactory("name"));
        // phone
        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory("phone"));
        // address
        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory("address"));
        customerList.forEach(customer -> {
            tableList.add(customer);
        });
        edit.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<Customer, String> cell = new TableCell<Customer, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("编辑");
                    button1.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        Customer editCustomer = tableList.get(getIndex());
                        System.out.println("editCustomer:"+ JSONObject.toJSONString(editCustomer));
                        //按钮事件自己添加

                    });

                    if (empty) {
                        //如果此列为空默认不添加元素
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(button1);
                    }
                }
            };
            cell.setStyle("-fx-alignment: center");
            return cell;
        });
        delete.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<Customer, String> cell = new TableCell<Customer, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("删除");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        Customer deleteCustomer = tableList.get(getIndex());
                        //按钮事件自己添加
                        System.out.println("deleteCustomer:"+ JSONObject.toJSONString(deleteCustomer));

                    });

                    if (empty) {
                        //如果此列为空默认不添加元素
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(button1);
                    }
                }
            };
            cell.setStyle("-fx-alignment: center");
            return cell;
        });
        customerTable.setItems(tableList);
    }

    public void addCustomer() {
        ViewUtil.showDialog(" add customer");
    }
}
