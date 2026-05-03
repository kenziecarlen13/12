package org.week.lany;

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
                connection = LanyDBConnection.getInstance();
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
        String query = "SELECT tickets.section_name, SUM(stock) AS count_kategori\n" +
                "FROM tickets\n" +
                "GROUP BY tickets.section_name;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String mark = resultSet.getString("section_name");
                int jumlah = resultSet.getInt("count_kategori");
                pieChartData.add(new PieChart.Data(mark, jumlah));
            }
            pieChart.setData(pieChartData);
            pieChart.setTitle("Jumlah Ticket berdasarkan Kategori");
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

