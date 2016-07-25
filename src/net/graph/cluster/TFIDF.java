package net.graph.cluster;

import java.util.ArrayList;

public class TFIDF {


   public static double tf(String[] doc, String term){
    	double n = 0;
    	for(String s:doc)
    		if(s.equalsIgnoreCase(term))
    			n++;
    	return n/doc.length;
    }

   public static double idf(ArrayList<String[]> docs, String term){
    	double n = 0;
    	for(String[] x:docs)
    		for(String s:x)
    			if(s.equalsIgnoreCase(term)){
    				n++;
    				break;
	    		}
    	return Math.log(docs.size()/n);
    }
	public static double cosSim(double[] a, double[] b){
    	double dotp=0, maga=0, magb=0;
    	for(int i=0;i<a.length;i++){
    		dotp+=a[i]*b[i];
    		maga+=Math.pow(a[i],2);
    		magb+=Math.pow(b[i],2);
    	}
    	maga = Math.sqrt(maga);
    	magb = Math.sqrt(magb);
    	double d = dotp / (maga * magb);
    	return d==Double.NaN?0:d;
    }
}


