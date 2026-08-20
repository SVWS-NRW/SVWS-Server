package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für eine KatalogID des CoreType Religion aus.
 * Es wird überprüft, ob die ID  im Katalog existiert und somit zulässig ist.
 */
public final class ValidatorIka01IntKatalogKonfessionenAsdKatalog extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<Long> _idKatalog;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorIka01IntKatalogKonfessionenAsdKatalog(final @NotNull Supplier<Long> idKatalog, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIka02IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idKatalog = _idKatalog.get();

		if (Religion.data().getSchluesselByIDOrNull(idKatalog) == null) {
			addFehler(0, "Konfession des Schülers: Das Feld 'Konfession ASD-Kürzel' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
