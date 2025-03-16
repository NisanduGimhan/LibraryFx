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

    /**
     * Shows a tiny "floating circle" stage with a transparent background
     * while the Jasper report is compiled/filled on a background thread.
     */
    private void showReportWithFloatingCircle(String reportPath, ActionEvent event) {
        // 1) Create a transparent stage (no title bar, no background)
        Stage loadingStage = new Stage(StageStyle.TRANSPARENT);

        // 2) Just a ProgressIndicator in a StackPane
        ProgressIndicator progressIndicator = new ProgressIndicator();
        StackPane root = new StackPane(progressIndicator);
        // Optional: some padding so it’s not cramped
        root.setStyle("-fx-padding: 10; -fx-background-color: transparent;");

        // 3) Scene with transparent fill
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        loadingStage.setScene(scene);

        // 4) Place it above the current window, if you like
        Window currentWindow = ((Node) event.getSource()).getScene().getWindow();
        loadingStage.initOwner(currentWindow);

        // 5) Show the spinner
        loadingStage.show();

        // 6) Load/compile/fill the report in a background thread
        Thread loadThread = new Thread(() -> {
            try {
                JasperDesign design = JRXmlLoader.load(reportPath);
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        null,
                        DBConnection.getInstance().getConnection()
                );

                // 7) Once done, close the spinner stage and show the Jasper Viewer
                Platform.runLater(() -> {
                    loadingStage.close();
                    JasperViewer.viewReport(jasperPrint, false);
                });
            } catch (Exception e) {
                // On any error, close the loader and show an error
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
