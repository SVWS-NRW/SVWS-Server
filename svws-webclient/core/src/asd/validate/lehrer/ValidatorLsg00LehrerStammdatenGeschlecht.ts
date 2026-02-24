import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorLsg01LehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsg01LehrerStammdatenGeschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsg00LehrerStammdatenGeschlecht extends Validator {

	/**
	 * Das Geschlecht des Lehrers
	 */
	private readonly daten: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsg01LehrerStammdatenGeschlecht(this.getNotNullSupplierInteger(daten), kontext));
	}

	protected pruefe(): boolean {
		const geschlecht: number | null = this.daten.get();
		if (geschlecht === null) {
			this.addFehler(0, "Das Feld 'Geschlecht' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsg00LehrerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsg00LehrerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsg00LehrerStammdatenGeschlecht>('de.svws_nrw.asd.validate.lehrer.ValidatorLsg00LehrerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsg00LehrerStammdatenGeschlecht(obj: unknown): ValidatorLsg00LehrerStammdatenGeschlecht {
	return obj as ValidatorLsg00LehrerStammdatenGeschlecht;
}
