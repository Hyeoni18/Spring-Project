package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.travel.form.TravelDescriptionForm;
import hello.hyeoni.springproject.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ModelMapper modelMapper;

    public Travel createNewTravel(Travel travel, Account account) {
        Travel newTravel = travelRepository.save(travel);
        newTravel.addManager(account);
        return newTravel;
    }

    public Travel getTravel(String path) {
        Travel travel = travelRepository.findByPath(path);
        checkIfExistingTravel(path, travel);
        return travel;
    }

    private void checkIfExistingTravel(String path, Travel travel) {
        if (travel == null) {
            throw new IllegalArgumentException(path+"에 해당하는 모집이 없습니다.");
        }
    }

    public void remove(Travel travel) {
        travelRepository.delete(travel);
    }

    public Travel getTravelToUpdate(Account account, String path) {
        Travel travel = this.getTravel(path);
        if(!travel.isManagedBy(account)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
        return travel;
    }

    public void updateTravelDescription(Travel travel, TravelDescriptionForm travelDescriptionForm) {
        modelMapper.map(travelDescriptionForm, travel);
    }

    public Travel getTravelToUpdateTags(Account account, String path) {
        Travel travel = travelRepository.findTravelWithTagsByPath(path);
        checkIfExistingTravel(path, travel);
        checkIfManager(account, travel);
        return travel;
    }

    private void checkIfManager(Account account, Travel travel) {
        if(!travel.isManagedBy(account)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    public void addTag(Travel travel, Tag tag) {
        travel.getTags().add(tag);
    }

    public void removeTag(Travel travel, Tag tag) {
        travel.getTags().remove(tag);
    }

    public Travel getTravelToUpdateZones(Account account, String path) {
        Travel travel = travelRepository.findTravelWithZonesByPath(path);
        checkIfExistingTravel(path, travel);
        checkIfManager(account, travel);
        return travel;
    }

    public void addZone(Travel travel, Zone zone) {
        travel.getZones().add(zone);
    }

    public void removeZone(Travel travel, Zone zone) {
        travel.getZones().remove(zone);
    }
}
