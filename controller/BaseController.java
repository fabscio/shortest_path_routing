/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: BaseController
 * Program's function: super class that works as base for all
 * ................... controllers.
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

public abstract class BaseController {

  protected Stage stage; // JavaFX window context

  /*********************************************************************
   * Method: BaseController
   * Function: constructor for controller base class.
   * Parameters: stage is the main window.
   * Return: object of a BaseController
   ******************************************************************* */
  public BaseController(Stage stage){
    this.stage = stage;
  }

  /*********************************************************************
   * Method: setInterface
   * Function: transitions the current window to a new view.
   * Parameters: view is the interface to render.
   * Return: void
   ******************************************************************* */
  public void setInterface(BaseView view){
    Scene scene = new Scene(view.getLayout(), 1200, 700);
    this.stage.setScene(scene);
  }

  /*********************************************************************
   * Method: setupInteractions
   * Function: abstract method to initialize event listeners.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  protected abstract void setupInteractions();

  /*********************************************************************
   * Method: getView
   * Function: abstract method to retrieve the controlled view.
   * Parameters: none.
   * Return: object of a BaseView
   ******************************************************************* */
  public abstract BaseView getView();
}