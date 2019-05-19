package musicplayer;  
import java.io.File;  
  
import javafx.application.Application;  
import static javafx.application.Application.launch;
import javafx.scene.Group;  
import javafx.scene.Scene;  
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;  
import javafx.stage.Stage;  
public class Video extends Application  
{  
  
    
    
    //constructor
    public void StartVideo(){
        System.out.println("test");

        Stage stage = new Stage();
    
        
        String path = "/Users/travis/Desktop/test/VID_20151026_010303~2.mp4";  
          
        //Instantiating Media class  
        Media media = new Media(new File(path).toURI().toString());  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  
          
        //Instantiating MediaView class   
        MediaView mediaView = new MediaView(mediaPlayer);  
          
        //by setting this property to true, the Video will be played   
        mediaPlayer.setAutoPlay(true);  
          
        //setting group and scene   
        Group root = new Group();  
        root.getChildren().add(mediaView);  
        Scene scene = new Scene(root,500,400);  
        stage.setScene(scene);  
        stage.setTitle("Playing video");  
        stage.show();  
        
        
    }
    
    
    
    @Override  
    public void start(Stage primaryStage) throws Exception {  
 
        
        System.out.println("test");
        
        String path = "/Users/travis/Desktop/test/VID_20151026_010303~2.mp4";  
          
        //Instantiating Media class  
        Media media = new Media(new File(path).toURI().toString());  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  
          
        //Instantiating MediaView class   
        MediaView mediaView = new MediaView(mediaPlayer);  
          
        //by setting this property to true, the Video will be played   
        mediaPlayer.setAutoPlay(true);  
          
        //setting group and scene   
        Group root = new Group();  
        root.getChildren().add(mediaView);  
        Scene scene = new Scene(root,500,400);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("Playing video");  
        primaryStage.show();  
    }  
    
    
    /*
    public static void main(String[] args) {  
        launch(args);  
    }  
      */
}  