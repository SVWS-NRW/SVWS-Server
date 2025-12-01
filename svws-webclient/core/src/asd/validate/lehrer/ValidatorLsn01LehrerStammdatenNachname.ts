import { ValidatorLsn08LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn08LehrerStammdatenNachname';
import { ValidatorLsn02LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn02LehrerStammdatenNachname';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorLsn03LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn03LehrerStammdatenNachname';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLsn04LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn04LehrerStammdatenNachname';
import { ValidatorLsn05LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn05LehrerStammdatenNachname';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLsn07LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn07LehrerStammdatenNachname';
import { ValidatorLsn06LehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsn06LehrerStammdatenNachname';

export class ValidatorLsn01LehrerStammdatenNachname extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
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
		const nachname: string | null = this.daten.nachname;
		if (JavaString.isBlank(nachname.trim())) {
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
