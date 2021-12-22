package top.binaryx.garen.server.service;


public interface PropertiesService {

    String getPropertiesValue(String name);

    int getInt(String name, int defaultValue);

    String getString(String name, String defaultValue);

    boolean getBoolean(String name, boolean defaultValue);
}
