package com.mengfly.lib;

import com.mengfly.lib.io.IOStreamUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class SensitiveWord {

    private volatile static SensitiveWord mInstance;
    private SensitiveWordFilter filterProvider;

    private String encoding = "UTF-8";
    // 初始的装载替换字符的字符串，使用字符串的目的是为了可以随意截取任意长度的替换字符串
    // 例如要获取****直接replaceAll.subString(0,4)即可完成
    private StringBuilder replaceAll;
    private String replaceStr = "*";
    private String fileName = "CensorWords.txt";
    private String punctuationRegex = "\\pP|\\pS| ";
    private Pattern punctuationPattern;

    private SensitiveWord() {
        punctuationPattern = Pattern.compile(punctuationRegex);
        replaceAll = new StringBuilder(500);
        for (int x = 0; x < 500; x++) {
            replaceAll.append(replaceStr);
        }
        filterProvider = new DFASensitiveWordFilter();
        filterProvider.prepare(fileName, encoding);
    }

    /**
     * 如果修改了类的属性，而想让这些属性生效的话，请调用这个方法
     */
    public void reInitiliazation() {
        filterProvider.prepare(fileName, encoding);
    }

    public static SensitiveWord getInstance() {
        if (mInstance == null) {
            synchronized (SensitiveWord.class) {
                if (mInstance == null) {
                    mInstance = new SensitiveWord();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param str 将要被过滤的信息
     * @return 过滤后的信息
     */
    public String filterInfo(String str) {
        if (str == null) {
            return "";
        }
        // 将原始字符串去除掉标点符号
        String replaceStr = str.replaceAll(punctuationRegex, "");

        String filterString = filterProvider.doFilter(replaceStr, replaceAll);

        // 将字符串还原回去
        Map<Integer, String> punctuationIndexMap = getPunctuationIndexMap(str);
        return convertResString(filterString, punctuationIndexMap);
    }

    public void setFilterProvider(SensitiveWordFilter filterProvider) {
        this.filterProvider = null;
        this.filterProvider = filterProvider;
        this.filterProvider.prepare(fileName, encoding);
    }

    /**
     * 将过滤后的字符串还原成带标点符号的过滤字符串
     *
     * @param filterString        过滤后的字符串
     * @param punctuationIndexMap 根据PunctuationIndexMap获取到的特殊字符位置内容信息
     * @return 还原后的字符串
     */
    private String convertResString(String filterString, Map<Integer, String> punctuationIndexMap) {
        StringBuilder builder = new StringBuilder(filterString);
        for (Integer integer : punctuationIndexMap.keySet()) {
            builder.insert(integer, punctuationIndexMap.get(integer));
        }
        return builder.toString();
    }

    /**
     * 获取到标点符号在字符串中的位置和标点符号内容的Map
     * 这个Map主要是用来对敏感词屏蔽后的字符串进行恢复标点符号
     *
     * @param str 原始的字符串
     * @return 包含特殊字符位置和内容的Map
     */
    private Map<Integer, String> getPunctuationIndexMap(String str) {
        String[] split = punctuationPattern.split(str);
        Map<Integer, String> pStrIndexMap = new TreeMap<>();
        int fromIndex = 0;
        int preEnd = 0;
        for (String s : split) {
            int start = str.indexOf(s, fromIndex);
            if (preEnd != 0) {
                String pStr = str.substring(preEnd, start);// 找到那个特殊字符
                pStrIndexMap.put(preEnd, pStr);
            } else {
                // 找到开始的字符是不是特殊字符
                if (start != 0) {
                    pStrIndexMap.put(0, str.substring(0, start));
                }
            }
            preEnd = start + s.length();
            fromIndex = preEnd;
        }
        // 找到最后一个标点符号
        if (split.length > 0) {
            if (preEnd < str.length()) {
                pStrIndexMap.put(preEnd, str.substring(preEnd, str.length()));
            }
        }
//        System.out.println(pStrIndexMap);
        return pStrIndexMap;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getReplaceStr() {
        return replaceStr;
    }

    public void setReplaceStr(String replaceStr) {
        this.replaceStr = replaceStr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isContainSensitiveWord(String text) {
        String replaceStr = text.replaceAll(punctuationRegex, "");
        return filterProvider.isContainsSensitiveWord(replaceStr);
    }

    public Set<String> getSensitiveWord(String text) {
        String replaceStr = text.replaceAll(punctuationRegex, "");
        return filterProvider.getSensitiveWord(replaceStr);
    }

    interface SensitiveWordFilter {
        // 服务的准备工作
        void prepare(String fileName, String encoding);

        String doFilter(String text, StringBuilder replaceAll);

        boolean isContainsSensitiveWord(String text);

        Set<String> getSensitiveWord(String text);

        default Set<String> loadData(String fileName, String encoding) {

            // 加载词库
            Set<String> arrayList = new HashSet<>();
            InputStreamReader read = null;
            BufferedReader bufferedReader = null;
            try {
                read = new InputStreamReader(SensitiveWord.class.getResourceAsStream(fileName), encoding);
                bufferedReader = new BufferedReader(read);
                for (String txt; (txt = bufferedReader.readLine()) != null; ) {
                    arrayList.add(txt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOStreamUtil.closeAll(bufferedReader, read);
            }
            return arrayList;
        }
    }

    /**
     * 敏感词过滤IndexOf算法
     */
    static class IndexSensitiveWordFilter implements SensitiveWordFilter {
        private Set<String> arrayList;

        @Override
        public void prepare(String fileName, String encoding) {
            this.arrayList = loadData(fileName, encoding);
        }

        /**
         * index算法
         *
         * @param text 要处理的字符串
         * @return 处理后的字符串
         */
        @Override
        public String doFilter(String text, StringBuilder replaceAll) {
            StringBuilder punctuationFilterString = new StringBuilder(text);
            HashMap<Integer, Integer> hash = new HashMap<>(arrayList.size());
            String temp;
            for (String anArrayList : arrayList) {
                temp = anArrayList;
                int findIndexSize = 0;
                for (int start; (start = punctuationFilterString.indexOf(temp, findIndexSize)) > -1; ) {
                    findIndexSize = start + temp.length();
                    Integer mapStart = hash.get(start);
                    if (mapStart == null || findIndexSize > mapStart) {
                        hash.put(start, findIndexSize);
                    }
                }
            }
            Collection<Integer> values = hash.keySet();
            for (Integer value : values) {
                Integer endIndex = hash.get(value);
                punctuationFilterString.replace(value, endIndex, replaceAll.substring(0, endIndex - value));
            }
            hash.clear();
            return punctuationFilterString.toString();
        }

        @Override
        public boolean isContainsSensitiveWord(String text) {
            for (String anArrayList : arrayList) {
                if (text.contains(anArrayList)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Set<String> getSensitiveWord(String text) {
            Set<String> set = new HashSet<>();
            for (String s : arrayList) {
                if (text.contains(s)) {
                    set.add(s);
                }
            }
            return set;
        }
    }

    /**
     * 敏感词过滤DFA算法
     */
    static class DFASensitiveWordFilter implements SensitiveWordFilter {
        public static int minMatchType = 1; // 最小匹配原则
        public static int maxMatchType = 2; // 最大匹配原则
        private int matchType = minMatchType;
        private Map<?, ?> sensitiveWordMap = null;

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void prepare(String fileName, String encoding) {
            Set<String> keyWordSet = loadData(fileName, encoding);
            sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
            String key;
            Map nowMap;
            Map<String, String> newWorMap;
            //迭代keyWordSet
            for (String aKeyWordSet : keyWordSet) {
                key = aKeyWordSet;    //关键字
                nowMap = sensitiveWordMap;
                for (int i = 0; i < key.length(); i++) {
                    char keyChar = key.charAt(i);       //转换成char型
                    Object wordMap = nowMap.get(keyChar);       //获取

                    if (wordMap != null) {        //如果存在该key，直接赋值
                        nowMap = (Map) wordMap;
                    } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                        newWorMap = new HashMap<>();
                        newWorMap.put("isEnd", "0");     //不是最后一个
                        nowMap.put(keyChar, newWorMap);
                        nowMap = newWorMap;
                    }

                    if (i == key.length() - 1) {
                        nowMap.put("isEnd", "1");    //最后一个
                    }
                }
            }
        }

        @Override
        public String doFilter(String text, StringBuilder replaceAll) {
            StringBuilder builder = new StringBuilder(text);
            for (int i = 0; i < text.length(); i++) {
                int matchFlag = checkSensitiveWord(text, i);
                if (matchFlag > 0) {
                    builder.replace(i, i + matchFlag, replaceAll.substring(0, matchFlag));
                    i = i + matchFlag;
                }
            }
            return builder.toString();
        }

        public Set<String> getSensitiveWord(String text) {
            Set<String> sensitiveWordList = new HashSet<>();

            for (int i = 0; i < text.length(); i++) {
                int length = checkSensitiveWord(text, i);
                if (length > 0) {
                    sensitiveWordList.add(text.substring(i, i + length));
                    i = i + length - 1;
                }
            }
            return sensitiveWordList;
        }

        /**
         * 检查文字中是否包含敏感字符
         *
         * @param text       text
         * @param beginIndex beginIndex
         * @return 如果存在，则返回敏感词字符的长度，不存在返回0
         */
        private int checkSensitiveWord(String text, int beginIndex) {
            boolean flag = false; // 敏感词结束标识位，用于敏感词只有一位的情况
            int matchFlag = 0;
            char word;
			Map<?, ?> nowMap = sensitiveWordMap;
            for (int i = beginIndex; i < text.length(); i++) {
                word = text.charAt(i);
                nowMap = (Map<?, ?>) nowMap.get(word);
                if (nowMap != null) {
                    matchFlag++;
                    if ("1".equals(nowMap.get("isEnd"))) {
                        flag = true;
                        if (minMatchType == matchType) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            if (matchFlag < 2 || !flag) {
                matchFlag = 0;
            }
            return matchFlag;
        }

        public boolean isContainsSensitiveWord(String text) {
            for (int i = 0; i < text.length(); i++) {
                int matchFlag = this.checkSensitiveWord(text, i);
                if (matchFlag > 0) {
                    return true;
                }
            }
            return false;
        }


        public int getMatchType() {
            return matchType;
        }

        public void setMatchType(int matchType) {
            this.matchType = matchType;
        }
    }

}
