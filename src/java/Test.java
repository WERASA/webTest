import utils.AnnotationParse;
import utils.SqlUtils;

import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) {

        try {
            AnnotationParse annotationParse=new AnnotationParse();
            annotationParse.parseMethod(SqlUtils.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

