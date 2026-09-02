package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idRechtsverhaeltnis  	das Rechtsverhaeltnis
	 * @param idBeschaeftigungsart  die Beschäftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

//		getNotNullSupplierObject

	@NotNull Supplier<@NotNull DateManager> datumUebergabe = getNotNullSupplierObject(getDateManagerSupplier(() -> "1971-06-04"));

		// Hier kann nicht nur die Vorbedingung 02 geprüft werden, da die ID's "null" sein könnten, muss vorher auch 00 und 01 geprüft werden
		if (new ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, () -> null, () -> null, kontext).pruefe()
				&& new ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplierLong(idBeschaeftigungsart), () -> null, () -> null, kontext).pruefe()
				&& new ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplierLong(idBeschaeftigungsart), () -> null, () -> null, kontext).pruefe()
				&& new ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> -1L, () -> null, idRechtsverhaeltnis, datumUebergabe, kontext).pruefe()
				&& new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> -1L, () -> null, getNotNullSupplierLong(idRechtsverhaeltnis), datumUebergabe, kontext).pruefe()
				&& new ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> -1L, () -> null, getNotNullSupplierLong(idRechtsverhaeltnis), datumUebergabe, kontext).pruefe()
				) {

			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> lehrerRechtsverhaeltnis =
					() -> LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis.get());
			final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> lehrerBeschaeftigungsart =
					() -> LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart.get());

			if (lehrerRechtsverhaeltnis.get() != null && lehrerBeschaeftigungsart.get() != null) {
				_validatoren.add(new ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb16LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb17LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
				_validatoren.add(new ValidatorLpprb18LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(getNotNullSupplierObject(lehrerRechtsverhaeltnis), getNotNullSupplierObject(lehrerBeschaeftigungsart),
						kontext));
			}
		}
	}


	@Override
	protected boolean pruefe() {
		return true;
	}

}
