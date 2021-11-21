package com.zf.product.doufu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.model.*;
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
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class OrderController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @FXML
    private DatePicker orderDate;

    @FXML
    private VBox tableViewBox;

    private OrderContent orderContent;

    private static final List<Product> productList;
    private static final List<Customer> customerList;
    private static final String[] productArray;
    private static final Map<String, Double> productPriceMap;
    private static final SimpleDateFormat ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    //customerName,productName,goods
    private Map<String, Map<String, Goods>> customerGoodsNameGoodsMap = new HashMap<>();

    static {
        productList = SheetReader.readProduct();
        customerList = SheetReader.readCustomer();
        productArray = productList.stream().map(Product::getName).collect(Collectors.toList()).toArray(new String[0]);
        productPriceMap = productList.stream().collect(Collectors.toMap(Product::getName, Product::getPrice));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("init ...");
        if (orderContent == null) {
            initOrderContent();
        }
        initTableViewBox(orderContent);
    }

    private void initOrderContent() {
        orderContent = new OrderContent();
        orderContent.setDate(ORDER_DATE_FORMAT.format(new Date()));
        List<Order> orderDetails = new ArrayList<>();
        customerList.forEach(customer -> {
            Order order = new Order();
            order.setCustomerName(customer.getName());
            List<Goods> goodsList = new ArrayList<>();
            productList.forEach(product -> {
                Goods goods = new Goods();
                goods.setName(product.getName());
                goods.setPrice(product.getPrice() == null ? 0d : product.getPrice());
                goods.setAmount(0d);
                goodsList.add(goods);
            });
            order.setGoodsList(goodsList);
            orderDetails.add(order);
        });
        orderContent.setOrderDetails(orderDetails);
        initCustomerGoodsNameGoodsMap(orderContent);
        logger.info("init order content {}", JSONObject.toJSONString(orderContent));
    }

    private void initCustomerGoodsNameGoodsMap(OrderContent orderContent) {
        customerGoodsNameGoodsMap.clear();
        List<Order> orderList = orderContent.getOrderDetails();
        orderList.forEach(order -> {
            String customerName = order.getCustomerName();
            List<Goods> goodsList = order.getGoodsList();
            Map<String, Goods> productNameGoodsMap = new HashMap<>();
            goodsList.stream().forEach(goods -> {
                String productName = goods.getName();
                productNameGoodsMap.put(productName, goods);
            });
            customerGoodsNameGoodsMap.put(customerName, productNameGoodsMap);
        });
    }

    private void updateOrder(String orderDate, String customerName, List<Goods> updateGoodsList) {
        updateOrderContent(orderDate, customerName, updateGoodsList);
        initTableViewBox(orderContent);
    }


    private void updateOrderContent(String orderDate, String customerName, List<Goods> updateGoodsList) {
        Map<String, Goods> productNameGoodsMap = customerGoodsNameGoodsMap.get(customerName);
        if (productNameGoodsMap == null) {
            productNameGoodsMap = new HashMap<>();
        }
        Map<String, Goods> finalProductNameGoodsMap = productNameGoodsMap;
        updateGoodsList.stream().forEach(goods -> {
            String productName = goods.getName();
            Goods tempGoods = finalProductNameGoodsMap.get(productName);
            if (tempGoods == null) {
                tempGoods = new Goods();
                tempGoods.setName(goods.getName());
                tempGoods.setPrice(goods.getPrice());
                tempGoods.setAmount(goods.getAmount());
                finalProductNameGoodsMap.put(productName, tempGoods);
            } else {
                if (goods.getAmount() == 0d) {
                    finalProductNameGoodsMap.remove(productName);
                } else {
                    tempGoods.setAmount(tempGoods.getAmount() + goods.getAmount());
                }
            }
        });
        customerGoodsNameGoodsMap.put(customerName, finalProductNameGoodsMap);

        orderContent = new OrderContent();
        orderContent.setDate(orderDate);
        orderContent.setBrief("");
        //TODO 计算 Content br
        List<Order> orderDetails = new ArrayList<>();
        customerGoodsNameGoodsMap.forEach((customer, nameGoodsMap) -> {
            Order order = new Order();
            order.setCustomerName(customer);
            StringBuffer stringBuffer = new StringBuffer();
            List<Goods> goodsList = new ArrayList<>();
            nameGoodsMap.forEach((productName, goods) -> {
                stringBuffer.append(productName + " " + goods.getAmount() + "斤, 预计金额：" + (goods.getAmount() * goods.getPrice()) + "元").append("\n");
                goodsList.add(goods);
            });
            order.setOrderBrief(stringBuffer.toString());
            order.setGoodsList(goodsList);
            orderDetails.add(order);
        });
        orderContent.setOrderDetails(orderDetails);
    }


    private void printTableView(List<Order> orderDetails) {
        for (Order order : orderDetails) {
            printCustomerOrderView(order);
        }
    }

    private void printCustomerOrderView(Order order) {
        //title box
        HBox titleBox = new HBox();
        //checkBox
        CheckBox checkBox = new CheckBox();
        //name
        Label customerName = new Label(order.getCustomerName());
        customerName.setFont(new Font(15));
        customerName.setPadding(new Insets(2, 2, 5, 5));
        //brief
        Label brief = new Label(order.getOrderBrief());
        brief.setFont(new Font(15));
        brief.setPadding(new Insets(2, 2, 5, 5));
        brief.setStyle("-fx-border-color: #848181");
        titleBox.getChildren().addAll(checkBox, customerName, brief);

        //tableView
        TableView<Goods> tableView = new TableView<>();
//        tableView.setEditable(false);
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

        //operate
        TableColumn<Goods, String> operate = new TableColumn<Goods, String>("操作");
        operate.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<Goods, String> cell = new TableCell<Goods, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("删除");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {
                        String date = orderDate.getValue().toString();
                        Goods temp = dataList.get(getIndex());
                        temp.setAmount(0d);
                        updateOrder(date, customerName.getText(), Arrays.asList(temp));
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
        //配置可编辑
        tableView.setEditable(true);
        //name
        produceName.setCellFactory(TextFieldTableCell.forTableColumn());
        produceName.setOnEditCommit(
                (TableColumn.CellEditEvent<Goods, String> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                    //TODO
//                    updateProduct( t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //price
        producePrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        producePrice.setOnEditCommit(
                (TableColumn.CellEditEvent<Goods, Double> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPrice(Double.valueOf(t.getNewValue()));
                    //TODO
//                    updateProduct(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        //amount
        produceAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        produceAmount.setOnEditCommit(
                (TableColumn.CellEditEvent<Goods, Double> t) -> {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setPrice(Double.valueOf(t.getNewValue()));
                    //TODO
//                    updateProduct(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                });
        tableView.setItems(dataList);
        tableView.getColumns().addAll(produceName, producePrice, produceAmount, operate);
        //add box
        HBox addBox = new HBox();
        ChoiceBox nameChoice = new ChoiceBox(FXCollections.observableArrayList(
                productArray));
        //商品价格
        TextField priceField = new TextField("商品价格");
        priceField.setEditable(false);
        nameChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_val, Number new_val) -> {
                    priceField.setText(String.valueOf(productPriceMap.get(productArray[new_val.intValue()])));
                });

        //数量
        TextField amountField = new TextField("订货数量");
//        amountField.setEditable(false);
        Button addGoodsButton = new Button("增加");
        addGoodsButton.setOnAction(event -> {
            String name = nameChoice.getValue().toString();
            try {
                Double price = Double.valueOf(priceField.getText());
                Double amount = Double.valueOf(amountField.getText());
                String date = orderDate.getValue().toString();
                updateOrder(date, customerName.getText(), Arrays.asList(new Goods(name, price, amount)));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            logger.info("add goods on action... {} name:{}, price:{}, amount:{}", orderDate.getValue().toString(), name, priceField.getText(), amountField.getText());
        });
        addBox.getChildren().addAll(nameChoice, priceField, amountField, addGoodsButton);
        //分隔线
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 10, 10, 10));

        tableViewBox.getChildren().addAll(titleBox, tableView, addBox, separator);
    }


    private void initTableViewBox(OrderContent orderContent) {
        tableViewBox.getChildren().clear();
        logger.info("init table view box...");
        if (StringUtils.isNotBlank(orderContent.getDate())) {
            LocalDate localDate = null;
            try {
                Date date = ORDER_DATE_FORMAT.parse(orderContent.getDate());
                Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                localDate = instant.atZone(zoneId).toLocalDate();
            } catch (ParseException e) {
                logger.error("Parse order date exception", e);
            } finally {
                orderDate.setValue(localDate);
            }
        } else {
            orderDate.setValue(LocalDate.now());
        }
        orderDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        logger.info("orderDate change {}", orderDate.getValue());

                    }
                };
            }
        });
        List<Order> orderList = orderContent.getOrderDetails();
        printTableView(orderList);
    }
}
