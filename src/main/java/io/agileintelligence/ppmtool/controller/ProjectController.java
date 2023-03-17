package io.agileintelligence.ppmtool.controller;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.service.MapValidationErrorService;
import io.agileintelligence.ppmtool.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private  ProjectRepository projectRepository;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
        if(errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdate(project);
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<Project> getProjectByIdentifier(@PathVariable String projectIdentifier){
        Project project = projectService.findProjectByIdentifier(projectIdentifier);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getProjects(){ return projectService.findAllProjects(); }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<?> deleteProjectByIdentifier(@PathVariable String projectIdentifier){
        projectService.deleteProjectByIdentifier(projectIdentifier);
        return new ResponseEntity<String>(projectIdentifier+" Project deleted Successfully", HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project){
        Project project1 = projectService.updateProject(project, project.getId());
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProjectById(@PathVariable Long id, @RequestBody Project project){
        Project updateProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectIdException("This Project ID: " + id + " does not exists"));

        updateProject.setProjectName(project.getProjectName());
        updateProject.setProjectIdentifier(project.getProjectIdentifier());
        updateProject.setDescription(project.getDescription());

        projectRepository.save(updateProject);

        return new ResponseEntity<Project>(updateProject, HttpStatus.OK);
    }
}
