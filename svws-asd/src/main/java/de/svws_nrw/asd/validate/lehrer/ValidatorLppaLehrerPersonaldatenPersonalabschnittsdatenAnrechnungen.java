package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Sammel-Validator für die Anrechnungsstunden der Lehrer-Personalabschnittsdaten (LPPA00 bis LPPA11).
 */
public final class ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Erstellt einen neuen Sammel-Validator für Anrechnungsdaten.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
