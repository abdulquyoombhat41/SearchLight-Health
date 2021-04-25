package com.searchlight.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

@Service
public class HttpClientService {
  @Autowired
  @Qualifier("appRestClient")
  private RestTemplate restTemplate;

  @PostConstruct
  protected void init()  {
    SimpleClientHttpRequestFactory clientHttpRequestFactory
        = new SimpleClientHttpRequestFactory();
    //Connect timeout
    clientHttpRequestFactory.setConnectTimeout(5000);

    //Read timeout
    clientHttpRequestFactory.setReadTimeout(600000);
    restTemplate.setRequestFactory(clientHttpRequestFactory);
  }
  public <T> ResponseEntity fetchResponse(Optional<T> requestBody, HttpMethod method, String path,
      Optional<Map<String, String>> headersMap, Optional<String> query){
    try {
      if (query.isPresent()) {
        path += "?" + query;
      }
      // here escaping is not needed, since the getRequestURI returns escaped. So using regular URI constructor
      URI uri = new URI(path);
      HttpHeaders headers = new HttpHeaders();
      headersMap.ifPresent(map -> {
        for(Entry e : map.entrySet()) {
          headers.set((String)e.getKey(), (String)e.getValue());
        }
      });
      HttpEntity<T> entity;
      entity = requestBody.map(body -> new HttpEntity<>(body, headers)).orElseGet(() -> new HttpEntity<>(headers));
      return restTemplate
          .exchange(uri, method, entity, String.class);
    } catch (URISyntaxException e){
      return noContent().build();
    } catch (HttpClientErrorException e){
      return status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
    } catch(HttpServerErrorException e) {
      return status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
    } catch(UnknownHttpStatusCodeException e) {
      return status(e.getRawStatusCode()).body(e.getResponseBodyAsByteArray());
    } catch (Exception e){
      return status(NOT_FOUND).body(e);
    }
  }

}
