package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // すべてのオリジンを許可
		configuration.addAllowedMethod("*"); // すべてのHTTPメソッドを許可
		configuration.addAllowedHeader("*"); // すべてのヘッダーを許可
		configuration.addExposedHeader("Custom-Header"); // カスタムヘッダーを許可
		configuration.addAllowedOrigin("https://*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}

/*デフォルト
@Configuration
public class CorsConfig {

	
    @Bean
        CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // すべてのオリジンを許可
        configuration.addAllowedMethod("*"); // すべてのHTTPメソッドを許可
        configuration.addAllowedHeader("*"); // すべてのヘッダーを許可
        configuration.addExposedHeader("Custom-Header"); // カスタムヘッダーを許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    } 
    
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://oriscore.net")
                .allowedMethods("GET", "POST") // 使用するHTTPメソッドを設定
                .allowedHeaders("*"); // 許可するヘッダーを設定
    }
} */

//追加 
/*
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://35.75.219.239")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");

        registry.addMapping("/apiTournament/**")
            .allowedOrigins("http://35.75.219.239")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
    } 
} */