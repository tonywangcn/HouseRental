/*
* AdminController.java
* Programmer: Yongtao Wang
* v 1.01
* Controller for Admin
*/

package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import application.Main;
import models.AdminModel;
import models.ClientModel;
public class AdminController implements Initializable {

	ClientModel cm;
	AdminModel am;
	static String userName;

	@FXML
	private Pane unpayedRentalPanel;
	@FXML
	private Label unpayedlabel;
	@FXML
	private Pane allRentalPanel;
	@FXML
	private Label allpayedlabel;
	@FXML
	private Label adminPanelWelcome;
	@FXML
	private TextField txtRentalUserId;
	@FXML
	private TextField txtRentalId;
	@FXML
	private TextField txtUserPaymentStatus;
	@FXML
	private Label unPayedUserIds;
	@FXML
	private Label userPaymentStatus;
	@FXML
	private Label allRentalRecords;

	public AdminController() {
		cm = new ClientModel();
		am = new AdminModel();
	}

	public void initialize(URL location, ResourceBundle resources) {
		adminPanelWelcome.setText( "Welcome! Admin " + userName+" !" );
		unpayedRentalPanel.setVisible(false);
		allRentalPanel.setVisible(false);
	}

	public static void setUsername(String username) {
		userName = username;
	}

	public void viewUnpayedRentals() {
		adminPanelWelcome.setVisible(false);
		allRentalPanel.setVisible(false);
		unpayedRentalPanel.setVisible(true);
	}

	public void viewAllRentals() {
		adminPanelWelcome.setVisible(false);
		unpayedRentalPanel.setVisible(false);
		allRentalPanel.setVisible(true);
	}
	public void askClientToPay() {
		try {
			String rs = am.askClientToPay(Integer.parseInt(this.txtRentalUserId.getText()));
			unpayedlabel.setText(rs);
		} catch (NumberFormatException e) {
			unpayedlabel.setText("Please type in a valid number");
		}


	}
	public void delRental() {
		try {
			String rs = am.delRental( Integer.parseInt( this.txtRentalId.getText() ) );
			allpayedlabel.setText(rs);
		} catch (NumberFormatException e) {
			allpayedlabel.setText("Please type in a valid number");
		}
	}
	public void getUnpayedUserIds() {
		String result = "Get Unpayed User Id:";
		result = result + am.getUnpayedUserIds();
		unPayedUserIds.setText(result);

	}
	public void checkUserPaymentStatus() {
		String result = "Check User Payment Status: ";
		try {
			int userid = Integer.parseInt( txtUserPaymentStatus.getText() );
			result = result + am.checkUserPaymentStatus(userid);
			userPaymentStatus.setText(result);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			userPaymentStatus.setText("Please type in a valid number");

		}

	}
	public void getAllRentalRecords() {
		String result = "";
		result = result + am.getAllRentalRecords();
		allRentalRecords.setText(result);
	}
	public void logout() {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Login");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}

}