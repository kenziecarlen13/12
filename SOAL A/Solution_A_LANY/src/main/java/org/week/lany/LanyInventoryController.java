package org.week.lany;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.week.lany.data.Ticket;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class LanyInventoryController implements Initializable {
    @FXML
    private TextField txtSectionName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtStock;
    private FilteredList<Ticket> inventoryList;
    @FXML
    private TableView<Ticket> table;
    @FXML
    private TableColumn<Ticket, Integer> colId;
    @FXML
    private TableColumn<Ticket, String> colSectionName;
    @FXML
    private TableColumn<Ticket, Double> colPrice;
    @FXML
    private TableColumn<Ticket, Integer> colStock;
    @FXML
    public TextField searchBox;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnHapus;
    Ticket selectedTicket;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryList = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        table.setItems(inventoryList);
        searchBox.textProperty().addListener(
                (observableValue, oldValue, newValue) -> inventoryList.setPredicate(createPredicate(newValue))
        );
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSectionName.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        getConnection();
        createTable();
        getAllData();
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ticket>() {
            @Override
            public void changed(ObservableValue<? extends Ticket> observableValue, Ticket oldTicket, Ticket newTicket) {
                if (observableValue.getValue() != null) {
                    selectedTicket = observableValue.getValue();
                    txtSectionName.setText(observableValue.getValue().getSectionName());
                    txtPrice.setText(String.valueOf(observableValue.getValue().getPrice()));
                    txtStock.setText(String.valueOf(observableValue.getValue().getStock()));

                    // Catat klik tiket ke SessionManager (Hanya untuk User)
                    if ("User".equals(LanySessionManager.getInstance().getRole())) {
                        LanySessionManager.getInstance().incrementClick(observableValue.getValue().getSectionName());
                    }
                }
            }
        });
        bersihkan();

        // UI Protection: Disable input fields and buttons if role is 'User'
        if ("User".equals(LanySessionManager.getInstance().getRole())) {
            txtSectionName.setDisable(true);
            txtPrice.setDisable(true);
            txtStock.setDisable(true);
            btnSimpan.setDisable(true);
            btnHapus.setDisable(true);
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = LanyDBConnection.getInstance();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* Create database tables if they don't exist */
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "section_name TEXT NOT NULL,"
                + "price DOUBLE NOT NULL,"
                + "stock INTEGER NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Ticket> getObservableList() {
        return (ObservableList<Ticket>) inventoryList.getSource();
    }

    private Predicate<Ticket> createPredicate(String searchText) {
        return ticket -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsTicket(ticket, searchText);
        };
    }

    private boolean searchFindsTicket(Ticket ticket, String searchText) {
        return ticket.getSectionName().toLowerCase().contains(searchText.toLowerCase()) ||
                String.valueOf(ticket.getPrice()).contains(searchText) ||
                String.valueOf(ticket.getStock()).contains(searchText);
    }

    private void getAllData() {
        String query = "SELECT * FROM tickets";
        getObservableList().clear();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String sectionName = rs.getString("section_name");
                Double price = rs.getDouble("price");
                Integer stock = rs.getInt("stock");
                getObservableList().add(new Ticket(id, sectionName, price, stock));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bersihkan() {
        txtSectionName.clear();
        txtPrice.clear();
        txtStock.clear();
        txtSectionName.requestFocus();
        table.getSelectionModel().clearSelection();
        selectedTicket = null;
    }

    private boolean isTicketUpdated() {
        if (selectedTicket == null) return false;
        return !selectedTicket.getSectionName().equalsIgnoreCase(txtSectionName.getText()) ||
                !String.valueOf(selectedTicket.getPrice()).equalsIgnoreCase(txtPrice.getText()) ||
                !String.valueOf(selectedTicket.getStock()).equalsIgnoreCase(txtStock.getText());
    }

    @FXML
    protected void onBtnSimpanClick() {
        try {
            String sectionName = txtSectionName.getText();
            Double price = Double.parseDouble(txtPrice.getText());
            Integer stock = Integer.parseInt(txtStock.getText());
            if (isTicketUpdated()) {
                if (updateTicket(selectedTicket, new Ticket(selectedTicket.getId(), sectionName, price, stock))) {
                    new Alert(Alert.AlertType.INFORMATION, "Ticket Dirubah!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ticket gagal Dirubah!").show();
                }
            } else {
                if (addTicket(new Ticket(sectionName, price, stock))) {
                    new Alert(Alert.AlertType.INFORMATION, "Ticket Ditambahkan!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ticket gagal Ditambahkan!").show();
                }
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Price harus angka desimal, Stock harus angka bulat!").show();
        }
        bersihkan();
    }

    @FXML
    protected void onBtnGrafik() throws IOException {
        // Hanya Admin yang dapat melihat Pie Chart
        if (!"Admin".equals(LanySessionManager.getInstance().getRole())) {
            new Alert(Alert.AlertType.WARNING, "Akses ditolak! Hanya Admin yang dapat melihat Pie Chart.").show();
            return;
        }
        Apps.openViewWithModal("pie-chart-view", "Pie Chart", false);
    }

    @FXML
    public void handleClearSearchText(ActionEvent event) {
        searchBox.setText("");
        event.consume();
    }

    @FXML
    protected void onBtnHapus() {
        if (selectedTicket != null && deleteTicket(selectedTicket)) {
            new Alert(Alert.AlertType.INFORMATION, "Ticket Dihapus!").show();
            bersihkan();
        }
    }

    public boolean deleteTicket(Ticket ticket) {
        String query = "DELETE FROM tickets WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ticket.getId());
            if (ps.executeUpdate() > 0) {
                getObservableList().remove(ticket);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addTicket(Ticket ticket) {
        String queryGetNextId = "SELECT seq FROM SQLITE_SEQUENCE WHERE name = 'tickets' LIMIT 1";
        String queryInsert = "INSERT INTO tickets (section_name, price, stock) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement getNextIdStmt = connection.prepareStatement(queryGetNextId);
                 PreparedStatement insertStmt = connection.prepareStatement(queryInsert)) {
                ResultSet rs = getNextIdStmt.executeQuery();
                int nextId = rs.next() ? rs.getInt("seq") + 1 : 1;
                insertStmt.setString(1, ticket.getSectionName());
                insertStmt.setDouble(2, ticket.getPrice());
                insertStmt.setInt(3, ticket.getStock());
                if (insertStmt.executeUpdate() > 0) {
                    ticket.setId(nextId);
                    getObservableList().add(ticket);
                    connection.commit();
                    return true;
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateTicket(Ticket oldTicket, Ticket newTicket) {
        String query = "UPDATE tickets SET section_name = ?, price = ?, stock = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newTicket.getSectionName());
            ps.setDouble(2, newTicket.getPrice());
            ps.setInt(3, newTicket.getStock());
            ps.setInt(4, oldTicket.getId());
            if (ps.executeUpdate() > 0) {
                int idx = getObservableList().indexOf(oldTicket);
                getObservableList().set(idx, newTicket);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    protected void onActionMenuAbout() throws IOException {
        Apps.openViewWithModal("about-view", "About Program", false);
    }

    @FXML
    protected void onActionLogOut() throws IOException {
        LanySessionManager.getInstance().logout();
        Apps.setRoot("login-view", "LANY Ticket Promotor", false);
    }
}
