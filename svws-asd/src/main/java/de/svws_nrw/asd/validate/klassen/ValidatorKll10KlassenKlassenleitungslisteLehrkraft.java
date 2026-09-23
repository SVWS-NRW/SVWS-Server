package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator Kl10: Prüft, ob Klassenleitungen eingetragen sind.
 */
public final class ValidatorKll10KlassenKlassenleitungslisteLehrkraft extends Validator {

	private final @NotNull Supplier<@NotNull List<@AllowNull Long>> _klassenLeitungen;
	/** Die Liste der Lehrer. */
	private final @NotNull Supplier<@NotNull List<Long>> _listLehrer;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param listLehrer         die Liste der Lehrer
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorKll10KlassenKlassenleitungslisteLehrkraft(
			final @NotNull Supplier<@NotNull List<@AllowNull Long>> klassenLeitungen,
		    final @NotNull Supplier<@NotNull List<Long>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenLeitungen = klassenLeitungen;
		this._listLehrer = listLehrer;

	}
// müssen wir hier dauernd auf allowNull abfragen? auch für die listLehrer???
	@Override
	protected boolean pruefe() {
		final @NotNull List<@AllowNull Long> klassenLeitungen = _klassenLeitungen.get();
		final @NotNull List<Long> listLehrer = _listLehrer.get();

//		if (klassenLeitungen == null || listLehrer == null) {
//			return true;
//		}

		for (final Long klassenleitung : klassenLeitungen) {
			boolean gefunden = false;
			for (final Long idLehrer : listLehrer) {
				if (klassenleitung == idLehrer) {
					gefunden = true;
				}
			}
			if (!gefunden) {
				addFehler(0,
						"Leitung der Klasse: Die id des Eintrages muss einer gültigen Lehrer ID zuordenbar sein.");
				return false;
			}
		}
		return true;
	}
}
