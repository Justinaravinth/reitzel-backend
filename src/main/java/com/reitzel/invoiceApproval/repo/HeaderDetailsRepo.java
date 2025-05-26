package com.reitzel.invoiceApproval.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reitzel.invoiceApproval.entity.HeaderDetailsVO;

public interface HeaderDetailsRepo extends JpaRepository<HeaderDetailsVO, Long> {

	@Query(nativeQuery = true, value = "select a.user_name,a.gstin,A.CLIENT_ID,A.CLIENT_SECRET,A.AUTHTOKEN,A.SEK from einvoiceheader a, einvoice b where A.GSTIN=b.SELLERGSTIN and b.DOCID=?1 \r\n"
			+ "group by a.user_name,a.gstin,A.CLIENT_ID,A.CLIENT_SECRET,A.AUTHTOKEN,A.SEK")
	Set<Object[]> getHeaderDetails(String docId);

	HeaderDetailsVO findByUserName(String userName);

	@Query(value = "select user_name from einvoiceheader  where token_expiry < to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS') or token_expiry is null",nativeQuery = true)
	List<Object[]> getAutomationTokenDetails();

}
