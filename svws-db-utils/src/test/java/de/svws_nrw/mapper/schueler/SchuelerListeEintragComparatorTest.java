package de.svws_nrw.mapper.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SchuelerListeEintragComparatorTest {

	private final SchuelerListeEintragComparator comparator = SchuelerListeEintragComparator.INSTANCE;

	private static SchuelerListeEintrag eintrag(final Long idKlasse, final String nachname, final String vorname) {
		final var eintrag = new SchuelerListeEintrag();
		eintrag.idKlasse = idKlasse;
		eintrag.nachname = nachname;
		eintrag.vorname = vorname;
		return eintrag;
	}

	@Test
	@DisplayName("Klasse null wird vor eine gesetzte Klasse sortiert")
	void klasseNull_wirdVorneSortiert() {
		final var ohneKlasse = eintrag(null, "Zander", "Zoe");
		final var mitKlasse = eintrag(1L, "Adler", "Anna");

		assertThat(comparator.compare(ohneKlasse, mitKlasse)).isNegative();
		assertThat(comparator.compare(mitKlasse, ohneKlasse)).isPositive();
	}

	@Test
	@DisplayName("Zwei Einträge ohne Klasse werden nach dem Nachnamen sortiert")
	void beideKlassenNull_sortiertNachNachname() {
		assertThat(comparator.compare(eintrag(null, "A", "A"), eintrag(null, "B", "B"))).isNegative();
		assertThat(comparator.compare(eintrag(null, "B", "B"), eintrag(null, "A", "A"))).isPositive();
		assertThat(comparator.compare(eintrag(null, "A", "A"), eintrag(null, "A", "A"))).isZero();
	}

	@Test
	@DisplayName("Unterschiedliche Klassen werden nach der Klassen-ID sortiert")
	void unterschiedlicheKlassen_sortiertNachKlassenId() {
		assertThat(comparator.compare(eintrag(1L, "Zander", "Zoe"), eintrag(2L, "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag(2L, "Adler", "Anna"), eintrag(1L, "Zander", "Zoe"))).isPositive();
	}

	@Test
	@DisplayName("Gleiche Klasse: Es wird nach dem Nachnamen sortiert")
	void gleicheKlasse_sortiertNachNachname() {
		assertThat(comparator.compare(eintrag(1L, "Adler", "Zoe"), eintrag(1L, "Becker", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag(1L, "Becker", "Anna"), eintrag(1L, "Adler", "Zoe"))).isPositive();
	}

	@Test
	@DisplayName("Gleiche Klasse und gleicher Nachname: Es wird nach dem Vornamen sortiert")
	void gleicherNachname_sortiertNachVorname() {
		assertThat(comparator.compare(eintrag(1L, "Adler", "Anna"), eintrag(1L, "Adler", "Zoe"))).isNegative();
		assertThat(comparator.compare(eintrag(1L, "Adler", "Zoe"), eintrag(1L, "Adler", "Anna"))).isPositive();
	}

	@Test
	@DisplayName("Vollständig gleiche Einträge sind gleich")
	void gleicheEintraege_sindGleich() {
		assertThat(comparator.compare(eintrag(1L, "Adler", "Anna"), eintrag(1L, "Adler", "Anna"))).isZero();
	}

	@Test
	@DisplayName("Nachname null wird vor einen gesetzten Nachnamen sortiert, bei zwei Nachnamen null entscheidet der Vorname")
	void nachnameNull_wirdVorneSortiert() {
		assertThat(comparator.compare(eintrag(1L, null, "Zoe"), eintrag(1L, "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag(1L, "Adler", "Anna"), eintrag(1L, null, "Zoe"))).isPositive();
		assertThat(comparator.compare(eintrag(1L, null, "Zoe"), eintrag(1L, null, "Anna"))).isPositive();
		assertThat(comparator.compare(eintrag(1L, null, "Anna"), eintrag(1L, null, "Anna"))).isZero();
	}

	@Test
	@DisplayName("Vorname null wird vor einen gesetzten Vornamen sortiert, bei zwei Vornamen null sind die Einträge gleich")
	void vornameNull_wirdVorneSortiert() {
		assertThat(comparator.compare(eintrag(1L, "Adler", null), eintrag(1L, "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag(1L, "Adler", "Anna"), eintrag(1L, "Adler", null))).isPositive();
		assertThat(comparator.compare(eintrag(1L, "Adler", null), eintrag(1L, "Adler", null))).isZero();
	}

	@Test
	@DisplayName("Umlaute werden nach deutscher Regel einsortiert (Ä bei A, nicht hinter Z)")
	void umlaute_werdenDeutschSortiert() {
		final var liste = new ArrayList<>(List.of(
				eintrag(1L, "Zander", "Zoe"),
				eintrag(1L, "Ärmel", "Anna"),
				eintrag(1L, "Becker", "Ben"),
				eintrag(1L, "Adler", "Alex")));

		liste.sort(comparator);

		assertThat(liste).extracting(e -> e.nachname).containsExactly("Adler", "Ärmel", "Becker", "Zander");
	}

}
