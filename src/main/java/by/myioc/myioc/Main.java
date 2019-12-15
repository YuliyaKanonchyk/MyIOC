package by.myioc.myioc;

import by.myioc.myioc.config.RootConfig;
import by.myioc.myioc.ioc.MyIocContainer;

public class Main {
    public static void main(String... args) {
        MyIocContainer myIocContainer = new MyIocContainer(RootConfig.class);
        System.out.println(myIocContainer.getComponents());
    }
}
