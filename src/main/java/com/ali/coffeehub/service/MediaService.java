package com.ali.coffeehub.service;

import com.ali.coffeehub.domain.MediaEntity;
import com.ali.coffeehub.repository.MediaRepository;
import com.ali.coffeehub.service.dto.MediaDTO;
import com.ali.coffeehub.service.mapper.MediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ali.coffeehub.domain.MediaEntity}.
 */
@Service
@Transactional
public class MediaService {

    private static final Logger LOG = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaService(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Save a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaDTO save(MediaDTO mediaDTO) {
        LOG.debug("Request to save Media : {}", mediaDTO);
        MediaEntity mediaEntity = mediaMapper.toEntity(mediaDTO);
        mediaEntity = mediaRepository.save(mediaEntity);
        return mediaMapper.toDto(mediaEntity);
    }

    /**
     * Update a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaDTO update(MediaDTO mediaDTO) {
        LOG.debug("Request to update Media : {}", mediaDTO);
        MediaEntity mediaEntity = mediaMapper.toEntity(mediaDTO);
        mediaEntity = mediaRepository.save(mediaEntity);
        return mediaMapper.toDto(mediaEntity);
    }

    /**
     * Partially update a media.
     *
     * @param mediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MediaDTO> partialUpdate(MediaDTO mediaDTO) {
        LOG.debug("Request to partially update Media : {}", mediaDTO);

        return mediaRepository
            .findById(mediaDTO.getId())
            .map(existingMedia -> {
                mediaMapper.partialUpdate(existingMedia, mediaDTO);

                return existingMedia;
            })
            .map(mediaRepository::save)
            .map(mediaMapper::toDto);
    }

    /**
     * Get all the media.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MediaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Media");
        return mediaRepository.findAll(pageable).map(mediaMapper::toDto);
    }

    /**
     * Get one media by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MediaDTO> findOne(Long id) {
        LOG.debug("Request to get Media : {}", id);
        return mediaRepository.findById(id).map(mediaMapper::toDto);
    }

    /**
     * Delete the media by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Media : {}", id);
        mediaRepository.deleteById(id);
    }
}
