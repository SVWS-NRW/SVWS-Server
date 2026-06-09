import { ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLssLehrerStammdatenStaatsangehoerigkeitID extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis      die ID des Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		const rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null> = { get: () => LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis.get()) };
		this._validatoren.add(new ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(idStaatsangehoerigkeit, rechtsverhaeltnis, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLssLehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLssLehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLssLehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLssLehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLssLehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLssLehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLssLehrerStammdatenStaatsangehoerigkeitID;
}
