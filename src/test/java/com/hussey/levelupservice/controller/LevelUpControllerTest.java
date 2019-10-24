package com.hussey.levelupservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hussey.levelupservice.dao.LevelUpDao;
import com.hussey.levelupservice.model.LevelUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class LevelUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LevelUpDao levelDao;

    private static ObjectMapper mapper = new ObjectMapper();
    private static LevelUp inputLvl;
    private static LevelUp outputLvl, outputLvl2;

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        inputLvl = new LevelUp(1, 10, LocalDate.now());
        outputLvl = new LevelUp(1, 10, LocalDate.now());
        outputLvl2 = new LevelUp(2, 40, LocalDate.now().minusDays(10));

        outputLvl.setLevelUpId(1);
        outputLvl2.setLevelUpId(2);
    }

    private <T> String writeToJson(T obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    @Test
    public void test_getAll_WillReturnListOfLevelUpObjects_AndStatusIs_200() throws Exception {
        List<LevelUp> all = new ArrayList<>();
        all.add(outputLvl);
        all.add(outputLvl2);

        when(levelDao.findAll()).thenReturn(all);

        mockMvc.perform(get("/levelup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writeToJson(all)));
    }

    @Test
    public void test_saveLevelUp_WillReturnObjectWithAnId_AndStatusIs_201() throws Exception {
        when(levelDao.saveLevelUp(inputLvl)).thenReturn(outputLvl);

        mockMvc.perform(post("/levelup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(writeToJson(inputLvl)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(writeToJson(outputLvl)));

    }

    @Test
    public void test_updateLevelUp_WillCallUpdateMethod_AndStatusIs_202() throws Exception {

        mockMvc.perform(put("/levelup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(writeToJson(outputLvl)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));
    }

    @Test
    public void test_FindLevelUpByCustomerId_WillReturnCorrectObject_AndStatusIs_200() throws Exception {
        when(levelDao.findByCustomerId(2)).thenReturn(outputLvl2);

        mockMvc.perform(get("/levelup/customer/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writeToJson(outputLvl2)));
    }

    @Test
    public void test_findLevelUpById_WillReturnObject_AndStatusIs_200() throws Exception {

        when(levelDao.findLevelUp(2)).thenReturn(outputLvl2);
        mockMvc.perform(get("/levelup/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(writeToJson(outputLvl2)));
    }

    @Test
    public void test_deleteLevelUpById_WillBeVoid_AndStatusIs_202() throws Exception {

        mockMvc.perform(delete("/levelup/10"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));

    }

    @Test
    public void test_saveLevelUp_WillFailInInputsAreMissing_AndStatusIs_422() throws Exception {

        LevelUp failLvl = new LevelUp();

        mockMvc.perform(post("/levelup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(writeToJson(failLvl)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void test_updateLevelUp_WillFailIfInputsAreMissing_AndStatusIs_422() throws Exception {
        LevelUp failUpdate = new LevelUp();

        mockMvc.perform(post("/levelup")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(writeToJson(failUpdate)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void test_FindLevelUpById_WillFailIfIdIsString_AndStatusIs_406() throws Exception {

        mockMvc.perform(get("/levelup/true=true"))
                .andDo(print())
                .andExpect(status().isNotAcceptable());

    }

    @Test
    public void test_FindLevelUpById_WillThrowErrorIfNotFound_AndStatusIs_404() throws Exception {

        mockMvc.perform(get("/levelup/10"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

}