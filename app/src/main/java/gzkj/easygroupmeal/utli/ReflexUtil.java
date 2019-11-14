package gzkj.easygroupmeal.utli;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * 排序反射类
 */
public class ReflexUtil {
    //getMethod
    static public Object invokeMethod(String propertiesName, Object object) {
        try {
            if(object==null) return null;
            if (!propertiesName.contains(".")) {
                String methodName = "get"+getMethodName(propertiesName);
                Method method = object.getClass().getMethod(methodName);
                return method.invoke(object);
            }
            String methodName = "get"+getMethodName(propertiesName.substring(0,propertiesName.indexOf(".")));
            Method method = object.getClass().getMethod(methodName);
            return invokeMethod(propertiesName.substring(propertiesName.indexOf(".")+1), method.invoke(object));

        } catch (Exception e) {
            Log.e(e.toString(), e.getMessage());
            return null;
        }
    }
    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
