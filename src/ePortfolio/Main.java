package ePortfolio;

/**
 * The Main class is the entry point of the ePortfolio application.
 * It handles loading investments from a file, initializing the user interface (GUI),
 * and saving the portfolio to a specified file upon exit.
 * 
 * It accepts two command-line arguments:
 * - args[0]: Path of the file to load investments from.
 * - args[1]: Path of the file to save investments to.
 */
public class Main {

     /**
     * Default constructor for the Main class.
     * Initializes the Main class for use in launching the ePortfolio application.
     */
     public Main() {
         // No specific initialization is needed for this class
     }
    
    /**
     * The main method is the entry point of the program. It initializes the
     * portfolio, loads investments, displays the GUI, and saves the portfolio state
     * when the program ends.
     * 
     * @param args Command-line arguments:
     *             - args[0]: Path of the file to load investments from.
     *             - args[1]: Path of the file to save investments to.
     */
    public static void main(String[] args) {

        // Check if the correct number of arguments (2) are provided for file paths
        if (args.length != 2) {
            // If not, print usage instructions and terminate
            System.out.println("Usage: java ePortfolio.Main <loadFile> <saveFile>");
            return;
        }

        // Initialize a new Portfolio object to manage the investments
        Portfolio portfolio = new Portfolio();

        // Get the file paths from the command-line arguments
        String loadFile = args[0]; // The first argument is the load file
        String saveFile = args[1]; // The second argument is the save file

        // Load investments from the specified load file
        // The method readInvestments handles reading the file and adding investments to
        // the portfolio
        portfolio.readInvestments(loadFile);

        // Initialize and show the GUI (Gui class instead of Display)
        // The Gui class manages the user interface for interacting with the portfolio
        Gui layout = new Gui();
        layout.setVisible(true);

        // After quitting the command loop, write the current state of the portfolio to
        // the save file
        // The method writeInvestment saves the portfolio's data to the specified file
        portfolio.writeInvestment(saveFile);
    }
}