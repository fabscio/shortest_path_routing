/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......: 09/04/2026
 * Program's name....: Link
 * Program's function: establish a link between two routers
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

public class Link {

  private final int weight;
  private final Router routerK;
  private final Router routerJ;

  /*********************************************************************
   * Method: Link
   * Function: constructor to establish a connection.
   * Parameters: routerK is endpoint A, routerJ is endpoint B, weight is cost.
   * Return: object of a Link
   ******************************************************************* */
  public Link(Router routerK, Router routerJ, int weight){
    this.routerK = routerK;
    this.routerJ = routerJ;
    this.weight = weight;
  }

  /*********************************************************************
   * Method: getOppositeRouter
   * Function: returns the router on the other side of the link.
   * Parameters: router is the querying node.
   * Return: object of a Router
   ******************************************************************* */
  public Router getOppositeRouter(Router router){
    return (router == this.routerJ) ? this.routerK : this.routerJ;
  }

  // -------------------------------------------------------------------
  // PUBLIC GETTERS
  // -------------------------------------------------------------------

  /*********************************************************************
   * Method: getWeight
   * Function: retrieves the connection cost.
   * Parameters: none.
   * Return: int representing the link weight.
   ******************************************************************* */
  public int getWeight() { return this.weight; }

  /*********************************************************************
   * Method: getRouterK
   * Function: retrieves the first endpoint.
   * Parameters: none.
   * Return: object of a Router
   ******************************************************************* */
  public Router getRouterK() { return this.routerK; }

  /*********************************************************************
   * Method: getRouterJ
   * Function: retrieves the second endpoint.
   * Parameters: none.
   * Return: object of a Router
   ******************************************************************* */
  public Router getRouterJ() { return this.routerJ; }

}