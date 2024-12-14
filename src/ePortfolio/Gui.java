package ePortfolio;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * GUI class for the ePortfolio application that handles the graphical user
 * interface.
 * This class is responsible for displaying menus, handling user input, and
 * interacting
 * with the portfolio to perform various actions like buying, selling, updating
 * investments, etc.
 */
public class Gui extends JFrame {

  // Constants for the font size and colors used in the GUI
  static final int Font_Size = 22;

  static final Color guiColorBlue = new Color(40, 50, 60);
  static final Color guiColorWhite = new Color(220, 220, 230);
  static final Color guiColorBlack = Color.BLACK;

/** The welcome message displayed at the start. */
String firstMessage = "Welcome to ePortfolio.\n\n\n\n\n" + 
    "Choose a command from the “Commands” menu to buy or sell\n" + 
    "an investment, update prices for all investments, get gain for the\n" + 
    "portfolio, search for relevant investments, or quit the program.";

  /** The portfolio object used to manage investments. */
  private Portfolio portfolio1 = new Portfolio();

  /** Text fields shared between various menu options. */
  JTextField inputField1, inputField2, inputField3, inputField4;
  /** The two main panels in the window. */
  JPanel controlPanel, dynamicPanel;
  /** prev/next buttons for the update menu. */
  JButton prevButton, nextButton;
  /** Scroll pane that the message box sits in. */
  JScrollPane messageScrollBars;
  /** Holds the index of the current investment for the update window. */
  int indexInv = 0;
  /** Combobox for the buy menu. */
  JComboBox<String> comboBox1;
  /** The message box for displaying information to the user. */
  JTextArea messageBox;

  /**
   * Constructor for the Gui class, initializes the graphical user interface.
   * It prepares the GUI and shows the menu bar.
   */
  public Gui() {
    super();
    prepareGUI(); // Method to set up the GUI
    showMenuBar(); // Method to display the menu bar
  }

