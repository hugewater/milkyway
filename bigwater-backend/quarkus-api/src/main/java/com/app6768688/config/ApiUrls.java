package com.app6768688.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Centralized API URLs configuration
 * This class contains all external API URLs used throughout the application
 */
@ApplicationScoped
public class ApiUrls {
    
    // Blockchain API URLs
    @Inject
    @ConfigProperty(name = "bw.api.polygon.url", defaultValue = "https://api.polygonscan.com/api")
    String polygonApiUrl;
    
    @Inject
    @ConfigProperty(name = "bw.api.tron.url", defaultValue = "https://api.trongrid.io")
    String tronApiUrl;
    
    @Inject
    @ConfigProperty(name = "bw.api.polygon.amoy.url", defaultValue = "https://api-amoy.polygonscan.com/api")
    String polygonAmoyApiUrl;
    
    @Inject
    @ConfigProperty(name = "bw.api.alchemy.polygon.url", defaultValue = "https://polygon-mainnet.g.alchemy.com/v2")
    String alchemyPolygonUrl;
    
    @Inject
    @ConfigProperty(name = "bw.api.moralis.polygon.url", defaultValue = "https://deep-index.moralis.io/api/v2")
    String moralisPolygonUrl;
    
    // Getters for API URLs
    public String getPolygonApiUrl() {
        return polygonApiUrl;
    }
    
    public String getTronApiUrl() {
        return tronApiUrl;
    }
    
    public String getPolygonAmoyApiUrl() {
        return polygonAmoyApiUrl;
    }
    
    public String getAlchemyPolygonUrl() {
        return alchemyPolygonUrl;
    }
    
    public String getMoralisPolygonUrl() {
        return moralisPolygonUrl;
    }
    
    /**
     * Get API URL by service and network
     * @param service Service name (polygon, tron, alchemy, moralis)
     * @param network Network name (mainnet, amoy, etc.)
     * @return API URL or null if not found
     */
    public String getApiUrl(String service, String network) {
        if ("polygon".equalsIgnoreCase(service)) {
            if ("amoy".equalsIgnoreCase(network)) {
                return polygonAmoyApiUrl;
            }
            return polygonApiUrl;
        } else if ("tron".equalsIgnoreCase(service)) {
            return tronApiUrl;
        } else if ("alchemy".equalsIgnoreCase(service)) {
            return alchemyPolygonUrl;
        } else if ("moralis".equalsIgnoreCase(service)) {
            return moralisPolygonUrl;
        }
        return null;
    }
    
    /**
     * Get all API URLs as a map
     * @return Map of service names to URLs
     */
    public java.util.Map<String, String> getAllApiUrls() {
        java.util.Map<String, String> urls = new java.util.HashMap<>();
        urls.put("polygon", polygonApiUrl);
        urls.put("tron", tronApiUrl);
        urls.put("polygon_amoy", polygonAmoyApiUrl);
        urls.put("alchemy_polygon", alchemyPolygonUrl);
        urls.put("moralis_polygon", moralisPolygonUrl);
        return urls;
    }
}
