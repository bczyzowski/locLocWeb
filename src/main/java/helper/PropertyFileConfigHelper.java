package helper;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;


public class PropertyFileConfigHelper implements PropertyFileConfig {
    @Override
    public String getPropertyFileName() {
        return "appConfig.properties";
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}
