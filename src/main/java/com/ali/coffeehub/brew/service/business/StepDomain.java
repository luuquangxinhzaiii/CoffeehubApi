package com.ali.coffeehub.brew.service.business;

import com.ali.coffeehub.brew.domain.StepEntity;
import com.ali.coffeehub.brew.service.dto.StepDTO;
import com.ali.coffeehub.service.business.AbstractEntity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
@Scope("Prototype")
public class StepDomain extends AbstractEntity<BrewAggregateRoot, Long> implements Serializable {
    @NotNull
    private String name;

    @NotNull
    @Lob
    private String detail;

    public StepDomain() {}

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDetail() {
        return detail;
    }

    public void setName(@NotNull String name) {
        if(Objects.equals(this.name, name)){
            this.name = name;
            modified();
        }
    }

    public void setDetail(@NotNull String detail) {
        if(Objects.equals(this.detail, detail)){
            this.detail = detail;
            modified();
        }
    }

    public StepDomain create(BrewAggregateRoot root, StepDTO stepDTO){
        markAsCreating(null);
        setAggregateRoot(root);
        createOrUpdate(stepDTO);
        return this;
    }

    public void update(StepDTO stepDTO){
        mustBeInitialized();
        createOrUpdate(stepDTO);
    }

    private void createOrUpdate(StepDTO stepDTO){
        setName(stepDTO.getName());
        setDetail(stepDTO.getDetail());
    }

    public void onSaved(StepEntity entity){
        if (!isCreating()) return;
        setId(entity.getId());
    }

    public StepDomain reconstruct(BrewAggregateRoot root, StepEntity entity){
        this.setId(entity.getId());
        this.setAggregateRoot(root);
        this.name = entity.getName();
        this.detail = entity.getDetail();
        maskAsInitialized();
        return this;
    }
}
