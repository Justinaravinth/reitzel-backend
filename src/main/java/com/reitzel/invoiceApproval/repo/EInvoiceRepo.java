package com.reitzel.invoiceApproval.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reitzel.invoiceApproval.entity.EInvoiceVO;

public interface EInvoiceRepo extends JpaRepository<EInvoiceVO, Long> {

	List<EInvoiceVO> findBydocid(String docId);

	@Query(nativeQuery = true, value = "SELECT  \r\n"
			+ "       docid, \r\n"
			+ "       docdate, \r\n"
			+ "       suptype, \r\n"
			+ "       documenttype, \r\n"
			+ "       buyergstin, \r\n"
			+ "       buyerlegalname, \r\n"
			+ "       buyertradename, \r\n"
			+ "       buyerpos, \r\n"
			+ "       buyeradd1, \r\n"
			+ "       add2, \r\n"
			+ "       buyerlocation, \r\n"
			+ "       buyerpincode, \r\n"
			+ "       buyerstcd, \r\n"
			+ "       tottaxablevalue, \r\n"
			+ "       vigstamt, \r\n"
			+ "       vcgstamt, \r\n"
			+ "       vsgstamt, \r\n"
			+ "       vothercharges, \r\n"
			+ "       totinvvalue\r\n"
			+ "FROM einvoice \r\n"
			+ "WHERE docid = ?1 group by  \r\n"
			+ "       docid, \r\n"
			+ "       docdate, \r\n"
			+ "       suptype, \r\n"
			+ "       documenttype, \r\n"
			+ "       buyergstin, \r\n"
			+ "       buyerlegalname, \r\n"
			+ "       buyertradename, \r\n"
			+ "       buyerpos, \r\n"
			+ "       buyeradd1, \r\n"
			+ "       add2, \r\n"
			+ "       buyerlocation, \r\n"
			+ "       buyerpincode, \r\n"
			+ "       buyerstcd, \r\n"
			+ "       tottaxablevalue, \r\n"
			+ "       vigstamt, \r\n"
			+ "       vcgstamt, \r\n"
			+ "       vsgstamt, \r\n"
			+ "       vothercharges, \r\n"
			+ "       totinvvalue")
	Object[] getHeaderDetails(String docId);

	@Query(nativeQuery = true,value = "select  slno, \r\n"
			+ "       productiondesc, \r\n"
			+ "       isservice, \r\n"
			+ "       hsncode, \r\n"
			+ "       quantity, \r\n"
			+ "       unitprice, \r\n"
			+ "       grossamount, \r\n"
			+ "       taxablevalue, \r\n"
			+ "       gstrate, \r\n"
			+ "       igstamt, \r\n"
			+ "       sgstamt, \r\n"
			+ "       cgstamt, \r\n"
			+ "       itemtotal, \r\n"
			+ "       unit \r\n"
			+ "FROM einvoice a where a.docid=?1 order by slno asc")
	List<Object[]> getChargeDetails(String docId);

	@Query(nativeQuery = true, value = " select docid from einvoice\r\n"
			+ "                   WHERE     to_date(docdate) = ?1\r\n"
			+ "                         AND ackno IS NULL\r\n"
			+ "                         AND cancel = 'F'\r\n"
			+ "                         group by docid")
	Set<Object[]> getDocId(String docDate);

	@Query(nativeQuery = true,value = "select docid,docdate from einvoicegenarate")
	List<Object[]> getPendingIRNDetails();

	@Query(nativeQuery = true,value = "select a.* from einvoice a where a.docid=?1")
	List<EInvoiceVO> getDocidDetails(String docId);

	@Query(nativeQuery = true,value = "SELECT  taxsch,case when revcharge='No' then 'N'else 'Y' end revcharge,case when igstonintra='No' then 'N'else 'Y' end igstonintra,\r\n"
			+ "       upper(sellergstin)sellergstin,upper(sellerlegalname)sellerlegalname,upper(sellertradename)sellertradename,upper(selleradd1)selleradd1,upper(selleradd2)selleradd2,upper(sellerlocation)sellerlocation,sellerpincode,sellerstcd\r\n"
			+ "FROM einvoice \r\n"
			+ "WHERE docid = ?1 group by taxsch,revcharge,igstonintra,sellergstin,sellerlegalname,sellertradename,selleradd1,selleradd2,sellerlocation,sellerpincode,sellerstcd")
	Object[] getHeaders(String docId);


	@Query(nativeQuery =  true,value = "SELECT e.irn, e.distance, e.transmode, e.transid, e.transname,\r\n"
			+ "       e.transdocno, e.transdocdate, e.vehicleno, e.vehicletype,\r\n"
			+ "       e.shipadd1, e.shipadd2 AS shipadd2, e.shiplocation, e.shippincode,\r\n"
			+ "       e.shipstcd, e.SELLERLEGALNAME, e.SELLERADD1, e.SELLERADD2, e.SELLERLOCATION, e.SELLERPINCODE, e.SELLERSTCD\r\n"
			+ "FROM einvoice e\r\n"
			+ "WHERE e.IRN IS NOT NULL AND docid =?1 \r\n"
			+ "GROUP BY e.irn, e.distance, e.transmode, e.transid, e.transname,\r\n"
			+ "         e.transdocno, e.transdocdate, e.vehicleno, e.vehicletype,\r\n"
			+ "         e.shipadd1, e.shipadd2 , e.shiplocation, e.shippincode,\r\n"
			+ "       e.shipstcd, e.SELLERLEGALNAME, e.SELLERADD1, e.SELLERADD2, e.SELLERLOCATION, e.SELLERPINCODE, e.SELLERSTCD")
	Set<Object[]> getEwayBillDetails(String docId);

	@Query(nativeQuery =true,value ="SELECT a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK \r\n"
			+ "FROM einvoiceheader a, einvoice b \r\n"
			+ "WHERE a.GSTIN = b.SELLERGSTIN AND b.docid =?1 \r\n"
			+ "GROUP BY a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK")
	Set<Object[]> getEwayHeaderDetails(String irn);

	@Query(value="select * from einvoice where docid=?1",nativeQuery =true)
	List<EInvoiceVO> getIrnDetails(String docId);

	@Query(nativeQuery = true,value = "select docid,docdate from ewaybillgenerate")
	List<Object[]> getPendingEwayDetails();

	@Query(nativeQuery =true,value ="SELECT a.user_name, a.gstin, a.CLIENT_ID, a.CLIENT_SECRET, a.AUTHTOKEN, a.SEK \r\n"
			+ "FROM einvoiceheader a where a.gstin=?1")
	Set<Object[]> getTokenDetails(String gstin);

	@Query(nativeQuery = true, value = "select suptype from einvoice where docid=?1 group by suptype")
	String getSupType(String irn);

	@Query(nativeQuery = true,value = "select irn from einvoice where docid=?1 group by irn")
	Set<Object[]> getIrnDetailsForCancel(String docId);

	@Query(nativeQuery = true, value = "SELECT TO_CHAR(TO_DATE(SUBSTR(ack_dt, 1, 10), 'YYYY-MM-DD'), 'DD-MM-YYYY') dt, count(*) AS IRNGenerated\r\n"
			+ "FROM irninvoice group by TO_CHAR(TO_DATE(SUBSTR(ack_dt, 1, 10), 'YYYY-MM-DD'), 'DD-MM-YYYY') order by TO_CHAR(TO_DATE(SUBSTR(ack_dt, 1, 10), 'YYYY-MM-DD'), 'DD-MM-YYYY') desc")
	List<Object[]> getAckDateWiseCount();
	




}
