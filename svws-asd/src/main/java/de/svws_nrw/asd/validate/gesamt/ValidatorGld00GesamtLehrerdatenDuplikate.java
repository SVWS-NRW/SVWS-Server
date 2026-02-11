package de.svws_nrw.asd.validate.gesamt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf Namen, Vornamen, Geschlecht und Geburtsdatum vorkommen.
 */
public final class ValidatorGld00GesamtLehrerdatenDuplikate extends Validator {

	private final @NotNull List<LehrerStammdaten> listLehrerStammdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrerStammdaten      die Liste aller Lehrerstammdaten
	 * @param kontext             		der Kontext des Validators
	 */
	public ValidatorGld00GesamtLehrerdatenDuplikate(final @NotNull List<LehrerStammdaten> listLehrerStammdaten, final @NotNull ValidatorKontext kontext) {
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
		final @NotNull Set<Long> ids = new HashSet<>();
		for (final LehrerStammdaten lehrer : listLehrerStammdaten) {

			// Prüfe, ob in der Liste eine ID doppelt enthalten ist. Dies wäre eine Fehler bei der Zusammenstellung der Liste
			final boolean istNeu = ids.add(lehrer.id);
			if (!istNeu) {
				this.addFehler(0, "Lehrkäfte: Die ID " + lehrer.id + " kommt in der Liste mehrfach vor.");
				success = false;
			}

		}
		return success;
	}

}
