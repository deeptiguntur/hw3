
// package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;
import java.awt.*;


public class TestExample {

    private ExpenseTrackerModel model;
    private ExpenseTrackerView view;
    private ExpenseTrackerController controller;

    @Before
    public void setup() {
        model = new ExpenseTrackerModel();
        view = new ExpenseTrackerView();
        controller = new ExpenseTrackerController(model, view);
    }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }

    public void checkTransaction(double amount, String category, Transaction transaction) {
        assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        } catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }

    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add a transaction
        double amount = 50.0;
        String category = "food";
        assertTrue(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains only
        // the added transaction
        assertEquals(1, model.getTransactions().size());

        // Check the contents of the list
        Transaction firstTransaction = model.getTransactions().get(0);
        checkTransaction(amount, category, firstTransaction);

        // Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }

    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add and remove a transaction
        double amount = 50.0;
        String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);

        // Pre-condition: List of transactions contains only
        // the added transaction
        assertEquals(1, model.getTransactions().size());
        Transaction firstTransaction = model.getTransactions().get(0);
        checkTransaction(amount, category, firstTransaction);

        assertEquals(amount, getTotalCost(), 0.01);

        // Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);

        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());

        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }

    @Test
    public void testAddTransactionWithAmountAndCategory() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add a transaction with amount 50.00 and category "food"
        double amount = 50.0;
        String category = "food";
        assertTrue(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains only the added transaction
        assertEquals(1, model.getTransactions().size());

        // Check the contents of the list
        Transaction firstTransaction = model.getTransactions().get(0);
        checkTransaction(amount, category, firstTransaction);

        // Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }

    @Test
    public void testFilterByAmount() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add a transaction with amount 50.00 and category "food"
        double amount = 50.0;
        String category = "food";
        assertTrue(controller.addTransaction(amount, category));

        amount = 20.0;
        category = "bills";
        assertTrue(controller.addTransaction(amount, category));

        amount = 10.0;
        category = "food";
        assertTrue(controller.addTransaction(amount, category));

        amount = 100.0;
        category = "other";
        assertTrue(controller.addTransaction(amount, category));

        amount = 50.0;
        category = "bills";
        assertTrue(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains only the added transaction
        assertEquals(5, model.getTransactions().size());

        // Check the total amount
        assertEquals(230.0, getTotalCost(), 0.01);

        AmountFilter amountFilter = new AmountFilter(50);
        controller.setFilter(amountFilter);
        controller.applyFilter();
        List<Transaction> filteredTransactions = amountFilter.filter(model.getTransactions());
        
        assertEquals(2, filteredTransactions.size());
        
        checkTransaction(model.getTransactions().get(0).getAmount(), model.getTransactions().get(0).getCategory(), filteredTransactions.get(0));
        checkTransaction(model.getTransactions().get(4).getAmount(), model.getTransactions().get(4).getCategory(), filteredTransactions.get(1));

        Color green = new Color(173, 255, 168);

        TableCellRenderer renderer1 = view.getTransactionsTable().getCellRenderer(0, 0);
        Component c1 = view.getTransactionsTable().prepareRenderer(renderer1, 0, 0);
        assertEquals(c1.getBackground().getRGB(), green.getRGB());

        TableCellRenderer renderer2 = view.getTransactionsTable().getCellRenderer(4, 0);
        Component c2 = view.getTransactionsTable().prepareRenderer(renderer2, 4, 0);
        assertEquals(c2.getBackground().getRGB(), green.getRGB());

    }

    @Test
    public void testFilterByCategory() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());

        // Perform the action: Add a transaction with amount 50.00 and category "food"
        double amount = 50.0;
        String category = "food";
        assertTrue(controller.addTransaction(amount, category));

        amount = 20.0;
        category = "bills";
        assertTrue(controller.addTransaction(amount, category));

        amount = 10.0;
        category = "food";
        assertTrue(controller.addTransaction(amount, category));

        amount = 100.0;
        category = "other";
        assertTrue(controller.addTransaction(amount, category));

        amount = 50.0;
        category = "bills";
        assertTrue(controller.addTransaction(amount, category));

        // Post-condition: List of transactions contains only the added transaction
        assertEquals(5, model.getTransactions().size());

        // Check the total amount
        assertEquals(230.0, getTotalCost(), 0.01);

        CategoryFilter categoryFilter = new CategoryFilter("bills");
        // controller.setFilter(categoryFilter);
        // controller.applyFilter();

        List<Transaction> filteredTransactions = categoryFilter.filter(model.getTransactions());
        controller.setFilter(categoryFilter);
        controller.applyFilter();
        
        assertEquals(2, filteredTransactions.size());
        
        checkTransaction(model.getTransactions().get(1).getAmount(), model.getTransactions().get(1).getCategory(), filteredTransactions.get(0));
        checkTransaction(model.getTransactions().get(4).getAmount(), model.getTransactions().get(4).getCategory(), filteredTransactions.get(1));


        Color green = new Color(173, 255, 168);

        TableCellRenderer renderer1 = view.getTransactionsTable().getCellRenderer(1, 0);
        Component c1 = view.getTransactionsTable().prepareRenderer(renderer1, 1, 0);
        assertEquals(c1.getBackground().getRGB(), green.getRGB());

        TableCellRenderer renderer2 = view.getTransactionsTable().getCellRenderer(4, 0);
        Component c2 = view.getTransactionsTable().prepareRenderer(renderer2, 4, 0);
        assertEquals(c2.getBackground().getRGB(), green.getRGB());
    }

  @Test
  public void testUndoAllowed() {
    // Pre-condition: List of transactions is empty
    assertEquals(0, model.getTransactions().size());

    // Perform the action: Add a transaction
    double amount = 50.0;
    String category = "food";
    assertTrue(controller.addTransaction(amount, category));

    // Post-condition: List of transactions contains the added transaction
    assertEquals(1, model.getTransactions().size());
    Transaction addedTransaction = model.getTransactions().get(0);
    checkTransaction(amount, category, addedTransaction);

    // Check the total amount
    assertEquals(amount, getTotalCost(), 0.01);

    // Perform the action: Undo the addition
    int lastIndex = model.getTransactions().size() - 1;
    controller.undoTransaction(lastIndex);

    // Post-condition: List of transactions is empty
    List<Transaction> transactions = model.getTransactions();
    assertEquals(0, transactions.size());

    // Check the total cost after undoing the addition
    double totalCost = getTotalCost();
    assertEquals(0.00, totalCost, 0.01);
  }


    
}
