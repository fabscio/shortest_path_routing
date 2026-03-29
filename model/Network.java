/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: Network
 * Program's function: Configures the routers and links and starts
 * ................... the transmission.
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

public class Network implements Runnable{

  private final List<Link> links;         // List of all active cables in the network
  private final List<Router> routers;     // List of all active routers
  private int transmitterRouterId;        // ID of the router starting the flood
  private int receptorRouterId;           // ID of the final destination router
  private int ttl;                        // Initial Time-To-Live for genesis packets
  private int currentSequenceNumber = 1;  // Tracker for Version 4 distinct transmissions

  /*********************************************************************
   * Method: Network
   * Function: constructor to initialize the simulation network.
   * Parameters: routersAmount is the node count, nodes is the matrix,
   * ........... selectedVersion is the routing algorithm.
   * Return: object of a Network
   ******************************************************************* */
  public Network(int routersAmount, int[][] nodes, int selectedVersion){
    this.routers = configureRouters(routersAmount, selectedVersion);
    this.links = estabilishLinks(nodes);
  }

  /*********************************************************************
   * Method: run
   * Function: continuous loop to trigger packet transmissions.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  @Override
  public void run(){
    while(true){
      try{
        transmit();
      }catch(Exception e){
        e.printStackTrace();
        break;
      }
    }
  }

  /*********************************************************************
   * Method: transmit
   * Function: creates and dispatches the genesis packet.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public void transmit(){
    Packet freshPacket = new Packet(this.ttl, this.transmitterRouterId, this.currentSequenceNumber, this.receptorRouterId);
    this.currentSequenceNumber++;
    this.routers.get(this.transmitterRouterId - 1).receivePacket(freshPacket);
  }

  /*********************************************************************
   * Method: configureTransmission
   * Function: sets the parameters for the incoming transmission.
   * Parameters: initialRouterId is the origin, ttl is the packet life,
   * ........... receptorRouterId is the final destination.
   * Return: void
   ******************************************************************* */
  public void configureTransmission(int initialRouterId, int ttl, int receptorRouterId){
    this.transmitterRouterId = initialRouterId;
    this.ttl = ttl;
    this.receptorRouterId = receptorRouterId;
  }

  /*********************************************************************
   * Method: attachListenerToAllRouters
   * Function: connects the UI to the routers for animation updates.
   * Parameters: listener is the observer object.
   * Return: void
   ******************************************************************* */
  public void attachListenerToAllRouters(PropertyChangeListener listener) {
    for (Router router : routers) {
      router.addPropertyChangeListener(listener);
    }
  }

  /*********************************************************************
   * Method: shutdownNetwork
   * Function: triggers the kill switch on all active router threads.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public void shutdownNetwork() {
    for (Router router : routers) {
      router.stopRouter();
    }
  }

  /*********************************************************************
   * Method: configureRouters
   * Function: instantiates and starts threads for all routers.
   * Parameters: amount is total routers, selectedVersion is algorithm.
   * Return: List of instantiated routers
   ******************************************************************* */
  private List<Router> configureRouters(int amount, int selectedVersion){
    List<Router> initializedRouters = new ArrayList<>();

    for(int i = 0; i < amount ; i++){
      Router r = new Router(i + 1, selectedVersion);
      initializedRouters.add(r);

      Thread t = new Thread(r);
      t.setDaemon(true);
      t.start();
    }

    return initializedRouters;
  }

  /*********************************************************************
   * Method: estabilishLinks
   * Function: reads the matrix and physically connects the routers.
   * Parameters: nodes is the connection matrix.
   * Return: List of established links
   ******************************************************************* */
  private List<Link> estabilishLinks(int [][] nodes){
    List<Link> establishedLinks = new ArrayList<>();

    for(int i = 0; i < nodes.length; i++){
      Router routerK = routers.get(nodes[i][0] - 1);
      Router routerJ = routers.get(nodes[i][1] - 1);
      int weight = nodes[i][2];
      establishedLinks.add(establishLink(routerK, routerJ, weight));
    }

    return establishedLinks;
  }

  /*********************************************************************
   * Method: establishLink
   * Function: creates a single 'bidirectional connection between two nodes.
   * Parameters: routerK is the first node, routerJ is the second, weight.
   * Return: object of a Link
   ******************************************************************* */
  private Link establishLink(Router routerK, Router routerJ, int weight) throws IllegalArgumentException{
    Link newLink = new Link(routerK, routerJ, weight);
    routerK.addLink(newLink);
    routerJ.addLink(newLink);
    return newLink;
  }

}