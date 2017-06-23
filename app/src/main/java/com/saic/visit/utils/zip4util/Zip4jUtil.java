package com.saic.visit.utils.zip4util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 *
 * @author caoruomou
 */
public class Zip4jUtil {
	private static Zip4jUtil intanceUtil = null;
	final private static String STRPASS_STRING = "tmdtwwj-gnlm-123";
	private Zip4jUtil() {
	}
	
	static public Zip4jUtil getZip4jUtilInstance() {
		if(intanceUtil == null)
		{
			intanceUtil = new Zip4jUtil();
		}
		return intanceUtil;
	}
	
	/**
	 *  Demonstrates adding files to zip file with AES Encryption
	 * @param zipFilePathName 压缩后的压缩文件名称
	 * @param zipFilePath 需要压缩的文件
	 */
	public void AddFilesWithAESEncryption(String zipFilePathName,String zipFilePath){
		ZipFile zipFile;
		
		ArrayList<File> filesToAdd = new ArrayList<File>();
		try {
			File file = new File(zipFilePath);
			if (!file.exists())
				throw new RuntimeException(zipFilePath + "不存在！");
			try {
				// Initiate ZipFile object with the path/name of the zip file.
				zipFile = new ZipFile(zipFilePathName);
				/* 判断是目录还是文件 */
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						/* 递归 */
						System.out.println("压缩：" + files[i].getName());
						filesToAdd.add(files[i]);
					}
				} else {
					System.out.println("压缩：" + zipFilePath);
					filesToAdd.add(file);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression
			
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 

			parameters.setEncryptFiles(true);
			
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			
			// Set password
			parameters.setPassword(STRPASS_STRING);
			
			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	public void ExtractByLoopAllFiles(String zipFilePathName,String zipFilePath) {
		File file = new File(zipFilePathName);
		if (!file.exists())
			throw new RuntimeException(zipFilePathName + "不存在！");
		
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipFilePathName);
			
			// Check to see if the zip file is password protected 
			if (zipFile.isEncrypted()) {
				// if yes, then set the password for the zip file
				zipFile.setPassword(STRPASS_STRING);
			}
			
			// Get the list of file headers from the zip file
			List fileHeaderList = zipFile.getFileHeaders();
			
			// Loop through the file headers
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
				// Extract the file to the specified destination
				zipFile.extractFile(fileHeader, zipFilePath);
			}
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
		
	}
}
