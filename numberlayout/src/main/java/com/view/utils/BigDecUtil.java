package com.view.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class BigDecUtil {
    private BigDecimal decA;
    private BigDecimal decB;
    private BigDecimal result;

    public Number getMultiplyValue(Number mulA, Number mulB) {
        return getValue("multiply", mulA, mulB, mulA.getClass());
    }

    public Number getMultiplyValue(Number mulA, Number mulB, Class clazz) {
        return getValue("multiply", mulA, mulB, clazz);
    }

    public Number getAddValue(Number mulA, Number mulB) {
        return getValue("add", mulA, mulB, mulA.getClass());
    }

    public Number getAddValue(Number mulA, Number mulB, Class clazz) {
        return getValue("add", mulA, mulB, clazz);
    }

    public Number getDivideValue(Number mulA, Number mulB) {
        transToValue(mulA, mulB);
        return decA.divide(decB, 5, BigDecimal.ROUND_CEILING).floatValue();
    }

    private void transToValue(Number a, Number b) {
        decA = new BigDecimal(toString(a));
        decB = new BigDecimal(toString(b));
    }

    private Number getValue(Class clazz) {
        if (clazz == Integer.class) {
            return result.intValue();
        } else if (clazz == Long.class) {
            return result.longValue();
        } else if (clazz == Float.class) {
            return result.floatValue();
        } else if (clazz == Double.class) {
            return result.doubleValue();
        } else {
            return result.floatValue();
        }
    }

    public String toString(Number a) {
        Class number = a.getClass();
        try {
            Field field = number.getField("TYPE");
            Class clazz = (Class) field.get(a);
            Method method = number.getMethod("toString", clazz);
            return (String) method.invoke(null, a);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Number getValue(String methodName, Number mulA, Number mulB, Class clz) {
        transToValue(mulA, mulB);
        Class clazz = decA.getClass();
        try {
            Method method = clazz.getMethod(methodName, BigDecimal.class);
            result = (BigDecimal) method.invoke(decA, decB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return getValue(clz);
    }

}
