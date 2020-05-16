package com.company;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    Button button;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kitchen Inventory Manager");
        button = new Button();
        button.setText("Login");
        button.setId("button");

        Label l = new Label("Welcome");
        l.getStyleClass().add("l");

        Label none = new Label(" ");
        none.getStyleClass().add("l");

        Label programName = new Label("Kitchen Inventory Manager");

        programName.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(programName, 0.0);
        AnchorPane.setRightAnchor(programName, 0.0);
        programName.setAlignment(Pos.CENTER);
        programName.setId("programName");

        button.setOnAction(e -> {
            Login rh = new Login();
            rh.login();
            primaryStage.close();
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(programName, none, l);


        //create the layout and bring the vbox
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(button);
        borderPane.setTop(vBox);

        Scene scene = new Scene(borderPane, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
