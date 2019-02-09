import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    public static final int TILE_SIZE = 256;
    public static final double ROOT_LonDPP = (ROOT_LRLON-ROOT_ULLON)/TILE_SIZE;

    public Rasterer() {

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        String[][] grid = new String[][]{};
        double ul_lon = 0;
        double ul_lat = 0;
        double lr_lon = 0;
        double lr_lat = 0;
        int depth =0;
        Boolean isSuccess = false;



        results.put("render_grid",grid);
        results.put("raster_ul_lon",ul_lon);
        results.put("raster_ul_lat",ul_lat);
        results.put("raster_lr_lon",lr_lon);
        results.put("raster_lr_lat",lr_lat);
        results.put("depth",depth);
        results.put("query_success",isSuccess);
        return results;
    }

    private static int getDepth(double ullon, double lrlon, double w){
        double lonDPP = (lrlon -ullon)/w;
        double depth = Math.ceil(Math.log(ROOT_LonDPP/lonDPP) / Math.log(2));
        if(depth < 0) depth = 0;
        else if(depth>7) depth = 7;
        return (int)depth;
    }

    private static double[] getRaster(double ullon, double ul_lat, double lrlon, double lr_lat, double w){
        double lonCellSize = (ROOT_LRLON - ROOT_ULLON)/(getDepth(ullon,lrlon,w) + 1);
        double latCellSize = (ROOT_ULLAT - ROOT_LRLAT)/(getDepth(ullon,lrlon,w) + 1);
        int ulx = (int) Math.floor((ullon - ROOT_ULLON) / lonCellSize);
        int uly = (int) Math.floor((ROOT_ULLAT - ul_lat) / latCellSize);
        int lrx = (int) Math.floor((lrlon - ROOT_ULLON) / lonCellSize);
        int lry = (int) Math.floor((ROOT_ULLAT - lr_lat) / lonCellSize);

        return new double[]{ulx, uly, lrx, lry};
    }

    public static void main(String[] args){
        double lrlon = -122.29705810546875;
        double ullon = -122.2998046875;

        System.out.println(getDepth(-122.2998046875, -122.29705810546875, 1000000));
        System.out.println(Math.log(ROOT_LonDPP/((lrlon -ullon)/256)));
        System.out.println(ROOT_LonDPP);
    }

}
