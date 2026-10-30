package de.svws_nrw.mapper.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;

/**
 * Test für die Klasse {@link KlassenMappingContext}
 */
class KlassenMappingContextTest {

	@Test
	@DisplayName("Fehlende Maps und Listen (null) werden durch leere ersetzt")
	void nullWirdZuLeer() {
		final KlassenMappingContext ctx = new KlassenMappingContext(Schulform.GY, new Schuljahresabschnitt(), null, null, null, null);

		assertThat(ctx.klassenVorher()).isEmpty();
		assertThat(ctx.klassenNachher()).isEmpty();
		assertThat(ctx.klassenLeitungen()).isEmpty();
		assertThat(ctx.schueler()).isEmpty();
	}

	@Test
	@DisplayName("Übergebene Maps und Listen werden unverändert übernommen")
	void werteWerdenUebernommen() {
		final Map<String, DTOKlassen> vorher = Map.of("06a", new DTOKlassen(1L, 10L, "06a"));
		final Map<String, DTOKlassen> nachher = Map.of("08a", new DTOKlassen(2L, 12L, "08a"));
		final List<Long> leitungen = List.of(3L);
		final List<Schueler> schueler = List.of(new Schueler());

		final KlassenMappingContext ctx = new KlassenMappingContext(Schulform.GY, new Schuljahresabschnitt(), vorher, nachher, leitungen, schueler);

		assertThat(ctx.klassenVorher()).isSameAs(vorher);
		assertThat(ctx.klassenNachher()).isSameAs(nachher);
		assertThat(ctx.klassenLeitungen()).isSameAs(leitungen);
		assertThat(ctx.schueler()).isSameAs(schueler);
	}

	@Test
	@DisplayName("Schulform und Schuljahresabschnitt dürfen nicht null sein")
	void pflichtfelder() {
		assertThatThrownBy(() -> new KlassenMappingContext(null, new Schuljahresabschnitt(), null, null, null, null))
				.isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> new KlassenMappingContext(Schulform.GY, null, null, null, null, null))
				.isInstanceOf(NullPointerException.class);
	}

}
