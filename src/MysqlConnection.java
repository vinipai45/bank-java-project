import java.sql.*;  
class MysqlConnection{  
	public Connection connect(){  
		try{ 
			
			String HOSTNAME = "localhost";
			String PORT = "3306";
			String DBNAME = "backjavaproject";
			String USERNAME = "root";
			String PASSWORD = "";
			
			Class.forName("com.mysql.jdbc.Driver");  
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/backjavaproject","root","");  
		}catch(Exception e){ 
			System.out.println("Connection to Database Failed!\n"+e);
		}
		return null;
	}  
}  