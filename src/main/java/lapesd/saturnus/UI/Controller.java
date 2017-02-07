package lapesd.saturnus.UI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lapesd.saturnus.simulator.SimulationController;
import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public TextArea consoleOutput;
    @FXML
    private Button startButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField numberTasks;
    @FXML
    private TextField numberSegments;
    @FXML
    private TextField numberDataNodes;
    @FXML
    private TextField blockSize;
    @FXML
    private TextField requestSize;
    @FXML
    private TextField stripeSize;
    @FXML
    private TextField stripeCount;
    @FXML
    private ChoiceBox fileType;
    @FXML
    private ChoiceBox accessPattern;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        numberTasks.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        numberSegments.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        numberDataNodes.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        blockSize.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        requestSize.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        stripeSize.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        stripeCount.addEventFilter(KeyEvent.KEY_TYPED, numericValidation());
        fileType.setItems(FXCollections.observableArrayList("File per Process", "Shared"));
        accessPattern.setItems(FXCollections.observableArrayList("Sequential", "Random"));
        startButton.setOnAction(startButtonHandler());
        clearButton.setOnAction(clearButtonHandler());
    }


    /**
     * Capture the text typed inside the text fields of the UI and add to a
     * dictionary (Map).
     * @return A Map with the integers typed on text fields.
     */
    public Map getTextFieldValues() throws NumberFormatException {
        Map<String, Integer> dict = new HashedMap();
        dict.put("numberTasks", Integer.parseInt(numberTasks.getText()));
        dict.put("numberSegments", Integer.parseInt(numberSegments.getText()));
        dict.put("numberDataNodes", Integer.parseInt(numberDataNodes.getText()));
        dict.put("blockSize", Integer.parseInt(blockSize.getText()));
        dict.put("requestSize", Integer.parseInt(requestSize.getText()));
        dict.put("stripeSize", Integer.parseInt(stripeSize.getText()));
        dict.put("stripeCount", Integer.parseInt(stripeCount.getText()));
        return dict;
    }


    // TODO: Acentos não são filtrados. Arrumar isso.
    /**
     * Simple event handler to receive a character and restrict just for
     * numbers, erasing letters.
     * @return The event handler.
     */
    private EventHandler<KeyEvent> numericValidation() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                TextField txt_TextField = (TextField)event.getSource();
                if(event.getCharacter().matches("[0-9]")) {
                    if(txt_TextField.getText().contains(".") && event.getCharacter().matches("[.]")){
                        event.consume();
                    }else if(txt_TextField.getText().length() == 0 && event.getCharacter().matches("[.]")){
                        event.consume();
                    }
                } else {
                    event.consume();
                }
            }
        };
    }

    private EventHandler<ActionEvent> startButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String fType = (String)fileType.getValue();
                    String aPattern = (String)accessPattern.getValue();
                    SimulationController.initSimulation(consoleOutput, getTextFieldValues(), fType, aPattern);
                } catch (NumberFormatException e) {
                    consoleOutput.appendText("One or more parameters have an invalid input!\n");
                } catch (IOException e) {
                    consoleOutput.appendText("IOException on class 'SimulationController'.\n");
                }
            }
        };
    }

    private EventHandler<ActionEvent> clearButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                consoleOutput.clear();
            }
        };
    }

}