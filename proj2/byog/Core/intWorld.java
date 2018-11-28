package byog.Core;

import java.io.Serializable;
import java.util.Random;

public class intWorld implements Serializable {
    private static final long serialVersionUID = 1L;
    public int WIDTH;
    public int HEIGHT;
    public int[][] myIntWorld;
    private Random RANDOM;
    public int[] DoorPosition;
    public int[] playerPosition;
    public int round = 1;
    public intWorld(int width, int height, Random random){
        WIDTH = width;
        HEIGHT = height;
        RANDOM = random;
        myIntWorld = new int[WIDTH][HEIGHT];
        initializeIntWorld();
    }

    public void initializeIntWorld() {
        int[] currentPosition = new int[]{WIDTH/2,HEIGHT/2,-1};
        for(int i =0; i < 70; i++){
            for(int j = 0; j<4; j++){
                currentPosition = addHall(-10,currentPosition);
                currentPosition = positionRandomDirection(currentPosition);
            }
            currentPosition = addRoom(-6,-6,currentPosition);
        }
        addWall();
        //add locked door: 1.find a place on wall, 2. add as door
        DoorPosition = randomPosition(2);
        addItem(DoorPosition,3);

        //add player: 1.find a place on floor, 2. add as player
        playerPosition = randomPosition(1);
        addItem(playerPosition,5);
    }

    //create a hall.
    // return end position of hall
    public int[] addHall(int length, int[] startPositionDirection) {
        int direction = getRandomDirection(startPositionDirection[2]);
        int[] currentDirection = new int[]{startPositionDirection[0], startPositionDirection[1], direction};
        int hallLength = getLength(length, currentDirection, true);
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];
        int xDirection = getDirection(direction)[0];
        int yDirection = getDirection(direction)[1];

        for (int i = 0; i < hallLength; i++) {
            for (int j = 0; j < hallLength; j++) {
                myIntWorld[xStart+i*xDirection][yStart+j*yDirection] = 1;
            }
        }
        int[] currentPositionDirection = new int[]{xStart+hallLength*xDirection,yStart+hallLength*yDirection,direction};
        return currentPositionDirection;
    }

    //create a room.
    // return outDoor position
    // length1 is parallel to direction, length2 is perpendicular to direction
    public int[] addRoom(int length1, int length2, int[] centerPositionDirection) {
        final int lengthX = getLength(length1,centerPositionDirection,false);
        final int lengthY = getLength(length2,centerPositionDirection,false);
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
                    myIntWorld[xStart + i][yStart + j] = 1;
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
    private int getRandomDirection(int direction){
        if (direction == 1 || direction == 2 || direction == 3 || direction == 4)
        {return direction;
        }else {
            return RANDOM.nextInt(4)+1;
        }
    }

    // if direction is in [1,4], return it; otherwise return a random direction
    public int[] positionRandomDirection(int[] currentPositionDirection){

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
    private int getLength(int length, int[] startPositionDirection, boolean Hall){
        int hallLength;
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];

        if(length <= 0){
            hallLength = RANDOM.nextInt(-length)+1;
        }else {
            hallLength = length;
        }

        while (outBoundary(hallLength,startPositionDirection,Hall)){
            hallLength--;
        }
        return hallLength;
    }

    //Hall=true: check one direction; Hall=false: check four direction;
    private boolean outBoundary(int length, int[] startPositionDirection, boolean Hall) {
        boolean outBoundary;
        int[]currentDirection = getDirection(startPositionDirection[2]);
        int xDirection = currentDirection[0];
        int yDirection = currentDirection[1];
        int xStart = startPositionDirection[0];
        int yStart = startPositionDirection[1];

        if (Hall) {
            outBoundary = xStart + xDirection * length > myIntWorld.length - 2 ||
                    xStart + xDirection * length < 1 ||
                    yStart + yDirection * length > myIntWorld[0].length - 2 ||
                    yStart + yDirection * length < 1;
        } else {
            outBoundary = xStart + length > myIntWorld.length - 2 ||
                    xStart - length < 1 ||
                    yStart + length > myIntWorld[0].length - 2 ||
                    yStart - length < 1;
        }
        return outBoundary;
    }

    //add wall.
    public void addWall() {

        for (int i = 0; i < myIntWorld.length; i++) {
            for (int j = 0; j < myIntWorld[0].length; j++) {
                if(myIntWorld[i][j] == 1){
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            myIntWorld[i+x][j+y] = myIntWorld[i+x][j+y]==1? 1:2;
                        }
                    }
                }
            }
        }
    }

    //add item. 3:locked door, 4:opened door, 5: player
    public void addItem(int[] itemPosition, int item) {
        myIntWorld[itemPosition[0]][itemPosition[1]] = item;
    }

    //move player
    public void movePlayer(String dir) {
        int x = playerPosition[0];
        int y = playerPosition[1];
        switch (dir){
            case "A":
                if(myIntWorld[x-1][y]!=2){myIntWorld[x-1][y] = 5; myIntWorld[x][y] = 1; playerPosition = new int[]{x-1,y};} break;
            case "S":
                if(myIntWorld[x][y-1]!=2){myIntWorld[x][y-1] = 5; myIntWorld[x][y] = 1; playerPosition = new int[]{x,y-1};} break;
            case "W":
                if(myIntWorld[x][y+1]!=2){myIntWorld[x][y+1] = 5; myIntWorld[x][y] = 1; playerPosition = new int[]{x,y+1};} break;
            case "D":
                if(myIntWorld[x+1][y]!=2){myIntWorld[x+1][y] = 5; myIntWorld[x][y] = 1; playerPosition = new int[]{x+1,y};} break;
            default:break;
        }
    }

    //return a random position in the map, 1:floor, 2:wall
    private int[] randomPosition(int type) {
        int x = RANDOM.nextInt(WIDTH);
        int y = RANDOM.nextInt(HEIGHT);
        switch (type){
            case 2 :
                boolean isWall = false;
                boolean withFloor = false;
                boolean withNothing = false;
                while (!(isWall && withFloor && withNothing)){
                    x = RANDOM.nextInt(WIDTH-4)+2;
                    y = RANDOM.nextInt(HEIGHT-4)+2;
                    isWall = false;
                    withFloor = false;
                    withNothing = false;
                    for(int i=-1; i<2; i++){
                        for(int j=-1; j<2; j++){
                            withNothing = withNothing || myIntWorld[x+i][y+j] == 0? true:false;
                        }
                    }
                    withFloor = myIntWorld[x+1][y] == 1 || myIntWorld[x-1][y] == 1 || myIntWorld[x][y+1] == 1 || myIntWorld[x][y-1] ==1;
                    isWall = myIntWorld[x][y] == 2;
                }break;
            default:
                while (myIntWorld[x][y] != type){
                    x = RANDOM.nextInt(WIDTH);
                    y = RANDOM.nextInt(HEIGHT);
                }break;
        }
        return new int[]{x,y};
    }
}
