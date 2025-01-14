module dk.easv.moviemakers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires java.desktop;


    opens dk.easv.moviemakers to javafx.fxml;
    opens dk.easv.moviemakers.BE to javafx.base;
    exports dk.easv.moviemakers;
    //exports dk.easv.moviemakers.GUI;
    //opens dk.easv.moviemakers.GUI to javafx.fxml;
    exports dk.easv.moviemakers.GUI.Controller;
    opens dk.easv.moviemakers.GUI.Controller to javafx.fxml;
}