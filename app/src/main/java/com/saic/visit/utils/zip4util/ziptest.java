package com.saic.visit.utils.zip4util;

public class ziptest {
	public static void main(String[] args) {
		//压缩,生成的压缩文件名
		//ZipCompressor zc = new ZipCompressor("E:\\test\\01.在阿尔卑斯山上.hymid");
		//需要压缩的文件、目录
		//zc.compress("E:\\test\\01.在阿尔卑斯山上.mid");

		//解密
		ZipDecompression zd = new ZipDecompression("E:\\test\\01.在阿尔卑斯山上.hymid","E:\\test");
		zd.decompression();
	}
}
