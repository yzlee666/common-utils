package cn.yzlee.util.file;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileCacheImageInputStream;

/**
 * @Author:lyz
 * @Date: 2018/3/14 16:38
 * @Desc: 图片处理类
 **/
public class ImgConvert
{
	private static final String IMG_TYPE="bmp|jpg|png|gif|jpeg";
	
	private static final Pattern p = Pattern.compile(IMG_TYPE);
	
	public static final String PNG= "png";
	public static final String JPEG= "jpg";
	public static final String BMP= "bmp";
	public static final String GIF= "gif";
	
	public static void convert(File source,String formateName,File result){
		try{
			source.canRead();
			BufferedImage src=ImageIO.read(source);
			ImageIO.write(src, "png", result);
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	/**
	 * 按指定伸缩比例进行图像伸缩并转存为指定的文件格式类型
	 * @param widthRatio 宽比
	 * @param heightRatio 高比
	 * @param srcImg 源图像文件
	 * @param resultImg 转换后的图像文件
	 * @param formatName 文件格式
	 */
	public static void scaleImage(double widthRatio,double heightRatio,File srcImg,File resultImg,String formatName){
		InputStream input=null;
		OutputStream output=null;
		try{
			if(srcImg.canRead()){
				input=new FileInputStream (srcImg);
				output=new FileOutputStream (resultImg);
				scaleImage(widthRatio,heightRatio,input,output,formatName);
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}finally{
			try{
				input.close();
				output.close();
			}catch(IOException iep){
				iep.printStackTrace();
			}
		}
	}
	
	public static void scaleImage(double widthRatio,double heightRatio,InputStream srcImg,OutputStream resultImg,String formatName){
		try{
			BufferedImage src=ImageIO.read(srcImg);
			int width=(int)(src.getWidth()* widthRatio);
			int height=(int)(src.getHeight()*heightRatio);
			Image tmp=src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics gp=tag.getGraphics();
			gp.drawImage(tmp, 0, 0,null);
			gp.dispose();
			ImageIO.write(tag,formatName,resultImg);
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	/**
	 * 按指定高度、宽度伸缩图像
	 * @param width 指定高度
	 * @param height 指定宽度
	 * @param srcImg 原图像文件
	 * @param resultImg 伸缩后图像文件
	 * @param formatName 保存的图像格式
	 */
	public static void scaleImage(int width,int height,File srcImg,File resultImg,String formatName,int imageType){
		InputStream input=null;
		OutputStream output=null;
		try{
			if(srcImg.canRead()){
				input=new FileInputStream (srcImg);
				output=new FileOutputStream (resultImg);
				scaleImage(width,height,input,output,formatName,imageType);
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}finally{
			try{
				input.close();
				output.close();
			}catch(IOException iep){
				iep.printStackTrace();
			}
			
		}
	}
	
	public static ImageSize getImgSize(File srcImg){
		try{
			BufferedImage src=ImageIO.read(srcImg);
			int oWidth=src.getWidth(),oHeight=src.getHeight();
			return createImageSize(oWidth,oHeight);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return null;
	}
	
	private static ImageSize createImageSize(int width,int height){
		return new ImageSize(width,height);
	}
	
	public static void autoScaleImage(int standar,File srcImg,File resultImg,String formatName,int imageType){
		try{
			int width,height;
			double widthRatio,heightRatio;
				BufferedImage src=ImageIO.read(srcImg);
				int oWidth=src.getWidth(),oHeight=src.getHeight();
				if(oWidth<oHeight){
					width=standar;
					widthRatio=standar/(double)oWidth;
					height=(int)(oHeight*widthRatio);
				}else{
					height=standar;
					heightRatio=standar/(double)oHeight;
					width=(int)(oWidth*heightRatio);
				}
				Image tmp=src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics gp=tag.getGraphics();
				gp.drawImage(tmp, 0, 0,null);
				gp.dispose();
				ImageIO.write(tag,formatName,resultImg);
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	public static class ImageSize{
		private  int width;
		private int height;
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
		public ImageSize(int width,int height){
			this.width=width;
			this.height=height;
		}
	}
	
	/**
	 * 按指定高度、宽度伸缩图像
	 * @param width 指定高度
	 * @param height 指定宽度
	 * @param srcImg 原图像文件
	 * @param resultImg 伸缩后图像文件
	 * @param formatName 保存的图像格式
	 */
	public static void scaleImage(int width,int height,InputStream srcImg,OutputStream resultImg,String formatName,int imageType){
		try{
			BufferedImage src=ImageIO.read(srcImg);
			Image tmp=src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, imageType);
			Graphics gp=tag.getGraphics();
			gp.drawImage(tmp, 0, 0,null);
			gp.dispose();
			ImageIO.write(tag,formatName,resultImg);
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	public static void translate(int x,int y,int width,int height,File srcImg,File resultImg,String formatName,int imageType)throws IOException{
		BufferedImage src=ImageIO.read(srcImg).getSubimage(x, y, width, height);
		BufferedImage subImage=new BufferedImage(width, height, imageType);
		Graphics g=subImage.getGraphics();
		g.drawImage(src, 0, 0,null);
		g.dispose();
		ImageIO.write(subImage, formatName, resultImg);
	}
	
	/**
	 * 获取指定图像文件的格式
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static String getFormateName(File img) throws Exception{
		FileCacheImageInputStream imgInput=new FileCacheImageInputStream(new FileInputStream(img),null);
		Iterator<ImageReader> ite=ImageIO.getImageReaders(imgInput);
		imgInput.close();
		if(ite.hasNext()){
			return ite.next().getFormatName();
		}
		return null;
	}
	
	public static boolean validImgFormat(String formatName) {
		Matcher m = p.matcher(formatName.toLowerCase());
		return m.matches();
	}

}
