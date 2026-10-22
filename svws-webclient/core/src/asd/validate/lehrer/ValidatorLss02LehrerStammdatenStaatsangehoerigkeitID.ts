import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	private readonly _idStaatsangehoerigkeit: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   ID der Staatsangehörigkeit des Lehrers
	 * @param schuljahr                das zu prüfende Schuljahr des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number>, schuljahr: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
	}

	protected pruefe(): boolean {
		if (!Nationalitaeten.data().isGueltig(this._idStaatsangehoerigkeit.get(), this._schuljahr.get())) {
			this.addFehler(0, ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss02LehrerStammdatenStaatsangehoerigkeitID;
}
