package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die KatalogID aus.
 * Es wird überprüft, ob das Feld für die KatalogID nicht leer ist.
 */
public final class ValidatorIka00IntKatalogKonfessionenAsdKatalog extends Validator {

	/** Die Katalog-ID. */
	private final @NotNull Supplier<Long> _idKatalog;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorIka00IntKatalogKonfessionenAsdKatalog(final @NotNull Supplier<Long> idKatalog, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIka01IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));

	}

	@Override
	protected boolean pruefe() {
		final Long idKatalog = _idKatalog.get();

		if (idKatalog == null) {
			addFehler(0, "Konfession des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
