import { Schulform } from '../../../asd/types/schule/Schulform';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private readonly _staatsangehoerigkeitSchluessel: Supplier<string>;

	private readonly schuljahr: number;

	private static readonly FEHLERTEXT: string = "Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitSchluessel              der staatsangehoerigkeitSchluessel des Lehrers
	 * @param schuljahr
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(staatsangehoerigkeitSchluessel: Supplier<string>, schuljahr: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._staatsangehoerigkeitSchluessel = staatsangehoerigkeitSchluessel;
		this.schuljahr = schuljahr.get().valueOf();
	}

	protected pruefe(): boolean {
		const schulform: Schulform | null = this.kontext().getSchulform();
		if (Nationalitaeten.data().getBySchuljahrAndSchulformAndSchluessel(this.schuljahr, schulform, this._staatsangehoerigkeitSchluessel.get()) === null) {
			this.addFehler(0, ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID;
}
