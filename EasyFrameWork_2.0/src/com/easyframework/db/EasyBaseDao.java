package com.easyframework.db;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.easyframework.util.EasyLog;

/**
 * EasyBaseDao.java 创建于 2013-7-21 下午11:29:24
 * 
 * @author zhangbp (zhang86-vip@163.com)
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */

public class EasyBaseDao extends Observable {
	private String TAG = "EasyFrameWor";
	private SQLiteOpenHelper dbHelper;

	public EasyBaseDao() {

	}

	public EasyBaseDao(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void setDbHelper(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	/**
	 * 添加一条记录
	 * 
	 * @param entity
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected <T> long insert(T entity) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		long rowId = 0;
		try {
			db = getWritableDatabase();
			ContentValues cv = entityToContentValues(entity);
			if (cv.size() > 0) {
				String tableName = getTableNameByDbTable(entity.getClass());
				if (tableName != null && !tableName.equals("")) {
					rowId = db.insert(tableName, null, cv);
				}
			}
		} catch (Exception e) {
			EasyLog.d(this.TAG, "[insert] into DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return rowId;
	}

	/**
	 * 批量添加记录
	 * 
	 * @param entity
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected <T> long insert(List<T> entitys) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		ArrayList<EasyDbMap> mlists = null;
		String tableName = null;
		long rowId = 0;
		try {
			db = getWritableDatabase();
			db.beginTransaction();

			for (T entity : entitys) {
				if (mlists == null) {
					mlists = parseAnnotation(entity.getClass(), null);
					tableName = getTableNameByDbTable(entity.getClass());
				}
				ContentValues cv = entityToContentValues(entity, mlists);

				if (cv.size() > 0 && tableName != null && !tableName.equals("")) {
					rowId = db.insert(tableName, null, cv);
				}
			}

			db.setTransactionSuccessful();
			db.endTransaction();

		} catch (Exception e) {
			EasyLog.d(this.TAG, "[insert] into DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return rowId;
	}

	/**
	 * 根据条件删除记录
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected int delete(String whereClause, String[] whereArgs, String tableName) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		int count = 0;
		try {
			db = getReadableDatabase();
			count = db.delete(tableName, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (db != null) {
				db.close();
			}
		}
		return count;
	}

	/**
	 * 根据条件修改记录
	 * 
	 * @param entity
	 * @param whereClause
	 * @param whereArgs
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected <T> int update(T entity, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		int count = 0;
		try {
			db = getWritableDatabase();
			ContentValues values = entityToContentValues(entity);
			if (values.size() > 0) {
				String tableName = getTableNameByDbTable(entity.getClass());
				if (tableName != null && !tableName.equals("")) {
					count = db.update(tableName, values, whereClause, whereArgs);
				}
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return 0;
	}

	/**
	 * 执行sql语句查询,把结果封装到list
	 * 
	 * @param sql
	 * @param selectionArgs
	 *            查询的参数
	 * @param mClass
	 *            要封装到的实体
	 * @return list
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected <T> List<T> rawQuery(String sql, String[] selectionArgs, Class<T> mClass) {
		// TODO Auto-generated method stub
		EasyLog.d(TAG, "[rawQuery]: " + sql);

		List<T> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			list = cursorToList(cursor, mClass);
		} catch (Exception e) {
			Log.e(this.TAG, "[rawQuery] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @param selectionArgs
	 *            查询的参数
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected void execSql(String sql, Object[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		EasyLog.d(TAG, "[execSql]: " + sql);
		try {
			db = getWritableDatabase();
			if (selectionArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, selectionArgs);
			}
		} catch (Exception e) {
			Log.e(TAG, "[execSql] DB exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * replace,主键相同就更新，否则插入
	 * 
	 * @param entity
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	protected <T> long replace(T entity) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		long rowId = 0;
		try {
			db = getWritableDatabase();
			ContentValues cv = entityToContentValues(entity);
			if (cv.size() > 0) {
				String tableName = getTableNameByDbTable(entity.getClass());
				if (tableName != null && !tableName.equals("")) {
					rowId = db.replace(tableName, null, cv);
				}
			}
		} catch (Exception e) {
			EasyLog.d(this.TAG, "[insert] into DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return rowId;
	}

	/**
	 * 从DbTable里解析出tableName
	 * 
	 * @param entityClass
	 * @return tableName
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	public static String getTableNameByDbTable(Class<?> entityClass) {
		if (entityClass.isAnnotationPresent(EasyDbTable.class)) {
			EasyDbTable table = (EasyDbTable) entityClass.getAnnotation(EasyDbTable.class);
			return table.value();
		}
		return null;
	}

	/**
	 * 解析javaBean里的字段对应的注解
	 * 
	 * @param entityClass
	 * @return map
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	public static ArrayList<EasyDbMap> parseAnnotation(Class<?> entityClass, Cursor mCursor) {
		// TODO Auto-generated method stub
		Field[] Fields = entityClass.getFields();
		ArrayList<EasyDbMap> mArrayList = new ArrayList<EasyDbMap>();
		for (Field field : Fields) {
			if (field.isAnnotationPresent(EasyDbColumn.class)) {
				EasyDbMap mEasyDbMap = new EasyDbMap();
				mEasyDbMap.columnName = field.getAnnotation(EasyDbColumn.class).value();
				mEasyDbMap.mField = field;
				if (mCursor != null) {
					int index = mCursor.getColumnIndex(mEasyDbMap.columnName);
					if (index >= 0) {
						mEasyDbMap.columnIndex = index;
					}
				}

				mArrayList.add(mEasyDbMap);
			}
		}
		return mArrayList;
	}

	/**
	 * 实体到ContentValues转换
	 * 
	 * @param entity
	 * @param map
	 *            ,Field和columnName 对照表
	 * @return ContentValues
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	public static ContentValues entityToContentValues(Object entity) throws IllegalAccessException {
		ArrayList<EasyDbMap> mlistm = parseAnnotation(entity.getClass(), null);
		return entityToContentValues(entity, mlistm);
	}

	/**
	 * 实体到ContentValues转换
	 * 
	 * @param entity
	 * @param map
	 *            ,Field和columnName 对照表
	 * @return ContentValues
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	public static ContentValues entityToContentValues(Object entity, ArrayList<EasyDbMap> mlistm) throws IllegalAccessException {
		ContentValues mContentValues = new ContentValues();
		Field mField;
		String columnName;

		for (EasyDbMap mEasyDbMap : mlistm) {
			columnName = mEasyDbMap.columnName;
			mField = mEasyDbMap.mField;
			mField.setAccessible(true);
			Object fieldValue = mField.get(entity);
			if (fieldValue == null)
				continue;
			mContentValues.put(columnName, fieldValue.toString());
		}
		return mContentValues;
	}

	/**
	 * Cursor到list转换
	 * 
	 * @param cursor
	 * @return List<T>
	 * @author Zhangbp(zhang86-vip@163.com)
	 */

	public static <T> List<T> cursorToList(Cursor cursor, Class<T> mcalss) throws IllegalAccessException, InstantiationException {
		List<T> list = new ArrayList<T>();
		ArrayList<EasyDbMap> mlistm = null;
		Object columnValue = null;
		
		if (mcalss != null) {
			mlistm = parseAnnotation(mcalss, cursor);
		}

		while (cursor.moveToNext()) {
			T entity = null;
			if (mlistm != null && mlistm.size() > 0) {
				entity = mcalss.newInstance();
				for (EasyDbMap mEasyDbMap : mlistm) {
					columnValue = null;
					Class<?> fieldType = mEasyDbMap.mField.getType();
					if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
						columnValue = cursor.getInt(mEasyDbMap.columnIndex);
					} else if (String.class == fieldType) {
						columnValue = cursor.getString(mEasyDbMap.columnIndex);
					} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
						columnValue = cursor.getLong(mEasyDbMap.columnIndex);
					} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
						columnValue = cursor.getFloat(mEasyDbMap.columnIndex);
					} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
						columnValue = cursor.getShort(mEasyDbMap.columnIndex);
					} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
						columnValue = cursor.getDouble(mEasyDbMap.columnIndex);
					} else if (Blob.class == fieldType) {
						columnValue = cursor.getBlob(mEasyDbMap.columnIndex);
					}
					if (columnValue != null && mEasyDbMap.mField != null) {
						mEasyDbMap.mField.set(entity, columnValue);
					}
				}
			}
			list.add((T) entity);
		}

		return list;
	}

	public SQLiteDatabase getWritableDatabase() {
		return this.dbHelper.getWritableDatabase();
	}

	public SQLiteDatabase getReadableDatabase() {
		return this.dbHelper.getReadableDatabase();
	}

}