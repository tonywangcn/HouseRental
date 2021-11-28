/*
* Main.java
* Programmer: Yongtao Wang
* v 1.01
* Main file to run when startup
*/

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ClientModel;
import models.LoginModel;

public class Main extends Application {

	public static Stage stage; // set global stage object!!!

	@Override
	public void start(Stage primaryStage) {
	
		try {
			LoginModel login = new LoginModel();
			login.initTable();
			ClientModel client = new ClientModel();
			client.initTable();

			stage = primaryStage;
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
			stage.setTitle("Login View");
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occured while inflating view: " + e);
		}
	}

	public static void main(String[] args) {

		launch(args);
	}
}
