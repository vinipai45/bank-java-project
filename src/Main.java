import java.util.*;
public class Main extends AccountDetails {
	
	public static void main(String[] args) {
		
		AccountDetails accountDetails = new AccountDetails();
		String input = new String("");
		System.out.print("******MOCK BANK APPLICATION****** ");
		while(!input.equals("N")) {
			Scanner sc= new Scanner(System.in);
			System.out.println("\n\nPlease select the option:");
			System.out.print("Y: For creating a new Account \r\n"
					+ "E: Update the details for an Existing account \r\n"
					+ "V: View the Account details \r\n"
					+ "D: Delete an account  \r\n"
					+ "N: Close this Screen \r\n"
					+ "");
			System.out.print("Enter Choice- ");
			input = sc.next().toUpperCase();
			
			switch(input) {
				case "Y": accountDetails.createAccount();
					break;
				case "E":accountDetails.updateAccount();
					break;
				case "V":		
					System.out.print("Enter Account Number to view:\n");
					accountDetails.setAccountNumber(sc.nextBigInteger());
					accountDetails.viewAccount();
					break;
				case "D":accountDetails.deleteAccount();
					break;
				case "N":System.out.print("BYE!");
					break;
				default:
					System.out.println("Invalid Input! \n Try again! \n\n");
			}
		}
	}

}
