package org.wso2.carbon.custom.user.operation.event.listener.internal;

import org.wso2.carbon.custom.user.operation.event.listener.CustomClaimUserOperationEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.wso2.carbon.user.core.listener.UserOperationEventListener;
import org.wso2.carbon.user.core.service.RealmService;

@Component(
        name = "org.wso2.carbon.custom.user.operation.event.listener",
        immediate = true
)
public class ServiceComponent {

    private static Log log = LogFactory.getLog(ServiceComponent.class);
    private static RealmService realmService;

    @Activate
    protected void activate(ComponentContext context) {

        // register the custom listener as an OSGI service.
        CustomClaimUserOperationEventListener listener = new CustomClaimUserOperationEventListener();
        //listener.init();
        context.getBundleContext().registerService(UserOperationEventListener.class.getName(), listener, null);
        log.info("CustomClaimUserOperationEventListener bundle activated successfully.");
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("CustomClaimUserOperationEventListener is deactivated ");
        }
    }

    @Reference(
            name = "user.realmservice.default",
            service = org.wso2.carbon.user.core.service.RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Realm Service");
        }
        this.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Realm Service");
        }
        this.realmService = null;
    }

    public static RealmService getRealmService() {
        return realmService;
    }
}
