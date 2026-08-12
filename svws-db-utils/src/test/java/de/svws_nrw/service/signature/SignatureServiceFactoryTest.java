package de.svws_nrw.service.signature;

import java.net.URI;

import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.oauth.OAuthHttpClientImpl;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignatureServiceFactoryTest {

	@Mock
	private OAuthHttpClientFactory authHttpClientFactory;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	@Mock
	private BenutzerServiceFactory benutzerServiceFactory;

	@BeforeEach
	void setUp() {
		lenient().when(authHttpClientFactory.getClient()).thenReturn(mock(OAuthHttpClientImpl.class));
	}

	// -------------------------------------------------------------------------
	// Factory-Erzeugung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getNewInstance()")
	class GetNewInstance {

		@Test
		@DisplayName("gibt eine nicht-null Instanz zurück")
		void getNewInstance_shouldReturnNonNullInstance() {
			final SignatureServiceFactory factory = SignatureServiceFactory.getNewInstance(authHttpClientFactory, eigeneSchuleServiceFactory, benutzerServiceFactory);

			assertThat(factory).isNotNull();
		}

		@Test
		@DisplayName("gibt bei jedem Aufruf eine neue Instanz zurück")
		void getNewInstance_shouldReturnNewInstanceOnEachCall() {
			final SignatureServiceFactory factory1 = SignatureServiceFactory.getNewInstance(authHttpClientFactory, eigeneSchuleServiceFactory, benutzerServiceFactory);
			final SignatureServiceFactory factory2 = SignatureServiceFactory.getNewInstance(authHttpClientFactory, eigeneSchuleServiceFactory, benutzerServiceFactory);

			assertThat(factory1).isNotSameAs(factory2);
		}

		@Test
		@DisplayName("wirft NullPointerException wenn httpClientFactory null ist")
		void getNewInstance_shouldThrow_whenHttpClientFactoryIsNull() {
			final SignatureServiceFactory factoryInstance = SignatureServiceFactory.getNewInstance(null, eigeneSchuleServiceFactory, benutzerServiceFactory);
			assertThatThrownBy(factoryInstance::getSignatureService)
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("wirft NullPointerException wenn schuleFactory null ist")
		void getNewInstance_shouldThrow_whenSchuleFactoryIsNull() {
			final SignatureServiceFactory factoryInstance = SignatureServiceFactory.getNewInstance(authHttpClientFactory, null, benutzerServiceFactory);
			assertThatThrownBy(factoryInstance::getSignatureService)
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("wirft NullPointerException wenn benutzerServiceFactory null ist")
		void getNewInstance_shouldThrow_whenBenutzerServiceFactoryIsNull() {
			final SignatureServiceFactory factoryInstance = SignatureServiceFactory.getNewInstance(authHttpClientFactory, eigeneSchuleServiceFactory, null);
			assertThatThrownBy(factoryInstance::getSignatureService)
					.isInstanceOf(NullPointerException.class);
		}
	}

	// -------------------------------------------------------------------------
	// Service-Erzeugung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getSignatureService()")
	class GetSignatureService {

		private SignatureServiceFactory cut;

		@BeforeEach
		void setUp() {
			cut = SignatureServiceFactory.getNewInstance(
					SignatureServiceFactoryTest.this.authHttpClientFactory,
					SignatureServiceFactoryTest.this.eigeneSchuleServiceFactory,
					SignatureServiceFactoryTest.this.benutzerServiceFactory
			);
		}

		@Test
		@DisplayName("gibt eine nicht-null Instanz zurück")
		void getSignatureService_shouldReturnNonNullInstance() {
			cut = spy(cut);
			doReturn(URI.create("https://test.signature-service.de/sign")).when(cut).getSignatureServiceURI();

			final SignatureService service = cut.getSignatureService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine SignatureServiceImpl-Instanz zurück")
		void getSignatureService_shouldReturnSignatureServiceImplInstance() {
			cut = spy(cut);
			doReturn(URI.create("https://test.signature-service.de/sign")).when(cut).getSignatureServiceURI();

			final SignatureService service = cut.getSignatureService();

			assertThat(service).isInstanceOf(SignatureServiceImpl.class);
		}

		@Test
		@DisplayName("gibt bei jedem Aufruf eine neue Instanz zurück")
		void getSignatureService_shouldReturnNewInstanceOnEachCall() {
			cut = spy(cut);
			doReturn(URI.create("https://test.signature-service.de/sign")).when(cut).getSignatureServiceURI();

			final SignatureService service1 = cut.getSignatureService();
			final SignatureService service2 = cut.getSignatureService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getClient() auf der httpClientFactory auf")
		void getSignatureService_shouldCallGetClientOnFactory() {
			cut = spy(cut);
			doReturn(URI.create("https://test.signature-service.de/sign")).when(cut).getSignatureServiceURI();

			cut.getSignatureService();

			verify(SignatureServiceFactoryTest.this.authHttpClientFactory, times(1)).getClient();
		}

		@Test
		@DisplayName("ruft bei mehrfachem Aufruf jedes Mal getClient() auf")
		void getSignatureService_shouldCallGetClientOnEachCall() {
			cut = spy(cut);
			doReturn(URI.create("https://test.signature-service.de/sign")).when(cut).getSignatureServiceURI();

			cut.getSignatureService();
			cut.getSignatureService();
			cut.getSignatureService();

			verify(SignatureServiceFactoryTest.this.authHttpClientFactory, times(3)).getClient();
		}
	}
}
