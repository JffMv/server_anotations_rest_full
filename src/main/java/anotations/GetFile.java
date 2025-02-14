package anotations;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetFile {
    public String value(); // value para que cuando use GetMapping("") pueda pasarle el paramero sin tener que decir que es ese paramentor
}