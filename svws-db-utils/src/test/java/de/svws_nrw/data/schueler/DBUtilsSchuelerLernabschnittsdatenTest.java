package de.svws_nrw.data.schueler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Tests für {@link DBUtilsSchuelerLernabschnittsdaten}.
 */
@DisplayName("DBUtilsSchuelerLernabschnittsdaten")
class DBUtilsSchuelerLernabschnittsdatenTest {

	@ParameterizedTest
	@ValueSource(longs = { 1L, 2L, 3L })
	@DisplayName("createByPrevious übernimmt idBetreuung aus dem vorigen Lernabschnitt")
	void createByPrevious_uebernimmtIdBetreuung(final long idBetreuung) throws Exception {
		final var conn = mock(DBEntityManager.class);
		final var slaPrev = new DTOSchuelerLernabschnittsdaten(1L, 10L, 20L, false, false, idBetreuung);
		slaPrev.WechselNr = 0;
		slaPrev.Klassen_ID = 30L;
		final var klasse = new DTOKlassen(30L, 20L, "05a", false, false);
		klasse.Jahrgang_ID = 40L;
		final var jahrgang = new DTOJahrgang(40L);
		when(conn.queryByKey(DTOKlassen.class, 30L)).thenReturn(klasse);
		when(conn.queryList(anyString(), any(), any(Object[].class))).thenAnswer(inv -> {
			final Class<?> cl = inv.getArgument(1);
			if (cl == DTOSchuelerLernabschnittsdaten.class) {
				final String sql = inv.getArgument(0);
				return sql.contains("<>") ? List.of() : List.of(slaPrev);
			}
			return List.of(klasse);
		});
		when(conn.transactionPersist(any())).thenReturn(true);

		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = 21L;
		abschnitt.schuljahr = 2026;
		abschnitt.abschnitt = 2;
		abschnitt.idVorigerAbschnitt = 20L;

		final var factory = mock(KatalogRepositoryFactory.class, RETURNS_DEEP_STUBS);
		when(factory.getJahrgangRepository().getById(40L)).thenReturn(jahrgang);
		try (var factoryStatic = mockStatic(KatalogRepositoryFactory.class)) {
			factoryStatic.when(KatalogRepositoryFactory::getNewInstance).thenReturn(factory);

			final var result = DBUtilsSchuelerLernabschnittsdaten.createByPrevious(2L, conn, 10L, abschnitt);

			assertThat(result.idBetreuung).isEqualTo(idBetreuung);
			assertThat(result.Schuljahresabschnitts_ID).isEqualTo(21L);
		}
	}

}
