package com.investxpert.searchservice.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Value("${opensearch.url}")
    private String opensearchUrl;

    @Value("${opensearch.username}")
    private String username;

    @Value("${opensearch.password}")
    private String password;

    @Bean
    public OpenSearchClient openSearchClient() {
        // Parse URL to extract host and port
        String host = opensearchUrl.replace("http://", "").replace("https://", "");
        String[] hostParts = host.split(":");
        String hostname = hostParts[0];
        int port = hostParts.length > 1 ? Integer.parseInt(hostParts[1]) : 9200;
        
        RestClient restClient = RestClient.builder(
            new HttpHost(hostname, port, opensearchUrl.startsWith("https") ? "https" : "http")
        ).build();

        RestClientTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper()
        );

        return new OpenSearchClient(transport);
    }
}
