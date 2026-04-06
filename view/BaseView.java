/* ***************************************************************
 * Author............: Fabricio da Silva Souza
 * Registration......: 202411217
 * Beginning.........: 28/03/2026
 * Last change.......:
 * Program's name....: BaseView
 * Program's function: super class that works as base for all views
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

public abstract class BaseView {

  protected final AnchorPane layout;            // Base container for elements
  private final String imgPathPrefix;           // Path reference for graphics
  private final double NORMAL_SCALE = 1.0;      // Standard graphic size
  private final double HOVER_SCALE = 1.2;       // Expanded graphic size for interaction

  /*********************************************************************
   * Method: BaseView
   * Function: constructor for the base view properties.
   * Parameters: imgPathPrefix is the location string of assets.
   * Return: object of a BaseView
   ******************************************************************* */
  public BaseView(String imgPathPrefix){
    this.imgPathPrefix = imgPathPrefix;
    this.layout = createLayout();
  }

  /*********************************************************************
   * Method: getLayout
   * Function: retrieves the parent configuration node.
   * Parameters: none.
   * Return: Parent container
   ******************************************************************* */
  public Parent getLayout(){
    return this.layout;
  }

  /*********************************************************************
   * Method: createLayout
   * Function: builds the root pane and attaches background.
   * Parameters: none.
   * Return: AnchorPane for elements
   ******************************************************************* */
  private AnchorPane createLayout(){
    AnchorPane pane = new AnchorPane();
    addElementTo(createImageViewElement("background.png", 1200, 700), pane);
    return pane;
  }

  /*********************************************************************
   * Method: addElementTo
   * Function: appends a child node to the pane.
   * Parameters: node is the child, pane is the container.
   * Return: void
   ******************************************************************* */
  protected void addElementTo(Node node, AnchorPane pane){
    pane.getChildren().add(node);
  }

  /*********************************************************************
   * Method: createImageViewElement
   * Function: retrieves graphical assets.
   * Parameters: imagePath is the file name, width, height.
   * Return: ImageView generated asset.
   ******************************************************************* */
  protected ImageView createImageViewElement(String imagePath, double width, double height){
    Image image = new Image(getClass().getResource(imgPathPrefix + imagePath).toExternalForm(), width, height, true, true);
    return new ImageView(image);
  }

  /*********************************************************************
   * Method: createButton
   * Function: generates styled interactive buttons.
   * Parameters: imgPath is asset path, width, height, x and y positions.
   * Return: Button with properties.
   ******************************************************************* */
  protected Button createButton(String imgPath, double width, double height, double x, double y){
    Button button = new Button();
    button.setGraphic(createImageViewElement(imgPath, width, height));
    addHoverEffectTo(button);
    makeTransparent(button);
    applyPosition(button, x, y);
    return button;
  }

  /*********************************************************************
   * Method: createSlider
   * Function: generates slider input mechanism.
   * Parameters: minValue, maxValue, initialValue, width.
   * Return: Slider input item.
   ******************************************************************* */
  protected Slider createSlider(double minValue, double maxValue, double initialValue, double width){
    Slider slider = new Slider(minValue, maxValue, initialValue);
    slider.setPrefWidth(width);
    makeTransparent(slider);
    return slider;
  }

  /*********************************************************************
   * Method: createChoiceBox
   * Function: builds string dropdown menus.
   * Parameters: options is list array, width, height.
   * Return: ChoiceBox parameterized element.
   ******************************************************************* */
  protected ChoiceBox<String> createChoiceBox(ObservableList<String> options, double width, double height){
    ChoiceBox<String> choiceBox = new ChoiceBox<>(options);
    choiceBox.setPrefWidth(width);
    choiceBox.setPrefHeight(height);

    if(!options.isEmpty()){
      choiceBox.setValue(options.get(0));
    }

    return choiceBox;
  }

  /*********************************************************************
   * Method: createIntegerChoiceBox
   * Function: builds integer dropdown menus.
   * Parameters: options is list array, width, height.
   * Return: ChoiceBox parameterized element.
   ******************************************************************* */
  protected ChoiceBox<Integer> createIntegerChoiceBox(ObservableList<Integer> options, double width, double height){
    ChoiceBox<Integer> choiceBox = new ChoiceBox<>(options);
    choiceBox.setPrefWidth(width);
    choiceBox.setPrefHeight(height);

    if(!options.isEmpty()){
      choiceBox.setValue(options.get(0));
    }

    return choiceBox;
  }

  /*********************************************************************
   * Method: createTextField
   * Function: constructs input field with standard formatting.
   * Parameters: defaultText is placeholder, width, height.
   * Return: TextField configured element.
   ******************************************************************* */
  protected TextField createTextField(String defaultText, double width, double height){
    TextField textField = new TextField(defaultText);
    textField.setPrefWidth(width);
    textField.setPrefHeight(height);
    textField.setStyle("-fx-alignment: center; -fx-font-weight: bold;");
    return textField;
  }

  /*********************************************************************
   * Method: createTextArea
   * Function: constructs input area with standard formatting.
   * Parameters: defaultText is placeholder, width, height, x, y.
   * Return: TextArea configured element.
   ******************************************************************* */
  protected TextArea createTextArea(String defaultText, double width, double height, double x, double y){
    TextArea text = new TextArea(defaultText);
    text.setPrefWidth(width);
    text.setPrefHeight(height);
    text.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
    applyPosition(text, x, y);
    addElementTo(text, layout);
    return text;
  }

  protected Label createLabel(double width, double height, double x, double y) {
    Label label = new Label();
    label.setPrefWidth(width);
    label.setPrefHeight(height);
    label.setStyle("-fx-text-fill: white; -fx-background-color: #2c3e50; -fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-border-color: black; -fx-border-radius: 5px;");
    applyPosition(label, x, y);
    addElementTo(label, layout);
    return label;
  }

  protected Label createLabel(String text, double width, double height, double x, double y) {
    Label label = new Label(text);
    label.setPrefWidth(width);
    label.setPrefHeight(height);
    label.setStyle("-fx-text-fill: white; -fx-background-color: #2c3e50; -fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-border-color: black; -fx-border-radius: 5px;");
    applyPosition(label, x, y);
    addElementTo(label, layout);
    return label;
  }

  /*********************************************************************
   * Method: applyPosition
   * Function: anchors an element layout.
   * Parameters: node is object to place, x and y are locations.
   * Return: void
   ******************************************************************* */
  protected void applyPosition(Node node, double x, double y){
    AnchorPane.setLeftAnchor(node, x);
    AnchorPane.setTopAnchor(node, y);
  }

  /*********************************************************************
   * Method: addHoverEffectTo
   * Function: implements size scaling mouse interactions.
   * Parameters: node is object to hover.
   * Return: void
   ******************************************************************* */
  protected void addHoverEffectTo(Node node){
    node.setOnMouseEntered(e -> {
      scaleElement(node, HOVER_SCALE);
    });
    node.setOnMouseExited(e -> {
      scaleElement(node, NORMAL_SCALE);
    });
  }

  /*********************************************************************
   * Method: scaleElement
   * Function: actively changes component proportions.
   * Parameters: node is object to size, scale is magnitude.
   * Return: void
   ******************************************************************* */
  protected void scaleElement(Node node, double scale){
    node.setScaleX(scale);
    node.setScaleY(scale);
  }

  /*********************************************************************
   * Method: makeTransparent
   * Function: removes component standard backgrounds.
   * Parameters: component is object to strip.
   * Return: void
   ******************************************************************* */
  protected void makeTransparent(Region component){
    component.setBackground(Background.EMPTY);
  }

}