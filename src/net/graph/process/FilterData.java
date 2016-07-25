package net.graph.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import kevin.zhang.SegmentationNLPIR;

public class FilterData {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url  ="jdbc:mysql://127.0.0.1:3306/sinanews?useSSL=false";
	public static String user = "root";
	public static String password = "19940314";
	public static void main(String[] args){
	try{
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		if (!conn.isClosed())
			System.out.println("Connecting to the Database successfully!");
		String sql = "select * from news";
		String newsID = null;
		Statement statement = conn.createStatement();
		Statement statement2 = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while(rs.next()){
			StringBuffer SegmentationWords = new StringBuffer("");
			//keywordsandentities.delete(0, keywordsandentities.length());
			newsID = rs.getString("newsID");
			//LinkedList<String> words = SegmentationNLPIR.Segmentation(rs.getString("newsContent"));
			String content = rs.getString("newsContent");
			if (content.contains("请点击升级您的Flash Player版本，最低版本要求：11.0 "))
				content = content.replaceAll("请点击升级您的Flash Player版本，最低版本要求：11.0 ", "");
			else if (content.contains("请点击升级您的Flash Player版本，最低版本要求：11.1 "))
				content = content.replaceAll("请点击升级您的Flash Player版本，最低版本要求：11.1 ", "");
			if (content.contains("自动播放 play "))
				content = content.replaceAll("自动播放 play ", "");
			else if (content.contains("自动播放  play "))
				content = content.replaceAll("自动播放  play ", "");
			if (content.contains("play"))
				content = content.replaceAll("play", "");
			System.out.println(content);
			sql = "update news set newsContent=\"" + content + "\" where newsID=\"" + newsID + "\"";
			statement2.execute(sql);
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}
