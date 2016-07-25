package net.graph.bloomFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.BitSet;  
/** 
 * 就是这个过滤器，有插入、查询等功能，可以设置位集的大小。虽然有删除功能，但是最好不要用 
 * @author chouyou 
 * 
 */  
public class bloomFilter {  
	private int defaultSize = Integer.MAX_VALUE;
    //private int defaultSize = 5000 << 1000000;// <<是移位运算  
    /** 
     * 从basic的使用来看，hashCode最后的结果会产生一个int类型的数，而这个int类型的数的范围就是0到baisc 
     * 所以basic的的值为defaultsize减一 
     */  
    private int basic = defaultSize -1;  
  
    private BitSet bits = new BitSet(defaultSize);//初始化一个一定大小的位集  
      
    public bloomFilter(){  
    }  
    /** 
     * 针对一个key，用8个不同的hash函数，产生8个不同的数，数的范围0到defaultSize-1 
     * 以这个8个数为下标，将位集中的相应位置设置成1 
     * @return 
     */  
    private int[] indexInSet(element ele){  
        int[] indexes = new int[8];  
        for (int i = 0;i<8;i++){  
            indexes[i] = hashCode(ele.getKey(),i);  
        }  
        return indexes;  
    }  
    /** 
     * 添加一个元素到位集内 
     */  
    private void add(element ele){  
        if(exist(ele)){  
            System.out.println("已经包含("+ele.getKey()+")");  
            return;  
        }  
        int keyCode[] = indexInSet(ele);  
        for (int i = 0;i<8;i++){  
            bits.set(keyCode[i]);  
        }  
    }  
    /** 
     * 判断是否存在 
     * @return 
     */  
    private boolean exist(element ele){  
        int keyCode[] = indexInSet(ele);  
        if(bits.get(keyCode[0])  
                &&bits.get(keyCode[1])  
                &&bits.get(keyCode[2])  
                &&bits.get(keyCode[3])  
                &&bits.get(keyCode[4])  
                &&bits.get(keyCode[5])  
                &&bits.get(keyCode[6])  
                &&bits.get(keyCode[7])){  
            return true;   
        }  
        return false;  
    }  
    /** 
     * Q传入不同的Q就可以得到简单的不同的hash函数
     * 哈希函数的选取？？ 
     */  
    private int hashCode(String key,int Q){  
        int h = 0;  
        int off = 0;  
        char val[] = key.toCharArray();  
        int len = key.length();  
        for (int i = 0; i < len; i++) {  
            h = (30 + Q) * h + val[off++];  
        }  
        return changeInteger(h);  
    }  
      
    private int changeInteger(int h) {  
        return basic & h;//&是位与运算符  
    }  
      
    //由外部函数调用
    public boolean BloomFilter(String url_url){
    	element ele = new element(url_url);
    	if (this.exist(ele))
    		return true;
    	else {
    		this.add(ele);
    		return false;
    	}
    }
    
    public static void main(String[] args) {  
    	String driver = "com.mysql.jdbc.Driver";
		String url  ="jdbc:mysql://192.168.140.200:3306/sensitive?useSSL=false";
		String user = "root";
		String password = "ekgdebs";
		try{
			Class.forName(driver);
			//Connection conn = DriverManager.getConnection(url, user, null);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement deletestatement = conn.createStatement();
			//读取news
			String sql = "select id, url_url from sample_urls";
			ResultSet rs = statement.executeQuery(sql);
			bloomFilter f=new bloomFilter();  
			System.out.println("位集大小："+f.defaultSize);
			bloomFilter filter = new bloomFilter();
			while(rs.next()){
				int id = Integer.parseInt(rs.getString("id"));
				String url_url = rs.getString("url_url");
				if (!filter.BloomFilter(url_url))
					System.out.println("Exists");
				element ele = new element(url_url);
				if (f.exist(ele)){
					System.out.println(id + ":" + url_url + " 已经存在");
					sql = "delete from sample_urls where id=" + id;
					//deletestatement.execute(sql);
				}
				else
					f.add(ele);
			
			}
			deletestatement.close();
			statement.close();
			conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
    }  
}  
