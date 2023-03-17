package io.agileintelligence.ppmtool.service;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdate(Project project){
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception ex){
            throw new ProjectIdException("Project Id '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project Id '"+projectIdentifier+"' does not exists");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectIdentifier){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Cannot delete project '"+projectIdentifier+"'. This project does not exists");
        }
        projectRepository.delete(project);
    }

    public Project updateProject(Project project, Long id){
        try{
            Project updateProject = projectRepository.findAllById(id);
            updateProject.setId(project.getId());
            updateProject.setProjectName(project.getProjectName());
            updateProject.setProjectIdentifier(project.getProjectIdentifier());
            updateProject.setDescription(project.getDescription());
            return projectRepository.save(updateProject);
        }catch (Exception ex){
            throw new ProjectIdException("Cannot update project id: "+project.getId()+". This project id does not exist");
        }
    }
}
