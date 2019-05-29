/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import java.io.File;
import java.net.URL;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author travis
 */
public class FXMLDocumentController implements Initializable {
    
@FXML
ListView<Song> songList = new ListView<Song>();

Stage videoStage;


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



boolean isFullscreen;

Duration currentTime;
double maxTime;

boolean canBrowse = true; //use this so the user cannot click add twice.

MediaView mediaView;
    MediaPlayer mediaPlayer;
    FileChooser fileChooser;
    boolean isPlaying;
    boolean hasSong;
    double volume = 1;
    
    
    boolean isShuffled;
    boolean isRepeat;

    boolean videoOpened;
    
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
            try{
                videoOpened = false;
                videoStage.close();
                mediaPlayer.stop();
            }
            catch(Exception e)
              {
              }

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

            
            
        String fileType = mediaPlayer.getMedia().getSource();
        fileType = fileType.substring(fileType.lastIndexOf(".") + 1);
        
        if(fileType.equals("mp4"))
        {
        StartVideo();

        }
            
            
        mediaPlayer.play();
        
        imagePlay = new Image("file:src/mediaplayer/images/PauseButton.png"); //change to pause image while playing
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

          try{
        ChangeVolume((double) volumeSlider.getValue());
        }
        catch(Exception e) {
        }
                
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
                       mediaPlayer.stop();

                       playRandom();
  
                       }
                       else{
                      mediaPlayer.stop();
                       playNext();

                       }
                       }
                       
                     
                       
                      });
    
    
    
    
    
    
    
    
    
    
               //when mouse is down, stop the slider from moving.
               durationSlider.setOnMousePressed((MouseEvent mouseEvent) -> {
                   
                    pause();
                mediaPlayer.seek(Duration.seconds(durationSlider.getValue()));
                           currentTime = mediaPlayer.getCurrentTime();
                           
                   seeking = true;
                                      


        
    });
               
               
               
                //dragged
               durationSlider.setOnMouseDragged((MouseEvent mouseEvent) -> {
                   
                    pause();
                mediaPlayer.seek(Duration.seconds(durationSlider.getValue()));
                           currentTime = mediaPlayer.getCurrentTime();
                           
                   seeking = true;
                                      
        
    });
               
               
               
               //Let the slider move freely again and play the song at the new slider position that the player moved it.
               durationSlider.setOnMouseReleased((MouseEvent mouseEvent) -> {
                mediaPlayer.seek(Duration.seconds(durationSlider.getValue()));
                   currentTime = mediaPlayer.getCurrentTime();

                   resume();

                  seeking = false;


        
    });
          



        }//end run function
        
        
        
    }); //end runnable check
            
            
    }
            
            
     
    
    
    private void pause()
    {
        imagePlay = new Image("file:src/mediaplayer/images/PlayButton.png");
        playButtonImage.setImage(imagePlay);
        mediaPlayer.pause();
        isPlaying = false;

         currentTime = mediaPlayer.getCurrentTime();
    
    }
    
        
    private void resume()
    {
                String fileType = mediaPlayer.getMedia().getSource();
        fileType = fileType.substring(fileType.lastIndexOf(".") + 1);
        
                if(fileType.equals("mp4"))
        {
        StartVideo();

        }
                
      mediaPlayer.seek(currentTime);  
      mediaPlayer.play();
    imagePlay = new Image("file:src/mediaplayer/images/PauseButton.png");
    playButtonImage.setImage(imagePlay);
    isPlaying = true;

    }

    
    
    @FXML
    private void playNext()
    {  
        videoOpened = false;
        videoStage.close();
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
                  videoOpened = false;
                videoStage.close();
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

                 mediaPlayer.seek(mediaPlayer.getStartTime());
             mediaPlayer.play();

      }
      
    }

    
    


        
    
    
    
    @FXML
    private void playRandom()
    {  

                videoOpened = false;
        videoStage.close();
      mediaPlayer.stop();
        
      
           int numberOfSongs = songList.getItems().size();

           
            int random = (int)(Math.random() * numberOfSongs);

      
      
     currentSongIndex = random;
     
  
     songList.getSelectionModel().select(currentSongIndex);
     play(songList.getSelectionModel().getSelectedItem().getName(),songList.getSelectionModel().getSelectedItem().getPath());

    
    }
    
    
    
    
    
        
    
    
    @FXML
    private void addSongs(ActionEvent event){
        
        if(canBrowse){
                canBrowse = false;
                fileChooser = new FileChooser();
                fileChooser.setTitle("Add Songs");
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a Song", "*.mp3", "*.wav", "*.mp4");
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
                canBrowse = true;

                    }
                }
                else
                {
                 System.out.println("Cancel.");
                canBrowse = true;

                }
                                                
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
alert.setHeaderText("Are you sure you want to remove this file?");
//alert.setContentText("Are you ok with this?");

Optional<ButtonType> result = alert.showAndWait();
if (result.get() == ButtonType.OK){
    
             Song selectedFromList = songList.getSelectionModel().getSelectedItem(); //get highlighted list item

            songList.getItems().remove(selectedFromList);
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
    
    
    
    
    
    
    
    
    
      @FXML
      public void StartVideo(){

        //If there is no video window
        if(!videoOpened)
        {
          mediaPlayer.stop();

         videoStage = new Stage();
           videoOpened = true;
           
                  //Instantiating MediaView class   
        mediaView = new MediaView(mediaPlayer);  
          

      mediaView.setPreserveRatio(true);

        
        //setting group and scene   
          StackPane root = new StackPane();
         root.getChildren().add(mediaView);
        Scene scene = new Scene(root,500,400);  
        
        
        
            DoubleProperty width = mediaView.fitWidthProperty();
    DoubleProperty height = mediaView.fitHeightProperty();
    
    width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
    height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));  
        
    
    
        //set background to black. need both pane and scene set as black
        scene.setFill(Color.BLACK);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
        

    

    
        videoStage.setScene(scene);  
        videoStage.setTitle("Playing video");  
        videoStage.show();  

        }
        else
        {

        }
    
        if(isFullscreen){
            videoStage.setFullScreen(true); //if last video was fulscreen, make this one fullscreen too
        }

        
        
                      //double clickinga  goes fullscreen
      mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent click) { 

        if (click.getClickCount() == 2) {
            try{
                if(isFullscreen){
                videoStage.setFullScreen(false);

                isFullscreen = false;
                }
                else
                {
                videoStage.setFullScreen(true);

                isFullscreen = true;

                }

            }
            catch(Exception e)
              {
                  
              }

          }
    }
});
        
        
        
        
        // When closing the video screen, stop player
 videoStage.setOnCloseRequest((WindowEvent event1) -> {
         mediaPlayer.stop();
           imagePlay = new Image("file:src/mediaplayer/images/PlayButton.png"); //change to pause image while playing
        playButtonImage.setImage(imagePlay);       
        isPlaying = false;
         videoOpened = false;
         isFullscreen = false;
         mediaPlayer.seek(mediaPlayer.getStartTime());
    });
 
 
  
        
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
