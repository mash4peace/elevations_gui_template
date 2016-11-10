import java.util.ArrayList;

/**
 * Created by we4954cp on 11/9/2016.
 */
public class Controller {

    static GUI gui;
    static DB db;

    public static void main(String[] args) {

        Controller controller = new Controller();
        controller.startApp();

    }

    private void startApp() {

        db = new DB();
        db.createTable();
        ArrayList<Elevation> allData = db.fetchAllRecords();
        gui = new GUI(this);
        gui.setListData(allData);
    }


    ArrayList<Elevation> getAllData() {
        return db.fetchAllRecords();
    }

    void addRecordToDatabase(Elevation elevation) {
        db.addRecord(elevation);
    }


}
