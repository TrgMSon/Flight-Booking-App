package UI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import manage.CustomerBusiness;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.DatePicker;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Random;
import entity.Customer;

public class FormCustomer extends Application {
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Thông tin khách hàng");
        BorderPane titlePane = new BorderPane();
        titlePane.setCenter(title);
        BorderPane.setAlignment(titlePane, Pos.CENTER);

        VBox form = new VBox(20);
        form.setPadding(new Insets(20, 40, 20, 40));
        form.setAlignment(Pos.CENTER);

        double labelWidth = 100;

        HBox nameRow = createFormRow("Tên đầy đủ", new TextField(""), labelWidth);
        ((TextField) nameRow.getChildren().get(1)).setPromptText("như trên CCCD/hộ chiếu");

        HBox dobRow = createFormRow("Ngày sinh", new DatePicker(), labelWidth);
        ((DatePicker) dobRow.getChildren().get(1)).setPromptText("ngày / tháng / năm");

        HBox phoneRow = createFormRow("Số điện thoại", new TextField(""), labelWidth);
        ((TextField) phoneRow.getChildren().get(1)).setPromptText("đủ 10 số");

        HBox mailRow = createFormRow("Email", new TextField(""), labelWidth);

        form.getChildren().addAll(nameRow, dobRow, phoneRow, mailRow);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(50, 40, 20, 40));

        Button accept = new Button("Xác nhận");
        initActionAccept(accept, stage, form);
        Button close = new Button("Đóng");
        initActionClose(close, stage);
        
        buttonBox.getChildren().addAll(accept, close);  
        form.getChildren().add(buttonBox);

        root.setTop(titlePane);
        root.setCenter(form);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight() - 20);
        scene.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        stage.setTitle("Nhập thông tin khách hàng");
        stage.setScene(scene);
        stage.show();
    }

    // làm popup thông báo khi nhập thông tin không đúng

    public void initInform(String message) {
        Stage inform = new Stage();
        inform.setTitle("Thông báo");
        inform.initModality(Modality.APPLICATION_MODAL);
        
        BorderPane root = new BorderPane();
        VBox informBox = new VBox(20);
        Label informLabel = new Label(message);
        Button close = new Button("Đóng");
        initActionClose(close, inform);
        informBox.setAlignment(Pos.CENTER);
        informBox.getChildren().addAll(informLabel, close);
        
        root.setCenter(informBox);
        Scene scene = new Scene(root, 300, 200);
        inform.setScene(scene);
        inform.show();
    }

    public void initActionAccept(Button accept, Stage stage, VBox form) {
        accept.setFocusTraversable(false);
        accept.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                HBox box = (HBox) form.getChildren().get(0);
                String name = ((TextField) box.getChildren().get(1)).getText();
                
                box = (HBox) form.getChildren().get(1);
                LocalDate localDate = ((DatePicker) box.getChildren().get(1)).getValue();
                String dob = "";
                if (localDate != null) {
                    dob = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }

                box = (HBox) form.getChildren().get(2);
                String phone = ((TextField) box.getChildren().get(1)).getText();

                box = (HBox) form.getChildren().get(3);
                String mail = ((TextField) box.getChildren().get(1)).getText();

                Random rand = new Random();
                String customer_id = "C" + String.format("%04d", rand.nextInt(10000));
                while (CustomerBusiness.isExist(customer_id)) {
                    customer_id = "C" + String.format("%04d", rand.nextInt(10000));
                }
                
                Customer customer = new Customer(customer_id, name, dob, phone, mail);
                if (CustomerBusiness.isValid(customer)) {
                    CustomerBusiness.addCustomer(customer);
                }
                else {
                    initInform("Vui lòng nhập đủ thông tin khách hàng");
                    return;
                }

                stage.close();
            }
        });
    }

    public void initActionClose(Button close, Stage stage) {
        close.setFocusTraversable(false);
        close.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                stage.close();
            }
        });
    }

    public HBox createFormRow(String labelText, Control input, double labelWidth) {
        Label label = new Label(labelText);
        label.setMinWidth(labelWidth);
        label.setAlignment(Pos.BASELINE_LEFT);

        if (input instanceof TextField) {
            ((TextField) input).setFocusTraversable(false);
            ((TextField) input).setPrefSize(500, 40);
        } 

        else if (input instanceof DatePicker) {
            ((DatePicker) input).setFocusTraversable(false);
            ((DatePicker) input).setPrefSize(500, 40);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.BASELINE_CENTER);
        row.getChildren().addAll(label, input);

        return row;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
