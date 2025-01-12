package com.ali.coffeehub.service;

import com.ali.coffeehub.domain.TagsEntity;
import com.ali.coffeehub.repository.TagsRepository;
import com.ali.coffeehub.service.dto.TagsDTO;
import com.ali.coffeehub.service.mapper.TagsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.domain.TagsEntity}.
 */
@Service
@Transactional
public class TagsService {

    private static final Logger LOG = LoggerFactory.getLogger(TagsService.class);

    private final TagsRepository tagsRepository;

    private final TagsMapper tagsMapper;

    public TagsService(TagsRepository tagsRepository, TagsMapper tagsMapper) {
        this.tagsRepository = tagsRepository;
        this.tagsMapper = tagsMapper;
    }

    /**
     * Save a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO save(TagsDTO tagsDTO) {
        LOG.debug("Request to save Tags : {}", tagsDTO);
        TagsEntity tagsEntity = tagsMapper.toEntity(tagsDTO);
        tagsEntity = tagsRepository.save(tagsEntity);
        return tagsMapper.toDto(tagsEntity);
    }

    /**
     * Update a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO update(TagsDTO tagsDTO) {
        LOG.debug("Request to update Tags : {}", tagsDTO);
        TagsEntity tagsEntity = tagsMapper.toEntity(tagsDTO);
        tagsEntity = tagsRepository.save(tagsEntity);
        return tagsMapper.toDto(tagsEntity);
    }

    /**
     * Partially update a tags.
     *
     * @param tagsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TagsDTO> partialUpdate(TagsDTO tagsDTO) {
        LOG.debug("Request to partially update Tags : {}", tagsDTO);

        return tagsRepository
            .findById(tagsDTO.getId())
            .map(existingTags -> {
                tagsMapper.partialUpdate(existingTags, tagsDTO);

                return existingTags;
            })
            .map(tagsRepository::save)
            .map(tagsMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TagsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tags");
        return tagsRepository.findAll(pageable).map(tagsMapper::toDto);
    }

    /**
     * Get one tags by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TagsDTO> findOne(Long id) {
        LOG.debug("Request to get Tags : {}", id);
        return tagsRepository.findById(id).map(tagsMapper::toDto);
    }

    /**
     * Delete the tags by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Tags : {}", id);
        tagsRepository.deleteById(id);
    }
}
