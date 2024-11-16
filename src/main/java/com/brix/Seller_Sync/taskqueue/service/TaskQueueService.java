package com.brix.Seller_Sync.taskqueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskQueueService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void enqueue( Long taskId) {
        redisTemplate.opsForList().leftPush("reportTasks", taskId.toString());
    }

    public Long dequeue(){
        String taskId = redisTemplate.opsForList().rightPop("reportTasks");

        return taskId != null ? Long.parseLong(taskId) : null;
    }

    public String getTask() {
        return redisTemplate.opsForList().rightPop("reportTasks");
    }
}
