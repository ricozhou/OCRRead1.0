package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import OCRread.BaiDuKey;

public class OCRUtils {
	// 保存到文件
	public void saveToConfigFile(BaiDuKey bdy) throws IOException {
		Properties pro = new Properties();
		File file = new File("OCR\\baiduOCR\\baiduaipconfig.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("APP_ID", bdy.getAPP_ID());
			pro.setProperty("API_KEY", bdy.getAPI_KEY());
			pro.setProperty("SECRET_KEY", bdy.getSECRET_KEY());
			fos = new FileOutputStream(file);
			obw = new OutputStreamWriter(fos, "utf-8");
			pro.store(obw, null);
			obw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				fos.close();
			}
			if (null != bf) {
				bf.close();
			}
		}
	}

	// 获取配置
	public BaiDuKey getBaiDuOCRConfig() throws Exception {
		BaiDuKey bdy = new BaiDuKey();
		Properties pro = new Properties();
		File f = new File("OCR\\baiduOCR\\baiduaipconfig.properties");
		if (!f.exists()) {
			return bdy;
		}
		FileInputStream fis = new FileInputStream(f);
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		bdy.setAPP_ID(pro.getProperty("APP_ID"));
		bdy.setAPI_KEY(pro.getProperty("API_KEY"));
		bdy.setSECRET_KEY(pro.getProperty("SECRET_KEY"));
		fis.close();
		bf.close();
		return bdy;
	}
}
