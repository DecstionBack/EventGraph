package net.graph.NLPIR;

import java.io.*;
import java.util.HashSet;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;

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
			String sInput = "五中全会落幕，令计划、周本顺等10人被开除党籍，这一数字创下历届全会之最。改革开放以来的历届中央全会都开除了哪些人？他们因为什么原因被开除？下场如何？10人党籍创历次全会之最本次全会确认中央政治局之前作出的给予令计划、周本顺、杨栋梁、朱明国、王敏、陈川平、仇和、杨卫泽、潘逸阳、余远辉开除党籍的处分。　一次开除10人党籍的数字创下历次全会之最。本次全会开除党籍的10人中，最年轻的51岁，年龄最大的62岁。其中3人为中央委员，7人为中央候补委员。中央委员或中央候补委员在全会上被开除党籍，在近五届中央委员会，也就是上世纪90年代开始逐渐增多。《新闻极客》梳理了1978年以来的54次中共中央全会发现，加上本次会议被开除党籍的10人，十四届中央委员会以来共有27名中央委员或中央候补委员在全会上被确认开除党籍。2014年十八届四中全会，李东生、蒋洁敏、杨金山、王永春、李春城、万庆良6人开除党籍。被给予开除党籍处分的，十七届有薄熙来、刘志军和康日新等3人；十六届有陈良宇、杜世成、田凤山等3人；十五届有王雪冰、石兆彬、李嘉廷和许运鸿等4人；十四届有陈希同。陈希同先是在1995年十四届五中全会上被撤销政治局委员和中央委员职务，1997年被开除党籍，1997年的第十四届七中全会《公报》写道，“全会审议通过了中央纪律检查委员会关于陈希同问题的审查报告。”“罪状”被开除党籍?《公报》没有详细提及10人因何种原因被开除党籍。不过上述被开除党籍的高官多涉贪腐案。历届全会《公报》大部分仅提到被给予开除党籍处分者“严重违纪”。按照党章规定，党纪处分有五种：警告、严重警告、撤销党内职务、留党察看、开除党籍。开除党籍是党纪的最高处分。1999年许运鸿被开除党籍，《公报》提到鉴于其“有些问题涉嫌触犯刑律，由司法机关对其依法处理。”《公报》对于陈希同的“罪状”比较详细，其中指出陈希同“严重失职”，“腐化堕落，生活奢糜；利用职权，为其亲属等人谋取非法利益；利用职务和公务之便，收受贵重物品。”就在被确定开除党籍的同一天，最高人民检察院经审查决定，依法对河北省委原书记、省人大常委会原主任周本顺以涉嫌受贿罪立案侦查并采取强制措施。案件侦查工作正在进行中。根据公开资料，去年被开除党籍的6人中，已有3人被判刑，其中蒋洁敏、李春城同日被判处有期徒刑16年，李东生受贿案已开庭审理，万庆良今年被终止党代表资格，正在接受调查；时任成都军区副司令员杨金山被开除党籍后，暂时没有进一步消息被报道。十四届、十五届、十六届和十七届被开除党籍的11人中，有两人被判死刑（缓期2年执行），4人判无期徒刑，5人被判有期徒刑。　多名中央委员被开除党籍留下空缺。按照《党章》规定，中央委员会委员出缺，由中央委员会候补委员按照得票多少依次递补。　本次全会决定递补中央委员会候补委员刘晓凯、陈志荣、金振吉为中央委员会委员。三人都是少数名族。其中此次递补的刘晓凯是“60后”，苗族，出生于贵州台江，1978年至1983年在清华大学机械工程系焊接专业学。根据公开履历来看，他一直都在贵州工作。2008年至2012年一直担任贵州省副省长，2012年起任贵州省委常委、统战部部长。目前是贵州省委常委、统战部部长。另外两人是“50后”。陈志荣，黎族，中央党校研究生学历，毕业后从广东省海南黎族苗族自治州开始了自己的仕途。海南建省后，他历任海南省三亚市计划局副局长、局长、副市长等职，2015年5月起，任海南省委常委、政法委书记。朝鲜族的金振吉，毕业于吉林大学经济管理学院世界经济专业，是经济学博士。金振吉最初是吉林省汪清县仲坪中学的一名教员，随后担任共青团延边州委宣传部干事、共青团龙井县委书记，走上仕途。他从1992年至2007年，历任共青团吉林省委副书记、党组成员、省青联主席，共青团吉林省委书记、党组书记兼省青联主席，吉林省延边州委副书记，延吉市委书记、延边州州长等职。2007年至2011年任吉林省副省长，2011年4月任吉林省委常委，政法委书记。";
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
			if (NLPIR.NLPIR_Init(argu.getBytes("GB2312"),1) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			
					//导入用户词典前
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(sInput.getBytes("GB2312"), 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
	
			System.out.println("分词结果为： " + nativeStr);
			
			String text = "新华社七问北京首发雾霾红色预警 各部门怎应对";
			EntityNLPIR s = new EntityNLPIR();
			System.out.println(s.KeyWordsAndEntities(text));
		}
		catch (Exception ex)
		{
		} 
	}
}


 
