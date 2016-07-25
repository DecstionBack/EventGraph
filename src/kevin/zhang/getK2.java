package kevin.zhang;   

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class Content{
	private String content = null;
	private LinkedList<String> words = new LinkedList<String>();
	private HashMap<String, Double> PMIs = new HashMap<String, Double>();
	private Map<Integer, Double> words_tfidf = new HashMap<Integer, Double>();
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public LinkedList<String> getWords(){
		return words;
	}
	public void setWords(LinkedList<String> words){
		this.words = words;
	}
	public Map<Integer, Double> getTFIDF(){
		return words_tfidf;
	}
	public void setTFIDF(Map<Integer, Double> words_tfidf){
		this.words_tfidf = words_tfidf;
	}
	public HashMap<String, Double> getPMIs(){
		return PMIs;
	}
	public void setPMIs(HashMap<String, Double> PMIs){
		this.PMIs = PMIs;
	}
}


public class getK2 {

	public static LinkedList<Content> getContent(String filename) throws Exception{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		LinkedList<Content> contents = new LinkedList<Content>();
		String line = null;
		while ((line=buffer.readLine()) != null){
			//System.out.println(line);
			Content content = new Content();
			content.setContent(line);
			contents.add(content);
		}
		return contents;
	}
	
	public static LinkedList<String> getString(String filename) throws Exception{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		LinkedList<String> dicts = new LinkedList<String>();
		String line = null;
		while ((line=buffer.readLine()) != null){
			//System.out.println(line);
			dicts.add(line);
		}
		return dicts;
	}
	
	public static double getPMIs(String word, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<String, Double> PMIs = new HashMap<String, Double>();
		int youhaiFreq = 0;
		for (Content item : youhaiContents){
			if (item.getWords().contains(word))
				youhaiFreq ++;
		}
		int AllFreq = youhaiFreq;
		for (Content item : wuhaiContents){
			if (item.getWords().contains(word))
				AllFreq++;
		}
		double PMI1 = Math.log10(youhaiFreq * 1.0 / (AllFreq * youhaiContents.size()) + 0.0001);
		
		int wuhaiFreq = 0;
		for (Content item : wuhaiContents){
			if (item.getWords().contains(word))
				wuhaiFreq ++;
		}
		double PMI2 = Math.log10(wuhaiFreq * 1.0 / (AllFreq * wuhaiContents.size()) + 0.0001);
		
		return PMI1 + PMI2;
	}
	
	public static LinkedList<String> getGlobalWords(LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		LinkedList<String> globalwords = new LinkedList<String>();
		for (Content content : youhaiContents){
			for (String word : content.getWords()){
				if (!globalwords.contains(word))
					globalwords.add(word);
			}
		}
//		for (Content content : wuhaiContents){
//			for (String word : content.getWords())
//				if (!globalwords.contains(word))
//					globalwords.add(word);
//		}
		return globalwords;
	}
	
	public static void WriteFile(String filename){
		
	}
	
	public static HashMap<Integer, Double> getTFIDF(Content content, LinkedList<String> PMIWords, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<Integer, Double> tfidf = new HashMap<Integer, Double>();
		LinkedList<Content> contents = new LinkedList<Content>();
		for (Content item : youhaiContents){
			contents.add(item);
		}
		for (Content item : wuhaiContents){
			contents.add(item);
		}
		for (String word : content.getWords()){
			if (PMIWords.contains(word))
				tfidf.put(PMIWords.indexOf(word), tf(content.getWords(), word) * idf(word, contents));
		}
		
		return tfidf;
	}
	
	public static double tf(LinkedList<String> words, String word){
		int num = 0;
		for (String item : words){
			if (item.equals(word))
				num ++;
		}
		return num * 1.0 / words.size();
	}
	
	public static double idf(String word, LinkedList<Content> contents){
		int num = 0;
		for (Content content : contents){
			if (content.getWords().contains(word))
				num ++;
		}
		return Math.log10(contents.size() * 1.0 / (num + 1));
	}
	
