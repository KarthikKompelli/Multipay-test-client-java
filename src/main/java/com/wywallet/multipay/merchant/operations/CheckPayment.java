package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by 4t-johoi on 12/11/14.
 */
public class CheckPayment extends Operation {


    private String paymentId;
    private String sharedSecret;
    private String serviceURL;
    private String errorMessage;
    private String resultJson;

    public CheckPayment(String paymentId, String sharedSecret, String serviceURL) {
        this.paymentId = paymentId;
        this.sharedSecret = sharedSecret;
        this.serviceURL = serviceURL;
    }

    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(paymentId);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }


    public void execute() throws IOException {
        String checkTransactionURLSuffix = "/payments/" + paymentId;
        String actualURL = serviceURL + checkTransactionURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.getFromURL(this, actualURL);
        handleResponse(actualURL, httpResponse);
    }


    @Override
    public String toString() {
        return "{PaymentId : " + paymentId + "}";
    }

    @Override
    public String toJsonString() {
        return "";
    }

    @Override
    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }

    @Override
    public String getJsonResult() {
        return resultJson;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
