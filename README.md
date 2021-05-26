# User Operation Event Listener to manipulate claims

This User Operation Event Listener can manipulate the cliams returned from the userstore.

Note: This is implemented for WSO2IS-5.11.0. If you want to use it in some other WSO2 server, you can simply modify the pom.xml file’s project dependency versions matching the same version packed in the product

### Steps to deploy
- Build the component by running "mvn clean install"
- Copy following jar file which can be found in target directory into <IS_HOME>/repository/components/dropins/
    - custom.claim.user.operation.event.listener-1.0.0.jar
- Configure the User Operation Event Listener by adding following lines into deployment.toml file.
    ```
    [[event_listener]]
    id = "custom-claim-user-operation-event-listener"
    type = "org.wso2.carbon.user.core.listener.UserOperationEventListener"
    name = "org.wso2.carbon.custom.user.operation.event.listener.CustomClaimUserOperationEventListener"
    order = 1356
    enable = true
    ```
- Restart WSO2 IS.
