package annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface SqlAnnotation {
    public String url();
    public String drivername();
    public String username();
    public String password();


}
