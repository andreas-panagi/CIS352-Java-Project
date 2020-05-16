package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Table {

    Stage window;
    TableView<Item> table;

    public void table() {
        window = new Stage();
        window.setTitle("Kitchen Inventory Management");

        //Create the columns for the TableView
        //productName Column
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(220);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        //productID Column
        TableColumn<Item, String> idColumn = new TableColumn<>("Barcode");
        idColumn.setMinWidth(220);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));

        //quantity Column
        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Stock");
        quantityColumn.setMinWidth(220);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //quantity Column
        TableColumn<Item, Date> boughColumn = new TableColumn<>("Bought Date");
        boughColumn.setMinWidth(220);
        boughColumn.setCellValueFactory(new PropertyValueFactory<>("boughtDate"));

        //create the table
        table = new TableView<>();
        table.setItems(getItems());
        table.getColumns().addAll(nameColumn, idColumn, quantityColumn, boughColumn);

        //Buttons
        Button button = new Button("< Main Menu");
        button.setId("backMenu");

        button.setOnAction(e -> {
            Menu menu = new Menu();
            menu.menu();
            window.close();
        });

        Button button1 = new Button("ADD");
        button1.setId("add");

        Button button2 = new Button("DELETE");
        button2.setOnAction(e -> deleteButtonClicked());
        button2.setId("delete");

        Button button3 = new Button("UPDATE");
        button3.setId("update");

        Label title = new Label("Your Kitchen Products");
        title.getStyleClass().add("l");
        title.setPadding(new Insets(10, 10, 10, 10));

        //LABELS for describing the textfields (ADD SECTION)
        Label namelabel = new Label();
        namelabel.setMinWidth(170);
        namelabel.setText("Product Name:");

        Label idlabel = new Label();
        idlabel.setMinWidth(170);
        idlabel.setText("Product Barcode:");

        Label quantityLabel = new Label();
        quantityLabel.setMinWidth(170);
        quantityLabel.setText("Stock:");

        //LABELS for describing the textfields (UPDATE SECTION)
        Label namelabel2 = new Label();
        namelabel2.setMinWidth(170);
        namelabel2.setText("Product Name:");

        Label idlabel2 = new Label();
        idlabel2.setMinWidth(170);
        idlabel2.setText("Product Barcode:");

        Label newIdLable2 = new Label();
        newIdLable2.setMinWidth(170);
        newIdLable2.setText("New Product Barcode:");

        Label quantityLabel2 = new Label();
        quantityLabel2.setMinWidth(170);
        quantityLabel2.setText("Stock:");

        //INPUTS FOR THE ADD SECTION
        //Name Input
        TextField nameInput = new TextField();
        nameInput.setPromptText("Product Name");
        nameInput.setMinWidth(100);

        //ID Input
        TextField idInput = new TextField();
        idInput.setPromptText("Barcode");

        //Quantity Input
        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Stock");

        //INPUTS FOR THE UPDATE SECTION
        //Name Input
        TextField updatenameInput = new TextField();
        updatenameInput.setPromptText("Product Name");
        updatenameInput.setMinWidth(100);

        //OLD ID Input
        Label updateidlabel = new Label();
        updateidlabel.setMinWidth(170);

        //New Id Input
        TextField updatenewIdInput = new TextField();
        updatenewIdInput.setPromptText("New ID");

        //Quantity Input
        TextField updatequantityInput = new TextField();
        updatequantityInput.setPromptText("Stock");

        //input, labels and button for STOCK CONTROL
        Label question = new Label("Did you consume:");
        question.setMinWidth(170);

        TextField stockInput = new TextField();
        stockInput.setPromptText("Stock");
        stockInput.setText("0");

        Label productLabel = new Label();
        productLabel.setMinWidth(170);

        Label howMany = new Label("How many?");

        Button yesButton = new Button("YES");
        yesButton.setId("yesButton");

        //BUTTONS (ADD, UPDATE) ACTIONS
        //Adding items Button
        button1.setOnAction(e -> {
            if (nameInput.getText().isEmpty() || idInput.getText().isEmpty() || quantityInput.getText().isEmpty()) {

                ErrorBox.display("Error", "You've left incomplete field(s)");

            } else {
                //GET the texts from textfields for sql INSERT
                Item product = new Item();
                product.setProductName(nameInput.getText());
                product.setProductID(idInput.getText());
                product.setQuantity(Integer.parseInt(quantityInput.getText()));

                java.util.Date date = new java.util.Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date1 = dateFormat.format(date);

                TableColumn<Item, String> column = idColumn; // column you want
                List<String> columnData = new ArrayList<>();

                for (Item item : table.getItems()) {
                    columnData.add(column.getCellObservableValue(item).getValue());
                }

                //Check for dublicates
                boolean doesExist = false;
                for (String id : columnData) {

                    if (idInput.getText().equals(id)) {

                        ErrorBox.display("Error", "This Entry: 'product ID' already exist.");
                        doesExist = true;
                        break;
                    }
                }

                if (!doesExist) {
                    table.getItems().add(product);

                    try {
                        //start the connection
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                        //test for data export from DB
                        String sql = "INSERT INTO `inventory`( `productName`, `productID`, `quantity`, `BoughtDate`) VALUES (?,?,?,?)";
                        PreparedStatement myStmt = conn.prepareStatement(sql);

                        myStmt.setString(1, nameInput.getText());
                        myStmt.setString(2, idInput.getText());
                        myStmt.setString(3, quantityInput.getText());
                        myStmt.setString(4, date1);

                        myStmt.executeUpdate();
                        conn.close();

                    } catch (Exception p) {
                        System.out.print("Did not connect to DB - Error:" + p);
                    }
                    window.close();
                    table();
                }
            }
        }); //END Adding items Button


        //This is for the update. it gets the values of the selected row.
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            updatenameInput.setText(newValue.getProductName());
            updateidlabel.setText(newValue.getProductID());
            updatequantityInput.setText("" + newValue.getQuantity());
            productLabel.setText(newValue.getProductName() + "?");
        });

        //UPDATE ACTION
        button3.setOnAction(e -> {
            if (updateidlabel.getText().isEmpty()) {
                ErrorBox.display("Error", "You've not selected a product");

            } else {

                if (updatenameInput.getText().isEmpty() || updatequantityInput.getText().isEmpty()) {
                    ErrorBox.display("Error", "You've left incomplete field(s)");

                } else {
                    TableColumn<Item, String> column = idColumn; // column you want
                    List<String> columnData = new ArrayList<>();

                    for (Item item : table.getItems()) {
                        columnData.add(column.getCellObservableValue(item).getValue());
                    }

                    boolean doesExist = false;
                    for (String id : columnData) {

                        if (updatenewIdInput.getText().equals(id)) {

                            ErrorBox.display("Error", "This Entry already exist.");
                            doesExist = true;
                            break;
                        }
                    }

                    if (!doesExist) {
                        try {
                            //start the connection
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                            //test for data export from DB
                            String sql = "UPDATE inventory SET productName= ?,productID= ?,quantity = ? WHERE productID = ?";
                            PreparedStatement myStmt = conn.prepareStatement(sql);
                            myStmt.setString(1, updatenameInput.getText());

                            //check if the new id input is empty. if it is then it will update with the old id
                            //this is happening in case the user wants to change their username
                            if (!updatenewIdInput.getText().isEmpty()) {
                                myStmt.setString(2, updatenewIdInput.getText());
                            } else {
                                myStmt.setString(2, updateidlabel.getText());
                            }

                            myStmt.setInt(3, Integer.parseInt(updatequantityInput.getText()));
                            myStmt.setString(4, updateidlabel.getText());

                            myStmt.executeUpdate();
                            conn.close();

                        } catch (Exception p) {
                            System.out.print("Did not connect to DB - Error:" + p);
                        }
                        window.close();
                        table();
                    }
                }
            }
        }); //END UPDATE ACTION


        //STOCK MANAGER ACTION
        yesButton.setOnAction(e -> {
            if (updatenameInput.getText().isEmpty()) {
                ErrorBox.display("Error", "You have not selected a product");

            } else {

                if (stockInput.getText().isEmpty() || Integer.parseInt(stockInput.getText()) <= 0) {
                    ErrorBox.display("Error", "You've left incomplete field(s)");

                } else {

                    if (Integer.parseInt(updatequantityInput.getText()) < Integer.parseInt(stockInput.getText())) {
                        ErrorBox.display("Error", "You don't have enough stock for this amount");

                    } else {

                        if (Integer.parseInt(stockInput.getText()) <= 0) {
                            ErrorBox.display("Error", "You have to enter a value of 1 or higher");

                        } else {
                            int newStockInput = Integer.parseInt(stockInput.getText()); //converts the STRING to INT

                            try {
                                //start the connection
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                                //test for data export from DB
                                String sql = "UPDATE inventory SET quantity = ? WHERE productID = ?";
                                PreparedStatement myStmt = conn.prepareStatement(sql);
                                int afterReduction = Integer.parseInt(updatequantityInput.getText()) - newStockInput; //REDUCE THE STOCK

                                myStmt.setInt(1, afterReduction);
                                myStmt.setString(2, updateidlabel.getText());

                                myStmt.executeUpdate();
                                conn.close();

                            } catch (Exception p) {
                                System.out.print("Did not connect to DB - Error:" + p);
                            }
                            window.close();
                            table();
                        }
                    }
                }
            }
        });


        //CREATE HBOX (ADD SECTION)
        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10, 10, 10, 10));
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(namelabel, idlabel, quantityLabel);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(3, 10, 10, 10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(nameInput, idInput, quantityInput, button1);


        //CREATE HBOX (UPDATE SECTION)
        HBox hbox4 = new HBox();
        hbox4.setPadding(new Insets(10, 10, 10, 10));
        hbox4.setSpacing(10);
        hbox4.getChildren().addAll(namelabel2, idlabel2, quantityLabel2, newIdLable2);

        HBox hbox3 = new HBox();
        hbox3.setPadding(new Insets(3, 10, 10, 10));
        hbox3.setSpacing(10);
        hbox3.getChildren().addAll(updatenameInput, updateidlabel, updatequantityInput, updatenewIdInput, button3, button2);


        //CREATE HBOX (STOCK SECTION)
        HBox hBox5 = new HBox();
        hBox5.setPadding(new Insets(10, 10, 5, 10));
        hBox5.setSpacing(10);
        hBox5.getChildren().add(question);

        HBox hbox6 = new HBox();
        hbox6.setPadding(new Insets(1, 10, 10, 10));
        hbox6.setSpacing(10);
        hbox6.getChildren().addAll(stockInput, productLabel, yesButton);

        HBox hbox7 = new HBox();
        hbox7.setPadding(new Insets(2, 10, 10, 10));
        hbox7.setSpacing(10);
        hbox7.getChildren().add(howMany);


        //Setting up the layout
        VBox vbox = new VBox();
        vbox.getChildren().add(button);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        VBox vbox2 = new VBox();
        vbox2.getChildren().addAll(hbox2, hbox, hBox5, hbox7, hbox6, hbox4, hbox3);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(title);
        borderPane.setCenter(table);
        borderPane.setBottom(vbox2);
        borderPane.setLeft(vbox);

        Scene scene = new Scene(borderPane, 1000, 750);
        scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }


    //DELETE ACTION METHOD
    public void deleteButtonClicked() {
        ObservableList<Item> itemsSelected, allProducts;
        allProducts = table.getItems();
        itemsSelected = table.getSelectionModel().getSelectedItems();
        String itemsel = itemsSelected.toString();
        itemsel = itemsel.replace("]", "");
        itemsel = itemsel.replace("[", "");

        if (itemsSelected.isEmpty()) {

            ErrorBox.display("Error", "You haven't selected any entry");

        } else {
            //First it make sure the user wants to delete that entry
            boolean userAnswer = ConfirmBox.display("You sure?", "Are you sure you want to delete this product?");

            if (userAnswer) {

                try {
                    //start the connection
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                    //test for data export from DB
                    String sql = "DELETE FROM `inventory` WHERE productID= ?";
                    PreparedStatement myStmt = conn.prepareStatement(sql);

                    myStmt.setString(1, itemsel);

                    myStmt.executeUpdate();
                    conn.close();

                } catch (Exception p) {
                    System.out.print("Did not connect to DB - Error:" + p);
                }
                itemsSelected.forEach(allProducts::remove);
            }
        }
    }

    //GET ALL PRODUCTS FROM DB AND PUT THEM IN AN OBSERVABLE LIST FOR TABLEVIEW
    public ObservableList<Item> getItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            //start the connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

            //test for data export from DB
            String sql = "select * from inventory";
            Statement myStmt = conn.createStatement();
            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                String productName = rs.getString("productName");
                String productID = rs.getString("productID");
                int quantity = rs.getInt("quantity");
                Date boughtDate = rs.getDate("BoughtDate");
                items.add(new Item(productName, productID, quantity, boughtDate));
            }
            conn.close();
        } catch (Exception e) {
            System.out.print("Do not connect to DB - Error:" + e);
        }
        return items;
    }

}