package controller;
import com.mysql.cj.x.protobuf.Mysqlx;
import dto.Book;
import dto.BorrowRecord;
import dto.Cart;
import dto.Member;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceFactory;
import service.custom.BookService;
import service.custom.BorrowRecordService;
import service.custom.MemberService;
import utill.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class BorrowRecordFormController implements Initializable {

    @FXML
    private Label lblBookId;
    @FXML
    private TableColumn colBorrowdate;

    @FXML
    private TableColumn colDueDate;

    @FXML
    private Label lblDate;

    @FXML
    private TableView tblBookCart;

    @FXML
    private TableColumn colBookIsbn;

    @FXML
    private TableColumn colMemberId;

    @FXML
    private JFXComboBox comBoxBookISBN;

    @FXML
    private JFXComboBox comBoxMemberID;

    @FXML
    private Label lblAuthor;

    @FXML
    private Label lblBookName;

    @FXML
    private Label lblBorrowId;

    @FXML
    private Label lblMemberName;


    BorrowRecordService service= ServiceFactory.getInstance().getServiceType(ServiceType.BORROWRECORD);


    @FXML
    void btnCompleteOnAction(ActionEvent event) throws SQLException {
        int bookId = Integer.parseInt(lblBookId.getText().trim());

        boolean allRecordsPlaced = true;

        for (Cart cart : cartlist) {
            BorrowRecord record = new BorrowRecord(
                    0,
                    cart.getMemberID(),
                    bookId,
                    cart.getBorrowDate(),
                    cart.getDueDate(),
                    null
            );

            boolean isPlaced = service.placeBookBorrowRecord(record);
            if (!isPlaced) {
                allRecordsPlaced = false;
            }
        }

        if (allRecordsPlaced) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "All book borrow records have been placed successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "There was an error placing one or more book borrow records.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void btnDeleteFromCartOnAction(ActionEvent event) {
        Cart selectedItem = (Cart) tblBookCart.getSelectionModel().getSelectedItem();

        if (selectedItem != null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to remove the book from the cart?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()&&result.get()==ButtonType.YES) {
                cartlist.remove(selectedItem);
                tblBookCart.refresh();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a book from the table to delete.").show();
        }
    }

    @FXML
    void btnReturnBookOnAction(ActionEvent event) {

    }

    ObservableList<Cart> cartlist = FXCollections.observableArrayList();
    @FXML
    void btnnAddToTableOnAction(ActionEvent event) {
        if (cartlist.size() >= 3) {
            new Alert(Alert.AlertType.INFORMATION, "Cart Limit Reached! " +
                    "You can only add up to 3 books in the cart.").show();
            return;
        }

        String borrowDate = lblDate.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate dueDate = LocalDate.parse(borrowDate, formatter).plusDays(14);
        String dueDateString = dueDate.format(formatter);

        String bookISBN = comBoxBookISBN.getValue().toString();
        Integer memberID = Integer.parseInt(comBoxMemberID.getValue().toString());

        cartlist.add(new Cart(
                bookISBN,
                memberID,
                lblDate.getText(),
                dueDateString
        ));

        tblBookCart.setItems(cartlist);
        comBoxMemberID.setDisable(true);
    }

    private  void loadDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(date);

        lblDate.setText(f.format(date));
    }
    private void setMembers(){
        List<Integer> allMembers = service.getAllMembers();
        ObservableList<Integer> list = FXCollections.observableArrayList(allMembers);
        comBoxMemberID.setItems(list);
    }

    private void setAllBooks(){
        List<String> allBooks = service.getAllBooks();
        ObservableList<String> list = FXCollections.observableArrayList(allBooks);
        comBoxBookISBN.setItems(list);
        System.out.println(comBoxBookISBN.getValue());
    }

    private void loadMembervalueToLables(Integer id){
        MemberService serviceMember= ServiceFactory.getInstance().getServiceType(ServiceType.MEMBER);
        Member member = serviceMember.searchMember(id);

        lblMemberName.setText(member.getName());


    }

    private void loadBooDetailsToLables(String id){
        BookService serviceMember= ServiceFactory.getInstance().getServiceType(ServiceType.BOOK);
        Book bk = serviceMember.searchBook((id));

        lblBookName.setText(bk.getTitle());
        lblAuthor.setText(bk.getAuthor());
        lblBookId.setText(String.valueOf(bk.getBookId()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colBookIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colBorrowdate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        loadDate();
        setMembers();
        setAllBooks();

        comBoxBookISBN.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue!=null){
                loadBooDetailsToLables((String) newValue);
            }

        }));

        comBoxMemberID.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                loadMembervalueToLables((Integer) newValue);
            }
        });


    }
}
