package com.hussey.levelupservice.dao;

import com.hussey.levelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LevelUpDaoJdbcImpl implements LevelUpDao {

    private final JdbcTemplate sql;

    @Autowired
    public LevelUpDaoJdbcImpl(JdbcTemplate sql) {
        this.sql = sql;
    }

    private final String INSERT_LVL_SQL =
            "insert into level_up (customer_id, points, member_date) values(?,?,?)";
    private final String FIND_LVL_SQL =
            "select * from level_up where level_up_id = ?";
    private final String UPDATE_LVL_SQL =
            "update level_up set points = ? where level_up_id = ?";
    private final String DELETE_LVL_SQL =
            "delete from level_up where level_up_id = ?";
    private final String FIND_ALL_SQL =
            "select * from level_up";
    private final String FIND_BY_CUSTOMER_ID_SQL =
            "select * from level_up where customer_id = ?";

    @Override
    @Transactional
    public LevelUp saveLevelUp(LevelUp level) {
        sql.update(INSERT_LVL_SQL, level.getCustomerId(), level.getPoints(), level.getMemberDate());
        Integer id = sql.queryForObject("select last_insert_id()", Integer.class);
        level.setLevelUpId(id);
        return level;
    }

    @Override
    public LevelUp findLevelUp(int id) {
        try {
            return sql.queryForObject(FIND_LVL_SQL, this::mapLvlToRow, id);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<LevelUp> findAll() {
        return sql.query(FIND_ALL_SQL, this::mapLvlToRow);
    }

    @Override
    public LevelUp findByCustomerId(int id) {
        try {
            return sql.queryForObject(FIND_BY_CUSTOMER_ID_SQL, this::mapLvlToRow, id);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLevelUp(LevelUp level) {
        sql.update(UPDATE_LVL_SQL, level.getPoints(), level.getLevelUpId());
    }

    @Override
    @Transactional
    public void deleteLevelUp(int id) {
        sql.update(DELETE_LVL_SQL, id);
    }

    private LevelUp mapLvlToRow(ResultSet set, int rowNumber) throws SQLException {
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
            columns.add(set.getMetaData().getColumnName(i));
        }
        columns = columns.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        List<Method> setters = Arrays.stream(LevelUp.class.getMethods()).filter(method -> method.getName().contains("set"))
                .sorted(Comparator.comparing(Method::getName)).collect(Collectors.toList());

        LevelUp result = new LevelUp();
        for (int i = 0; i < setters.size(); i++) {
            try {
                if (setters.get(i).getParameterTypes()[0].getSimpleName().equalsIgnoreCase("localdate")) {
                    setters.get(i).invoke(result, set.getDate(columns.get(i)).toLocalDate());
                } else {
                    setters.get(i).invoke(result, set.getObject(columns.get(i)));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
