package svws.gradle.java

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test

/**
 * Dieses Plugin fasst die grundlegende Konfiguration für die
 * Java-basierte-Bibliotheken des SVWS-Projektes zusammen. Die dient der
 * Vereinheitlichung der Java-Teilprojekte.
 */
class JavaLibPlugin implements Plugin<Project> {

	def project;


	void addCrypto() {
		def versionJCCrypt = '0.4.7';
		def versionBouncyCastle = '1.84';
		project.configurations.create('crypto');
		project.dependencies.add('crypto', 'de.svws-nrw.ext:jbcrypt:' + versionJCCrypt);
		project.dependencies.add('crypto', 'org.bouncycastle:bcprov-jdk18on:' + versionBouncyCastle);
		project.dependencies.add('crypto', 'org.bouncycastle:bcpkix-jdk18on:' + versionBouncyCastle);
	}


	void addDatabase() {
		def versionPersistenceAPI = '3.2.0';
		def versionEclipselink = '4.0.9';
		def versionMariaDB = '3.5.9';
		def versionSQLite = '3.53.2.0';
		def versionUCanAccess = '5.1.7';
		def versionJackcess = '5.1.2';
		def versionMySQL = '9.6.0';
		def versionMSSQL = '13.4.0.jre11';
		project.configurations.create('database');
		project.dependencies.add('database', 'jakarta.persistence:jakarta.persistence-api:' + versionPersistenceAPI);
		project.dependencies.add('database', 'org.eclipse.persistence:org.eclipse.persistence.jpa:' + versionEclipselink);
		project.dependencies.add('database', 'org.mariadb.jdbc:mariadb-java-client:' + versionMariaDB);
		project.dependencies.add('database', 'org.xerial:sqlite-jdbc:' + versionSQLite);
		project.dependencies.add('database', 'io.github.spannm:ucanaccess:' + versionUCanAccess);
		project.dependencies.add('database', 'io.github.spannm:jackcess:' + versionJackcess);
		project.dependencies.add('database', 'com.mysql:mysql-connector-j:' + versionMySQL);
		project.dependencies.add('database', 'com.microsoft.sqlserver:mssql-jdbc:' + versionMSSQL);
		// vulnerability fix for: io.github.spannm:ucanaccess:5.1.7 -> pin org.apache.logging.log4j:log4j-api:2.26.1
		project.dependencies.add('database', 'org.apache.logging.log4j:log4j-api:2.26.1');
	}


