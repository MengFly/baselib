package com.mengfly.lib.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author wangp
 */
public class IOStreamUtil {

	/**
	 * 关闭所有可关闭的数据流
	 */
    public static void closeAll(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
