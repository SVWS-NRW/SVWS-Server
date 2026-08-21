package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepository;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthCredentialServiceFactoryTest {

	@Mock
	private OAuthCredentialRepositoryFactory repositoryFactory;

	private OAuthCredentialServiceFactory cut;

	@BeforeEach
	void setUp() {
		cut = OAuthCredentialServiceFactory.getNewInstance(repositoryFactory);
	}

	@Test
	@DisplayName("getClientCredentialService | Service wird erstellt und Repository ueber die Factory bezogen")
	void getClientCredentialService() {
		when(repositoryFactory.getRepository()).thenReturn(mock(OAuthCredentialRepository.class));

		final var service = cut.getClientCredentialService();

		assertNotNull(service);
		verify(repositoryFactory).getRepository();
	}
}
