package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    //create a hall.
    // return end position of hall
    public static int[] addHall(int length, int[] startPositionDirection, int[][] world) {
        int direction = getRandomDirection(startPositionDirection[2]);
        int[] currentDirection = new int[]{startPositionDirection[0], startPositionDirection[1], direction};
        int hallLength = getLength(length, currentDirection, world,true);
//        while (hallLength<=0){
//            startPositionDirection = new int[]{startPositionDirection[0],startPositionDirection[1],(startPositionDirection[1]+RANDOM.nextInt(4))%4};
//            hallLength = getLength(length, startPositionDirection, world,true);
//        }
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];
        int xDirection = getDirection(direction)[0];
        int yDirection = getDirection(direction)[1];

        for (int i = 0; i < hallLength; i++) {
            for (int j = 0; j < hallLength; j++) {
                world[xStart+i*xDirection][yStart+j*yDirection] = 1;
            }
        }
        int[] currentPositionDirection = new int[]{xStart+hallLength*xDirection,yStart+hallLength*yDirection,direction};
        return currentPositionDirection;
    }

    //create a room.
    // return outDoor position
    // length1 is parallel to direction, length2 is perpendicular to direction
    public static int[] addRoom(int length1, int length2, int[] centerPositionDirection, int[][] world) {
        final int lengthX = getLength(length1,centerPositionDirection,world,false);
        final int lengthY = getLength(length2,centerPositionDirection,world,false);
        final int xStart = centerPositionDirection[0];
        final int yStart = centerPositionDirection[1];
        int[] outDoorPosition = new int[]{0,0,0};

        if(lengthX<=0 || lengthY<=0){
            return new int[]{centerPositionDirection[0], centerPositionDirection[1], RANDOM.nextInt(4) + 1};
        }else {
            //set random placement of indoor
            int displaceX = RANDOM.nextInt(lengthX);
            int displaceY = RANDOM.nextInt(lengthY);
            for (int i = -displaceX; i < lengthX - displaceX; i++) {
                for (int j = -displaceY; j < lengthY - displaceY; j++) {
                    world[xStart + i][yStart + j] = 1;
                }
            }

            switch (RANDOM.nextInt(4) + 1) {
                case 1:
                    outDoorPosition = new int[]{xStart - displaceX + RANDOM.nextInt(lengthX), yStart + lengthY - displaceY - 1, 1};
                    break;
                case 2:
                    outDoorPosition = new int[]{xStart + lengthX - displaceX - 1, yStart - displaceY + RANDOM.nextInt(lengthY), 2};
                    break;
                case 3:
                    outDoorPosition = new int[]{xStart - displaceX + RANDOM.nextInt(lengthX), yStart - displaceY, 3};
                    break;
                case 4:
                    outDoorPosition = new int[]{xStart - displaceX, yStart - displaceY + RANDOM.nextInt(lengthY), 4};
                    break;
            }
            return outDoorPosition;
        }
    }

    // if direction is in [1,4], return it; otherwise return a random direction
    private static int getRandomDirection(int direction){
        if (direction == 1 || direction == 2 || direction == 3 || direction == 4)
        {return direction;
        }else {
            return RANDOM.nextInt(4)+1;
        }
    }

    // if direction is in [1,4], return it; otherwise return a random direction
    public static int[] positionRandomDirection(int[] currentPositionDirection){

            return new int[]{currentPositionDirection[0],currentPositionDirection[1],RANDOM.nextInt(4)+1};
    }

    // direction=1, return[0,1]; direction=2, return[1,0]; direction=3, return[0,-1]; direction=4, return[-1,0];
    private static int[] getDirection(int direction){
        int xDirection = 0;
        int yDirection = 0;

        switch (direction) {
            case 1: yDirection = 1;break;
            case 2: xDirection = 1;break;
            case 3: yDirection = -1;break;
            case 4: xDirection = -1;break;
            default: throw new IllegalArgumentException("direction should be 1:up, 2:right, 3:down, 4:left");
        }
        int[] currentDirection = new int[]{xDirection,yDirection};
        return currentDirection;
    }

    // out of domain, length =0;
    // in domain:  length>0, return length; length <=0, return random [0,-length];
    private static int getLength(int length, int[] startPositionDirection, int[][] world, boolean Hall){
        int hallLength;
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];

        if(length <= 0){
            hallLength = RANDOM.nextInt(-length)+1;
        }else {
            hallLength = length;
        }

        while (outBoundary(hallLength,startPositionDirection,world,Hall)){
            hallLength--;
        }

        return hallLength;
    }

    //Hall=true: check one direction; Hall=false: check four direction;
    private static boolean outBoundary(int length, int[] startPositionDirection, int[][] world, boolean Hall) {
        boolean outBoundary;
        int[]currentDirection = getDirection(startPositionDirection[2]);
        int xDirection = currentDirection[0];
        int yDirection = currentDirection[1];
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];

        if (Hall) {
            outBoundary = xStart + xDirection * length > world.length - 2 ||
                    xStart + xDirection * length < 1 ||
                    yStart + yDirection * length > world[0].length - 2 ||
                    yStart + yDirection * length < 1;
        } else {
            outBoundary = xStart + length > world.length - 2 ||
                    xStart - length < 1 ||
                    yStart + length > world[0].length - 2 ||
                    yStart - length < 1;
        }
        return outBoundary;
    }

    //add wall.
    public static void addWall(int[][] world) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if(world[i][j] == 1){
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            world[i+x][j+y] = world[i+x][j+y]==1? 1:2;
                        }
                    }
                }
            }
        }
    }

    //add tile.
    public static void addTile(TETile[][] world, int[][] intWorld) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                switch (intWorld[i][j]){
                    case 1: world[i][j] = Tileset.FLOOR;break;
                    case 2: world[i][j] = Tileset.WALL;break;
                }
            }
        }
    }

}
