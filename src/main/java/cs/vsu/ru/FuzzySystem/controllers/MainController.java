package cs.vsu.ru.FuzzySystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cs.vsu.ru.FuzzySystem.FuzzySystemApplication;
import cs.vsu.ru.FuzzySystem.services.FuzzySystemService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private FuzzySystemService fService;

    @GetMapping("/test")
    public String test() {
        return "wot otvet";
    }

    @GetMapping("/graphic")
    public String graphic() {
        
        return "wot otvet";
    }

    @PostMapping("/")
    public String tse(){
        return "wot otvet";
    }
    @PostMapping("/input_file")
    public String index(@RequestParam("input_file")MultipartFile file){
        return file.getName();
    }
}
