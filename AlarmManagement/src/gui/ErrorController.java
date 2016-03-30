package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import plc.Solution;
import storage.CRUDController;

public class ErrorController implements Initializable
{
    @FXML
    private TextArea displayError;
    @FXML
    private TextArea commentArea;
    @FXML
    private Button solveErrorBtn;
    @FXML
    private ChoiceBox<Solution> solutionBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //solutionBox.getItems().addAll(GUIController.get().getCurrentError().getSolutions());
        //displayError.setText(GUIController.get().getCurrentError().toString());
    }    

    @FXML
    private void solveError(ActionEvent event)
    {
        CRUDController.get().updateError(GUIController.get().getCurrentError().getId(), solutionBox.getSelectionModel().getSelectedItem().getId(), commentArea.getText());
        
    }    
}
