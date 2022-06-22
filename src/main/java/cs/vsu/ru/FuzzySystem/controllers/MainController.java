package cs.vsu.ru.FuzzySystem.controllers;

import cs.vsu.ru.FuzzySystem.model.*;
import cs.vsu.ru.FuzzySystem.services.FuzzySystemService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MainController {

	private final FuzzySystemService fService;

	public MainController(FuzzySystemService fService) {
		this.fService = fService;
	}

	@PostMapping(value = "/get_output", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Map<String, Double> getEvaluation(@RequestBody FuzzySystem fuzzySystem) throws Exception {
		return fService.getOutputValues(fuzzySystem);
	}

	@PostMapping("/input_file")
	public String index(@RequestParam("input_file") MultipartFile file) {
		return file.getName();
	}
}
