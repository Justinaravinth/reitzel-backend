package com.reitzel.invoiceApproval.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.EwayHeadersVO;

@Repository
public interface EwayHeadersRepo extends JpaRepository<EwayHeadersVO, Long>{

	@Query(nativeQuery =true,value ="SELECT a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK \r\n"
			+ "FROM ewayheader a, einvoice b \r\n"
			+ "WHERE a.GSTIN = b.SELLERGSTIN AND b.irn =?1 \r\n"
			+ "GROUP BY a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK")
	Set<Object[]> getEwayHeaderDetails(String irn);

}
