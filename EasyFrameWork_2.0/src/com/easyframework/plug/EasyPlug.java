package com.easyframework.plug;

import android.content.res.Resources;
import android.content.res.Resources.Theme;

/**
 * @author zhangbp
 *
 */
public interface EasyPlug {
	public Resources getResources(String packageName);
	public Theme getTheme(String packageName);
}
