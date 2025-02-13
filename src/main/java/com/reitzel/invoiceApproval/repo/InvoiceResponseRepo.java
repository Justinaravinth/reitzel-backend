package com.reitzel.invoiceApproval.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reitzel.invoiceApproval.entity.InvoiceResponseVO;

public interface InvoiceResponseRepo extends JpaRepository<InvoiceResponseVO, Long> {

}
