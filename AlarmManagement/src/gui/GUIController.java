package gui;

import business.MainController;
import java.io.IOException;
import java.net.URL;
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
import javafx.event.EventType;
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
import javax.swing.JOptionPane;
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
    private TableView<Error> errorTable;
    @FXML
    private TableColumn<Error, Integer> errorTableCode;
    @FXML
    private TableColumn<Error, String> errorTableDescription;
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
        get();
        MainController.get().initialize();
        obList = FXCollections.observableArrayList(PLCController.get().getGreenhouses());
        greenhouseListView.getItems().addAll(obList);
        greenhouseListView.getSelectionModel().selectedItemProperty().addListener(e ->
        {
            errorListView.setItems(FXCollections.observableArrayList(PLCController.get().getErrors(greenhouseListView.getSelectionModel().getSelectedItem())));
        });
        
        
    }

    public void removeError(Error e)
    {
        errorListView.getItems().remove(e);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof Error)
        {
            errorListView.setItems(FXCollections.observableArrayList(PLCController.get().getErrors(greenhouseListView.getSelectionModel().getSelectedItem())));
        } else if (arg instanceof Greenhouse)
        {
            greenhouseListView.setItems(FXCollections.observableArrayList(PLCController.get().getGreenhouses()));
        }
    }

    public Error getCurrentError()
    {
        return error;
    }

    @FXML
    private void handleErrorAction(ActionEvent event)
    {
        if (errorListView.getSelectionModel().getSelectedItem() != null)
        {
            error = errorListView.getSelectionModel().getSelectedItem();
            String s = JOptionPane.showInputDialog("Check", "Please enter admin password");
            if (s.equals("1234"))
            {
                try
                {
                    Parent root1 = FXMLLoader.load(getClass().getResource("FXMLHandleError.fxml"));
                    Scene scene = new Scene(root1);
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
    }

    @FXML
    private void newError(ActionEvent event)
    {
    }

    @FXML
    private void newSolution(ActionEvent event)
    {
    }
}
