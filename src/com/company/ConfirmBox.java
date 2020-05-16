package com.company;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//this class is used for the delete section. it lets the user to make a decision and returns the answer
public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //we are gonna block users other windows util he's done
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        //Create two buttons for user's choice
        Button yesButton = new Button("YES");
        Button noButton = new Button("NO");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        HBox forLabel = new HBox(10);
        forLabel.setPadding(new Insets(10, 10, 10, 10));

        HBox forButtons = new HBox(10);
        forButtons.setPadding(new Insets(10, 50, 10, 50));

        forLabel.getChildren().add(label);
        forButtons.getChildren().addAll(yesButton, noButton);


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(forButtons);
        borderPane.setTop(forLabel);
        Scene scene = new Scene(borderPane);
        window.setScene(scene); //we add the scene to the window
        window.showAndWait();

        return answer;
    }
}
