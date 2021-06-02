package kodlamaio.hrms.api.controllers;

import kodlamaio.hrms.business.abstracts.ResumeService;
import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.core.utilities.results.ErrorDataResult;
import kodlamaio.hrms.core.utilities.results.Result;
import kodlamaio.hrms.entities.concretes.Resume;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/resumes")
public class ResumesController {

    private final ResumeService resumeService;

    public ResumesController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> add(@Valid @RequestBody Resume resume) {
        return ResponseEntity.ok(this.resumeService.add(resume));
    }

    @PostMapping(value = "/upload-image")
    public Result uploadImage(@RequestParam int resumeId, @Valid @RequestParam("image") MultipartFile multipartFile) {
        return this.resumeService.upload(resumeId, multipartFile);
    }

    @GetMapping(value = "/get-all-sorted-by-graduation-date")
    public DataResult<List<Resume>> getAllSortedByGraduationDate() {
        return this.resumeService.getAllSortedByGraduationDate();
    }

    @GetMapping(value = "/get-all-sorted-by-experience-year")
    public DataResult<List<Resume>> getAllSortedByExperienceYear() {
        return this.resumeService.getAllSortedByExperienceYear();
    }

    //All Resumes of Employee
    @GetMapping(value = "/get-by-employee-id")
    public DataResult<List<Resume>> getByEmployee_Id(@RequestParam int employeeId) {
        return this.resumeService.getByEmployee_Id(employeeId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDataResult<Object> validationExceptionHandler(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<>();
        for(FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ErrorDataResult<>(validationErrors, "Validation errors");
    }
}