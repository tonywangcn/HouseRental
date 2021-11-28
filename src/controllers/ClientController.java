/*
* ClientController.java
* Programmer: Yongtao Wang
* v 1.01
* controller for client
*/

package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.ClientModel;
 

public class ClientController implements Initializable {
	
	static int userid;
	ClientModel cm;
	
	/***** TABLEVIEW intel *********************************************************************/

	@FXML
	private TableView<ClientModel> rentalHistory;
	@FXML
	private TableColumn<ClientModel, Integer> id;
	@FXML
	private TableColumn<ClientModel, Integer> userId;
	@FXML
	private TableColumn<ClientModel, String> checkInAt;
	@FXML
	private TableColumn<ClientModel, String> checkOutAt;
	@FXML
	private TableColumn<ClientModel, Boolean> isPayed;
	@FXML
	private TableColumn<ClientModel, Integer> totalDay;

	@FXML
	private Label tblLabel;

	private Boolean rentalHistoryViewVisible;

	public void initialize(URL location, ResourceBundle resources) {
		id.setCellValueFactory(new PropertyValueFactory<ClientModel, Integer>("id"));
		userId.setCellValueFactory(new PropertyValueFactory<ClientModel, Integer>("userId"));
		checkInAt.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("checkInAt"));
		checkOutAt.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("checkOutAt"));
		isPayed.setCellValueFactory(new PropertyValueFactory<ClientModel, Boolean>("isPayed"));
		totalDay.setCellValueFactory(new PropertyValueFactory<ClientModel, Integer>("totalDay"));

		// auto adjust width of columns depending on their content
		rentalHistory.setColumnResizePolicy((param) -> true);
		Platform.runLater(() -> customResize(rentalHistory));

		rentalHistory.setVisible(false); // set invisible initially
		this.rentalHistoryViewVisible = false;
	}

    public void customResize(TableView<?> view) {

        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size()));
            });
        }
    }
    
	public void viewRentalHistory() throws IOException {
		if (this.rentalHistoryViewVisible) {
			rentalHistory.setVisible(false);
			this.rentalHistoryViewVisible = false;
			return;
		}
		rentalHistory.getItems().setAll(cm.getRentalHistory(userid)); // load table data from ClientModel List
		rentalHistory.setVisible(true); // set tableview to visible if not
		this.rentalHistoryViewVisible = true;
	}

	public void checkIn() {
		rentalHistory.setVisible(false); // set tableview to visible if not
		this.rentalHistoryViewVisible = false;
		String r = cm.checkIn(userid);
		if (r.length() ==0) {
			tblLabel.setText("You checked in successfully! And Got a new house!");
			return;
		}
		tblLabel.setText(r);

	}
	
	public void checkOut() {
		rentalHistory.setVisible(false); // set tableview to visible if not
		this.rentalHistoryViewVisible = false;
		String r = cm.checkOut(userid);
		if (r.length() == 0) {
			tblLabel.setText("You checkec out successfully! Have a good day!");
			return;
		}
		tblLabel.setText(r);
	}

	public void logout() {
		// System.exit(0);
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

	public void createTransaction() {

		TextInputDialog dialog = new TextInputDialog("Enter dollar amount");
		dialog.setTitle("Bank Account Entry Portal");
		dialog.setHeaderText("Enter Transaction");
		dialog.setContentText("Please enter your balance:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Balance entry: " + result.get());
			// cm.insertRecord(userid,Double.parseDouble(result.get()));
		}

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(balance -> System.out.println("Balance entry: " + balance));

	}

	public static void setUserid(int userId) {
		userid = userId;
		System.out.println("Welcome id " + userid);
	}

	public ClientController() {

		/*
		 * Alert alert = new Alert(AlertType.INFORMATION);
		 * alert.setTitle("From Customer controller");
		 * alert.setHeaderText("Bank Of IIT- Chicago Main Branch");
		 * alert.setContentText("Welcome !"); alert.showAndWait();
		 */

		cm = new ClientModel();

	}

}
