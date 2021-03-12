package utility;


import constants.Constants;
import library.SessionManager;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBUtility {

    private String dbUrl;
    private String username;
    private String password;
    private Connection connect;

    private LogUtility logUtility = new LogUtility(DBUtility.class);

    public DBUtility(SessionManager sessionManager) {
        try {
            logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
            Properties environmentProperties = PropertyFileUtility.propertyFile("");
            dbUrl = environmentProperties.getProperty("dbUrl");
            username = environmentProperties.getProperty("username");
            password = environmentProperties.getProperty("password");
            connect = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }

    public void closeDbConnection() {
        try {
            connect.close();
            logUtility.logDebug("DB connection is closed.");
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        Statement statement = null;
        try {
            statement = connect.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return rs;
    }

    public void updateQuery(String query) {
        int rs = 0;
        Statement statement = null;
        try {
            statement = connect.createStatement();
            rs = statement.executeUpdate(query);
        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        logUtility.logDebug("Number of rows affected- " + rs);
    }

    public int getMakeId(String makeName) {
        int makeId = 0;
        try {
            String sqlQuery = "SELECT make_id  FROM `make` WHERE `make_name` LIKE  '" + makeName.toUpperCase() + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                makeId = rs.getInt("make_id");
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return makeId;
    }

    public int getModelId(String modelName) {
        int modelId = 0;
        try {
            String sqlQuery = "SELECT model_id FROM model WHERE model_name= '" + modelName.toUpperCase() + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                modelId = rs.getInt("model_id");
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return modelId;
    }

    public int getVariantId(String variantName, String modelName) {
        int variantId = 0;
        try {
            String sqlQuery = "SELECT variant_id FROM variant where variant_name='" + variantName.toUpperCase() + "' and model_id=" + getModelId(modelName);
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                variantId = rs.getInt("variant_id");
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return variantId;
    }

    public String getUserToken(String dealerEmail) {
        String userToken = null;
        try {
            String sqlQuery = "SELECT user_token FROM `app_user_token` WHERE IsActiveDevice = 1 AND is_logged_in = 1 AND `user_email` LIKE '" + dealerEmail + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                userToken = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return userToken;
    }

    public String getAnyToken(String dealerEmail) {
        String userToken = null;
        try {
            String sqlQuery = "SELECT user_token FROM `app_user_token` WHERE `user_email` LIKE '" + dealerEmail + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                userToken = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return userToken;
    }

    //NOTE: This will work only if the dealer has logged in at least once so that its token is created.
    public String getDealerId(String dealerEmail) {
        String dealerId = null;
        try {
            String sqlQuery = "SELECT fk_dealer_id FROM `app_user_token` WHERE `user_email` LIKE '" + dealerEmail + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                dealerId = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return dealerId;
    }

    public List<String> getActiveUserTokenIds(String dealerEmail) {
        List<String> activeUserTokenIds = new ArrayList<>();
        try {
            String sqlQuery = "SELECT app_user_token_id FROM `app_user_token` WHERE IsActiveDevice = 1 AND is_logged_in = 1 AND `user_email` LIKE '" + dealerEmail + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                activeUserTokenIds.add(rs.getString(1));
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return activeUserTokenIds;
    }

    public List<String> getPushTokenIds(List<String> userTokenIds) {
        List<String> activeUserTokenIds = new ArrayList<>();
        try {
            for(String userTokenId: userTokenIds) {
                String sqlQuery = "SELECT id_app_push_token FROM `app_user_push_token` WHERE id_app_user_token LIKE '" + userTokenId + "'";
                ResultSet rs = executeQuery(sqlQuery);
                while (rs.next()) {
                    activeUserTokenIds.add(rs.getString(1));
                }
                closeResultSet(rs);
            }
        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return activeUserTokenIds;
    }

    public List<String> getPushTokens(List<String> pushTokenIds) {
        List<String> pushTokens = new ArrayList<>();
        try {
            for (String pushTokenId: pushTokenIds) {
                String sqlQuery = "SELECT push_token FROM `app_push_token` WHERE id_app_push_token LIKE '" + pushTokenId + "'";
                ResultSet rs = executeQuery(sqlQuery);
                while (rs.next()) {
                    pushTokens.add(rs.getString(1));
                }
                closeResultSet(rs);
            }
        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return pushTokens;
    }

    public void updatePushTokens(List<String> pushTokenIds, List<String> pushTokens) {
        pushTokenIds.forEach(pushTokenId -> {
            String pushToken = pushTokens.get(pushTokenIds.indexOf(pushTokenId));
            String updatePushTokenQuery = "UPDATE app_push_token set push_token = '" + pushToken + "' where id_app_push_token='" + pushTokenId + "'";
            updateQuery(updatePushTokenQuery);
        });
    }

    public String getDealerName(String dealerEmail) {

        String dealerName = null;
        try {
            String sqlQuery = "SELECT dealer_name FROM `dealer` WHERE `email` LIKE '" + dealerEmail + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                dealerName = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return dealerName;
    }

    public String getDealerGroupId(String dealerName) {

        String dealerGroupId = null;
        try {
            String sqlQuery = "SELECT dealers_group_id FROM `dealer` WHERE dealer_name LIKE '" + dealerName + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                dealerGroupId = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return dealerGroupId;
    }

    public String getProcurementGroupId(String dealerGroupId) {

        String dealerProcurementGroupId = null;
        try {
            String sqlQuery = "SELECT procurement_associate_id FROM `dealers_group` WHERE `dealers_group_id` = '" + dealerGroupId + "'";
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                dealerProcurementGroupId = rs.getString(1);
            }
            closeResultSet(rs);

        } catch (SQLException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return dealerProcurementGroupId;
    }


    public void removeAllClusters(String dealerProcurementId) {
        String sqlQuery = "DELETE FROM user_clusters WHERE user_id = '" + dealerProcurementId + "'";
        updateQuery(sqlQuery);
    }

    public void endAuction(String date, String carId) {
        String sqlQuery = "UPDATE app_auction SET auction_end_time='" + date + "' WHERE auction_status='1' AND carId='" + carId + "'";
        updateQuery(sqlQuery);
        JavaUtility.sleep(15);
    }
    
    public void updateCarInspectionDate(String carId) {
        String timeStamp = JavaUtility.getCurrentDateTime();
        String sqlQuery = "UPDATE orders SET last_inspection_date='" + timeStamp + "' where car_id=" + carId;
        updateQuery(sqlQuery);
    }

    public String getInspectionStatusID(String appointmentId) {
         String status_id = null;
         try {
             String sqlQuery = "SELECT status_id FROM orders WHERE lead_id=" + appointmentId ;
             ResultSet rs = executeQuery(sqlQuery);
             while (rs.next()) {
                 status_id = rs.getString("status_id");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return status_id;
    }

    public String getDealerCode(String dealerEmail) {
        String dealerCode = null;
        try {
            String sqlQuery = "SELECT dealer_code FROM dealer WHERE email='" + dealerEmail + "';" ;
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                dealerCode = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dealerCode;
    }

    public String getLastBidTransactionId(String appointmentId) {
        String lastBidTransactionId = null;
        try {
            String sqlQuery = "SELECT id_app_bid FROM app_bid WHERE appointment_id=" + " " + appointmentId + " ORDER BY id_app_bid DESC LIMIT 1" ;
            ResultSet rs = executeQuery(sqlQuery);
            while (rs.next()) {
                lastBidTransactionId = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastBidTransactionId;
    }

}
