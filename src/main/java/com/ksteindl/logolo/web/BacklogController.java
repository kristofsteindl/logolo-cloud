package com.ksteindl.logolo.web;

import com.ksteindl.logolo.domain.Project;
import com.ksteindl.logolo.domain.ProjectInput;
import com.ksteindl.logolo.domain.Task;
import com.ksteindl.logolo.domain.TaskInput;
import com.ksteindl.logolo.services.MapValidationErrorService;
import com.ksteindl.logolo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/backlog")
@CrossOrigin // TODO maybe we should remove this?
public class BacklogController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<Task> addTaskToBacklog(
            @Valid @RequestBody TaskInput taskInput,
            BindingResult result,
            Principal principal) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Task newTask = taskService.addTask(taskInput, principal.getName());
        return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
    }

    @GetMapping("/{projectKey}")
    public ResponseEntity<List<Task>> getProjectBacklog(@PathVariable String projectKey, Principal principal) {
        return new ResponseEntity(taskService.findBacklogById(projectKey, principal.getName()), HttpStatus.OK);
    }


    // TODO this should be definetily be refactored. Maybe projectSequence can be renamed and/or
    @GetMapping("/{projectKey}/{projectSequence}")
    public ResponseEntity<Task> getTask(
            @PathVariable String projectKey,
            @PathVariable String projectSequence,
            Principal principal) {
        Task task = taskService.findTaskByProjectSequence(projectKey, projectSequence, principal.getName());
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskInput taskInput,
            BindingResult result,
            Principal principal ) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Task task = taskService.updateTask(taskInput, id, principal.getName());
        return new ResponseEntity(task, HttpStatus.CREATED);
    }

    // TODO - this endpoint should be deleted
    @PutMapping("/{projectKey}/{projectSequence}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String projectKey,
            @PathVariable String projectSequence,
            @Valid @RequestBody TaskInput taskInput,
            BindingResult result,
            Principal principal) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Task task = taskService.updateTask(taskInput, projectKey, projectSequence, principal.getName());
        return new ResponseEntity(task, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Principal principal) {
        taskService.deleteTaskByProjectSequence(id, principal.getName());
        return new ResponseEntity("Task with id " + id + " has been deleted", HttpStatus.OK);
    }

    // TODO - this endpoint should be deleted
    @DeleteMapping("/{projectKey}/{projectSequence}")
    public ResponseEntity<String> deleteTask(@PathVariable String projectKey, @PathVariable String projectSequence, Principal principal) {
        taskService.deleteTaskByProjectSequence(projectKey, projectSequence, principal.getName());
        return new ResponseEntity("Task with project sequence " + projectSequence + " has been deleted", HttpStatus.OK);
    }


}
