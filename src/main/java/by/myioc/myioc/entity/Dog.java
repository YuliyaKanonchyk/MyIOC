package by.myioc.myioc.entity;

import by.myioc.myioc.ioc.annotations.Component;
import by.myioc.myioc.ioc.annotations.Value;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
//@NoArgsConstructor
@Component
public class Dog {
    @Value(value = "Dog1")
    private String name;

}
