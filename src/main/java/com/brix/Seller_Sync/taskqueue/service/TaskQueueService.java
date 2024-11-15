package com.brix.Seller_Sync.taskqueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskQueueService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // public enum TaskQueue {
    //     TASK_QUEUE("taskQueue");

    //     private String queueName;

    //     TaskQueue(String queueName) {
    //         this.queueName = queueName;
    //     }

    //     public String getQueueName() {
    //         return queueName;
    //     }
    // }


    // public void enqueue(String queueName, Long taskId) {
    //     redisTemplate.opsForList().leftPush(queueName, taskId.toString());
    // }

    // public String dequeue(String queueName) {
    //     return redisTemplate.opsForList().rightPop(queueName);
    // }

    public void enqueue( Long taskId) {
        redisTemplate.opsForList().leftPush("reportTasks", taskId.toString());
    }

    public Long dequeue(){
        String taskId = redisTemplate.opsForList().rightPop("reportTasks");

        return taskId != null ? Long.parseLong(taskId) : null;
    }
}
