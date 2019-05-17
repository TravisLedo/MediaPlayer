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
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
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
import javax.swing.event.ChangeEvent;
import java.nio.file.Paths;
import java.util.Random;
import javafx.scene.control.CheckBox;

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
int currentSongIndex;

@FXML
String name;
int nameIndex;
int maxIndex;

@FXML
Label nameLabel;
@FXML
Label timeLabel;
    
@FXML
Slider volumeSlider;

@FXML
Slider durationSlider;

@FXML
CheckBox shuffleCheckbox;

@FXML
CheckBox repeatCheckbox;




String temp;

Duration currentTime;
double maxTime;


      int totalFrames;
      long timeInMillis;


        Duration previousTime;





    MediaPlayer mediaPlayer;
    FileChooser fileChooser;
    Song currentSong;
    int currentSongCursor;
    boolean isPlaying;
    boolean hasSong;
    double volume = 1;
    
    
    boolean isShuffled;
    boolean isRepeat;

    
    
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


    boolean seeking; //to stop the slider from moving when user is trying to move it

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        
           
        while(isPlaying)
        {
            timeLabel.setText(mediaPlayer.getCurrentTime().toString());
        }



              isPlaying = false;
             // nameList.setPlaceholder(new Label("No content"));
              nameLabel.setText("Happy Jam'n!");
              

              
              
              
              //double clickinga  song on the list plays the song
      songList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    @Override
    public void handle(MouseEvent click) {

        if (click.getClickCount() == 2) {
           Song selectedFromList = songList.getSelectionModel().getSelectedItem(); //get highlighted list item
                play(selectedFromList.getName(),selectedFromList.getPath());
                                currentSongIndex = songList.getSelectionModel().getSelectedIndex();
          }
    }
});
    
    
              
              
              
              //change volume when slider is moved
volumeSlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                
                try{
                ChangeVolume((double) arg0.getValue());
                }
                catch(Exception e) {
                }

            }
        });
              
           volumeSlider.setValue(volume*100); //set initial volume to max
        
    

 
           
           
           //Shuffle check box initialize and listen
           shuffleCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){

                isShuffled = true;

            }else{

              isShuffled = false;

            }
        }
    });
           
           
           
           
           
           //Repeat check box initialize and listen
           repeatCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){

                isRepeat = true;
            shuffleCheckbox.setDisable(true);
            }else{

              isRepeat = false;
            shuffleCheckbox.setDisable(false);

            }
        }
    });

           
           
           
           

    }    



    
    
    
    
    @FXML
    private void playButton() {
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
         currentSongIndex = songList.getSelectionModel().getSelectedIndex();

       //  System.out.println(currentSongIndex);

        Media song = new Media(new File(path).toURI().toString());
        

        mediaPlayer = new MediaPlayer(song);
        

        
            mediaPlayer.setOnReady(new Runnable() { //run everything here once media is finished loading

           @Override
        public void run() {

        mediaPlayer.play();
        
        imagePlay = new Image("file:src/musicplayer/images/PauseButton.png"); //change to pause image while playing
        playButtonImage.setImage(imagePlay);

        nameLabel.setText(name);
        isPlaying = true;
        hasSong = true;
         
        maxTime = song.getDuration().toSeconds();
        currentTime = mediaPlayer.getCurrentTime();
         
        durationSlider.setMax(maxTime); //set slider to be porportional to the duration of the song
                   
       // System.out.println(maxTime);
        
        
        //make the slider move with the duration
    mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
        
        
                if(!seeking){ //only move the slider automatically when the user is not clicking on it.
                durationSlider.setValue(newValue.toSeconds());
                
        
                }
                
               
                timeLabel.textProperty().set("" + formatDuration(newValue));
                
                
                
    });
    
    
    
    
    
                 //Check for song ended and play next
                   mediaPlayer.setOnEndOfMedia(() -> {
                       
                       if(isRepeat)
                       {
                         mediaPlayer.stop();
                       mediaPlayer.seek(Duration.seconds(0));
                         mediaPlayer.play();

                       }
                       else{
                             //if shuffle is on
                       if(isShuffled)
                       {
                       playRandom();
  
                       }
                       else{
                       playNext();

                       }
                       }
                       
                     
                       
                      });
    
    
    
    
    
    
    
    
    
    
               //when mouse is down, stop the slider from moving.
               durationSlider.setOnMousePressed((MouseEvent mouseEvent) -> {

                   seeking = true;
                                      


        
    });
               
               
               
               //Let the slider move freely again and play the song at the new slider position that the player moved it.
               durationSlider.setOnMouseReleased((MouseEvent mouseEvent) -> {

                  seeking = false;

        mediaPlayer.seek(Duration.seconds(durationSlider.getValue()));

        
    });
          



        }//end run function
        
        
        
    }); //end runnable check
            
            
    }
            
            
     
    
    
    private void pause()
    {
        imagePlay = new Image("file:src/musicplayer/images/PlayButton.png");
        playButtonImage.setImage(imagePlay);
        mediaPlayer.pause();
        isPlaying = false;

         currentTime = mediaPlayer.getCurrentTime();
    
    }
    
        
    private void resume()
    {
      mediaPlayer.setStartTime(currentTime);  
      mediaPlayer.play();
    imagePlay = new Image("file:src/musicplayer/images/PauseButton.png");
    playButtonImage.setImage(imagePlay);
    isPlaying = true;

    }

    
    @FXML
    private void playNext()
    {  

        mediaPlayer.stop();
        
        
     currentSongIndex ++;
     
     int numberOfSongs = songList.getItems().size();
     
     if(currentSongIndex<numberOfSongs){
  
     songList.getSelectionModel().select(currentSongIndex);
     play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());
     }
     else{
         currentSongIndex = 0;
          songList.getSelectionModel().selectFirst();
          play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());

     }
    
    }
    
    
    
    
    
    @FXML
    private void playPrevious()
    {  
       
        
        
      
      //If clicked back twice, go previous. Otherqwise restart song.
      if(mediaPlayer.getCurrentTime().toSeconds()<.5)
      {

       mediaPlayer.stop();

      

          int numberOfSongs = songList.getItems().size();

     if(currentSongIndex!=0){ //if at the beginning of playlist, go to last song
       currentSongIndex --;
     
     songList.getSelectionModel().select(currentSongIndex);
     play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());
     }
     else{ //if not at beginning of playlist, go back one song
         currentSongIndex = numberOfSongs;
          songList.getSelectionModel().selectLast();
          play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());

     }
      
      }
      else //didnt click twice, only restart song
      {
             mediaPlayer.stop();
                 mediaPlayer.seek(Duration.seconds(0));
             mediaPlayer.play();

      }
      
    }

    
    


        
    
    
    
    @FXML
    private void playRandom()
    {  

      mediaPlayer.stop();
        
      
           int numberOfSongs = songList.getItems().size();

           
            int random = (int)(Math.random() * numberOfSongs);

      
      
     currentSongIndex = random;
     
  
     songList.getSelectionModel().select(currentSongIndex);
     play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());

    
    }
    
    
    
    
    
        
    
    
    @FXML
    private void addSongs(ActionEvent event){
                fileChooser = new FileChooser();
                fileChooser.setTitle("Add Songs");
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a Song", "*.mp3", "*.wav");
                fileChooser.getExtensionFilters().add(filter);
                List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
                
                if (selectedFiles != null) {
                    for (int i = 0 ; i < selectedFiles.size();i++){
                     //songList.getItems().add(selectedFiles.get(i).getName());
                    // songList.getItems().add(selectedFiles.get(i).getAbsolutePath());
                    
                    
                    Song selectedSong = new Song(selectedFiles.get(i).getName(), selectedFiles.get(i).getAbsolutePath());
                    
                   //   nameList.getItems().add(selectedSong.getName());
                      songList.getItems().add(selectedSong);
                      
                    
                      
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
    



    
    
    
        @FXML
    private void ChangeVolume(double volume)
    {  

        this.volume = volume/100;
        mediaPlayer.setVolume(this.volume);
        
        //System.out.println("new volume: " + this.volume);
    }
    
    
    
    
    
    
    //format time
    public static String formatDuration(Duration duration) {
    long seconds = (long)duration.toSeconds();
    long absSeconds = Math.abs(seconds);
    String positive = String.format(
        "%02d:%02d:%02d",
        absSeconds / 3600,
        (absSeconds % 3600) / 60,
        absSeconds % 60);
    return seconds < 0 ? "-" + positive : positive;
}
    
    
    
}
