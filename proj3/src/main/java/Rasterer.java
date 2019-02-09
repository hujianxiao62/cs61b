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
        boolean r =checkInput(params);
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid",getGrid(params,getDepth(params)));
        results.put("raster_ul_lon",getRaster(params,"ullon"));
        results.put("raster_ul_lat",getRaster(params,"ullat"));
        results.put("raster_lr_lon",getRaster(params,"lrlon"));
        results.put("raster_lr_lat",getRaster(params,"lrlat"));
        results.put("depth",getDepth(params));
        results.put("render_grid",getGrid(params,getDepth(params)));
        results.put("query_success",isSuccess(results,params));
        return results;
    }

    //get depth of the figure with right lonDPP
    private static int getDepth(Map<String, Double> params){
        double ullon = params.get("ullon");
        double lrlon = params.get("lrlon");
        double w = params.get("w");
        double lonDPP = (lrlon -ullon)/w;
        double depth = Math.ceil(Math.log(ROOT_LonDPP/lonDPP) / Math.log(2));
        if(depth < 0) depth = 0;
        else if(depth>7) depth = 7;
        return (int)depth;
    }

    //get the ul and lr index, in rang[0, 2^depth]
    private static int[] getGridXY(Map<String, Double> params){
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        int depth = getDepth(params);
        double lonCellSize = (ROOT_LRLON - ROOT_ULLON)/Math.pow(2,depth);
        double latCellSize = (ROOT_ULLAT - ROOT_LRLAT)/Math.pow(2,depth);
        int ulx = (int) Math.floor((ullon - ROOT_ULLON) / lonCellSize);
        int uly = (int) Math.floor((ROOT_ULLAT - ullat) / latCellSize);
        int lrx = (int) Math.floor((lrlon - ROOT_ULLON) / lonCellSize);
        int lry = (int) Math.floor((ROOT_ULLAT - lrlat) / latCellSize);
        int[] gridXY = new int[]{ulx, uly, lrx, lry};
        for (int i=0; i<4; i++){
        gridXY[i] = gridXY[i]<0? 0:gridXY[i];
        gridXY[i] = gridXY[i]>Math.pow(2,depth)? (int)Math.pow(2,depth):gridXY[i];
        }

        return new int[]{gridXY[0], gridXY[1], gridXY[2], gridXY[3]};
    }

    //get String[][] of figure name
    private static String[][] getGrid(Map<String, Double> params, int depth){
        int[] gridXY = getGridXY(params);
        int gridLengthX = gridXY[2] - gridXY[0] + 1;
        int gridLengthY = gridXY[3] - gridXY[1] + 1;
        String[][] grid = new String[gridLengthY][gridLengthX];
        for(int x = 0; x < gridLengthX; x++){
            for (int y = 0; y < gridLengthY; y++){
                int xx = x+gridXY[0];
                int yy = y+gridXY[1];
                grid[y][x] = "d"+depth+"_x"+xx+"_y"+yy+".png";
            }
        }
        return grid;
    }

    //get raster_ul_lon, raster_ul_lat, raster_lr_lon, raster_lr_lat
    private static double getRaster(Map<String, Double> params, String direction){
        int depth = getDepth(params);
        double cellSizeLon = (ROOT_LRLON - ROOT_ULLON)/Math.pow(2,depth);
        double cellSizeLat = (ROOT_ULLAT - ROOT_LRLAT)/Math.pow(2,depth);
        int[] gridXY = getGridXY(params);
        double ullon = ROOT_ULLON + cellSizeLon*gridXY[0];
        double ullat = ROOT_ULLAT - cellSizeLat*gridXY[1];
        double lrlon = ROOT_ULLON + cellSizeLon*(gridXY[2]+1);
        double lrlat = ROOT_ULLAT - cellSizeLat*(gridXY[3]+1);

        double value = 3.1415926; //dummy value
        switch (direction){
            case "ullon": value = ullon; break;
            case "ullat": value = ullat; break;
            case "lrlon": value = lrlon; break;
            case "lrlat": value = lrlat; break;
        }
        return value;
    }

    //check results
    private static boolean isSuccess(Map<String, Object> results, Map<String, Double> params){
        double ullon = (double) results.get("raster_ul_lon");
        double ullat = (double) results.get("raster_ul_lat");
        double lrlon = (double) results.get("raster_lr_lon");
        double lrlat = (double) results.get("raster_lr_lat");
        int depth = (int) results.get("depth");

        boolean lon = ROOT_LRLON < ullon || lrlon < ROOT_ULLON; //true while no intersection
        boolean lat = ROOT_LRLAT > ullat || lrlat > ROOT_ULLAT; //true while no intersection
        boolean bigger = ullon < lrlon && ullat > lrlat;
        boolean dep = 0<= depth && depth<=7;
        boolean input = checkInput(params);
        return !lon && !lat && bigger && dep && input;
    }

    private static boolean checkInput(Map<String, Double> params){
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");

        boolean lon = ROOT_LRLON < ullon || lrlon < ROOT_ULLON; //true while no intersection
        boolean lat = ROOT_LRLAT > ullat || lrlat > ROOT_ULLAT; //true while no intersection
        boolean bigger = ullon < lrlon && ullat > lrlat;
        boolean ww = w>0;
        boolean hh = h>0;
        return !lon && !lat && bigger && hh && ww;
    }


}