	public static HashMap<String, Integer> getWordFreq(LinkedList<String> globalwords, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
		for (String globalword : globalwords){
			for (Content content : youhaiContents){
				for(String word : content.getWords()){
					if (word.equals(globalword)){
						if (!wordFreq.containsKey(globalword)){
							wordFreq.put(globalword, 1);
							break;
						}
						else{
							wordFreq.put(globalword, wordFreq.get(word) + 1);
							break;
						}
					}
				}
			}
//			for (Content content : wuhaiContents){
//				for(String word : content.getWords()){
//					if (word.equals(globalword)){
//						if (!wordFreq.containsKey(globalword)){
//							wordFreq.put(globalword, 1);
//							break;
//						}
//						else{
//							wordFreq.put(globalword, wordFreq.get(word) + 1);
//							break;
//						}
//					}
//				}
//			}
		}
		return wordFreq;
	}
	
	public static double calcK2(String word, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		int a = 0;
		for (Content content : youhaiContents){
			for (String contentword : content.getWords()){
				if (word.equals(contentword)){
					a ++;
					break;
				}
			}
		}
		int b = youhaiContents.size() - a;
		int c = 0;
		for (Content content : wuhaiContents){
			for (String contentword : content.getWords()){
				if (word.equals(contentword)){
					c ++;
					break;
				}
			}
		}	
		int d = wuhaiContents.size() - c;
		return Math.pow((a * d - b * c), 2) * 1.0 / ((a + c) * (b + d));
	}
	
	public static HashMap<String, Double> getK_2(LinkedList<String> globalwords, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<String, Double> K2 = new HashMap<String, Double>();
		for (String word : globalwords){
			K2.put(word, calcK2(word, youhaiContents, wuhaiContents));
		}
		return K2;
	}
	
	public static void writeWords(String filename, LinkedList<Content> Contents) throws IOException{
		File file = new File(filename);
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		for (Content content : Contents){
			for (String word : content.getWords())
				buffer.write(word + " ");
			buffer.newLine();
		}
		buffer.close();
		fileWriter.close();
	}
	
	public static void main(String[] args) throws Exception{
		LinkedList<Content> youhaiContents = getContent(".\\sample\\youhai.txt");
		LinkedList<Content> wuhaiContents = getContent(".\\sample\\wuhai.txt");
		LinkedList<Content> trainyouhaiContents = getContent(".\\sample\\train_youhai.txt");
		LinkedList<Content> trainwuhaiContents = getContent(".\\sample\\train_wuhai.txt");
		LinkedList<Content> testyouhaiContents = getContent(".\\sample\\test_youhai.txt");
		LinkedList<Content> testwuhaiContents = getContent(".\\sample\\test_wuhai.txt");
		LinkedList<String> userDict = getString(".\\sample\\userdic.txt");
		LinkedList<String> stopwords = getString("停用词表.txt");
		
		
		//读取特征词
		//LinkedList<String> PMIWords = getString(".\\sample\\feature.txt");
		//System.out.println(PMIWords);
		
		LinkedList<Content> sixfour = getContent(".\\sample\\六四.txt");
		
		
		System.out.println("read files complete...");
		
		for (Content content : sixfour){
			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(),stopwords));
		}
		LinkedList<String> globalwords = getGlobalWords(sixfour, null);
		HashMap<String, Integer> wordFreq = getWordFreq(globalwords, sixfour, null);
		System.out.println("wordFreq set...");
		File file =  new File("Freq.txt");
		FileWriter writer = new FileWriter(file);
		
		BufferedWriter buffer = new BufferedWriter(writer);
		for (String key : wordFreq.keySet()){
			buffer.write(key + " " + wordFreq.get(key));
			buffer.newLine();
		}
		
		
		buffer.close();
		writer.close();
		//分词
