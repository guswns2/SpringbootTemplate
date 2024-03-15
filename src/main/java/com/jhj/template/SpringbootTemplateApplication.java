package com.jhj.template;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ObjectUtils;

import com.jhj.template.common.Properties;
import com.jhj.template.utils.StringBuilderPlus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class SpringbootTemplateApplication {

	@Autowired
	Properties properties;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTemplateApplication.class, args);
	}

	@Bean
	public TomcatServletWebServerFactory servletContainer() {
		int port = 8080;
		if (!ObjectUtils.isEmpty(properties.serverPort)) {
			port = Integer.parseInt(properties.serverPort);
		}

		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		if ("dev".equals(properties.profilesActive)) {
			String baseDir = properties.basedir;
			if (baseDir != null && !baseDir.equals("")) {
				File file = new File(baseDir);
				tomcat.setDocumentRoot(file);
			}
		}
		tomcat.setPort(port);
		tomcat.setUriEncoding(Charset.forName("UTF-8"));

		TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				log.info("CONTEXT RELOADABLE : {}", context.getReloadable());

				/**
				 * TODO : Remove comment slash to use custom 404 error page
				 */
				ErrorPage err400 = new ErrorPage();
				err400.setErrorCode(400);
				err400.setLocation("/error/400.html");
				context.addErrorPage(err400);

				ErrorPage err403 = new ErrorPage();
				err403.setErrorCode(403);
				err403.setLocation("/error/403.html");
				context.addErrorPage(err403);

				ErrorPage err404 = new ErrorPage();
				err404.setErrorCode(404);
				err404.setLocation("/error/404.html");
				context.addErrorPage(err404);

				ErrorPage err405 = new ErrorPage();
				err405.setErrorCode(405);
				err405.setLocation("/error/405.html");
				context.addErrorPage(err404);

				ErrorPage err500 = new ErrorPage();
				err500.setErrorCode(500);
				err500.setLocation("/error/500.html");
				context.addErrorPage(err500);

				ErrorPage err503 = new ErrorPage();
				err503.setErrorCode(503);
				err503.setLocation("/error/503.html");
				context.addErrorPage(err503);
			}
		};
		tomcat.addContextCustomizers(contextCustomizer);

		StringBuilderPlus builder = new StringBuilderPlus();
		builder.appendLine(
				"===========================================================================================================================");
		builder.appendLine("TOMCAT CONFIG ......");
		builder.appendLine(MessageFormat.format("IP : {0}", tomcat.getAddress()));
		builder.appendLine(MessageFormat.format("PORT : {0}", tomcat.getPort()));
		builder.appendLine(MessageFormat.format("CONTEXT PATH : {0}",
				tomcat.getContextPath()));
		builder.appendLine(MessageFormat.format("URI ENCODING : {0}",
				tomcat.getUriEncoding()));
		builder.appendLine(MessageFormat.format("SESSION TIME OUT : {0}",
				tomcat.getSession().getTimeout()));
		builder.appendLine(MessageFormat.format("OS : {0}",
				System.getProperty("os.name").toLowerCase()));
		builder.appendLine(MessageFormat.format("배포환경 : {0}",
				properties.profilesActive));
		builder.appendLine(
				"===========================================================================================================================");
		log.info(builder.toString());

		return tomcat;
	}

}
