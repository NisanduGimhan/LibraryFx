package controller;

import dto.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.ServiceFactory;
import service.custom.UserService;
import utill.ServiceType;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    UserService service = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnMoveToRegisterpage(ActionEvent event) throws IOException {
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/SignUpForm.fxml"))));
        stage1.setTitle("SmartLibraryX");
        stage1.setResizable(false);
        stage1.show();

        Stage currentStage = (Stage) txtEmail.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnOnActionLoginClick(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        Stage loadingStage = new Stage(StageStyle.TRANSPARENT);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        StackPane root = new StackPane(progressIndicator);
        root.setStyle("-fx-padding: 10; -fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        loadingStage.setScene(scene);
        loadingStage.initOwner(txtEmail.getScene().getWindow());
        loadingStage.show();

        Thread loginThread = new Thread(() -> {
            User user = service.validateUser(email, password);

            Platform.runLater(() -> {
                loadingStage.close();
                if (user != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardForm.fxml"));
                        Stage stage1 = new Stage();
                        stage1.setScene(new Scene(loader.load()));
                        stage1.setTitle("SmartLibraryX");
                        stage1.setResizable(false);
                        stage1.show();
                        ((Stage) txtEmail.getScene().getWindow()).close();
                    } catch (IOException e) {
                        showErrorDialog("Failed to load Dashboard", e.getMessage());
                    }
                } else {
                    showErrorDialog("Login Failed", "Invalid Username or Password!");
                }
            });
        });
        loginThread.setDaemon(true);
        loginThread.start();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
