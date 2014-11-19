package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by 4t-johoi on 13/11/14.
 */
public class GetRefund extends Operation {

    private String refundId;
    private String sharedSecret;
    private String serviceURL;
    private String errorMessage;
    private String resultJson;


    public GetRefund(String refundId, String sharedSecret, String serviceURL) {
        this.refundId = refundId;
        this.sharedSecret = sharedSecret;
        this.serviceURL = serviceURL;
    }

    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(refundId);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }

    public void execute() throws IOException {
        String checkrefundURLSuffix = "/refunds/" + refundId;
        String actualURL = serviceURL + checkrefundURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.getFromURL(this, actualURL);
        handleResponse(actualURL, httpResponse);
    }


    @Override
    public String toString() {
        return "{\"refundId\" : \"" + refundId + "\"" +
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
