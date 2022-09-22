package svws.gradle.java;

import org.gradle.api.Plugin 
import org.gradle.api.Project 
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
	    def versionJCCrypt = '0.4.0';
	    project.configurations.create('crypto');
	    project.dependencies.add('crypto', 'de.nrw.schule.svws.ext.jbcrypt:jbcrypt:' + versionJCCrypt); 
    }


	void addDatabase() {
		def versionPersistenceAPI = '3.0.0';
		def versionEclipsePersistence = '2.2.3';
		def versionEclipselink = '3.1.0-M1';
		def versionMariaDB = '2.7.4';  
		def versionSQLite = '3.36.0.3';  
		def versionUCanAccess = '5.0.1';  
		def versionJackcess = '4.0.1';  
		def versionMySQL = '8.0.29';  
		def versionMSSQL = '9.4.1.jre16';  
		project.configurations.create('database');
		project.dependencies.add('database', 'jakarta.persistence:jakarta.persistence-api:' + versionPersistenceAPI);
	    project.dependencies.add('database', 'org.eclipse.persistence:jakarta.persistence:' + versionEclipsePersistence);
		project.dependencies.add('database', 'org.eclipse.persistence:org.eclipse.persistence.jpa:' + versionEclipselink);
	    project.dependencies.add('database', 'org.mariadb.jdbc:mariadb-java-client:' + versionMariaDB);
	    project.dependencies.add('database', 'org.xerial:sqlite-jdbc:' + versionSQLite);
	    project.dependencies.add('database', 'net.sf.ucanaccess:ucanaccess:' + versionUCanAccess);
    	project.dependencies.add('database', 'com.healthmarketscience.jackcess:jackcess:' + versionJackcess);
	    project.dependencies.add('database', 'mysql:mysql-connector-java:' + versionMySQL);
	    project.dependencies.add('database', 'com.microsoft.sqlserver:mssql-jdbc:' + versionMSSQL);
    }


	void addJacksonConfiguration() {
		def version = "2.12.3";
		project.configurations.create('jackson');
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-annotations:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-core:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.core:jackson-databind:' + version);
		project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-csv:' + version);
    	project.dependencies.add('jackson', 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:' + version);
	}
	
	
	void addJettyConfiguration() {
		// TODO replace with addJetty11Configuration as soon as all dependency conflicts with the jakarta name space are solved
		def version = "10.0.9";
		def versionServletApi = "4.0.6";
		project.configurations.create('jetty');
	    project.dependencies.add('jetty', 'org.eclipse.jetty.toolchain:jetty-servlet-api:' + versionServletApi);
	    project.dependencies.add('jetty', 'org.eclipse.jetty.http2:http2-server:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-servlet:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-servlets:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-jaas:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-server:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-java-server:' + version);
	}


	void addJetty11Configuration() {
		// TODO for future use - see above
		def version = "11.0.9";
		def versionServletApi = "5.0.2";
		project.configurations.create('jetty');
	    project.dependencies.add('jetty', 'org.eclipse.jetty.toolchain:jetty-jakarta-servlet-api:' + versionServletApi);
	    project.dependencies.add('jetty', 'org.eclipse.jetty.http2:http2-server:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-servlet:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-servlets:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-jaas:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-server:' + version);
	    project.dependencies.add('jetty', 'org.eclipse.jetty:jetty-alpn-java-server:' + version);
	}


	void addPdfConfiguration() {
		def version = "2.0.26";
		def openHtmlToPdfVersion = "1.0.10";
		project.configurations.create('pdf');
		project.dependencies.add('pdf', 'org.apache.pdfbox:pdfbox:' + version);
		project.dependencies.add('pdf', 'com.openhtmltopdf:openhtmltopdf-core:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'com.openhtmltopdf:openhtmltopdf-pdfbox:' + openHtmlToPdfVersion);
		project.dependencies.add('pdf', 'com.openhtmltopdf:openhtmltopdf-svg-support:' + openHtmlToPdfVersion);
	}



	void addRestEasyConfiguration() {
		def version = "5.0.1.Final";
		project.configurations.create('resteasy');
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-core:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jackson2-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-multipart-provider:' + version);
		project.dependencies.add('resteasy', 'org.jboss.resteasy:resteasy-jaxb-provider:' + version);
	}


	void addSwagger() {
	    def version = "2.1.11";
	    def versionUI = "4.1.3";
		project.configurations.create('swagger');
	    project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-jaxrs2:' + version);
		project.dependencies.add('swagger', 'io.swagger.core.v3:swagger-annotations:' + version);
		project.dependencies.add('swagger', 'org.webjars.npm:swagger-ui-dist:' + versionUI);
	}


	void addValidation() {
		def version = "3.0.1";
		project.configurations.create('validation');
		project.dependencies.add('validation', 'jakarta.validation:jakarta.validation-api:' + version);
    }


  	void apply(Project project) {
    	this.project = project;
    	project.pluginManager.apply "java"
    	
    	project.dependencies.add('testImplementation', 'org.junit.jupiter:junit-jupiter:5.8.2');
    	project.dependencies.add('testImplementation', 'org.junit.platform:junit-platform-launcher:1.8.2');

		project.sourceCompatibility = 17
		project.targetCompatibility = 17
    	
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
		this.addRestEasyConfiguration();
		this.addSwagger();
		this.addValidation();
    }
    
}
