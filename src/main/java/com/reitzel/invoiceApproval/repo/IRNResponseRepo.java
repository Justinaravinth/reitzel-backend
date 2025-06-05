package com.reitzel.invoiceApproval.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reitzel.invoiceApproval.entity.IRNResponseVO;

public interface IRNResponseRepo extends JpaRepository<IRNResponseVO, Long> {

	IRNResponseVO findByDocid(String docId);

}
