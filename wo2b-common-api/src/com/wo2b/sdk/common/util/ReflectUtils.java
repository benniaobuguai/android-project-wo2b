package com.wo2b.sdk.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * 常用的反射工具类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-24
 */
public class ReflectUtils
{

	/**
	 * TODO: 返回泛型, 待测试
	 * 
	 * @param clazz
	 * @param type
	 * @return
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericType(Class<?> clazz)
	{
		Class<T> type = (Class<T>) ((ParameterizedType) clazz.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];

		return type;
	}

	/**
	 * 执行某个类的某个方法, 目录类要有无参构造函数
	 * 
	 * @param className 目标类名
	 * @param methodName 方法名
	 * @param parameterTypes 方法参数类型
	 * @param parameters 方法的参数, 数据类型对应的#parameterTypes
	 * @return
	 * @throws Exception
	 */
	public static Object executeMethod(String className, String methodName, Class<?>[] parameterTypes,
			Object[] parameters) throws Exception
	{
		Class<?> clazz = Class.forName(className);
		Object object = clazz.newInstance();
		Method method = clazz.getMethod(methodName, parameterTypes);

		return method.invoke(object, parameters);
	}

}
