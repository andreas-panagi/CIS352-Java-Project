package com.company;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu {

    public void menu() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Kitchen Inventory Manager");

        //Create Buttons
        Button button = new Button();
        button.setText("Inventory");
        button.setId("inventory");

        Button button1 = new Button();
        button1.setText("Users Manager");
        button1.setId("users");

        Button button3 = new Button();
        button3.setText("Log Out");
        button3.setId("logout");

        //Create Labels
        Label l = new Label("Main Menu");
        l.getStyleClass().add("l");
        Label a = new Label("Make an Option");
        a.setId("option");

        //Create the actions for each button
        button.setOnAction(e -> {
            Table tb = new Table();
            tb.table();
            primaryStage.close();
        });

        button1.setOnAction(e -> {
            TableUsers users = new TableUsers();
            users.tableUsers();
            primaryStage.close();

        });

        button3.setOnAction(e -> {
            Login login = new Login();
            login.login();
            primaryStage.close();
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(button, button1);
        hbox.setPadding(new Insets(170, 10, 10, 350));
        hbox.setSpacing(20);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(l, a);

        VBox vbox2 = new VBox();
        vbox2.setPadding(new Insets(10, 10, 10, 10));
        vbox2.getChildren().addAll(button3);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vbox);
        borderPane.setCenter(hbox);
        borderPane.setBottom(vbox2);

        Scene scene = new Scene(borderPane, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
