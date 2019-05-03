/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import com.sun.javafx.scene.control.skin.LabeledText;
import java.io.File;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import static javafx.util.Duration.millis;

/**
 *
 * @author travis
 */
public class FXMLDocumentController implements Initializable {
    
@FXML
ListView<Song> songList = new ListView<Song>();



//@FXML
//ListView<String> nameList = new ListView<String>();

String path;
int pathIndex;

@FXML
String name;
int nameIndex;
int maxIndex;

@FXML
Label nameLabel;
@FXML
Label timeLabel;
        
String temp;


Duration currentTime;

    MediaPlayer mediaPlayer;
    FileChooser fileChooser;
    String selectedSong;
    int currentSongCursor;
    boolean isPlaying;
    boolean hasSong;

    
    

    
    
    @FXML
    ImageView playButtonImage;
    Image imagePlay;
    
    @FXML
    ImageView skipRightButtonImage;
    Image imageSkipRight;
    
    @FXML
    ImageView skipLeftButtonImage;
    Image imageSkipLeft;
    
        @FXML
    ImageView addImage;
    Image imageAdd;
    
        @FXML
    ImageView subtractImage;
    Image imageSubtract;
    
    @FXML
    ImageView eqTestImage;
    Image imageEQ;


    

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        imagePlay = new Image("file:PlayButton.png");
        imageSkipRight = new Image("file:SkipRightButton.png");
        imageSkipLeft = new Image("file:SkipLeftButton.png");
        imageEQ = new Image("file:SampleEQ.png");
        imageAdd = new Image("file:AddIcon.png");
        imageSubtract = new Image("file:SubtractIcon.png");

        addImage.setImage(imageAdd);
        subtractImage.setImage(imageSubtract);
        eqTestImage.setImage(imageEQ);
        playButtonImage.setImage(imagePlay);
        skipRightButtonImage.setImage(imageSkipRight);
        skipLeftButtonImage.setImage(imageSkipLeft);
        
        
              isPlaying = false;
             // nameList.setPlaceholder(new Label("No content"));
              nameLabel.setText("Happy Jam'n!");
              
/*
    songList.setOnMouseClicked(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
           (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0)) {

           //your code here       
            System.out.println("clicked.");

         }    
    }
});
              */
              
    }    



    
    
    
    
    @FXML
    private void playButton(ActionEvent event) {
        System.out.println("Clicked");
           Song selectedFromList;

        if(songList.getItems().isEmpty())
        {
          nameLabel.setText("Empty Playlist!");
        }
        else
        {
            if(isPlaying == false && !hasSong){ //if there is no song on pause and there is no song playing, play new song
                selectedFromList = songList.getSelectionModel().getSelectedItem(); //get highlighted list item
                
                if(selectedFromList==null){ //if no song selected, play first song
                    songList.getSelectionModel().select(0);
                    selectedFromList = songList.getSelectionModel().getSelectedItem(); //get highlighted list item
                }
                
                play(selectedFromList.getName(),selectedFromList.getPath());
                 
            }
            else if(isPlaying == false && hasSong){
                resume();
                
            }
            else if(isPlaying == true && hasSong){
                    pause();
             }
        }
    }
    
    
    
    
    private void play(String name, String path)
    {

        Media song = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(song);
        mediaPlayer.play();
        
        imagePlay = new Image("file:PauseButton.png"); //change to pause image while playing
        playButtonImage.setImage(imagePlay);

        nameLabel.setText(name);
        isPlaying = true;
        hasSong = true;
    }
    
    
    private void pause()
    {
        imagePlay = new Image("file:PlayButton.png");
        playButtonImage.setImage(imagePlay);
        mediaPlayer.pause();
        isPlaying = false;

         currentTime = mediaPlayer.getCurrentTime();
    
    }
    
        
    private void resume()
    {
      mediaPlayer.setStartTime(currentTime);  
      mediaPlayer.play();
    imagePlay = new Image("file:PauseButton.png");
    playButtonImage.setImage(imagePlay);
    isPlaying = true;

    }
    
    
    private void stop()
    {

    
    }
    
    
    @FXML
    private void playNext()
    {  
                      
                      
    
    }
    
    
    
    
    
    @FXML
    private void playPrevious()
    {  
         
    }

    
    


        
        
    
    
    @FXML
    private void addSongs(ActionEvent event){
                fileChooser = new FileChooser();
                fileChooser.setTitle("Add Songs");
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a Song (*.mp3)", "*.mp3");
                fileChooser.getExtensionFilters().add(filter);
                List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
                
                if (selectedFiles != null) {
                    for (int i = 0 ; i < selectedFiles.size();i++){
                     //songList.getItems().add(selectedFiles.get(i).getName());
                    // songList.getItems().add(selectedFiles.get(i).getAbsolutePath());
                    
                    
                    Song selectedSong = new Song(selectedFiles.get(i).getName(), selectedFiles.get(i).getAbsolutePath());
                    
                   //   nameList.getItems().add(selectedSong.getName());
                      songList.getItems().add(selectedSong);
                      
                     
                      System.out.println(""+selectedSong.getName());
                      System.out.println(""+selectedSong.getPath());
                      
                      
                    nameLabel.setText("Time to Rock Out!");
                    maxIndex = maxIndex+1;

                    }
                }
                else
                {
                 System.out.println("Cancel.");

                }
                                                

    }
    
        @FXML
    private void removeSongs(ActionEvent event){
        

            RemoveConfirmBox();

    }
    
    
    
    private void RemoveConfirmBox()
    {
Alert alert = new Alert(AlertType.CONFIRMATION);
alert.setTitle("Remove Song");
alert.setHeaderText("Are you sure you want to remove this song?");
//alert.setContentText("Are you ok with this?");

Optional<ButtonType> result = alert.showAndWait();
if (result.get() == ButtonType.OK){
               // currentSongCursor = nameList.getSelectionModel().getSelectedIndex();
          //  nameList.getItems().remove(nameIndex);
            nameIndex = nameIndex-1;
           
            maxIndex = maxIndex-1;

} else {
    // ... user chose CANCEL or closed the dialog
}


    }
    















    
}
