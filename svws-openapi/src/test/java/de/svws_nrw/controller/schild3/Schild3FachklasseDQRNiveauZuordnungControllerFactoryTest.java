package de.svws_nrw.controller.schild3;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.service.schild3.Schild3FachklasseDQRNiveauZuordnungServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Schild3FachklasseDQRNiveauZuordnungControllerFactoryTest {

	@InjectMocks
	private Schild3FachklasseDQRNiveauZuordnungControllerFactory cut;

	@Mock
	private Schild3FachklasseDQRNiveauZuordnungServiceFactory schild3FachklasseDqrNiveauZuordnungServiceFactory;

	@Test
	@DisplayName("Test: Die Methode withReadAccess baut die Datenbank-Verbindung ohne Kompetenz im Dev-Mode auf und gibt die Factory zurück")
	void withReadAccess() throws ApiOperationException {
		final HttpServletRequest requestMock = mock(HttpServletRequest.class);

		try (MockedStatic<DBBenutzerUtils> dbUtilsMock = mockStatic(DBBenutzerUtils.class);
				MockedStatic<DbConnectionProvider> repoSupportMock = mockStatic(DbConnectionProvider.class)) {
			repoSupportMock.when(DbConnectionProvider::getConnection).thenReturn(mock(DBEntityManager.class));

			final Schild3FachklasseDQRNiveauZuordnungControllerFactory result = Schild3FachklasseDQRNiveauZuordnungControllerFactory.withReadAccess(requestMock);

			assertThat(result).isNotNull();
			dbUtilsMock.verify(() -> DBBenutzerUtils.getDBConnection(requestMock, ServerMode.DEV, BenutzerKompetenz.KEINE), times(1));
		}
	}

	@Test
	void getDQRNiveauController() {
		cut = spy(cut);

		final Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl result = cut.getDQRNiveauController();

		assertThat(result).isNotNull();
		verify(schild3FachklasseDqrNiveauZuordnungServiceFactory, times(1)).getSchild3FachklasseDQRNiveauZuordnungService();

	}
}
