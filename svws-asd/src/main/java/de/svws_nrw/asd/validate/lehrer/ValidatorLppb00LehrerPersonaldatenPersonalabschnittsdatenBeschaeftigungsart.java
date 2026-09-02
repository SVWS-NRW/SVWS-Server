package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschaeftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschaeftigungsart */
	private final @NotNull Supplier<@AllowNull Long> _idBeschaeftigungsart;
	private static final @NotNull String FEHLERTEXT = "Kein Wert im Feld 'beschaeftigungsart'.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idBeschaeftigungsart = idBeschaeftigungsart;

		_validatoren.add(
				new ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplierLong(idBeschaeftigungsart), pflichtstundensoll, einsatzstatus,
						kontext));

	}


	@Override
	public boolean pruefe() {
		// Bestimme die Beschäftigungsart.
		final Long idBeschaeftigungsart = _idBeschaeftigungsart.get();

		if (idBeschaeftigungsart == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
