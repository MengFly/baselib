package com.mengfly.lib;


import org.junit.jupiter.api.Test;

import com.mengfly.lib.SensitiveWord.DFASensitiveWordFilter;
import com.mengfly.lib.SensitiveWord.IndexSensitiveWordFilter;

class SensitiveWordTest {

	@Test
	void test() {
		//测试花费的时间

        String str = " ,.,.2321, 太多,太多的伤yuming感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20哀2015/4/16 20152015/4/16乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "关, 人, 流, 电, 发, 情, 太, 限, 法#轮&(^*$功, 个人, 经, 色, 许, 公, 动, 地, 方, 基, 在, 上, 红, 强, 自杀指南, 制, 卡, 三级片, 一, 夜, 多, 手机, 于, 自，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        StringBuilder largeStr = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            largeStr.append(str);
        }
        SensitiveWord sensitiveWord = SensitiveWord.getInstance();
        System.out.println(largeStr.length());
        System.out.println(str.length());
        // 测试 词库大小：1974
        // 短类型文字 长度 508
        // 长类型文字 长度  10160

        // 测试IndexOf算法效率
        System.out.println("IndexOf algorithm:");
        long start = System.currentTimeMillis();
        sensitiveWord.setFilterProvider(new IndexSensitiveWordFilter());
        for (int i = 0; i < 1000; i++) {
            // 处理少量文本，比如转换上面的str 测试平均时间为0.3ms
            sensitiveWord.filterInfo(str);
            // 处理大量文本，比如转换上面的str的20倍，将近10000个字符，已经超过一般文章的大小了，测试平均时间为5.2ms
//            sensitiveWord.filterInfo(largeStr.toString());
        }
        System.out.println("totalTime:" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("avg:" + (System.currentTimeMillis() - start) / 1000.0);
        System.out.println(SensitiveWord.getInstance().filterInfo(str));
        System.out.println(SensitiveWord.getInstance().getSensitiveWord(str));

        // 测试DFA算法效率
        System.out.println("DFA algorithm:");
        start = System.currentTimeMillis();
        DFASensitiveWordFilter filterProvider = new DFASensitiveWordFilter();
        filterProvider.setMatchType(DFASensitiveWordFilter.maxMatchType);
        sensitiveWord.setFilterProvider(filterProvider);
        for (int i = 0; i < 1000; i++) {
            // 处理少量文本，比如转换上面的str 测试平均时间为0.05ms
            sensitiveWord.filterInfo(str);
            // 处理大量文本，比如转换上面的str的20倍，奖金10000个字符，已经超过一般文章的大小了，测试平均时间为1.16ms
//            sensitiveWord.filterInfo(largeStr.toString());
        }
        System.out.println("totalTime:" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("avg:" + (System.currentTimeMillis() - start) / 1000.0);
        System.out.println(SensitiveWord.getInstance().filterInfo(str));
        System.out.println(SensitiveWord.getInstance().getSensitiveWord(str));

        // 无论是长字符，还是短字符使用DFA的算法要比IndexOf算法快5-6倍
	}

}
