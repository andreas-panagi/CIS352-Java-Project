package com.company;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {

    public void login() {
        Stage s = new Stage();
        Button button = new Button();
        button.setText("Login");

        Button button2 = new Button();
        button2.setText("Sign Up");

        // set title for the stage
        s.setTitle("Kitchen Inventory Management");

        // create a textfields
        TextField a = new TextField();
        a.setPromptText("Username");
        a.setMaxWidth(200);

        PasswordField b = new PasswordField();
        b.setPromptText("Password");
        b.setMaxWidth(200);

        // create labels for the textfields
        Label loginLabel = new Label("Login");
        loginLabel.getStyleClass().add("l");

        Label l = new Label("");    //used for printing a message if username/pass dont match records
        Label usernameLabel = new Label("Username:");
        Label passlabel = new Label("Password:");

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    //start the connection
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testjava", "root", "");
                    //test for data export from DB
                    String sql = "select * from users";
                    Statement myStmt = conn.createStatement();
                    ResultSet rs = myStmt.executeQuery(sql);

                    while (rs.next()) {
                        if (rs.getString("username").equals(a.getText()) && rs.getString("Password").equals(b.getText())) {
                            l.setText("welcome " + a.getText());

                            Menu menu = new Menu();
                            menu.menu();
                            s.close();

                        } else {
                            l.setText("Your username or password is wrong");
                        }
                    }
                } catch (Exception b) {
                    System.out.print("Do not connect to DB - Error:" + b);
                }
            }
        };

        // when button is pressed
        button.setOnAction(event);

        button2.setOnAction(f -> {
            SignUp signUp = new SignUp();
            signUp.signUp();
            s.close();
        });

        HBox hBox = new HBox(20);
        hBox.getChildren().addAll(button, button2);

        VBox layout1 = new VBox(20); //it stacks every bottom into a column with 20 space
        layout1.setPadding(new Insets(170, 10, 10, 50));
        layout1.setSpacing(10);
        layout1.getChildren().addAll(usernameLabel, a, passlabel, b, l, hBox);

        VBox vbox = new VBox(20);
        vbox.getChildren().add(loginLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout1);
        borderPane.setLeft(vbox);

        // create a scene
        Scene sc = new Scene(borderPane, 600, 550);
        sc.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());

        // set the scene
        s.setScene(sc);
        s.show();

    }

}
