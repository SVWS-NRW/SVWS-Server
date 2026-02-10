import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsd10LehrerStammdatenGeburtsdatum extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geburtsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		let geburtsdatum: DateManager | null = null;
		try {
			geburtsdatum = DateManager.from(this.daten.get());
		} catch(e : any) {
			e.printStackTrace();
		}
		const finalGeburtsdatum: DateManager | null = geburtsdatum;
		const schuljahr: number = this.kontext().getSchuljahr();
		if (finalGeburtsdatum === null || !finalGeburtsdatum.istInJahren(schuljahr - 80, schuljahr - 18)) {
			this.addFehler(1, "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte " + (schuljahr - 80) + " bis " + (schuljahr - 18) + ".");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd10LehrerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd10LehrerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsd10LehrerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLsd10LehrerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsd10LehrerStammdatenGeburtsdatum(obj: unknown): ValidatorLsd10LehrerStammdatenGeburtsdatum {
	return obj as ValidatorLsd10LehrerStammdatenGeburtsdatum;
}
