import { ValidatorLsn08LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn08LehrerStammdatenNachname';
import { ValidatorLsn02LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn02LehrerStammdatenNachname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorLsn03LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn03LehrerStammdatenNachname';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLsn04LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn04LehrerStammdatenNachname';
import { ValidatorLsn05LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn05LehrerStammdatenNachname';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLsn07LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn07LehrerStammdatenNachname';
import { ValidatorLsn06LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn06LehrerStammdatenNachname';

export class ValidatorLsn01LehrerStammdatenNachname extends Validator {

	/**
	 * Der Lehrer-Nachname
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsn02LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn03LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn04LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn05LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn06LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn07LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn08LehrerStammdatenNachname(daten, kontext));
	}

	protected pruefe(): boolean {
		const nachname: string = this.daten.get();
		if (JavaString.isBlank(nachname)) {
			this.addFehler(1, "Nachname der Lehrkraft: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn01LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn01LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn01LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn01LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn01LehrerStammdatenNachname(obj: unknown): ValidatorLsn01LehrerStammdatenNachname {
	return obj as ValidatorLsn01LehrerStammdatenNachname;
}
