package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    public Travel createNewTravel(Travel travel, Account account) {
        Travel newTravel = travelRepository.save(travel);
        newTravel.addManager(account);
        return newTravel;
    }

    public Travel getTravel(String path) {
        Travel travel = travelRepository.findByPath(path);
        checkIfExistingStudy(path, travel);
        return travel;
    }

    private void checkIfExistingStudy(String path, Travel travel) {
        if (travel == null) {
            throw new IllegalArgumentException(path+"에 해당하는 모집이 없습니다.");
        }
    }
}
