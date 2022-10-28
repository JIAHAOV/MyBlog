package com.study.reproduce.confiig;

import com.study.reproduce.confiig.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {
    private final int port;
    private final String host;
    private final String username;
    private final String password;

    private final ElasticsearchProperties elasticsearchProperties;

    public ElasticsearchConfig(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.host = elasticsearchProperties.getHost();
        this.password = elasticsearchProperties.getPassword();
        this.username = elasticsearchProperties.getUsername();
        this.port = elasticsearchProperties.getPort();
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(new HttpHost(host, port, "http")));

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        /*设置账号密码*/
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username,password));

        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            //设置账号密码等
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            //最大连接数
            httpAsyncClientBuilder.setMaxConnTotal(5);
            //每5分钟发生一次心跳保持连接
            httpAsyncClientBuilder.setKeepAliveStrategy((response, context) -> 	Duration.ofMinutes(5).toMillis());

            return httpAsyncClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}
