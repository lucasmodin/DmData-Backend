package DmData.message.recaptcha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;


/*
Denne fil er en service fil fordi den i bund og grund kun fungerer som et led mellem
Captcha Controller og beskeden
*/


@Service
public class Verifycaptcha {

    public boolean verifyCaptcha(String captchaResponse) {
        try {
            String secretKey = System.getenv("RECAPTCHA_SECRET"); //miljø variable
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "secret=" + URLEncoder.encode(secretKey, "UTF-8")
                    + "&response=" + URLEncoder.encode(captchaResponse, "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(params);
                out.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
            return json.get("success").getAsBoolean();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
