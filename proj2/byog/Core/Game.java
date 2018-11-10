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
    public static int[] addHall(int length, int direction, int[] startPosition, TETile[][] world) {

        int[] currentDirection = getDirection(direction);
        int hallLength = getLength(length,currentDirection,startPosition,world,true);

        int xStart = startPosition[0];
        int yStart = startPosition[1];
        int xDirection = currentDirection[0];
        int yDirection = currentDirection[1];

        for (int i = 0; i < hallLength; i++) {
            for (int j = 0; j < hallLength; j++) {
                world[xStart+i*xDirection][yStart+j*yDirection] = Tileset.FLOOR;
            }
        }
        int[] currentPosition = new int[]{xStart+hallLength*xDirection,yStart+hallLength*yDirection};
        return currentPosition;
    }

    //create a room.
    // return outDoor position
    // length1 is parallel to direction, length2 is perpendicular to direction
    public static int[] addRoom(int length1, int length2, int direction, int[] inDoorPosition, TETile[][] world) {
        final int[] currentDirection = getDirection(direction);
        final int length11 = getLength(length1,currentDirection,inDoorPosition,world,true);
        final int length22 = getLength(length2,currentDirection,inDoorPosition,world,false);
        final int xDirection = currentDirection[0];
        final int yDirection = currentDirection[1];
        int[] outDoorPosition = new int[2];

        //set random placement of indoor
        int displace = RANDOM.nextInt(length22);
        for (int i = -displace; i < length22 - displace; i++) {
            for (int j = -displace; j < length22 - displace; j++) {
                int[] startPosition = new int[]{inDoorPosition[0] + i*yDirection, inDoorPosition[1] + j*xDirection};
                addHall(length11,direction,startPosition,world);
            }
        }
        //TODO TIME
//        switch (RANDOM.nextInt(4)){
//            case 0: outDoorPosition = inDoorPosition; break;
//            case 1: outDoorPosition = [];
//        }
//        int xOutDoor = RANDOM
        return inDoorPosition;
    }

    // direction=1, return[0,1]; direction=2, return[1,0]; direction=3, return[0,-1]; direction=4, return[-1,0];, direction= other, return random of these four;
    private static int[] getDirection(int direction){
        int xDirection = 0;
        int yDirection = 0;

        switch (direction) {
            case 1: yDirection = 1;break;
            case 2: xDirection = 1;break;
            case 3: yDirection = -1;break;
            case 4: xDirection = -1;break;
            //random direction for hall
            default: xDirection = RANDOM.nextInt(3) -1;
                if(xDirection == 0){
                    yDirection = RANDOM.nextInt(2) == 1 ? 1:-1;
                }
                break;
        }
        int[] currentDirection = new int[]{xDirection,yDirection};
        return currentDirection;
    }

    // out of domain, length =0;
    // in domain:  length>0, return length; length <=0, return random [0,-length];
    private static int getLength(int length, int[] currentDirection, int[] startPosition, TETile[][] world, boolean parallel){
        int hallLength;
        int xDirection = currentDirection[0];
        int yDirection = currentDirection[1];
        int xStart = startPosition[0];
        int yStart = startPosition[1];
        boolean outBoundary;

        if(length <= 0){
            hallLength = RANDOM.nextInt(-length + 1);
        }else {
            hallLength = length;
        }

        if(parallel) {
            // true: the length parallel to direction is out the domain
            outBoundary = xStart + xDirection * hallLength > world.length - 1 ||
                    xStart + xDirection * hallLength < 0 ||
                    yStart + yDirection * hallLength > world[0].length - 1 ||
                    yStart + yDirection * hallLength < 0;
        }else {
            // true: the length perpendicular to direction is out the domain
            outBoundary = xStart + hallLength > world.length - 1 ||
                    xStart - hallLength < 0 ||
                    yStart + hallLength > world[0].length - 1 ||
                    yStart - hallLength < 0;
        }

        if (outBoundary){
            hallLength = 0;
        }
        return hallLength;
    }
}
