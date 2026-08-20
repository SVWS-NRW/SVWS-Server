package de.svws_nrw.asd.validate.klassen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KCKP10: Prüft, ob für alle Klassen eine Kombination aus Jahrgang und Parallelität mehrfach vorkommt.
 */
public final class ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet extends Validator {

	private final @NotNull Supplier<List<KlassenStatistikGesamt>> listKlassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param listKlassenStatistikGesamt   die Liste aller Klassendaten
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet(
			final @NotNull Supplier<List<KlassenStatistikGesamt>> listKlassenStatistikGesamt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listKlassenDaten = listKlassenStatistikGesamt;
	}

	@Override
	protected boolean pruefe() {
		final List<KlassenStatistikGesamt> klassendatenList = listKlassenDaten.get();

		if (klassendatenList.isEmpty()) {
			return true;
		}

		// Durchlaufen der Klassendatenliste und Speichern einer Kombination aus Jahrgang und Parallelität in einem Set, um Duplikate zu erkennen.
		final @NotNull Set<String> ids = new HashSet<>();
		for (final KlassenStatistikGesamt klassendaten : klassendatenList) {
			if (klassendaten.idJahrgang == null) {
				return true;
			}

			final String idJahrgangText = klassendaten.idJahrgang.toString();
			final String parallelitaet = klassendaten.parallelitaet;

			// Hinzufügen einer bereits vorhandenen Kombination aus Jahrgang und Parallelität erzeugt einen Fehler.
			final boolean unique = ids.add(idJahrgangText + parallelitaet);
			if (!unique) {
				this.addFehler(0, "Die Kombination von Klassenjahrgang und Parallelitaet existiert bereits.");
				return false;
			}
		}

		return true;
	}

}
