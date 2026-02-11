package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpl03LehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull DateManager geburtsdatum;

	private @NotNull Set<LehrerLehramt> regulaereLehraemter = Set.of(LehrerLehramt.ID_00, LehrerLehramt.ID_01, LehrerLehramt.ID_02, LehrerLehramt.ID_04, LehrerLehramt.ID_08,
			LehrerLehramt.ID_09, LehrerLehramt.ID_10, LehrerLehramt.ID_11, LehrerLehramt.ID_12, LehrerLehramt.ID_14, LehrerLehramt.ID_15, LehrerLehramt.ID_16,
			LehrerLehramt.ID_17, LehrerLehramt.ID_19, LehrerLehramt.ID_20, LehrerLehramt.ID_21, LehrerLehramt.ID_24, LehrerLehramt.ID_25, LehrerLehramt.ID_27,
			LehrerLehramt.ID_29, LehrerLehramt.ID_30, LehrerLehramt.ID_31, LehrerLehramt.ID_35, LehrerLehramt.ID_40, LehrerLehramt.ID_50, LehrerLehramt.ID_51,
			LehrerLehramt.ID_52, LehrerLehramt.ID_53, LehrerLehramt.ID_54, LehrerLehramt.ID_55, LehrerLehramt.ID_96);

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param geburtsdatum          das Geburtsdatum des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLpl03LehrerPersonaldatenLehramt(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
		this.geburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPL3 Überprüfung, ob bei einer jungen Lehrerkraft ein 'reguläres' Lehramt vorliegt
		if (geburtsdatum.getJahr() >= 2003 && geburtsdatum.getJahr() <= 2006) {
			for (final LehrerLehramtEintrag lehrerLehramtEintrag2 : lehrerPersonaldaten.lehraemter) {
				final LehrerLehramt lehrerLehramt2 = LehrerLehramt.data().getWertByIDOrNull(lehrerLehramtEintrag2.idKatalogLehramt);
				if (lehrerLehramt2 == null)
					continue;
				if (regulaereLehraemter.contains(lehrerLehramt2)) {
					try {
						this.addFehler(3, "Für das Lehramt '" + LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag2.idKatalogLehramt).text + "' ist die Lehrkraft sehr jung. Wenn das Alter der Lehrkraft korrekt ist, sollte das eingetragene Lehramt überprüft werden. Bitte verwenden Sie die 'regulären' Lehrämter nur dann, wenn eine entsprechende abgeschlossene Ausbildung vorliegt. Wenn es sich um einen Studierenden handelt, der neben seinem Studium als Lehrkraft tätig ist, verwenden sie bitte das Lehramt 'Studierende'. Ansonsten tragen Sie bitte das Lehramt 'Sonstiges' ein. ");
					} catch (@SuppressWarnings("unused") CoreTypeException e) {
						this.addFehler(3, "Für das Lehramt mit der ID '" + lehrerLehramtEintrag2.idKatalogLehramt + "' ist die Lehrkraft sehr jung. Wenn das Alter der Lehrkraft korrekt ist, sollte das eingetragene Lehramt überprüft werden. Bitte verwenden Sie die 'regulären' Lehrämter nur dann, wenn eine entsprechende abgeschlossene Ausbildung vorliegt. Wenn es sich um einen Studierenden handelt, der neben seinem Studium als Lehrkraft tätig ist, verwenden sie bitte das Lehramt 'Studierende'. Ansonsten tragen Sie bitte das Lehramt 'Sonstiges' ein. ");
					}
					return false;
				}
			}
		}

		return true;
	}

}
