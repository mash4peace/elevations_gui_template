import java.sql.*;
import java.util.ArrayList;

/**
 * Created by we4954cp on 11/9/2016.
 */
public class DB {


    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/geography";     //Connection string – where's the database?
    private static final String USER = "clara";   //TODO replace with your username
    private static final String PASSWORD = "clara";   //TODO replace with your password
    private static final String TABLE_NAME = "elevations";
    private static final String PLACE_COL = "place";
    private static final String ELEV_COL = "elev";


    DB() {

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }
    }

    void createTable() {

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            //You should have already created a database via terminal/command prompt

            //Create a table in the database, if it does not exist already
            //Can use String formatting to build this type of String from constants coded in your program
            //Don't do this with variables with data from the user!! That's what ParameterisedStatements are, and that's for queries, updates etc. , not creating tables.
            // You shouldn't make database schemas from user input anyway.
            String createTableSQLTemplate = "CREATE TABLE IF NOT EXISTS %s (%s VARCHAR (100), %s DOUBLE)";
            String createTableSQL = String.format(createTableSQLTemplate, TABLE_NAME, PLACE_COL, ELEV_COL);

            statement.executeUpdate(createTableSQL);
            System.out.println("Created elevations table");

            statement.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }


    void addRecord(Elevation elevation)  {

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            String addElevationSQL = "INSERT INTO " + TABLE_NAME + " VALUES ( ? , ? ) " ;
            PreparedStatement addElevationPS = conn.prepareStatement(addElevationSQL);
            addElevationPS.setString(1, elevation.place);
            addElevationPS.setDouble(2, elevation.elevation);

            addElevationPS.execute();

            System.out.println("Added elevation record for place " + elevation);

            addElevationPS.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }

    }


    ArrayList<Elevation> fetchAllRecords() {

        ArrayList<Elevation> allRecords = new ArrayList();

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            String selectAllSQL = "SELECT * FROM " + TABLE_NAME;
            ResultSet rsAll = statement.executeQuery(selectAllSQL);

            while (rsAll.next()) {
                String place = rsAll.getString(PLACE_COL);
                double elevation = rsAll.getDouble(ELEV_COL);
                Elevation elevationRecord = new Elevation(place, elevation);
                allRecords.add(elevationRecord);
            }

            rsAll.close();
            statement.close();
            conn.close();

            return allRecords;    //If there's no data, this will be empty

        } catch (SQLException se) {
            se.printStackTrace();
            return null;  //since we have to return something.
        }
    }


}
