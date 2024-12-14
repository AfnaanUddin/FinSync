package ePortfolio;

import java.util.Objects;

/**
 * Represents a generic investment in the ePortfolio system.
 * This abstract class provides a common structure and functionality
 * for specific types of investments such as Stocks and Mutual Funds.
 */
public abstract class Investment {

    /** The unique symbol representing the investment. */
    protected String Symbol;

    /** The name of the investment. */
    protected String Name;

    /** The quantity of the investment owned. */
    protected int Quantity;

    /** The price per unit of the investment. */
    protected double Price;

    /** The book value of the investment, representing its cost basis. */
    protected double bookValue;

    /**
     * Constructs a new Investment object with the specified details.
     * Validates input to ensure all fields are properly initialized.
     *
     * @param symbol   the unique symbol identifying the investment
     * @param name     the name of the investment
     * @param quantity the quantity of the investment owned
     * @param price    the price per unit of the investment
     * @throws Exception if any parameter is invalid (e.g., empty strings or
     *                   non-positive values)
     */
    public Investment(String symbol, String name, int quantity, double price) throws Exception {
        // Validate and set the symbol for the investment
        if (!symbol.equals("")) {
            this.Symbol = symbol;
        } else {
            throw new Exception("Symbol can't be left empty.");
        }

        // Validate and set the name for the investment
        if (!name.equals("")) {
            this.Name = name;
        } else {
            throw new Exception("Name can't be left empty.");
        }

        // Validate and set the quantity for the investment
        if (quantity > 0) {
            this.Quantity = quantity;
        } else {
            throw new Exception("Quantity must be greater than 0.");
        }

        // Validate and set the price for the investment
        if (price > 0) {
            this.Price = price;
        } else {
            throw new Exception("Price must be greater than 0 and should be a double value.");
        }

        // Initialize book value to 0
        this.bookValue = 0;
    }

    /**
     * Copy constructor to create a new Investment object based on an existing one.
     *
     * @param investment1 the Investment object to copy
     */
    public Investment(Investment investment1) {
        // Copy the attributes from the given investment object
        this.Symbol = investment1.Symbol;
        this.Name = investment1.Name;
        this.Quantity = investment1.Quantity;
        this.Price = investment1.Price;
        this.bookValue = investment1.bookValue;
    }

    /**
     * Abstract method to calculate and update the book value after a transaction.
     * This method must be implemented by subclasses to provide specific logic.
     *
     * @param quantity the number of units involved in the transaction
     * @param price    the price per unit involved in the transaction
     */
    public abstract void calculateBookValue(int quantity, double price);

    /**
     * Abstract method to calculate the payment received after selling a quantity of
     * the investment.
     * This method must be implemented by subclasses to provide specific logic.
     *
     * @param quantity the number of units sold
     * @return the total payment received
     */
    public abstract double payment(int quantity);

    /**
     * Adjusts the book value of the investment after selling a portion of it.
     * The adjustment is proportional to the remaining quantity.
     *
     * @param quantityOld the quantity of the investment before selling
     */
    public void bValueSellC(int quantityOld) {
        // Calculate the new book value after selling a portion
        double result = bookValue * Quantity / quantityOld;
        this.bookValue = result; // Update book value based on the remaining quantity
    }

    /**
     * Calculates the gain of the investment, which is the difference between the
     * payment for the current quantity and the book value.
     *
     * @return the calculated gain
     */
    public double Gain() {
        // Calculate the gain as the difference between payment and book value
        double result = payment(this.Quantity) - this.bookValue;
        return result;
    }

    // Getter and setter methods for each field

    /**
     * Gets the symbol of the investment.
     *
     * @return the symbol of the investment
     */
    public String getSymbol() {
        return this.Symbol;
    }

    /**
     * Sets the symbol of the investment.
     *
     * @param symbol the new symbol to set
     */
    public void setSymbol(String symbol) {
        this.Symbol = symbol;
    }

    /**
     * Gets the name of the investment.
     *
     * @return the name of the investment
     */
    public String getName() {
        return this.Name;
    }

    /**
     * Sets the name of the investment.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * Gets the quantity of the investment owned.
     *
     * @return the quantity of the investment
     */
    public int getQuantity() {
        return this.Quantity;
    }

    /**
     * Sets the quantity of the investment owned.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    /**
     * Gets the price per unit of the investment.
     *
     * @return the price per unit
     */
    public double getPrice() {
        return this.Price;
    }

    /**
     * Sets the price per unit of the investment.
     *
     * @param price the new price per unit to set
     */
    public void setPrice(double price) {
        this.Price = price;
    }

    /**
     * Gets the book value of the investment.
     *
     * @return the book value of the investment
     */
    public double getBookValue() {
        return this.bookValue;
    }

    /**
     * Sets the book value of the investment.
     *
     * @param bookValue the new book value to set
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     * Returns a string representation of the Investment object,
     * including all its details.
     *
     * @return a formatted string with the investment's details
     */
    @Override
    public String toString() {
        // Return a formatted string representing the investment's details
        return "Symbol: " + this.getSymbol() + "\n" +
                "Name: " + this.getName() + "\n" +
                "Quantity: " + this.getQuantity() + "\n" +
                "Price: " + this.getPrice() + "\n" +
                "Book Value: " + this.getBookValue();
    }

    /**
     * Compares this Investment object to another for equality based on its
     * attributes.
     *
     * @param obj the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Investment that = (Investment) obj;
        // Compare the investment attributes for equality
        return Symbol.equals(that.Symbol) &&
                Name.equals(that.Name) &&
                Quantity == that.Quantity &&
                Double.compare(that.Price, Price) == 0;
    }

    /**
     * Generates a hash code for the Investment object based on its attributes.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // Generate a hash code using the investment's attributes
        return Objects.hash(Symbol, Name, Quantity, Price);
    }
}