  // Listener for the Buy button to change display to the buy menu
  private class BuyListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showBuyMenu(); // Show the buy menu when the buy button is pressed
    }
  }

  // Listener for the Sell button to change display to the sell menu
  private class SellListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showSellMenu(); // Show the sell menu when the sell button is pressed
    }
  }

  // Listener for the Update button to change display to the update menu
  private class UpdateListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      indexInv = 0; // Reset the investment index to 0 for the update menu
      showUpdateMenu(); // Show the update menu
      if (Portfolio.getInvestListSize() != 0) {
        setUpdateBoxes(indexInv); // Set the text boxes for the first investment
      }
    }
  }

  // Listener for the Get Gain button to change display to the gain menu
  private class GetGainListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showGainMenu(); // Show the gain menu when the Get Gain button is pressed
    }
  }

  // Listener for the Search button to change display to the search menu
  private class SearchListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showSearchMenu(); // Show the search menu when the Search button is pressed
    }
  }

  // Listener for the Quit button to exit the program
  private class QuitListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      portfolio1.writeInvestment("investments.txt"); // Save the portfolio data before quitting
      System.exit(0); // Exit the program
    }
  }

  // Listener for the Reset button to clear text fields
  private class ResetListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      resetTextFields(); // Reset the text fields to empty
    }
  }

  // Listener for the Next button to cycle to the next investment in the update
  // menu
  private class NextListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      indexInv++; // Move to the next investment in the list
      setUpdateBoxes(indexInv); // Set the text boxes to show the new investment

      // Disable/enable next/prev buttons if they reach the end of the list
      if (indexInv == Portfolio.getInvestListSize() - 1) {
        nextButton.setEnabled(false); // Disable next button at the end of the list
      }
      if (indexInv > 0) {
        prevButton.setEnabled(true); // Enable prev button once we're past the first investment
      }
    }
  }

  // Listener for the Prev button to cycle to the previous investment in the
  // update menu
  private class PrevListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      indexInv--; // Move to the previous investment in the list
      setUpdateBoxes(indexInv); // Set the text boxes to show the new investment

      // Disable/enable next/prev buttons if they reach the end of the list
      if (indexInv == 0) {
        prevButton.setEnabled(false); // Disable prev button at the beginning of the list
      }
      if (indexInv < Portfolio.getInvestListSize()) {
        nextButton.setEnabled(true); // Enable next button once we're past the first investment
      }
    }
  }

  // Logic for the Buy button in the buy menu
  private class BuyInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = ""; // String to hold any error messages during validation

      // Get the information from the input fields
      String type = comboBox1.getSelectedItem().toString();
      String symbol = inputField1.getText();
      String name = inputField2.getText();
      String quantity = inputField3.getText();
      String price = inputField4.getText();

      // Validate the symbol
      if (Portfolio.checkifString(symbol, true) == false) {
        errorMessage += "Symbol Can't be left Empty.\n\n";
      }

      // Validate the name (required only if symbol is unique to the portfolio)
      if (Portfolio.checkifString(name, true) == false && Portfolio.findInvestment(symbol) == -1) {
        errorMessage += "Name can't be left Empty.\n\n";
      }

      // Validate the quantity (must be a positive integer)
      if (Portfolio.checkifInt(quantity, true) == false) {
        errorMessage += "Quantity must be a valid, positive integer.\n\n";
      } else if (Integer.parseInt(quantity) < 1) {
        errorMessage += "Quantity must be a positive integer greater than 0.\n\n";
      }

      // Validate the price (must be a positive double)
      if (Portfolio.checkIfDouble(price, true) == false) {
        errorMessage += "Price must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "Price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Display error message if any

      // If no errors, proceed with buying the investment
      if (errorMessage.equals("")) {
        if (name.equals("")) {
          name = " "; // Default to empty name if not provided
        }
        messageBox.setText(Portfolio.buy(type, symbol, name, Integer.parseInt(quantity), Double.parseDouble(price))); // Execute
                                                                                                                      // buy
                                                                                                                      // and
                                                                                                                      // display
                                                                                                                      // the
                                                                                                                      // result
      }
    }
  }

  // Logic for the Sell button in the sell menu
  private class SellInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = ""; // String to hold any error messages during validation

      // Get the information from the input fields
      String symbol = inputField1.getText();
      String quantity = inputField2.getText();
      String price = inputField3.getText();

      // Validate the symbol
      if (Portfolio.checkifString(symbol, true) == false) {
        errorMessage += "Symbol can't be left Empty.\n\n";
      }

      // Validate the quantity (must be a positive integer)
      if (Portfolio.checkifInt(quantity, true) == false) {
        errorMessage += "Quantity must be a valid, positive integer.\n\n";
      } else if (Integer.parseInt(quantity) < 1) {
        errorMessage += "Quantity must be a positive integer greater than 0.\n\n";
      }

      // Validate the price (must be a positive double)
      if (Portfolio.checkIfDouble(price, true) == false) {
        errorMessage += "Price must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "Price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Display error message if any

      // If no errors, proceed with selling the investment
      if (errorMessage.equals("")) {
        messageBox.setText(Portfolio.sell(symbol, Integer.parseInt(quantity), Double.parseDouble(price))); // Execute
                                                                                                           // sell and
                                                                                                           // display
                                                                                                           // the result
      }

    }
  }

  // Logic for Save button in update menu
  /**
   * ActionListener for the 'Save' button in the update menu.
   * This listener validates the input price and updates the investment's price if
   * valid.
   */
  private class UpdateInvestmentListener implements ActionListener {

    /**
     * Handles the action of clicking the 'Save' button.
     * Validates the price input and updates the investment if no errors are found.
     * Displays error messages in the messageBox if the validation fails.
     */
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the new price from the input field
      String price = inputField3.getText();

      // Validate price to ensure it's a valid positive double
      if (Portfolio.checkIfDouble(price, true) == false) {
        errorMessage += "Price must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "Price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Set text to error message if validation fails

      // If no errors are found, update the investment's price and display the result
      if (errorMessage.equals("")) {
        messageBox.setText(Portfolio.update(Portfolio.getInvestment(indexInv),
            Double.parseDouble(price)));
      }
    }
  }

  // Logic for Search button in search menu
  /**
   * ActionListener for the 'Search' button in the search menu.
   * This listener validates the input fields and performs a search based on the
   * criteria.
   */
  private class SearchInvestmentListener implements ActionListener {

    /**
     * Handles the action of clicking the 'Search' button.
     * Validates the input fields for symbol, name, and price range, and performs a
     * search if validation passes.
     * Displays error messages in the messageBox if the validation fails.
     */
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the search criteria from input fields
      String symbol = inputField1.getText();
      String nameKeys = inputField2.getText();
      String lowPrice = inputField3.getText();
      String highPrice = inputField4.getText();

      // Validate low price input
      if (Portfolio.checkIfDouble(lowPrice, false) == false) {
        errorMessage += "Low price must be a valid, positive double.\n\n";
      } else if (!lowPrice.equals("") && Double.parseDouble(lowPrice) < 0) {
        errorMessage += "Low price must be a positive double >= to 0.\n\n";
      }

      // Validate high price input
      if (Portfolio.checkIfDouble(highPrice, false) == false) {
        errorMessage += "High price must be a valid, positive double.\n\n";
      } else if (!highPrice.equals("") && Double.parseDouble(highPrice) < 0) {
        errorMessage += "High price must be a positive double greater >= to 0.\n\n";
      }

      // Validate that highPrice is greater or equal to lowPrice if both are specified
      if (Portfolio.checkIfDouble(lowPrice, true) == true
          && Portfolio.checkIfDouble(highPrice, true) == true) {
        if (Double.parseDouble(highPrice) < Double.parseDouble(lowPrice)) {
          errorMessage += "High price must be greater or equal to low price.\n\n";
        }
      }

      messageBox.setText(errorMessage); // Set text to error message if validation fails

      // Set high/low price to "-1" if not specified (indicating no price filter)
      if (lowPrice.equals("")) {
        lowPrice = "-1";
      }
      if (highPrice.equals("")) {
        highPrice = "-1";
      }

      // If no errors are found, perform the search and display the results
      if (errorMessage.equals("")) {
        messageBox.setText(
            Portfolio.search(symbol, nameKeys, Double.parseDouble(lowPrice), Double.parseDouble(highPrice)));
      }
    }
  }

  /**
   * Creates the main window for the GUI.
   * Sets up the JFrame, panels, and main menu for the application.
   */
  private void prepareGUI() {
    // Setup the window with title, size, and layout
    setTitle("ePortfolio");
    setSize(900, 600);
    setLayout(new BorderLayout());
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Setup panels for control and dynamic content
    controlPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    dynamicPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Set background color for both panels
    controlPanel.setBackground(guiColorWhite);
    dynamicPanel.setBackground(guiColorWhite);

    // Show the main menu when the window is prepared
    showMainMenu();

    // Add panels to the window layout
    add(dynamicPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.NORTH);
  }

  /**
   * Creates the menu bar for the GUI.
   * Adds a 'Commands' menu with options like Buy, Sell, Update, etc.
   */
  private void showMenuBar() {
    // Create a JMenuBar
    JMenuBar menuBar = new JMenuBar();

    // Create the "Commands" menu
    JMenu commandsMenu = new JMenu("Commands");

    // Create menu items for each command
    JMenuItem buyMenuItem = new JMenuItem("Buy");
    buyMenuItem.addActionListener(new BuyListener()); // Attach listener to Buy option

    JMenuItem sellMenuItem = new JMenuItem("Sell");
    sellMenuItem.addActionListener(new SellListener()); // Attach listener to Sell option

    JMenuItem updateMenuItem = new JMenuItem("Update");
    updateMenuItem.addActionListener(new UpdateListener()); // Attach listener to Update option

    JMenuItem getGainMenuItem = new JMenuItem("Get Gain");
    getGainMenuItem.addActionListener(new GetGainListener()); // Attach listener to Get Gain option

    JMenuItem searchMenuItem = new JMenuItem("Search");
    searchMenuItem.addActionListener(new SearchListener()); // Attach listener to Search option

    JMenuItem quitMenuItem = new JMenuItem("Quit");
    quitMenuItem.addActionListener(new QuitListener()); // Attach listener to Quit option

    // Add menu items to the "Commands" menu
    commandsMenu.add(buyMenuItem);
    commandsMenu.add(sellMenuItem);
    commandsMenu.add(updateMenuItem);
    commandsMenu.add(getGainMenuItem);
    commandsMenu.add(searchMenuItem);
    commandsMenu.add(quitMenuItem);

    // Add the "Commands" menu to the menu bar
    menuBar.add(commandsMenu);

    // Set the menu bar for the JFrame
    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(controlPanel); // Ensure correct frame context
    frame.setJMenuBar(menuBar);

    // Remove any previously added buttons to prevent overlap
    controlPanel.removeAll(); // Clear existing components
    controlPanel.revalidate(); // Revalidate layout
    controlPanel.repaint(); // Repaint the panel to show the new menu
  }

  /**
   * Displays the main menu in the dynamic panel.
   * Sets up the initial message and scrollable message box.
   */
  private void showMainMenu() {
    modifyScrollMessageBox(firstMessage, 16, -1); // Set the initial message in the scrollable box

    dynamicPanel.removeAll(); // Clear previous content
    dynamicPanel.add(messageScrollBars, BorderLayout.CENTER); // Add the scrollable message box
    dynamicPanel.revalidate(); // Revalidate the panel layout to reflect changes
  }

  /**
   * Displays the buy menu for purchasing investments.
   * The buy menu allows the user to input investment details and choose between
   * stock or mutual fund.
   */
  private void showBuyMenu() {
    // Create panels for layout
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(5, 2, 0, 10)), -1, -1, guiColorWhite);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiColorWhite);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Create combo box for investment type (stock or mutual fund)
    String[] comboBoxOptions = { "stock", "mutualfund" };
    comboBox1 = new JComboBox<>(comboBoxOptions);

    // Create text fields for user input (Symbol, Name, Quantity, Price)
    inputField1 = new JTextField(); // Symbol
    inputField2 = new JTextField(); // Name
    inputField3 = new JTextField(); // Quantity
    inputField4 = new JTextField(); // Price

    // Reset button
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, true);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Buy button
    JButton buyButton = formatButton(new JButton("Buy"), 100, 150, true);
    BuyInvestmentListener buyListener = new BuyInvestmentListener();
    buyButton.addActionListener(buyListener);

    // Modify borders of the button panel for better button fit
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to display messages properly
    modifyScrollMessageBox("", 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Type"), Font_Size));
    inputPanel.add(comboBox1);
    inputPanel.add(formatLabel(new JLabel("Symbol"), Font_Size));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Name"), Font_Size));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Quantity"), Font_Size));
    inputPanel.add(inputField3);
    inputPanel.add(formatLabel(new JLabel("Price"), Font_Size));
    inputPanel.add(inputField4);

    // Add buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(buyButton);

    // Add labels and the message scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), Font_Size), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add all panels to the backPanel for layout organization
    backPanel.add(formatLabel(new JLabel("Buying an investment"), 22), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all existing panels and add the new backPanel
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /**
   * Displays the sell menu for selling investments.
   * The sell menu allows the user to input the symbol, quantity, and price of the
   * investment to sell.
   * It also checks if there are any existing investments to sell.
   */
  private void showSellMenu() {
    boolean investmentsExist = true; // True if investments exist
    String boxMessage = ""; // Message to display in the message box

    // Check if investments exist, set message accordingly
    if (Portfolio.getInvestListSize() == 0) {
      investmentsExist = false;
      boxMessage = "- No investments exist -";
    }

    // Create panels for layout
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(3, 2, 0, 30)), -1, -1, guiColorWhite);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiColorWhite);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Create text fields for user input (Symbol, Quantity, Price)
    inputField1 = createTextField(investmentsExist); // Symbol
    inputField2 = createTextField(investmentsExist); // Quantity
    inputField3 = createTextField(investmentsExist); // Price

    // Reset button
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, investmentsExist);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Sell button
    JButton sellButton = formatButton(new JButton("Sell"), 100, 150, investmentsExist);
    SellInvestmentListener sellListener = new SellInvestmentListener();
    sellButton.addActionListener(sellListener);

    // Modify borders of the button panel for better button fit
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to display messages properly
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add input fields and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), Font_Size));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Quantity"), Font_Size));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Price"), Font_Size));
    inputPanel.add(inputField3);

    // Add buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(sellButton);

    // Add labels and message box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), Font_Size), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add all panels to the backPanel for layout organization
    backPanel.add(formatLabel(new JLabel("Selling an investment"), Font_Size), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all existing panels and add the new backPanel
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /**
   * Displays the update menu, allowing users to update the details of an
   * investment.
   * If there are no investments, a message is shown indicating that no
   * investments exist.
   */
  private void showUpdateMenu() {
    boolean investmentsExist = true; // true if investments exist
    String boxMessage = ""; // message to display in message box

    // Check if investments exist in the portfolio
    if (Portfolio.getInvestListSize() == 0) {
      investmentsExist = false; // Set to false if no investments exist
      boxMessage = "- No investments exist -"; // Set message to show to user
    }

    // Create panels for layout
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(3, 2, 0, 30)), -1, -1, guiColorWhite);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(3, 1, 10, 15)), -1, 300, guiColorWhite);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Create text fields for user input
    inputField1 = createTextField(false); // Symbol input field
    inputField2 = createTextField(false); // Name input field
    inputField3 = createTextField(investmentsExist); // Price input field

    // Previous button to navigate through investments
    prevButton = formatButton(new JButton("Prev"), 100, 150,
        investmentsExist && Portfolio.getInvestListSize() > 1 && indexInv != 0); // Enable only if more than 1
                                                                                 // investment
    PrevListener prevListener = new PrevListener();
    prevButton.addActionListener(prevListener); // Add action listener for "Prev" button

    // Next button to navigate to next investment
    nextButton = formatButton(new JButton("Next"), 100, 150,
        investmentsExist && Portfolio.getInvestListSize() > 1); // Enable only if more than 1 investment
    NextListener nextListener = new NextListener();
    nextButton.addActionListener(nextListener); // Add action listener for "Next" button

    // Save button to save updated investment information
    JButton saveButton = formatButton(new JButton("Save"), 100, 150, investmentsExist);
    UpdateInvestmentListener updateListener = new UpdateInvestmentListener();
    saveButton.addActionListener(updateListener); // Add action listener for "Save" button

    // Modify the button panel border for better spacing
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the message box to fit properly with the message
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add labels and text fields to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), Font_Size)); // Add label for symbol
    inputPanel.add(inputField1); // Add symbol text field
    inputPanel.add(formatLabel(new JLabel("Name"), Font_Size)); // Add label for name
    inputPanel.add(inputField2); // Add name text field
    inputPanel.add(formatLabel(new JLabel("Price"), Font_Size)); // Add label for price
    inputPanel.add(inputField3); // Add price text field

    // Add buttons to the button panel
    buttonPanel.add(prevButton); // Add previous button
    buttonPanel.add(nextButton); // Add next button
    buttonPanel.add(saveButton); // Add save button

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), Font_Size), BorderLayout.NORTH); // Message label
    messagePanel.add(messageScrollBars, BorderLayout.CENTER); // Scrollable message box

    // Add panels to the backPanel (main container for the update menu)
    backPanel.add(formatLabel(new JLabel("Updating investments"), Font_Size), BorderLayout.NORTH); // Title label
    backPanel.add(inputPanel, BorderLayout.CENTER); // Input panel in the center
    backPanel.add(buttonPanel, BorderLayout.EAST); // Button panel on the right
    backPanel.add(messagePanel, BorderLayout.SOUTH); // Message panel at the bottom

    // Remove all existing panels and add the new update menu panels
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate(); // Revalidate the panel layout
  }

  /**
   * Displays the get gain menu.
   * This method retrieves the total gain from the portfolio and displays it in a
   * GUI.
   */
  private void showGainMenu() {
    String boxMessage = Portfolio.getSingleGain();

    // Check if investments exist
    if (Portfolio.getInvestListSize() == 0) {
      boxMessage = "- No investments exist -";
    }

    // Create panels for organizing the layout of the gain menu
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(1, 2, 0, 30)), -1, -1, guiColorWhite);
    JPanel fillerPanel = formatPanel(new JPanel(new GridLayout(1, 1)), 100, 350, guiColorWhite);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Create text field for displaying the total gain
    inputField1 = createTextField(false); // Total Gain
    inputField1.setText(Portfolio.getGain());

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 300);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Total gain"), Font_Size));
    inputPanel.add(inputField1);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Individual gains"), Font_Size), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel for layout arrangement
    backPanel.add(formatLabel(new JLabel("Getting total gain"), Font_Size), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(fillerPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /**
   * Displays the search menu.
   * This method allows the user to search for investments by various criteria.
   */
  private void showSearchMenu() {
    boolean investmentsExist = true; // true if investments exist
    String boxMessage = ""; // message to display in message box

    // Check if investments exist
    if (Portfolio.getInvestListSize() == 0) {
      investmentsExist = false;
      boxMessage = "- No investments exist -";
    }

    // Create panels for organizing the search menu layout
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(4, 2, 0, 20)), -1, -1, guiColorWhite);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiColorWhite);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiColorWhite);

    // Create text fields for input (Symbol, Name Keywords, Low Price, High Price)
    inputField1 = createTextField(investmentsExist); // Symbol
    inputField2 = createTextField(investmentsExist); // Name Keywords
    inputField3 = createTextField(investmentsExist); // Low Price
    inputField4 = createTextField(investmentsExist); // High Price

    // Reset button that clears the input fields
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, investmentsExist);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Search button that triggers the search action
    JButton searchButton = formatButton(new JButton("Search"), 100, 150, investmentsExist);
    SearchInvestmentListener searchListener = new SearchInvestmentListener();
    searchButton.addActionListener(searchListener);

    // Modify the borders of the button panel so buttons fit better
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), Font_Size));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Name keywords"), Font_Size));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Low price"), Font_Size));
    inputPanel.add(inputField3);
    inputPanel.add(formatLabel(new JLabel("High price"), Font_Size));
    inputPanel.add(inputField4);

    // Add all the buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(searchButton);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Search results"), Font_Size), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel for layout arrangement
    backPanel.add(formatLabel(new JLabel("Searching investments"), Font_Size), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /**
   * Formats a JLabel based on the passed values.
   * 
   * @param label    is the JLabel to format
   * @param fontSize is the font size of the JLabel
   * @return the formatted JLabel
   */
  private JLabel formatLabel(JLabel label, int fontSize) {
    label.setFont(new Font("Courier", Font.PLAIN, fontSize));
    label.setForeground(guiColorBlue);
    label.setBorder(new EmptyBorder(0, 10, 10, 10));
    return label;
  }

  /**
   * Changes the message box based on the passed values.
   * 
   * @param message  displayed in the box
   * @param fontSize is the font size of the message box
   * @param height   is the height of the message box
   */
  private void modifyScrollMessageBox(String message, int fontSize, int height) {
    messageBox = new JTextArea(message);
    messageScrollBars = new JScrollPane(messageBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    messageBox.setBackground(guiColorWhite);
    messageBox.setForeground(Color.BLACK); // Or any other dark color you prefer
    messageBox.setLineWrap(true);
    messageBox.setWrapStyleWord(true);
    messageBox.setFont(new Font("Courier", Font.PLAIN, fontSize));
    messageBox.setBorder(new EmptyBorder(10, 10, 10, 10));
    messageBox.setEditable(false);

    if (height > 0) {
      messageScrollBars.setPreferredSize(new Dimension(800, height));
    }
  }

  /**
   * Displays investment information for an investment in the non-editable boxes
   * in the update menu.
   * 
   * @param index the index of the investment to display
   */
  private void setUpdateBoxes(int index) {
    Investment investment = Portfolio.getInvestment(index);
    inputField1.setText(investment.getSymbol());
    inputField2.setText(investment.getName());
  }

  /**
   * Resets all four text fields to empty values.
   * This method clears the input fields used for searching and updating
   * investments.
   */
  private void resetTextFields() {
    inputField1.setText("");
    inputField2.setText("");
    inputField3.setText("");
    inputField4.setText("");
  }

  /**
   * Returns a new JTextField based on the editable state.
   * 
   * @param editable is true if the user can edit the text field
   * @return returns the new text field
   */
  private JTextField createTextField(boolean editable) {
    JTextField field = new JTextField();
    if (editable == false) {
      field.setEditable(false);
      field.setForeground(Color.white);
      field.setBackground(Color.darkGray);
      field.setText("");
    }
    return field;
  }

  /**
   * Formats a JPanel based on the passed values.
   * 
   * @param panel  is the panel to format
   * @param height is the height of the panel
   * @param width  is the width of the panel
   * @param color  is the color of the panel
   * @return the formatted JPanel
   */
  private JPanel formatPanel(JPanel panel, int height, int width, Color color) {
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.setBackground(color);

    if (width > 0 && height > 0) {
      panel.setPreferredSize(new Dimension(width, height));
    }
    return panel;
  }

  /**
   * Formats a JButton based on passed values
   * 
   * @param button  is the button to format
   * @param height  is the height in pixels of the button
   * @param width   is the width in pixels of the button
   * @param enabled true if the button should be enabled
   * @return the formatted JButton
   */
  private JButton formatButton(JButton button, int height, int width, boolean enabled) {
    button.setBorder(new EmptyBorder(10, 10, 10, 10));
    button.setEnabled(enabled);

    if (width > 0 && height > 0) {
      button.setPreferredSize(new Dimension(width, height));
    }
    return button;
  }

}