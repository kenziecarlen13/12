module org.week.lany {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.week.lany to javafx.fxml;
    exports org.week.lany;
    exports org.week.lany.data;
    opens org.week.lany.data to javafx.fxml;
}
