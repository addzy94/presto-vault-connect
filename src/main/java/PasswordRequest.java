import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.*;

class PasswordRequest {

    String url;
    HttpClient client;
    String token;

    public PasswordRequest() throws Exception {

        String KEYSTOREPATH = System.getProperty("root_folder") + "identityKeystore.jks";
        String KEYSTOREPASS = "manageit";
        String KEYPASS = "ZoomGamings777@";

        FileInputStream fs = new FileInputStream(new File(KEYSTOREPATH));
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(fs, KEYSTOREPASS.toCharArray());

        SSLContext sslContext = SSLContexts
                .custom()
                .loadKeyMaterial(keyStore, KEYPASS.toCharArray())
                .build();

        client = HttpClients.custom().setSSLContext(sslContext).build();
    }

    public void Login() throws Exception {

        url = "https://secvault.glb.prod.skumart.com/vault-impl/rest/vault/v3/auth/KeypairAuth/login";

        String sign = "jAiaWXA+SD4CUnrhluwDdN/UFgZups8HOYWDU14JZBQrs7NOs6DvZlY2pZ8xAofKTcLUIeoTkb/EO72G7Wl9EZb+MfxmXfTwS/1zcmrO0qFLNPs/rtwm9gbjsM+X3dT9HSZKvbh79rHTVfziwo6JKpyvdrQHhurp97mj58wFU+PwhFYUoobo18X0CPz6GU84bheMDdQh9pBT8hWUE4eTtXAs7IFUlULtv9KS/s+hE+OZZSuuUeLb7Nk2PxoFG3tTlk+LtyM2J52245eP/LefP77vlVBavL82q0zhO7/PsvzoUxMaK9OEKvXktfta8ZCSxibPu1ZML1qMF5uNbDuoF8lqAn47sHDy3iIwytAQ9fnl4TLekTXVHzvjC9rvvG/F5xIqJtJxb2SAG8N1+T2iW9TCCNcYe84X5aFLMu6sDWce6x4/r8iybDjj5Y0he0Fie8n+OD9z3OhzS3KtdI9DTtNYajZZeAe66+RQg3jE5I0VCeUms9Is9VHZjZnCtL2QRghbZR+P/aNIni8OJse+oAJMPEq4pLXgGVOUoZEZeAiJfnPr+NC51Vn7NfDj+FyxML+ciZCkHS31ou2uCel0M40rGZFFgoTdycm8N7WX+x+QRotHwnuU/gPdl0dCpGGiG2WLXTskIeWTIYv2davDBfuz9wPR+8gP6kfLtTl7yx4=";

        JSONObject obj = new JSONObject();
        obj.put("clientName", "presConnect");
        obj.put("zoneName", "nonPCI");
        obj.put("signAlgo","SHA256withRSA");
        obj.put("sign" , sign);

        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(obj.toString()));

        HttpResponse response = client.execute(request);
        token = Extractor(response, "token");
    }

    public String Retrieve() throws IOException {

        url = "https://secvault.glb.prod.skumart.com/vault-impl/rest/vault/v3/data/retrieve";

        String dataRefId = "fbbe3585f0cd16d2";

        JSONObject obj = new JSONObject();
        obj.put("dataRefId", dataRefId);

        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.addHeader("Authorization", "Bearer " + token);
        request.setEntity(new StringEntity(obj.toString()));

        HttpResponse response = client.execute(request);
        return(Extractor(response, "data"));

    }

    private String Extractor(HttpResponse response, String key) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String line = "";
        StringBuilder outputString = new StringBuilder();
        while ((line = br.readLine()) != null) {
            outputString.append(line);
        }

        JSONObject outputJSON = new JSONObject(outputString.toString());
        return(outputJSON.get(key).toString());

    }
}
