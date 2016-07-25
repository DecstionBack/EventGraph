package net.graph.NLPIR;

public class NLPIR {
    public static native boolean  NLPIR_Init(byte[] sDataPath,int encoding);
		public static native boolean  NLPIR_Exit();
		public native int NLPIR_ImportUserDict(byte[] sPath);
		public native float NLPIR_GetUniProb(byte[] sWord);
		public native boolean NLPIR_IsWord(byte[] sWord);
		public native byte[] NLPIR_ParagraphProcess(byte[] text,int bPOSTagged);
		public native boolean NLPIR_FileProcess(byte[] sSrcFilename,byte[] sDestFilename,int bPOSTagged);
		public native byte[] nativeProcAPara(byte[] src);
		public native int NLPIR_AddUserWord(byte[] sWord);//add by qp 2008.11.10
		public native int NLPIR_SaveTheUsrDic();
		public native int NLPIR_DelUsrWord(byte[] sWord);
	 
	 	public native int NLPIR_SetPOSmap(int nPOSmap);
		public native int NLPIR_GetElemLength(int nIndex);
		
		public native byte[] NLPIR_GetKeyWords( String nativeStr,int nMaxKeyLimit,boolean bWeightOut);
		public native byte[] NLPIR_GetFileKeyWords( byte[] sFilename,int nMaxKeyLimit,boolean bWeightOut);
		public native byte[] NLPIR_GetNewWords( byte[]sLine,int nMaxKeyLimit,boolean bWeightOut);
		public native byte[] NLPIR_GetFileNewWords( byte[] sFilename,int nMaxKeyLimit,boolean bWeightOut);
		public native boolean NLPIR_NWI_Start();//New Word Indentification Start
	public native int  NLPIR_NWI_AddFile(byte[]sFilename);
	public native boolean NLPIR_NWI_AddMem(byte[]sText);
	public native boolean NLPIR_NWI_Complete();//�´�
	public native byte[] NLPIR_NWI_GetResult(boolean bWeightOut);
	public native int  NLPIR_NWI_Result2UserDict();
    /* Use static intializer */
    static {
			System.loadLibrary("NLPIR_JNI");
    }
}


