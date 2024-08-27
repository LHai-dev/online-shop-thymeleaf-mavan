package com.onlineshopthymeleaf.service;

import com.onlineshopthymeleaf.model.Color;
import com.onlineshopthymeleaf.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.valves.rewrite.InternalRewriteMap;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    public List<Color> getAllColor(){
        return colorRepository.findAll();
    }
    public Color getColorById(Byte id) {
        return colorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
    }
    public List<Color> findAllById(List<Byte> ids) {
        return colorRepository.findAllById(ids);
    }
    public void saveColor(Color color) {
        // Convert the name to uppercase
        if (color.getName() != null) {
            color.setName(color.getName().toUpperCase());
        }
        colorRepository.save(color);
    }

    public void deleteColor(Byte id) {
        colorRepository.deleteById(id);
    }
}
