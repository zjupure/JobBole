package simit.org.jobbole.locationbean;

import java.util.List;

/**
 * Created by liuchun on 2016/5/23.
 */
public class FullAddress {

    private List<ShortAddr> address_components;

    private String formatted_address;

    private Geometry geometry;

    private String place_id;

    private List<String> types;

    public List<ShortAddr> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<ShortAddr> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public static class Geometry{
        Location location;

        String location_type;

        ViewPort viewport;
    }

    public static class ViewPort{
        Location northeast;

        Location southwest;
    }

    public static class Location{
        double lat;
        double lng;
    }
}
