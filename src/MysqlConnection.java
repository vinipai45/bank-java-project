import java.sql.*;  
class MysqlConnection{  
	public Connection connect(){  
		try{ 
			Class.forName("com.mysql.jdbc.Driver");  
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/backjavaproject","root","");  
		}catch(Exception e){ 
			System.out.println("Connection to Database Failed!\n"+e);
		}
		return null;
	}  
}  