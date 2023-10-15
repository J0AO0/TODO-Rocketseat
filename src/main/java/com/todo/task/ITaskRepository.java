package com.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    //AULA 04
    List<TaskModel> findByIdUser(UUID uuid);
}
