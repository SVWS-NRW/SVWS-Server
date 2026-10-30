package de.svws_nrw.mapper.klassen;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;

/**
 * Der Kontext für das Mapping einer {@link DTOKlassen} in {@link de.svws_nrw.asd.data.klassen.KlassenDaten}.
 * Er enthält alle Daten, die der {@link KlassenMapper} benötigt und nicht selbst aus der Datenbank laden darf.
 * Die Klassen der Nachbarabschnitte, die Klassenleitungen und die Schüler sind optional. Werden sie mit
 * {@code null} übergeben, so werden sie wie leere Maps bzw. Listen behandelt.
 *
 * @param schulform          die Schulform der Schule
 * @param schuljahresabschnitt   der Schuljahresabschnitt der Klasse
 * @param klassenVorher      die Klassen des vorigen Schuljahresabschnitts mit dem Kürzel als Schlüssel
 * @param klassenNachher     die Klassen des folgenden Schuljahresabschnitts mit dem Kürzel als Schlüssel
 * @param klassenLeitungen   die IDs der Lehrer, welche die Klasse leiten
 * @param schueler           die Schüler der Klasse
 */
public record KlassenMappingContext(
		Schulform schulform,
		Schuljahresabschnitt schuljahresabschnitt,
		Map<String, DTOKlassen> klassenVorher,
		Map<String, DTOKlassen> klassenNachher,
		List<Long> klassenLeitungen,
		List<Schueler> schueler
) {

	/**
	 * Erstellt einen neuen Kontext. Fehlende Maps und Listen werden durch leere ersetzt.
	 *
	 * @param schulform              die Schulform der Schule, darf nicht null sein
	 * @param schuljahresabschnitt   der Schuljahresabschnitt der Klasse, darf nicht null sein
	 * @param klassenVorher          die Klassen des vorigen Schuljahresabschnitts mit dem Kürzel als Schlüssel
	 * @param klassenNachher         die Klassen des folgenden Schuljahresabschnitts mit dem Kürzel als Schlüssel
	 * @param klassenLeitungen       die IDs der Lehrer, welche die Klasse leiten
	 * @param schueler               die Schüler der Klasse
	 */
	public KlassenMappingContext {
		Objects.requireNonNull(schulform, "Die Schulform darf nicht null sein.");
		Objects.requireNonNull(schuljahresabschnitt, "Der Schuljahresabschnitt darf nicht null sein.");
		klassenVorher = (klassenVorher == null) ? Map.of() : klassenVorher;
		klassenNachher = (klassenNachher == null) ? Map.of() : klassenNachher;
		klassenLeitungen = (klassenLeitungen == null) ? List.of() : klassenLeitungen;
		schueler = (schueler == null) ? List.of() : schueler;
	}

	/**
	 * Gibt das Schuljahr des Schuljahresabschnitts der Klasse zurück.
	 *
	 * @return das Schuljahr
	 */
	public int schuljahr() {
		return schuljahresabschnitt.schuljahr;
	}

	/**
	 * Gibt die Nummer des Schuljahresabschnitts der Klasse innerhalb des Schuljahres zurück.
	 *
	 * @return die Nummer des Abschnitts
	 */
	public int abschnitt() {
		return schuljahresabschnitt.abschnitt;
	}

}
