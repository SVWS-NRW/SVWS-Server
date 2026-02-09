package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule in Kombination zu anderen vorhandenen Lehrämtern aus.
 */
public final class ValidatorLplk16LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk16LehrerPersonaldatenLehramtKombination(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK16 Die Lehramtseinträge 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)'.
		boolean lehramtId57Vorhanden = false;
		boolean lehramtId60Vorhanden = false;
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag57 = LehrerLehramt.ID_57.daten(this.kontext().getSchuljahr());
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag60 = LehrerLehramt.ID_60.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag57 != null && lehrerLehramtKatalogEintrag60 != null)
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag57.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId57Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag60.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId60Vorhanden = true;

		if (lehramtId57Vorhanden && lehramtId60Vorhanden) {
			this.addFehler(6,
					"Die Lehramtseinträge 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)'.");
			return false;
		}

		return true;
	}
}
