/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......: 09/04/2026
 * Program's name....: TopologyReader
 * Program's function: Reads the topology from the backbone.txt
 ************************************************************** */

package util;

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

public class TopologyReader {

  /*********************************************************************
   * Method: loadTopology
   * Function: dynamically parses text file parameters to memory.
   * Parameters: filePath is the local string path of text file.
   * Return: object of a NetworkTopology
   ******************************************************************* */
  public static NetworkTopology loadTopology(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

      String firstLine = br.readLine();
      if (firstLine == null) return null;

      int numRouters = Integer.parseInt(firstLine.replace(";", "").trim());

      List<int[]> linksList = new ArrayList<>();
      String line;

      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) continue;

        String[] parts = line.split(";");
        if (parts.length >= 3) {
          int r1 = Integer.parseInt(parts[0].trim());
          int r2 = Integer.parseInt(parts[1].trim());
          int weight = Integer.parseInt(parts[2].trim());
          linksList.add(new int[]{r1, r2, weight});
        }
      }

      int[][] nodes = new int[linksList.size()][3];
      for (int i = 0; i < linksList.size(); i++) {
        nodes[i] = linksList.get(i);
      }

      return new NetworkTopology(numRouters, nodes);

    } catch (IOException | NumberFormatException e) {
      System.out.println("Error reading the backbone file: " + e.getMessage());
      return null;
    }
  }
}