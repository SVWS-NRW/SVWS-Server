package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.annotations.TsObject;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Basisklasse für Validatoren.
 *
 * @param <T>  der Typ der zu validierenden Daten, welcher durch die abgeleitete Klasse bestimmt ist
 */
public abstract class Validator<@TsObject T> {

	/** Die zu validierenden Daten */
	private final @NotNull T _daten;

	/** Der vom Validator genutzte Kontext */
	private final @NotNull ValidatorKontext _kontext;

	/** Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden. */
	protected final @NotNull List<Validator<?>> _validatoren = new ArrayList<>();

	/** Eine Liste mit Fehlern bei der Validierung */
	private final @NotNull List<ValidatorFehler<?>> _fehler = new ArrayList<>();

    /** Die DTO-Klasse, welche von dem Validator geprüft wurde */
    private final @NotNull Class<T> _dtoClass;


	/**
	 * Erstellt einen neuen Validator in dem übegebenen Kontext
	 *
	 * @param daten     die zu validierenden Daten
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	@SuppressWarnings("unchecked")
	protected Validator(final @NotNull T daten, final @NotNull ValidatorKontext kontext) {
		this._daten = daten;
		this._kontext = kontext;
		this._dtoClass = (@NotNull Class<T>) daten.getClass();
	}


	/**
	 * Gibt den Kontext des Validators zurück.
	 *
	 * @return der Kontext des Validators
	 */
	public @NotNull ValidatorKontext kontext() {
		return _kontext;
	}

	/**
	 * Gibt die zu validierenden Daten zurück.
	 *
	 * @return die zu validierenden Daten
	 */
	public @NotNull T daten() {
		return _daten;
	}


	/**
	 * Gibt die Klasse der zu validierenden Daten zurück.
	 *
	 * @return die Klasse der zu validierenden Daten
	 */
	public @NotNull Class<T> getDTOClass() {
		return _dtoClass;
	}


	/**
	 * Führt die Prüfungen des Validators aus. Dabei wird zunächst die Fehlerliste
	 * geleert und durch die ausführenden Prüfroutinen befüllt.
	 *
	 * @return true, falls alle Prüfroutinen erfolgreich waren, und ansonsten false
	 */
	public final boolean run() {
		boolean success = true;
		_fehler.clear();
		if (_kontext.getValidatorManager().isValidatorActiveInSchuljahr(_kontext.getSchuljahr(), this.getClass().getCanonicalName())) {
			for (final @NotNull Validator<?> validator : _validatoren) {
				if (!validator.run())
					success = false;
				_fehler.addAll(validator._fehler);
			}
			// Berücksichtige auch Exceptions bei der Prüfung dieses Validators
			try {
				if (!this.pruefe())
					success = false;
			} catch (final Exception e) {
				addFehler("Unerwarteter Fehler bei der Validierung: " + e.getMessage());
			}
		}
		return success;
	}

	/**
	 * Erstellt einen neuen Fehler mit der übergebenen Fehlermeldung
	 *
	 * @param fehlermeldung   die Fehlermeldung
	 */
	protected void addFehler(final @NotNull String fehlermeldung) {
		_fehler.add(new ValidatorFehler<>(this, fehlermeldung));
	}

	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	public @NotNull List<ValidatorFehler<?>> getFehler() {
		return new ArrayList<>(_fehler);
	}

	/**
	 * Führt die Prüfung der Daten aus. Befüllt ggf. die Fehlerliste, falls
	 * es zu Fehlern kommt.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	protected abstract boolean pruefe();

}
