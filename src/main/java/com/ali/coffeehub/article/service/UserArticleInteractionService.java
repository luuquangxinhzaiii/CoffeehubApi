package com.ali.coffeehub.article.service;

import com.ali.coffeehub.article.domain.UserArticleInteractionEntity;
import com.ali.coffeehub.article.repository.UserArticleInteractionRepository;
import com.ali.coffeehub.article.service.dto.UserArticleInteractionDTO;
import com.ali.coffeehub.article.service.mapper.UserArticleInteractionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.article.domain.UserArticleInteractionEntity}.
 */
@Service
@Transactional
public class UserArticleInteractionService {

    private static final Logger LOG = LoggerFactory.getLogger(UserArticleInteractionService.class);

    private final UserArticleInteractionRepository userArticleInteractionRepository;

    private final UserArticleInteractionMapper userArticleInteractionMapper;

    public UserArticleInteractionService(
        UserArticleInteractionRepository userArticleInteractionRepository,
        UserArticleInteractionMapper userArticleInteractionMapper
    ) {
        this.userArticleInteractionRepository = userArticleInteractionRepository;
        this.userArticleInteractionMapper = userArticleInteractionMapper;
    }

    /**
     * Save a userArticleInteraction.
     *
     * @param userArticleInteractionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserArticleInteractionDTO save(UserArticleInteractionDTO userArticleInteractionDTO) {
        LOG.debug("Request to save UserArticleInteraction : {}", userArticleInteractionDTO);
        UserArticleInteractionEntity userArticleInteractionEntity = userArticleInteractionMapper.toEntity(userArticleInteractionDTO);
        userArticleInteractionEntity = userArticleInteractionRepository.save(userArticleInteractionEntity);
        return userArticleInteractionMapper.toDto(userArticleInteractionEntity);
    }

    /**
     * Update a userArticleInteraction.
     *
     * @param userArticleInteractionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserArticleInteractionDTO update(UserArticleInteractionDTO userArticleInteractionDTO) {
        LOG.debug("Request to update UserArticleInteraction : {}", userArticleInteractionDTO);
        UserArticleInteractionEntity userArticleInteractionEntity = userArticleInteractionMapper.toEntity(userArticleInteractionDTO);
        userArticleInteractionEntity = userArticleInteractionRepository.save(userArticleInteractionEntity);
        return userArticleInteractionMapper.toDto(userArticleInteractionEntity);
    }

    /**
     * Partially update a userArticleInteraction.
     *
     * @param userArticleInteractionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserArticleInteractionDTO> partialUpdate(UserArticleInteractionDTO userArticleInteractionDTO) {
        LOG.debug("Request to partially update UserArticleInteraction : {}", userArticleInteractionDTO);

        return userArticleInteractionRepository
            .findById(userArticleInteractionDTO.getId())
            .map(existingUserArticleInteraction -> {
                userArticleInteractionMapper.partialUpdate(existingUserArticleInteraction, userArticleInteractionDTO);

                return existingUserArticleInteraction;
            })
            .map(userArticleInteractionRepository::save)
            .map(userArticleInteractionMapper::toDto);
    }

    /**
     * Get all the userArticleInteractions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserArticleInteractionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserArticleInteractions");
        return userArticleInteractionRepository.findAll(pageable).map(userArticleInteractionMapper::toDto);
    }

    /**
     * Get one userArticleInteraction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserArticleInteractionDTO> findOne(Long id) {
        LOG.debug("Request to get UserArticleInteraction : {}", id);
        return userArticleInteractionRepository.findById(id).map(userArticleInteractionMapper::toDto);
    }

    /**
     * Delete the userArticleInteraction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserArticleInteraction : {}", id);
        userArticleInteractionRepository.deleteById(id);
    }
}
