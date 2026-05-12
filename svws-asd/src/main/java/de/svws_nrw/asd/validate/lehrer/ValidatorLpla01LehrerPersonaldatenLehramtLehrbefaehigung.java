package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Lehrbefähigung zulässig, bzw. in der JSON-Datei vorhanden ist.
 */
public final class ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;

		_validatoren.add(new ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerLehramtEintrag> liste = this.lehraemter.get();

		if (liste == null) {
			addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
			return false;
		}

		for (final LehrerLehramtEintrag lehrerLehramtEintrag : liste) {
			for (final LehrerLehrbefaehigungEintrag lehrerLehrbefaehigungEintrag : lehrerLehramtEintrag.lehrbefaehigungen) {
				// Prüfung, ob die ID im Katalog vorhanden ist.
				if (LehrerLehrbefaehigung.data().getWertByIDOrNull(lehrerLehrbefaehigungEintrag.idLehrbefaehigung) == null) {
					addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
					return false;
				}
			}
		}

		return true;
	}
}
