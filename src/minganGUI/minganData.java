package minganGUI;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.lang.Boolean;

import jxl.write.*;
import jxl.*;

public class minganData {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url  ="jdbc:mysql://192.168.140.200:3306/mingan?useSSL=false";
	public static String user = "root";
	public static String password = "123456";
	
	public static String getKeywords(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql = "select * from keyword_lys";
			ResultSet rs = statement.executeQuery(sql);
			StringBuilder builder = new StringBuilder();
			Integer count = 0;
			while (rs.next()){
				count++;
				builder.append(rs.getString("keyword") + "；");
			}
			
			return count + "::::" + builder.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String findData(LinkedList<Boolean> findList, String table){
		//findList= baidu + weixin + google
		//boolean baidu = findList.get(0);
		//boolean weixin = findList.get(1);
		//boolean google = findList.get(2);
		
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			Statement insertstatement = conn.createStatement();
			String sql = "select * from result_lys" + table + " where tag > 0 order by tag desc";
			System.out.println("keyword table read complete");
			ResultSet rs = statement.executeQuery(sql);
			StringBuilder result = new StringBuilder("");
			while (rs.next()){
				Integer id = Integer.parseInt(rs.getString("id"));
				String keyword = rs.getString("keyword");
				String type = rs.getString("type");
				//System.out.println(id + keyword);
				result.append(keyword+"\n" + type + "\n"); 
				sql = "select url,urlplace,abstract from keywordurl_lys where keywordid=" + id + " and tag>0";
				ResultSet rs2 = statement2.executeQuery(sql);
				//temp text
				//StringBuilder temptext = new StringBuilder("");
				while (rs2.next()){
				//	temptext.append(rs2.getString("url") + "::::" + rs2.getString("urlplace") + "::::" + rs2.getString("abstract"));
					result.append(rs2.getString("url") + "\n" + rs2.getString("urlplace") + "\n" + rs2.getString("abstract") + "\n");
				//	sql = "insert into temptext(keywordid,keyword,type,text) values(" + id + ",\"" + keyword + "\",\"" + type + "\",\"" + temptext.toString() + "\")";
				//	insertstatement.execute(sql);
				}
				result.append("\n\n");
				//System.out.println(id);
				//String keyword = null;
				//没次查询结果会有一个关键词对应好多个url，关键词每次只需要查询一次就可以了
				//System.out.println("preid:" + preid + "  id:" + id);
				//if (id >=140) break;
				/*if (!id.equals(preid)){
				outputCount++;
				result.append("\n");
				sql = "select keyword from keyword where id=" + id;
				ResultSet rs2 = statement2.executeQuery(sql);
				while(rs2.next()){
					keyword = rs2.getString("keyword");
					result.append(keyword + "   ");
					preid=id;
					break;
				}
				rs2.close();}*/
				
				//String url = rs.getString("url");
				//result.append(url + " ; ");
				//preid = id;
				
				//String keyword = rs.getString("keyword");
				//String baidu_tag = rs.getString("baidu_tag");
				//String weixin_tag = rs.getString("weixin_tag");
				//String google_tag = rs.getString("google_tag");
				
				/*if (baidu){
					if (baidu_tag.equals("1"))
						if(!findData.contains(keyword)){
							findData.add(keyword);
							outputCount++;
						}
				}
				if (weixin){
					if (weixin_tag.equals("1"))
						if(!findData.contains(keyword)){
							findData.add(keyword);
							outputCount++;
						}
				}
				if (google){
					if (google_tag.equals("1"))
						if(!findData.contains(keyword)){
							findData.add(keyword);
							outputCount++;
						}
				}*/
			}
			//System.out.println(result.toString());
			rs.close();
			statement.close();
			conn.close();
			//result.deleteCharAt(0);
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	public static String tempfindData(LinkedList<Boolean> findList, String table){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			Statement insertstatement = conn.createStatement();
			String sql = "select distinct(keyword) from temptext";
			//String sql = "select * from filted_words" + table + " order by count desc";
			ResultSet rs = statement.executeQuery(sql);
			StringBuilder result = new StringBuilder("");
			while (rs.next()){
				//String keyword = rs.getString("keyword");
				String keyword = rs.getString(1);
			//	System.out.println(keyword);
				//String type = rs.getString("type");
				//System.out.println(id + keyword);
				//result.append(keyword+"\n" + type + "\n");
				//type先忽略
				result.append(keyword+"\n");
				sql = "select * from temptext where keyword=\"" + keyword + "\"";
				ResultSet rs2 = statement2.executeQuery(sql);
				while (rs2.next()){
					result.append(rs2.getString("url") + "\n" + rs2.getString("urlplace") + "\n" + rs2.getString("text") + "\n");
				}
				result.append("\n\n");
			}
			rs.close();
			statement.close();
			conn.close();
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	public static void tempExportExcel(String filePath, String text) throws Exception{
		File fs = new File(filePath);
		WritableWorkbook workbook = Workbook.createWorkbook(fs);
		WritableSheet sheet = workbook.createSheet("敏感词结果", 0);
		sheet.addCell(new Label(0, 0, "关键词"));
		sheet.addCell(new Label(1, 0, "关键词类型"));
		sheet.addCell(new Label(2, 0, "链接地址"));
		sheet.addCell(new Label(3, 0, "链接地址所处地区"));
		sheet.addCell(new Label(4, 0, "内容"));
		int row = 1;
			try{
				Class.forName(driver);
				Connection conn = DriverManager.getConnection(url, user, password);
				Statement statement = conn.createStatement();
				String sql = "select * from temptext";
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()){
					sheet.addCell(new Label(0, row, rs.getString("keyword")));
					sheet.addCell(new Label(1, row, rs.getString("type")));
					sheet.addCell(new Label(2, row, rs.getString("url")));
					sheet.addCell(new Label(3, row, rs.getString("urlplace")));
					sheet.addCell(new Label(4, row, rs.getString("text")));
					row++;
				}
				workbook.write();
				workbook.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		
	}
	public static void ExportExcel(String filePath, String text) throws Exception{
		File fs = new File(filePath);
		WritableWorkbook workbook = Workbook.createWorkbook(fs);
		WritableSheet sheet = workbook.createSheet("敏感词结果", 0);
		sheet.addCell(new Label(0, 0, "关键词"));
		sheet.addCell(new Label(1, 0, "关键词类型"));
		sheet.addCell(new Label(2, 0, "链接地址"));
		sheet.addCell(new Label(3, 0, "链接地址所处地区"));
		sheet.addCell(new Label(4, 0, "内容"));
		int row = 1;
		for (String paragraph : text.split("\n\n\n")){
			String[] lines = paragraph.split("\n");
			String keyword = lines[0];
			String type = lines[1];
//			sheet.addCell(new Label(0, row, keyword));
//			sheet.addCell(new Label(1, row, type));
			for (int i = 0; i < (lines.length - 2) / 3; i++){
				String url = lines[3 * i + 2];
				String urlplace = lines[3 * i + 3];
				String content = lines[3 * i + 4];
				sheet.addCell(new Label(0, row, keyword));
				sheet.addCell(new Label(1, row, type));
				sheet.addCell(new Label(2, row, url));
				sheet.addCell(new Label(3, row, urlplace));
				sheet.addCell(new Label(4, row, content));
				row++;
			}
		}
		workbook.write();
		workbook.close();
	}
}
