package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by 4t-johoi on 11/11/14.
 */
public class CheckTransaction extends Operation {

    private String transactionId;
    private String sharedSecret;
    private String serviceURL;
    private String errorMessage;
    private String resultJson;


    public CheckTransaction(String transactionId, String sharedSecret, String serviceURL) {
        this.transactionId = transactionId;
        this.sharedSecret = sharedSecret;
        this.serviceURL = serviceURL;
    }

    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(transactionId);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }


    public void execute() throws IOException {
        String checkTransactionURLSuffix = "/transactions/" + transactionId;
        String actualURL = serviceURL + checkTransactionURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.getFromURL(this, actualURL);
        handleResponse(actualURL, httpResponse);
    }


    @Override
    public String toString() {
        return "{\"TransactionId\" : \"" + transactionId + "\"" +
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
