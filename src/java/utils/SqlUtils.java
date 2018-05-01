package utils;

import annotations.Column;
import annotations.SqlAnnotation;
import annotations.Table;
import javafx.scene.control.Tab;
import net.sf.json.JSONObject;
import netscape.javascript.JSObject;
import users.UserBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SqlUtils {


    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "yexie123";


    public static Connection getConnection(String url, String drivername, String username, String password) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName(drivername).newInstance();
        System.out.println(drivername);
        Connection con = null;
        con = (Connection) DriverManager.getConnection(url, username, password);
        System.out.println(con);
        return con;
    }

    public String insert(Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        Class mClass = object.getClass();
        boolean isExist = mClass.isAnnotationPresent(Table.class);
        if (!isExist) {
            return null;
        }
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        Table table = (Table) mClass.getAnnotation(Table.class);
        String tableName = table.value();
        stringBuilder.append("INSERT INTO ").append(tableName);
        Field[] fields = mClass.getDeclaredFields();
        for (Field f : fields) {
            boolean isFExist = f.isAnnotationPresent(Column.class);
            if (!isFExist) {
                continue;
            }
            Column column = f.getAnnotation(Column.class);
            String columnName = column.value();
            Object filedValue = null;
            try {

                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), mClass);
                Method method = pd.getReadMethod();
                filedValue = method.invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.add((String) filedValue);
            columnNames.add(columnName);
        }
        stringBuilder.append("(");
        for (int i = 0; i < columnNames.size(); i++) {
            stringBuilder.append(columnNames.get(i)).append(",");

        }
        stringBuilder.append(")");
        stringBuilder.append("Values");
        for (int i = 0; i < columnNames.size(); i++) {
            stringBuilder.append(values.get(i)).append(",");

        }
        stringBuilder.append(")");
        try {

            Connection connection=getConnection(DB_URL, JDBC_DRIVER, USER, PASS);
            Statement statement=connection.createStatement();
            statement.executeUpdate(stringBuilder.toString());
            statement.close();
            connection.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static JSONObject query(Object object) {
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        Class mClass = object.getClass();
        boolean isExist = mClass.isAnnotationPresent(Table.class);
        if (!isExist)
            return null;
        Table table = (Table) mClass.getAnnotation(Table.class);
        String tabName = table.value();
        //拼装SQL表名
        sb.append("select * from ").append(tabName).append(" where 1=1");

        //获取类的所有字段
        Field[] fields = mClass.getDeclaredFields();
        ArrayList<String> columnNames = new ArrayList<>();
        for (Field f : fields) {
            //下面代码获取字段注解
            boolean isFExist = f.isAnnotationPresent(Column.class);
            if (!isFExist) {
                continue;
            }
            Column column = f.getAnnotation(Column.class);
            String columnName = column.value();

            //下面代码获取字段值
            Object filedValue = null;
            try {
                //PropertyDescriptor 类表示JavaBean类通过存储器导出一个属性
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), mClass);
                //getReadMethod() 获得用于读取属性值的方法,即getter方法
                Method method = pd.getReadMethod();
                //通过反射调用getter方法
                filedValue = method.invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            columnNames.add(columnName);
            //   System.out.println(columnName);
            //拼装SQL条件
            if (filedValue == null || (filedValue instanceof Integer && (Integer) filedValue == 0)) {
                continue;
            }
            sb.append(" and ").append(columnName);
            if (filedValue instanceof String) {
                //包含多段
                if (((String) filedValue).contains(",")) {
                    String[] arr = ((String) filedValue).split(",");
                    sb.append(" in(");
                    for (String str : arr) {
                        sb.append("'").append(str).append("'").append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1).append(")");
                } else {
                    sb.append("=").append("'").append(filedValue).append("'");
                }
            } else if (filedValue instanceof Boolean) {
                sb.append("=").append("'").append(filedValue).append("'");
            } else if (filedValue instanceof Integer) {
                sb.append("=").append(filedValue);
            }
        }//end for


        System.out.println(sb.toString());
        try {
            Connection connection = getConnection(DB_URL, JDBC_DRIVER, USER, PASS);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            while (rs.next()) {
                for (int i = 0; i < columnNames.size(); i++) {
                    jsonObject.put(columnNames.get(i), rs.getString(columnNames.get(i)));
                }
            }
            rs.close();
            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static void main(String[] args) {

        UserBean stu1 = new UserBean();
        stu1.setPassword("124");
        // UserBean stu2 = new UserBean();
        //stu2.setUserName("郭峰");//查询学生名为郭峰的学生
        //  UserBean stu3 = new UserBean();
        // stu3.setNickName("青岛市,烟台市");//查询地址为任意其中一个的学生

        JSONObject jsonObject = new JSONObject();
        jsonObject = query(stu1);
        //String sql2 = query(stu
        //String sql3 = query(stu3);

        System.out.println(jsonObject);
        // System.out.println(sql2);
        //  System.out.println(sql3);

    }
}

