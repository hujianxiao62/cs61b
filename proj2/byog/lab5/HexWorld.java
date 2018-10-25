package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int hexSize = 3;
    private static final int hexWidth = 3*hexSize-2;
    private static final int hexHeight = 2*hexSize;
    private static final int WIDTH = 11*hexSize+4;
    // TODO: 10/24/2018
    private static final int HEIGHT = 5*hexHeight + 10;


    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        addHexagon(hexSize,world);
        ter.renderFrame(world);


    }

    public static void addHexagon(int size, TETile[][] world){
        int px;
        int py;
        for(int i=3; i<=7; i++){
            int j; // column number of hex: 3,4,5,4,3
            if(i<=5) j =i;
            else j = 5- i%5;

            for(int k =0; k<j; k++){
                px = (i-3)*(2*size-1); // x position of the left top corner of the rectangle, starts from 0
                // TODO: 10/24/2018
                py = (HEIGHT - j*hexWidth)/2 + k*(hexHeight-1);

                addHexagonHelper(size,px,py,world);

            }
        }
    }


// add a single hexagon
    private static void addHexagonHelper(int size, int x, int y, TETile[][] world){

        for(int row = 1; row <= 2*size; row++){

            addHexagonHelperLine(size,row,x,y, world);
        }

    }

    private static void addHexagonHelperLine(int size,int row, int x, int y, TETile[][] world) {

        //generate the hex symmetrically
        int symmetricRow = row;
        if (row > size) symmetricRow = 2*size - row + 1;

        for (int j = 1; j <= hexWidth; j++) {
            if (j > size - symmetricRow && j <= 2 * size + symmetricRow - 2) {
                world[x + j][y + row] = Tileset.WALL;
            }
        }
    }
}
