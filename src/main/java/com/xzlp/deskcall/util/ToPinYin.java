package com.xzlp.deskcall.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ToPinYin {
	
	public static List<String> getPinyinList(List<String> list){
		List<String> pinyinList = new ArrayList<String>();
		for(Iterator<String> i=list.iterator(); i.hasNext();) {
			String str = (String)i.next();
			try {
				String pinyin = getPinYin(str);
				pinyinList.add(pinyin);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return pinyinList;
	}
	
    public static String getPinYin(String zhongwen)   
            throws BadHanyuPinyinOutputFormatCombination {   
  
        String zhongWenPinYin = "";   
        char[] chars = zhongwen.toCharArray();   
  
        for (int i = 0; i < chars.length; i++) {   
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());   
            if (pinYin != null) {   
            	zhongWenPinYin += pinYin[0];   
            } else {   
                zhongWenPinYin += chars[i];   
            }   
        }   
        return zhongWenPinYin;   
    }   
  
    public static String[] getNameIndex(String zhongwen)   
            throws BadHanyuPinyinOutputFormatCombination {
    	String nameIndex = "";
        String zhongWenPinYin = "";   
        char[] chars = zhongwen.toCharArray();   
  
        for (int i = 0; i < chars.length; i++) {   
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());   
            if (pinYin != null) {   
            	zhongWenPinYin += pinYin[0];   
            	nameIndex += pinYin[0].charAt(0);
            } else {   
                zhongWenPinYin += chars[i];   
                nameIndex += chars[i];
            }   
        }   
        return new String[]{nameIndex,zhongWenPinYin};   
    }   
    
    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {   
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();   
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);   
        return format;   
    }   
}
