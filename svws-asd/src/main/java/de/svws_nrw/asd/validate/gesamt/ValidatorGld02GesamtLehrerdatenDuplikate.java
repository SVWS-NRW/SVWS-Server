package de.svws_nrw.asd.validate.gesamt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf Namen, Vornamen, Geschlecht und Geburtsdatum vorkommen.
 */
public final class ValidatorGld02GesamtLehrerdatenDuplikate extends Validator {

	private final @NotNull List<LehrerStammdaten> listLehrerStammdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrerStammdaten      die Liste aller Lehrerstammdaten
	 * @param kontext             		der Kontext des Validators
	 */
	public ValidatorGld02GesamtLehrerdatenDuplikate(final @NotNull List<LehrerStammdaten> listLehrerStammdaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listLehrerStammdaten = listLehrerStammdaten;
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;

		// Bei einer leeren Liste ist hier nichts zu prüfen. Dies ist ein Spezialfall, der nur bei einer neu angelegten Schul-Datenbank vorkommen sollte.
		if (listLehrerStammdaten.isEmpty())
			return success;

		// Gehe die Liste der Lehrer durch und speichere diese in einer HashMap, um Duplikate zu erkennen
		final @NotNull Map<String, LehrerStammdaten> keys = new HashMap<>();
		for (final LehrerStammdaten lehrer : listLehrerStammdaten) {
			// Erzeuge zur Duplikaterkennung einen Key zusammengesetzt aus Nachname, Vorname, Geburtsdatum und Geschlecht
			final Geschlecht geschlecht = Geschlecht.fromValue(lehrer.geschlecht);
			final @NotNull String key = lehrer.nachname + "__" + lehrer.vorname + "__" + ((lehrer.geburtsdatum == null) ? "" : lehrer.geburtsdatum)
					+ "__" + ((geschlecht == null) ? lehrer.geschlecht : geschlecht.kuerzel);
			// Füge die Lehrerstammdaten in die Map ein, damit dieser Datensatz zukünftig bei der Duplikaterkennung berücksichtigt wird
			// In der Map bereits existierende Stammdaten mit dem gleichen Key werden dabei zurückgegeben.
			final LehrerStammdaten other = keys.put(key, lehrer);
			if (other == null)
				continue;

			final String fehlermeldung = "Lehrkäfte: Bei den IDs " + lehrer.id + " und " + other.id
					+ " kommt die Kombination aus Nachname '" + lehrer.nachname + "', Vorname '" + lehrer.vorname + "', Geburtsdatum '" + lehrer.geburtsdatum
					+ "' und Geschlecht '" + lehrer.geschlecht + "' mehrmals vor."
					+ " Falls es sich hierbei um eine Person handelt, so fassen Sie die Datensätze bitte unter einer Lehrerabkürzung zusammen.";

			this.addFehler(2, fehlermeldung);
			success = false;

		}
		return success;
	}

}
