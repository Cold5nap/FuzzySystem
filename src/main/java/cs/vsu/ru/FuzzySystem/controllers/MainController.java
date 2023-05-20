package cs.vsu.ru.FuzzySystem.controllers;

import cs.vsu.ru.FuzzySystem.model.*;
import cs.vsu.ru.FuzzySystem.services.FuzzySystemService;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081")
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

	@GetMapping(value = "/examples/{id}")
	public String getExample(@PathVariable("id") Long id) throws Exception {
		System.out.println(new ClassPathResource("static/examples/"+id+".json").getPath());
		InputStream input = new ClassPathResource("static/examples/"+id+".json").getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    StringBuilder stringBuilder = new StringBuilder();
		String line;
    while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
    }
    reader.close();
    return stringBuilder.toString();
  }

  @PostMapping(value = "/export_fcl")
  public String exportFCL(@RequestBody FuzzySystem fuzzySystem) throws Exception {
    return fService.toStringFCL(fuzzySystem);
  }

  @PostMapping(value = "/export")
  public String export(@RequestBody FuzzySystem fuzzySystem) throws Exception {
    return fService.toString(fuzzySystem);
  }

  @PostMapping("/input_file")
  @ResponseBody
  public FuzzySystem index(@RequestParam("file") MultipartFile file) throws Exception {
    return fService.fromString(new String(file.getBytes()));
  }
}
