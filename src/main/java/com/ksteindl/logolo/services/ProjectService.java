package com.ksteindl.logolo.services;

import com.ksteindl.logolo.domain.Backlog;
import com.ksteindl.logolo.domain.Project;
import com.ksteindl.logolo.domain.ProjectInput;
import com.ksteindl.logolo.domain.User;
import com.ksteindl.logolo.exceptions.ResourceNotFoundException;
import com.ksteindl.logolo.exceptions.ValidationException;
import com.ksteindl.logolo.repositories.ProjectRepository;
import com.ksteindl.logolo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project createProject(ProjectInput projectInput, String principalName) {
        try {
            Project project = convertToProject(projectInput);
            project.setProjectLeader(principalName);
            Project found = projectRepository.findByProjectKey(project.getProjectKey());
            if (found != null) {
                throw new ValidationException("Project with key '" + found.getProjectKey() + "' already exists");
            }
            Backlog backlog = new Backlog();
            project.setBacklog(backlog);
            backlog.setProject(project);
            backlog.setProjectKey(project.getProjectKey());
            return projectRepository.save(project);
            // It is because the javax.persistence Entity validation. This is redundant, but double check is better then no check.
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ValidationException("During database persisting,  'DataIntegrityViolationException' was thrown. This means the previously defined validation rule(s) was/were violated.");
        }
    }


    public Project findProjectByKey(String projectKey, String username) {
        Project project = projectRepository.findByProjectKey(projectKey.toUpperCase());
        if (project == null) {
            throw new ResourceNotFoundException("project", "Projectkey '" + projectKey.toUpperCase() + "' does not exist");
        }
        if (!project.getProjectLeader().equals(username)) {
            throw new ValidationException("project", "Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByKey(String projectKey, String username) {
        projectRepository.delete(findProjectByKey(projectKey, username));
    }

    public Project updateProject(ProjectInput projectInput, Long id, String username) {
        Project oldProject = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("project", "Cannot find project with id '" + id + "'"));
        if(!oldProject.getProjectLeader().equals(username)) {
            throw new ValidationException("project", "Project not found in your account");
        }
        try {
            updateOldProject(oldProject, projectInput);
            return projectRepository.save(oldProject);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ValidationException("During database persisting,  'DataIntegrityViolationException' was thrown. This means the previously defined validation rule(s) was/were violated.");
        }
    }

    private void updateOldProject(Project oldProject, ProjectInput projectInput) {
        oldProject.setProjectName(projectInput.getProjectName());
        oldProject.setDescription((projectInput.getDescription()));
        oldProject.setStartDate(projectInput.getStartDate());
        oldProject.setEndDate(projectInput.getEndDate());
    }

    private Project convertToProject(ProjectInput projectInput) {
        Project project = new Project();
        project.setProjectKey(projectInput.getProjectKey().toUpperCase());
        project.setProjectName(projectInput.getProjectName());
        project.setDescription((projectInput.getDescription()));
        return project;
    }



}
