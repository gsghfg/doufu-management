package com.zf.product.doufu.controller;

import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.model.Goods;
import com.zf.product.doufu.model.Order;
import com.zf.product.doufu.model.OrderContent;
import com.zf.product.doufu.utils.TableUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class DayStatisticsController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DayStatisticsController.class);

    @FXML
    private ChoiceBox monthChoice;
    @FXML
    private ChoiceBox dayChoice;
    @FXML
    private Label dayBrief;

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
                    dayBrief.setText(orderContent.getBrief());
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

//        Label orderBrief = new Label(order.getOrderBrief());
//        customerName.setFont(new Font(15));
//        customerName.setPadding(new Insets(2, 2, 5, 5));

        //tableView
        TableView<Goods> tableView = new TableView<>();
        ObservableList<Goods> dataList =
                FXCollections.observableArrayList(order.getGoodsList());
        //name
        TableColumn<Goods, String> produceName =
                new TableColumn<>("????????????");
        produceName.setMinWidth(100);
        produceName.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        //price
        TableColumn<Goods, Double> producePrice =
                new TableColumn<>("????????????");
        producePrice.setMinWidth(100);
        producePrice.setCellValueFactory(
                new PropertyValueFactory<>("price"));
        producePrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //amount
        TableColumn<Goods, Double> produceAmount = new TableColumn<>("????????????");
        produceAmount.setMinWidth(100);
        produceAmount.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        produceAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //charge
        TableColumn<Goods, Double> produceCharge = new TableColumn<>("????????????");
        produceCharge.setMinWidth(100);
        produceCharge.setCellValueFactory(
                new PropertyValueFactory<>("charge"));
        produceAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        tableView.setItems(dataList);
        tableView.getColumns().addAll(produceName, producePrice, produceAmount, produceCharge);

        //?????????
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 10, 10, 10));

        final double[] count = {0d};
        order.getGoodsList().forEach(goods -> {
            count[0] += Double.valueOf(goods.getCharge());
        });
        HBox button = new HBox();
        Label blank = new Label();
        blank.setMinWidth(300d);

        Label orderBrief = new Label();
        orderBrief.setText("?????????" + TableUtils.formatter.format(count[0]) + "???");
        orderBrief.setFont(new Font(15));
        orderBrief.setPadding(new Insets(2, 2, 5, 5));
        button.getChildren().addAll(blank, orderBrief);

        tableViewBox.getChildren().addAll(titleBox, tableView, button, separator);
    }


    private void initTableViewBox() {
        tableViewBox.getChildren().clear();
        logger.info("init table view box...");
        List<Order> orderList = orderContent.getOrderDetails();
        printTableView(orderList);
    }

}
