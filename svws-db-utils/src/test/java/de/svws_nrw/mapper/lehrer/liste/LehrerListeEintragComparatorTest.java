package de.svws_nrw.mapper.lehrer.liste;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für den {@link LehrerListeEintragComparator}.
 */
class LehrerListeEintragComparatorTest {

	private final LehrerListeEintragComparator comparator = LehrerListeEintragComparator.INSTANCE;

	private static LehrerListeEintrag eintrag(final String kuerzel, final String nachname, final String vorname) {
		final var eintrag = new LehrerListeEintrag();
		eintrag.kuerzel = kuerzel;
		eintrag.nachname = nachname;
		eintrag.vorname = vorname;
		return eintrag;
	}

	@Test
	@DisplayName("Es wird zuerst nach dem Kürzel sortiert")
	void sortiertNachKuerzel() {
		assertThat(comparator.compare(eintrag("ABC", "Zander", "Zoe"), eintrag("XYZ", "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag("XYZ", "Adler", "Anna"), eintrag("ABC", "Zander", "Zoe"))).isPositive();
	}

	@Test
	@DisplayName("Bei gleichem Kürzel wird nach dem Nachnamen sortiert")
	void gleichesKuerzel_sortiertNachNachname() {
		assertThat(comparator.compare(eintrag("ABC", "Adler", "Zoe"), eintrag("ABC", "Becker", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag("ABC", "Becker", "Anna"), eintrag("ABC", "Adler", "Zoe"))).isPositive();
	}

	@Test
	@DisplayName("Bei gleichem Kürzel und Nachnamen wird nach dem Vornamen sortiert")
	void gleicherNachname_sortiertNachVorname() {
		assertThat(comparator.compare(eintrag("ABC", "Adler", "Anna"), eintrag("ABC", "Adler", "Zoe"))).isNegative();
		assertThat(comparator.compare(eintrag("ABC", "Adler", "Anna"), eintrag("ABC", "Adler", "Anna"))).isZero();
	}

	@Test
	@DisplayName("Ein Wert null wird vor jeden anderen Wert sortiert")
	void nullWirdVorneSortiert() {
		assertThat(comparator.compare(eintrag(null, "Adler", "Anna"), eintrag("ABC", "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag("ABC", null, "Anna"), eintrag("ABC", "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag("ABC", "Adler", null), eintrag("ABC", "Adler", "Anna"))).isNegative();
		assertThat(comparator.compare(eintrag("ABC", "Adler", "Anna"), eintrag("ABC", "Adler", null))).isPositive();
	}

	@Test
	@DisplayName("Umlaute werden nach dem deutschen Collator einsortiert")
	void umlauteNachDeutschemCollator() {
		final var liste = new ArrayList<>(List.of(eintrag("Z", "Zander", "A"), eintrag("OE", "Östlich", "A"), eintrag("O", "Ostermann", "A")));

		liste.sort(comparator);

		assertThat(liste).extracting(e -> e.kuerzel).containsExactly("O", "OE", "Z");
	}

}
