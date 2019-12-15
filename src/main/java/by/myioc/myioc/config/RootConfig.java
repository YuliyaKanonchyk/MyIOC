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

    @Component(name = "DiDiDi")
    public User user1(@Qualifier(name = "dog1") Dog dog1, Cat cat) {
        return new User("User1", dog1, cat);
    }

    @Component(name = "JJJ")
    public User user2(@Qualifier(name = "dog2") Dog dog2, Cat cat) {
        return new User("User2", dog2, cat);
    }

    @Component(name = "Dog1")
    public Dog dog1() {
        return new Dog("Dog1");
    }

    @Component(name = "Dog2")
    public Dog dog2() {
        return new Dog("Dog2");
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
