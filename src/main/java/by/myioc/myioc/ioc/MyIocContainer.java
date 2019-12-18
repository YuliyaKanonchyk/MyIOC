package by.myioc.myioc.ioc;

import by.myioc.myioc.ioc.annotations.Component;
import by.myioc.myioc.ioc.annotations.ComponentScan;
import by.myioc.myioc.ioc.annotations.Qualifier;
import by.myioc.myioc.ioc.annotations.Value;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class MyIocContainer implements MyIOC {


    private PriorityQueue<Method> queueMethods = new PriorityQueue<>(new MethodComparator());
    private PriorityQueue<Constructor> queueConstructor;
    private Class configClass;
    private Map<String, Object> stringObjectMap;

    public MyIocContainer(Class configClass) {
        this.configClass = configClass;
        this.stringObjectMap = new ConcurrentHashMap<>();
//        queueMethodCreation();
//        instanceCreationBasedOnPriorityQueue();
        queueConstructorCreation();
        createComponentsBasedOnClasses();
//        instanceCreationBasedOnConstructorPriorityQueue();
//        this.createComponentsBasedOnClasses(); //create dog and cat
//        this.createComponentsBasedOnMethods(); //create user

//        this.injectValue();
//        this.injectSetter();
    }

    private void queueMethodCreation() {
        Method[] methods = configClass.getMethods();
        for (Method field : methods) {
            if (field.isAnnotationPresent(Component.class)) {
                queueMethods.add(field);
            }
        }
    }

    private void queueConstructorCreation() {

        ComponentScan annotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String s = annotation.basePackage();
        Reflections reflections = new Reflections(s);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        Set<Constructor> toArr = new TreeSet<>(new ConstructorComparator());

        queueConstructor = new PriorityQueue<>(toArr);

        System.out.println(typesAnnotatedWith);
        for (Class<?> aClass : typesAnnotatedWith) {
            if (aClass.getDeclaredConstructors().length == 1) {
                if (aClass.getConstructors()[0].getParameterCount() == 0) {
//                    queueConstructor.add(aClass.getConstructors()[0]);
                        toArr.add(aClass.getConstructors()[0]);

                } else {
                    Constructor bigConstructor = null;
                    for (Constructor constructor : aClass.getConstructors()) {
                        if (bigConstructor == null) {
                            bigConstructor = constructor;
                        } else {
                            if (bigConstructor.getParameterCount() < constructor.getParameterCount()) {
                                bigConstructor = constructor;
                            }
                        }
                    }
                    toArr.add(bigConstructor);
                }
            }
        }
        queueConstructor = new PriorityQueue<>(toArr);
        System.out.println(queueConstructor);
    }


    private void instanceCreationBasedOnConstructorPriorityQueue() {
        for (Constructor constructor : queueConstructor) {
            Object constructorInstance = null; //для того чтобы положить в Map
            String nameOfConstructor = null;//для того чтобы положить в Map
            constructor.setAccessible(true);
            if (constructor.getParameterCount() == 0) {
                constructorInstance = constructor;
                nameOfConstructor = constructorInstance.getClass().getSimpleName();
            } else {
                Class[] parameterTypes = constructor.getParameterTypes();
                Object[] objects = new Object[parameterTypes.length];
                for (Class<?> parameter : parameterTypes) {
                    int count = 0;
                    for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                        if (parameter.equals(stringObjectEntry)) {
                            objects[count++] = stringObjectEntry.getValue();
                        }
                    }
                }
                try {
                    constructorInstance = constructor.newInstance(configClass.newInstance(), objects);
                    nameOfConstructor = constructorInstance.getClass().getSimpleName();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


            stringObjectMap.put(nameOfConstructor, constructorInstance);
        }
    }

    private void instanceCreationBasedOnMethodPriorityQueue() {
        for (Method method : queueMethods) {
            Object methodInstance = null;
            String nameOfMethod = null;
            try {
                if (method.getParameterCount() == 0) {
                    methodInstance = method.invoke(configClass.newInstance());
                    nameOfMethod = methodInstance.getClass().getSimpleName();
                } else {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] objects = new Object[parameterTypes.length];
                    for (Class<?> parameter : parameterTypes) {
                        int count = 0;
                        for (Map.Entry<String, Object> a : stringObjectMap.entrySet()) {
                            if (parameter.equals(a.getValue())) {
                                objects[count++] = a.getValue();
                            }
                        }
                    }
                    methodInstance = method.invoke(configClass.newInstance(), objects);
                    nameOfMethod = methodInstance.getClass().getSimpleName();
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            stringObjectMap.put(nameOfMethod, methodInstance);
        }
//        System.out.println(stringObjectMap);
    }

    private void injectValue() {
        for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
            for (Field declaredField : stringObjectEntry.getValue().getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Value.class)) {
                    declaredField.setAccessible(true);
                    String value = declaredField.getAnnotation(Value.class).value();
                    try {
                        declaredField.set(stringObjectEntry.getValue(), value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void injectSetter() {
        for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
            a:
            for (Method method : stringObjectEntry.getValue().getClass().getDeclaredMethods()) {
                String name = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] a = new Object[parameterTypes.length];
                if (name.contains("set")) {
                    for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                        if (parameterTypes[0].equals(String.class)) continue a;
                        if (entry.getValue().getClass().equals(parameterTypes[0])) {
                            a[0] = entry.getValue();
                        }
                    }
                    try {
                        method.invoke(stringObjectEntry.getValue(), a);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Object getComponent(String componentName) {
        return stringObjectMap.get(componentName);
    }

    @Override
    public <T> T getComponent(String componentName, Class<T> c) {
        return c.cast(stringObjectMap.get(componentName));
    }

    @Override
    public List<Object> getComponents() {
        ArrayList<Object> objects = new ArrayList<>();
        for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
            objects.add(stringObjectEntry.getValue());
        }
        return objects;
    }

    private void createComponentsBasedOnClasses() {
        for (Constructor<?> constructor : queueConstructor) {
            try {
                if (constructor.getParameterCount() == 0) {
                    constructor.newInstance();
                } else {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] obj = new Object[constructor.getParameterCount()];
                    int count = 0;
                    for (Class<?> parameterType : parameterTypes) {
                        if (parameterType.equals(String.class)) {
                            if (parameterType.isAnnotationPresent(Value.class)) {
                                obj[count++] = parameterType.getAnnotation(Value.class).value();
                            } else {
                                obj[count++] = "";
                            }
                        } else {
                            for (Map.Entry<String, Object> objectEntry : stringObjectMap.entrySet()) {
                                if (parameterType.equals(objectEntry.getValue().getClass())) {
                                    obj[count++] = objectEntry.getValue();
                                }
                            }
                        }

                    }
                    stringObjectMap.put(constructor.getDeclaringClass().getSimpleName().toLowerCase(), constructor.newInstance(obj));
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void createComponentsBasedOnMethods() {
        Method[] methods = configClass.getMethods();
        Object o = null;
        try {
            o = configClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(Component.class)) {

//                String name = method.getName();

                String name = method.getAnnotation(Component.class).name();


                int parameterCount = method.getParameterCount();
                Object[] a = new Object[parameterCount];
                int count = 0;
                final Parameter[] parameters = method.getParameters();

                for (Parameter parameterType : parameters) {
                    if (parameterType.isAnnotationPresent(Qualifier.class)) {


                        String name1 = parameterType.getAnnotation(Qualifier.class).name();


                        a[count++] = stringObjectMap.get(name1);
                        continue;
                    }
                    for (Map.Entry<String, Object> obj : stringObjectMap.entrySet()) {
                        if (parameterType.equals(obj.getValue().getClass())) {
                            a[count++] = obj.getValue();
                            break;
                        }
                    }
                }
                try {
                    Object invoke = method.invoke(o, a);
                    if (invoke == null) throw new NullPointerException("Invoke method which the return null");
                    stringObjectMap.put(name, invoke);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
