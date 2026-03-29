/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: GameView
 * Program's function: Create the elements from the game interface
 *************************************************************** */

package view;

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

public class GameView extends BaseView {

  private ImageView router1, router2, router3, router4, router5, router6, router7, router8; // UI Network Endpoints
  private Button startButton, changeButton;                                                 // UI Controls
  private TextArea packetCounterBox;                                                        // UI Monitor box
  private TextArea router1Label, router2Label, router3Label, router4Label, router5Label, router6Label, router7Label, router8Label;
  private TextArea linkLabel;

  /*********************************************************************
   * Method: GameView
   * Function: constructor triggering interface configuration.
   * Parameters: none.
   * Return: object of a GameView
   ******************************************************************* */
  public GameView(){
    super("/img/game/");
    setInterfaceElements();
  }

  /*********************************************************************
   * Method: setInterfaceElements
   * Function: builds and anchors all hardcoded game features.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  private void setInterfaceElements(){

    router1 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router1, 38.5, 262.1);
    addElementTo(router1, layout);


    router2 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router2, 229.6, 70.0);
    addElementTo(router2, layout);

    router3 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router3, 907.3, 70.0);
    addElementTo(router3, layout);

    router4 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router4, 1098.4, 262.1);
    addElementTo(router4, layout);

    router5 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router5, 420.7, 261.1);
    addElementTo(router5, layout);

    router6 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router6, 716.2, 261.1);
    addElementTo(router6, layout);

    router7 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router7, 232.8, 452.4);
    addElementTo(router7, layout);

    router8 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router8, 910.5, 452.4);
    addElementTo(router8, layout);

    startButton = createButton("/start_button.png", 182.3, 39.3, 638.3, 627.3);
    addElementTo(startButton, layout);

    changeButton = createButton("/change_button.png", 182.3, 39.4, 855.2, 627.3);
    addElementTo(changeButton, layout);

    packetCounterBox = new TextArea();
    packetCounterBox.setPrefHeight(45.9);
    packetCounterBox.setPrefWidth(69.0);
    packetCounterBox.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
    applyPosition(packetCounterBox, 508.0, 623.0);
    addElementTo(packetCounterBox, layout);

  }

  /*********************************************************************
   * Method: bringRoutersToFront
   * Function: secures visibility of assets against moving graphics.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public void bringRoutersToFront() {
    router1.toFront();
    router2.toFront();
    router3.toFront();
    router4.toFront();
    router5.toFront();
    router6.toFront();
    router7.toFront();
    router8.toFront();
  }

  /*********************************************************************
   * Method: createNewRouter
   * Function: instantiates dynamic router node for UI view.
   * Parameters: x and y position data.
   * Return: ImageView of router.
   ******************************************************************* */
  public ImageView createNewRouter(double x, double y){
    ImageView router = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router, x, y);
    addElementTo(router, layout);
    return router;
  }

  /*********************************************************************
   * Method: createNewPacket
   * Function: instantiates animating packet elements.
   * Parameters: x and y position data.
   * Return: ImageView of packet.
   ******************************************************************* */
  public ImageView createNewPacket(double x, double y){
    ImageView packetGraphic = createImageViewElement("/packet_icon.png", 38.5, 32.2);
    applyPosition(packetGraphic, x, y);
    addElementTo(packetGraphic, layout);
    return packetGraphic;
  }

  // Getters Section
  public Button getStartButton(){ return startButton; }
  public Button getChangeButton(){ return changeButton; }
  public TextArea getPacketCounterBox(){ return packetCounterBox; }
  public ImageView getRouter1() { return router1; }
  public ImageView getRouter2() { return router2; }
  public ImageView getRouter3() { return router3; }
  public ImageView getRouter4() { return router4; }
  public ImageView getRouter5() { return router5; }
  public ImageView getRouter6() { return router6; }
  public ImageView getRouter7() { return router7; }
  public ImageView getRouter8() { return router8; }

}