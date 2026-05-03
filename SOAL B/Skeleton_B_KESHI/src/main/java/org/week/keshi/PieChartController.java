package org.week.keshi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PieChartController implements Initializable {
    private Connection connection;
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getConnection();
        preparedData();
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = KeshiDBConnection.getInstance();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection error
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
                // Handle database connection closure error
            }
        }
    }

    private void preparedData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT ticket_inventory.tier_name, SUM(availability) AS count_kategori\n" +
                "FROM ticket_inventory\n" +
                "GROUP BY ticket_inventory.tier_name;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String mark = resultSet.getString("tier_name");
                int jumlah = resultSet.getInt("count_kategori");
                pieChartData.add(new PieChart.Data(mark, jumlah));
            }
            pieChart.setData(pieChartData);
            pieChart.setTitle("Jumlah TicketTier berdasarkan Kategori");
            //setting the direction to arrange the data
            pieChart.setClockwise(true);
            //Setting the length of the label line
            pieChart.setLabelLineLength(50);
            //Setting the labels of the pie chart visible
            pieChart.setLabelsVisible(true);
            //Setting the start angle of the pie chart
            pieChart.setStartAngle(180);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
    }
}

