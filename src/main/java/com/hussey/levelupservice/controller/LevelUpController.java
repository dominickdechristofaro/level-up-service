package com.hussey.levelupservice.controller;

import com.hussey.levelupservice.dao.LevelUpDao;
import com.hussey.levelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/levelup")
public class LevelUpController {
    @Autowired
    private LevelUpDao levelDao;

    @GetMapping
    public List<LevelUp> getAll() {
        return levelDao.findAll();
    }

    @PostMapping
    public LevelUp saveLevelUp(@RequestBody @Valid LevelUp level) {
        return null;
    }

    @PutMapping
    public void updateLevelUp(@RequestBody @Valid LevelUp level) {

    }

    @GetMapping("/{id}")
    public LevelUp findLevelUpById(int id) {
        return null;
    }

    @DeleteMapping("{id}")
    public void deleteLevelUpById(int id) {}
}
