/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......: 09/04/2026
 * Program's name....: MenuView
 * Program's function: Create the elements of the menu interface.
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

public class MenuView extends BaseView {

  private Button startButton;
  private ChoiceBox<Integer> transmissorChoiceBox, receptorChoiceBox;
  private ObservableList<Integer> routersLabels = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8);
  private TextField ttlInput;

  /*********************************************************************
   * Method: MenuView
   * Function: constructor triggering interface configuration.
   * Parameters: none.
   * Return: object of a MenuView
   ******************************************************************* */
  public MenuView(){
    super("/img/menu/");
    setInterfaceElements();
  }

  // -------------------------------------------------------------------
  // PUBLIC GETTERS
  // -------------------------------------------------------------------

  public Button getStartButton(){ return startButton; }
  public TextField getTtlInput() { return ttlInput; }
  public ChoiceBox<Integer> getTransmissorChoiceBox() { return transmissorChoiceBox; }
  public ChoiceBox<Integer> getReceptorChoiceBox() { return receptorChoiceBox; }
  public ObservableList<Integer> getRoutersLabels() { return routersLabels; }

  // -------------------------------------------------------------------
  // PRIVATE LOW-LEVEL BUILDERS
  // -------------------------------------------------------------------

  /*********************************************************************
   * Method: setInterfaceElements
   * Function: builds and anchors all menu features.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  private void setInterfaceElements(){
    startButton = createButton("start_button.png", 182.3, 50.4, 672.9, 272.9);
    addElementTo(startButton, layout);

    transmissorChoiceBox = createIntegerChoiceBox(routersLabels, 69.0, 39.4);
    applyPosition(transmissorChoiceBox, 782.8, 364.3);
    addElementTo(transmissorChoiceBox, layout);

    receptorChoiceBox = createIntegerChoiceBox(routersLabels, 69.0, 39.4);
    applyPosition(receptorChoiceBox, 782.8, 426.8);
    addElementTo(receptorChoiceBox, layout);

    ttlInput = createTextField("10", 69.0, 32.5);
    applyPosition(ttlInput, 782.8, 497.5);
    addElementTo(ttlInput, layout);
  }
}