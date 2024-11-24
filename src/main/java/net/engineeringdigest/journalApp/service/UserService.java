package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    private static final Logger logger= LoggerFactory.getLogger(JournalEntryService.class);
    public boolean saveNewEntry(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }   catch (Exception e){
            logger.info("hahahahahahahahaha");
            logger.debug("hahahahahahahahaha");
            return false;
        }
    }
    public void saveEntry(User user){
        try{
            userRepository.save(user);
        }   catch (Exception e){
            log.error("Exception ",e);
        }
    }
    public List<User> getEntries(){
        return userRepository.findAll();
    }
    public Optional<User> getEntry(ObjectId objectId){
         return userRepository.findById(objectId);
    }
    public void deleteEntry(ObjectId objectId){
        userRepository.deleteById(objectId);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void saveAdmin(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER","ADMIN"));
            userRepository.save(user);
        }   catch (Exception e){
            log.error("Exception ",e);
        }
    }
}
