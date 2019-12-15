package by.myioc.myioc.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Target – Эта аннотация позволит вам указать те java элементы, к которой аннотация должна быть применена.
 * Аннотацией @Target указывается, какой элемент программы будет использоваться аннотацией.
 * Так, в примере эта аннотация имеет тип ElementType.TYPE, что означает что она может быть объявлена перед классом,
 * интерфейсом или enum. Объявление @Target в любых других местах программы будет воспринято компилятором как ошибка.
 * PACKAGE - назначением является целый пакет (package);
 * TYPE - класс, интерфейс, enum или другая аннотация:
 * METHOD - метод класса, но не конструктор (для конструкторов есть отдельный тип CONSTRUCTOR);
 * PARAMETER - параметр метода;
 * CONSTRUCTOR - конструктор;
 * FIELD - поля-свойства класса;
 * LOCAL_VARIABLE - локальная переменная (обратите внимание, что аннотация не может быть прочитана
 * во время выполнения программы, то есть, данный тип аннотации может использоваться только на уровне
 * компиляции как, например, аннотация @SuppressWarnings);
 * ANNOTATION_TYPE - другая аннотация.
 **/

/**
 * @Retention – Эта аннотация позволит вам указать, когда аннотация будет доступна. Возможные значения:
 * CLASS, RUNTIME и SOURCE.
 * SOURCE - аннотация доступна только в исходном коде и сбрасывается во время создания .class файла;
 * CLASS - аннотация хранится в .class файле, но недоступна во время выполнения программы;
 * RUNTIME - аннотация хранится в .class файле и доступна во время выполнения программы.
 **/

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String name() default "";
}
