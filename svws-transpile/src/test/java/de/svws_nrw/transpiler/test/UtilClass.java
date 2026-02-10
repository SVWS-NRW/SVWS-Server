package de.svws_nrw.transpiler.test;

import java.util.function.Supplier;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Testklasse für den Transpiler
 */
public class UtilClass {

	private final @NotNull Supplier<String> sup;

	/**
	 * Konstruktor
	 *
	 * @param sup   a supplier
	 */
	public UtilClass(final @NotNull Supplier<String> sup) {
		this.sup = sup;
	}

	/**
	 * Eine Test-Methode
	 *
	 * @return ein Supplier
	 */
	public Supplier<String> getSup() {
		return sup;
	}

}
