import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private readonly _idStaatsangehoerigkeit: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   ID der Staatsangehörigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
	}

	protected pruefe(): boolean {
		if (!Nationalitaeten.data().isGueltig(this._idStaatsangehoerigkeit.get(), this.kontext().getSchuljahr())) {
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
