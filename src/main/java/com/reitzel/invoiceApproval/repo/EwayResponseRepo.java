package com.reitzel.invoiceApproval.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.ErrorListVO;
import com.reitzel.invoiceApproval.entity.EwayResponseVO;

@Repository
public interface EwayResponseRepo extends JpaRepository<EwayResponseVO, Long>{

	@Query(nativeQuery = true, value = "select a.* from errorlist a where a.errorcode in(?1)")
	List<ErrorListVO> getErrorDetails(String l);

}
