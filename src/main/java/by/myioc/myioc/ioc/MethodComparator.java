package by.myioc.myioc.ioc;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodComparator implements Comparator<Method> {
    @Override
    public int compare(Method o1, Method o2) {
        return Integer.compare(o1.getParameterCount(), o2.getParameterCount());
    }
}
