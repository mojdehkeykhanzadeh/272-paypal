package com.paypal.controller;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import junit.textui.ResultPrinter;

import com.paypal.api.payments.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class paymentController {
String clientId = "<id>";
String clientSecret = "<secret>";
@RequestMapping(value = "/payment", method = RequestMethod.POST, produces = "application/json")
public @ResponseBody Payment makePayment() throws PayPalRESTException {
	Amount amount = new Amount();
	amount.setCurrency("USD");
	amount.setTotal("1.00");

	Transaction transaction = new Transaction();
	transaction.setAmount(amount);
	List<Transaction> transactions = new ArrayList<Transaction>();
	transactions.add(transaction);

	Payer payer = new Payer();
	payer.setPaymentMethod("paypal");

	Payment payment = new Payment();
	payment.setIntent("sale");
	payment.setPayer(payer);
	payment.setTransactions(transactions);

	RedirectUrls redirectUrls = new RedirectUrls();
	redirectUrls.setCancelUrl("https://example.com/cancel");
	redirectUrls.setReturnUrl("https://example.com/return");
	payment.setRedirectUrls(redirectUrls);
	try {
	    APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
	    Payment createdPayment = payment.create(apiContext);
	    apiContext.usingGoogleAppEngine(true);
	    return createdPayment;
//	    return createdPayment.toJSON();
	} catch (PayPalRESTException e) {
	    // Handle errors
		e.printStackTrace();
        throw new PayPalRESTException("TRANSACTION FAILURE"
                + e.getLocalizedMessage());
	}
}
}
