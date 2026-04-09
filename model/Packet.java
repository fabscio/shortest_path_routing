/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......: 09/04/2026
 * Program's name....: Packet
 * Program's function: Store information and stamps
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

public class Packet {

  public static AtomicInteger globalPacketCount = new AtomicInteger(0);

  private final int transmitterRouterId;
  private final int sequenceNumber;
  private final int receptorRouterId;
  private int previousRouterId;
  private int ttl;

  /*********************************************************************
   * Method: Packet
   * Function: constructor of a packet.
   * Parameters: ttl is the time-to-live, transmitterRouterId is the origin,
   * ........... sequenceNumber is the unique ID, receptorRouterId is the goal.
   * Return: object of a Packet
   ******************************************************************* */
  public Packet(int ttl, int transmitterRouterId, int sequenceNumber, int receptorRouterId){
    this.transmitterRouterId = transmitterRouterId;
    this.sequenceNumber = sequenceNumber;
    this.receptorRouterId = receptorRouterId;
    this.ttl = ttl;
    globalPacketCount.incrementAndGet();
  }

  /*********************************************************************
   * Method: resetGlobalCounter
   * Function: resets the static packet counter to zero.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public static void resetGlobalCounter() {
    globalPacketCount.set(0);
  }

  /*********************************************************************
   * Method: decrementTTL
   * Function: reduces the packet's time-to-live by one.
   * Parameters: none.
   * Return: void
   ******************************************************************* */
  public void decrementTTL(){
    this.ttl--;
  }

  /*********************************************************************
   * Method: setPreviousRouterId
   * Function: registers the last router that handled this packet.
   * Parameters: routerId is the id of the previous router.
   * Return: void
   ******************************************************************* */
  public void setPreviousRouterId(int routerId){
    this.previousRouterId = routerId;
  }

  /*********************************************************************
   * Method: getTTL
   * Function: retrieves the current time-to-live.
   * Parameters: none.
   * Return: int representing the TTL.
   ******************************************************************* */
  public int getTTL(){ return this.ttl; }

  /*********************************************************************
   * Method: getSourceRouterId
   * Function: retrieves the originating router's id.
   * Parameters: none.
   * Return: int representing the origin id.
   ******************************************************************* */
  public int getSourceRouterId() { return this.transmitterRouterId; }

  /*********************************************************************
   * Method: getSequenceNumber
   * Function: retrieves the packet's sequence number.
   * Parameters: none.
   * Return: int representing the sequence number.
   ******************************************************************* */
  public int getSequenceNumber() { return this.sequenceNumber; }

  /*********************************************************************
   * Method: getDestinationRouterId
   * Function: retrieves the final destination router's id.
   * Parameters: none.
   * Return: int representing the destination id.
   ******************************************************************* */
  public int getDestinationRouterId(){ return this.receptorRouterId; }

  /*********************************************************************
   * Method: getPreviousRouterId
   * Function: retrieves the previous router's id.
   * Parameters: none.
   * Return: int representing the previous router id.
   ******************************************************************* */
  public int getPreviousRouterId(){ return this.previousRouterId; }

}