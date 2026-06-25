package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.oauth.internal.Credentials;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CredStoreService")
class CredStoreServiceTest {

	@Mock
	private SchemaService schemaService;

	@Mock
	private CredentialStore credentialStore;

	@InjectMocks
	private CredStoreService credStoreService;

	private Schema schema;

	@BeforeEach
	void setUp() {
		schema = mock(Schema.class);
	}

	@Test
	@DisplayName("getBySchema – liefert Credentials zurück, die der Store für das Schema bereithält")
	void getBySchema_happyPath_returnsCredentials() {
		final Credentials expected = mock(Credentials.class);
		when(credentialStore.forSchema(schema)).thenReturn(expected);

		Credentials result = credStoreService.getBySchema(schema);

		assertThat(result).isSameAs(expected);
	}

	@Test
	@DisplayName("getBySchema – delegiert genau einmal an CredentialStore.forSchema() mit dem übergebenen Schema")
	void getBySchema_delegatesToCredentialStore_exactlyOnce() {
		when(credentialStore.forSchema(schema)).thenReturn(mock(Credentials.class));

		credStoreService.getBySchema(schema);

		verify(credentialStore, times(1)).forSchema(schema);
		verifyNoMoreInteractions(credentialStore);
		verifyNoInteractions(schemaService);
	}

	@Test
	@DisplayName("getBySchema – gibt null zurück, wenn der CredentialStore null zurückliefert")
	void getBySchema_storeReturnsNull_returnsNull() {
		when(credentialStore.forSchema(schema)).thenReturn(null);

		Credentials result = credStoreService.getBySchema(schema);

		assertThat(result).isNull();
	}

	@Test
	@DisplayName("getBySchema – übergibt null-Schema unverändert an den CredentialStore")
	void getBySchema_nullSchema_passedThroughToStore() {
		when(credentialStore.forSchema(null)).thenReturn(null);

		credStoreService.getBySchema(null);

		verify(credentialStore).forSchema(null);
	}
}
