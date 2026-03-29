/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: Principal
 * Program's function: Start the whole application
 *************************************************************** */

// ==========================================
// STANDARD JAVA IMPORTS
// ==========================================
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

// ==========================================
// JAVAFX IMPORTS
// ==========================================
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

// ==========================================
// PROJECT ARCHITECTURE IMPORTS
// ==========================================
import model.Link;
import model.Network;
import model.NetworkTopology;
import model.Packet;
import model.Router;
import view.BaseView;
import view.GameView;
import view.MenuView;
import controller.BaseController;
import controller.GameController;
import controller.MenuController;
import util.TopologyReader;

public class Principal extends Application {

  /*********************************************************************
   * Method: main
   * Function: initialization entry point for compiler.
   * Parameters: args execution arguments.
   * Return: void
   ******************************************************************* */
  public static void main(String[] args){
    launch(args);
  }

  /*********************************************************************
   * Method: start
   * Function: builds the windowing application graph context.
   * Parameters: stage primary framework window instance.
   * Return: void
   ******************************************************************* */
  @Override
  public void start(Stage stage){
    stage.setTitle("Shortest Path Routing");
    Platform.setImplicitExit(true);
    stage.setResizable(false);
    stage.getIcons().add(new Image(getClass().getResource("/img/application_icon.png").toExternalForm()));
    stage.setOnCloseRequest(e -> {
      System.exit(0);
    });

    NetworkTopology topology = TopologyReader.loadTopology("backbone.txt");

    MenuController menuController = new MenuController(stage, topology);
    menuController.setInterface(menuController.getView());
    stage.show();
  }

}