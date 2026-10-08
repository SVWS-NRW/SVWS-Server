package de.svws_nrw.service.oauth.credential;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialMapper;
import de.svws_nrw.mapper.oauth.OAuthDomainMapper;
import de.svws_nrw.oauth.internal.Credentials;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepository;
import de.svws_nrw.service.crypto.secret.SecretCipher;
import jakarta.ws.rs.core.Response.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthCredentialServiceTest {

	private static final String MASKED_SECRET = "*******";

	@Mock
	private OAuthCredentialRepository repository;

	@Mock
	private SecretCipher secretCipher;

	private final OAuthCredentialMapper mapper = Mappers.getMapper(OAuthCredentialMapper.class);
	private final OAuthDomainMapper oAuthDomainMapper = Mappers.getMapper(OAuthDomainMapper.class);

	private OAuthCredentialService cut;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		cut = new OAuthCredentialService(repository, mapper, oAuthDomainMapper, secretCipher);

		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.any(Runnable.class)))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}


	@Test
	@DisplayName("getAll | gibt alle gemappten Credentials mit maskiertem Secret zurueck, ohne zu entschluesseln")
	void getAllSuccess() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		when(repository.getAll()).thenReturn(List.of(dto));

		final var result = cut.getAll();

		assertThat(result).containsExactly(withSecret(mapper.fromDomain(dto), MASKED_SECRET));
		verifyNoInteractions(secretCipher);
	}

	@Test
	@DisplayName("getAll(serviceDomain) | gibt Credentials der Domaene mit maskiertem Secret zurueck, ohne zu entschluesseln")
	void getAllByDomainSuccess() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		when(repository.findAllByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(List.of(dto));

		final var result = cut.getAll(OAuthDomain.IT_NRW);

		assertThat(result).containsExactly(withSecret(mapper.fromDomain(dto), MASKED_SECRET));
		verifyNoInteractions(secretCipher);
	}

	@Test
	@DisplayName("get(domain) | gibt Credentials mit entschluesseltem Secret zurueck wenn vorhanden")
	void getByDomainSuccess() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		dto.clientSecret = "CHIFFRAT";
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(Optional.of(dto));
		when(secretCipher.decrypt("CHIFFRAT")).thenReturn("client-secret");

		final var result = cut.get(OAuthDomain.IT_NRW);

		assertThat(result).contains(withSecret(mapper.fromDomain(dto), "client-secret"));
	}

	@Test
	@DisplayName("get(domain) | gibt leeres Optional zurueck wenn keine Credentials existieren")
	void getByDomainNotFound() {
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(Optional.empty());

		final var result = cut.get(OAuthDomain.IT_NRW);

		assertThat(result).isEmpty();
		verifyNoInteractions(secretCipher);
	}

	@Test
	@DisplayName("get | gibt Credentials mit maskiertem Secret zurueck, ohne zu entschluesseln")
	void getSuccess() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		when(repository.findById(3L)).thenReturn(Optional.of(dto));

		final var result = cut.get(3L);

		assertThat(result).contains(withSecret(mapper.fromDomain(dto), MASKED_SECRET));
		verifyNoInteractions(secretCipher);
	}

	@Test
	@DisplayName("get | gibt leeres Optional zurueck wenn keine Credentials existieren")
	void getNotFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		final var result = cut.get(99L);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("create | speichert das Secret verschluesselt und gibt es maskiert zurueck")
	void createSuccess() {
		final var credentials = buildCredentials(OAuthDomain.IT_NRW);
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(Optional.empty());
		when(secretCipher.encrypt("client-secret")).thenReturn("CHIFFRAT");

		final var result = cut.create(credentials);

		assertThat(result).hasFieldOrPropertyWithValue("clientId", credentials.clientId())
				.hasFieldOrPropertyWithValue("clientSecret", MASKED_SECRET)
				.hasFieldOrPropertyWithValue("authServerUrl", credentials.authServerUrl())
				.hasFieldOrPropertyWithValue("serviceDomain", credentials.serviceDomain());

		final var captor = ArgumentCaptor.forClass(DTOSchuleOAuthSecrets.class);
		verify(repository).create(captor.capture());
		assertThat(captor.getValue())
				.hasFieldOrPropertyWithValue("clientId", "client-id")
				.hasFieldOrPropertyWithValue("clientSecret", "CHIFFRAT")
				.hasFieldOrPropertyWithValue("authServerUrl", "https://auth.example.com/token")
				.hasFieldOrPropertyWithValue("serviceDomain", OAuthServiceDomain.IT_NRW)
				.hasFieldOrPropertyWithValue("requestedScope", "scope");
		verify(secretCipher, times(1)).encrypt("client-secret");
	}

	@Test
	@DisplayName("create | BAD_REQUEST wenn fuer die Domaene bereits Credentials existieren, ohne zu verschluesseln")
	void createBadRequestWennDomainBelegt() {
		final var credentials = buildCredentials(OAuthDomain.IT_NRW);
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW))
				.thenReturn(Optional.of(buildEntity(3L, OAuthServiceDomain.IT_NRW)));

		Assertions.assertThatThrownBy(() -> cut.create(credentials))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.BAD_REQUEST);

		verify(repository, never()).create(ArgumentMatchers.any(DTOSchuleOAuthSecrets.class));
		verifyNoInteractions(secretCipher);
	}

	@Test
	@DisplayName("get(domain) | Fehler beim Entschluesseln wird unveraendert durchgereicht")
	void getByDomainEntschluesselnFehlgeschlagen() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(Optional.of(dto));
		final var fehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Das Secret konnte nicht entschlüsselt werden.");
		when(secretCipher.decrypt("client-secret")).thenThrow(fehler);

		Assertions.assertThatThrownBy(() -> cut.get(OAuthDomain.IT_NRW)).isSameAs(fehler);
	}

	@Test
	@DisplayName("create | Fehler beim Verschluesseln verhindert das Speichern")
	void createVerschluesselnFehlgeschlagen() {
		final var credentials = buildCredentials(OAuthDomain.IT_NRW);
		when(repository.findByServiceDomain(OAuthServiceDomain.IT_NRW)).thenReturn(Optional.empty());
		final var fehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Das Secret konnte nicht verschlüsselt werden.");
		when(secretCipher.encrypt("client-secret")).thenThrow(fehler);

		Assertions.assertThatThrownBy(() -> cut.create(credentials)).isSameAs(fehler);
		verify(repository, never()).create(ArgumentMatchers.any(DTOSchuleOAuthSecrets.class));
	}

	@Test
	@DisplayName("delete | loescht vorhandene Credentials")
	void deleteSuccess() {
		final var dto = buildEntity(3L, OAuthServiceDomain.IT_NRW);
		final List<DTOSchuleOAuthSecrets> entities = List.of(dto);
		final List<Long> ids = List.of(3L);
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = cut.delete(3L);

		assertThat(result)
				.isNotNull().hasFieldOrPropertyWithValue("success", true);

		verify(repository, times(1)).findListByIds(ids);
		verify(repository, times(1)).delete(entities);
	}

	@Test
	@DisplayName("delete | tut nichts wenn keine Credentials mit der ID existieren")
	void deleteNotFound() {
		List<Long> ids = List.of(99L);
		when(repository.findListByIds(ids)).thenReturn(new ArrayList<>());

		final var result = cut.delete(99L);

		assertThat(result)
				.isNotNull().hasFieldOrPropertyWithValue("success", false);
	}


	private static DTOSchuleOAuthSecrets buildEntity(final long id, final OAuthServiceDomain domain) {
		final var dto = new DTOSchuleOAuthSecrets(id, "https://auth.example.com/token", "client-id", "client-secret");
		dto.serviceDomain = domain;
		dto.requestedScope = "scope";
		return dto;
	}

	private static Credentials buildCredentials(final OAuthDomain domain) {
		return new Credentials("client-id", "client-secret", URI.create("https://auth.example.com/token"), "scope", domain);
	}

	private static Credentials withSecret(final Credentials c, final String secret) {
		return new Credentials(c.id(), c.clientId(), secret, c.authServerUrl(), c.requestedScope(), c.serviceDomain());
	}
}
