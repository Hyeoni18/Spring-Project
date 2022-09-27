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
}
