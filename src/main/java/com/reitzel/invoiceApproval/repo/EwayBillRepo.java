package com.reitzel.invoiceApproval.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.EwayBillVO;

@Repository
public interface EwayBillRepo extends JpaRepository<EwayBillVO, Long> {

	@Query(nativeQuery = true, value = "select a.* from einvoice a where a.IRN=?1")
	List<EwayBillVO> getIrnDetails(String irn);

}
