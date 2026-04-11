/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......: 09/04/2026
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
import javafx.scene.shape.Line;
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

  private ImageView router1, router2, router3, router4, router5, router6, router7, router8;
  private Button startButton, changeButton;
  private TextArea packetCounterBox;
  private Label router1Label, router2Label, router3Label, router4Label, router5Label, router6Label, router7Label, router8Label;
  private Label link1Label, link2Label, link3Label, link4Label, link5Label, link6Label, link7Label, link8Label, link9Label, link10Label, link11Label;

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
    router1Label = createLabel("R1 IS TENTATIVE", 120, 22.5, 10.0, 350.0);

    router2 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router2, 229.6, 70.0);
    addElementTo(router2, layout);
    router2Label = createLabel("R2 IS TENTATIVE", 120, 22.5, 196.6, 156.2);

    router3 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router3, 907.3, 70.0);
    addElementTo(router3, layout);
    router3Label = createLabel("R3 IS TENTATIVE", 110.0, 22.5, 878.1, 156.2);

    router4 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router4, 1098.4, 262.1);
    addElementTo(router4, layout);
    router4Label = createLabel("R4 IS TENTATIVE", 120.0, 22.5, 1070.0, 350.0);

    router5 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router5, 420.7, 261.1);
    addElementTo(router5, layout);
    router5Label = createLabel("R5 IS TENTATIVE", 120.0, 22.5, 392.2, 350.0);

    router6 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router6, 716.2, 261.1);
    addElementTo(router6, layout);
    router6Label = createLabel("R6 IS TENTATIVE", 120.0, 22.5, 683.2, 350.0);

    router7 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router7, 232.8, 452.4);
    addElementTo(router7, layout);
    router7Label = createLabel("R7 IS TENTATIVE", 120.0, 22.5, 196.6, 538.6);

    router8 = createImageViewElement("/router_icon.png", 63.1, 80.2);
    applyPosition(router8, 910.5, 452.4);
    addElementTo(router8, layout);
    router8Label = createLabel("R8 IS TENTATIVE", 120.0, 22.5, 878.1, 538.6);

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

    link1Label = createLabel("[∞, -]", 59.9, 22.5, 70.0, 161.4);
    link2Label = createLabel("[∞, -]", 59.9, 22.5, 570.0, 56.6);
    link3Label = createLabel("[∞, -]", 59.9, 22.5, 70.0, 445.6);
    link4Label = createLabel("[∞, -]",59.9, 22.5, 411.6, 423.1);
    link5Label = createLabel("[∞, -]",59.9, 22.5, 411.6, 174.3);
    link6Label = createLabel("[∞, -]",59.9, 22.5, 570.0, 520.7);
    link7Label = createLabel("[∞, -]",59.9, 22.5, 1070.1, 445.6);
    link8Label = createLabel("[∞, -]",59.9, 22.5, 1070.1, 161.4);
    link9Label = createLabel("[∞, -]",59.9, 22.5, 570.0, 228.9);
    link10Label = createLabel("[∞, -]",59.9, 22.5, 746.5, 423.1);
    link11Label = createLabel("[∞, -]",59.9, 22.5, 743.2, 174.3);
  }

  // -------------------------------------------------------------------
  // HIGHER-LEVEL UI UPDATERS AND THEIR HELPERS
  // -------------------------------------------------------------------

  /*********************************************************************
   * Method: updateRouterLabel
   * Function: Changes the color and text of the router's label
   ******************************************************************* */
  public void updateRouterLabel(int routerId, String state) {
    Label target = getRouterLabel(routerId);
    if (target != null) {
      if (state.equals("PERMANENT")) {
        target.setText("R" + routerId + " IS " + state);
        target.setStyle("-fx-text-fill: #2ecc71; -fx-background-color: #2c3e50; -fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-border-color: #2ecc71; -fx-border-radius: 5px;");
      } else if (state.equals("SUCCESS")) {
        target.setText(state);
        target.setStyle("-fx-text-fill: #f1c40f; -fx-background-color: #2c3e50; -fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-border-color: #f1c40f; -fx-border-radius: 5px;");
      }
    }
  }

  /*********************************************************************
   * Method: getRouterLabel
   * Function: Internal helper to map an ID to its specific Label.
   ******************************************************************* */
  public Label getRouterLabel(int routerId) {
    switch (routerId) {
      case 1: return router1Label; case 2: return router2Label;
      case 3: return router3Label; case 4: return router4Label;
      case 5: return router5Label; case 6: return router6Label;
      case 7: return router7Label; case 8: return router8Label;
      default: return null;
    }
  }

  /*********************************************************************
   * Method: updateLinkLabel
   * Function: Updates the [distance, predecessor] text under the links
   ******************************************************************* */
  public void updateLinkLabel(int rA, int rB, int distance, int predecessor) {
    Label target = getLinkLabel(rA, rB);
    if (target != null) {
      target.setText("[" + distance + ", R" + predecessor + "]");
    }
  }

  /*********************************************************************
   * Method: getLinkLabel
   * Function: Internal helper to map a cable connection to its Label.
   ******************************************************************* */
  public Label getLinkLabel(int rA, int rB) {
    if ((rA == 1 && rB == 2) || (rA == 2 && rB == 1)) return link1Label;
    if ((rA == 2 && rB == 3) || (rA == 3 && rB == 2)) return link2Label;
    if ((rA == 1 && rB == 7) || (rA == 7 && rB == 1)) return link3Label;
    if ((rA == 7 && rB == 5) || (rA == 5 && rB == 7)) return link4Label;
    if ((rA == 2 && rB == 5) || (rA == 5 && rB == 2)) return link5Label;
    if ((rA == 7 && rB == 8) || (rA == 8 && rB == 7)) return link6Label;
    if ((rA == 4 && rB == 8) || (rA == 8 && rB == 4)) return link7Label;
    if ((rA == 3 && rB == 4) || (rA == 4 && rB == 3)) return link8Label;
    if ((rA == 6 && rB == 5) || (rA == 5 && rB == 6)) return link9Label;
    if ((rA == 6 && rB == 8) || (rA == 8 && rB == 6)) return link10Label;
    if ((rA == 6 && rB == 3) || (rA == 3 && rB == 6)) return link11Label;
    return null;
  }

  // -------------------------------------------------------------------
  // INDEPENDENT DRAWING METHODS
  // -------------------------------------------------------------------

  /*********************************************************************
   * Method: drawLink
   * Function: Draws the white/red lines between two visual router nodes
   ******************************************************************* */
  public void drawLink(ImageView rA, ImageView rB, String state) {
    if (rA != null && rB != null) {
      Line line = new Line();

      line.setStartX(rA.getLayoutX() + (63.1 / 2));
      line.setStartY(rA.getLayoutY() + (80.2 / 2));
      line.setEndX(rB.getLayoutX() + (63.1 / 2));
      line.setEndY(rB.getLayoutY() + (80.2 / 2));
      line.setStrokeWidth(4.0);

      if (state.equals("EXPLORE")) {
        line.setStyle("-fx-stroke: white; -fx-stroke-dash-array: 10 10;");
      } else if (state.equals("CONFIRM")) {
        line.setStyle("-fx-stroke: #e74c3c;");
      }

      addElementTo(line, layout);
      bringRoutersToFront();
    }
  }

  /*********************************************************************
   * Method: bringRoutersToFront
   * Function: secures visibility of assets against moving graphics.
   ******************************************************************* */
  public void bringRoutersToFront() {
    router1.toFront(); router2.toFront(); router3.toFront(); router4.toFront();
    router5.toFront(); router6.toFront(); router7.toFront(); router8.toFront();
  }

  /*********************************************************************
   * Method: createNewRouter
   * Function: instantiates dynamic router node for UI view.
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
   ******************************************************************* */
  public ImageView createNewPacket(double x, double y){
    ImageView packetGraphic = createImageViewElement("/packet_icon.png", 38.5, 32.2);
    applyPosition(packetGraphic, x, y);
    addElementTo(packetGraphic, layout);
    return packetGraphic;
  }

  // -------------------------------------------------------------------
  // PUBLIC GETTERS
  // -------------------------------------------------------------------

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