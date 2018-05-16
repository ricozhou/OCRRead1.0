package utils;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QRUtils {
	// 缩放图片
	public BufferedImage zoomImage(String absolutePath, int width, int height) {
		double wr = 0, hr = 0;
		File srcFile = new File(absolutePath);
		BufferedImage bufImg = null;
		try {
			bufImg = ImageIO.read(srcFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Image Itemp = bufImg.getScaledInstance(width, height, bufImg.SCALE_SMOOTH);
		wr = width * 1.0 / bufImg.getWidth();
		hr = height * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		return (BufferedImage) Itemp;
	}

	// 缩放图片
	public BufferedImage zoomImage2(String absolutePath, int jpwidth, int jpheight, BufferedImage bufImg) {
		double wr = 0, hr = 0;
		if (absolutePath != null) {
			File srcFile = new File(absolutePath);
			bufImg = null;
			try {
				bufImg = ImageIO.read(srcFile);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		int w = bufImg.getWidth();
		int h = bufImg.getHeight();
		int[] wh = proImage(w, h, jpwidth, jpheight);

		Image Itemp = bufImg.getScaledInstance(wh[0], wh[1], bufImg.SCALE_SMOOTH);
		wr = wh[0] * 1.0 / bufImg.getWidth();
		hr = wh[1] * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		return (BufferedImage) Itemp;
	}

	private int[] proImage(int w, int h, int jpwidth, int jpheight) {
		// 判断大小
		// 先以宽为缩放准则
		if (jpwidth * h / w <= jpheight) {
			// 如果高不超过
			return new int[] { jpwidth, jpwidth * h / w };
		} else {
			// 如果高超过了
			// 则以高为准则
			if (w * jpheight / h <= jpwidth) {
				return new int[] { w * jpheight / h, jpheight };
			} else {
				return null;
			}
		}
	}
}
