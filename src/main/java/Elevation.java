/**
 * Created by we4954cp on 11/9/2016.
 */
public class Elevation {

    String place;
    double elevation;

    Elevation(String p, double e) {
        place = p;
        elevation = e;
    }

    @Override
    public String toString() {
        return "Place: " + place + " is at elevation " + elevation + " meters";
    }

}
