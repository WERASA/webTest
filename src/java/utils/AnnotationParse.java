package utils;

import annotations.Column;
import annotations.SqlAnnotation;
import annotations.Table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class AnnotationParse {

    public static void parseMethod(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object object=clazz.newInstance();
        Method[] methods=clazz.getDeclaredMethods();
        for(Method m:methods){
            SqlAnnotation db=m.getAnnotation(SqlAnnotation.class);
            String url="";
            String drivername="";
            String username="";
            String password="";
            if(db!=null){
                url=db.url();
                drivername=db.drivername();
                username=db.username();
                password=db.password();
                m.invoke(object, url,drivername,username,password);
            }

    }
}




}
