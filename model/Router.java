/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: Router.java
 * Program's function: Receive packets and forward them to other
 * ................... routers through the link
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

public class Router implements Runnable {

  private final int id;                                            // Router's unique identifier
  private final PropertyChangeSupport support;                     // Support for UI property change events
  private final List<Link> links;                                  // List of connected network links
  private final BlockingQueue<Packet> inbox;                       // Thread-safe queue for incoming packets
  private volatile boolean isRunning = true;                       // Flag to control the main execution loop
  private final HashMap<Integer, List<Integer>> seenPacketsLedger; // Memory ledger for processed packets

  /*********************************************************************
   * Method: Router
   * Function: constructor of a router.
   * Parameters: id is the router's id and selectedVersion is the
   * ........... algorithm chosen.
   * Return: object of a Router
   ******************************************************************* */
  public Router(int id){
    this.id = id;
    this.links = new ArrayList<>();
    this.inbox = new LinkedBlockingQueue<>();
    this.support = new PropertyChangeSupport(this);
    this.seenPacketsLedger = new HashMap<>();
  }

  /*********************************************************************
   * Method: run
   * Function: main execution loop processing packets from the inbox.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  @Override
  public void run() {
    while (isRunning){
      try {
        Packet incomingPacket = inbox.take();

        if (!isRunning) {
          break;
        }

        shortestPathRouting(incomingPacket);

        Thread.sleep(10);

      } catch (Exception e){
        e.printStackTrace();
        break;
      }
    }
  }

  /*********************************************************************
   * Method: shortestPathRouting
   * Function: executes the shortest path Dijkstra's algorithm
   * Parameters: incomingPacket is the packet to process.
   * Return: void
   ******************************************************************* */
  public void shortestPathRouting(Packet incomingPacket){
    Packet clonedPacket = clonePacket(incomingPacket);

    for(int i = 0; i < links.size(); i++){
      sendPacketTo(links.get(i).getOppositeRouter(this), clonedPacket);
    }
  }

  /*********************************************************************
   * Method: clonePacket
   * Function: creates a copy of a packet and decrements its TTL.
   * Parameters: p is the original packet.
   * Return: object of a Packet
   ******************************************************************* */
  private Packet clonePacket(Packet p){
    Packet newPacket = new Packet(p.getTTL(), p.getSourceRouterId(), p.getSequenceNumber(), p.getDestinationRouterId());
    newPacket.setPreviousRouterId(p.getPreviousRouterId());
    newPacket.decrementTTL();
    return newPacket;
  }

  /*********************************************************************
   * Method: checkTTL
   * Function: verifies if a packet's Time-To-Live has expired.
   * Parameters: packet is the packet to check.
   * Return: boolean indicating if TTL is zero or less.
   ******************************************************************* */
  private boolean checkTTL(Packet packet){
    return (packet.getTTL() <= 0);
  }

  /*********************************************************************
   * Method: sendPacketTo
   * Function: forwards a packet to a destination router after a delay.
   * Parameters: destinationRouter is the target, packet is the payload.
   * Return: void
   ******************************************************************* */
  public void sendPacketTo(Router destinationRouter, Packet packet){
    if(this.id == packet.getDestinationRouterId()) {
      return;
    }

    support.firePropertyChange(String.valueOf(packet.getTTL()), this.id, destinationRouter.getId());

    new Thread(() -> {
      try {
        Thread.sleep(750);
        destinationRouter.receivePacket(packet);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  /*********************************************************************
   * Method: receivePacket
   * Function: places an incoming packet into the router's inbox queue.
   * Parameters: packet is the packet being received.
   * Return: void
   ******************************************************************* */
  public void receivePacket(Packet packet){
    this.inbox.offer(packet);
  }

  /*********************************************************************
   * Method: stopRouter
   * Function: safely terminates the router's execution thread.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public void stopRouter() {
    this.isRunning = false;
    this.inbox.offer(new Packet(0, 0, 0, 0));
  }

  /*********************************************************************
   * Method: addLink
   * Function: adds a network link to the router's connections.
   * Parameters: newLink is the connection to be added.
   * Return: void
   ******************************************************************* */
  public void addLink(Link newLink){
    this.links.add(newLink);
  }

  /*********************************************************************
   * Method: addPropertyChangeListener
   * Function: attaches a listener to monitor property changes for the UI.
   * Parameters: pcl is the listener to be attached.
   * Return: void
   ******************************************************************* */
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  /*********************************************************************
   * Method: getId
   * Function: retrieves the router's identifier.
   * Parameters: none.
   * Return: int representing the router's id.
   ******************************************************************* */
  public int getId(){
    return this.id;
  }

}