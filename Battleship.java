import java.util.*;
import java.io.*;

public class Battleship {
    static final int SIZE = 10;
    static final char EMP = '-';
    static final char MISS = 'O';
    static final char HIT = 'X';
    static final String[] NAMES = {"Carrier", "Battleship" , "Cruiser" , "Submarine", "Destroyer"};
    static final int[] LENGTHS = {5, 4, 3, 3, 2};
    static final char[] CHARS = {'A', 'B', 'C', 'S', 'D'};
    public static void main(String[] args) {
        boolean quit = false;
        while (!quit) {
            System.out.println("Enter an instruction: ");
            System.out.println("1. Start new game \n2. Load Game \n3. Display instructions \n4. Quit");
            int c = getInt();
            switch (c) {
                case 1:
                    newGame();
                    break;
                case 2: 
                    System.out.println("Enter a file name to load from");
                    loadGame(getLine());
                    break;
                case 3:
                    displayInstructions();
                    break;
                case 4:
                    quit = true;
                    break;
            }
        }
    }
    public static void saveGame(String file, char[][] pShips, char[][] cShips, char[][] pShots, char[][] cShots, int difficulty, int[] prev) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".txt", false));
            bw.write(difficulty +""); bw.newLine();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    bw.write(pShips[i][j]);
                }
                bw.newLine();
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    bw.write(cShips[i][j]);
                }
                bw.newLine();
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    bw.write(pShots[i][j]);
                }
                bw.newLine();
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    bw.write(cShots[i][j]);
                }
                bw.newLine();
            }
            bw.write(prev[0] + ""); bw.newLine();
            bw.write(prev[1] + ""); bw.newLine();
            bw.write(prev[2] + ""); bw.newLine();
            bw.close();
            System.out.println("Game saved succesfully");
        } catch (IOException e) {
            System.out.println("ERORR" + e);
        }
    }
    public static void loadGame(String file) {
        char[][] pShips = new char[SIZE][SIZE];
        char[][] cShips = new char[SIZE][SIZE];
        char[][] pShots = new char[SIZE][SIZE];
        char[][] cShots = new char[SIZE][SIZE];
        int difficulty;
        try {  
            BufferedReader br = new BufferedReader(new FileReader(file + ".txt"));
            difficulty = Integer.parseInt(br.readLine());
            for (int i = 0; i < SIZE; i++) {
                String s = br.readLine();
                for (int j = 0; j < SIZE; j++) {
                    pShips[i][j] = s.charAt(j);
                }
            }
            for (int i = 0; i < SIZE; i++) {
                String s = br.readLine();
                for (int j = 0; j < SIZE; j++) {
                    cShips[i][j] = s.charAt(j);
                }
            }
            for (int i = 0; i < SIZE; i++) {
                String s = br.readLine();
                for (int j = 0; j < SIZE; j++) {
                    pShots[i][j] = s.charAt(j);
                }
            }
            for (int i = 0; i < SIZE; i++) {
                String s = br.readLine();
                for (int j = 0; j < SIZE; j++) {
                    cShots[i][j] = s.charAt(j);
                }
            }
            int[] prev = new int[3];
            prev[0] = Integer.parseInt(br.readLine());
            prev[1] = Integer.parseInt(br.readLine());
            prev[2] = Integer.parseInt(br.readLine());
            br.close();
            playGame(pShips, pShots, cShips, cShots, difficulty, prev);
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }
    public static void newGame() {
        System.out.println("Select a difficulty (0 easy, 1 hard)");
        int diff = getInt();

        char[][] pShips = new char[SIZE][SIZE];
        char[][] cShips = new char[SIZE][SIZE];
        char[][] pShots = new char[SIZE][SIZE];
        char[][] cShots = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                pShips[i][j] = EMP;
                cShips[i][j] = EMP;
                pShots[i][j] = EMP;
                cShots[i][j] = EMP;
            }
        }
        for (int i = 0; i < NAMES.length; i++) {
            System.out.println("Current Board");
            displayBoard(pShips, pShots);

            System.out.println("Enter the top left x coordinate for piece " + NAMES[i] + " of length " + LENGTHS[i]);
            int x = getInt() - 1;
            System.out.println("Enter the top left y coordinate for piece " + NAMES[i] + " of length " + LENGTHS[i]);
            int y = getInt() - 1;
            System.out.println("Enter an orientation for the piece (0 horiantal) (1 vertical)");
            int o = getInt();
            if (checkOverlap(x, y, o, LENGTHS[i], pShips)) {
                System.out.println("Your ships overlap \nTry Again\n\n");
                i--;
            } else {
                place(x, y, o, LENGTHS[i], pShips, CHARS[i]);
            } 
        }
        System.out.println("These are your ships");
        displayBoard(pShips, pShots);
        //AI placing ships randomly
        for (int i = 0; i < NAMES.length; i++) {
            int x = (int) (Math.random() * SIZE);
            int y = (int) (Math.random() * SIZE);
            int o = (int) (Math.random() * 2);
            if (checkOverlap(x, y, o, LENGTHS[i], cShips)) {
                i--;
            } else {
                place(x, y, o, LENGTHS[i], cShips, CHARS[i]);
            } 
        }
        playGame(pShips, pShots, cShips, cShots, diff, new int[]{0, 0, 0});
    }
    public static boolean checkOverlap(int r, int c, int o, int l, char[][] board) {
        if (o == 1) {
            for (int i = r; i < r + l; i++) {
                if (!onBoard(i, c) || board[i][c] != EMP) return true;
            }
        } else {
            for (int i = c; i < c + l; i++) {
                if (!onBoard(r, i) || board[r][i] != EMP) return true;
            }
        }
        return false;
    }
    public static void place(int r, int c, int o, int l, char[][] board, char ch) {
        if (o == 1) {
            for (int i = r; i < r + l; i++) {
                board[i][c] = ch;
            }
        } else {
            for (int i = c; i < c + l; i++) {
                board[r][i] = ch;
            }
        }
    }
    public static void playGame(char[][] pShips, char[][] pShots, char[][] cShips, char[][] cShots, int difficulty, int[] prev) {
        boolean quit = false;

        while (!quit) {
            System.out.println("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\nCurrent board");
            displayBoard(pShips, pShots);
            System.out.println("Enter an instruction");
            System.out.println("1. Place a shot \n2. Save the game \n3. Surrender");
            int op = getInt();
            switch (op) {
                case 1:
                    System.out.println("Enter a shot x and y coordinate between 1 and 10"); 
                    int x = getInt() - 1;
                    int y = getInt() - 1;
                    if (onBoard(x, y)) {
                        System.out.println("\n\n\nPlayer move: ");
                        makeMove(x, y, cShips, pShots);
                        System.out.println("Computer move: ");
                        prev = moveComp(pShips, cShots, difficulty, prev);
                    }
                    break;
                case 2:
                    System.out.println("Enter a filename to save to");
                    saveGame(getLine(), pShips, cShips, pShots, cShots, difficulty, prev);
                    break;
                case 3:
                    quit = true;
                    break;
            }
        }
    }
    public static int[] moveComp(char[][] pShips, char[][] cShots, int difficulty, int[] prevShot) {
        //System.out.println("TESTING CODE"); //debbuging code
        //displayBoard(pShips, cShots);
        //System.out.println("prev" + prevShot[0] + " " + prevShot[1] + " " +prevShot[2]);
        int x = prevShot[0], y = prevShot[1];
        if (difficulty == 1 && prevShot[2] == 1) {
            //System.out.println("HARD");
            int[][] moves = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            for (int[] m: moves) {
                int newx = x + m[0], newy = y + m[1];
                if (onBoard(newx, newy) && cShots[newx][newy] == EMP) {
                    if (makeMove(newx, newy, pShips, cShots)) prevShot[2] = 0;
                    else if (cShots[newx][newy] == HIT) {
                        prevShot[0] = newx;
                        prevShot[1] = newy;
                    }
                    return prevShot;
                }
            }
            //System.out.println("fail");
        } 
        while (!(onBoard(prevShot[0], prevShot[1]) && cShots[prevShot[0]][prevShot[1]] == EMP)) {
            prevShot[0] = (int) (Math.random() * SIZE);
            prevShot[1] = (int) (Math.random() * SIZE);
        }
        if (makeMove(prevShot[0], prevShot[1], pShips, cShots)) prevShot[2] = 0;
        else if (cShots[prevShot[0]][prevShot[1]] == HIT) prevShot[2] = 1; 
        return prevShot;
    }
    public static boolean makeMove(int x, int y, char[][] ships, char[][] shots) {
        char c = ships[x][y];
        if (c != EMP) {
            System.out.println("HIT ON SQUARE (" + (x+1) + "," + (y+1) + ")");
            place(x, y, 0, 1, shots, HIT);
            place(x, y, 0, 1, ships, HIT);
            if (checkSink(ships, c)) {
                System.out.println("SUNK ENEMY " + getShip(c));
                return true;
            }
        } else {
            System.out.println("MISS ON SQUARE (" + (x+1) + "," + (y+1) + ")");
            place(x, y, 0, 1, shots, MISS);
            place(x, y, 0, 1, ships, MISS);
        }
        return false;
    }
    public static boolean onBoard(int x, int y) {
        if (x < 0 || x >= SIZE) return false;
        if (y < 0 || y >= SIZE) return false;
        return true;
    }
    public static boolean checkSink(char[][] ships, char c) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j] == c) return false;
            }
        }
        return true;
    }
    public static String getShip(char c) {
        for (int i = 0; i < NAMES.length; i++) {
            if (CHARS[i] == c) return NAMES[i];
        }
        return "ERROR";
    }
    public static void displayBoard(char[][] pb, char[][] cb) {
        System.out.printf("%-20s%s%n", "Ships:", "Shots:");
        for (int i = 0; i < SIZE + 1; i++) {
            if (i == 0) {
                System.out.print("  ");
                for (int j = 1; j <= SIZE; j++) {
                    System.out.print(" " + j);
                }
                System.out.print("    ");
                for (int j = 1; j <= SIZE; j++) {
                    System.out.print(" " + j);
                }
            } else {
                System.out.printf("%2d", i);
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(" " + pb[i-1][j]);
                }
                System.out.printf("   %2d", i);
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(" " + cb[i-1][j]);
                }  
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void displayInstructions() {
        //TODO
    }
    public static int getInt() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                int n = Integer.parseInt(sc.nextLine());
                if (n < 0) throw new NumberFormatException();
                return n;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input, Try Again");
            }
        }
    }
    public static String getLine() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
