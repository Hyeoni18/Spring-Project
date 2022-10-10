package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.notification.TravelCreatedEvent;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.travel.form.TravelDescriptionForm;
import hello.hyeoni.springproject.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hello.hyeoni.springproject.travel.form.TravelForm.VALID_PATH_PATTERN;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

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
        if(travel.isRemovable()) {
            travelRepository.delete(travel);
        } else {
            throw new IllegalArgumentException("모집을 삭제할 수 없습니다.");
        }
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

    public Travel getTravelToUpdateStatus(Account account, String path) {
        Travel travel = travelRepository.findTravelWithManagersByPath(path);
        checkIfExistingTravel(path, travel);
        checkIfManager(account, travel);
        return travel;
    }

    public void publish(Travel travel) {
        travel.publish();
        this.eventPublisher.publishEvent(new TravelCreatedEvent(travel));
    }

    public void close(Travel travel) {
        travel.close();
    }

    public boolean isValidPath(String newPath) {
        if(!newPath.matches(VALID_PATH_PATTERN)) {
           return false;
        }
        return !travelRepository.existsByPath(newPath);
    }

    public void updateTravelPath(Travel travel, String newPath) {
        travel.setPath(newPath);
    }


    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void updateTravelTitle(Travel travel, String newTitle) {
        travel.setTitle(newTitle);
    }

    public void startRecruit(Travel travel) {
        travel.startRecruit();
    }

    public void stopRecruit(Travel travel) {
        travel.stopRecruit();
    }

    public void addMember(Travel travel, Account account) {
        travel.addMember(account);
    }

    public void removeMember(Travel travel, Account account) {
        travel.removeMember(account);
    }
}
