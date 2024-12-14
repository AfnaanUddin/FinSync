package ePortfolio;

/**
 * Represents a Stock investment in the ePortfolio system.
 * A Stock is a type of Investment with a fixed commission fee
 * for buying and selling transactions.
 * This class provides methods to calculate the book value and payment,
 * as well as to adjust the book value when stocks are sold.
 * 
 * @see Investment
 */
public class Stock extends Investment {

    /**
     * The fixed commission fee applied to each stock transaction.
     */
    private static final double COMMISSION = 9.99;

    /**
     * Constructs a new Stock object with the specified details.
     * 
     * @param symbol   the unique symbol identifying the stock
     * @param name     the name of the stock
     * @param quantity the number of units of the stock
     * @param price    the price per unit of the stock
     * @throws Exception if invalid arguments are provided (e.g., negative values)
     */
    public Stock(String symbol, String name, int quantity, double price) throws Exception {
        super(symbol, name, quantity, price); // Call the parent class constructor
    }

    /**
     * Creates a copy of an existing Stock object.
     * 
     * @param stock1 the Stock object to copy
     */
    public Stock(Stock stock1) {
        super(stock1); // Call the copy constructor of the parent class
    }

    /**
     * Calculates and updates the book value for this stock after a purchase.
     * The book value is the total cost of the stock, including commission fees.
     * 
     * @param quantity the number of stocks purchased
     * @param price    the price per stock unit
     */
    @Override
    public void calculateBookValue(int quantity, double price) {
        // Compute the total cost of the purchased stocks, including commission
        double result = (quantity * price) + COMMISSION;
        // Add the cost to the current book value
        this.setBookValue(this.getBookValue() + result);
    }

    /**
     * Calculates the payment received after selling a specified quantity of stocks.
     * The payment excludes the commission fee.
     * 
     * @param quantity the number of stocks sold
     * @return the total payment after deducting the commission fee
     */
    @Override
    public double payment(int quantity) {
        // Compute the payment for the sold stocks, deducting the commission
        double result = quantity * this.getPrice() - COMMISSION;
        return result;
    }

    /**
     * Adjusts the book value of the stock after selling a specified quantity.
     * The adjustment is based on the proportion of the stock that remains.
     * 
     * @param quantityOld the total quantity of stocks before selling
     */
    @Override
    public void bValueSellC(int quantityOld) {
        // Calculate the adjusted book value based on the remaining stock quantity
        double result = this.getBookValue() * this.getQuantity() / quantityOld;
        this.setBookValue(result); // Update the book value
    }
}