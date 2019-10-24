package com.hussey.levelupservice.dao;

import com.hussey.levelupservice.model.LevelUp;

import java.util.List;

public interface LevelUpDao {
    LevelUp saveLevelUp(LevelUp level);
    LevelUp findLevelUp(int id);
    List<LevelUp> findAll();
    LevelUp findByCustomerId(int id);
    void updateLevelUp(LevelUp level);
    void deleteLevelUp(int id);
}
