import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountDetails extends MysqlConnection {
	private int accountType;
	private BigInteger accountNumber;
	private String name;
	private BigInteger mobileNumber;
	private double balance;
	private String currencyType;
	
	Scanner sc= new Scanner(System.in);
	MysqlConnection mysqlConnection = new MysqlConnection();
	
	//getters
	public int getAccountType() {
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
	public void setAccountType(int accountType) {
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
		ArrayList<Object> newAccount = new ArrayList<>();
		
		System.out.print("Enter Type of Account:\n1.Saving\n2.Current\n(1/2)?");
		this.setAccountType(sc.nextInt());
		System.out.print("\nEnter the Account Holder Full Name:\n");
		this.setName(sc.next());
		System.out.print("\nEnter Mobile Number:\n");
		this.setMobileNumber(sc.nextBigInteger());
		System.out.print("\nEnter 16-digit account number: \n");
		this.setAccountNumber(sc.nextBigInteger());
		System.out.print("\nEnter the Amount: \n");
		this.setBalance(sc.nextInt());
		System.out.print("\nEnter the Currency: \n");
		this.setCurrencyType(sc.next());
		
		String query = "INSERT INTO ACCOUNT VALUES ("
				+ this.getAccountType()+","
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
				System.out.print("____________________________");
				if(this.getAccountType()==1) {
					System.out.print("\nAccount Type : " + "Savings");
				}else {
					System.out.print("\nAccount Type : " + "Current");
				}
				System.out.print("\nAccount Number : " + this.getAccountNumber());
				System.out.print("\nAccount Holder Name : " + this.getName());
				System.out.print("\nMobile Number : " + this.getMobileNumber());
				System.out.print("\nAmount in Account : " + this.getBalance());
				System.out.print("\nCurrency mode : " + this.getCurrencyType() );
				System.out.print("\n____________________________");
				
				newAccount.add(this.getAccountType());
				newAccount.add(this.getAccountNumber());
				newAccount.add(this.getName());
				newAccount.add(this.getMobileNumber());
				newAccount.add(this.getBalance());
				newAccount.add(this.getCurrencyType());
//				System.out.println(newAccount);
				
			}
			conn.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	public void updateAccount() {
		System.out.print("Enter Account Number to update:\n");
		this.setAccountNumber(sc.nextBigInteger());
		if(this.findAccountNumber(this.getAccountNumber())){
			String input = new String("");
			while(!input.equals("6")) {
				Scanner sc= new Scanner(System.in);
				System.out.print("\n\nWhat do you want to update ? \n");
				System.out.print("1: Account Holder Name \n"
						+ "2: Account Type \n"
						+ "3: Mobile Number \n"
						+ "4: Amount in Account  \n"
						+ "5: Currency Type \n"
						+ "6: Exit Update!\n"
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
					case "4":	this.updateBalance(this.getAccountNumber());
								this.viewAccount();
								break;
					case "5":	this.updateCurrencyType(this.getAccountNumber());
								this.viewAccount();
								break;
					case "6": 
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
		String query = "SELECT * from ACCOUNT WHERE ACCOUNT_NUMBER="+this.getAccountNumber();

		try {

			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			ResultSet rs=statement.executeQuery(query);
			while(rs.next())  
				System.out.println(
					"_______________________________\n"+
					"Account Type: "+rs.getInt(1)+"\n"+
					"Account Number: "+ rs.getString(2)+"\n"+
					"Account Holder: "+ rs.getString(3)+"\n"+
					"Mobile Number: "+ rs.getString(4)+"\n"+
					"Amount in Account: "+ rs.getString(5)+"\n"+
					"Currency Type: "+ rs.getString(6)+
					"\n_______________________________"
				); 
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
		this.setName(sc.next());
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
		this.setAccountType(sc.nextInt());
		if(this.getAccountType()==1||this.getAccountType()==2) {
			String query = "UPDATE ACCOUNT"
					+ " SET ACCOUNT_TYPE ="+this.getAccountType()
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
		}else {
			System.out.print("\nInvalid Account Type!!\n");
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
	
	public void updateBalance(BigInteger accountNumber) {
		System.out.print("Enter new Amount : \n");
		this.setBalance(sc.nextDouble());
		String query = "UPDATE ACCOUNT"
					+ " SET BALANCE ="+this.getBalance()
					+ " WHERE ACCOUNT_NUMBER = "+accountNumber;
		
		try {
			Connection conn = mysqlConnection.connect();
			Statement statement = conn.createStatement();
			int count= statement.executeUpdate(query);
			if(count>=1) {
				System.out.print("\nAMOUNT UPDATE SUCCESSFUL!!\n");
			}
			conn.close();
		}catch(Exception e) {
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
