package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by 4t-johoi on 13/11/14.
 */
public class RefundPayment extends Operation {


    private final String serviceURL;
    private final String transactionId;
    private String sharedSecret;

    private int amount;
    private int vat;
    private String vatFormat;


    private String transmissionTime;

    private String errorMessage;
    private String resultJson;
    private String description;


    public RefundPayment(String transactionId, String description, String transmissionTime, int amount, int vatAmount, String vatFormat, String sharedSecret, String serviceUrl) {
        this.transactionId = transactionId;
        this.description = description;
        this.amount = amount;
        this.vat = vatAmount;
        this.vatFormat = vatFormat;
        this.transmissionTime = transmissionTime;

        this.sharedSecret = sharedSecret;


        this.serviceURL = serviceUrl;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append(System.lineSeparator() + "  \"description\" : \"" + description + "\",");
        sb.append(System.lineSeparator() + "  \"transactionId\" : \"" + transactionId + "\",");
        sb.append(System.lineSeparator() + "    \"amount\" : \"" + amount + "\",");
        sb.append(System.lineSeparator() + "    \"vat\" : \"" + vat + "\",");
        sb.append(System.lineSeparator() + "    \"vatFormat\" : \"" + vatFormat + "\",");
        sb.append(System.lineSeparator() + "  \"transmissionTime\" : \"" + transmissionTime + "\"");
        sb.append(System.lineSeparator() + "}");

        return sb.toString();
    }

    public String toJsonString() {
        return toString();
    }


    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(description);
        sb.append(transactionId);
        sb.append(amount);
        sb.append(vat);
        sb.append(vatFormat);
        sb.append(transmissionTime);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }


    @Override
    public void execute() throws IOException {
        String purchaseURLSuffix = "/transactions/refund/";
        String actualURL = serviceURL + purchaseURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.postToURL(this, actualURL);
        handleResponse(actualURL, httpResponse);
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
