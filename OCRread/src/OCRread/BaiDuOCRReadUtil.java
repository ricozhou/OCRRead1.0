package OCRread;

import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

import utils.OCRUtils;

public class BaiDuOCRReadUtil {
	public OCRUtils ocru = new OCRUtils();
	private static AipOcr client;

	// 识别
	public String recognizeText(File file) throws Exception {
		// 加载配置
		BaiDuKey bdy = ocru.getBaiDuOCRConfig();
		// 实例化baiduocr对象
		client = new AipOcr(bdy.getAPP_ID(), bdy.getAPI_KEY(), bdy.getSECRET_KEY());
		String path = file.getAbsolutePath();
		// 调用接口
		JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		// 结果
		String searchResult = res.toString();
		if (searchResult.contains("error_msg")) {
			JOptionPane.showMessageDialog(null, "网络连接失败！\n或者OCR可使用次数不足！\n请检查网络！或者换key或使用Tesseract引擎！", "提示消息",
					JOptionPane.WARNING_MESSAGE);
		}
		// 正常结果
		JSONArray jsonArray = res.getJSONArray("words_result");
		// System.out.println(jsonArray);
		StringBuilder sb = new StringBuilder();
		for (Object aJsonArray : jsonArray) {
			String str = aJsonArray.toString();
			str = str.substring(10, str.lastIndexOf('"'));
			sb.append(str);
			sb.append("\n");
		}
		// 返回
		// System.out.println(sb.toString());
		return sb.toString();
	}
}
