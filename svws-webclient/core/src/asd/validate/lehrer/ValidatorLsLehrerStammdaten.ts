import { ValidatorLsgLehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsgLehrerStammdatenGeschlecht';
import { ValidatorLskLehrerStammdatenKuerzel } from '../../../asd/validate/lehrer/ValidatorLskLehrerStammdatenKuerzel';
import { ValidatorLsdLehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsdLehrerStammdatenGeburtsdatum';
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
	 * @param nachname        die Daten des Validators
	 * @param vorname         die Daten des Validators
	 * @param geburtsdatum    die Daten des Validators
	 * @param geschlecht      die Daten des Validators
	 * @param kuerzel         die Daten des Validators
	 * @param kontext         der Kontext des Validators
	 */
	public constructor(nachname: Supplier<string | null>, vorname: Supplier<string | null>, geburtsdatum: Supplier<string | null>, geschlecht: Supplier<number | null>, kuerzel: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsnLehrerStammdatenNachname(nachname, kontext));
		this._validatoren.add(new ValidatorLsvLehrerStammdatenVorname(vorname, kontext));
		this._validatoren.add(new ValidatorLsdLehrerStammdatenGeburtsdatum(geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLsgLehrerStammdatenGeschlecht(geschlecht, kontext));
		this._validatoren.add(new ValidatorLskLehrerStammdatenKuerzel(kuerzel, kontext));
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
