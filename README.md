# Star Stream Ledger

<details>
  <summary> <h2> Table of Contents</h2> </summary>
    <ol>
      <li> Description </li>
      <li> Features </li>
      <li> Tech Stack </li>
      <li> How to Run </li>
      <li> Examples </li>
      <li> Author </li>
    </ol>
</details>
 
### Description
This Java application reads from a .csv files, stores the transactions in memory, allows the user to add new transactions (Deposits / Payments), and provide various ways to view and filter the financial transaction data. 

The program has a focus on using menus to guide the user through different options for each functionality.

### Features 
- Add Deposits
- Add Payment 
- View all transactions
- Generate Reports for:
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
- Custom Filter View by:
  - Date Range
  - Description
  - Vendor
  - Amount Range
- Save new transactions into the .csv file

### Tech Stack
* Java
* Object Oriented Programming
* File I/O (CSV handling)
* Console based UI

### How to Run
1. Clone or download the project
2. **_Make sure the star_stream_transactions.csv is in the proper directory (root)_**
3. Compile and run program

### Examples

#### Main Menu
Home Screen with add deposits / payments functionalities, ledger for all transactions and exit program
![Home / Main Menu](https://github.com/user-attachments/assets/f8ea2644-a613-40c7-83aa-792f7aa528e2)

#### Deposit Function
Deposit functionality that prompts for description / reason, "constellation modifier" or name, and the amount of coins for the deposit  
![Deposit Example](https://github.com/user-attachments/assets/cb91b04d-a7ad-41af-9540-25ce8b77f45e)

#### Ledger Display Format
Display all transactions, with the newest transaction being at the top of the list
![Ledger All Transactions Output](https://github.com/user-attachments/assets/a2cbbe61-ecb5-4e3b-b418-8ea6dcf33777)

#### Report Display Format
Monthly report output. Displays all transaction in that time period and gives number of entries and net deposits and payments 
![Monthly Report Example](https://github.com/user-attachments/assets/4ba41583-97ce-41ba-ad89-88318b68b3a5)

#### Custom Search 
Filters all the transactions based on user input and allows for user to skip certain input as well as allow for a partial search
![Custom Search Example](https://github.com/user-attachments/assets/238a8366-2de9-414f-9112-ac32c0b83be3)

### Author
#### Kevin Nguyen 
Email: knguyen@my.yearupunited.org  
