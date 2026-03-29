/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: GameController
 * Program's function: Control the elements from the game interface,
 * ................... animate the elements, and glue the network
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
public class GameController extends BaseController implements PropertyChangeListener {

  private final GameView view;                // Interface associated with the game
  private final Network network;              // Engine managing the routing algorithms
  private final NetworkTopology topology;     // Structural matrix of the nodes
  private final int transmissionRouterId;     // Source node origin
  private final int receiverRouterId;         // Final destination node
  private final int ttl;                      // Packet lifetime configuration
  private final int selectedVersion;          // Chosen algorithm version

  /*********************************************************************
   * Method: GameController
   * Function: constructor for the game controller.
   * Parameters: stage is the window, tId is source, rId is destination,
   * ........... selectedTtl is time limit, topology is graph, selectedVersion.
   * Return: object of a GameController
   ******************************************************************* */
  public GameController(Stage stage, int tId, int rId, int selectedTtl, NetworkTopology topology, int selectedVersion) {
    super(stage);
    this.view = new GameView();
    this.topology = topology;
    this.transmissionRouterId = tId;
    this.receiverRouterId = rId;
    this.ttl = selectedTtl;
    this.selectedVersion = selectedVersion;

    this.network = new Network(topology.getNumRouters(), topology.getNodes(), this.selectedVersion);

    configureNetwork(this.network);
    setupInteractions();
  }

  /*********************************************************************
   * Method: configureNetwork
   * Function: prepares the routing engine for initial transmission.
   * Parameters: network is the engine object.
   * Return: void
   ******************************************************************* */
  private void configureNetwork(Network network){
    network.attachListenerToAllRouters(this);
    network.configureTransmission(this.transmissionRouterId, this.ttl, this.receiverRouterId);
  }

  /*********************************************************************
   * Method: setupInteractions
   * Function: binds button clicks to network operations.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  @Override
  protected void setupInteractions() {
    view.getStartButton().setOnAction(e -> {
      network.transmit();
    });

    view.getChangeButton().setOnAction(e -> {
      network.shutdownNetwork();
      Packet.resetGlobalCounter();
      setInterface(new MenuController(stage, topology).getView());
    });
  }

  /*********************************************************************
   * Method: propertyChange
   * Function: observer event trigger for packet UI updates.
   * Parameters: evt contains packet transition data.
   * Return: void
   ******************************************************************* */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    int routerKId = (int) evt.getOldValue();
    int routerJId = (int) evt.getNewValue();

    Platform.runLater( () -> {
      animatePacket(getRouterIcon(routerKId), getRouterIcon(routerJId), evt.getPropertyName());
      this.view.getPacketCounterBox().setText(String.valueOf(Packet.globalPacketCount));
    });
  }

  /*********************************************************************
   * Method: animatePacket
   * Function: generates smooth movement and TTL tracking on the UI.
   * Parameters: routerK is origin, routerJ is target, ttlText is counter.
   * Return: void
   ******************************************************************* */
  private void animatePacket(ImageView routerK, ImageView routerJ, String ttlText){

    ImageView packet = view.createNewPacket(routerK.getLayoutX(), routerK.getLayoutY());
    Label ttlLabel;

    if(this.selectedVersion == 1 || this.selectedVersion == 2){
      ttlLabel = new Label("");
    } else {
      ttlLabel = new Label("TTL: " + ttlText);
    }

    ttlLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
    ttlLabel.setLayoutX(routerK.getLayoutX());
    ttlLabel.setLayoutY(routerK.getLayoutY() + 30);

    Pane parentPane = (javafx.scene.layout.Pane) packet.getParent();
    parentPane.getChildren().add(ttlLabel);

    view.bringRoutersToFront();

    KeyValue targetX = new KeyValue(packet.translateXProperty(), (routerJ.getLayoutX() - routerK.getLayoutX()));
    KeyValue targetY = new KeyValue(packet.translateYProperty(), (routerJ.getLayoutY() - routerK.getLayoutY()));
    KeyValue labelTargetX = new KeyValue(ttlLabel.translateXProperty(), (routerJ.getLayoutX() - routerK.getLayoutX()));
    KeyValue labelTargetY = new KeyValue(ttlLabel.translateYProperty(), (routerJ.getLayoutY() - routerK.getLayoutY()));

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(750),
      targetX, targetY, labelTargetX, labelTargetY
    ));

    timeline.setOnFinished(e -> {
      parentPane.getChildren().removeAll(packet, ttlLabel);
    });

    timeline.play();
  }

  /*********************************************************************
   * Method: getRouterIcon
   * Function: retrieves the visual representation for an ID.
   * Parameters: routerId is the specific node integer.
   * Return: ImageView of the router.
   ******************************************************************* */
  private ImageView getRouterIcon(int routerId){
    switch (routerId){
      case 1: return view.getRouter1();
      case 2: return view.getRouter2();
      case 3: return view.getRouter3();
      case 4: return view.getRouter4();
      case 5: return view.getRouter5();
      case 6: return view.getRouter6();
      case 7: return view.getRouter7();
      case 8: return view.getRouter8();
      default: return null;
    }
  }

  /*********************************************************************
   * Method: getView
   * Function: retrieves the managed interface view.
   * Parameters: none.
   * Return: object of a BaseView
   ******************************************************************* */
  @Override
  public BaseView getView() {
    return this.view;
  }

}