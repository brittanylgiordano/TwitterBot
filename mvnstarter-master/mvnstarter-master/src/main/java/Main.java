import com.opencsv.CSVReader;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.net.URLEncoder;

public class Main
{
    private static final String accessTokenStr = "478150048-FsjpGMm2GQJ6YIakIdR2p6FeTJ0Qr1biUGjLbiuV";
    private static final String accessTokenSecretStr = "7zqSheQvs4YAUEWvPksx9iffsXaCqjzD5qpSpd08p88Wt";
    private static final String consumerKeyStr = "7cASACGVjhJucWPlu0su6qg5E";
    private static final String consumerSecretStr = "V57kwM4Eoa5NHBEnDCEmDbu0oBuZ9azcs0rsvmFpCKsl2bnRUc";

    public static void main(String[] args) throws Exception
    {
        //key is author's name
        HashMap<String, HashSet<String>> quotes = new HashMap<>();
        ArrayList<String> randomKeys = new ArrayList<>();
        try
        {

            Scanner read = new Scanner(new File("C:/Users/brittany/Downloads/mvnstarter-master/mvnstarter-master/src/main/java/quotes.csv"));


            while (read.hasNextLine())
            {

                String line = read.nextLine();
                String tokens[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                String key = tokens[0];
                randomKeys.add(key);

                HashSet<String> quoteSet = new HashSet<>();

                if(tokens.length < 1)
                {
                    key = "Anonymous";
                    quoteSet.add(tokens[0]);
                }
                else
                {
                    quoteSet.add(tokens[1]);
                }

                quotes.put(key, quoteSet);


//                for (String keys: quotes.keySet())
//                {
//                    System.out.println(keys + quotes.get(keys));
//                }
            }
            read.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        Collections.shuffle(randomKeys);
//        String value = quotes.get(randomKeys.get(0)).toString();

            Object value = quotes.get(randomKeys.get(0));
            System.out.println("Quote: " + value + " \nAuthor: " + randomKeys.get(0));

        postExample("Quote: " + value + " \nAuthor: " + randomKeys.get(0));

        }


    public static void postExample(String msg) throws Exception {
        OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr, consumerSecretStr);
        oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);
        String encoded = URLEncoder.encode(msg,"UTF-8");
        HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + encoded);
        oAuthConsumer.sign(httpPost);
//        HttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println(statusCode + ':' + httpResponse.getStatusLine().getReasonPhrase());
        System.out.println(IOUtils.toString(httpResponse.getEntity().getContent()));
    }


}