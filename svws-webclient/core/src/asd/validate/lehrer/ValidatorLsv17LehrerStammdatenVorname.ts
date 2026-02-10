import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsv17LehrerStammdatenVorname extends Validator {

	/**
	 * Der Vorname des Lehrers
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const nLower: string | null = this.daten.get().toLowerCase();
		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			this.addFehler(8, "Vorname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Vornamen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv17LehrerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsv17LehrerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsv17LehrerStammdatenVorname>('de.svws_nrw.asd.validate.lehrer.ValidatorLsv17LehrerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsv17LehrerStammdatenVorname(obj: unknown): ValidatorLsv17LehrerStammdatenVorname {
	return obj as ValidatorLsv17LehrerStammdatenVorname;
}
