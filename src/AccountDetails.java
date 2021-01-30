import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountDetails extends MysqlConnection {
	private String accountType;
	private BigInteger accountNumber;
	private String name;
	private BigInteger mobileNumber;
	private double balance;
	private String currencyType;
	
	Scanner sc= new Scanner(System.in);
	Scanner scName= new Scanner(System.in).useDelimiter(" ");
	MysqlConnection mysqlConnection = new MysqlConnection();
	
	//getters
	public String getAccountType() {
	    return accountType;
	}
	
	public BigInteger getAccountNumber() {
	    return accountNumber;
	}
	
	public String getName() {
	    return name;
	}
	
	public BigInteger getMobileNumber() {
	    return mobileNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String getCurrencyType() {
		return currencyType;
	}
	
	
	//setters
	public void setAccountType(String accountType) {
	    this.accountType = accountType;
	}
	
	public void setAccountNumber(BigInteger accountNumber) {
	    this.accountNumber=accountNumber;
	}
	
	public void setName(String name) {
	    this.name = name;
	}
	
	public void setMobileNumber(BigInteger mobileNumber) {
	    this.mobileNumber=mobileNumber;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	//CRUD - operations
	public void createAccount() {		
		System.out.print("Enter Type of Account:\n1.Saving\n2.Current\n(1/2)?");
		int type =sc.nextInt();
		String[] at = {"Savings","Current"};
		if(type == 1) {
			this.setAccountType(at[0]);
		}else if(type == 2) {
			this.setAccountType(at[1]);
		}else {
			System.out.print("Invalid Input");
			return;
		}
		System.out.print("\nEnter the Account Holder Full Name:\n");
		this.setName(scName.nextLine());
		System.out.print("\nEnter Mobile Number:\n");
		this.setMobileNumber(sc.nextBigInteger());
		System.out.print("\nEnter 16-digit account number: \n");
		accountNumber = sc.nextBigInteger();
		if(accountNumber.toString().length()>16 || accountNumber.toString().length()<16 ) {
			System.out.print("Not a 16 digit number");
			return;
		}else {
			this.setAccountNumber(accountNumber);
		}
		System.out.print("\nEnter the Amount: \n");
		this.setBalance(sc.nextInt());
		System.out.print("\nEnter the Currency: \n");
		this.setCurrencyType(sc.next());
		
		String query = "INSERT INTO ACCOUNT VALUES ("
				+ "'"+this.getAccountType()+"'"+","
				+ this.getAccountNumber()+"," 
				+ "'"+this.getName()+"'"+","
				+ this.getMobileNumber()+","
				+ this.getBalance()+","
				+ "'"+this.getCurrencyType()+"'"+");";
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			int count = statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nAccount Added in Database \n");
				this.viewAccount();
			}
			conn.close();
		}catch(Exception e) {
			System.out.println("Entered catch in createAccount");
			System.out.println(e);
		}

	}
	
	public void updateAccount() {
		System.out.print("Enter Account Number to update:\n");
		this.setAccountNumber(sc.nextBigInteger());
		if(this.findAccountNumber(this.getAccountNumber())){
			String input = new String("");
			while(!input.equals("7")) {
				Scanner sc= new Scanner(System.in);
				System.out.print("\n\nWhat do you want to update ? \n");
				System.out.print("1: Account Holder Name \n"
						+ "2: Account Type \n"
						+ "3: Mobile Number \n"
						+ "4: Credit Amount  \n"
						+ "5: Debit Amount  \n"
						+ "6: Currency Type \n"
						+ "7: Exit Update!\n"
						+ "Your Choice - ");
				input = sc.next();
				switch(input) {
					case "1": 	this.updateName(this.getAccountNumber());
								this.viewAccount();
								break;
					case "2":	this.updateAccountType(this.getAccountNumber());
								this.viewAccount();
								break;
					case "3":	this.updateMobileNumber(this.getAccountNumber());
								this.viewAccount();
								break;
					case "4":	this.updateCreditAmount(this.getAccountNumber());
								this.viewAccount();	
								break;
					case "5":	this.updateDebitAmount(this.getAccountNumber());
								this.viewAccount();	
								break;
					case "6":	this.updateCurrencyType(this.getAccountNumber());
								this.viewAccount();
								break;
					case "7": 
								break;
					default:
						System.out.println("Invalid Input! \n Try again! \n\n");
				}		
			}
		}else {
			System.out.print("Can't find Account Number in Database!!");
		}
		
	}
	
	public void viewAccount() {
		ArrayList<Object> newAccount = new ArrayList<>();
		String query = "SELECT * from ACCOUNT WHERE ACCOUNT_NUMBER="+this.getAccountNumber();

		try {

			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			ResultSet rs=statement.executeQuery(query);
			while(rs.next()) { 
				System.out.println(
					"_______________________________\n"+
					"Account Type: "+rs.getString(1)+"\n"+
					"Account Number: "+ rs.getString(2)+"\n"+
					"Account Holder: "+ rs.getString(3)+"\n"+
					"Mobile Number: "+ rs.getString(4)+"\n"+
					"Amount in Account: "+ rs.getString(5)+"\n"+
					"Currency Type: "+ rs.getString(6)+
					"\n_______________________________"
				); 
				newAccount.add(rs.getString(1));
				newAccount.add(rs.getString(2));
				newAccount.add(rs.getString(3));
				newAccount.add(rs.getString(4));
				newAccount.add(rs.getString(5));
				newAccount.add(rs.getString(6));
				
			}
			
			System.out.println("\n\n"+newAccount);
			
			conn.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void deleteAccount() {
		System.out.print("Enter Account Number to delete:\n");
		this.setAccountNumber(sc.nextBigInteger());
		System.out.print("Are you sure you want to delete " + this.getAccountNumber() + "?:(Y/N)");
		String confirm = sc.next().toUpperCase();
		if(confirm.equals("Y")) {
			String query = "DELETE FROM ACCOUNT WHERE ACCOUNT_NUMBER="+this.getAccountNumber();
			
			try {
				Connection conn = mysqlConnection.connect();
				Statement statement = conn.createStatement();
				int count= statement.executeUpdate(query);
				if(count>=1) {
					System.out.print("Account Number "+this.getAccountNumber()+" was successfully deleted!");
				}
				conn.close();
			}catch(Exception e) {
				System.out.println(e);
			}
		}else {
			return;
		}
		
		
		
	}
	
	public Boolean findAccountNumber(BigInteger accountNumber) {
		String query = "SELECT * from ACCOUNT WHERE ACCOUNT_NUMBER="+accountNumber;
		try {

			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			ResultSet rs=statement.executeQuery(query);
			if(rs.next()) {
				conn.close();
				return true;
			}else {
				conn.close();
				return false;
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
		
		
	}
	
	//update information functions
	public void updateName(BigInteger accountNumber) {
		System.out.print("\nEnter New Name:\n");
		this.setName(scName.nextLine());
		String query = "UPDATE ACCOUNT"
					+ " SET NAME ="+"'"+this.getName()+"'" 
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nNAME UPDATE SUCCESSFUL!!\n");
				
			}
			conn.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void updateAccountType(BigInteger accountNumber) {
		System.out.print("\nEnter Type of Account: \n1.Saving \n2.Current \n(1/2)?\n");
		int type=sc.nextInt();
		String[] at = {"Savings","Current"};
		if(type == 1) {
			this.setAccountType(at[0]);
		}else if(type == 2) {
			this.setAccountType(at[1]);
		}else {
			System.out.print("Invalid Input");
			return;
		}
		String query = "UPDATE ACCOUNT"
				+ " SET ACCOUNT_TYPE ="+"'"+this.getAccountType()+"'"
				+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		
			try {
				Connection conn = mysqlConnection.connect();
				Statement statement = conn.createStatement();
				int count= statement.executeUpdate(query);
				if(count>=1) {
					System.out.print("\nACCOUNT-TYPE UPDATE SUCCESSFUL!!\n");
				}
				conn.close();
			}catch(Exception e) {
				System.out.println(e);
			}
	
		
	}
	
	public void updateMobileNumber(BigInteger accountNumber) {
		System.out.print("\nEnter new Mobile Number: \n");
		this.setMobileNumber(sc.nextBigInteger());
		String query = "UPDATE ACCOUNT"
					+ " SET MOBILE_NUMBER ="+this.getMobileNumber()
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nMOBILE NUMBER UPDATE SUCCESSFUL!!\n");
			}
			conn.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	

	public void updateCreditAmount(BigInteger accountNumber) {
		System.out.print("Enter Amount to credit : \n");
		Double amount = sc.nextDouble();
		
		String selectQuery = "SELECT BALANCE FROM account WHERE ACCOUNT_NUMBER = "+this.getAccountNumber();
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			ResultSet rs=statement.executeQuery(selectQuery);
			while(rs.next()) {
				this.setBalance(rs.getDouble(1)+amount);
			}
				
		String query = "UPDATE ACCOUNT"
					+ " SET BALANCE ="+this.getBalance()
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		
		
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nAMOUNT CREDITED SUCCESSFUL!!\n");
			}
			conn.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}

		
	}

	public void updateDebitAmount(BigInteger accountNumber) {
		System.out.print("Enter Amount to debit : \n");
		Double amount = sc.nextDouble();
		
		String selectQuery = "SELECT BALANCE FROM account WHERE ACCOUNT_NUMBER = "+this.getAccountNumber();
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			ResultSet rs=statement.executeQuery(selectQuery);
			while(rs.next()) {
				if(amount>rs.getDouble(1)) {
					System.out.print("No enough Balance to debit!!\n");
					return;
				}else {
					this.setBalance(rs.getDouble(1)-amount);
				}
			}
				
		String query = "UPDATE ACCOUNT"
					+ " SET BALANCE ="+this.getBalance()
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		
		
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nAMOUNT CREDITED SUCCESSFUL!!\n");
			}
			conn.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void updateCurrencyType(BigInteger accountNumber) {
		System.out.print("Enter new Currenct Type:\n");
		this.setCurrencyType(sc.next());
		String query = "UPDATE ACCOUNT"
					+ " SET CURRENCY_TYPE ="+"'"+this.getCurrencyType()+"'"
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nCURRENCY-TYPE UPDATE SUCCESSFUL!!\n");
			}
			conn.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
}
