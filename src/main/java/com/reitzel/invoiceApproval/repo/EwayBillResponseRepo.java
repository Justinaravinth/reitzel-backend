package com.reitzel.invoiceApproval.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.EwayBillResponseVO;

@Repository
public interface EwayBillResponseRepo extends JpaRepository<EwayBillResponseVO, Long>{

}
