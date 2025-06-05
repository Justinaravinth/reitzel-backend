package com.reitzel.invoiceApproval.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reitzel.invoiceApproval.entity.EwayBillResponseVO;

@Repository
public interface EwayBillResponseRepo extends JpaRepository<EwayBillResponseVO, Long>{

	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    SUPPLYTYPE,\r\n"
			+ "    SUBSUPPLYTYPE,\r\n"
			+ "    SUBSUPPLYDESC,\r\n"
			+ "    DOCTYPE,\r\n"
			+ "    DOCNO,\r\n"
			+ "    DOCDATE,\r\n"
			+ "    FROMGSTIN,\r\n"
			+ "    FROMTRDNAME,\r\n"
			+ "    FROMADDR1,\r\n"
			+ "    FROMADDR2,\r\n"
			+ "    FROMPLACE,\r\n"
			+ "    FROMPINCODE,\r\n"
			+ "    ACTFROMSTATECODE,\r\n"
			+ "    FROMSTATECODE,\r\n"
			+ "    TOGSTIN,\r\n"
			+ "    TOTRDNAME,\r\n"
			+ "    TOADDR1,\r\n"
			+ "    TOADDR2,\r\n"
			+ "    TOPLACE,\r\n"
			+ "    TOPINCODE,\r\n"
			+ "    ACTTOSTATECODE,\r\n"
			+ "    TOSTATECODE,\r\n"
			+ "    TRANSACTIONTYPE,\r\n"
			+ "    OTHERVALUE,\r\n"
			+ "    CGSTVALUE,\r\n"
			+ "    SGSTVALUE,\r\n"
			+ "    IGSTVALUE,\r\n"
			+ "    CESSVALUE,\r\n"
			+ "    CESSNONADVOLVALUE,\r\n"
			+ "    TOTINVVALUE,\r\n"
			+ "    TRANSPORTERID,\r\n"
			+ "    TRANSPORTERNAME,\r\n"
			+ "    TRANSDOCNO,\r\n"
			+ "    TRANSMODE,\r\n"
			+ "    TRANSDISTANCE,\r\n"
			+ "    TRANSDOCDATE,\r\n"
			+ "    VEHICLENO,\r\n"
			+ "    VEHICLETYPE\r\n"
			+ "FROM EWAYBILL_REQUEST where docno=?1 group by SUPPLYTYPE,\r\n"
			+ "    SUBSUPPLYTYPE,\r\n"
			+ "    SUBSUPPLYDESC,\r\n"
			+ "    DOCTYPE,\r\n"
			+ "    DOCNO,\r\n"
			+ "    DOCDATE,\r\n"
			+ "    FROMGSTIN,\r\n"
			+ "    FROMTRDNAME,\r\n"
			+ "    FROMADDR1,\r\n"
			+ "    FROMADDR2,\r\n"
			+ "    FROMPLACE,\r\n"
			+ "    FROMPINCODE,\r\n"
			+ "    ACTFROMSTATECODE,\r\n"
			+ "    FROMSTATECODE,\r\n"
			+ "    TOGSTIN,\r\n"
			+ "    TOTRDNAME,\r\n"
			+ "    TOADDR1,\r\n"
			+ "    TOADDR2,\r\n"
			+ "    TOPLACE,\r\n"
			+ "    TOPINCODE,\r\n"
			+ "    ACTTOSTATECODE,\r\n"
			+ "    TOSTATECODE,\r\n"
			+ "    TRANSACTIONTYPE,\r\n"
			+ "    OTHERVALUE,\r\n"
			+ "    CGSTVALUE,\r\n"
			+ "    SGSTVALUE,\r\n"
			+ "    IGSTVALUE,\r\n"
			+ "    CESSVALUE,\r\n"
			+ "    CESSNONADVOLVALUE,\r\n"
			+ "    TOTINVVALUE,\r\n"
			+ "    TRANSPORTERID,\r\n"
			+ "    TRANSPORTERNAME,\r\n"
			+ "    TRANSDOCNO,\r\n"
			+ "    TRANSMODE,\r\n"
			+ "    TRANSDISTANCE,\r\n"
			+ "    TRANSDOCDATE,\r\n"
			+ "    VEHICLENO,\r\n"
			+ "    VEHICLETYPE")
	Object[] getHeaderDetails(String docId);

	@Query(nativeQuery = true,value = "select productname,productdesc,hsncode,quantity,qtyunit,cgstrate,sgstrate,igstrate,cessrate,cessnonadvol,taxableamount from EWAYBILL_REQUEST where docno=?1\r\n"
			+ "group by productname,productdesc,hsncode,quantity,qtyunit,cgstrate,sgstrate,igstrate,cessrate,cessnonadvol,taxableamount")
	List<Object[]> getItemListDetails(String docId);
	
	
	@Query(nativeQuery = true,value = "SELECT a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK \r\n"
			+ "            FROM einvoiceheader a, EWAYBILL_REQUEST b \r\n"
			+ "           WHERE a.GSTIN = b.FROMGSTIN AND b.docno =?1 \r\n"
			+ "           GROUP BY a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK")
	Set<Object[]> getEwayHeaderDetails(String docId);

	@Query(nativeQuery = true,value = "select docno,docdate from EWAYBILL_REQUEST where genewaybill='T' and eapicall='F'  group by docno,docdate")
	List<Object[]> getPendingEwayNonIRNDetails();

}
