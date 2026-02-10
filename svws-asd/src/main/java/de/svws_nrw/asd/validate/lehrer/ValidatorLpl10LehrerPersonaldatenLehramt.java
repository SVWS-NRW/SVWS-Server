package de.svws_nrw.asd.validate.lehrer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpl10LehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLpl10LehrerPersonaldatenLehramt(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		// Fehlerkürzel: LPL2 Überprüfung, ob bei einer Lehrerkraft ein Lehramt mehrmals eingetragen wurde
		final @NotNull Map<Long, LehrerLehramtEintrag> lehramtMap = new HashMap<>();

		for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : this.lehraemter.get()) {
			// Ermittlung des aktuell gültigen Lehramtstextes aus der LehrerLehramt.json-Datei
			if (lehramtMap.put(lehrerLehramtEintrag.idKatalogLehramt, lehrerLehramtEintrag) != null) {
				try {
					this.addFehler(2, "Das Lehramt '" + LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).text + "' ist mehrfach eingetragen. Bitte löschen Sie die überflüssigen Einträge.");
				} catch (@SuppressWarnings("unused") CoreTypeException e) {
					this.addFehler(2, "Das Lehramt '" + lehrerLehramtEintrag.idKatalogLehramt + "' ist mehrfach eingetragen. Bitte löschen Sie die überflüssigen Einträge.");
				}
				return false;
			}
		}


		return true;
	}

}
