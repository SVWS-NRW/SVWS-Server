package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Basisklasse für Validatoren.
 */
public abstract class Validator extends BasicValidator {

	/** Der vom Validator genutzte Kontext */
	private final @NotNull ValidatorKontext _kontext;


	/**
	 * Erstellt einen neuen Validator in dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext, in dem der Validator ausgeführt wird
	 */
	protected Validator(final @NotNull ValidatorKontext kontext) {
		super(ValidatorFehlerart.UNGENUTZT);
		_kontext = kontext;
		_defaultValidatorFehlerart = getValidatorFehlerart();
	}


	/**
	 * Wandelt einen Supplier für Object in einen Supplier für Object zurück, welcher keine null-Werte liefert,
	 * sondern eine {@link NullPointerException} wirft.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Objekte liefern kann
	 * @param <T>        der Datentyp, der vom Supplier geliefert wird
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert, sondern ggf. eine NullPointerException wirft.
	 */
	@SuppressWarnings("static-method")
	protected final <T> @NotNull Supplier<T> getNotNullObjectSupplier(final @NotNull Supplier<@AllowNull T> supplier) {
		return () -> {
			final T value = supplier.get();
			if (value == null) {
				throw new NullPointerException();
			}
			return value;
		};
	}


	/**
	 * Wandelt einen Supplier für Strings in einen Supplier für Strings zurück, welcher keine null-Werte liefert,
	 * sondern nur leere Strings.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Strings liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	@SuppressWarnings("static-method")
	protected final @NotNull Supplier<String> getNotNullSupplier(final @NotNull Supplier<@AllowNull String> supplier) {
		return () -> {
			final String value = supplier.get();
			return value == null ? "" : value;
		};
	}


	/**
	 * Wandelt einen Supplier für Integer in einen Supplier für Integer zurück, welcher keine null-Werte liefert,
	 * sondern -1 falls der Integer-Wert null ist.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Integer liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	@SuppressWarnings("static-method")
	protected final @NotNull Supplier<Integer> getNotNullSupplierInteger(final @NotNull Supplier<@AllowNull Integer> supplier) {
		return () -> {
			final Integer value = supplier.get();
			return value == null ? -1 : value;
		};
	}


	/**
	 * Wandelt einen Supplier für Long in einen Supplier für Long zurück, welcher keine null-Werte liefert,
	 * sondern -1 falls der Long-Wert null ist.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Long liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	@SuppressWarnings("static-method")
	protected final @NotNull Supplier<Long> getNotNullSupplierLong(final @NotNull Supplier<@AllowNull Long> supplier) {
		return () -> {
			final Long value = supplier.get();
			return value == null ? -1L : value;
		};
	}

	/**
	 * Wandelt einen Supplier für Double in einen Supplier für Double um, welcher keine null-Werte liefert,
	 * sondern -1 falls der Double-Wert null ist.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Double liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	@SuppressWarnings("static-method")
	protected final @NotNull Supplier<Double> getNotNullSupplierDouble(final @NotNull Supplier<@AllowNull Double> supplier) {
		return () -> {
			final Double value = supplier.get();
			return value == null ? -1 : value;
		};
	}


	/**
	 * Wandelt einen Supplier für Strings in einen Supplier für Strings zurück, welcher keine null-Werte liefert,
	 * sondern nur leere Strings.
	 *
	 * @param supplier   der Supplier, welcher auch null-Werte für Strings liefern kann
	 *
	 * @return ein Supplier, welcher keine Null-Werte liefert.
	 */
	@SuppressWarnings("static-method")
	protected final @NotNull Supplier<@AllowNull DateManager> getDateManagerSupplier(final @NotNull Supplier<@AllowNull String> supplier) {
		return () -> {
			final String value = supplier.get();
			if (value == null) {
				return null;
			}
			try {
				return DateManager.from(value);
			} catch (@SuppressWarnings("unused") final InvalidDateException e) {
				return null;
			}
		};
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
	 * Gibt den zugehörigen ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public @NotNull ValidatorManager getValidatorManager() {
		return _kontext.getValidatorManager();
	}


	/**
	 * Prüft, ob der Validator aktiv ist.
	 *
	 * @return true, falls der Validator aktiv ist
	 */
	@Override
	protected boolean isActive() {
		return _kontext.getValidatorManager().isValidatorActiveInSchuljahr(_kontext.getSchuljahr(), this.getClass().getCanonicalName());
	}


	/**
	 * Gibt die Fehler des Validators als unmodifiable List zurück.
	 *
	 * @return die Liste der Fehler als unmodifiable List
	 */
	@Override
	public @NotNull List<ValidatorFehler> getFehler() {
		return new ArrayList<>(_fehler);
	}


	/**
	 * Die Fehlerart, welche diesem speziellen Validator zugeordnet ist.
	 *
	 * @return die Fehlerart
	 */
	@Override
	public @NotNull ValidatorFehlerart getValidatorFehlerart() {
		return _kontext.getValidatorManager().getFehlerartBySchuljahrAndValidatorClass(_kontext.getSchuljahr(), this.getClass());
	}


	/**
	 * Gibt das Fehlercode-Präfix zurück, welcher diesem speziellen Validator zugeordnet ist.
	 *
	 * @return das Fehlercode-Präfix
	 */
	@Override
	public @NotNull String getFehlercodePraefix() {
		return _kontext.getValidatorManager().getFehlercodePraefixBySchuljahrAndValidatorClass(_kontext.getSchuljahr(), this.getClass());
	}


}
