package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CatsController {
    @FXML
    private ImageView img;
    @FXML
    private TextField txtVotes;
    @FXML
    private Button btnNext;
    @FXML
    private Text txtName;

    private ArrayList<String> cats;
    private String token;

    private int index=0;
    private int page=1;

    public void setView(){
        if (index<cats.size()) {
            System.out.println(cats.get(index));
            JSONObject jsonObject = new JSONObject(cats.get(index));
            int votes = jsonObject.getInt("vote_count");
            String name= jsonObject.getString("name");
            String imgUrl = jsonObject.getString("url");
            Image image=new Image(imgUrl);
            img.setImage(image);
            txtVotes.setText(String.valueOf(votes));
            txtName.setText(name);
            System.out.println(index++);
        }else{
            btnNext.setDisable(true);
            try {
                page++;
                URL url = new URL("http://smieszne-koty.herokuapp.com/api/kittens?page="+page+"&access_token="+token);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(30000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine=input.readLine())!=null)
                    response.append(inputLine);
                input.close();
                JSONArray kotkiJson=new JSONArray(response.toString());
                System.out.println(response);
                cats.clear();
                for (int i=0;i<kotkiJson.length();i++){
                    String kot=kotkiJson.getJSONObject(i).toString();
                    cats.add(kot);
            }
                index=0;
                btnNext.setDisable(false);
            }catch (Exception e){
                ModalError.setErLabel("Ups... Something went wrong!");
                ModalError.display();
            }

        }

    }
    public void setCats(ArrayList<String> cats){
        this.cats=cats;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void onClickNext(ActionEvent actionEvent) {
        System.out.println("next");
        setView();
    }
}
