package org.example.awsplayground.loadbalancing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@RestController
public class LoadBalancingController {

    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);

    @Value("${server.port:8080}")
    private int serverPort;

    @GetMapping(value = "/lb", produces = MediaType.TEXT_HTML_VALUE)
    public String instanceInfo() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName();
        String ip = addr.getHostAddress();
        int color = Math.abs(instanceId.hashCode()) % 0xFFFFFF;
        String bgColor = String.format("#%06x", color);

        return """
                <!DOCTYPE html>
                <html>
                <head><title>Instance Info</title></head>
                <body style="margin:0; display:flex; justify-content:center; align-items:center; height:100vh; background:%s; font-family:sans-serif;">
                  <div style="background:white; padding:40px 60px; border-radius:16px; box-shadow:0 4px 24px rgba(0,0,0,0.15); text-align:center;">
                    <h1 style="margin-bottom:24px;">Instance Info</h1>
                    <table style="text-align:left; font-size:18px; border-collapse:collapse;">
                      <tr><td style="padding:8px 16px; font-weight:bold;">Instance ID</td><td style="padding:8px 16px; font-family:monospace;">%s</td></tr>
                      <tr><td style="padding:8px 16px; font-weight:bold;">Hostname</td><td style="padding:8px 16px; font-family:monospace;">%s</td></tr>
                      <tr><td style="padding:8px 16px; font-weight:bold;">IP Address</td><td style="padding:8px 16px; font-family:monospace;">%s</td></tr>
                      <tr><td style="padding:8px 16px; font-weight:bold;">Port</td><td style="padding:8px 16px; font-family:monospace;">%d</td></tr>
                    </table>
                  </div>
                </body>
                </html>
                """.formatted(bgColor, instanceId, hostname, ip, serverPort);
    }
}
