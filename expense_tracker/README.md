# hw1- Manual Review

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTracker
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

# Homework 3
## Undo functionality

Select any table row <br>
Click Undo button <br>
-> Transaction will be removed from table and total cost is updated <br>
<br>
Exceptional cases - <br>
1. List is empty <br>
-> Error message will be shown that row needs to be selected for undo.<br>
<br>
2. No row is selected<br>
-> Error message will be shown that row needs to be selected for undo.<br>
<br>
3. Is multiple rows are selected<br>
-> Error message will be shown that only 1 row needs to be selected.<br>
<br>
4. If last row which is total cost row is selected<br>
-> Error message will be shown that last row cannot be deleted.<br>
