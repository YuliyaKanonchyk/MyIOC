package by.myioc.myioc.entity;

import by.myioc.myioc.ioc.annotations.Component;
import by.myioc.myioc.ioc.annotations.Value;
import lombok.*;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Component
// TODO: 19.11.2019 Решить проблему с сеттером стринг
public class User {
    @Value(value = "User1")
    private String name;
    private Dog dog;
    private Cat cat;
}
