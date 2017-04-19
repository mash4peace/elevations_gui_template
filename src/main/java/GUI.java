import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by we4954cp on 11/9/2016.
 */

public class GUI extends JFrame {

    private JPanel mainPanel;

    private JTextField enterPlaceName;
    private JTextField enterElevation;
    private JLabel placeLabel;
    private JLabel elevationLabel;
    private JButton submitButton;
    private JButton deleteBtn;

    private JList<Elevation> allElevationsList;
    private JScrollPane allElevationsListScrollPane;
    private  DefaultListModel<Elevation> allElevationsModel;

    private Controller controller;


    GUI(Controller controller) {

        this.controller = controller;  //Store a reference to
        // the controller object.
        // Need this to make requests to the database.

        addComponents();   //Add GUI components

        //Configure the list model

        allElevationsModel = new DefaultListModel<Elevation>();
        allElevationsList.setModel(allElevationsModel);

        //and listeners - only one in this program, but put in method to keep tidy.
        addListeners();

        //Regular setup stuff for the window / JFrame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setVisible(true);

    }



    private void addListeners() {
        deleteBtn.addActionListener(new ActionListener() {
                @Override
        public void actionPerformed(ActionEvent e) {
            Elevation elevation = allElevationsList.getSelectedValue();
            if (elevation == null) {
                JOptionPane.showMessageDialog(GUI.this, "Please select an elevation to delete ?");

            } else {

                controller.deleting(elevation);
                ArrayList<Elevation> elevations = controller.getAllData();
                setListData(elevations);

            }
        }
    });



        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Read data, send message to database via controller

                String place = enterPlaceName.getText();

                if (place.isEmpty()) {
                    JOptionPane.showMessageDialog(GUI.this, "Enter a place name");
                    return;
                }
                double elev;

                try {
                    elev = Double.parseDouble(enterElevation.getText());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(GUI.this, "Enter a number for elevation");
                    return;
                }

                Elevation elevationRecord = new Elevation(place, elev);
                controller.addRecordToDatabase(elevationRecord);

                //Clear input JTextFields
                enterPlaceName.setText("");
                enterElevation.setText("");

                //and request all data from DB to update list
                ArrayList<Elevation> allData = controller.getAllData();
                setListData(allData);
            }
        });




    }

    //This does the same as the IntelliJ GUI designer.
    private void addComponents() {

        //Initialize the components
        submitButton = new JButton("Submit");
        enterPlaceName = new JTextField();
        enterElevation = new JTextField();
        placeLabel = new JLabel("Place name");
        elevationLabel = new JLabel("Elevation in meters");

        //and the JList, add it to a JScrollPane
        allElevationsList = new JList<Elevation>();
        allElevationsListScrollPane = new JScrollPane(allElevationsList);

        deleteBtn = new JButton("Delete");

        //Create a JPanel to hold all of the above
        mainPanel = new JPanel();

        //A LayoutManager is in charge of organizing components within a JPanel.
        //BoxLayout can display items in a vertical or horizontal list, (and also other configurations, see JavaDoc)
        BoxLayout layoutMgr = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layoutMgr);

        //And add the components to the JPanel mainPanel
        mainPanel.add(placeLabel);
        mainPanel.add(enterPlaceName);
        mainPanel.add(elevationLabel);
        mainPanel.add(enterElevation);
        mainPanel.add(submitButton);
        mainPanel.add(allElevationsListScrollPane);
        mainPanel.add(deleteBtn);


    }


    void setListData(ArrayList<Elevation> data) {

        //Display data in allDataTextArea
        allElevationsModel.clear();

        for (Elevation elev : data) {
            allElevationsModel.addElement(elev);
        }
    }

}
