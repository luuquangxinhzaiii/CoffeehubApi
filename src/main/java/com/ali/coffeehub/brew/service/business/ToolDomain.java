package com.ali.coffeehub.brew.service.business;

import com.ali.coffeehub.brew.domain.ToolEntity;
import com.ali.coffeehub.brew.service.dto.ToolDTO;
import com.ali.coffeehub.service.business.AbstractEntity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
@Scope("Prototype")
public class ToolDomain extends AbstractEntity<BrewAggregateRoot, Long> implements Serializable {
    @NotNull
    private String name;

    @NotNull
    @Lob
    private String detail;

    public ToolDomain() {}

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

    public ToolDomain create(BrewAggregateRoot root, ToolDTO toolDTO){
        markAsCreating(null);
        setAggregateRoot(root);
        createOrUpdate(toolDTO);
        return this;
    }

    public void update(ToolDTO toolDTO){
        mustBeInitialized();
        createOrUpdate(toolDTO);
    }

    private void createOrUpdate(ToolDTO toolDTO){
        setName(toolDTO.getName());
        setDetail(toolDTO.getDetail());
    }

    public void onSaved(ToolEntity entity){
        if (!isCreating()) return;
        setId(entity.getId());
    }

    public ToolDomain reconstruct(BrewAggregateRoot root, ToolEntity entity){
        this.setId(entity.getId());
        this.setAggregateRoot(root);
        this.name = entity.getName();
        this.detail = entity.getDetail();
        maskAsInitialized();
        return this;
    }
}
