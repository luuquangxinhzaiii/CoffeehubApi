package com.ali.coffeehub.cafeteria.service;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity;
import com.ali.coffeehub.cafeteria.repository.CoffeeShopReactionRepository;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopReactionDTO;
import com.ali.coffeehub.cafeteria.service.mapper.CoffeeShopReactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity}.
 */
@Service
@Transactional
public class CoffeeShopReactionService {

    private static final Logger LOG = LoggerFactory.getLogger(CoffeeShopReactionService.class);

    private final CoffeeShopReactionRepository coffeeShopReactionRepository;

    private final CoffeeShopReactionMapper coffeeShopReactionMapper;

    public CoffeeShopReactionService(
        CoffeeShopReactionRepository coffeeShopReactionRepository,
        CoffeeShopReactionMapper coffeeShopReactionMapper
    ) {
        this.coffeeShopReactionRepository = coffeeShopReactionRepository;
        this.coffeeShopReactionMapper = coffeeShopReactionMapper;
    }

    /**
     * Save a coffeeShopReaction.
     *
     * @param coffeeShopReactionDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopReactionDTO save(CoffeeShopReactionDTO coffeeShopReactionDTO) {
        LOG.debug("Request to save CoffeeShopReaction : {}", coffeeShopReactionDTO);
        CoffeeShopReactionEntity coffeeShopReactionEntity = coffeeShopReactionMapper.toEntity(coffeeShopReactionDTO);
        coffeeShopReactionEntity = coffeeShopReactionRepository.save(coffeeShopReactionEntity);
        return coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);
    }

    /**
     * Update a coffeeShopReaction.
     *
     * @param coffeeShopReactionDTO the entity to save.
     * @return the persisted entity.
     */
    public CoffeeShopReactionDTO update(CoffeeShopReactionDTO coffeeShopReactionDTO) {
        LOG.debug("Request to update CoffeeShopReaction : {}", coffeeShopReactionDTO);
        CoffeeShopReactionEntity coffeeShopReactionEntity = coffeeShopReactionMapper.toEntity(coffeeShopReactionDTO);
        coffeeShopReactionEntity = coffeeShopReactionRepository.save(coffeeShopReactionEntity);
        return coffeeShopReactionMapper.toDto(coffeeShopReactionEntity);
    }

    /**
     * Partially update a coffeeShopReaction.
     *
     * @param coffeeShopReactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoffeeShopReactionDTO> partialUpdate(CoffeeShopReactionDTO coffeeShopReactionDTO) {
        LOG.debug("Request to partially update CoffeeShopReaction : {}", coffeeShopReactionDTO);

        return coffeeShopReactionRepository
            .findById(coffeeShopReactionDTO.getId())
            .map(existingCoffeeShopReaction -> {
                coffeeShopReactionMapper.partialUpdate(existingCoffeeShopReaction, coffeeShopReactionDTO);

                return existingCoffeeShopReaction;
            })
            .map(coffeeShopReactionRepository::save)
            .map(coffeeShopReactionMapper::toDto);
    }

    /**
     * Get all the coffeeShopReactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CoffeeShopReactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CoffeeShopReactions");
        return coffeeShopReactionRepository.findAll(pageable).map(coffeeShopReactionMapper::toDto);
    }

    /**
     * Get one coffeeShopReaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoffeeShopReactionDTO> findOne(Long id) {
        LOG.debug("Request to get CoffeeShopReaction : {}", id);
        return coffeeShopReactionRepository.findById(id).map(coffeeShopReactionMapper::toDto);
    }

    /**
     * Delete the coffeeShopReaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CoffeeShopReaction : {}", id);
        coffeeShopReactionRepository.deleteById(id);
    }
}
