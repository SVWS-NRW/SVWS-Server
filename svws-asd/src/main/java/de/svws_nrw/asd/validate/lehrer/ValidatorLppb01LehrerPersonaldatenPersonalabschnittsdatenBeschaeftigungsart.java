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
public final class ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschaeftigungsart */
	private final @NotNull Supplier<@NotNull Long> _idBeschaeftigungsart;
	private static final @NotNull String FEHLERTEXT = "Lehrer Beschäftigungsart: Das Feld 'Beschäftigungsart' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@NotNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idBeschaeftigungsart = idBeschaeftigungsart;

		final @NotNull Integer schuljahr2 = kontext.getSchuljahr();
		final @NotNull Supplier<Integer> schuljahr = () -> schuljahr2;

		_validatoren.add(
				new ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, pflichtstundensoll,
						einsatzstatus,
						kontext));

	}

	@Override
	public boolean pruefe() {
		// Bestimme die Beschäftigungsart.
		final @NotNull Long idBeschaeftigungsart = _idBeschaeftigungsart.get();
		final @AllowNull LehrerBeschaeftigungsart ba = LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart);

		if (ba == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
