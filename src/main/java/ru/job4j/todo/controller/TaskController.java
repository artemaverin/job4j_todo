package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.status.Status;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;
    private PriorityService priorityService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("class_active", "task");
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/list";
    }

    @GetMapping("/complete")
    public String getComplete(Model model) {
        model.addAttribute("tasks", taskService.findByStatus(Status.COMPLETED));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskService.findByStatus(Status.NEW));
        return "tasks/list";
    }

    @GetMapping("/status")
    public String getStatus() {
        return "redirect:/tasks";
    }

    @PostMapping
    public String create(@ModelAttribute Task task, Model model, @SessionAttribute User user) {
        task.setUser(user);
        var optionalTask = taskService.save(task);
        if (optionalTask.isEmpty()) {
            model.addAttribute("error", "Ошибка");
            return "errors/404";
        }
        return "redirect:/tasks";

    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задачи с указанным id не существует");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задачи с указанным id не существует");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("error", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/done/{id}")
    public String done(@PathVariable int id, Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задачи с указанным id не существует");
            return "error/404";
        }
        var task = taskOptional.get();
        if (!task.isDone()) {
            task.setDone(true);
            taskService.complete(task);
        }
        model.addAttribute("task", task);
        return "tasks/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

}
