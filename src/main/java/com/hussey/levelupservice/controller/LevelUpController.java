package com.hussey.levelupservice.controller;

import com.hussey.levelupservice.dao.LevelUpDao;
import com.hussey.levelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/levelup")
public class LevelUpController {

    @Autowired
    private LevelUpDao levelDao;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> getAll() {
        return levelDao.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp saveLevelUp(@RequestBody @Valid LevelUp level) {
        return levelDao.saveLevelUp(level);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateLevelUp(@RequestBody @Valid LevelUp level) {
        levelDao.updateLevelUp(level);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp findLevelUpById(@PathVariable int id) {
        return Optional.ofNullable(levelDao.findLevelUp(id))
                .orElseThrow(() -> new NoSuchElementException("No Element found for that id"));
    }

    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp findLevelUpByCustomerId(@PathVariable int id) {
        return Optional.ofNullable(levelDao.findByCustomerId(id))
                .orElseThrow(()-> new NoSuchElementException("Could not find that customer in the system"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteLevelUpById(@PathVariable int id) {
        levelDao.deleteLevelUp(id);
    }
}
