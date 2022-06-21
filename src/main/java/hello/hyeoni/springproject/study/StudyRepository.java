package hello.hyeoni.springproject.study;

import org.springframework.stereotype.Repository;

public interface StudyRepository {

    @Repository
    public class MyStudyRepository implements StudyRepository {

    }

    @Repository
    public class HelloStudyRepository implements StudyRepository {

    }
}
