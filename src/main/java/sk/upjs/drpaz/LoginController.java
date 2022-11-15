package sk.upjs.drpaz;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sk.upjs.drpaz.storage.DaoFactory;
import sk.upjs.drpaz.storage.Employee;

public class LoginController {
	private Stage stage;

	@FXML
	private TextField loginTextField;
	@FXML
	private PasswordField passwordField;

	@FXML
	private Label wrongCredentialsLabel;

	public LoginController(Stage stage) {
		this.stage = stage;
		
	}

	@FXML
	void onLoginButtonClick(ActionEvent event) {
		// TODO ??????
				Employee currentUser = DaoFactory.INSTANCE.getEmployeeDao().getByLoginAndPassword(loginTextField.getText(),
						passwordField.getText());
				if (currentUser == null) {
					passwordField.setStyle("-fx-border-color: red");
					wrongCredentialsLabel.setVisible(true);

				} else {
					System.out.println("GOOD");
				}
	}

	@FXML
	void onKeyPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			onLoginButtonClick(null);
		}
		if (event.getCode().equals(KeyCode.TAB)) {
			if (stage.getScene().getFocusOwner() == loginTextField)
				passwordField.requestFocus();

			else
				loginTextField.requestFocus();
		}
		
	}

	@FXML
	void onActionTextField(ActionEvent event) {
		passwordField.setStyle("-fx-border-color: black");
		wrongCredentialsLabel.setVisible(false);
	}

	void inputChangedTextField(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				passwordField.setStyle("-fx-border-style: none;");
				wrongCredentialsLabel.setVisible(false);

			}
		});
	}

	@FXML
	void initialize() {
		passwordField.setFocusTraversable(false);
		inputChangedTextField(passwordField);
		inputChangedTextField(loginTextField);
	}

}
