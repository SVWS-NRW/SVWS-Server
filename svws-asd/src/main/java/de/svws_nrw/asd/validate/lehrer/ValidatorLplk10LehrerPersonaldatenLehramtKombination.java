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
public final class ValidatorLplk10LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk10LehrerPersonaldatenLehramtKombination(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;

	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK10 Neben dem Lehramtseintrag 'Schulverwaltungsassistenten/-innen' sollten keine weiteren Lehramtseinträge vorliegen.
		boolean lehramtId70Vorhanden = false;
		boolean anderesLehramtVorhanden = false;
		final LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag = LehrerLehramt.ID_70.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag != null)
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag.id == LehrerLehramt.data()
						.getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId70Vorhanden = true;
				else
					anderesLehramtVorhanden = true;

		if (lehramtId70Vorhanden && anderesLehramtVorhanden) {
			this.addFehler(0,
					"Neben dem Lehramtseintrag 'Schulverwaltungsassistenten/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}

		return true;
	}
}
