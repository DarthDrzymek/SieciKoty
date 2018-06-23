package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LoginController {
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPass;

    private ArrayList<String> cats =new ArrayList<>();
    private String token;

    public void onClikcSingIn(ActionEvent actionEvent) throws Exception {
        URL url = new URL("http://smieszne-koty.herokuapp.com/oauth/token"+
                "?grant_type=password&email="+
                textEmail.getText()+
                "&password="+
                textPass.getText());

        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setReadTimeout(30000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine=input.readLine())!=null)
                response.append(inputLine);
            input.close();
            JSONObject authorization= new JSONObject(response.toString());
            token= authorization.getString("access_token");
            System.out.println("Token: "+authorization.getString("access_token"));

            url = new URL("http://smieszne-koty.herokuapp.com/api/kittens?access_token="+token);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(30000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            response = new StringBuilder();
            while ((inputLine=input.readLine())!=null)
                response.append(inputLine);
            input.close();
            JSONArray kotkiJson=new JSONArray(response.toString());
            for (int i=0;i<kotkiJson.length();i++){
                String kot=kotkiJson.getJSONObject(i).toString();
                cats.add(kot);
            }
            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CatsView.fxml"));
            stageTheEventSourceNodeBelongs.setScene(new Scene((Parent) loader.load()));
            CatsController controller=loader.getController();
            controller.setCats(cats);
            controller.setToken(token);
            controller.setView();
        }catch (IOException e){
            ModalError.setErLabel("Something went wrong! :/\n" +
                    "Password or email mismatch!");
            ModalError.display();
        }





    }
}
