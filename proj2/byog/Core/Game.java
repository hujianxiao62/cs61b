package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.io.*;
import java.util.Random;
public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private intWorld gameIntWorld;
    private long SEED = 12354; //Real SEED is parsed by args, this value is for loading the game without saved file
    private Random RANDOM = new Random(SEED);
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);

        //ter.renderFrame(world);
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

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        world = addTile(world,gameIntWorld);
        return world;
    }

    private static intWorld loadWorld() {
        File f = new File("./proj2.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                intWorld loadWorld = (intWorld) os.readObject();
                os.close();
                System.out.println(loadWorld.WIDTH + "load");
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
            System.out.println(w.playerPosition[0]);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //add tile.
    public static TETile[][] addTile(TETile[][] world, intWorld World) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                switch (World.myIntWorld[i][j]){
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

}
