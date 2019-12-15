package by.myioc.myioc.ioc;

import java.util.List;

public interface MyIOC {
    Object getComponent(String componentName);
    <T> T getComponent(String componentName, Class<T> c);
    List<Object> getComponents();
}
