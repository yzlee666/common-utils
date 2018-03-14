package cn.yzlee.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import cn.yzlee.exception.ApplicationException;
import cn.yzlee.exception.UnableCreateFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(FileUtil.class);

	/**
	 * 从输入流中读取字节写入到指定文件
	 * 
	 * @param inputStream
	 * @param outFile
	 * @return
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static long writeToFile(InputStream inputStream, File outFile)
			throws FileNotFoundException, IOException {
		FileOutputStream fout = new FileOutputStream(outFile);
		BufferedOutputStream out = new BufferedOutputStream(fout);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		byte[] buffer = new byte[8192];
		int position = 0;
		int len = 0;
		try {
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
				position += len;
			}
			fout.flush();
			out.flush();
			logger.info("写入文件{} {}字节", outFile.getName(), position);
		} finally {
			fout.close();
			out.close();
		}
		return position;
	}

	/**
	 * 创建指定路径目录
	 * @param dirPath 指定路径
	 * @return
	 */
	public static File mkDir(String dirPath) {
		File dir = null;
		try {
			dir = new File(dirPath);
			if (!dir.exists() && !dir.mkdir()) {
				throw new UnableCreateFileException();
			}
			if (!dir.isDirectory()) {
				dir.mkdir();
			}
		} catch (Exception exp) {
			throw new UnableCreateFileException(exp);
		}
		return dir;
	}

	/**
	 * 压缩一组文件成一个压缩文件
	 * @param srcFiles File {@link java.io.File} 数组,待压缩的文件数组
	 * @param desFile File 指定生成的压缩文件
	 * @throws java.io.IOException
	 */
	public static void compressFile(File[] srcFiles, File desFile)
			throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(desFile));
		ZipOutputStream zos = new ZipOutputStream(bos);
		for (File file : srcFiles) {
			ZipEntry entry = new ZipEntry(file.getName());
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buffer = new byte[512];
			int b = 0;
			while ((b = bis.read(buffer)) > -1) {
				zos.write(buffer, 0, b);
			}
			bis.close();
			zos.closeEntry();
			logger.debug("{} 压缩完成", file.getName());
		}
		zos.flush();
		zos.close();
		logger.debug("生成压缩文件:{}", desFile.getAbsolutePath());
	}

	public static void compressFile(InputStream stream, String name,
			ZipOutputStream zos) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(stream);
			ZipEntry entry = new ZipEntry(name);
			zos.putNextEntry(entry);
			byte[] buffer = new byte[512];
			int b = 0;
			while ((b = bis.read(buffer)) > -1) {
				zos.write(buffer, 0, b);
			}
			bis.close();
			bis = null;
			zos.closeEntry();
			logger.debug("{} 压缩完成", name);
		} catch (Exception exp) {
			exp.printStackTrace();
			logger.error("执行流压缩失败,{}", exp.getMessage());
		} finally {
			try {
				if (bis != null)
					bis.close();
				stream = null;
			} catch (IOException ioe) {
				logger.error("执行流压缩后关闭输入流失败,{}", ioe.getMessage());
			}
		}
	}
	
	/**
	 * 解压缩zip文件
	 * @param destDir 解压后文件存放的目录
	 * @param zipFile 需要解压缩的文件
	 * @throws Exception
	 */
	public static void unZip(Path destDir,File zipFile) throws Exception{
		try(ZipFile file=new ZipFile(zipFile,Charset.forName("GBK"));Stream<? extends ZipEntry> entries=file.stream()){
			entries.filter(e-> ! e.isDirectory()).parallel().forEach(e->{			
				try(BufferedInputStream bis=new BufferedInputStream(file.getInputStream(e))){	
					Path tp=destDir.resolve(e.getName());
					if(!Files.exists(tp.getParent())){
						Files.createDirectories(tp.getParent());
					}
					Path eFile=Files.createFile(tp);
					
					try(BufferedOutputStream fos=new BufferedOutputStream(Files.newOutputStream(eFile))){
						byte [] buffer=new byte[8192];
						int len=0;
						while((len=bis.read(buffer))>0){
							fos.write(buffer, 0, len);
						}
						fos.flush();
					}
				}catch(IOException ioe){
					ioe.printStackTrace();
					throw new ApplicationException(ioe);
				}
			});;
		}
	}
	
	public static void unFlatZip(Path destDir,File zipFile) throws Exception{
		try(ZipFile file=new ZipFile(zipFile);Stream<? extends ZipEntry> entries=file.stream()){
			entries.filter(e-> !e.isDirectory()).parallel().forEach(e->{				
				try(BufferedInputStream bis=new BufferedInputStream(file.getInputStream(e))){					
					Path eFile=Files.createFile(destDir.resolve(e.getName()));
					try(BufferedOutputStream fos=new BufferedOutputStream(Files.newOutputStream(eFile))){
						byte [] buffer=new byte[8192];
						int len=0;
						while((len=bis.read(buffer))>0){
							fos.write(buffer, 0, len);
						}
						fos.flush();
					}
				}catch(IOException ioe){
					ioe.printStackTrace();
					throw new ApplicationException(ioe);
				}
			});;
		}
	}

	public static void zip(Path souceFile, ZipOutputStream out, String base)
			throws IOException {

		if (Files.isDirectory(souceFile)) {
			try (DirectoryStream<Path> files = Files
					.newDirectoryStream(souceFile)) {
				out.putNextEntry(new ZipEntry(base + "/"));
				base = base.length() == 0 ? "" : base + "/";
				for (Path file : files) {
					zip(file, out, base + file.getFileName());
				}
			}
		} else {
			if (base.length() > 0) {
				out.putNextEntry(new ZipEntry(base));
			} else {
				out.putNextEntry(new ZipEntry(souceFile.getFileName()
						.toString()));
			}
			try (BufferedInputStream bufferInput = new BufferedInputStream(
					Files.newInputStream(souceFile))) {
				int b;
				byte[] by = new byte[8192];
				while ((b = bufferInput.read(by)) != -1) {
					out.write(by, 0, b);
				}
			}
		}
	}

	public static void writeObject(Object ob, String file) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ob);
			oos.close();
		} catch (Exception exp) {
			logger.error("执行对象序列化失败", exp);
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException ioe) {
				logger.error("执行对象序列化后关闭输出流失败,{}", ioe.getMessage());
			}
		}
	}

	public static Object readObject(File file) throws Exception {
		ObjectInputStream ois = null;
		try {
			logger.debug("读取序列化文件：{}", file.getAbsolutePath());
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (FileNotFoundException fne) {
			throw fne;
		} catch (Exception exp) {
			logger.error("执行对象序列化失败,{}", exp);
			return exp;
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException ioe) {
				logger.error("执行对象序列化后关闭输出流失败,{}", ioe.getMessage());
			}
		}
	}

	public static void compressDir(File[] srcFiles, File desFile)
			throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(desFile));
		ZipOutputStream zos = new ZipOutputStream(bos);
		for (File file : srcFiles) {
			ZipEntry entry = null;
			if (file.isDirectory()) {
				entry = new ZipEntry(file.getName() + "/");
				zos.putNextEntry(entry);
			} else {
				entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				byte[] buffer = new byte[512];
				int b = 0;
				while ((b = bis.read(buffer)) > -1) {
					zos.write(buffer, 0, b);
				}
				bis.close();
			}
			zos.closeEntry();
			logger.debug("{} 压缩完成", file.getName());
		}
		zos.flush();
		zos.close();
		logger.debug("生成压缩文件:{}", desFile.getAbsolutePath());
	}

	/**
	 * 删除指定目录以及子目录的所有文件
	 * @param temp
	 */
	public static void removeFiles(Path temp){
		try {
			Files.walkFileTree(temp, new FileVisitor<Path>() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object,
				 * java.nio.file.attribute.BasicFileAttributes)
				 */
				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					// TODO Auto-generated method stub
					return FileVisitResult.CONTINUE;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.nio.file.FileVisitor#visitFile(java.lang.Object,
				 * java.nio.file.attribute.BasicFileAttributes)
				 */
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					// TODO Auto-generated method stub
					Files.deleteIfExists(file);
					return FileVisitResult.CONTINUE;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.nio.file.FileVisitor#visitFileFailed(java.lang.Object,
				 * java.io.IOException)
				 */
				@Override
				public FileVisitResult visitFileFailed(Path file,
						IOException exc) throws IOException {
					// TODO Auto-generated method stub
					return FileVisitResult.CONTINUE;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object
				 * , java.io.IOException)
				 */
				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					// TODO Auto-generated method stub
					Files.deleteIfExists(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			logger.debug("删除文件目录 {} 成功", temp);
		} catch (Exception exp) {
			exp.printStackTrace();
			logger.error("删除文件失败", exp);
		}
	}
		
		
		
	public static void main(String[] args) {
		try {
			File zipFile = new File("E:\\update.zip");
			Path dest = Paths.get("e:/","unzip");
			if(!Files.exists(dest)){
				Files.createDirectories(dest);
			}
			FileUtil.unZip(dest, zipFile);
			//Thread.sleep(10000);
			//FileUtil.removeFiles(dest);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
