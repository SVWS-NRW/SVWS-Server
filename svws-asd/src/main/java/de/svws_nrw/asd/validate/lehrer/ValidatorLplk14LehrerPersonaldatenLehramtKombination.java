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
public final class ValidatorLplk14LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk14LehrerPersonaldatenLehramtKombination(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK14 Die Lehramtseinträge 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Erzieher/-in mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Erzieher/-in ohne sonderpädagogische Zusatzausbildung'.
		boolean lehramtId58Vorhanden = false;
		boolean lehramtId61Vorhanden = false;
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag58 = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag61 = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag58 != null && lehrerLehramtKatalogEintrag61 != null)
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag58.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId58Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag61.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId61Vorhanden = true;

		if (lehramtId58Vorhanden && lehramtId61Vorhanden) {
			this.addFehler(4,
					"Die Lehramtseinträge 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Erzieher/-in mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Erzieher/-in ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}

		return true;
	}
}
