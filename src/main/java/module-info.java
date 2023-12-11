module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    opens com.brickbreakergame to javafx.fxml;
    exports com.brickbreakergame;
    exports com.brickbreakergame.screens;
    opens com.brickbreakergame.screens to javafx.fxml;
    exports com.brickbreakergame.menus;
    opens com.brickbreakergame.menus to javafx.fxml;
    exports com.brickbreakergame.managers;
    opens com.brickbreakergame.managers to javafx.fxml;
}