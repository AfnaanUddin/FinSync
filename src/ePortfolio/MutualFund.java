package ePortfolio;

/**
 * Represents a Mutual Fund investment in the ePortfolio system.
 * A Mutual Fund is a type of Investment with a fixed redemption fee
 * when units are sold. This class provides methods to calculate
 * the book value and payment, as well as to adjust the book value
 * when mutual fund units are sold.
 * 
 * @see Investment
 */
public class MutualFund extends Investment {

    /**
     * The fixed redemption fee applied to each mutual fund selling transaction.
     */
    private static final double REDEMPTION_FEE = 45.00;

    /**
     * Constructs a new MutualFund object with the specified details.
     * 
     * @param symbol   the unique symbol identifying the mutual fund
     * @param name     the name of the mutual fund
     * @param quantity the number of units of the mutual fund
     * @param price    the price per unit of the mutual fund
     * @throws Exception if invalid arguments are provided (e.g., negative values)
     */
    public MutualFund(String symbol, String name, int quantity, double price) throws Exception {
        super(symbol, name, quantity, price); // Call the parent class constructor
    }

    /**
     * Creates a copy of an existing MutualFund object.
     * 
     * @param mutualfund1 the MutualFund object to copy
     */
    public MutualFund(MutualFund mutualfund1) {
        super(mutualfund1); // Call the copy constructor of the parent class
    }

    /**
     * Calculates and updates the book value for this mutual fund after a purchase.
     * The book value is the total cost of the mutual fund units purchased.
     * 
     * @param quantity the number of mutual fund units purchased
     * @param price    the price per mutual fund unit
     */
    @Override
    public void calculateBookValue(int quantity, double price) {
        // Compute the total cost of the purchased mutual fund units
        double result = quantity * price;
        // Add the cost to the current book value
        this.setBookValue(this.getBookValue() + result);
    }

    /**
     * Calculates the payment received after selling a specified quantity of mutual fund units.
     * The payment excludes the redemption fee.
     * 
     * @param quantity the number of mutual fund units sold
     * @return the total payment after deducting the redemption fee
     */
    @Override
    public double payment(int quantity) {
        // Compute the payment for the sold mutual fund units, deducting the redemption fee
        double result = quantity * this.getPrice() - REDEMPTION_FEE;
        return result;
    }

    /**
     * Adjusts the book value of the mutual fund after selling a specified quantity of units.
     * The adjustment is based on the proportion of the mutual fund that remains.
     * 
     * @param quantityOld the total quantity of mutual fund units before selling
     */
    @Override
    public void bValueSellC(int quantityOld) {
        // Calculate the adjusted book value based on the remaining mutual fund quantity
        double result = this.getBookValue() * this.getQuantity() / quantityOld;
        this.setBookValue(result); // Update the book value
    }
}