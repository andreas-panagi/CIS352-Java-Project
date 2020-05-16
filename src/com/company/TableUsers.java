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
import java.util.ArrayList;
import java.util.List;


public class TableUsers {

    Stage window;
    TableView<User> table;

    public void tableUsers() {
        window = new Stage();
        window.setTitle("Kitchen Inventory Management");

        //username Column
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(445);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        //password Column
        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(445);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        //CREATE BUTTONS
        //Back Button
        Button button = new Button("< Main Menu");
        button.setId("backMenu");

        button.setOnAction(e -> {
            Menu menu = new Menu();
            menu.menu();
            window.close();
        });

        //add Button
        Button button1 = new Button("ADD");
        button1.setId("add");

        //Delete Button
        Button button2 = new Button("DELETE");
        button2.setOnAction(e -> deleteButtonClicked());
        button2.setId("delete");

        //Update Button
        Button button3 = new Button("UPDATE");
        button3.setId("update");

        Label title = new Label("All the Users");
        title.getStyleClass().add("l");
        title.setPadding(new Insets(10, 10, 10, 10));


        //INPUTS FOR THE ADD SECTION
        //Username Input
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.setMinWidth(100);

        //Password Input
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");


        //INPUTS FOR THE UPDATE SECTION
        //Username Input
        Label usernameInput2 = new Label();
        usernameInput2.setMinWidth(170);

        //Password Input
        TextField passwordInput2 = new TextField();
        passwordInput2.setPromptText("Password");
        passwordInput2.setMinWidth(170);

        //New Username Input
        TextField newusernameInput = new TextField();
        newusernameInput.setPromptText("New Username");


        //LABELS for describing the textfields (ADD SECTION)
        Label usernameLabel = new Label();
        usernameLabel.setMinWidth(170);
        usernameLabel.setText("Username:");

        Label passwordLabel = new Label();
        passwordLabel.setMinWidth(170);
        passwordLabel.setText("Password:");


        //LABELS for describing the textfields (UPDATE SECTION)
        Label usernameLabel2 = new Label();
        usernameLabel2.setMinWidth(170);
        usernameLabel2.setText("Username:");

        Label passwordLabel2 = new Label();
        passwordLabel2.setMinWidth(170);
        passwordLabel2.setText("Password:");

        Label newUsernameLable2 = new Label();
        newUsernameLable2.setMinWidth(170);
        newUsernameLable2.setText("New Username:");

        //OLD ID Input (this is used to store the old barcode and make it non-editable)
        Label oldUsernamelabel = new Label();
        oldUsernamelabel.setMinWidth(170);

        //Create table
        table = new TableView<>();
        table.setItems(getUsers());
        table.getColumns().addAll(usernameColumn, passwordColumn);

        //add Button Action
        button1.setOnAction(e -> {
            if (usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {

                ErrorBox.display("Error", "You've left uncompleted field(s)");

            } else {
                User users = new User();
                users.setUsername(usernameInput.getText());
                users.setPassword(passwordInput.getText());


                TableColumn<User, String> column = usernameColumn; // column you want
                List<String> columnData = new ArrayList<>();

                for (User item : table.getItems()) {
                    columnData.add(column.getCellObservableValue(item).getValue());
                }

                boolean doesExist = false;
                for (String username : columnData) {

                    if (usernameInput.getText().toLowerCase().equals(username)) {

                        ErrorBox.display("Error", "This Entry: 'Username' already exist.");
                        doesExist = true;
                        break;
                    }
                }

                if (!doesExist) {
                    table.getItems().add(users);

                    try {
                        //start the connection
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                        //test for data export from DB
                        String sql = "INSERT INTO users (username, Password) VALUES (?,?)";
                        PreparedStatement myStmt = conn.prepareStatement(sql);

                        myStmt.setString(1, usernameInput.getText().toLowerCase());
                        myStmt.setString(2, passwordInput.getText().toLowerCase());

                        myStmt.executeUpdate();
                        conn.close();

                    } catch (Exception p) {
                        System.out.print("Did not connect to DB - Error:" + p);
                    }
                    window.close();
                    tableUsers();
                }
            }
        }); //END Adding users Button


        //This is for the update. it gets the values of the selected row.
        table.getSelectionModel().selectedItemProperty().addListener((observableValue1, oldValue2, newValue3) -> {
            usernameInput2.setText(newValue3.getUsername());
            passwordInput2.setText(newValue3.getPassword());
        });


        //UPDATE USERS ACTION
        button3.setOnAction(e -> {
            if (usernameInput2.getText().isEmpty()) {

                ErrorBox.display("Error", "You haven't selected any entry");

            } else {

                if (usernameInput2.getText().isEmpty() || passwordInput2.getText().isEmpty()) {

                    ErrorBox.display("Error", "You've left incomplete field(s)");

                } else {
                    TableColumn<User, String> column = usernameColumn; // column you want
                    List<String> columnData = new ArrayList<>();

                    for (User item : table.getItems()) {
                        columnData.add(column.getCellObservableValue(item).getValue());
                    }

                    boolean doesExist = false;
                    for (String username : columnData) {

                        if (newusernameInput.getText().equals(username)) {

                            ErrorBox.display("Error", "This Entry: 'Username' already exist.");
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
                            String sql = "UPDATE users SET username= ?,Password= ? WHERE username = ?";
                            PreparedStatement myStmt = conn.prepareStatement(sql);

                            if (!newusernameInput.getText().isEmpty()) {
                                myStmt.setString(1, newusernameInput.getText().toLowerCase());
                            } else {
                                myStmt.setString(1, usernameInput2.getText().toLowerCase());
                            }

                            myStmt.setString(2, passwordInput2.getText().toLowerCase());
                            myStmt.setString(3, usernameInput2.getText().toLowerCase());

                            myStmt.executeUpdate();
                            conn.close();

                        } catch (Exception p) {
                            System.out.print("Did not connect to DB - Error:" + p);
                        }
                        window.close();
                        tableUsers();
                    }
                }
            }
        }); //END OF UPDATE ACTION

        //ADDING section hboxes
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(usernameLabel, passwordLabel);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10, 10, 10, 10));
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(usernameInput, passwordInput, button1);


