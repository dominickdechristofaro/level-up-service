package com.hussey.levelupservice.dao;

import com.hussey.levelupservice.model.LevelUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LevelUpDaoJdbcImplTest {

    @Autowired
    private LevelUpDao dao;

    private static LevelUp testLevel, testLevel2;

    static {
      testLevel = new LevelUp(1, 10, LocalDate.now());
      testLevel2 = new LevelUp(2, 30, LocalDate.now());
    }

    @BeforeEach
    void setUp() {
        dao.findAll().forEach(lvl -> dao.deleteLevelUp(lvl.getLevelUpId()));
    }

    @Test
    void test_saveLevelUp_WillReturnObjectWithAnId() {
        testLevel = dao.saveLevelUp(testLevel);
        assertNotNull(testLevel.getLevelUpId());
    }

    @Test
    void test_findLevelUp_WillReturnTheCorrectObject() {
        testLevel = dao.saveLevelUp(testLevel);
        LevelUp fromDb = dao.findLevelUp(testLevel.getLevelUpId());
        assertEquals(testLevel, fromDb);
    }

    @Test
    void test_findAll_WillReturnAllObjectsInTheDb() {
        dao.saveLevelUp(testLevel);
        dao.saveLevelUp(testLevel2);
        assertEquals(2, dao.findAll().size());
    }

    @Test
    void test_updateLevelUp_WillUpdateEntryInTheDb() {
        testLevel = dao.saveLevelUp(testLevel);
        testLevel.setPoints(50);
        dao.updateLevelUp(testLevel);
        LevelUp test = dao.findLevelUp(testLevel.getLevelUpId());
        assertEquals(testLevel, test);
    }

    @Test
    void test_deleteLevelUp_WillRemoveEntryFromTheDb() {
        testLevel = dao.saveLevelUp(testLevel);
        dao.deleteLevelUp(testLevel.getLevelUpId());
        assertNull(dao.findLevelUp(testLevel.getLevelUpId()));
    }

}