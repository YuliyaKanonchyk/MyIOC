package by.myioc.myioc.config;

import by.myioc.myioc.entity.Cat;
import by.myioc.myioc.entity.Dog;
import by.myioc.myioc.entity.User;
import by.myioc.myioc.ioc.annotations.Component;
import by.myioc.myioc.ioc.annotations.ComponentScan;
import by.myioc.myioc.ioc.annotations.Configuration;
import by.myioc.myioc.ioc.annotations.Qualifier;

@Configuration
@ComponentScan(basePackage = "by.myioc.myioc.entity")
public class RootConfig {

    @Component(name = "111")
    public User user1(@Qualifier(name = "dog1") Dog dog1, Cat cat) {
        return new User("коля", dog1, cat);
    }

    @Component(name = "222")
    public User user2(@Qualifier(name = "dog2") Dog dog2, Cat cat) {
        return new User("паша", dog2, cat);
    }

    @Component(name = "Dog1")
    public Dog dog1() {
        return new Dog("тузик");
    }

    @Component(name = "Dog2")
    public Dog dog2() {
        return new Dog("шарик");
    }
//
//    @Component
//    public User test2() {
//        User user = new User();
//        user.setName("test2");
//        return user;
//    }
//
//    @Component
//    public String myName() {
//        return "Name";
//    }
//
//    @Component
//    public List<User> userList() {
//        return new ArrayList<>();
//    }
}
