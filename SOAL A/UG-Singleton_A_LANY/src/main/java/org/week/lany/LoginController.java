package org.week.lany;

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
        // Mempersiapkan database saat aplikasi dijalankan
        LanyDBConnection.createUsersTable();
    }

    @FXML
    protected void onKeyPressEvent(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            btnLoginClick();
        }
    }

    @FXML
    protected void btnLoginClick() throws IOException {
        // TODO: Implementasikan logika autentikasi dua arah. Jika input merupakan kredensial Admin statis, atur role menjadi 'Admin' pada SessionManager. Jika bukan, validasi melalui DBConnection dan atur role menjadi 'User'. Tampilkan Alert yang sesuai dan arahkan pengguna ke inventory-view jika berhasil.
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

        // Panggil DBConnection untuk mendaftarkan user baru
        if (LanyDBConnection.registerUser(inputUser, inputPass)) {
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
