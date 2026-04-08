import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsd01SchuelerStammdatenGeburtsdatum extends Validator {

	/**
	 * Das Geburtsdatumm des Schülers
	 */
	private readonly fieldGeburtsdatum: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsdatumm des Schülers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(geburtsdatum: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.fieldGeburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		let geburtsdatum: DateManager | null = null;
		let errorMsg: string = "";
		try {
			geburtsdatum = DateManager.from(this.fieldGeburtsdatum.get());
		} catch(e : any) {
			errorMsg = e.getMessage();
		}
		const finalGeburtsdatum: DateManager | null = geburtsdatum;
		if (finalGeburtsdatum === null) {
			this.addFehler(0, "Das Geburtsdatum ist ungültig: " + errorMsg);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsd01SchuelerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsd01SchuelerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsd01SchuelerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.schueler.ValidatorSsd01SchuelerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsd01SchuelerStammdatenGeburtsdatum(obj: unknown): ValidatorSsd01SchuelerStammdatenGeburtsdatum {
	return obj as ValidatorSsd01SchuelerStammdatenGeburtsdatum;
}
