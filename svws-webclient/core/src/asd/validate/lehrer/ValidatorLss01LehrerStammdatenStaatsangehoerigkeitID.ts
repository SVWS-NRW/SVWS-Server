import { ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten                 die StaatsangehoerigkeitID des Lehrers
	 * @param idRechtsverhaeltnis   das Rechtsverhältnis des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, idRechtsverhaeltnis: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(daten, kontext));
		this._validatoren.add(new ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(daten, idRechtsverhaeltnis, kontext));
	}

	protected pruefe(): boolean {
		const staatsangehoerigkeitID: Nationalitaeten | null = Nationalitaeten.getByDESTATIS(this.daten.get());
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
