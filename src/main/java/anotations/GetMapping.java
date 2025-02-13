package anotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    public String value(); // value para que cuando use GetMapping("") pueda pasarle el paramero sin tener que decir que es ese paramentor

}
