package de.svws_nrw.asd.validate.gesamt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf Namen, Vornamen, Geschlecht und Geburtsdatum vorkommen.
 */
public final class ValidatorGld11GesamtLehrerdatenDuplikate extends Validator {

	private final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste aller Lehrerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGld11GesamtLehrerdatenDuplikate(
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}


	@Override
	protected boolean pruefe() {
		boolean success = true;
		final @NotNull List<LehrerStatistikGesamt> list = listLehrer.get();

		// Bei einer leeren Liste ist hier nichts zu prüfen. Dies ist ein Spezialfall, der nur bei einer neu angelegten Schul-Datenbank vorkommen sollte.
		if (list.isEmpty())
			return success;

		// Gehe die Liste der Lehrer durch und speichere diese in einer HashMap, um Duplikate zu erkennen
		final @NotNull Map<String, LehrerStatistikGesamt> keys = new HashMap<>();
		for (final LehrerStatistikGesamt lehrer : list) {
			// Erzeuge zur Duplikaterkennung einen Key zusammengesetzt aus Nachname, Vorname, Geburtsdatum und Geschlecht
			final Geschlecht geschlecht = Geschlecht.fromValue(lehrer.geschlecht);
			final @NotNull String key = lehrer.nachname + "__" + lehrer.vorname + "__" + ((lehrer.geburtsdatum == null) ? "" : lehrer.geburtsdatum)
					+ "__" + ((geschlecht == null) ? lehrer.geschlecht : geschlecht.kuerzel);
			// Füge die Lehrerstammdaten in die Map ein, damit dieser Datensatz zukünftig bei der Duplikaterkennung berücksichtigt wird
			// In der Map bereits existierende Stammdaten mit dem gleichen Key werden dabei zurückgegeben.
			final LehrerStatistikGesamt other = keys.put(key, lehrer);
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
