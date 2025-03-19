package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import utill.ServiceType;

import java.io.IOException;

public class SignUpFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    UserService service= ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnLogPageFromSignUpOnAction(ActionEvent event) throws IOException {

        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
        stage1.setTitle("SmartLibraryX");
        stage1.setResizable(false);
        stage1.show();

        Stage currentStage = (Stage) txtEmail.getScene().getWindow();
        currentStage.close();

    }
    @FXML
    void btnSignUpOnActon(ActionEvent event) throws IOException {
        Integer id=null;
        String email = txtEmail.getText();
        String Password = txtPassword.getText();
        String name = txtName.getText();

        User user = service.saveUser(new User(0, name, email, Password));

        System.out.println(user);

        if (user!=null){

            Stage stage1 = new Stage();
            stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
            stage1.setTitle("SmartLibraryX");
            stage1.setResizable(false);
            stage1.show();

            Stage currentStage = (Stage) txtEmail.getScene().getWindow();
            currentStage.close();
        }else {
            new Alert(Alert.AlertType.ERROR,"User Added Unsuccessful").show();
        }
    }

}
