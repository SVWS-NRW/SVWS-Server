package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

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

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<@AllowNull Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 **/

	public ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idLehramt,
   		    final @NotNull Supplier<@AllowNull Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schuljahr = schuljahr;
		_idFachrichtung = idFachrichtung;


		_validatoren.add(new ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung,  kontext));
		_validatoren.add(new ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung,  kontext));
		_validatoren.add(new ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung(schuljahr, idFachrichtung,  kontext));
	}

	@Override
	protected boolean pruefe() {

		final Long idFachrichtung = _idFachrichtung.get();

		//Null Prüfung ist nötig, da es für diesen Fehler keinen 00er Prüfer gibt
		if (idFachrichtung != null) {
			// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
			if (!LehrerFachrichtung.data().isGueltig(_idFachrichtung.get(), _schuljahr.get())) {
				addFehler(0,
						"Lehrer Fachrichtung: Der eingetragene Wert für das Feld 'Fachrichtung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

}
