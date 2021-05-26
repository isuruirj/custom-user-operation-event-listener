package org.wso2.carbon.custom.user.operation.event.listener;

import org.wso2.carbon.custom.user.operation.event.listener.internal.ServiceComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserRealmService;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserOperationEventListener;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import java.util.*;

/**
 *
 */
public class CustomClaimUserOperationEventListener extends AbstractUserOperationEventListener {

    private static final String APPLICATION_PERMISSION_NODE = "/permission/applications/";
    private static final String DEFAULT_PERMISSION_SEPARATOR = "/";
    private static final String ABB_PERMISSION_SEPARATOR = ":";
    private static final String ENTITLEMENTS_CLAIM = "http://wso2.org/claims/entitlements";
    private static Log log = LogFactory.getLog(CustomClaimUserOperationEventListener.class);

    @Override
    public int getExecutionOrderId() {

        return 1356;
    }

    @Override
    public boolean doPostGetUserClaimValueWithID(String userID, String claim, List<String> claimValue,
                                                 String profileName, UserStoreManager userStoreManager)
            throws UserStoreException {

        Set<String> permissionSet = new HashSet<String>();
        StringBuilder permissionString = new StringBuilder();
        String userName = getUserNameFromUserID(userID, (AbstractUserStoreManager) userStoreManager);
        UserRealmService userRealmService = ServiceComponent.getRealmService();
        UserRealm userRealm = ((RealmService) userRealmService).getUserRealm(userStoreManager.getRealmConfiguration());

        try {
            String[] permissionList = userRealm.getAuthorizationManager().getAllowedUIResourcesForUser(userName, APPLICATION_PERMISSION_NODE);
            if (log.isDebugEnabled()) {
                log.debug("Permissions are allowed for user : " + userName + " & list : "
                        + Arrays.toString(permissionList));
            }
            for (String permission : permissionList) {
                if (permission.startsWith(APPLICATION_PERMISSION_NODE)) {
                    String spPermission = permission.substring(APPLICATION_PERMISSION_NODE.lastIndexOf("s") + 2);
                    if (spPermission.contains(DEFAULT_PERMISSION_SEPARATOR)) {
                        spPermission = spPermission.replace(DEFAULT_PERMISSION_SEPARATOR, ABB_PERMISSION_SEPARATOR);
                        permissionSet.add(spPermission);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Retrieving all the permission of the application : " + spPermission);
                        }
                        /*
                        to-do
                         */
                    }
                }
            }
            for (String permission : permissionSet) {
                if (permissionString.toString().equals("")) {
                    permissionString = new StringBuilder(permission);
                } else {
                    permissionString.append(",").append(permission);
                }
            }
        } catch (Exception e) {
            log.error("Error while retrieving user claims: ", e);
        }

        claimValue.add(permissionString.toString());

        if (log.isDebugEnabled()) {
            log.debug("Permission String " + permissionString.toString() + " for user " + userName);
        }

        return true;
    }

    @Override
    public boolean doPostGetUserClaimValuesWithID(String userID, String[] claims, String profileName,
                                                  Map<String, String> claimMap, UserStoreManager userStoreManager)
            throws UserStoreException {

        Set<String> permissionSet = new HashSet<String>();
        StringBuilder permissionString = new StringBuilder();
        String userName = getUserNameFromUserID(userID, (AbstractUserStoreManager) userStoreManager);
        UserRealmService userRealmService = ServiceComponent.getRealmService();
        UserRealm userRealm = ((RealmService) userRealmService).getUserRealm(userStoreManager.getRealmConfiguration());

        try {
            String[] permissionList = userRealm.getAuthorizationManager().getAllowedUIResourcesForUser(userName, APPLICATION_PERMISSION_NODE);
            if (log.isDebugEnabled()) {
                log.debug("Permissions are allowed for user : " + userName + " & list : "
                        + Arrays.toString(permissionList));
            }
            for (String permission : permissionList) {
                if (permission.startsWith(APPLICATION_PERMISSION_NODE)) {
                    String spPermission = permission.substring(APPLICATION_PERMISSION_NODE.lastIndexOf("s") + 2);
                    if (spPermission.contains(DEFAULT_PERMISSION_SEPARATOR)) {
                        spPermission = spPermission.replace(DEFAULT_PERMISSION_SEPARATOR, ABB_PERMISSION_SEPARATOR);
                        permissionSet.add(spPermission);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Retrieving all the permission of the application : " + spPermission);
                        }
                        /*
                        to-do
                         */
                    }
                }
            }
            for (String permission : permissionSet) {
                if (permissionString.toString().equals("")) {
                    permissionString = new StringBuilder(permission);
                } else {
                    permissionString.append(",").append(permission);
                }
            }
        } catch (Exception e) {
            log.error("Error while retrieving user claims: ", e);
        }

        claimMap.put(ENTITLEMENTS_CLAIM, permissionString.toString());

        if (log.isDebugEnabled()) {
            log.debug("Permission String " + permissionString.toString() + " for user " + userName);
        }

        return true;
    }

    private String getUserNameFromUserID(String userID, AbstractUserStoreManager userStoreManager) throws
            UserStoreException {

        return UserCoreUtil.removeDomainFromName(userStoreManager.getUserNameFromUserID(userID));
    }

}
