package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
//    @GetMapping
//    public ResponseEntity<?> getUsers(){
//        List<User>users=userService.getEntries();
//        if(users!=null && users.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(users,HttpStatus.OK);
//    }

    @GetMapping("id/{id}")
    public ResponseEntity<User> getJournal(@PathVariable ObjectId id){
        Optional<User> user = userService.getEntry(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @DeleteMapping("id/{id}")
//    public ResponseEntity<?> deleteJournal(@PathVariable ObjectId id){
//        userService.deleteEntry(id);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }
    @DeleteMapping
    public ResponseEntity<?> deleteJournal(){
//        userService.deleteEntry(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        userRepository.deleteByUserName(userName);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> putJournal(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User oldEntry=userService.findByUserName(userName);
        oldEntry.setUserName(user.getUserName());
        oldEntry.setPassword(user.getPassword());
        userService.saveNewEntry(oldEntry);
        return new ResponseEntity<>(oldEntry,HttpStatus.OK);
//        if(oldEntry!=null){
//            oldEntry.setUserName(user.getUserName());
//            oldEntry.setPassword(user.getPassword());
//            userService.saveEntry(oldEntry);
//            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}