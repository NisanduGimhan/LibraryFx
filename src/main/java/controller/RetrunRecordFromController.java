package controller;

import dto.ReturnRecord;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.ReturnRecordService;
import utill.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class RetrunRecordFromController implements Initializable {
    @FXML
    private Label lblFineCal;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblBorrowID;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblBookID;

    @FXML
    private Label lblBookId;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblFine;

    @FXML
    private Label lblMemberName;

    @FXML
    private Label lblfine;



    @FXML
    private TextField txtMemberId;
    ReturnRecordService service= ServiceFactory.getInstance().getServiceType(ServiceType.RETURNRECORD);
    @FXML
    void btnReturnCompletedOnAction(ActionEvent event) {
        try {
            LocalDate value = datePicker.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = value.format(formatter);

            boolean b = service.returnBookWithTransaction(
                    Integer.valueOf(lblBorrowID.getText()),
                    dateString,
                    Double.parseDouble(lblfine.getText())
            );

            if (b) {
                new Alert(Alert.AlertType.INFORMATION, "Return Completed").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Return not Completed").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }


    @FXML
    void btnSearchMemberIDOnAction(ActionEvent event) throws SQLException {
        ReturnRecord record = service.findById(Integer.valueOf(txtMemberId.getText()));
        System.out.println(record);
       lblBookID.setText(String.valueOf(record.getBookID()));
       lblMemberName.setText(record.getMemberName());
       lblDueDate.setText(record.getDueDate());
       lblfine.setText(String.valueOf(record.getFineAmount()));
       lblBorrowID.setText(String.valueOf(record.getBorrowID()));


        LocalDate dueDate = LocalDate.parse(record.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate currentDate = LocalDate.now();


        long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
        double fineAmount = (daysOverdue > 0) ? daysOverdue * 1 : 0;


        lblFineCal.setText(String.valueOf(fineAmount));

    }
    private  void loadDateAndTime(){
        Date date= new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(date);
        lblDate.setText(f.format(date));

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
    }
}
