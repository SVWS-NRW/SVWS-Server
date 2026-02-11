import { ValidatorLsgLehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsgLehrerStammdatenGeschlecht';
import { ValidatorLskLehrerStammdatenKuerzel } from '../../../asd/validate/lehrer/ValidatorLskLehrerStammdatenKuerzel';
import { ValidatorLsdLehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsdLehrerStammdatenGeburtsdatum';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorLsnLehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsnLehrerStammdatenNachname';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLsvLehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsvLehrerStammdatenVorname';

export class ValidatorLsLehrerStammdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<LehrerStammdaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsnLehrerStammdatenNachname({ get: () => daten.get().nachname }, kontext));
		this._validatoren.add(new ValidatorLsvLehrerStammdatenVorname({ get: () => daten.get().vorname }, kontext));
		this._validatoren.add(new ValidatorLsdLehrerStammdatenGeburtsdatum({ get: () => daten.get().geburtsdatum }, kontext));
		this._validatoren.add(new ValidatorLsgLehrerStammdatenGeschlecht({ get: () => daten.get().geschlecht }, kontext));
		this._validatoren.add(new ValidatorLskLehrerStammdatenKuerzel({ get: () => daten.get().kuerzel }, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsLehrerStammdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsLehrerStammdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsLehrerStammdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLsLehrerStammdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsLehrerStammdaten(obj: unknown): ValidatorLsLehrerStammdaten {
	return obj as ValidatorLsLehrerStammdaten;
}
