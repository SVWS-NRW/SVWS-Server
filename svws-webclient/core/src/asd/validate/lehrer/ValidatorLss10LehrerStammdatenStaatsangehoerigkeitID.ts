import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string>;

	private static readonly FEHLERTEXT: string = "Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten              die StaatsangehoerigkeitID des Lehrers
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const schuljahr: number = this.kontext().getSchuljahr();
		const staatsangehoerigkeitID: Nationalitaeten | null = Nationalitaeten.getByDESTATIS(this.daten.get());
		if (staatsangehoerigkeitID === null) {
			return true;
		}
		for (const historie of staatsangehoerigkeitID.historie()) {
			const gueltigVon: number = (historie.gueltigVon === null) ? 0 : historie.gueltigVon;
			const gueltigBis: number = (historie.gueltigBis === null) ? 99999 : historie.gueltigBis;
			if (gueltigVon <= schuljahr && gueltigBis >= schuljahr) {
				return true;
			}
		}
		this.addFehler(0, ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID.FEHLERTEXT);
		return false;
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
