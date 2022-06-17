package cs.vsu.ru.FuzzySystem.controllers;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cs.vsu.ru.FuzzySystem.FuzzySystemApplication;
import cs.vsu.ru.FuzzySystem.model.Function;
import cs.vsu.ru.FuzzySystem.model.Method;
import cs.vsu.ru.FuzzySystem.model.Rule;
import cs.vsu.ru.FuzzySystem.services.FuzzySystemService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private FuzzySystemService fService;

    @PostMapping(value = "/evualation")
    public String getEvualation(@RequestBody Method rules) {
        return rules.getName();
    }

    @PostMapping("/input_file")
    public String index(@RequestParam("input_file") MultipartFile file) {
        return file.getName();
    }
}
