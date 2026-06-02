package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
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
	private final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> _beschaeftigungsartNotNull;

	/** Das Schuljahr */
	private final @NotNull int _schuljahr;
	private static final @NotNull String FEHLERTEXT = "Lehrer Beschäftigungsart: Der eingetragene Wert für das Feld 'Beschäftigungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param beschaeftigungsartNotNull     die Beschäftigungsart
	 * @param schuljahr
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> beschaeftigungsartNotNull,
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_beschaeftigungsartNotNull = beschaeftigungsartNotNull;
		_schuljahr = schuljahr.get();

		_validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, kontext));
		_validatoren.add(
				new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, pflichtstundensoll,
						kontext));

	}

	@Override
	protected boolean pruefe() {
		final @AllowNull LehrerBeschaeftigungsartKatalogEintrag lehrerBeschaeftigungsartKatalogEintrag = _beschaeftigungsartNotNull.get().daten(_schuljahr);
		if (lehrerBeschaeftigungsartKatalogEintrag == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
