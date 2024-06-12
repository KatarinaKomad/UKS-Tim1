package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Item;
import uns.ac.rs.uks.repository.ItemRepository;

import java.util.UUID;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Item getById(UUID itemId) throws NotFoundException {
        return itemRepository.findById(itemId).orElseThrow(()->new NotFoundException("User not found."));
    }
}
