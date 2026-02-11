import { ValidatorLsn11LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn11LehrerStammdatenNachname';
import { ValidatorLsn12LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn12LehrerStammdatenNachname';
import { ValidatorLsn14LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn14LehrerStammdatenNachname';
import { ValidatorLsn13LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn13LehrerStammdatenNachname';
import { ValidatorLsn17LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn17LehrerStammdatenNachname';
import { ValidatorLsn15LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn15LehrerStammdatenNachname';
import { ValidatorLsn16LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn16LehrerStammdatenNachname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsn10LehrerStammdatenNachname extends Validator {

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
		this._validatoren.add(new ValidatorLsn11LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn12LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn13LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn14LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn15LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn16LehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsn17LehrerStammdatenNachname(daten, kontext));
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
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsn10LehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsn10LehrerStammdatenNachname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsn10LehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsn10LehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsn10LehrerStammdatenNachname(obj: unknown): ValidatorLsn10LehrerStammdatenNachname {
	return obj as ValidatorLsn10LehrerStammdatenNachname;
}
