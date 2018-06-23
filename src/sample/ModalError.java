package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalError {


    private static Stage errorStage;

    static Label erLabel=new Label();

    public static Label getErLabel() {
        return erLabel;
    }

    public static void setErLabel(String txt) {
        erLabel.setText(txt);
    }

    public static void display() {

        VBox layout= new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(20,20,20,20));


        Button btnAccept=new Button("OK");
        btnAccept.setOnAction(e->{
            errorStage.close();
        });
        errorStage = new Stage();
        errorStage.setTitle("ERROR");
        errorStage.initModality(Modality.WINDOW_MODAL);





        HBox hBox1=new HBox(erLabel);
        hBox1.setAlignment(Pos.CENTER);

        layout.getChildren().add(hBox1);
        layout.getChildren().add(btnAccept);
        Scene scene = new Scene(layout);
        errorStage.setScene(scene);
        errorStage.show();
    }
}


