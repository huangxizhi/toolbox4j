package club.crazyai.utils;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;


public enum ConfigManager {
    INSTANCE();
    private final Properties properties;
    
    ConfigManager() {
        properties = new Properties();
        String filename = Constants.configFileAddr + Constants.configFileName;

        try {
            properties.load(new FileInputStream(filename));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return ConfigManager.INSTANCE.properties;
    }

    
    public Integer getInteger(final String keyName) {
        String strValue = getStr(keyName);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        Integer ret;
        try {
            ret = Integer.parseInt(strValue);
        } catch (Exception e) {
            ret = null;
        }

        return ret;
    }

    public String getStr(final String keyName) {
        if (!properties.containsKey(keyName)) {
            return "";
        }
        return properties.get(keyName).toString().trim();
    }

    public Boolean getBoolean(final String keyName) {
        String strValue = getStr(keyName);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }
        return Boolean.parseBoolean(strValue);
    }

    public Double getDouble(final String keyName) {
        String strValue = getStr(keyName);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        Double ret;
        try {
            ret = Double.parseDouble(strValue);
        } catch (Exception e) {
            ret = null;
        }

        return ret;
    }
    
    public Float getFloat(final String keyName) {
		String strValue = getStr(keyName);
		if (StringUtils.isEmpty(strValue)) {
			return null;
		}

		Float ret;
		try {
			ret = Float.parseFloat(strValue);
		} catch (Exception e) {
			ret = null;
		}

		return ret;
	}

    public static class Constants {
        final static String configFileName = "config.properties";
        public final static String configFileAddr = "config/";
    }
}
