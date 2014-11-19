package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by 4t-johoi on 13/11/14.
 */
public class CancelPayment extends Operation {

    private String transactionId;
    private String transmissionTime;
    private String sharedSecret;
    private String serviceURL;
    private String errorMessage;
    private String resultJson;


    public CancelPayment(String transactionId, String transmissionTime, String sharedSecret, String serviceUrl) {
        this.transactionId = transactionId;
        this.sharedSecret = sharedSecret;
        this.serviceURL = serviceUrl;
        this.transmissionTime = transmissionTime;
    }

    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(transactionId);
        sb.append(transmissionTime);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }


    public void execute() throws IOException {
        String checkTransactionURLSuffix = "/transactions/cancel";
        String actualURL = serviceURL + checkTransactionURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.postToURL(this, actualURL);
        handleResponse(actualURL, httpResponse);
    }


    @Override
    public String toString() {
        return "{\"transactionId\" : \"" + transactionId + "\", " +
                "\"transmissionTime\" : \"" + transmissionTime + "\"" +
                "}";
    }

    @Override
    public String toJsonString() {
        return toString();
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

