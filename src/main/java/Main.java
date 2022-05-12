import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static final ObjectMapper mapper = new ObjectMapper();//Объект для де/сериализации json

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
// создание объекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        //request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
// отправка запроса
       try {
           CloseableHttpResponse response = httpClient.execute(request);
       } catch (ClientProtocolException e){
           System.out.println(e);
       }
// вывод полученных заголовков, оставила на память
//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//// чтение и печать тела ответа, оставила на память
        ////String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        //// System.out.println(body);

        List<Anymal> anymals = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Anymal>>() {
        });
        anymals.forEach(System.out::println);
        anymals.stream()
                // .filter(value -> value.getUpvotes() != null) не понятная проверка, как можно вернуть int = null? это же будет 0
                .filter(value -> value.getUpvotes() > 0)
                .forEach(System.out::println);
        response.close();
        httpClient.close();

    }
}

