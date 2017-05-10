package net.gegy1000.communicator.device;

import java.util.Random;

public class DeviceInfo {
    public static final String[][] DEVICES = new String[][] {
            { "iPhone", "iPhone OS", "9.1" },
            { "iPad", "iPhone OS", "9.1" },
            { "iPhone", "iPhone OS", "9.2" },
            { "iPhone", "iPhone OS", "9.2.1" },
            { "iPad", "iPhone OS", "9.2" },
            { "iPad", "iPhone OS", "9.2.1" },
            { "iPhone", "iPhone OS", "9.3" },
            { "iPhone", "iPhone OS", "9.3.1" },
            { "iPhone", "iPhone OS", "9.3.2" },
            { "iPhone", "iPhone OS", "9.3.3" },
            { "iPhone", "iPhone OS", "9.3.4" },
            { "iPad", "iPhone OS", "9.3" },
            { "iPad", "iPhone OS", "9.3.1" },
            { "iPad", "iPhone OS", "9.3.2" },
            { "iPad", "iPhone OS", "9.3.3" },
            { "iPad", "iPhone OS", "9.3.4" },
            { "iPhone", "iOS", "10.2" },
            { "iPhone", "iOS", "10.2.1" },
            { "iPad", "iOS", "10.2" },
            { "iPad", "iOS", "10.2.1" },
    };

    private String device;
    private String os;
    private String osVersion;

    public DeviceInfo(String device, String os, String osVersion) {
        this.device = device;
        this.os = os;
        this.osVersion = osVersion;
    }

    /**
     * Generates a random {@link DeviceInfo} object
     *
     * @param random random number generator
     * @return random device info
     */
    public static DeviceInfo random(Random random) {
        String[] device = DEVICES[random.nextInt(DEVICES.length)];
        return new DeviceInfo(device[0], device[1], device[2]);
    }

    @Override
    public String toString() {
        return this.device + "; " + this.os + " " + this.osVersion + "; Scale/2.00";
    }
}
