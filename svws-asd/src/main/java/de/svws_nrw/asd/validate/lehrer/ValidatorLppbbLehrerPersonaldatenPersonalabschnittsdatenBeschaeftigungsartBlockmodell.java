package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator für Beschäftigungsart = "TS" (Teilzeit im Blockmodell).
 */
public final class ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell extends Validator {

	/**
	 * Erstellt einen neuen Validator.
	 *
	 * @param pflichtstundensoll   der Pflichtstundensoll
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param einsatzstatus        der Einsatz-Status
	 * @param mehrleistungen       die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen     die Liste mit den Einträgen zu Minderleistungen
	 * @param kontext  der Kontext der Validierung
	 */
	public ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
			final @NotNull Supplier<@AllowNull String> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull String> einsatzstatus,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(pflichtstundensoll,
				beschaeftigungsart, einsatzstatus, mehrleistungen, minderleistungen, kontext));

	}

	@Override
	protected boolean pruefe() {
		return true;
	}
}
