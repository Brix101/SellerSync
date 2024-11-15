package com.brix.Seller_Sync.taskqueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brix.Seller_Sync.taskqueue.entity.ReportTask;

public interface ReportTaskRepository extends JpaRepository<ReportTask, Long> {

}
