module org.week.keshi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.week.keshi to javafx.fxml;
    exports org.week.keshi;
    exports org.week.keshi.data;
    opens org.week.keshi.data to javafx.fxml;
}
