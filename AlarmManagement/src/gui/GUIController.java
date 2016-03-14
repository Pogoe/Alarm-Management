package gui;

import business.MainController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import plc.Greenhouse;
import java.util.List;
import java.util.Observable;
import javafx.collections.FXCollections;
import plc.PLCController;
import java.util.Observer;

public class GUIController implements Initializable, Observer
{

    @FXML
    private ListView<Greenhouse> greenHouseListView;

    private static GUIController instance;
    private List<Greenhouse> obList;
    
    private GUIController()
    {
        
    }
    
    public static GUIController get()
    {
        if(instance == null)
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
        greenHouseListView.getItems().addAll(obList);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        obList.add((Greenhouse) arg);
    }
}
