package com.reitzel.invoiceApproval.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.EwayResponseVO;

@Repository
public interface EwayResponseRepo extends JpaRepository<EwayResponseVO, Long>{

}
