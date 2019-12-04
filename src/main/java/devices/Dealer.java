package devices;

import com.mongodb.util.JSON;
import constants.Constants;
import constants.Defaults;
import library.SessionManager;
import org.json.simple.JSONObject;
import utility.ApiUtility;
import utility.DBUtility;
import utility.LogUtility;
import utility.PropertyFileUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Dealer {

    private String dealerEmail;

    private String dealerPassword;

    private String dealerType;

    private String dealerName;

    private String dealerCode;

    private String dealerId;

    private String dealerGroupId;

    private String dealerProcurementId;

    private ApiUtility apiUtility;

    private LogUtility logUtility;

    private Properties environmentProperties = PropertyFileUtility.propertyFile(Constants.ENVIRONMENT_PROPERTIES_PATH);
    private Properties slugProperties = PropertyFileUtility.propertyFile(Constants.CAR_CREATION_FLOW_RESOURCES + Constants.slash + "data" + Constants.slash + "slugs.properties");

    public Dealer(String dealerType, SessionManager sessionManager) {
        DBUtility dbUtility = new DBUtility(sessionManager);
        apiUtility = new ApiUtility(sessionManager);
        logUtility = new LogUtility(Dealer.class);

        this.dealerEmail = environmentProperties.getProperty(dealerType);
        this.dealerPassword = Defaults.PASSWORD;
        this.dealerType = dealerType;
        this.dealerId = dbUtility.getDealerId(dealerEmail);

        JSONObject dealerInfoJson = getDealerInfoFromAdminPanel(dealerId);
        this.dealerCode = (String) dealerInfoJson.get("dealerCode");
        this.dealerName = (String) dealerInfoJson.get("dealerName");

        dbUtility.closeDbConnection();
    }

    public JSONObject getDealerInfoFromAdminPanel(String dealerId) {
        String dealerInfoUrl = environmentProperties.get("adminPanelBaseUrl") + slugProperties.getProperty("dealerInfo") + dealerId;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");

        JSONObject dealerInfoResponseJson = apiUtility.convertResponseToJsonObject(apiUtility.sendRequest("get", dealerInfoUrl, headers));
        logUtility.logDebug("Get dealer info from admin panel");
        logUtility.logDebug("Request-");
        logUtility.logDebug(dealerInfoUrl);
        logUtility.logDebug("Response-");
        logUtility.logDebug(dealerInfoResponseJson.toJSONString());
        JSONObject dealerInfoResponseDetailsJson = (JSONObject) dealerInfoResponseJson.get("detail");

        return dealerInfoResponseDetailsJson;
    }

    public Dealer(String dealerEmail, String dealerPassword, SessionManager sessionManager) {
        DBUtility dbUtility = new DBUtility(sessionManager);

        this.dealerEmail = dealerEmail;
        this.dealerPassword = dealerPassword;
        this.dealerType = dealerEmail;
        this.dealerCode = dbUtility.getDealerCode(dealerEmail);
        this.dealerName = dbUtility.getDealerName(dealerEmail);
        this.dealerGroupId = dbUtility.getDealerGroupId(dealerName);
        this.dealerProcurementId = dbUtility.getProcurementGroupId(dealerGroupId);
    }

    public String getDealerEmail() {
        return dealerEmail;
    }

    public String getDealerPassword() {
        return dealerPassword;
    }

    public String getDealerType() {
        return dealerType;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public String getDealerId() {
        return dealerId;
    }

    public String getDealerGroupId() {
        return dealerGroupId;
    }

    public String getDealerProcurementId() {
        return dealerProcurementId;
    }

    public boolean isUnnatiDealer() {
        boolean isUnnatiDealer = false;
        if(dealerType.equalsIgnoreCase(Constants.NORMAL_UNNATI_DEALER) || dealerType.equalsIgnoreCase(Constants.HUNDRED_PERCENT_UNNATI_DEALER)){
            isUnnatiDealer = true;
        }
        return isUnnatiDealer;
    }

    public boolean isOutstationDealer() {
        boolean iOutstationDealer = false;
        if(dealerType.equalsIgnoreCase(Constants.NON_UNNATI_OUTSTATION_DEALER)){
            iOutstationDealer = true;
        }
        return iOutstationDealer;
    }

    public boolean isOcbDealer() {
        boolean isOcbDealer = false;
        if(dealerType.equalsIgnoreCase(Constants.OCB_DEALER) || dealerType.equalsIgnoreCase(Constants.INTERNAL_DEALER)) {
            isOcbDealer = true;
        }
        return isOcbDealer;
     }
}
