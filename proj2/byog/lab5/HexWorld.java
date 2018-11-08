package byog.lab5;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int hexSize = 6;
    private static final int hexWidth = 3*hexSize-2;
    private static final int hexHeight = 2*hexSize;
    private static final int hexWidthNoOverlap = 2*hexSize -1;
    private static final int WIDTH = 4*(hexWidthNoOverlap)+hexWidth+1;
    private static final int HEIGHT = 5*hexHeight+1;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


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

        addHexagon(hexSize,world,true);
        ter.renderFrame(world);


    }

    public static void addHexagon(int size, TETile[][] world, boolean randomWorld){
        int px;
        int py;
        for(int i=3; i<=7; i++){
            int j;
            if(i<=5) j =i;
            else j = 5- i%5;
            // column number of hex: j = 3,4,5,4,3

            for(int k =0; k<j; k++){
                px = (i-3)*hexWidthNoOverlap; // x position started from 0
                py = (5 - j)*hexSize + k*(hexHeight); // y position started from 0
                if(randomWorld){
                    int tileType = RANDOM.nextInt(5);
                    addHexagonHelper(size, px, py, world, tileType);
                }else {
                    addHexagonHelper(size, px, py, world, 0);
                }
            }
        }
    }


// add a single hexagon
    private static void addHexagonHelper(int size, int x, int y, TETile[][] world, int tileType){

        for(int row = 1; row <= 2*size; row++){

            addHexagonHelperLine(size,row,x,y,world,tileType);
        }

    }

    private static void addHexagonHelperLine(int size,int row, int x, int y, TETile[][] world, int tileType) {

        //generate the hex symmetrically
        int symmetricRow = row;
        if (row > size) symmetricRow = 2*size - row + 1;

        for (int j = 1; j <= hexWidth; j++) {
            if (j > size - symmetricRow && j <= 2 * size + symmetricRow - 2) {
                switch (tileType) {
                        case 0: world[x + j][y + row] = Tileset.WALL; break;
                        case 1: world[x + j][y + row] = Tileset.FLOWER; break;
                        case 2: world[x + j][y + row] = Tileset.GRASS; break;
                        case 3: world[x + j][y + row] = Tileset.MOUNTAIN; break;
                        case 4: world[x + j][y + row] = Tileset.LOCKED_DOOR; break;
                        case 5: world[x + j][y + row] = Tileset.TREE; break;
                    default: world[x + j][y + row] = Tileset.NOTHING; break;
                }
            }
        }
    }
}
