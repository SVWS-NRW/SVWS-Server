import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Der Lehrer-Nachname
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten                 die StaatsangehörigkeitID des Lehrers
	 * @param idRechtsverhaeltnis   das Rechtsverhältnis des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, idRechtsverhaeltnis: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(this.getNotNullSupplier(daten), this.getNotNullSupplierLong(idRechtsverhaeltnis), kontext));
	}

	protected pruefe(): boolean {
		const staatsangehoerigkeitID: string | null = this.daten.get();
		if (staatsangehoerigkeitID === null || JavaString.isEmpty(staatsangehoerigkeitID)) {
			this.addFehler(0, "Das Feld 'Staatsangehörigkeit' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID;
}
