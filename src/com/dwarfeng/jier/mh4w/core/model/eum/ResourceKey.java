package com.dwarfeng.jier.mh4w.core.model.eum;

import com.dwarfeng.dutil.basic.str.Name;

/**
 * 有关于资源的键。
 * @author  DwArFeng
 * @since 0.0.1-beta
 */
public enum ResourceKey implements Name{
	
	/**记录器设置*/
	LOGGER_SETTING("logger.setting"),
	/**主程序的配置*/
	CONFIGURATION_CORE("configuration.core"),
	/**记录器多语言化的设置*/
	MUTILANG_LOGGER_SETTING("mutilang.logger.setting"),
	/**记录器多语言化的设置*/
	MUTILANG_LABEL_SETTING("mutilang.label.setting"),
	/**工具信息*/
	TOOL_INFO("tool.info"),
	/**工具库*/
	TOOL_LIB("tool.lib"),
	/**工具数据*/
	TOOL_DATA("tool.data"),
	/**工具历史*/
	TOOL_HISTORY("tool.history"),
	/**班次信息*/
	DEFINE_SHIFTS("define.shifts"),
	/**工作信息*/
	DEFINE_JOBS("define.jobs"),
	/**日期类型*/
	STORAGE_DATE_TYPE("storage.date_type"),
	/**考勤补偿*/
	STORAGE_ATT_OFFSET("storage.att_offset"),

	;

	private final String name;
	
	private ResourceKey(String name) {
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.str.Name#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
}
