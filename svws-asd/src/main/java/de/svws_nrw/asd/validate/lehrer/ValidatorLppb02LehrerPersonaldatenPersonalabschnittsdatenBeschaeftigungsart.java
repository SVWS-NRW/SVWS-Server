package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Anrechnungsgrund im Katalog der Anrechnungsgründe existiert.
 */
public final class ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschaeftigungsart */
	private final @NotNull @NotNull @NotNull Supplier<@NotNull Long> _idBeschaeftigungsart;

	private static final @NotNull String FEHLERTEXT =
			"Lehrer Beschäftigungsart: Der eingetragene Wert für das Feld 'Beschäftigungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param pflichtstundensoll       das Pflichtstundensoll
	 * @param einsatzstatus            der Einsatzstatus
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull @NotNull Supplier<@NotNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idBeschaeftigungsart = idBeschaeftigungsart;

		final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> beschaeftigungsartNotNull =
				() -> LehrerBeschaeftigungsart.data().getWertByID(getNotNullSupplierLong(idBeschaeftigungsart).get());

		_validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, kontext));
		_validatoren.add(
				new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, pflichtstundensoll,
						kontext));

	}

	@Override
	public boolean pruefe() {
		if (!LehrerBeschaeftigungsart.data().isGueltig(_idBeschaeftigungsart.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
