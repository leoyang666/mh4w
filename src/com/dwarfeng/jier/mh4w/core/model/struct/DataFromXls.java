package com.dwarfeng.jier.mh4w.core.model.struct;

/**
 * 指示着这条数据是从 xls 中读取的。
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public interface DataFromXls {

	/**
	 * 获取文件的名字。
	 * @return 文件的名字。
	 */
	public String getFileName();
	
	/**
	 * 获取数据的行号。
	 * @return 数据的行号。
	 */
	public int getRow();
	
}
