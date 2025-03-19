package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {

    public Button logoutButton;
    @FXML
    private AnchorPane changepage;

    @FXML
    private BarChart chartCatogory;

    @FXML
    private Label lblBookTotal;

    @FXML
    private Label lblBookTotal1;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblMemberTotal;

    @FXML
    private Label lblOverDueBkTotal;

    @FXML
    private Label lblTime;

    @FXML
    void btnBookOnAction(ActionEvent event) throws IOException {

        URL resource = this.getClass().getResource("/view/BookForm.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);
    }

    @FXML
    void btnBorrowinRecordOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/BorrowRecordForm.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/HomeForm.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();  // Close current window

        // Optional: Load and show login screen
        try {
            URL resource = this.getClass().getResource("/view/LoginForm.fxml");
            assert resource!=null;
            Parent load = FXMLLoader.load(resource);
            this.changepage.getChildren().clear();
            this.changepage.getChildren().add(load);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnMemberOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/MembersForm.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);

    }

    @FXML
    void btnReportOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/Reports.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL resource = this.getClass().getResource("/view/HomeForm.fxml");
        assert resource!=null;
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);
    }

    @FXML
    void btnReturnReocrdOnAction(ActionEvent actionEvent) throws IOException{
        URL resource = this.getClass().getResource("/view/ReturnRecordForm.fxml");
        assert resource!=null;
        Parent load = FXMLLoader.load(resource);
        this.changepage.getChildren().clear();
        this.changepage.getChildren().add(load);

    }
}
