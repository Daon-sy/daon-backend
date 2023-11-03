package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }
}
