package controller;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import dto.Category;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.BookService;
import service.custom.MemberService;
import utill.ServiceType;
import javafx.scene.text.Font;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HomeFormController implements Initializable {

    @FXML
    private AnchorPane changepage;

    @FXML
    private BarChart<String, Number> chartCatogory;

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

    BookService serviceBook= ServiceFactory.getInstance().getServiceType(ServiceType.BOOK);
    MemberService serviceMember= ServiceFactory.getInstance().getServiceType(ServiceType.MEMBER);

    private  void loadDateAndTime(){
        Date date= new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(date);

        lblDate.setText(f.format(date));
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime cTime=LocalTime.now();
            lblTime.setText(
                    cTime.getHour() + ":" + cTime.getMinute() + ":" + cTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void setDueDate(){
        int i = serviceBook.setDeuDateValueToCard();
        lblOverDueBkTotal.setText(String.valueOf(i));
    }

    private void displayTotalBook(){
        int count = serviceBook.setValueToCard();
        lblMemberTotal.setText(String.valueOf(count));
    }

    private void displayTotalMembers(){
        int count = serviceMember.setValueToCard();
        lblBookTotal.setText(String.valueOf(count));
    }


    private void loadChartData() {
        List<Category> categoriesList = serviceBook.getCategories();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Categories");


        for (Category category : categoriesList) {
            series.getData().add(new XYChart.Data<>(category.getCategoryName(), category.getCategoryID()));
        }


        chartCatogory.getData().clear();
        chartCatogory.getData().add(series);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        displayTotalBook();
        displayTotalMembers();
        loadChartData();
        setDueDate();

        new FadeIn(chartCatogory).play();
        new BounceIn(chartCatogory).setSpeed(1.2).play();
    }

}
