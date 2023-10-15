package com.todo.task;

import com.todo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        //AULA 04
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        //AULA 04
        var currentDate = LocalDateTime.now();
        // current - 10/11/2023
        // startAt - 11/10/2023
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início/término deve ser maior do que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser menor do que a data de término");
        }

       var task = this.taskRepository.save(taskModel);
       return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    //LISTA DE TAREFAS

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID)idUser);
        return tasks;
    }

    //HttpServletRequest serve para garantir se o usuario esta autenticado na aplicação.
    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
        var task = this.taskRepository.findById(id).orElse(null);

        Utils.copyNonNullProperties(taskModel, task);

        return this.taskRepository.save(task);
    }
}
