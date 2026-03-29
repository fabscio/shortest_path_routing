/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: MenuController
 * Program's function: Control the elements from the menu interface.
 *************************************************************** */

package controller;

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

public class MenuController extends BaseController {

  private final MenuView view;           // Interface associated with the menu
  private final NetworkTopology topology;// Parsed configuration of the network
  private int selectedVersion = 1;       // Version of routing logic selected

  /*********************************************************************
   * Method: MenuController
   * Function: constructor for the main menu controller.
   * Parameters: stage is the window, topology is the network data.
   * Return: object of a MenuController
   ******************************************************************* */
  public MenuController(Stage stage, NetworkTopology topology) {
    super(stage);
    this.topology = topology;
    this.view = new MenuView();
    setupInteractions();
  }

  /*********************************************************************
   * Method: setupInteractions
   * Function: binds button clicks and interactions to logical actions.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  @Override
  protected void setupInteractions() {

    view.getVersionOneButton().setOnAction(e -> selectVersion(1));
    view.getVersionTwoButton().setOnAction(e -> selectVersion(2));
    view.getVersionThreeButton().setOnAction(e -> selectVersion(3));
    view.getVersionFourButton().setOnAction(e -> selectVersion(4));

    view.getChangeButton().setOnAction(e -> {
      view.getVersionOneButton().setVisible(true);
      view.getVersionTwoButton().setVisible(true);
      view.getVersionThreeButton().setVisible(true);
      view.getVersionFourButton().setVisible(true);
      this.selectedVersion = 1;
    });

    view.getAboutButton().setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("About Algorithm");
      alert.setHeaderText("Algorithm Version " + selectedVersion);

      String description = "";

      switch (selectedVersion) {
        case 1:
          description = "Each packet that arrives in a router is sent to every network interface of this router.";
          break;
        case 2:
          description = "Each packet that arrives in a router is sent to every network interface of this router but the one that it arrived from.";
          break;
        case 3:
          description = "Each packet that arrives in a router is sent to every network interface of this router but the one that it arrived from. And each router checks the TTL to decide if the packet should be send forward.";
          break;
        case 4:
          description = "The source router puts a sequence number in each packet and each router has a list per source router telling which sequence numbers originating at that source have already been seen.\n" +
            "When a packet comes in, the router checks it on the list, if it finds, it is not flooded and is discarded, if not, it is flooded.";
          break;
      }

      alert.setContentText(description);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.showAndWait();
    });

    view.getStartButton().setOnAction(e -> {
      try {
        int transmitterId = view.getTransmissorChoiceBox().getValue();
        int receptorId = view.getReceptorChoiceBox().getValue();

        if (transmitterId == receptorId) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Invalid Route");
          alert.setHeaderText("Same Router Selected");
          alert.setContentText("The transmitter and receptor cannot be the same router. Please choose two different routers.");
          alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
          alert.showAndWait();
          return;
        }

        int ttl = Integer.parseInt(view.getTtlInput().getText());
        setInterface(new GameController(stage, transmitterId, receptorId, ttl, topology, selectedVersion).getView());

      } catch (NumberFormatException ex) {
        System.out.println("Invalid TTL entered. Defaulting to 10.");
        int transmitterId = view.getTransmissorChoiceBox().getValue();
        int receptorId = view.getReceptorChoiceBox().getValue();

        if (transmitterId == receptorId){
          return;
        }

        setInterface(new GameController(stage, transmitterId, receptorId, 10, topology, selectedVersion).getView());
      }
    });
  }

  /*********************************************************************
   * Method: selectVersion
   * Function: updates internal version state and hides unselected buttons.
   * Parameters: version is the chosen algorithm integer.
   * Return: void
   ******************************************************************* */
  private void selectVersion(int version) {
    this.selectedVersion = version;
    view.getVersionOneButton().setVisible(version == 1);
    view.getVersionTwoButton().setVisible(version == 2);
    view.getVersionThreeButton().setVisible(version == 3);
    view.getVersionFourButton().setVisible(version == 4);
  }

  /*********************************************************************
   * Method: getView
   * Function: retrieves the managed interface view.
   * Parameters: none.
   * Return: object of a BaseView
   ******************************************************************* */
  @Override
  public BaseView getView(){
    return this.view;
  }
}