package hello.hyeoni.springproject.account;

import com.querydsl.core.types.Predicate;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.zone.Zone;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        return account.zones.any().in(zones).or(account.tags.any().in(tags));
    }
}
