package de.svws_nrw.module.reporting.signing;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.schulbescheinigung.SchulbescheinigungBuilder;
import de.svws_nrw.schulbescheinigung.SchulbescheinigungSerializer;
import de.svws_nrw.schulbescheinigung.SchulbescheinigungService;
import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;

/**
 * Anti-Corruption-Layer zwischen den Reporting-Typen und dem xschule-Builder. Liest aus {@link ReportingSchueler}
 * und {@link ReportingSchule} und erzeugt daraus das XSchule-XML einer Schulbescheinigung als String.
 * Die Klasse hält das Mapping isoliert (Eingabe: Reporting-Objekte, Ausgabe: XML-String). Aufgerufen wird sie
 * im Bulk-Loader des {@code ReportingRepositorySchueler} je Schüler einmal.
 */
public final class SchulbescheinigungXmlFactory {

	/** Sprachcode des Dokuments gemäß Codeliste (ISO 639-2/T für Deutsch). */
	private static final String SPRACHE = "deu";

	/** Titel der Schulbescheinigung. */
	private static final String TITEL = "Schulbescheinigung";

	private SchulbescheinigungXmlFactory() {
		throw new IllegalStateException("Statische Factory-Klasse. Initialisierung nicht möglich.");
	}

	/**
	 * Erzeugt das XSchule-XML einer Schulbescheinigung für den übergebenen Schüler.
	 *
	 * @param schueler             Der Schüler, dessen Stammdaten in die Bescheinigung übernommen werden.
	 * @param schule               Die Schule, deren Bezeichnung als Schulname übernommen wird.
	 * @param ausstellungOrt       Ort der Ausstellung (im Loader aus {@code schule.ort()} ermittelt).
	 * @param ausstellungDatum     Datum der Ausstellung im ISO-Format (z. B. {@code 2026-06-05}).
	 * @param bildungsgangEnddatum Enddatum des Bildungsgangs im ISO-Format (z. B. {@code 2031-07-31}).
	 *
	 * @return das XSchule-XML der Schulbescheinigung als String.
	 */
	public static String erzeugeXml(final ReportingSchueler schueler, final ReportingSchule schule,
			final String ausstellungOrt, final String ausstellungDatum, final String bildungsgangEnddatum) {
		final XSchuleSchuelerSchulbescheinigung0004 dok = new SchulbescheinigungBuilder()
				.sprache(SPRACHE)
				.titel(TITEL)
				.schuelerNachname(schueler.nachname())
				.schuelerVorname(schueler.vornamen())
				.schuelerGeburtsdatum(schueler.geburtsdatum())
				.schuleName(schule.bezeichnungSchuleZeileEins())
				.bildungsgangEnddatum(bildungsgangEnddatum)
				.ausstellungOrt(ausstellungOrt)
				.ausstellungDatum(ausstellungDatum)
				.build();
		// createSchulbescheinigungXml ist eine Instanzmethode → Service mit Serializer instanziieren.
		return new SchulbescheinigungService(new SchulbescheinigungSerializer()).createSchulbescheinigungXml(dok);
	}

}
