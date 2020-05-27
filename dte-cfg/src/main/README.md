# Configuration directory
* It contains configuration templates will be filled by maven to run applications when the profile dev is enabled.
* To override Spring Boot configuration files, add following the argument to java command  
```--spring.config.location```

* DB connexion variables, they are required for production like environments
```
DTE_DBC_HOST
DTE_DBC_PORT
DTE_DBC_NAME
DTE_DBC_SCHEMA
DTE_DBC_USERNAME
DTE_DBC_PASSWORD
```



