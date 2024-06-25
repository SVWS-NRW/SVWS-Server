package svws.gradle.java;

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.JavaVersion
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.JavaExec


/**
 * Dieses Plugin fasst die grundlegende Konfiguration für die
 * Java-basierte-Bibliotheken des SVWS-Projektes zusammen. Die dient der
 * Vereinheitlichung der Java-Teilprojekte.
 */
class JavaLibPlugin implements Plugin<Project> {

	def project;


	void addCrypto() {
		def versionJCCrypt = '0.4.7';
		project.configurations.create('crypto');
		project.dependencies.add('crypto', 'de.svws-nrw.ext:jbcrypt:' + versionJCCrypt);
	}


	void addDatabase() {
		def versionPersistenceAPI = '3.2.0';
		def versionEclipselink = '4.0.3';
		def versionMariaDB = '3.4.0';
		def versionSQLite = '3.46.0.0';
		def versionUCanAccess = '5.1.1';
		def versionJackcess = '4.0.6';
		def versionMySQL = '8.4.0';
		def versionMSSQL = '12.6.0.jre11';
		project.configurations.create('database');
		project.dependencies.add('database', 'jakarta.persistence:jakarta.persistence-api:' + versionPersistenceAPI);
		project.dependencies.add('database', 'org.eclipse.persistence:org.eclipse.persistence.jpa:' + versionEclipselink);
		project.dependencies.add('database', 'org.mariadb.jdbc:mariadb-java-client:' + versionMariaDB);
		project.dependencies.add('database', 'org.xerial:sqlite-jdbc:' + versionSQLite);
		project.dependencies.add('database', 'io.github.spannm:ucanaccess:' + versionUCanAccess);
		project.dependencies.add('database', 'com.healthmarketscience.jackcess:jackcess:' + versionJackcess);
		project.dependencies.add('database', 'com.mysql:mysql-connector-j:' + versionMySQL);
		project.dependencies.add('database', 'com.microsoft.sqlserver:mssql-jdbc:' + versionMSSQL);
	}


	void addJacksonConfiguration() {
		def version = "2.17.1";
		project.configurations.create('jackson');
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-annotations:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-core:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-databind:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-csv:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:' + version);
	}


	void addJettyConfiguration() {
		def version = "12.0.10";
		def versionServletApi = "6.1.0";
		project.configurations.create('jetty');
		project.dependencies.add('jetty', 'jakarta.servlet:jakarta.servlet-api:' + versionServletApi);
		project.dependencies.add('jetty', 'org.eclipse.jetty.http2:jetty-http2-server:' + version);
		project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-security:' + version);
		project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-server:' + version);
		project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-java-server:' + version);
		project.dependencies.add('jetty', 'org.eclipse.jetty.ee10:jetty-ee10-servlet:' + version);
		project.dependencies.add('jetty', 'org.eclipse.jetty.ee10:jetty-ee10-servlets:' + version);
	}


	void addPdfConfiguration() {
		def version = "3.0.0";
		def openHtmlToPdfVersion = "1.1.4";
		project.configurations.create('pdf');
		project.dependencies.add('pdf', 'org.apache.pdfbox:pdfbox:' + version);
		project.dependencies.add('pdf', 'at.datenwort.openhtmltopdf:openhtmltopdf-core:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'at.datenwort.openhtmltopdf:openhtmltopdf-pdfbox:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'at.datenwort.openhtmltopdf:openhtmltopdf-svg-support:' + openHtmlToPdfVersion);
	}


	void addThymeleafConfiguration() {
		def version = "3.1.2.RELEASE";
		project.configurations.create('thymeleaf');
		project.dependencies.add('thymeleaf', 'org.thymeleaf:thymeleaf:' + version);
	}


	void addRestEasyConfiguration() {
		def version = "6.2.9.Final";
		project.configurations.create('resteasy');
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-core:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jackson2-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-multipart-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jaxb-provider:' + version);
	}


	void addSwagger() {
		def version = "2.2.22";
		def versionUI = "5.17.14";
		project.configurations.create('swagger');
		project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-jaxrs2-jakarta:' + version);
		project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-annotations-jakarta:' + version);
		project.dependencies.add('swagger', 'org.webjars.npm:swagger-ui-dist:' + versionUI);
	}


	void addValidation() {
		def version = "3.1.0";
		project.configurations.create('validation');
		project.dependencies.add('validation', 'jakarta.validation:jakarta.validation-api:' + version);
	}


	void addEmail() {
		def version = "2.0.3";
		project.configurations.create('email');
		project.dependencies.add('email', 'org.eclipse.angus:angus-mail:' + version);
	}



	void apply(Project project) {
		this.project = project;
		project.pluginManager.apply "java"

		project.dependencies.add('testImplementation', 'org.junit.jupiter:junit-jupiter:5.10.2');
		project.dependencies.add('testImplementation', 'org.junit.platform:junit-platform-launcher:1.10.2');

		project.java.sourceCompatibility = JavaVersion.VERSION_21
		project.java.targetCompatibility = JavaVersion.VERSION_21

		project.tasks.withType(JavaCompile.class, {
			options.encoding = 'UTF-8'
			options.warnings = true
		})

		project.tasks.withType(JavaExec.class, {
			jvmArgs '-Dfile.encoding=UTF-8', '-Dline.separator=\n'
		})

		this.addCrypto();
		this.addDatabase();
		this.addJacksonConfiguration();
		this.addJettyConfiguration();
		this.addPdfConfiguration();
		this.addThymeleafConfiguration();
		this.addRestEasyConfiguration();
		this.addSwagger();
		this.addValidation();
		this.addEmail();
	}

}
