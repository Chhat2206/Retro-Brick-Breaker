module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    opens brickGame to javafx.fxml;
    exports brickGame;
    exports brickGame.screens;
    opens brickGame.screens to javafx.fxml;
    exports brickGame.menus;
    opens brickGame.menus to javafx.fxml;
}