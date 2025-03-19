package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Book;
import dto.Member;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.MemberService;
import utill.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MembersFormController implements Initializable {

    @FXML
    private TableColumn colContactNo;

    @FXML
    private TableColumn colMemberID;

    @FXML
    private TableColumn colMembershipdate;

    @FXML
    private TableColumn colName;

    @FXML
    private TableView tblMembers;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private DatePicker txtDate;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtName;

    MemberService service= ServiceFactory.getInstance().getServiceType(ServiceType.MEMBER);

    @FXML
    void btnAddOnAction(ActionEvent event) throws SQLException {

        boolean isAdd= service.addMember(new Member(
                0,
                txtName.getText(),
                txtContactNo.getText(),
                txtDate.getValue()

        ));
        if (isAdd){
            new Alert(Alert.AlertType.INFORMATION,"Member Added Successfully").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Member Added Unsuccessful").show();
        }
        loadTable();
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        boolean idDeleted= service.deleteMember(Integer.valueOf(txtID.getText()));

        if (idDeleted){
            new Alert(Alert.AlertType.INFORMATION,"Member Deleted!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Member Not Found").show();
        }
        loadTable();
        clearFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Member member = service.searchMember(Integer.valueOf(txtID.getText()));

        if(member==null){
            new Alert(Alert.AlertType.ERROR, "Book not found").show();
        }else {
            txtName.setText(member.getName());
            txtContactNo.setText(member.getContactInfo());
            txtDate.setValue(member.getMembershipDate());
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Member existingmbr = service.searchMember(Integer.valueOf(txtID.getText()));

        if (existingmbr != null) {
            existingmbr.setName(txtName.getText());
            existingmbr.setContactInfo(txtContactNo.getText());
            existingmbr.setMembershipDate(txtDate.getValue());

            boolean isUpdate = service.updateMember(existingmbr);
            if (isUpdate) {
                new Alert(Alert.AlertType.INFORMATION, "Updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not updated").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Member not found").show();
        }
        loadTable();
        clearFields();
    }

    private void loadTable(){
        List<Member> mbr = service.getAll();
        ObservableList<Member> oblist = FXCollections.observableArrayList();
        oblist.addAll(mbr);
        tblMembers.setItems(oblist);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        colMembershipdate.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));

        loadTable();

        tblMembers.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldvalue, newValue) -> {
            if (newValue!=null){
                SetTextForFieldsWhenItSelected((Member) newValue);
            }
        }));
    }
    private void SetTextForFieldsWhenItSelected(Member mbr){
        txtID.setText(String.valueOf(mbr.getMemberID()));
        txtName.setText(mbr.getName());
        txtContactNo.setText(mbr.getContactInfo());
        txtDate.setValue(mbr.getMembershipDate());
    }

    private void clearFields(){
        txtName.clear();
        txtID.clear();
        txtContactNo.clear();

    }
}
