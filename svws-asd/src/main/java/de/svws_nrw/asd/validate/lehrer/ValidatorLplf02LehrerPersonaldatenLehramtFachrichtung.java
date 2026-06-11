package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob der eingetragene Wert für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(final @NotNull Supplier<Long> idFachrichtung, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;
	}

	@Override
	protected boolean pruefe() {
		final Long idFachrichtung = _idFachrichtung.get();

		// Holt den Core-Type-Wert oder null, falls die ID komplett unbekannt ist
		final @AllowNull LehrerFachrichtung wert = LehrerFachrichtung.data().getWertByIDOrNull(idFachrichtung);

		// Bestimmt den für das aktuelle Schuljahr gültigen Katalog-Eintrag (wird null, wenn wert bereits null ist)
		final int schuljahr = kontext().getSchuljahr();
		final @AllowNull LehrerFachrichtungKatalogEintrag eintragAktuell =
				(wert == null) ? null : LehrerFachrichtung.data().getEintragBySchuljahrUndWert(schuljahr, wert);

		// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
		if ((eintragAktuell == null) || (eintragAktuell.id != idFachrichtung)) {
			addFehler(0,
					"Lehrer Fachrichtung: Der eingetragene Wert für das Feld 'Fachrichtung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
