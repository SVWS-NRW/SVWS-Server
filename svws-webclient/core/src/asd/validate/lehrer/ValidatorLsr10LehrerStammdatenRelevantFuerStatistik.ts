import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsr10LehrerStammdatenRelevantFuerStatistik extends Validator {

	/**
	 * Das Abgangsdatum
	 */
	private readonly _abgangsdatum: Supplier<string | null>;

	/**
	 * Relevant für Statistik
	 */
	private readonly _istRelevantFuerStatistik: Supplier<boolean | null>;

	private _kontextNeu: ValidatorKontext;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param istRelevantFuerStatistik     ist relevant für Statistik
	 * @param abgangsdatum                 das Abgangsdatum des Lehrers
	 * @param kontext                      der Kontext des Validators
	 */
	public constructor(istRelevantFuerStatistik: Supplier<boolean | null>, abgangsdatum: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._istRelevantFuerStatistik = istRelevantFuerStatistik;
		this._abgangsdatum = abgangsdatum;
		this._kontextNeu = kontext;
	}

	protected pruefe(): boolean {
		if (this._istRelevantFuerStatistik.get() === null) {
			return true;
		}
		const istRelevantFuerStatistik: boolean | null = this._istRelevantFuerStatistik.get();
		let abgangsdatum: DateManager | null = null;
		let errorMsg: string = "";
		try {
			abgangsdatum = DateManager.from(this._abgangsdatum.get());
		} catch (e: any) {
			errorMsg = e.getMessage();
		}
		if (abgangsdatum === null) {
			return true;
		}
		if (istRelevantFuerStatistik) {
			if ((abgangsdatum.getJahr() < this._kontextNeu.getSchuljahr()) || (((abgangsdatum.getJahr() === this._kontextNeu.getSchuljahr())) && ((abgangsdatum.getMonat() < 8)))) {
				this.addFehler(1, "Statistikrelevanz des Lehrers: Das Feld 'Ist relevant für Statistik' darf nicht ausgewählt sein, wenn die Lehrkraft im vorherigen Schuljahr abgegangen ist." + errorMsg);
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsr10LehrerStammdatenRelevantFuerStatistik';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsr10LehrerStammdatenRelevantFuerStatistik', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsr10LehrerStammdatenRelevantFuerStatistik>('de.svws_nrw.asd.validate.lehrer.ValidatorLsr10LehrerStammdatenRelevantFuerStatistik');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsr10LehrerStammdatenRelevantFuerStatistik(obj: unknown): ValidatorLsr10LehrerStammdatenRelevantFuerStatistik {
	return obj as ValidatorLsr10LehrerStammdatenRelevantFuerStatistik;
}
