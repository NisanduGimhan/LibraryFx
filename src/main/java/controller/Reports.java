package controller;

import db.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Reports {

    @FXML
    void btnBookReportOnAction(ActionEvent event) {
        String reportPath = "D:/JavaFX/LibraryFX/src/main/resources/Reports/borrowFormbook.jrxml";
        showReportWithFloatingCircle(reportPath, event);
    }

    @FXML
    void btnFineCompletedOnAction(ActionEvent event) {
        String reportPath = "D:/JavaFX/LibraryFX/src/main/resources/Reports/allfines.jrxml";
        showReportWithFloatingCircle(reportPath, event);
    }

    @FXML
    void btnMemberReportOnAction(ActionEvent event) {
        String reportPath = "D:/JavaFX/LibraryFX/src/main/resources/Reports/Member.jrxml";
        showReportWithFloatingCircle(reportPath, event);
    }

    @FXML
    void btnReturnCompletedOnAction(ActionEvent event) {
        String reportPath = "D:/JavaFX/LibraryFX/src/main/resources/Reports/borrowallrecords.jrxml";
        showReportWithFloatingCircle(reportPath, event);
    }

    @FXML
    void btnBookDetailCompletedOnAction(ActionEvent event) {
        String reportPath = "D:/JavaFX/LibraryFX/src/main/resources/Reports/books.jrxml";
        showReportWithFloatingCircle(reportPath, event);
    }


    private void showReportWithFloatingCircle(String reportPath, ActionEvent event) {

        Stage loadingStage = new Stage(StageStyle.TRANSPARENT);


        ProgressIndicator progressIndicator = new ProgressIndicator();
        StackPane root = new StackPane(progressIndicator);

        root.setStyle("-fx-padding: 10; -fx-background-color: transparent;");


        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        loadingStage.setScene(scene);


        Window currentWindow = ((Node) event.getSource()).getScene().getWindow();
        loadingStage.initOwner(currentWindow);


        loadingStage.show();


        Thread loadThread = new Thread(() -> {
            try {
                JasperDesign design = JRXmlLoader.load(reportPath);
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        null,
                        DBConnection.getInstance().getConnection()
                );


                Platform.runLater(() -> {
                    loadingStage.close();
                    JasperViewer.viewReport(jasperPrint, false);
                });
            } catch (Exception e) {

                Platform.runLater(() -> {
                    loadingStage.close();
                    showErrorDialog("Failed to load report", e.getMessage());
                });
            }
        });
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