//		for (Content content : youhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("youhai_words.txt",youhaiContents);
//		
//		System.out.println("youhai segmentation complete...");
//		for (Content content : wuhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("wuhai_words.txt", wuhaiContents);
//		System.out.println("wuhai segmentation complete...");
		
//		for (Content content : trainyouhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("train_youhai_words.txt",trainyouhaiContents);
//		
//		System.out.println("train_youhai segmentation complete...");
//		
//		for (Content content : trainwuhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("train_wuhai_words.txt", trainwuhaiContents);
//		System.out.println("train_wuhai segmentation complete...");
//		for (Content content : testyouhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("test_youhai_words.txt",testyouhaiContents);
//		
//		System.out.println("test_youhai segmentation complete...");
//		for (Content content : testwuhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//		}
//		writeWords("test_wuhai_words.txt", testwuhaiContents);
//		System.out.println("test_wuhai segmentation complete...");
		boolean flag = true;
		if (flag)
			return;
		
//		LinkedList<Content> trainyouhaiContents = getContent(".\\sample\\test_youhai.txt");
//		LinkedList<Content> trainwuhaiContents = getContent(".\\sample\\test_wuhai.txt");
//		for (Content content : trainyouhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//			//System.out.println(content.getWords());
//		}
//		System.out.println("train_youhai segmentation complete...");
//		
//		for (Content content : trainwuhaiContents){
//			content.setWords(SegmentationNLPIR2.Segmentation(content.getContent(), userDict, stopwords));
//			//System.out.println(content.getWords());
//		}
//		System.out.println("train_wuhai segmentation complete...");
		
		LinkedList<String> globalwords1 = getGlobalWords(youhaiContents, wuhaiContents);
		System.out.println("globalwords set...");
		
		
		
		boolean flag1 = true;
		if (flag1)
			return;
		HashMap<String, Double> K2 = getK_2(globalwords1, youhaiContents, wuhaiContents);
		System.out.println("K2 set...");
		
		LinkedList<Map.Entry<String, Double>> arrayList = new LinkedList<Map.Entry<String, Double>>(K2.entrySet());
		Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>(){
			@Override
			public int compare(Entry<String, Double> o1,Entry<String, Double> o2){
				return o1.getValue().compareTo(o2.getValue()) * (-1);
			}
		});
		
//		File file3 = new File(".\\sample\\result_K2.txt");
//		FileWriter fileWriter3 = new FileWriter(file3);
//		BufferedWriter buffer3 = new BufferedWriter(fileWriter3);
//		for (Map.Entry<String, Double> entry : arrayList){
//			buffer3.write(entry.getKey() + " " + wordFreq.get(entry.getKey()) + " " + entry.getValue());
//			buffer3.newLine();
//		}
//		buffer3.close();
//		fileWriter3.close();
		
		
//		File file = new File(".\\sample\\result_test_TFIDF.txt");
//		FileWriter fileWriter = new FileWriter(file);
//		BufferedWriter buffer = new BufferedWriter(fileWriter);
//		for(Content content : trainyouhaiContents){
//			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
//			
//			List<Integer> keys = new ArrayList<Integer>();
//			for (Integer key : content.getTFIDF().keySet())
//				keys.add(key);
//			if (keys.size() > 0){
//			Collections.sort(keys);
//			buffer.write("1 ");
//			for (Integer key : keys)
//				buffer.write(key + ":" + content.getTFIDF().get(key) + " ");
//			}
//			buffer.newLine();
//		}
//		
//		for(Content content : trainwuhaiContents){
//			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
//			List<Integer> keys = new ArrayList<Integer>();
//			for (Integer key : content.getTFIDF().keySet())
//				keys.add(key);
//			if (keys.size() > 0){
//			Collections.sort(keys);
//			buffer.write("0 ");
//			for (Integer key : keys)
//				buffer.write(key + ":" + content.getTFIDF().get(key) + " ");
//			}
//			buffer.newLine();
//		}
//		buffer.close();
//		fileWriter.close();
	}
}
