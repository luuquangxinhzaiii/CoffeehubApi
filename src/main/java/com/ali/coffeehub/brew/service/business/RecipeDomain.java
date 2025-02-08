package com.ali.coffeehub.brew.service.business;

import com.ali.coffeehub.brew.domain.RecipeEntity;
import com.ali.coffeehub.brew.service.dto.RecipeDTO;
import com.ali.coffeehub.service.business.AbstractEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
@Scope("Prototype")
public class RecipeDomain extends AbstractEntity<BrewAggregateRoot, Long> implements Serializable {
    @NotNull
    private String name;

    @NotNull
    private String detail;

    public RecipeDomain(){}

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDetail() {
        return detail;
    }

    public RecipeDomain create(BrewAggregateRoot root, RecipeDTO recipeDTO) {
        markAsCreating(null);
        setAggregateRoot(root);
        createOrUpdate(recipeDTO);
        return this;
    }

    public void update(RecipeDTO recipeDTO) {
        mustBeInitialized();
        createOrUpdate(recipeDTO);
    }

    private void createOrUpdate(RecipeDTO recipeDTO) {
        setName(recipeDTO.getName());
        setDetail(recipeDTO.getDetail());
    }

    public void setName(@NotNull String name) {
        if (!Objects.equals(this.name, name)) {
            this.name = name;
            modified();
        }
    }

    public void setDetail(@NotNull String detail) {
        if(!Objects.equals(this.detail, detail)){
            this.detail = detail;
            modified();
        }
    }

    public void onSaved(RecipeEntity entity) {
        if (!isCreating()) return;
        setId(entity.getId());
    }

    public RecipeDomain reconstruct(BrewAggregateRoot root, RecipeEntity entity) {
        this.setId(entity.getId());
        this.setAggregateRoot(root);
        this.name = entity.getName();
        this.detail = entity.getDetail();
        maskAsInitialized();
        return this;
    }
}
