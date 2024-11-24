package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    Logger logger=LoggerFactory.getLogger(JournalEntryService.class);
    @Transactional
    public void saveNewEntry(JournalEntry journalEntry, String userName){
        try{
            User user=userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }   catch (Exception e){
                throw new RuntimeException("An error occurred while saving the entry.",e);
        }
    }
    public void saveEntry(JournalEntry journalEntry) {
            journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getEntries(){
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId objectId){
         return journalEntryRepository.findById(objectId);
    }
//    public Optional<JournalEntry> findByUserName(String userName){
//        return userService.findByUserName(userName);
//    }
    @Transactional
    public boolean deleteEntry(ObjectId objectId, String userName){
        boolean removed=false;
        try {
            User user=userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(objectId));
            if(removed) {
                userService.saveEntry(user);
                journalEntryRepository.deleteById(objectId);
            }
        }
        catch(Exception e){
            logger.error("Error",e);
            throw new RuntimeException("An error occured while deleting the entry.",e);
        }
        return removed;
    }
}
