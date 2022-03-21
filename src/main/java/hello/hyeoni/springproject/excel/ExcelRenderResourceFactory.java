package hello.hyeoni.springproject.excel;

import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.util.*;

//필요한 리소스를 렌더링 하는 클래스인가봐
public final class ExcelRenderResourceFactory {

    public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb) {
        Map<String, String> headerNamesMap = new LinkedHashMap<>();
        List<String> fieldNames = new ArrayList<>();

        for(Field field : getAllFields(type)) {
            if(field.isAnnotationPresent(ExcelHeader.class)) {
                ExcelHeader annotation = field.getAnnotation(ExcelHeader.class);
                fieldNames.add(field.getName());
                headerNamesMap.put(field.getName(), annotation.headerName());
            }
        }
        return new ExcelRenderResource(headerNamesMap, fieldNames);
    }

    static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for(Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields;
    }

     static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
        List<Class<?>> classes = new ArrayList<>();
        while(clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if(fromSuper) {
            Collections.reverse(classes);
        }
        return classes;
    }
}
