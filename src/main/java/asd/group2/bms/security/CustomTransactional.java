package asd.group2.bms.security;

import javax.transaction.Transactional;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackOn = Exception.class)
@Documented
public @interface CustomTransactional {

}