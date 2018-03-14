package cn.yzlee.util.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * @Author:lyz
 * @Date: 2018/3/14 16:38
 * @Desc: 二维码生成工具类
 **/
public class QRCodeGenerator
{

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final QRCodeGenerator self = new QRCodeGenerator();

	private QRCodeGenerator() {
	}

	public static enum FORMAT {
		PNG, JPEG, JPG, BMP;
	}

	public static enum CODE_TYPE {
		QR, BAR;
	}

	public static QRCodeGenerator getInstance() {
		return self;
	}

	public static void main(String[] args)  throws IOException{
		QRCodeGenerator qr = new QRCodeGenerator();
		//qr.generate("99526", 200, 100, 8, CODE_TYPE.QR);
		
		qr.generateAsFile(
				"17041413283509585",
				600, 200, 10, CODE_TYPE.BAR, new File("d:/tm128.jpg"), FORMAT.JPEG);
	}

	public void generateAsFile(String content, int width, int height,
			int margin, CODE_TYPE type, File file, FORMAT format)
			throws IOException {
		BitMatrix bitMatrix = generate(content, width, height, margin, type);
		writeToFile(bitMatrix, format.toString(), file);
	}
	
	public void generateBarCode39AsFile(String content, int width, int height,
			int margin, CODE_TYPE type, File file, FORMAT format)
			throws IOException {
		BitMatrix bitMatrix = generateBarCode39(content, width, height, margin, type);
		writeToFile(bitMatrix, format.toString(), file);
	}

	public byte[] generateAsBytes(String content, int width, int height,
			int margin, CODE_TYPE type, FORMAT format) throws IOException {
		BitMatrix bitMatrix = generate(content, width, height, margin, type);
		ByteArrayOutputStream bos = writeToByteArray(bitMatrix, format);
		return bos.toByteArray();
	}
	
	private BitMatrix generateBarCode39(String content, int width, int height,
			int margin, CODE_TYPE type) {
		BitMatrix bitMatrix = null;
		try {
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, new Integer(margin));
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			if (type == CODE_TYPE.BAR) {
				int codeWidth = 3 + // start guard
						(7 * 6) + // left bars
						5 + // middle guard
						(7 * 6) + // right bars
						3; // end guard
				codeWidth = Math.max(codeWidth, width);
				bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.CODE_39, codeWidth, height, hints);
			} else {
				bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.QR_CODE, width, height, hints);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitMatrix;
	}

	private BitMatrix generate(String content, int width, int height,
			int margin, CODE_TYPE type) {
		BitMatrix bitMatrix = null;
		try {
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, new Integer(margin));
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			if (type == CODE_TYPE.BAR) {
				int codeWidth = 3 + // start guard
						(7 * 6) + // left bars
						5 + // middle guard
						(7 * 6) + // right bars
						3; // end guard
				codeWidth = Math.max(codeWidth, width);
				bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.CODE_128, codeWidth, height, hints);
			} else {
				bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.QR_CODE, width, height, hints);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitMatrix;
	}

	private ByteArrayOutputStream writeToByteArray(BitMatrix matrix,
			FORMAT format) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (!ImageIO.write(image, format.toString(), output)) {
			throw new IOException("Could not write an image of format "
					+ format + " to " + output);
		}
		return output;
	}

	private void writeToFile(BitMatrix matrix, String format, File file)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "
					+ format + " to " + file);
		}
	}

	private BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

}
