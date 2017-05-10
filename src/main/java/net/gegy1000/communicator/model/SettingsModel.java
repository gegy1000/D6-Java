package net.gegy1000.communicator.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class SettingsModel {
    private int serial;
    private String appName;
    private String appType;
    private AppURLs urls;

    private Defaults defaults;
    private Clients clients;
    private Registration registration;
    private Advertisements ads;
    private String[] languages;
    private Map<String, String> countries = new HashMap<>();
    private Map<String, Map<String, String>> regions = new HashMap<>();
    private String[] logos;

    public int getSerial() {
        return this.serial;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getAppType() {
        return this.appType;
    }

    public AppURLs getUrls() {
        return this.urls;
    }

    public Defaults getDefaults() {
        return this.defaults;
    }

    public Clients getClients() {
        return this.clients;
    }

    public Registration getRegistration() {
        return this.registration;
    }

    public Advertisements getAds() {
        return this.ads;
    }

    public String[] getLanguages() {
        return this.languages;
    }

    public Map<String, String> getCountries() {
        return this.countries;
    }

    public Map<String, Map<String, String>> getRegions() {
        return this.regions;
    }

    public String[] getLogos() {
        return this.logos;
    }

    public static class AppURLs {
        private String app;
        private String auth;
        private String password;
        private String banners;
        private String logos;
        private String clients;
        private String content;
        @SerializedName("client_logos")
        private String clientLogos;

        public String getApp() {
            return this.app;
        }

        public String getAuth() {
            return this.auth;
        }

        public String getPassword() {
            return this.password;
        }

        public String getBanners() {
            return this.banners;
        }

        public String getLogos() {
            return this.logos;
        }

        public String getClients() {
            return this.clients;
        }

        public String getContent() {
            return this.content;
        }

        public String getClientLogos() {
            return this.clientLogos;
        }
    }

    public static class Defaults {
        private String lang;
        private int country;
        private int region;
        private String landingScreen;

        public String getLang() {
            return this.lang;
        }

        public int getCountry() {
            return this.country;
        }

        public int getRegion() {
            return this.region;
        }

        public String getLandingScreen() {
            return this.landingScreen;
        }
    }

    public static class Clients {
        private boolean enabled;

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    public static class Registration {
        private boolean enabled;
        @SerializedName("skip_android")
        private boolean skipAndroid;
        @SerializedName("skip_ios")
        private boolean skipIOS;
        @SerializedName("skip_win")
        private boolean skipWindows;
        @SerializedName("skip_other")
        private boolean skipOther;

        public boolean isEnabled() {
            return this.enabled;
        }

        public boolean isSkipAndroid() {
            return this.skipAndroid;
        }

        public boolean isSkipIOS() {
            return this.skipIOS;
        }

        public boolean isSkipWindows() {
            return this.skipWindows;
        }

        public boolean isSkipOther() {
            return this.skipOther;
        }
    }

    public static class Advertisements {
        private boolean enabled;
    }
}
