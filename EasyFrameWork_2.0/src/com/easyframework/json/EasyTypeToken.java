package com.easyframework.json;

import java.io.Serializable;
import java.lang.reflect.Type;

public class EasyTypeToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Type mType;
	
	public EasyTypeToken(Type mType){
		this.mType = mType;
	}
}
