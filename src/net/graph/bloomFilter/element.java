package net.graph.bloomFilter;

/** 
 * 位集里面的每一个元素 
 * @author chouyou 
 * 
 */  
public class element {  
    private String key = null;  
    public element(String key){  
        this.setKey(key);  
    }  
    public String getKey() {  
        return key;  
    }  
    public void setKey(String key) {  
        this.key = key;  
    }  
}  
