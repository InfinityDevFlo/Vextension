/**
 *   Copyright © 2020 | vironlab.eu | Licensed under the GNU General Public license Version 3<p>
 * <p>
 *      ___    _______                        ______         ______  <p>
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 *<p>
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 *<p>
 *<p>
 *   This program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   This program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.dependency;

import eu.vironlab.vextension.document.DocumentInit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;

public class DependencyAgent {

    static File libDir = new File(System.getProperty("dependencyLibDir") != null ? System.getProperty("dependencyLibDir") : ".libs");
    static Instrumentation instrumentation;

    static {
        if (!libDir.exists()) {
            try {
                Files.createDirectories(libDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            load("org.jetbrains.kotlin:kotlin-stdlib:1.5.10");
            load("org.jetbrains.kotlin:kotlin-serialization:1.5.10");
            load("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3");
            for (String depend : DocumentInit.getDocumentDependCoords()) {
                load(depend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void premain(String args, Instrumentation instrumentation) {
        DependencyAgent.instrumentation = instrumentation;
    }

    static void agentmain(String args, Instrumentation instrumentation) {
        DependencyAgent.instrumentation = instrumentation;
    }

    public static void appendJarFile(JarFile file) throws IOException {
        if (instrumentation != null) {
            instrumentation.appendToSystemClassLoaderSearch(file);
        }
    }

    private static void load(String dependencyStr) throws IOException {
        String[] splittet = dependencyStr.split(":");
        Dependency dependency = new Dependency(splittet[0], splittet[1], splittet[2]);
        String filePath = dependency.group.replace(".", "/") + "/" + dependency.name + "/" + dependency.version;
        String fileName = dependency.name + "-" + dependency.version + ".jar";
        File folder = new File(libDir, filePath);
        File dest = new File(folder, fileName);
        if (!dest.exists()) {
            try {
                if (!folder.exists()) {
                    Files.createDirectories(folder.toPath());
                }
                URL server = new URL("https://repo1.maven.org/maven2/" + filePath + "/" + fileName);
                InputStream stream = server.openStream();
                Files.copy(stream, dest.toPath());
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String digest = "";
        try {
            MessageDigest fileCheckDigest = MessageDigest.getInstance("MD5");
            fileCheckDigest.update(Files.readAllBytes(dest.toPath()));
            byte[] b = fileCheckDigest.digest();
            for (int i = 0; i < b.length; i++) {
                digest += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Append JarFile " + dest.getName() + " Digest: " + digest);
        appendJarFile(new JarFile(dest));
    }

    private static boolean is200(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        conn.connect();
        int rs = conn.getResponseCode();
        conn.disconnect();
        return rs == 200;
    }

    public static class Dependency {
        public String group;
        public String name;
        public String version;

        public Dependency(String group, String name, String version) {
            this.group = group;
            this.name = name;
            this.version = version;
        }
    }

}
