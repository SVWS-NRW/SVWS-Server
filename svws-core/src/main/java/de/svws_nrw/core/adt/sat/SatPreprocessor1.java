package de.svws_nrw.core.adt.sat;

import java.util.function.Function;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public final class SatPreprocessor1 implements Function<@NotNull SatInput, @NotNull SatOutput> {

	private final @NotNull Function<@NotNull SatInput, @NotNull SatOutput> next;

	/**
	 * Konstruktor.
	 *
	 * @param next Der nächste Preprocessor oder SatSolver
	 */
	public SatPreprocessor1(@NotNull final Function<@NotNull SatInput, @NotNull SatOutput> next) {
		super();
		this.next = next;
	}

	@Override
	public @NotNull SatOutput apply(@NotNull final SatInput t) {
		return next.apply(t);
	}



}
