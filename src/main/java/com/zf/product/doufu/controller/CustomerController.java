package com.zf.product.doufu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.excel.SheetWriter;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.OrderContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController extends BaseController implements Initializable {
    @FXML
    private TableColumn<Customer, String> index;
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> address;

    @FXML
    private TableColumn<Customer, String> operate;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    @FXML
    private TableView<Customer> customerTableView;

    public ObservableList<Customer> tableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCustomerTable();
    }

    private void initCustomerTable() {
        tableList.clear();
        List<Customer> customerList = SheetReader.readCustomer();
        ObservableList<TableColumn<Customer, ?>> observableList = customerTableView
                .getColumns();
        // name
        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory("name"));
        // phone
        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory("phone"));
        // address
        observableList.get(3).setCellValueFactory(
                new PropertyValueFactory("address"));
        customerList.forEach(customer -> {
            tableList.add(customer);
        });
        //index
        index.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> col) {
                TableCell<Customer, String> cell = new TableCell<Customer, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);

                        if (!empty) {
                            int rowIndex = this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;
            }
        });
        //配置可编辑
        customerTableView.setEditable(true);
        //name
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                (TableColumn.CellEditEvent<Customer, String> t) -> {
                    ((Customer) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                    updateCustomer((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //phone
        phone.setCellFactory(TextFieldTableCell.forTableColumn());
        phone.setOnEditCommit(
                (TableColumn.CellEditEvent<Customer, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPhone(t.getNewValue());
                    updateCustomer(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //address
        address.setCellFactory(TextFieldTableCell.forTableColumn());
        address.setOnEditCommit(
                (TableColumn.CellEditEvent<Customer, String> t) -> {
                    ((Customer) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setAddress(t.getNewValue());
                    updateCustomer((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //operate
        operate.setCellFactory((col) -> {
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
                        SheetWriter.deleteRow(ExcelConstants.CUSTOMER_SHEET_NAME, deleteCustomer.getRowNumber());
                        initCustomerTable();
                        //按钮事件自己添加
                        System.out.println("deleteCustomer:" + JSONObject.toJSONString(deleteCustomer));

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
        customerTableView.setItems(tableList);
    }


    private void updateCustomer(Customer customer) {
        SheetWriter.updateCustomer(Arrays.asList(new Customer[]{customer}));
        initCustomerTable();
    }


    public void addCustomer() {
        Customer customer = new Customer();
        customer.setName(nameField.getText());
        customer.setPhone(phoneField.getText());
        customer.setAddress(addressField.getText());
        SheetWriter.appendCustomer(customer);
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        initCustomerTable();
    }
}
