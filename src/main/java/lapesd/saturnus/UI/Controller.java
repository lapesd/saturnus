package lapesd.saturnus.UI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private Button startButton;
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
        numberTasks.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        numberSegments.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        numberDataNodes.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        blockSize.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        requestSize.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        stripeSize.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        stripeCount.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
        fileType.setItems(FXCollections.observableArrayList("File per Process", "Shared"));
        accessPattern.setItems(FXCollections.observableArrayList("Sequential", "Random"));
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SimulationController.initSimulation(getTextFieldValues());
                } catch (NumberFormatException e) {
                    System.out.println("One or more parameters have an invalid input!");
                } catch (IOException e) {
                    System.out.printf("IOException on class 'SimulationController'.");
                }
            }
        });
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
    public EventHandler<KeyEvent> numeric_Validation() {
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

}