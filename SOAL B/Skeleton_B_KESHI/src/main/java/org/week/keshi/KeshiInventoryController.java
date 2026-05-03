package org.week.keshi;

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
import org.week.keshi.data.TicketTier;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class KeshiInventoryController implements Initializable {
    @FXML
    private TextField txtTierName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtAvailability;
    private FilteredList<TicketTier> inventoryList;
    @FXML
    private TableView<TicketTier> table;
    @FXML
    private TableColumn<TicketTier, Integer> colId;
    @FXML
    private TableColumn<TicketTier, String> colTierName;
    @FXML
    private TableColumn<TicketTier, Double> colPrice;
    @FXML
    private TableColumn<TicketTier, Integer> colAvailability;
    @FXML
    public TextField searchBox;
    TicketTier selectedBooking;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryList = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        table.setItems(inventoryList);
        searchBox.textProperty().addListener(
                (observableValue, oldValue, newValue) -> inventoryList.setPredicate(createPredicate(newValue))
        );
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTierName.setCellValueFactory(new PropertyValueFactory<>("tierName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        getConnection();
        createTable();
        getAllData();
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TicketTier>() {
            @Override
            public void changed(ObservableValue<? extends TicketTier> observableValue, TicketTier oldBooking, TicketTier newBooking) {
                if (observableValue.getValue() != null) {
                    selectedBooking = observableValue.getValue();
                    txtTierName.setText(observableValue.getValue().getTierName());
                    txtPrice.setText(String.valueOf(observableValue.getValue().getPrice()));
                    txtAvailability.setText(String.valueOf(observableValue.getValue().getAvailability()));
                }
            }
        });
        bersihkan();
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = KeshiDBConnection.getInstance();
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
        String sql = "CREATE TABLE IF NOT EXISTS ticket_inventory ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tier_name TEXT NOT NULL,"
                + "price DOUBLE NOT NULL,"
                + "availability INTEGER NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<TicketTier> getObservableList() {
        return (ObservableList<TicketTier>) inventoryList.getSource();
    }

    private Predicate<TicketTier> createPredicate(String searchText) {
        return TicketTier -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsBooking(TicketTier, searchText);
        };
    }

    private boolean searchFindsBooking(TicketTier TicketTier, String searchText) {
        return TicketTier.getTierName().toLowerCase().contains(searchText.toLowerCase()) ||
                String.valueOf(TicketTier.getPrice()).contains(searchText) ||
                String.valueOf(TicketTier.getAvailability()).contains(searchText);
    }

    private void getAllData() {
        String query = "SELECT * FROM ticket_inventory";
        getObservableList().clear();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String tierName = rs.getString("tier_name");
                Double price = rs.getDouble("price");
                Integer availability = rs.getInt("availability");
                getObservableList().add(new TicketTier(id, tierName, price, availability));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bersihkan() {
        txtTierName.clear();
        txtPrice.clear();
        txtAvailability.clear();
        txtTierName.requestFocus();
        table.getSelectionModel().clearSelection();
        selectedBooking = null;
    }

    private boolean isBookingUpdated() {
        if (selectedBooking == null) return false;
        return !selectedBooking.getTierName().equalsIgnoreCase(txtTierName.getText()) ||
                !String.valueOf(selectedBooking.getPrice()).equalsIgnoreCase(txtPrice.getText()) ||
                !String.valueOf(selectedBooking.getAvailability()).equalsIgnoreCase(txtAvailability.getText());
    }

    @FXML
    protected void onBtnSimpanClick() {
        try {
            String tierName = txtTierName.getText();
            Double price = Double.parseDouble(txtPrice.getText());
            Integer availability = Integer.parseInt(txtAvailability.getText());
            if (isBookingUpdated()) {
                if (updateBooking(selectedBooking, new TicketTier(selectedBooking.getId(), tierName, price, availability))) {
                    new Alert(Alert.AlertType.INFORMATION, "TicketTier Dirubah!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "TicketTier gagal Dirubah!").show();
                }
            } else {
                if (addBooking(new TicketTier(tierName, price, availability))) {
                    new Alert(Alert.AlertType.INFORMATION, "TicketTier Ditambahkan!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "TicketTier gagal Ditambahkan!").show();
                }
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Price harus angka desimal, Availability harus angka bulat!").show();
        }
        bersihkan();
    }

    @FXML
    protected void onBtnGrafik() throws IOException {
        Apps.openViewWithModal("pie-chart-view", "Pie Chart", false);
    }

    @FXML
    public void handleClearSearchText(ActionEvent event) {
        searchBox.setText("");
        event.consume();
    }

    @FXML
    protected void onBtnHapus() {
        if (selectedBooking != null && deleteBooking(selectedBooking)) {
            new Alert(Alert.AlertType.INFORMATION, "TicketTier Dihapus!").show();
            bersihkan();
        }
    }

    public boolean deleteBooking(TicketTier TicketTier) {
        String query = "DELETE FROM ticket_inventory WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, TicketTier.getId());
            if (ps.executeUpdate() > 0) {
                getObservableList().remove(TicketTier);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addBooking(TicketTier TicketTier) {
        String queryGetNextId = "SELECT seq FROM SQLITE_SEQUENCE WHERE name = 'ticket_inventory' LIMIT 1";
        String queryInsert = "INSERT INTO ticket_inventory (tier_name, price, availability) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement getNextIdStmt = connection.prepareStatement(queryGetNextId);
                 PreparedStatement insertStmt = connection.prepareStatement(queryInsert)) {
                ResultSet rs = getNextIdStmt.executeQuery();
                int nextId = rs.next() ? rs.getInt("seq") + 1 : 1;
                insertStmt.setString(1, TicketTier.getTierName());
                insertStmt.setDouble(2, TicketTier.getPrice());
                insertStmt.setInt(3, TicketTier.getAvailability());
                if (insertStmt.executeUpdate() > 0) {
                    TicketTier.setId(nextId);
                    getObservableList().add(TicketTier);
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

    private boolean updateBooking(TicketTier oldBooking, TicketTier newBooking) {
        String query = "UPDATE ticket_inventory SET tier_name = ?, price = ?, availability = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newBooking.getTierName());
            ps.setDouble(2, newBooking.getPrice());
            ps.setInt(3, newBooking.getAvailability());
            ps.setInt(4, oldBooking.getId());
            if (ps.executeUpdate() > 0) {
                int idx = getObservableList().indexOf(oldBooking);
                getObservableList().set(idx, newBooking);
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
}

