package de.svws_nrw.module.reporting.types.schueler;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;

/**
 * Test der Lernabschnitts-Auflösung über den Schuljahresabschnitt in {@link ReportingSchueler}.
 * <p>Die Abiturzeugnis-Vorlagen übergeben hier den Abitur-Schuljahresabschnitt des Schülers, und der ist {@code null}, wenn die Schule den Abschnitt nicht
 * angelegt hat - etwa bei einem Jahrgang, der sein Abitur erst ablegt. Ohne den Guard bräche die Druckausgabe mit einer NullPointerException ab.</p>
 */
class TestReportingSchueler {

	@Test
	void testDieLernabschnittsAufloesungGehtMitFehlendemSchuljahresabschnittUm() {
		// Der Schüler besitzt Lernabschnitte; allein der übergebene Abschnitt fehlt. Nur so ist belegt, dass der Guard und nicht die leere
		// Lernabschnittsliste den Abbruch verhindert.
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.lernabschnitte()).thenReturn(List.of(mock(ReportingSchuelerLernabschnitt.class)));
		when(schueler.aktiverLernabschnittInSchuljahresabschnitt(null)).thenCallRealMethod();

		assertNull(schueler.aktiverLernabschnittInSchuljahresabschnitt(null),
				"Ohne Schuljahresabschnitt gibt es keinen Lernabschnitt; die Vorlage zeigt ihre Leerwerte.");
	}

}