        //UPDATE section hboxes
        HBox hbox3 = new HBox();
        hbox3.setPadding(new Insets(10, 10, 10, 10));
        hbox3.setSpacing(10);
        hbox3.getChildren().addAll(usernameLabel2, passwordLabel2, newUsernameLable2);

        HBox hbox4 = new HBox();
        hbox4.setPadding(new Insets(10, 10, 10, 10));
        hbox4.setSpacing(10);
        hbox4.getChildren().addAll(usernameInput2, passwordInput2, newusernameInput, button3, button2);


        //setting up the layout
        VBox vbox = new VBox();
        vbox.getChildren().add(button);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        VBox vbox2 = new VBox();
        vbox2.getChildren().addAll(hbox, hbox2, hbox3, hbox4);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(title);
        borderPane.setCenter(table);
        borderPane.setBottom(vbox2);
        borderPane.setLeft(vbox);

        Scene scene = new Scene(borderPane, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }

    //DELETE ACTION METHOD
    public void deleteButtonClicked() {
        ObservableList<User> itemsSelected, allUsers;
        allUsers = table.getItems();
        itemsSelected = table.getSelectionModel().getSelectedItems();
        String usersel = itemsSelected.toString();

        usersel = usersel.replace("]", "");
        usersel = usersel.replace("[", "");

        if (itemsSelected.isEmpty()) {

            ErrorBox.display("Error", "You haven't selected any entry");

        } else {

            boolean userAnswer = ConfirmBox.display("You sure?", "Are you sure you want to delete this user?");

            if (userAnswer) {

                try {
                    //start the connection
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

                    //test for data export from DB
                    String sql = "DELETE FROM users WHERE username = ?";
                    PreparedStatement myStmt = conn.prepareStatement(sql);

                    myStmt.setString(1, usersel);
                    myStmt.executeUpdate();
                    conn.close();

                } catch (Exception p) {
                    System.out.print("Did not connect to DB - Error:" + p);
                }
                itemsSelected.forEach(allUsers::remove);
            }
        }
    } //END DELETE ACTION METHOD

    public ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            //start the connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");

            //test for data export from DB
            String sql = "select * from users";
            Statement myStmt = conn.createStatement();
            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("username");
                String password = rs.getString("Password");
                users.add(new User(id, username, password));
            }
            conn.close();

        } catch (Exception e) {
            System.out.print("Do not connect to DB - Error:" + e);
        }
        return users;
    }
}
