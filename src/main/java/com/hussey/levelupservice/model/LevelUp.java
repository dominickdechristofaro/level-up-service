package com.hussey.levelupservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUp implements Serializable {
    private Integer levelUpId;
    @NotNull(message = "customer id cannot be null")
    private Integer customerId;
    @NotNull(message = "points cannot be null")
    private Integer points;
    @JsonDeserialize(as = LocalDateDeserializer.class)
    @JsonSerialize(as = LocalDateSerializer.class)
    private LocalDate memberDate;

    public LevelUp() {
    }

    public LevelUp(@NotNull(message = "customer id cannot be null") Integer customerId, @NotNull(message = "points cannot be null") Integer points, LocalDate memberDate) {
        this.customerId = customerId;
        this.points = points;
        this.memberDate = memberDate;
    }

    public Integer getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(Integer levelUpId) {
        this.levelUpId = levelUpId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelUp levelUp = (LevelUp) o;
        return Objects.equals(levelUpId, levelUp.levelUpId) &&
                customerId.equals(levelUp.customerId) &&
                points.equals(levelUp.points) &&
                memberDate.equals(levelUp.memberDate);
    }

}
