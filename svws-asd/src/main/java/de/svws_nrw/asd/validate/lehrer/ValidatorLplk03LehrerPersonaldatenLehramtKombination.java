package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule in Kombination zu anderen vorhandenen Lehrämtern aus.
 */
public final class ValidatorLplk03LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrer-Personaldaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk03LehrerPersonaldatenLehramtKombination(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK03 Neben dem Lehramtseintrag 'Lehramtsanwärter/-in / Studienreferendar/-in' dürfen keine weiteren Lehramtseinträge vorliegen. Bitte überprüfen Sie Ihre Angaben.
		boolean lehramtId98Vorhanden = false;
		boolean	anderesLehramtVorhanden = false;
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag = LehrerLehramt.ID_98.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag != null) {
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : lehrerPersonaldaten.lehraemter) {
			  if (lehrerLehramtKatalogEintrag.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
				  lehramtId98Vorhanden = true;
			  else
				  anderesLehramtVorhanden = true;
			}
		}

		if (lehramtId98Vorhanden && anderesLehramtVorhanden) {
			this.addFehler(3, "Neben dem Lehramtseintrag 'Lehramtsanwärter/-in / Studienreferendar/-in' dürfen keine weiteren Lehramtseinträge vorliegen. Bitte überprüfen Sie Ihre Angaben.");
			return false;
		}

		return true;
	}
}
