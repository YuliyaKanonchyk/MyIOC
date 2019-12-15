package by.myioc.myioc.entity;

import by.myioc.myioc.ioc.annotations.Component;
import by.myioc.myioc.ioc.annotations.Value;
import lombok.*;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Component
public class User {
    @Value(value = "User1")
    private String name;
    private Dog dog;
    private Cat cat;
}
