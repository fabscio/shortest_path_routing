/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: NetworkTopology
 * Program's function: Hold the topology read in the backbone.txt
 * ................... by the TopologyReader
 ************************************************************** */

package model;

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

public class NetworkTopology {

  private final int numRouters; // Total number of routers in the network
  private final int[][] nodes;  // Matrix defining connections and weights

  /*********************************************************************
   * Method: NetworkTopology
   * Function: constructor for the topology data structure.
   * Parameters: numRouters is the total count, nodes is the connection matrix.
   * Return: object of a NetworkTopology
   ******************************************************************* */
  public NetworkTopology(int numRouters, int[][] nodes) {
    this.numRouters = numRouters;
    this.nodes = nodes;
  }

  /*********************************************************************
   * Method: getNumRouters
   * Function: retrieves the total number of routers.
   * Parameters: none.
   * Return: int representing router count.
   ******************************************************************* */
  public int getNumRouters() {
    return this.numRouters;
  }

  /*********************************************************************
   * Method: getNodes
   * Function: retrieves the connection matrix.
   * Parameters: none.
   * Return: int[][] representing network links.
   ******************************************************************* */
  public int[][] getNodes() {
    return this.nodes;
  }
}