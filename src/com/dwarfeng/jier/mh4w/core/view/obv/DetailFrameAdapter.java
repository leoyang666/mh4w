package com.dwarfeng.jier.mh4w.core.view.obv;

import com.dwarfeng.jier.mh4w.core.model.struct.UnsafeAttendanceOffset;

/**
 * ��ϸ����۲�����������
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public abstract class DetailFrameAdapter implements DetailFrameObverser {

	@Override
	public void fireHideDetailFrame() {}
	@Override
	public void fireExportCountResult() {}
	@Override
	public void fireSubmitAttendanceOffset(UnsafeAttendanceOffset unsafeAttendanceOffset) {}
	
}
