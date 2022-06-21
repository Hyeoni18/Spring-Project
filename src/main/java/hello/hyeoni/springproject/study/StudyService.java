package hello.hyeoni.springproject.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class StudyService {

    @Autowired
    List<StudyRepository> studyRepositories;

    public void printStudyRepository() throws ClassNotFoundException {
        //리플렉션 사용방법
        Class c = Class.forName("Board");
        Method[] m = c.getMethods();
        Field[] f = c.getFields();
        Constructor[] cs = c.getConstructors();
        Class[] inter = c.getInterfaces();
        Class superClass = c.getSuperclass();
        

        this.studyRepositories.forEach(System.out::println);
    }
}
