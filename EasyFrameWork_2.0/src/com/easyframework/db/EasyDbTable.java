package com.easyframework.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface EasyDbTable {
	/**
	 * 表名注解
	 * @author Zhangbp(zhang86-vip@163.com)
	 */
	public String value();
}