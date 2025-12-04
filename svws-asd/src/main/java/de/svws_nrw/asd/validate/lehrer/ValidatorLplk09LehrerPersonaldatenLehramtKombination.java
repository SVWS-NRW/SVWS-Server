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
public final class ValidatorLplk09LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk09LehrerPersonaldatenLehramtKombination(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK09 Neben dem Lehramtseintrag 'Alltagshelfern/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.
		boolean lehramtId65Vorhanden = false;
		boolean	anderesLehramtVorhanden = false;
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag = LehrerLehramt.ID_65.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag != null) {
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId65Vorhanden = true;
				else
					anderesLehramtVorhanden = true;
			}
		}

		if (lehramtId65Vorhanden && anderesLehramtVorhanden) {
			this.addFehler(9, "Neben dem Lehramtseintrag 'Alltagshelfern/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}

		return true;
	}
}
