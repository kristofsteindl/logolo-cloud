package com.ksteindl.logolo.web;

import com.ksteindl.logolo.domain.Project;
import com.ksteindl.logolo.domain.ProjectInput;
import com.ksteindl.logolo.services.MapValidationErrorService;
import com.ksteindl.logolo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<Project> createNewProject(@Valid @RequestBody ProjectInput projectInput, BindingResult result, Principal principal) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Project persisted = projectService.createProject(projectInput, principal.getName());
        return new ResponseEntity(persisted, HttpStatus.CREATED);
    }

    @GetMapping("/{projectKey}")
    public ResponseEntity<Project> getProjectByKey(@PathVariable String projectKey, Principal principal) {
        Project project = projectService.findProjectByKey(projectKey, principal.getName());
        return new ResponseEntity(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProject(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectKey}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectKey, Principal principal) {
        projectService.deleteProjectByKey(projectKey, principal.getName());
        return new ResponseEntity("Project with id " + projectKey + " has been deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectInput projectInput,
            Principal principal,
            BindingResult result) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Project updated = projectService.updateProject(projectInput, id, principal.getName());
        return new ResponseEntity(updated, HttpStatus.CREATED);
    }
}
