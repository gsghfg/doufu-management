package com.zf.product.doufu.controller;

import com.zf.product.doufu.model.Customer;
import com.zf.product.doufu.model.Goods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private TableColumn<Goods, String> name;
    private TableColumn<Goods, Double> price;
    private TableColumn<Goods, Double> amount;
    private TableColumn<Goods, String> operate;

    private ChoiceBox nameChoice;
    private TextField priceField;
    private TextField operateField;
    private Button addGoodsButton;

    @FXML
    private DatePicker orderDate;

    @FXML
    private Label labelText;
    @FXML
    private TableView<Customer> customerTableView;

    public ObservableList<Customer> tableList = FXCollections.observableArrayList();


    @FXML
    private VBox tableViewBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableViewBox();
    }

    private void initTableViewBox() {
        logger.info("init table view box");
        HBox titleBox = new HBox();

        CheckBox checkBox = new CheckBox();
//        tableViewBox.getChildren().add(checkBox);
        TextField name = new TextField("zhangsan");
        name.setEditable(false);
        name.setStyle("-fx-border-color: #848181");
//        tableViewBox.getChildren().add(name);
        TextField brief = new TextField("brief");
        brief.setEditable(false);
        brief.setStyle("-fx-border-color: #848181");
//        tableViewBox.getChildren().add(brief);

        titleBox.getChildren().addAll(checkBox, name, brief);
        TableView<Goods> table = new TableView<>();
        ObservableList<Goods> data =
                FXCollections.observableArrayList(
                        new Goods("product1", 3.4d, 0d),
                        new Goods("product2", 3.4d, 0d),
                        new Goods("product3", 3.4d, 0d),
                        new Goods("product4", 3.4d, 0d),
                        new Goods("product5", 3.4d, 0d));
        TableColumn<Goods, String> productName =
                new TableColumn<>("商品名称");
        productName.setMinWidth(100);
        productName.setCellValueFactory(
                new PropertyValueFactory<>("name"));

       /* firstNameCol.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
        firstNameCol.setOnEditCommit(
            (CellEditEvent<Person, String> t) -> {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setFirstName(t.getNewValue());
        });*/


        TableColumn<Goods, Double> producePrice =
                new TableColumn<>("商品价格");
        producePrice.setMinWidth(100);
        producePrice.setCellValueFactory(
                new PropertyValueFactory<>("price"));
      /* lastNameCol.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
       lastNameCol.setOnEditCommit(
            (CellEditEvent<Person, String> t) -> {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setLastName(t.getNewValue());
        });*/

        TableColumn<Goods, String> produceAmount = new TableColumn<>("产品数量");
        produceAmount.setMinWidth(200);
        produceAmount.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        produceAmount.setCellFactory(TextFieldTableCell.<Goods>forTableColumn());
//        emailCol.setOnEditCommit(
//                (TableColumn.CellEditEvent<Person, String> t) -> {
//                    ((Person) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setEmail(t.getNewValue());
//                });

        table.setItems(data);

        operate = new TableColumn<Goods, String>("操作");
        operate.setCellFactory((col) -> {
            //UserLoad换成你自己的实体名称
            TableCell<Goods, String> cell = new TableCell<Goods, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Button button1 = new Button("删除");
                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
                    button1.setOnMouseClicked((col) -> {

                        //获取list列表中的位置，进而获取列表对应的信息数据
//                        Product deleteProduct = tableList.get(getIndex());
//                        SheetWriter.deleteRow(ExcelConstants.PRODUCT_SHEET_NAME, deleteProduct.getRowNumber());
//                        initProductTable();
                        //按钮事件自己添加
//                        System.out.println("deleteProduct:" + JSONObject.toJSONString(deleteProduct));

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


        table.getColumns().addAll(productName, producePrice, produceAmount, operate);


        HBox addBox = new HBox();
        nameChoice = new ChoiceBox(FXCollections.observableArrayList(
                new String[]{"shangping1,shangping2"}));
        TextField priceField = new TextField("价格");
        TextField amountField = new TextField("数量");
        Button addGoodsButton = new Button("添加");
        addBox.getChildren().addAll(nameChoice, priceField, amountField, addGoodsButton);
        tableViewBox.getChildren().addAll(titleBox, table, addBox);

//        tableViewBox.setSpacing(40);
//        tableViewBox.setPadding(new Insets(20, 10, 10, 20));
    }

//    private void initCustomerTable() {
//        tableList.clear();
//        List<Customer> customerList = SheetReader.readCustomer();
//        ObservableList<TableColumn<Customer, ?>> observableList = customerTableView
//                .getColumns();
//        // name
//        observableList.get(0).setCellValueFactory(
//                new PropertyValueFactory("name"));
//        // phone
//        observableList.get(1).setCellValueFactory(
//                new PropertyValueFactory("phone"));
//        // address
//        observableList.get(2).setCellValueFactory(
//                new PropertyValueFactory("address"));
//        customerList.forEach(customer -> {
//            tableList.add(customer);
//        });
//        //配置可编辑
//        customerTableView.setEditable(true);
//        //name
//        name.setCellFactory(TextFieldTableCell.forTableColumn());
//        name.setOnEditCommit(
//                (TableColumn.CellEditEvent<Customer, String> t) -> {
//                    ((Customer) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setName(t.getNewValue());
//                    updateCustomer((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()));
//                });
//        //phone
//        phone.setCellFactory(TextFieldTableCell.forTableColumn());
//        phone.setOnEditCommit(
//                (TableColumn.CellEditEvent<Customer, String> t) -> {
//                    (t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setPhone(t.getNewValue());
//                    updateCustomer(t.getTableView().getItems().get(t.getTablePosition().getRow()));
//                });
//        //address
//        address.setCellFactory(TextFieldTableCell.forTableColumn());
//        address.setOnEditCommit(
//                (TableColumn.CellEditEvent<Customer, String> t) -> {
//                    ((Customer) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                    ).setAddress(t.getNewValue());
//                    updateCustomer((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow()));
//                });
//        operate.setCellFactory((col) -> {
//            //UserLoad换成你自己的实体名称
//            TableCell<Customer, String> cell = new TableCell<Customer, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    Button button1 = new Button("删除");
//                    button1.setStyle("-fx-background-color: #ff0000;-fx-text-fill: #ffffff");
//                    button1.setOnMouseClicked((col) -> {
//
//                        //获取list列表中的位置，进而获取列表对应的信息数据
//                        Customer deleteCustomer = tableList.get(getIndex());
//                        SheetWriter.deleteRow(ExcelConstants.CUSTOMER_SHEET_NAME, deleteCustomer.getRowNumber());
//                        initCustomerTable();
//                        //按钮事件自己添加
//                        System.out.println("deleteCustomer:" + JSONObject.toJSONString(deleteCustomer));
//
//                    });
//
//                    if (empty) {
//                        //如果此列为空默认不添加元素
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        this.setGraphic(button1);
//                    }
//                }
//            };
//            cell.setStyle("-fx-alignment: center");
//            return cell;
//        });
//        customerTableView.setItems(tableList);
//    }


}
