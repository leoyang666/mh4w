package com.dwarfeng.jier.mh4w.core.view.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;
import com.dwarfeng.jier.mh4w.core.model.struct.UnsafeAttendanceOffset;

/**
 * 详细面板观察器。
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public interface DetailFrameObverser extends Obverser{

	/**
	 * 通知隐藏详细界面。
	 */
	public void fireHideDetailFrame();

	/**
	 * 通知导出统计结果
	 */
	public void fireExportCountResult();

	/**
	 * 通知提交考勤补偿。
	 * @param unsafeAttendanceOffset 不安全考勤补偿。
	 */
	public void fireSubmitAttendanceOffset(UnsafeAttendanceOffset unsafeAttendanceOffset);

	/**
	 * 通知清除考勤补偿。
	 */
	public void fireClearAttendanceOffset();

	/**
	 * 通知保存考勤补偿。
	 */
	public void fireSaveAttendanceOffset();

	/**
	 * 通知读取考勤数据补偿。
	 */
	public void fireLoadAttendanceOffset();

	/**
	 * 通知移除考勤数据补偿。
	 * @param index 移除的序号。
	 */
	public void fireRemoveAttendanceOffset(int index);

	/**
	 * 通知更新统计结果。
	 */
	public void fireUpdateCountResult();

}
