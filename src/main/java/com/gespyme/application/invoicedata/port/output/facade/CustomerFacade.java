package com.gespyme.application.invoicedata.port.output.facade;

import com.gespyme.commons.model.customer.CustomerModelApi;
import java.util.List;

public interface CustomerFacade {
  List<CustomerModelApi> getCustomers(String customerName);
}
