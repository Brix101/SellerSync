package com.brix.Seller_Sync.taskqueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.taskqueue.repository.ReportTaskRepository;

import lombok.extern.java.Log;

@Service
@Log
public class TaskProcessor {

    @Autowired
    private ReportTaskRepository reportTaskRepository;

    @Autowired
    private TaskQueueService taskQueueService;

    @Scheduled(fixedRate = 5000)
    public void processTask() {
        log.info("Processing task");
        String taskId = taskQueueService.getTask();
        log.info("Task ID: " + taskId);
        // if (task != null) {
        //     String url = task.getUrl();
        //     restTemplate.getForObject(url, String.class);
        //     task.setStatus("COMPLETED");
        //     reportTaskRepository.save(task);
        // }
    }
}
