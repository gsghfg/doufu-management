package com.zf.product.doufu.controller;

import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.model.Goods;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.model.OrderContent;
import com.zf.product.doufu.pdf.PDFUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PrintController extends BaseController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PrintController.class);

    @FXML
    private ChoiceBox monthChoice;
    @FXML
    private ChoiceBox dayChoice;

    @FXML
    private VBox tableViewBox;

    private static Map<String, String[]> orderMonthDayMap = new HashMap<>();
    private static final String[] monthArray;

    static {
        orderMonthDayMap = SheetReader.readOrderDays(ExcelConstants.ORDER_DIRECTORY_PATH);
        monthArray = orderMonthDayMap.keySet().toArray(new String[0]);
    }

    private OrderContent orderContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBox();
    }

    public void initChoiceBox() {
        monthChoice.setItems(FXCollections.observableArrayList(monthArray));
        monthChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_val, Number new_val) -> {
                    dayChoice.setItems(FXCollections.observableArrayList(
                            orderMonthDayMap.get(monthArray[new_val.intValue()])));
                });
        dayChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_val, Number new_val) -> {
                    String orderDay = orderMonthDayMap.get(monthChoice.getValue().toString())[new_val.intValue()];
                    logger.info("choice month:{}", orderDay);
                    orderContent = SheetReader.readOrderContent(orderDay);
                    initTableViewBox();
                });
    }


    private void printTableView(List<Order> orderDetails) {
        for (Order order : orderDetails) {
            printCustomerOrderView(order);
        }
    }

    private void printCustomerOrderView(Order order) {
        //title box
        HBox titleBox = new HBox();
        //name
        Label customerName = new Label(order.getCustomerName());
        customerName.setFont(new Font(15));
        customerName.setPadding(new Insets(2, 2, 5, 5));
        titleBox.getChildren().addAll(customerName);

        //tableView
        TableView<Goods> tableView = new TableView<>();
        ObservableList<Goods> dataList =
                FXCollections.observableArrayList(order.getGoodsList());
        //name
        TableColumn<Goods, String> produceName =
                new TableColumn<>("商品名称");
        produceName.setMinWidth(100);
        produceName.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        //price
        TableColumn<Goods, Double> producePrice =
                new TableColumn<>("商品价格");
        producePrice.setMinWidth(100);
        producePrice.setCellValueFactory(
                new PropertyValueFactory<>("price"));
        producePrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //amount
        TableColumn<Goods, Double> produceAmount = new TableColumn<>("订货数量");
        produceAmount.setMinWidth(200);
        produceAmount.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        produceAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        tableView.setItems(dataList);
        tableView.getColumns().addAll(produceName, producePrice, produceAmount);

        //分隔线
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 10, 10, 10));

        tableViewBox.getChildren().addAll(titleBox, tableView, separator);
    }


    private void initTableViewBox() {
        tableViewBox.getChildren().clear();
        logger.info("init table view box...");
        List<Order> orderList = orderContent.getOrderDetails();
        printTableView(orderList);
    }

    public void printAllOrder() {
        try {
            logger.info("print all order ...");
            PDFUtils.printOrder(dayChoice.getValue().toString(), orderContent.getOrderDetails().get(0));
        } catch (Exception e) {
            logger.error("saveOrder error", e);
        }
    }

}
