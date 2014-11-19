package com.wywallet.multipay.merchant; /**
 * Created by 4t-johoi on 30/10/14.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wywallet.multipay.merchant.operations.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PurchaseServlet extends HttpServlet {

    private Boolean useDeliveryAddress;
    private String transmissionTime;
    private String currency;
    private String vatFormat;
    private int vatAmount;
    private String orderId;
    private String description;
    private boolean isImmediate;
    private int amount;
    private String msisdn;
    private String merchantId;
    private String sharedSecret;
    private String cancelURL;
    private String returnURL;
    private String postbackURL;
    private String displayMode;
    private String serviceUrl;
    private String transactionId;
    private String paymentId;
    private boolean isLastCapture;
    private String cancelId;
    private String refundId;
    private String captureId;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException {
        response.setContentType("text/html");
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException ioe) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ioe.printStackTrace(pw);
            String stackTrace = sw.toString();
            System.out.println("Server threw error: " + stackTrace);
            return;
        }
        out.println("<!DOCTYPE html><html><title>Multipay Merchant Java Reference Implementation</title>" +
                "<body bgcolor=FFFFFF>");

        out.println("<h2>Multipay Merchant Java Reference Implementation</h2>");

        populateUserInput(request);

        out.println("<FORM METHOD=\"POST\" ACTION=\"./com.wywallet.multipay.merchant.PurchaseServlet\">");
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationButtonClicked\" value=\"true\">");
        String operationDropdown = request.getParameter("operationDropdown");

        out.println("Choose operation \n" +
                "<select name=\"operationDropdown\">\n" +
                "  <option value=\"Purchase\" " + ((operationDropdown != null && operationDropdown.equals("Purchase")) ? "selected" : "") + ">Purchase</option>\n" +
                "  <option value=\"Check Transaction\" " + ((operationDropdown != null && operationDropdown.equals("Check Transaction")) ? "selected" : "") + ">Get Transaction</option>\n" +
                "  <option value=\"Check Payment\" " + ((operationDropdown != null && operationDropdown.equals("Check Payment")) ? "selected" : "") + ">Get Payment</option>\n" +
                "  <option value=\"Cancel\" " + ((operationDropdown != null && operationDropdown.equals("Cancel")) ? "selected" : "") + ">Cancel</option>\n" +
                "  <option value=\"Capture\" " + ((operationDropdown != null && operationDropdown.equals("Capture")) ? "selected" : "") + ">Capture</option>\n" +
                "  <option value=\"Refund\" " + ((operationDropdown != null && operationDropdown.equals("Refund")) ? "selected" : "") + ">Refund</option>\n" +
                "  <option value=\"Get Capture\" " + ((operationDropdown != null && operationDropdown.equals("Get Capture")) ? "selected" : "") + ">Get Capture</option>\n" +
                "  <option value=\"Get Cancel\" " + ((operationDropdown != null && operationDropdown.equals("Get Cancel")) ? "selected" : "") + ">Get Cancel</option>\n" +
                "  <option value=\"Get Refund\" " + ((operationDropdown != null && operationDropdown.equals("Get Refund")) ? "selected" : "") + ">Get Refund</option>\n" +
                "</select>" +
                "<br>");

        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Select Operation\"></FORM>");

        out.println("<FORM METHOD=\"POST\" ACTION=\"./com.wywallet.multipay.merchant.PurchaseServlet\">");
        out.println("<INPUT TYPE=\"hidden\" NAME=\"executionButtonClicked\" value=\"true\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">merchantId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"merchantId\" SIZE=12 value=" + merchantId + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">sharedSecret:</td><td><INPUT TYPE=\"TEXT\" NAME=\"sharedSecret\" SIZE=12 value=" + sharedSecret + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">serviceUrl:</td><td><INPUT TYPE=\"TEXT\" NAME=\"serviceUrl\" SIZE=12 value=" + serviceUrl + "> <br></td></tr>");
        out.println("</table></td><td><table>");
        out.println("</table></td></tr></table>");


        if (operationDropdown != null && operationDropdown.equals("Purchase")) {
            purchase(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Check Transaction")) {
            checkTransaction(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Check Payment")) {
            checkPayment(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Cancel")) {
            cancelPayment(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Refund")) {
            refundPayment(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Capture")) {
            capturePayment(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Get Capture")) {
            getCapture(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Get Cancel")) {
            getCancel(request, out);
        } else if (operationDropdown != null && operationDropdown.equals("Get Refund")) {
            getRefund(request, out);
        } else {
            String operationButtonClicked = request.getParameter("operationButtonClicked");
            if (operationButtonClicked != null && operationButtonClicked.equals("true")) {
                out.println("Operation not supported: " + operationDropdown);
            }

        }
        out.println("<br><a href=" + response.encodeURL(request.getContextPath()) + ">Reset</a>");

        out.println("</body>");
        out.println("</html>");
        out.close();

    }

    private void getRefund(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Get Refund\">");
        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">refundId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"refundId\" SIZE=35 value=" + refundId + "> <br></td></tr>");
        out.println("</table></td><td><table>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (refundId != null && !refundId.equals("")) {
                try {
                    GetRefund getRefund = new GetRefund(refundId, sharedSecret, serviceUrl);
                    getRefund.execute();

                    printRequestResponseFields(out, getRefund);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("refundId required as input");
            }
        }
    }


    private void getCancel(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Get Cancel\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">cancelId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"cancelId\" SIZE=35 value=" + cancelId + "> <br></td></tr>");
        out.println("</table></td><td><table>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (cancelId != null && !cancelId.equals("")) {
                try {
                    GetCancel getCancel = new GetCancel(cancelId, sharedSecret, serviceUrl);
                    getCancel.execute();

                    printRequestResponseFields(out, getCancel);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("cancelId required as input");
            }
        }
    }

    private void getCapture(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Get Capture\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">captureId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"captureId\" SIZE=35 value=" + captureId + "> <br></td></tr>");
        out.println("</table></td><td><table>");
        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (captureId != null && !captureId.equals("")) {
                try {
                    GetCapture getCapture = new GetCapture(captureId, sharedSecret, serviceUrl);
                    getCapture.execute();

                    printRequestResponseFields(out, getCapture);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("captureId required as input");
            }
        }
    }

    private void refundPayment(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Refund\">");
        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">transactionId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transactionId\" SIZE=35 value=" + transactionId + "> <br></td></tr>");
        addAmountFields(out);
        out.println("</table></td><td><table>");
        out.println("<tr><td style=\"width:150px\">description:</td><td><INPUT TYPE=\"TEXT\" NAME=\"description\" SIZE=35 value='" + description + "'> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">transmissionTime:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transmissionTime\" SIZE=35 value='" + transmissionTime + "'> <br></td></tr>");
        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (transactionId != null && !transactionId.equals("")) {
                try {
                    RefundPayment refundPayment = new RefundPayment(transactionId, description, transmissionTime, amount, vatAmount, vatFormat, sharedSecret, serviceUrl);
                    refundPayment.execute();

                    printRequestResponseFields(out, refundPayment);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("TransactionId required as input");
            }
        }
    }

    private void capturePayment(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Capture\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">transactionId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transactionId\" SIZE=35 value=" + transactionId + "> <br></td></tr>");
        addAmountFields(out);
        out.println("</table></td><td><table>");
        out.println("<tr><td style=\"width:150px\">transmissionTime:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transmissionTime\" SIZE=35 value='" + transmissionTime + "'> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">isLastCapture:</td><td>" +
                "<INPUT TYPE=\"radio\" NAME=\"isLastCapture\" SIZE=12 value=\"false\" " +
                (!isLastCapture ? "checked" : "") + "> false " +
                "<INPUT TYPE=\"radio\" NAME=\"isLastCapture\" SIZE=12 value=\"true\" " +
                (isLastCapture ? "checked" : "") + "> true " +
                "<br></td></tr>");
        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (transactionId != null && !transactionId.equals("")) {
                try {
                    CapturePayment capturePayment = new CapturePayment(transactionId, transmissionTime, isLastCapture, amount, vatAmount, vatFormat, sharedSecret, serviceUrl);
                    capturePayment.execute();

                    printRequestResponseFields(out, capturePayment);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("TransactionId required as input");
            }
        }
    }


    private void cancelPayment(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Cancel\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">transactionId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transactionId\" SIZE=35 value=" + transactionId + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">transmissionTime:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transmissionTime\" SIZE=35 value='" + transmissionTime + "'> <br></td></tr>");
        out.println("</table></td><td><table>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (transactionId != null && !transactionId.equals("")) {
                try {
                    CancelPayment cancelPayment = new CancelPayment(transactionId, transmissionTime, sharedSecret, serviceUrl);
                    cancelPayment.execute();

                    printRequestResponseFields(out, cancelPayment);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("TransactionId required as input");
            }
        }
    }

    private void purchase(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Purchase\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">MSISDN:</td><td><INPUT TYPE=\"TEXT\" NAME=\"MSISDN\" SIZE=12 value=" + msisdn + "> <br></td></tr>");
        addAmountFields(out);
        out.println("<tr><td style=\"width:150px\">currency:</td><td><INPUT TYPE=\"TEXT\" NAME=\"currency\" SIZE=12 value=" + currency + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">isImmediate:</td><td>" +
                "<INPUT TYPE=\"radio\" NAME=\"isImmediate\" SIZE=12 value=\"false\" " +
                (!isImmediate ? "checked" : "") + "> false " +
                "<INPUT TYPE=\"radio\" NAME=\"isImmediate\" SIZE=12 value=\"true\" " +
                (isImmediate ? "checked" : "") + "> true " +
                "<br></td></tr>");
        out.println("<tr><td style=\"width:150px\">useDeliveryAddress:</td><td>" +
                "<INPUT TYPE=\"radio\" NAME=\"useDeliveryAddress\" SIZE=12 value=\"false\" " +
                (useDeliveryAddress != null && !useDeliveryAddress ? "checked" : "") + "> false " +
                "<INPUT TYPE=\"radio\" NAME=\"useDeliveryAddress\" SIZE=12 value=\"true\" " +
                (useDeliveryAddress != null && useDeliveryAddress ? "checked" : "") + "> true " +
                "<br>" + "</td></tr>");
        out.println("</table></td><td><table>");
        out.println("<tr><td style=\"width:150px\">description:</td><td><INPUT TYPE=\"TEXT\" NAME=\"description\" SIZE=35 value='" + description + "'> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">orderId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"orderId\" SIZE=35 value=" + orderId + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">transmissionTime:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transmissionTime\" SIZE=35 value='" + transmissionTime + "'> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">cancelURL:</td><td><INPUT TYPE=\"TEXT\" NAME=\"cancelURL\" SIZE=35 value=" + cancelURL + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">returnURL:</td><td><INPUT TYPE=\"TEXT\" NAME=\"returnURL\" SIZE=35 value=" + returnURL + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">postbackURL:</td><td><INPUT TYPE=\"TEXT\" NAME=\"postbackURL\" SIZE=35 value=" + postbackURL + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">displayMode:</td><td><INPUT TYPE=\"TEXT\" NAME=\"displayMode\" SIZE=35 value=" + displayMode + "> <br></td></tr>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            try {
                Purchase purchase = new Purchase(msisdn, amount, vatAmount, vatFormat, currency, isImmediate,
                        useDeliveryAddress, description, orderId, transmissionTime, cancelURL, returnURL, postbackURL, merchantId, sharedSecret, displayMode, serviceUrl);
                purchase.execute();

                if (displayMode.equals("cashier-iframe")) {
                    out.println("<iframe src=\"" + purchase.getFrameURL() + "\" id=\"ww-kassan-iframe\" " +
                            "name=\"ww-kassan-iframe\" class=\"loaded\" frameborder=\"0\" scrolling=\"no\"\n" +
                            "      style=\"width: 450px; min-height:375px;\n" +
                            "      -webkit-transition: min-height 0.15s; transition: min-height 0.15s;\"></iframe>\n");
                } else if (displayMode.equals("cashier-standalone")) {
                    out.println("<a href=" + purchase.getStandaloneURL() + "\">Launch standalone purchase URL!</a>");
                } else {
                    out.println("<br> unsupported display mode: " + displayMode);
                }

                printRequestResponseFields(out, purchase);

            } catch (IOException ioe) {
                handleException(out, ioe);
            }
        }
    }

    private void checkTransaction(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Check Transaction\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">transactionId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"transactionId\" SIZE=35 value=" + transactionId + "> <br></td></tr>");
        out.println("</table></td><td><table>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (transactionId != null && !transactionId.equals("")) {
                try {
                    CheckTransaction checkTransaction = new CheckTransaction(transactionId, sharedSecret, serviceUrl);
                    checkTransaction.execute();

                    printRequestResponseFields(out, checkTransaction);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("TransactionId required as input");
            }
        }
    }

    private void printRequestResponseFields(PrintWriter out, Operation checkTransaction) {
        out.println("<h3>Request JSON</h3>");
        printJsonInTextArea(out, checkTransaction.toString());

        String errorMessage = checkTransaction.getErrorMessage();
        if (!errorMessage.equals("") && errorMessage != null) {
            out.println("<br>" + errorMessage);
            if (!checkTransaction.getJsonResult().equals("")) {
                out.println("<h3>Response JSON</h3>");
                printJsonInTextArea(out, checkTransaction.getJsonResult());
            }
        } else {
            out.println("<h3>Response JSON</h3>");
            printJsonInTextArea(out, checkTransaction.getJsonResult());
        }
    }


    private void checkPayment(HttpServletRequest request, PrintWriter out) {
        out.println("<INPUT TYPE=\"hidden\" NAME=\"operationDropdown\" value=\"Check Payment\">");

        out.println("<br><table><tr><td style=\"width:305px\"><table>");
        out.println("<tr><td style=\"width:150px\">paymentId:</td><td><INPUT TYPE=\"TEXT\" NAME=\"paymentId\" SIZE=35 value=" + paymentId + "> <br></td></tr>");
        out.println("</table></td><td><table>");

        out.println("</table></td></tr></table>");
        out.println("<P><INPUT TYPE=\"SUBMIT\" VALUE=\"Execute\"></FORM>");

        String executionButtonClicked = request.getParameter("executionButtonClicked");
        if (executionButtonClicked != null && executionButtonClicked.equals("true")) {
            if (paymentId != null && !paymentId.equals("")) {
                try {
                    CheckPayment checkPayment = new CheckPayment(paymentId, sharedSecret, serviceUrl);
                    checkPayment.execute();

                    printRequestResponseFields(out, checkPayment);
                } catch (IOException ioe) {
                    handleException(out, ioe);
                }
            } else {
                out.println("PaymentId required as input");
            }
        }
    }


    private void populateUserInput(HttpServletRequest request) {
        msisdn = (request.getParameter("MSISDN") != null) ? request.getParameter("MSISDN") : "\"\"";
        amount = (request.getParameter("AMOUNT") != null) ? Integer.valueOf(request.getParameter("AMOUNT")) : 1;

        vatAmount = (request.getParameter("vat") != null) ? Integer.valueOf(request.getParameter("vat")) : 0;
        vatFormat = (request.getParameter("vatFormat") != null) ? request.getParameter("vatFormat") : "PERCENT";
        currency = (request.getParameter("currency") != null) ? request.getParameter("currency") : "SEK";
        isImmediate = (request.getParameter("isImmediate") != null) ? Boolean.valueOf(request.getParameter("isImmediate")) : true;
        useDeliveryAddress = (request.getParameter("useDeliveryAddress") != null) ? Boolean.valueOf(request.getParameter("useDeliveryAddress")) : null;
        description = (request.getParameter("description") != null) ? request.getParameter("description") : "Multipay Merchant Java Reference Implementation";
        orderId = (request.getParameter("orderId") != null) ? request.getParameter("orderId") : "111-222-333";

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);

        transmissionTime = (request.getParameter("transmissionTime") != null) ? request.getParameter("transmissionTime") : now.format(formatter);

        cancelURL = (request.getParameter("cancelURL") != null) ? request.getParameter("cancelURL") : "\"\"";
        returnURL = (request.getParameter("returnURL") != null) ? request.getParameter("returnURL") : "\"\"";
        postbackURL = (request.getParameter("postbackURL") != null) ? request.getParameter("postbackURL") : "\"\"";
        displayMode = (request.getParameter("displayMode") != null) ? request.getParameter("displayMode") : "cashier-iframe";

        merchantId = (request.getParameter("merchantId") != null) ? request.getParameter("merchantId") : ""; //Stage
        sharedSecret = (request.getParameter("sharedSecret") != null) ? request.getParameter("sharedSecret") : ""; //Stage
        serviceUrl = (request.getParameter("serviceUrl") != null) ? request.getParameter("serviceUrl") : ""; //Stage

        transactionId = (request.getParameter("transactionId") != null) ? request.getParameter("transactionId") : "\"\"";
        paymentId = (request.getParameter("paymentId") != null) ? request.getParameter("paymentId") : "\"\"";
        captureId = (request.getParameter("captureId") != null) ? request.getParameter("captureId") : "\"\"";
        cancelId = (request.getParameter("cancelId") != null) ? request.getParameter("cancelId") : "\"\"";
        refundId = (request.getParameter("refundId") != null) ? request.getParameter("refundId") : "\"\"";

        isLastCapture = (request.getParameter("isLastCapture ") != null) ? Boolean.valueOf(request.getParameter("isLastCapture ")) : false;
    }


    private void printJsonInTextArea(PrintWriter out, String json) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);

        out.println("<textarea style=\"resize:none;\" rows=\"20\" cols=\"80\" readonly>" + gson.toJson(je) + "</textarea>");
    }


    private void addAmountFields(PrintWriter out) {
        out.println("<tr><td style=\"width:150px\">Amount:</td><td><INPUT TYPE=\"TEXT\" NAME=\"AMOUNT\" SIZE=12 value=" + amount + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">vat:</td><td><INPUT TYPE=\"TEXT\" NAME=\"vat\" SIZE=12 value=" + vatAmount + "> <br></td></tr>");
        out.println("<tr><td style=\"width:150px\">vatFormat:</td><td><INPUT TYPE=\"TEXT\" NAME=\"vatFormat\" SIZE=12 value=" + vatFormat + "> <br></td></tr>");
    }


    private void handleException(PrintWriter out, IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ioe.printStackTrace(pw);
        String stackTrace = sw.toString();
        out.println("Server threw error: <br>" + stackTrace);
    }
}