	void addJacksonConfiguration() {
		def version = "2.22.1";
		def version_annotations = "2.22";
		project.configurations.create('jackson');
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-annotations:' + version_annotations);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-core:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-databind:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-csv:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:' + version);
	}


	void addJettyConfiguration() {
		def version = "12.1.10";
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
		def version = "3.0.6";
		def openHtmlToPdfVersion = "1.1.37";
		project.configurations.create('pdf');
		project.dependencies.add('pdf', 'org.apache.pdfbox:pdfbox:' + version);
		project.dependencies.add('pdf', 'io.github.openhtmltopdf:openhtmltopdf-core:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'io.github.openhtmltopdf:openhtmltopdf-pdfbox:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'io.github.openhtmltopdf:openhtmltopdf-svg-support:' + openHtmlToPdfVersion);
	}


	void addThymeleafConfiguration() {
		def version = "3.1.5.RELEASE";
		project.configurations.create('thymeleaf');
		project.dependencies.add('thymeleaf', 'org.thymeleaf:thymeleaf:' + version);
	}


	void addBarcodeConfiguration() {
		def version = "3.2.0";
		project.configurations.create('barcode');
		project.dependencies.add('barcode', 'de.vwsoft:barcodelib4j:' + version);
	}


	void addRestEasyConfiguration() {
		def version = "7.0.2.Final";
		def versionJsonNullable = "0.2.10";
		project.configurations.create('resteasy');
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-core:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jackson2-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-multipart-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jaxb-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-validator-provider:' + version);
		// vulnerability fix for: org.jboss.resteasy:resteasy-multipart-provider:6.2.12.Final -> pin org.eclipse.angus:angus-mail:2.0.4
		project.dependencies.add('resteasy', 'org.eclipse.angus:angus-mail:2.0.5');
		project.dependencies.add('resteasy', 'org.openapitools:jackson-databind-nullable:' + versionJsonNullable);
	}


	void addSwagger() {
		def version = "2.2.43";
		def versionUI = "5.32.0";
		project.configurations.create('swagger');
		project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-jaxrs2-jakarta:' + version);
		project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-annotations-jakarta:' + version);
		project.dependencies.add('swagger', 'org.webjars.npm:swagger-ui-dist:' + versionUI);
		// vulnerability fix for: io.swagger.core.v3:swagger-jaxrs2-jakarta:2.2.34 -> pin org.apache.commons:commons-lang3:3.18.0
		project.dependencies.add('swagger', 'org.apache.commons:commons-lang3:3.18.0');
	}


	void addValidation() {
		def version = "3.1.1";
		project.configurations.create('validation');
		project.dependencies.add('validation', 'jakarta.validation:jakarta.validation-api:' + version);
		project.dependencies.add('validation', 'org.hibernate.validator:hibernate-validator:9.1.0.Final')
		project.dependencies.add('validation', 'org.glassfish.expressly:expressly:6.0.0')
	}


	void addEmail() {
		def version = "2.0.5";
		project.configurations.create('email');
		project.dependencies.add('email', 'org.eclipse.angus:angus-mail:' + version);
	}


	void addJaxbRuntime() {
		def jaxbVersion = "4.0.6";
		def jakartaVersion = "4.0.5";
		project.configurations.create('jaxbRuntime');
		project.dependencies.add('jaxbRuntime', 'org.glassfish.jaxb:jaxb-runtime:' + jaxbVersion);
		project.dependencies.add('jaxbRuntime', 'jakarta.xml.bind:jakarta.xml.bind-api:' + jakartaVersion);
	}


	void addXjc() {
		def version = "4.0.6";
		project.configurations.create('xjc');
		project.dependencies.add('xjc', 'org.glassfish.jaxb:jaxb-xjc:' + version);
	}

	void addMapstruct() {
		def version = "1.6.3"
		project.configurations.create('mapstructApi')
		project.configurations.create('mapstructProcessor')

		project.dependencies.add('mapstructApi', 'org.mapstruct:mapstruct:' + version)
		project.dependencies.add('mapstructProcessor', 'org.mapstruct:mapstruct-processor:' + version)
	}


	void addTika() {
		project.configurations.create('tika');
		project.dependencies.add('tika', 'org.apache.tika:tika-core:3.3.1');
	}


	@Override
	void apply(Project project) {
		this.project = project;
		project.pluginManager.apply "java"

		project.dependencies.add('testImplementation', 'org.junit.jupiter:junit-jupiter:6.0.3');
		project.dependencies.add('testImplementation', 'org.junit.platform:junit-platform-launcher:6.0.3');
		project.dependencies.add('testImplementation', 'org.mockito:mockito-core:5.22.0');
		project.dependencies.add('testImplementation', 'org.mockito:mockito-junit-jupiter:5.22.0');
		project.dependencies.add('testImplementation', 'org.assertj:assertj-core:3.27.7');
		project.dependencies.add('testImplementation', 'org.awaitility:awaitility:4.3.0');

		project.java.sourceCompatibility = JavaVersion.VERSION_21
		project.java.targetCompatibility = JavaVersion.VERSION_21

		project.tasks.withType(JavaCompile.class, {
			options.encoding = 'UTF-8'
			options.warnings = true
		})

		project.tasks.withType(JavaExec.class, {
			jvmArgs '-Dfile.encoding=UTF-8', '-Dline.separator=\n'
		})

		project.tasks.withType(Test).configureEach {
			doFirst {
				def mockitoCoreJar = classpath.files.find { it.name ==~ /mockito-core-.*\.jar/ }
				if (mockitoCoreJar == null) {
					throw new GradleException("Die Mockito-Core-JAR konnte für den Testtask '${path}' nicht ermittelt werden.")
				}
				jvmArgs "-javaagent:${mockitoCoreJar.absolutePath}"
			}
		}

		this.addCrypto();
		this.addDatabase();
		this.addJacksonConfiguration();
		this.addJettyConfiguration();
		this.addPdfConfiguration();
		this.addThymeleafConfiguration();
		this.addBarcodeConfiguration();
		this.addRestEasyConfiguration();
		this.addSwagger();
		this.addValidation();
		this.addEmail();
		this.addJaxbRuntime()
		this.addXjc()
		this.addMapstruct()
		this.addTika()
	}

}
