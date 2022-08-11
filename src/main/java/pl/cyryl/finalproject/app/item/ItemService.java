package pl.cyryl.finalproject.app.item;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findItemsFromUser(long userId);
    List<Item> findVisibleItemsFromUser(long userId);
    void save(Item item);
    Optional<Item> findItem(long id);
    Optional<Item> findPublicItem(long id);
}