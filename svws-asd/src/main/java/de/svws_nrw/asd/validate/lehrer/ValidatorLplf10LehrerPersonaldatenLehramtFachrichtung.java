package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob der eingetragene Wert für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<@AllowNull Long> _idFachrichtung;
	/** Die Katalog-ID des Lehramtes. */
	private final @NotNull Supplier<@AllowNull Long> _idLehramt;



	private static final @NotNull Set<LehrerLehramt> listeLehraemter = Set.of(LehrerLehramt.ID_08, LehrerLehramt.ID_09, LehrerLehramt.ID_10, LehrerLehramt.ID_11, LehrerLehramt.ID_12, LehrerLehramt.ID_14,
			LehrerLehramt.ID_27, LehrerLehramt.ID_31, LehrerLehramt.ID_35, LehrerLehramt.ID_90, LehrerLehramt.ID_96, LehrerLehramt.ID_98);

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idLehramt,
			final @NotNull Supplier<@AllowNull Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schuljahr = schuljahr;
		_idLehramt = idLehramt;
		_idFachrichtung = idFachrichtung;
	}

	@Override
	protected boolean pruefe() {

		final Long idFachrichtung = _idFachrichtung.get();
		final LehrerLehramt lehrerLehramt = LehrerLehramt.data().getWertByIDOrNull(_idLehramt.get());

		    // Prüft auf Lehramt und Fachrichtung
		    //Null Prüfung aufs Lehramt ist nötig, da es für diesen Fehler keinen 00er Prüfer gibt
			if (lehrerLehramt != null && listeLehraemter.contains(lehrerLehramt) && idFachrichtung != null) {
				addFehler(0,
						"Fachrichtung des Lehrers: Bei Lehrkräften, die über das angegebene Lehramt verfügen, darf das Feld 'Fachrichtung' nicht besetzt sein.");
				return false;
			}

			return true;
		}

}
