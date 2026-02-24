import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsg01LehrerStammdatenGeschlecht extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const geschlecht: Geschlecht | null = Geschlecht.fromValue(this.daten.get());
		if (geschlecht === null) {
			this.addFehler(0, "Unzulässiger Schlüssel '" + geschlecht + "' im Feld 'Geschlecht'. Die gültigen Schlüssel entnehmen Sie bitte dem Pulldownmenü.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsg01LehrerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsg01LehrerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsg01LehrerStammdatenGeschlecht>('de.svws_nrw.asd.validate.lehrer.ValidatorLsg01LehrerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsg01LehrerStammdatenGeschlecht(obj: unknown): ValidatorLsg01LehrerStammdatenGeschlecht {
	return obj as ValidatorLsg01LehrerStammdatenGeschlecht;
}
