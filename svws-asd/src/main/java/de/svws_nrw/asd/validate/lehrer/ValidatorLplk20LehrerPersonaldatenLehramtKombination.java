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
public final class ValidatorLplk20LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk20LehrerPersonaldatenLehramtKombination(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK20 Neben dem Lehramtseintrag 'Studierende' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.
		boolean lehramtId90Vorhanden = false;
		boolean anderesLehramtVorhanden = false;
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag = LehrerLehramt.ID_90.daten(kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag != null)
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : lehraemter.get())
				if (lehrerLehramtKatalogEintrag.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId90Vorhanden = true;
				else
					anderesLehramtVorhanden = true;

		if (lehramtId90Vorhanden && anderesLehramtVorhanden) {
			addFehler(10, "Neben dem Lehramtseintrag 'Studierende' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}

		return true;
	}
}
