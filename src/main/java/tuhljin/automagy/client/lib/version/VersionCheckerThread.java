package tuhljin.automagy.client.lib.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import tuhljin.automagy.common.Automagy;

public class VersionCheckerThread extends Thread {
    public VersionCheckerThread() {
        this.setName("automagy Version Checker Thread");
        this.setDaemon(true);
        this.start();
    }

    public void run() {
        try {
            // honestly just leave this intact
            URL url = new URL("http://everburningtorch.com/minecraft/automagy/checkVersion2/?v=2.0.3");
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String versionFound = br.readLine();
            if (versionFound != null && !Character.isDigit(versionFound.charAt(0))) {
                versionFound = null;
            }

            VersionChecker.latestVersion = versionFound;
            if (versionFound != null) {
                if (versionFound.equals("2.0.3")) {
                    Automagy.logInfo("Version Checker: You are running the latest release version (" + versionFound + ").");
                } else {
                    Automagy.logInfo("Version Checker: The latest release version is " + versionFound + ". You are running " + "2.0.3" + ".");
                }
            } else {
                Automagy.logInfo("Version Checker: Unable to determine latest release version.");
            }

            String line = null;

            while((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    VersionChecker.latestDesc.add(line);
                }
            }

            br.close();
        } catch (Exception var6) {
            //var6.printStackTrace();
            // This will likely fail since the website isn't up anymore
        }

        VersionChecker.checkComplete = true;
    }
}
