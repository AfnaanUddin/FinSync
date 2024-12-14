package ePortfolio;

import java.util.*;
import java.io.*;

/**
 * The Portfolio class manages a collection of investments (either Stock or
 * MutualFund).
 * It provides methods for adding, removing, and updating investments, as well
 * as reading from
 * and writing to files. Additionally, the class maintains a keyword index for
 * searching investments.
 */
public class Portfolio {

    /**
     * Default constructor for the Portfolio class.
     */
    public Portfolio() {
    }

    // List to store all investments (stocks and mutual funds)
    private static ArrayList<Investment> investments = new ArrayList<>(1);

    // Map to store keywords for searching investments (indexed by investment names)
    private static HashMap<String, ArrayList<Integer>> IndexofKeyword = new HashMap<>();

    // Variable to store the name of the file where investments will be saved
    private static String fileSaveName = "";

    /**
     * Writes the current investments to the specified file.
     * 
     * @param f_Name The file name where investments will be written.
     */
    public void writeInvestment(String f_Name) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter(f_Name))) {
            // Iterate through all investments and write their details to the file
            for (Investment investment : investments) {
                write.write("Type = \"" + (investment instanceof Stock ? "Stock" : "MutualFund") + "\"\n");
                write.write("Symbol = \"" + investment.getSymbol() + "\"\n");
                write.write("Name = \"" + investment.getName() + "\"\n");
                write.write("Quantity = \"" + investment.getQuantity() + "\"\n");
                write.write("Price = \"" + investment.getPrice() + "\"\n");
                write.write("BookValue = \"" + investment.getBookValue() + "\"\n");
            }

            System.out.println("The investments were successfully written to: " + f_Name);
        } catch (IOException e) {
            // Handle any IO errors while writing to the file
            System.out.println("Error in writing the investments to the file: " + f_Name);
        }
    }

    /**
     * Reads investments from the specified file and adds them to the portfolio.
     * The file format must contain information about the investment type, symbol,
     * name, quantity, price, and book value.
     * 
     * @param f_Name The file name from which investments will be read.
     */
    public void readInvestments(String f_Name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(f_Name))) {
            String line;
            Investment investment = null;
            String type = "", symbol = "", name = "";
            int quantity = 0;
            double price = 0.0, bookValue = 0.0;

            // Process each line in the file
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Parse each line to extract key-value pairs
                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.out.println("The line format is invalid: " + line);
                    continue; // Skip invalid lines
                }

                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim().replace("\"", "");

                // Switch based on the key and assign appropriate values
                switch (key) {
                    case "type":
                        type = value;
                        break;
                    case "symbol":
                        symbol = value;
                        break;
                    case "name":
                        name = value;
                        break;
                    case "quantity":
                        try {
                            quantity = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid quantity format in line: " + line);
                            quantity = -1; // Invalid quantity, skip this entry
                        }
                        break;
                    case "price":
                        try {
                            price = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid price format in line: " + line);
                            price = -1.0; // Invalid price, skip this entry
                        }
                        break;
                    case "bookvalue":
                        try {
                            bookValue = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid book value format in line: " + line);
                            bookValue = -1.0; // Invalid book value, skip this entry
                        }
                        break;
                    default:
                        System.out.println("Warning: Unknown attribute " + key + " in line: " + line);
                        break;
                }

                // Once all fields are collected, create the investment object if the entry is
                // complete
                if (!type.isEmpty() && !symbol.isEmpty() && !name.isEmpty() && quantity > 0 && price > 0.0
                        && bookValue >= 0.0) {
                    if (type.equalsIgnoreCase("stock")) {
                        try {
                            investment = new Stock(symbol, name, quantity, price);
                        } catch (Exception e) {
                            System.out.println("Error in creating the Stock investment: " + e.getMessage());
                            // Skip this investment and continue processing
                            continue;
                        }
                    } else if (type.equalsIgnoreCase("mutualfund")) {
                        try {
                            investment = new MutualFund(symbol, name, quantity, price);
                        } catch (Exception e) {
                            System.out.println("Error in creating the MutualFund investment: " + e.getMessage());
                            // Skip this investment and continue processing
                            continue;
                        }
                    }

                    // Add the investment to the portfolio and update the keyword index
                    if (investment != null) {
                        investments.add(investment);
                        addToKeywordIndex(name); // Assuming this method adds keyword index
                        System.out.println("Investment has been loaded successfully: " + investment); // Optional: Log
                                                                                                      // successful
                                                                                                      // loading
                    }

                    // Reset for the next entry
                    type = "";
                    symbol = "";
                    name = "";
                    quantity = 0;
                    price = 0.0;
                    bookValue = 0.0;
                }
            }

            // Confirmation message after reading the investments
            System.out.println("Investments have been read from the file successfully: " + f_Name);

        } catch (IOException e) {
            System.out.println("Error in loading the investments from the file: " + e.getMessage());
        }
    }

    /**
     * Buys a new investment (either stock or mutual fund) and adds it to the
     * portfolio.
     * If an investment with the same symbol already exists, it updates the existing
     * investment.
     * 
     * @param type     The type of investment ("stock" or "mutualfund").
     * @param symbol   The symbol of the investment (e.g., stock ticker).
     * @param name     The name of the investment.
     * @param quantity The quantity to buy.
     * @param price    The price per unit of the investment.
     * @return A message indicating the success or failure of the purchase.
     */
    public static String buy(String type, String symbol, String name, int quantity, double price) {
        Investment newInvestment = null;

        try {
            // Create a new investment based on the type
            if (type.equalsIgnoreCase("stock")) {
                newInvestment = new Stock(symbol, name, quantity, price);
            } else if (type.equalsIgnoreCase("mutualfund")) {
                newInvestment = new MutualFund(symbol, name, quantity, price);
            } else {
                return "Invalid investment Type '" + type + "'.";
            }
        } catch (Exception e) {
            return "Failed to Create The Investment. The Details are as shown: " + e.toString();
        }

        // Check if the investment symbol already exists in the opposite investment type
        int index = findInvestment(symbol);
        if (index != -1) {
            Investment newInvestment1 = investments.get(index);

            if ((type.equalsIgnoreCase("stock") && newInvestment1 instanceof MutualFund) ||
                    (type.equalsIgnoreCase("mutualfund") && newInvestment1 instanceof Stock)) {
                return "Error: Symbol '" + symbol
                        + "' already exists in the other investment type.";
            } else {
                // Update the existing investment's quantity and price
                newInvestment1.setQuantity(quantity + newInvestment1.getQuantity());
                newInvestment1.setPrice(price);
                newInvestment1.calculateBookValue(quantity, price);
                return quantity + " Units were successfully added to an existing investment:\n\n"
                        + newInvestment1.toString();
            }
        }

        // Calculate book value for the new investment and add it to the list
        newInvestment.calculateBookValue(quantity, price);
        investments.ensureCapacity(investments.size() + 1); // Ensure capacity for the new investment
        investments.add(newInvestment);

        // Tokenize the investment name and add keywords to the index
        addToKeywordIndex(newInvestment.getName());

        // Return success message
        return quantity + " Purchase was successfully made for " + symbol + ":\n\n" + newInvestment.toString();
    }

    /**
     * Sells a specified quantity of shares from an investment and returns the
     * payment received.
     * 
     * @param symbol   The symbol of the investment to sell.
     * @param quantity The quantity of shares to sell.
     * @param price    The price at which the shares are sold.
     * @return A message indicating the result of the sale.
     */
    public static String sell(String symbol, int quantity, double price) {
        // Find the matching investment index
        int indexMatch = findInvestment(symbol);

        // If a matching investment is found
        if (indexMatch != -1) {
            Investment sellInvestment = investments.get(indexMatch); // Get the investment object
            int oldQuantity = sellInvestment.getQuantity(); // Store the current quantity

            // Check if enough quantity is available to sell
            if (oldQuantity >= quantity) {
                sellInvestment.setPrice(price); // Update the price
                sellInvestment.setQuantity(oldQuantity - quantity); // Update the quantity
                sellInvestment.bValueSellC(oldQuantity); // Recalculate book value

                // Remove the investment if all shares are sold
                if (sellInvestment.getQuantity() == 0) {
                    removeFromkeyWordIndex(investments.indexOf(sellInvestment)); // Remove from keyword index
                    investments.remove(indexMatch); // Remove from list
                    investments.trimToSize(); // Adjust list size
                }

                // Return the payment earned from selling
                return "The payment received from selling " + quantity + " shares of " + symbol + " is: "
                        + sellInvestment.payment(quantity);
            } else {
                // Error message if selling more than available quantity
                return "ERROR: Cannot sell " + quantity + " shares when only " + oldQuantity + " exist.";
            }
        } else {
            // Error message if no investment matches the given symbol
            return "ERROR: No investments exist with symbol: '" + symbol + "'.";
        }
    }

    /**
     * Updates the price of an investment and returns the updated details.
     * 
     * @param investment1 The investment to be updated.
     * @param newPrice    The new price for the investment.
     * @return A message indicating the successful update and details of the
     *         investment.
     */
    public static String update(Investment investment1, double newPrice) {
        // Update the price of the investment
        investment1.setPrice(newPrice);

        // Return success message and updated investment details
        return String.format("Investment was successfully updated.\n%s\n\n", investment1);
    }

    /**
     * Calculates and returns the total gain of all investments.
     * 
     * @return A string representation of the total gain.
     */
    public static String getGain() {
        // Calculate total gain by iterating over the investments list
        double totalGain = investments.stream()
                .mapToDouble(Investment::Gain) // Calculate gain for each investment
                .sum(); // Sum all the gains

        return String.valueOf(totalGain); // Return the total gain as a string
    }

    /**
     * Returns the individual gain of each investment in a formatted string.
     * 
     * @return A string containing individual gains for each investment.
     */
    public static String getSingleGain() {
        // Build a string with all individual gains using StringBuilder for performance
        StringBuilder tempGain = new StringBuilder();

        // Iterate over the investment list and append the gain for each investment
        investments.forEach(investment -> {
            tempGain.append("Symbol: \"").append(investment.getSymbol()).append("\"\n")
                    .append("Gain:   ").append(investment.Gain()).append("\n\n");
        });

        return tempGain.toString(); // Return the accumulated string
    }

    /**
     * Searches for investments based on symbol, keywords, and price range.
     * 
     * @param symbol        The symbol of the investment to search for.
     * @param keywordString The keywords to filter investments.
     * @param priceLow      The minimum price of the investment.
     * @param priceHigh     The maximum price of the investment.
     * @return A string containing all the matching investments.
     */
    public static String search(String symbol, String keywordString, double priceLow, double priceHigh) {
        StringBuilder Stringmatch = new StringBuilder();
        ArrayList<Integer> index = new ArrayList<>();
        String[] Arraykeyword = keywordString.split(" "); // Split the input keywords

        // Set default values for price range if not specified
        if (priceLow == -1)
            priceLow = 0;
        if (priceHigh == -1)
            priceHigh = Integer.MAX_VALUE;

        // Process keyword search, filling indexes array
        if (!keywordString.isEmpty()) {
            index = IndexofKeyword.get(cleanInput(Arraykeyword[0])); // Get the first keyword matches
            for (int i = 1; i < Arraykeyword.length; i++) {
                if (index != null) {
                    index.retainAll(IndexofKeyword.get(cleanInput(Arraykeyword[i]))); // Retain common elements
                }
            }
        } else {
            // If no keywords are provided, consider all investments
            for (int i = 0; i < investments.size(); i++) {
                index.add(i);
            }
        }

        // Iterate through the filtered investments and check for matches
        for (int index1 : index) {
            Investment tempInvestment = investments.get(index1);
            boolean printMatch = true;

            // Check if symbol matches
            if (!symbol.isEmpty() && !tempInvestment.getSymbol().equals(symbol)) {
                printMatch = false;
            }

            // Check if price matches the range
            if (tempInvestment.getPrice() < priceLow || tempInvestment.getPrice() > priceHigh) {
                printMatch = false;
            }

            // If all conditions match, add the investment's details to the result string
            if (printMatch) {
                Stringmatch.append(tempInvestment.toString()).append("\n");
            }
        }

        return Stringmatch.toString();
    }

    /**
     * Checks if a string is non-empty if required.
     * 
     * @param string   The string to check.
     * @param required If the string is required to be non-empty.
     * @return True if the string is valid, false otherwise.
     */
    public static boolean checkifString(String string, boolean required) {
        // Return false if the string is empty and it's required
        return !(string.isEmpty() && required);
    }

    /**
     * Checks if a string can be converted to an integer.
     * 
     * @param string   The string to check.
     * @param required If the string is required to be a valid integer.
     * @return True if the string is a valid integer, false otherwise.
     */
    public static boolean checkifInt(String string, boolean required) {
        // If the string is empty and not required, return true
        if (string.isEmpty() && !required) {
            return true;
        }

        try {
            // Try parsing the string to an integer
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            // Return false if the parsing fails
            return false;
        }

        return true;
    }

    /**
     * Checks if a string can be converted to a double.
     * 
     * @param string   The string to check.
     * @param required If the string is required to be a valid double.
     * @return True if the string is a valid double, false otherwise.
     */
    public static boolean checkIfDouble(String string, boolean required) {
        // If the string is empty and not required, return true
        if (string.isEmpty() && !required) {
            return true;
        }

        try {
            // Try parsing the string to a double
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            // Return false if the parsing fails
            return false;
        }

        return true;
    }

    /**
     * Finds the index of an investment by its symbol.
     * 
     * @param symbol The symbol of the investment to search for.
     * @return The index of the investment, or -1 if not found.
     */
    public static int findInvestment(String symbol) {
        // Iterate through the investment list to find a matching symbol
        for (int i = 0; i < investments.size(); i++) {
            // If a match is found, return the index immediately
            if (investments.get(i).getSymbol().equals(symbol)) {
                return i;
            }
        }
        // Return -1 if no match is found
        return -1;
    }

    /**
     * Cleans input string by removing spaces and converting to lowercase.
     * 
     * @param input The string to clean.
     * @return The cleaned string.
     */
    private static String cleanInput(String input) {
        return input.toLowerCase().replaceAll("\\s", "");
    }

    /**
     * Prints a summary of all investments, categorized into stocks and mutual
     * funds.
     * 
     * @return a formatted string containing the investments' details
     */
    public static String printInvestments() {
        StringBuilder returnString = new StringBuilder("STOCKS===========================\n");

        // Print all stocks
        investments.stream()
                .filter(investment -> investment instanceof Stock)
                .forEach(investment -> returnString.append(investment.toString()).append("\n"));

        returnString.append("MUTUAL FUNDS=====================\n");

        // Print all mutual funds
        investments.stream()
                .filter(investment -> investment instanceof MutualFund)
                .forEach(investment -> returnString.append(investment.toString()).append("\n"));

        return returnString.toString();
    }

    /**
     * Removes the investment's index from the keyword index.
     * 
     * @param index the index of the investment to remove from the keyword index
     */
    private static void removeFromkeyWordIndex(Integer index) {
        List<String> keysToRemove = new ArrayList<>(); // To store keys to be removed

        // Iterate through all keys in the map
        for (String currentKey : IndexofKeyword.keySet()) {
            ArrayList<Integer> locations = IndexofKeyword.get(currentKey);

            // Remove the index from the locations list
            if (locations.remove(index)) {
                // Trim the size after removal
                locations.trimToSize();

                // Decrease the value of all indexes greater than the current one
                for (int j = 0; j < locations.size(); j++) {
                    if (locations.get(j) > index) {
                        locations.set(j, locations.get(j) - 1);
                    }
                }

                // If the locations list is empty, mark the key for removal
                if (locations.isEmpty()) {
                    keysToRemove.add(currentKey);
                } else {
                    // Update the map with the modified list of locations
                    IndexofKeyword.put(currentKey, locations);
                }
            }
        }

        // Remove all keys that are empty
        for (String key : keysToRemove) {
            IndexofKeyword.remove(key);
        }
    }

    /**
     * Adds an investment's index to the keyword index.
     * 
     * @param name the name of the investment to add to the keyword index
     */
    private static void addToKeywordIndex(String name) {
        // Clean up the input string: trim, replace multiple spaces, and convert to
        // lowercase
        name = name.trim().replaceAll("\\s+", " ").toLowerCase();

        // Split the cleaned name into words
        String[] words = name.split(" ");

        // Iterate through all words in the array
        for (String word : words) {
            // Get the list of locations associated with the word
            ArrayList<Integer> locations = (ArrayList<Integer>) IndexofKeyword.getOrDefault(word,
                    new ArrayList<Integer>());

            // Ensure the list contains the index of the current investment
            if (!locations.contains(investments.size() - 1)) {
                locations.add(investments.size() - 1);
            }

            // Put the updated list of locations back into the map
            IndexofKeyword.put(word, locations);
        }
    }

    /**
     * Retrieves the investment at the given index from the list.
     * 
     * @param index the index of the investment
     * @return the investment at the specified index, or a default value if the list
     *         is empty
     */
    public static Investment getInvestment(int index) {
        if (investments.isEmpty()) {
            try {
                return new Stock("-", "-", 0, 0);
            } catch (Exception e) {
                e.printStackTrace(); // You can log or handle the exception here
                return null; // Or return a default value if desired
            }
        }

        try {
            return investments.get(index % investments.size());
        } catch (Exception e) {
            e.printStackTrace(); // Handle any unexpected exceptions
            return null; // Or return a default value if desired
        }
    }

    /**
     * Returns the current size of the investments list.
     * 
     * @return the size of the investments list
     */
    public static int getInvestListSize() {
        return investments.size();
    }

    /**
     * Returns the file name where the portfolio data is saved.
     * This method provides the current file name used for saving the portfolio
     * data.
     * 
     * @return the file name (String)
     */
    public static String getFileName() {
        // Return the static fileSaveName variable which holds the name of the file
        return fileSaveName;
    }
}
