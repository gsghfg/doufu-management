package com.zf.product.doufu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.excel.SheetWriter;
import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController extends BaseController implements Initializable {
    @FXML
    private TableColumn<Product, String> index;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Double> price;

    @FXML
    private TableColumn<Product, String> operate;

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;

    @FXML
    private TableView<Product> productTableView;

    public ObservableList<Product> tableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProductTable();
    }

    private void initProductTable() {
        tableList.clear();
        List<Product> productList = SheetReader.readProduct();
        ObservableList<TableColumn<Product, ?>> observableList = productTableView
                .getColumns();
        // name
        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory("name"));
        // price
        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory("price"));
        productList.forEach(product -> {
            tableList.add(product);
        });

        //index
        index.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(TableColumn<Product, String> col) {
                TableCell<Product, String> cell = new TableCell<Product, String>() {
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
        productTableView.setEditable(true);
        //name
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                (TableColumn.CellEditEvent<Product, String> t) -> {
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                    updateProduct( t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //price
        price.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        price.setOnEditCommit(
                (TableColumn.CellEditEvent<Product, Double> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPrice(Double.valueOf(t.getNewValue()));
                    updateProduct(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });

        operate.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<Product, String> cell = new TableCell<Product, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("删除");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        Product deleteProduct = tableList.get(getIndex());
                        SheetWriter.deleteRow(ExcelConstants.PRODUCT_SHEET_NAME, deleteProduct.getRowNumber());
                        initProductTable();
                        //按钮事件自己添加
                        System.out.println("deleteProduct:" + JSONObject.toJSONString(deleteProduct));

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
        productTableView.setItems(tableList);
    }


    private void updateProduct(Product product) {
        System.out.println(JSONObject.toJSONString(product));
        SheetWriter.updateProduct(Arrays.asList(new Product[]{product}));
        initProductTable();
    }

    public void addProduct() {
        try {
            Product product = new Product();
            product.setName(nameField.getText());
            product.setPrice(Double.valueOf(priceField.getText()));
            SheetWriter.appendProduct(product);
            nameField.setText("");
            priceField.setText("");
            initProductTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
