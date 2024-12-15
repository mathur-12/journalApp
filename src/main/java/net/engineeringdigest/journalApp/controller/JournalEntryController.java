package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry> ltEntries=user.getJournalEntries();
        if(ltEntries!=null && ltEntries.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ltEntries,HttpStatus.OK);
    }

//    @GetMapping("id/{id}")
//    public ResponseEntity<JournalEntry> getJournal(@PathVariable ObjectId id){
//        Optional<JournalEntry> entry = journalEntryService.getEntry(id));
//        if(entry.isPresent()){
//            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
//        }
//        else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournal(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry>collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> entry = journalEntryService.findById(id);
            if(entry.isPresent()){
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournal(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        boolean removed=journalEntryService.deleteEntry(id, userName);
        if(removed){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> postJournal(@RequestBody JournalEntry journalEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName= authentication.getName();
            journalEntryService.saveNewEntry(journalEntry,userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> putJournalById(@PathVariable ObjectId id,
                                         @RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry>collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry>journalEntry=journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry oldEntry=journalEntry.get();
                oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle():oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("")?newEntry.getContent():oldEntry.getContent());
                oldEntry.setSentiment(newEntry.getSentiment()!=null && !newEntry.getSentiment().equals("")?newEntry.getSentiment():oldEntry.getSentiment());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}