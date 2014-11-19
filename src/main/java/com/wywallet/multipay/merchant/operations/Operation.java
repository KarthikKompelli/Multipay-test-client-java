package com.wywallet.multipay.merchant.operations;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 4t-johoi on 11/11/14.
 */
public abstract class Operation {

    public abstract void execute() throws IOException;

    public abstract String toJsonString();

    public abstract String calculateMac();

    public abstract void setResultJson(String resultJson);

    public abstract String getJsonResult();

    public abstract String getErrorMessage();

    public abstract void setErrorMessage(String errorMessage);

    public void handleResponse(String actualURL, HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        setResultJson(responseString);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 401 || statusCode == 404 || statusCode == 201) {
            setErrorMessage("Something went wrong with the server request to URL " + actualURL +
                    httpResponse.getStatusLine().getStatusCode() +
                    ". Message: " + httpResponse.getStatusLine().getReasonPhrase() + ".");
        }
    }
}
