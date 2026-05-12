import { ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly _staatsangehoerigkeitID: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitID   die StaatsangehoerigkeitID des Lehrers
	 * @param rechtsverhaeltnis        das Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(staatsangehoerigkeitID: Supplier<string>, rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._staatsangehoerigkeitID = staatsangehoerigkeitID;
		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitID, kontext));
		this._validatoren.add(new ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitID, rechtsverhaeltnis, kontext));
	}

	protected pruefe(): boolean {
		const staatsangehoerigkeitID: Nationalitaeten | null = Nationalitaeten.getByDESTATIS(this._staatsangehoerigkeitID.get());
		if (staatsangehoerigkeitID === null) {
			this.addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID;
}
