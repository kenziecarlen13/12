package org.week.keshi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class LoginController {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    @FXML
    public void initialize() {
        // Pastikan tabel users dan 3 user default sudah ada di database
        KeshiDBConnection.createUsersTable();
    }

    @FXML
    protected void onKeyPressEvent(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            btnLoginClick();
        }
    }

    @FXML
    protected void btnLoginClick() throws IOException {
        String inputUser = txtUsername.getText();
        String inputPass = txtPassword.getText();
        Alert alert;

        // PATH 1: Admin Login (kredensial statis)
        if (inputUser.equals(ADMIN_USERNAME) && inputPass.equals(ADMIN_PASSWORD)) {
            KeshiSessionManager.getInstance().setCurrentUser(inputUser);
            KeshiSessionManager.getInstance().setUsername(inputUser);
            KeshiSessionManager.getInstance().setRole("Admin");

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information");
            alert.setContentText("Login success!! Welcome, Admin.");
            alert.showAndWait();
            Apps.setRoot("keshi-inventory-view", "KESHI Promotor Panel", false);

        // PATH 2: User Login (dari database)
        } else if (KeshiDBConnection.validateUser(inputUser, inputPass)) {
            KeshiSessionManager.getInstance().setCurrentUser(inputUser);
            KeshiSessionManager.getInstance().setUsername(inputUser);
            KeshiSessionManager.getInstance().setRole("User");

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information");
            alert.setContentText("Login success!! Welcome, " + inputUser + ".");
            alert.showAndWait();
            Apps.setRoot("keshi-inventory-view", "KESHI Promotor Panel", false);

        // PATH 3: Login gagal
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Login failed!! Please check again.");
            alert.showAndWait();
            txtUsername.requestFocus();
        }
    }

    @FXML
    protected void btnRegisterClick() {
        String inputUser = txtUsername.getText();
        String inputPass = txtPassword.getText();
        Alert alert;

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Warning");
            alert.setContentText("Username dan Password tidak boleh kosong!");
            alert.showAndWait();
            return;
        }

        if (KeshiDBConnection.registerUser(inputUser, inputPass)) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information");
            alert.setContentText("Registrasi berhasil! Silakan login.");
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Registrasi gagal! Username mungkin sudah terdaftar.");
            alert.showAndWait();
        }
    }
}
