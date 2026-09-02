import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb17LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb17LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLpprb11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb16LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb16LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb18LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb18LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLpprb12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprb12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idRechtsverhaeltnis  	das Rechtsverhaeltnis
	 * @param idBeschaeftigungsart  die Beschäftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(idRechtsverhaeltnis: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		let datumUebergabe: Supplier<DateManager> = this.getNotNullSupplierObject(this.getDateManagerSupplier({ get: () => "1971-06-04" }));
		if (new ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, { get: () => null }, { get: () => null }, kontext).pruefe() && new ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(this.getNotNullSupplierLong(idBeschaeftigungsart), { get: () => null }, { get: () => null }, kontext).pruefe() && new ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(this.getNotNullSupplierLong(idBeschaeftigungsart), { get: () => null }, { get: () => null }, kontext).pruefe() && new ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis({ get: () => -1 }, { get: () => null }, idRechtsverhaeltnis, datumUebergabe, kontext).pruefe() && new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis({ get: () => -1 }, { get: () => null }, this.getNotNullSupplierLong(idRechtsverhaeltnis), datumUebergabe, kontext).pruefe() && new ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis({ get: () => -1 }, { get: () => null }, this.getNotNullSupplierLong(idRechtsverhaeltnis), datumUebergabe, kontext).pruefe()) {
			const lehrerRechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null> = { get: () => LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis.get()) };
			const lehrerBeschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null> = { get: () => LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart.get()) };
			if (lehrerRechtsverhaeltnis.get() !== null && lehrerBeschaeftigungsart.get() !== null) {
				this._validatoren.add(new ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb16LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb17LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
				this._validatoren.add(new ValidatorLpprb18LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(this.getNotNullSupplierObject(lehrerRechtsverhaeltnis), this.getNotNullSupplierObject(lehrerBeschaeftigungsart), kontext));
			}
		}
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(obj: unknown): ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart {
	return obj as ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart;
}
