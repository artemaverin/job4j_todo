package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;

    @GetMapping
    public String getAll(Model model, @RequestParam(name = "status", defaultValue = "1") int id) {
        if (id == 2) {
            model.addAttribute("tasks", taskService.findByStatus(true));
            return "tasks/list";
        }
        if (id == 3) {
            model.addAttribute("tasks", taskService.findByStatus(false));
            return "tasks/list";
        }
        model.addAttribute("class_active", "task");
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/status")
    public String getStatus() {
        return "redirect:/tasks";
    }

    @PostMapping
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.save(task);
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
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
        return "tasks/update";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
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
            taskService.update(task);
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
