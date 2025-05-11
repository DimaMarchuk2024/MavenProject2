package com.dima.service;

import com.dima.dao.impl.PizzaDao;
import com.dima.dto.PizzaCreateEditDto;
import com.dima.dto.PizzaReadDto;
import com.dima.entity.Pizza;
import com.dima.mapper.PizzaCreateEditMapper;
import com.dima.mapper.PizzaReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PizzaService {

    private final PizzaDao pizzaDao;
    private final PizzaReadMapper pizzaReadMapper;
    private final PizzaCreateEditMapper pizzaCreateEditMapper;
    private final ImageService imageService;

    public Page<PizzaReadDto> findAll(Pageable pageable) {
        return pizzaDao.findAll(pageable)
                .map(pizzaReadMapper::map);
    }

    public Optional<PizzaReadDto> findById(Integer id) {
        return pizzaDao.findById(id)
                .map(pizzaReadMapper::map);
    }

    public Optional<byte[]> findAvatar(Integer id) {
        return pizzaDao.findById(id)
                .map(Pizza::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public PizzaReadDto create(PizzaCreateEditDto pizzaCreateEditDto) {
        return Optional.of(pizzaCreateEditDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return pizzaCreateEditMapper.map(dto);
                })
                .map(pizzaDao::save)
                .map(pizzaReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<PizzaReadDto> update(Integer id, PizzaCreateEditDto pizzaCreateEditDto) {
        return pizzaDao.findById(id)
                .map(pizza ->{
                    uploadImage(pizzaCreateEditDto.getImage());
                    return  pizzaCreateEditMapper.map(pizzaCreateEditDto, pizza);
                })
                .map(pizzaDao::saveAndFlush)
                .map(pizzaReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
    public boolean delete(Integer id) {
        return pizzaDao.findById(id)
                .map(pizza -> {
                    pizzaDao.delete(pizza);
                    pizzaDao.flush();
                    return true;
                })
                .orElse(false);
    }
}
