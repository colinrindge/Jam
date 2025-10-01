package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.io.IOException;
import java.util.Scanner;

public class JamPTUI implements Observer<JamModel, String> {
    private JamModel model;

    /**
     * Creates model, adds this as observer, loads file, and shows help popup
     * @param filename file to load
     */
    public void init(String filename){
        this.model = new JamModel();
        this.model.addObserver(this);
        this.model.loadFile(filename);
        displayHelp();
    }

    /**
     * Prints any passed messages and prints out the current grid
     *
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel model, String data) {
        if (data != null && !data.isEmpty()) {
            System.out.println(data);
        }
        System.out.println(this.model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Checks user inputs and runs corresponding methods in model
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith( "h" )) {
                    this.model.getHint();
                } else if (words[0].startsWith( "r" )) {
                    this.model.loadDefault();
                } else if (words[0].startsWith( "l" )) {
                    this.model.loadFile(words[1]);
                } else if (words[0].startsWith( "s" )) {
                    if(words.length != 3) {
                        System.out.println("Usage: s(elect) r c");
                    } else {
                        this.model.selectCar(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                    }
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else {
            JamPTUI ptui = new JamPTUI();
            ptui.init(args[0]);
            ptui.run();
        }
    }
}
