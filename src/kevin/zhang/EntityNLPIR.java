package kevin.zhang;

import java.io.*;
import java.util.HashSet;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;

import net.graph.NLPIR.DocExtractLibray;

//import kevin.zhang.DocExtractLibray;

public class EntityNLPIR {
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"./NLPIR", CLibrary.class);
		
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		try
		{
			String sInput = "　《报告》显示，目前决策层主导互联网规划工作的企业比例为13.0%。“互联网+”传统企业的深入融合，不仅是技术与设备的应用，更重要的是从思想、战略层面引入互联网思维所倡导的“用户至上”、“快速迭代”、“普惠服务”。但是目前，大部分传统企业距离“互联网+”深入融合的发展层次尚远，最大障碍源自企业不肯打破思维桎梏、导致在转型过程中长期处于被动地位。只有决策层重视互联网的作用，将其从工具层面、渠道层面提升至战略层面，才能充分发挥互联网的先导力量。";
			test(sInput);
			//test(sInput);	
			//test2(sInput);
			KeyWordsAndEntities(sInput);
			
		}
		catch (Exception ex)
		{
		} 


	}
	public static HashSet<String> KeyWordsAndEntities(String sInput) throws UnsupportedEncodingException{
		String argu = "";
		int charset = 1;
		int init_flag = DocExtractLibray.Instance.DE_Init("", 1, "");
		//System.out.println("init_flag:" + init_flag);
		String nativeBytes = null;
		
		if (init_flag ==0 ){
			nativeBytes = DocExtractLibray.Instance.DE_GetLastErrMsg();
			System.out.println(nativeBytes);
		}
		if (DocExtractLibray.Instance.DE_Init(argu, charset, "0") == 1){
			//System.out.println("DocExtractor初始化成功");
			}
		String content = sInput;
		NativeLong handle = DocExtractLibray.Instance.DE_ParseDocE(content, "mqc#nqd", true, DocExtractLibray.ALL_REQUIRED);
		/*System.out.println("抽取的人名为：" + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PERSON));
		System.out.println("抽取的地名为：" + DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_LOCATION));
		System.out.println("抽取的机构名为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ORGANIZATION));
		System.out.println("抽取的关键词为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_KEYWORD));
		System.out.println("抽取的文章作者为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_AUTHOR));
		System.out.println("抽取的媒体为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_MEDIA));
		System.out.println("抽取的文章对应的所在国别为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_COUNTRY));
		System.out.println("抽取的文章对应的所在省份为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PROVINCE));
				
		System.out.println("抽取的文章摘要为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ABSTRACT));
		System.out.println("输出文章的正面情感词为-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_POSITIVE));
		System.out.println("输出文章的副面情感词-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_NEGATIVE));
				*/
		//System.out.println("输出文章原文-->" + content);
		//System.out.println("输出文章去除网页等标签后的正文-->"
		//		+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_DEL_HTML));
		//System.out.println("去除空格:" + DocExtractLibray.Instance.DE_GetResult(handle, 11).replaceAll("[　*| *| *|//s*]*", ""));
		
		//System.out.println("自定义词(mgc)-->"
		//		+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 1));
		//System.out.println("情感值---->" + DocExtractLibray.Instance.DE_GetSentimentScore(handle));
		//System.out.println("\n");
		HashSet<String> set = new HashSet<String>();
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_KEYWORD));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PERSON));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PERSON));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_LOCATION));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_ORGANIZATION));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_AUTHOR));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_MEDIA));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_COUNTRY));
		AddEntity(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_PROVINCE));
		AddEntityEmotion(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_POSITIVE));
		AddEntityEmotion(set, DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_NEGATIVE));
		DocExtractLibray.Instance.DE_ReleaseHandle(handle);
		//System.out.println(set);
		return set;
	}
	
	public static HashSet<String> AddEntity(HashSet<String> set, String str){
		if (str == null)
			return set;
		for (String s : str.split("#")){
			if (s.equals(" ") || s.equals("")) continue;
			if (s.contains("@")) {set.add(s.substring(1, s.length())); continue;}
			set.add(s);
		}
		return set;
	}
	public static HashSet<String> AddEntityEmotion(HashSet<String> set, String str){
		if (str == null)
			return set;
		for (String s : str.split("#")){
				if (s.equals(" ") || s.equals("")) continue;
				if (s.contains("@")) {set.add(s.substring(1, s.length())); continue;}
				set.add(s.split("/")[0]);
			}
		return set;
	}
	public static void test(String sInput) throws Exception{
		try
		{
			NLPIR testNLPIR = new NLPIR();
	
			String argu = "";
			System.out.println("NLPIR_Init");
			if (NLPIR.NLPIR_Init(argu.getBytes("UTF-8"),1) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			
					//导入用户词典前
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(sInput.getBytes("UTF-8"), 0);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
	
			System.out.println("分词结果为： " + nativeStr);
			
			//String text = "北京气象局:冷空气已到内蒙 雾霾今夜逐渐散去";
			EntityNLPIR s = new EntityNLPIR();
			System.out.println(s.KeyWordsAndEntities(sInput));
		}
		catch (Exception ex)
		{
		} 
	}
}


 
