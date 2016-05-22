package gui;

import business.ConnectionController;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import plc.Greenhouse;
import plc.Error;
import java.util.List;
import java.util.Observable;
import javafx.collections.FXCollections;
import plc.PLCController;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import plc.ErrorType;
import plc.Solution;

public class GUIController implements Initializable, Observer
{
    private static GUIController instance;
    private Error error;
    private List<Greenhouse> obList;
    @FXML
    private ListView<Greenhouse> greenhouseListView;
    @FXML
    private ListView<Error> errorListView;
    @FXML
    private Label greenhouseLabel, errorLabel;
    @FXML
    private Button handleErrorButton, newErrorBtn, newSolutionBtn;
    @FXML
    private TableView<ErrorType> errorTable;
    @FXML
    private TableColumn<ErrorType, Integer> errorTableCode;
    @FXML
    private TableColumn<ErrorType, String> errorTableDescription;
    @FXML
    private TableView<Solution> solutionTable;
    @FXML
    private TableColumn<Solution, Integer> solutionTableId, solutionTableCode;
    @FXML
    private TableColumn<Solution, String> solutionTableDescritption;
    @FXML
    private TabPane pane;
    @FXML
    private Tab overviewTab, greenhouseTab, databaseTab;
    
    public static GUIController get()
    {
        if (instance == null)
        {
            instance = new GUIController();
        }
        return instance;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        GUIController.get().obList = FXCollections.observableArrayList(PLCController.get().getGreenhouses());
        GUIController.get().greenhouseListView.getItems().addAll(GUIController.get().obList);
        GUIController.get().greenhouseListView.getSelectionModel().selectedItemProperty().addListener(listener ->
        {
            GUIController.get().errorListView.setItems(FXCollections.observableArrayList(PLCController.get().getErrors(GUIController.get().greenhouseListView.getSelectionModel().getSelectedItem())));
        });
    }

    public void removeError(Error e)
    {
        errorListView.getItems().remove(e);
    }
    
    public void setError(Error e)
    {
        error = e;
    }

    public Error getCurrentError()
    {
        return error;
    }

    @FXML
    private void handleErrorAction(ActionEvent event)
    {
        if (GUIController.get().errorListView.getSelectionModel().getSelectedItem() != null)
        {
            GUIController.get().setError(GUIController.get().errorListView.getSelectionModel().getSelectedItem());
            
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLHandleError.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex)
            {
                Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void updateTables(ActionEvent event)
    {
        ConnectionController.get().update(errorTable.getItems(), solutionTable.getItems());
        errorTable.setItems(FXCollections.observableArrayList(ConnectionController.get().getErrorTypes().values()));
        solutionTable.setItems(FXCollections.observableArrayList(ConnectionController.get().getSolutions()));
    }
    
    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof Error)
        {
            GUIController.get().errorListView.setItems(FXCollections.observableArrayList(PLCController.get().getErrors(GUIController.get().greenhouseListView.getSelectionModel().getSelectedItem())));
        } else if (arg instanceof Greenhouse)
        {
            GUIController.get().greenhouseListView.setItems(FXCollections.observableArrayList(PLCController.get().getGreenhouses()));
        }
    }
}
