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
public final class ValidatorLplk15LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk15LehrerPersonaldatenLehramtKombination(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK15 Die Lehramtseinträge 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung' und 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung'.
		boolean lehramtId59Vorhanden = false;
		boolean lehramtId62Vorhanden = false;
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag59 = LehrerLehramt.ID_59.daten(this.kontext().getSchuljahr());
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag62 = LehrerLehramt.ID_62.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag59 != null && lehrerLehramtKatalogEintrag62 != null)
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag59.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId59Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag62.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId62Vorhanden = true;

		if (lehramtId59Vorhanden && lehramtId62Vorhanden) {
			this.addFehler(5,
					"Die Lehramtseinträge 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung' und 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}

		return true;
	}
}
