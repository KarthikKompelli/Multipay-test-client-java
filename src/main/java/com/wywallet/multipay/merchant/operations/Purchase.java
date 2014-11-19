package com.wywallet.multipay.merchant.operations;

import com.wywallet.multipay.merchant.JSONInterfaceTranslator;
import com.wywallet.multipay.merchant.Sha1Hash;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 4t-johoi on 03/11/14.
 */
public class Purchase extends Operation {

    private final String displayMode;
    private final String serviceURL;
    private String sharedSecret;
    private String merchantId;
    private String msisdn;
    private int amount;
    private int vat;
    private String vatFormat;
    private String currency;
    private boolean isImmediate;
    private Boolean useDeliveryAddress;
    private String description;
    private String orderId;
    private String transmissionTime;
    private String cancelURL;
    private String returnURL;
    private String postbackURL;
    private String errorMessage;
    private String resultJson;
    private String iframeURL;
    private String standaloneURL;

    public Purchase(String msisdn, int amount, int vat, String vatFormat, String currency, boolean isImmediate,
                    Boolean useDeliveryAddress, String description, String orderId, String transmissionTime,
                    String cancelURL, String returnURL, String postbackURL, String merchantId, String sharedSecret, String displayMode, String serviceURL) {

        this.msisdn = msisdn;
        this.amount = amount;
        this.vat = vat;
        this.vatFormat = vatFormat;
        this.currency = currency;
        this.isImmediate = isImmediate;
        this.useDeliveryAddress = useDeliveryAddress;
        this.description = description;
        this.orderId = orderId;
        this.transmissionTime = transmissionTime;
        this.cancelURL = cancelURL;
        this.returnURL = returnURL;
        this.postbackURL = postbackURL;
        this.merchantId = merchantId;
        this.sharedSecret = sharedSecret;
        this.displayMode = displayMode;
        this.serviceURL = serviceURL;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append(System.lineSeparator() + "  \"merchantId\" : \"" + merchantId + "\",");
        sb.append(System.lineSeparator() + "  \"transmissionTime\" : \"" + transmissionTime + "\",");
        sb.append(System.lineSeparator() + "  \"transaction\": {");
        sb.append(System.lineSeparator() + "    \"merchantOrderId\" : \"" + orderId + "\",");
        sb.append(System.lineSeparator() + "    \"isImmediate\" : \"" + isImmediate + "\",");
        sb.append(System.lineSeparator() + "    \"amount\" : \"" + amount + "\",");
        sb.append(System.lineSeparator() + "    \"vat\" : \"" + vat + "\",");
        sb.append(System.lineSeparator() + "    \"vatFormat\" : \"" + vatFormat + "\",");
        sb.append(System.lineSeparator() + "    \"currency\" : \"" + currency + "\",");
        sb.append(System.lineSeparator() + "    \"description\" : \"" + description + "\"");
        sb.append(System.lineSeparator() + "  }");
        if (msisdn != null && !msisdn.equals("")) {
            sb.append("," + System.lineSeparator() + "  \"msisdn\" : \"" + msisdn + "\"");
        }
        if (returnURL != null && !returnURL.equals("")) {
            sb.append("," + System.lineSeparator() + "  \"returnUrl\" : \"" + returnURL + "\"");
        }
        if (postbackURL != null && !postbackURL.equals("")) {
            sb.append("," + System.lineSeparator() + "  \"postbackUrl\" : \"" + postbackURL + "\"");
        }
        if (cancelURL != null && !cancelURL.equals("")) {
            sb.append("," + System.lineSeparator() + "  \"cancelUrl\" : \"" + cancelURL + "\"");
        }
        if (useDeliveryAddress != null) {
            sb.append("," + System.lineSeparator() + "  \"useDeliveryAddress\" : \"" + useDeliveryAddress + "\"");
        }
        sb.append(System.lineSeparator() + "}");
        return sb.toString();
    }

    public String toJsonString() {
        // TODO use gson
        return toString();
    }


    public String calculateMac() {
        Sha1Hash sha1HashCalculator = new Sha1Hash();
        StringBuffer sb = new StringBuffer();
        sb.append(merchantId);
        sb.append(transmissionTime);
        sb.append(orderId);
        sb.append(isImmediate);
        sb.append(amount);
        sb.append(vat);
        sb.append(vatFormat);
        sb.append(currency);
        sb.append(description);
        sb.append(sharedSecret);
        return (sha1HashCalculator.getHashedValue(sb.toString()));
    }


    @Override
    public void execute() throws IOException {
        String purchaseURLSuffix = "/payments/";
        String actualURL = serviceURL + purchaseURLSuffix;
        HttpResponse httpResponse = JSONInterfaceTranslator.postToURL(this, actualURL);
        HttpEntity entity = httpResponse.getEntity();

        String responseString = EntityUtils.toString(entity, "UTF-8");
        setResultJson(responseString);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 401 || statusCode == 404) {
            setErrorMessage("Something went wrong with the server request to URL " + actualURL + httpResponse.getStatusLine().getStatusCode() + ". Message: " + httpResponse.getStatusLine().getReasonPhrase() + ".");
        } else {
            if (displayMode.equals("cashier-iframe")) {
                setIframeURL(JSONInterfaceTranslator.getIframeURI(responseString));
            } else if (displayMode.equals("cashier-standalone")) {
                setStandaloneURL(JSONInterfaceTranslator.getStandaloneURL(responseString));
            }
        }
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

    public void setIframeURL(String iframeURL) {
        this.iframeURL = iframeURL;
    }

    public String getFrameURL() {
        return iframeURL;
    }

    public String getStandaloneURL() {
        return standaloneURL;
    }

    public void setStandaloneURL(String standaloneURL) {
        this.standaloneURL = standaloneURL;
    }
}
