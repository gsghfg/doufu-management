package com.zf.product.doufu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zf.product.doufu.constants.ExcelConstants;
import com.zf.product.doufu.excel.SheetReader;
import com.zf.product.doufu.model.OrderContent;
import com.zf.product.doufu.model.OrderFile;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OrderManagerController extends HomeController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(OrderManagerController.class);
    @FXML
    private TableColumn<OrderContent, String> index;
    @FXML
    private TableColumn<OrderContent, String> date;
    @FXML
    private TableColumn<OrderContent, String> brief;
    @FXML
    private TableColumn<OrderContent, String> details;
    @FXML
    private TableColumn<OrderContent, String> copy;
    @FXML
    private TableColumn<OrderContent, String> print;

    @FXML
    private HBox choiceBox;
    final static Label choiceLabel = new Label("选择订单时间:");

    static {
        choiceLabel.setFont(Font.font("Tahoma", 18));
    }

    @FXML
    private TableView<OrderContent> orderManagerTableView;

    public ObservableList<OrderContent> tableList = FXCollections.observableArrayList();

    private List<OrderContent> orderContentList = new ArrayList<>();

    private Map<String, List<OrderContent>> monthOrderListMap = null;
    private String[] monthArray = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<OrderFile> orderFileList = SheetReader.readOrderFiles(ExcelConstants.ORDER_DIRECTORY_PATH);
        monthOrderListMap = orderFileList.stream().collect(
                Collectors.toMap(OrderFile::getOrderMonth, OrderFile::getOrderContents));
        monthArray = monthOrderListMap.keySet().toArray(new String[0]);
        orderContentList = monthOrderListMap.get(monthArray[0]);
        initCheckBox(monthArray);
        initOrderTableView();
    }

    private void initOrderTableView() {
        logger.info("initOrderTableView orderContentList:{}", JSONObject.toJSONString(orderContentList));
        tableList.clear();
        ObservableList<TableColumn<OrderContent, ?>> observableList = orderManagerTableView
                .getColumns();
        index.setCellFactory(new Callback<TableColumn<OrderContent, String>, TableCell<OrderContent, String>>() {
            @Override
            public TableCell<OrderContent, String> call(TableColumn<OrderContent, String> col) {
                TableCell<OrderContent, String> cell = new TableCell<OrderContent, String>() {
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
        // date
        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory("date"));
        // brief
        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory("brief"));
        orderContentList.forEach(orderContent -> {
            tableList.add(orderContent);
        });
        //查看详情
        details.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<OrderContent, String> cell = new TableCell<OrderContent, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("查看订单详情");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        OrderContent orderContent = tableList.get(getIndex());
                        try {
                            // 创建新的stage
                            Stage secondStage = new Stage();
                            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/orderDetails.fxml")));
                            secondScene.setUserData(orderContent);
                            secondStage.setScene(secondScene);
                            secondStage.show();
                        } catch (Exception e) {
                            logger.error("skip error", e);
                        }

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
        //复制下单
        copy.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<OrderContent, String> cell = new TableCell<OrderContent, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("复制订单");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        OrderContent orderContent = tableList.get(getIndex());


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

        //复制下单
        print.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<OrderContent, String> cell = new TableCell<OrderContent, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("打印订单");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
                        OrderContent orderContent = tableList.get(getIndex());


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


        orderManagerTableView.setItems(tableList);
    }

    private void initCheckBox(String[] monthArray) {
        logger.info("init check box monthArray:{}", JSONObject.toJSONString(monthArray));
        if (monthArray == null || monthArray.length == 0) {
            return;
        }
        ChoiceBox choiceMonth = new ChoiceBox(FXCollections.observableArrayList(
                monthArray)
        );
        choiceMonth.setValue(monthArray[0]);
        choiceMonth.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_val, Number new_val) -> {
                    orderContentList = monthOrderListMap.get(monthArray[new_val.intValue()]);
                    logger.info("choice value:{}, orderContentList:{}", new_val.intValue(), JSONObject.toJSONString(orderContentList));
                    initOrderTableView();
                });
        choiceBox.getChildren().addAll(choiceLabel, choiceMonth);
    }


    public static void main(String[] args) {
        String path = "/Users/jhyang/IdeaProjects/doufu/doufu-management/src/main/resources/order/order-202111.xlsx";
        File tempFile = new File(path.trim());
        String fileName = tempFile.getName();
        String orderMonth = fileName.substring(0, fileName.lastIndexOf(".")).split("-")[1];
        System.out.println(orderMonth);
        System.out.println(orderMonth.substring(0, 4));
        System.out.println(orderMonth.substring(4));
    }

}
