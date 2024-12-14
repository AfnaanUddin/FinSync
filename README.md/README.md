FinSync with GUI

Overview:

This Java-based ePortfolio application allows users to manage an investment portfolio containing stocks and mutual funds. The program provides an interactive Graphical User Interface (GUI) for easy navigation and user interaction. Users can perform key operations such as buying, selling, updating prices, calculating total profits, and searching for investments, all through the GUI. The program also supports loading and saving investments to a file for persistent data storage.

Key Features:
	•	GUI Interface: A user-friendly graphical interface for interacting with the portfolio.
	•	Investment Management: Ability to buy, sell, update prices, and view overall gains.
	•	Search Functionality: Search for investments based on symbol, name, and price range.
	•	File I/O: Load and save investments to and from a file.
	•	Input Validation: Ensures correct input format and handles invalid commands gracefully.

Assumptions:

	•	There are two types of investments: Stock and Mutual Fund.
	•	Stocks have a $9.99 commission on trading transactions.
	•	Mutual Funds have no commission for buying, but they have a $45 redemption fee for selling.

Running the Project:

	1.	Compile the Source Code:
	•	Open a terminal or command prompt and navigate to the directory containing the Java files.
	•	Compile the source code with the following command: javac ePortfolio/*.java   
  Run the Application:
	•	To run the application, use the following command: java ePortfolio.Main saveFile.txt saveFile.txt

  	3.	Interacting with the Application:
	•	The GUI will provide buttons for various actions such as buying, selling, updating, searching investments, and calculating the total gain.
	•	Users can also interact with the data fields to enter relevant information like investment type, symbol, amount, price, etc.
	•	The system will provide feedback on the status of the operations (e.g., success, errors).

Commands & Features:

	•	Buy Investment: Allows the user to buy a stock or mutual fund by entering the relevant details.
	•	Sell Investment: Allows the user to sell part or all of an existing investment.
	•	Update Prices: Updates the prices of all investments in the portfolio.
	•	Get Gain: Calculates and displays the overall gain of the portfolio.
	•	Search Investments: Search for investments by symbol, name, or price range.
	•	Quit: Saves the portfolio to a file and exits the application.

Test Plan:

The test plan for the application covers key scenarios such as valid and invalid input handling, ensuring the application works correctly and that errors are handled gracefully.

Test Cases:

	1.	GUI Interaction:
	•	Test the GUI elements (buttons, text fields, etc.) to ensure they function correctly.
	•	Verify that input fields accept correct input and provide appropriate error messages for invalid input.
	2.	Investment Operations:
	•	Buy: Test buying new investments and adding quantities to existing investments through the GUI.
	•	Sell: Verify selling functionality, ensuring that it handles partial/full sales and generates errors for insufficient quantities.
	•	Update Prices: Test the price update functionality to ensure that it correctly updates investment prices.
	•	Get Gain: Verify that the total gain is calculated correctly, including edge cases like an empty portfolio.
	•	Search: Test the search functionality to ensure it can filter investments correctly by symbol, name, and price range.
	3.	File Operations:
	•	Load File: Test that the portfolio correctly loads data from a file.
	•	Save File: Verify that the portfolio saves its data to a file correctly when exiting the application.
	4.	Edge Cases:
	•	Test with no investments loaded to ensure the system handles an empty portfolio gracefully.
	•	Test invalid user input (e.g., incorrect price range, invalid characters) and verify that the system displays appropriate error messages.

Improvements:

	•	GUI Enhancements: Future work could include improving the user interface for better accessibility and responsiveness, such as adding icons and tooltips for buttons.
	•	Persistent Storage Options: Instead of using a simple text file, the application could support saving data in a database for more robust data management.
	•	Advanced Search Functionality: Enhance the search functionality with more advanced filters and options