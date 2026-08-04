package de.svws_nrw.service.wiedervorlage;

import de.svws_nrw.oauth.SchemaServiceFactory;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.erzieher.ErzieherRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WiedervorlageServiceFactoryTest {
	@Mock
	private WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory;
	@Mock
	private BenutzerRepositoryFactory benutzerRepositoryFactory;
	@Mock
	private LehrerRepositoryFactory lehrerRepositoryFactory;
	@Mock
	private SchuelerRepositoryFactory schuelerRepositoryFactory;
	@Mock
	private ErzieherRepositoryFactory erzieherRepositoryFactory;
	@Mock
	private SchemaServiceFactory schemaServiceFactory;

	private WiedervorlageServiceFactory cut;

	@BeforeEach
	void setUp() {
		cut = WiedervorlageServiceFactory.getNewInstance(
				wiedervorlageRepositoryFactory,
				benutzerRepositoryFactory,
				lehrerRepositoryFactory,
				schuelerRepositoryFactory,
				erzieherRepositoryFactory,
				schemaServiceFactory
		);
	}

	@Test
	@DisplayName("getWiedervorlageService | Service wird erstellt und alle Repositories abgerufen")
	void getWiedervorlageService() {
		final var service = cut.getWiedervorlageService();

		assertNotNull(service);
	}
}
