package net.graph.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class getUserdict {
	public static void main(String[] args){
		String driver = "com.mysql.jdbc.Driver";
		String url  ="jdbc:mysql://127.0.0.1:3306/sinanews?useSSL=false";
		String user = "root";
		String password = "ekgdebs";
		try{
			Class.forName(driver);
			//Connection conn = DriverManager.getConnection(url, user, null);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
	
			//读取news
			String sql = "select * from news_final";
			ResultSet rs = statement.executeQuery(sql);
			LinkedList<String> entity_list = new LinkedList<String>();
			while (rs.next()){
				String entities = rs.getString("newsKeywordsAndEntities");
				for (String entity : entities.split(" "))
					entity_list.add(entity);
			}
			File file = new File("entity_list.txt");
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter buffer = new BufferedWriter(fileWriter);
			for (String entity : entity_list){
				buffer.write(entity + "\n");
			}
			buffer.close();
			fileWriter.close();
			statement.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
