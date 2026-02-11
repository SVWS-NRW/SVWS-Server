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
public final class ValidatorLplk02LehrerPersonaldatenLehramtKombination extends Validator {

	/** Die Lehrer-Personaldaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplk02LehrerPersonaldatenLehramtKombination(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPLK02 Die Lehramtseinträge 'Schulkindergärtner/-in', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)', 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht in Kombination mit anderen als diesen Lehramtseinträgen vorliegen.
		boolean lehramtId56Vorhanden = false;
		boolean lehramtId57Vorhanden = false;
		boolean lehramtId58Vorhanden = false;
		boolean lehramtId60Vorhanden = false;
		boolean lehramtId61Vorhanden = false;
		boolean	anderesLehramtVorhanden = false;

		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag56 = LehrerLehramt.ID_56.daten(this.kontext().getSchuljahr());
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag57 = LehrerLehramt.ID_57.daten(this.kontext().getSchuljahr());
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag58 = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag60 = LehrerLehramt.ID_60.daten(this.kontext().getSchuljahr());
		LehrerLehramtKatalogEintrag lehrerLehramtKatalogEintrag61 = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());

		if (lehrerLehramtKatalogEintrag56 != null && lehrerLehramtKatalogEintrag57 != null && lehrerLehramtKatalogEintrag58 != null && lehrerLehramtKatalogEintrag60 != null && lehrerLehramtKatalogEintrag61 != null) {
			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag56.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId56Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag57.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
				 	lehramtId57Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag58.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId58Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag60.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId60Vorhanden = true;
				else if (lehrerLehramtKatalogEintrag61.id == LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId61Vorhanden = true;
				else
					anderesLehramtVorhanden = true;
			}
		}

		if ((lehramtId56Vorhanden || lehramtId57Vorhanden || lehramtId58Vorhanden || lehramtId60Vorhanden || lehramtId61Vorhanden) && anderesLehramtVorhanden) {
			this.addFehler(2, "Die Lehramtseinträge 'Schulkindergärtner/-in', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)', 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht in Kombination mit anderen als diesen Lehramtseinträgen vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}

		return true;
	}
}
