package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;
public class Game {
    /* Feel free to change the width and height. */
    private static TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private intWorld gameIntWorld;
    private long SEED = 12354; //Real SEED is parsed by args, this value is for loading the game without saved file
    private Random RANDOM = new Random(SEED);
    private boolean gameOver = false;
    private int page = 1;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.enableDoubleBuffering();
        ter.initialize(WIDTH, HEIGHT);
        while (true) {
            switch (page) {
                case 1:
                    showPage();
                    if (StdDraw.hasNextKeyTyped()) {
                        String c = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();
                        switch (c) {
                            case "N":
                                SEED = getSeed();
                                RANDOM = new Random(SEED);
                                gameIntWorld = new intWorld(WIDTH,HEIGHT,RANDOM);
                                page = 3;
                                showPage();
                                break;
                            case "L":
                                gameIntWorld = loadWorld();
                                page = 2;
                                break;
                            default:break;
                        }
                    }
                    break;
                case 2:
                    showPage();
                    while (!gameOver) {
                        if (StdDraw.hasNextKeyTyped()) {
                            String c = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();
                            if(c.equals("Q")){
                                saveWorld(gameIntWorld);
                                System.exit(0);
                                break;
                            }else {
                                gameIntWorld.movePlayer(c);
                                if( gameIntWorld.playerPosition[0] == gameIntWorld.DoorPosition[0] &&
                                        gameIntWorld.playerPosition[1] == gameIntWorld.DoorPosition[1]){
                                    gameOver = true;
                                    drawOpenDoor();
                                    StdDraw.pause(500);
                                    page = 3;
                                }else {
                                    showPage();
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println(page);
                    showPage();
                    SEED += 1;
                    RANDOM = new Random(SEED);
                    gameIntWorld = new intWorld(WIDTH,HEIGHT,RANDOM);
                    page = 2;
                    gameOver = false;
                    System.out.println(page);
                    break;
            }
        }
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
        String command = input.toUpperCase();
        if(command.charAt(0)=='L') {
            gameIntWorld = loadWorld();
        }else if(command.charAt(0)=='N'){
            //get seed number
            int j = 1;
            String Seed = "";
            while (command.charAt(j) != 'S') {
                Seed += command.charAt(j);
                j++;
            }
            SEED = Integer.parseInt(Seed);
            RANDOM = new Random(SEED);
            gameIntWorld = new intWorld(WIDTH,HEIGHT,RANDOM);
        }

        //move player, if char(i) is not aswd, nothing will change
        for (int i = 1; i < command.length(); i++) {
            gameIntWorld.movePlayer(String.valueOf(command.charAt(i)));
        }

        //save game
        if(command.charAt(command.length()-1)=='Q'){
        saveWorld(gameIntWorld);
        }
        return addTile(gameIntWorld);
    }

    private void showPage(){
        switch (page){
            case 1:
                StdDraw.clear();
                StdDraw.clear(Color.black);
                // Draw the actual text
                Font bigFont = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(bigFont);
                StdDraw.setPenColor(Color.white);
                StdDraw.text(WIDTH / 2, HEIGHT/2+5, "Wellcome to MT's maze!");
                bigFont = new Font("Monaco", Font.BOLD, 20);
                StdDraw.setFont(bigFont);
                StdDraw.text(WIDTH / 2, HEIGHT/2, "Press N to start a new game.");
                StdDraw.text(WIDTH / 2, HEIGHT/2-3, "Press L to load previous game.");
                StdDraw.show();
                break;
            case 2:
                StdDraw.setFont();
                TETile[][] w = addTile(gameIntWorld);
                ter.renderFrame(w);
                StdDraw.pause(100);
                break;
            case 3:
                StdDraw.clear();
                StdDraw.clear(Color.black);
                // Draw the actual text
                bigFont = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(bigFont);
                StdDraw.setPenColor(Color.white);
                StdDraw.text(WIDTH/2, HEIGHT/2, "Open the locked door!");
                StdDraw.show();
                StdDraw.pause(500);
                break;
        }
    }

    private static intWorld loadWorld() {
        File f = new File("./proj2.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                intWorld loadWorld = (intWorld) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        /* In the case no World has been saved yet, we return a new one. */
        return new intWorld(WIDTH,HEIGHT,new Random(1));
    }

    private static void saveWorld(intWorld w) {
        File f = new File("./proj2.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //add tile.
    public static TETile[][] addTile(intWorld World) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                switch (World.myIntWorld[i][j]){
                    case 0: world[i][j] = Tileset.NOTHING;break;
                    case 1: world[i][j] = Tileset.FLOOR;break;
                    case 2: world[i][j] = Tileset.WALL;break;
                    case 3: world[i][j] = Tileset.LOCKED_DOOR;break;
                    case 4: world[i][j] = Tileset.UNLOCKED_DOOR;break;
                    case 5: world[i][j] = Tileset.PLAYER;break;
                }
            }
        }
        return world;
    }

    private void drawOpenDoor() {
        gameIntWorld.addItem(gameIntWorld.DoorPosition,4);
        TETile[][] w = addTile(gameIntWorld);
        ter.renderFrame(w);
        StdDraw.pause(100);
    }

    public long getSeed() {
        String input = " ";
        drawSeed(input);

        while (input.charAt(input.length()-1) != 'S' && input.charAt(input.length()-1) != 's') {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
                char key = StdDraw.nextKeyTyped();
                input += String.valueOf(key);
                drawSeed(input);
        }
            StdDraw.pause(500);
        return Long.valueOf(input.substring(1,input.length()-1));
    }

    public void drawSeed(String s) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight+5, "Please enter a random number and end with S");
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

}
