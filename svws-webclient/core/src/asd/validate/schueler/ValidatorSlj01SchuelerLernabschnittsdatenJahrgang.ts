import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JahrgaengeStatistikGesamt } from '../../../asd/data/statistik/JahrgaengeStatistikGesamt';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlj01SchuelerLernabschnittsdatenJahrgang extends Validator {

	/**
	 * Die ID des Jahrgangs des Schülers
	 */
	private readonly _jahrgangID: Supplier<number | null>;

	/**
	 * Die Liste aller Jahrgänge der Schule
	 */
	private readonly _jahrgaenge: Supplier<List<JahrgaengeStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param jahrgangID   die ID des Jahrgangs des Schülers
	 * @param jahrgaenge   die Liste aller Jahrgänge der Schule
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(jahrgangID: Supplier<number | null>, jahrgaenge: Supplier<List<JahrgaengeStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._jahrgangID = jahrgangID;
		this._jahrgaenge = jahrgaenge;
	}

	protected pruefe(): boolean {
		const jahrgangID: number | null = this._jahrgangID.get();
		const listJahrgaenge: List<JahrgaengeStatistikGesamt> | null = this._jahrgaenge.get();
		if ((jahrgangID === null) || (listJahrgaenge === null)) {
			return true;
		}
		for (const jahrgang of listJahrgaenge) {
			if (jahrgang.id === jahrgangID) {
				return true;
			}
		}
		this.addFehler(0, "Jahrgang des Schülers: Das Feld 'Jahrgang' muss zulässig sein.");
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlj01SchuelerLernabschnittsdatenJahrgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlj01SchuelerLernabschnittsdatenJahrgang', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlj01SchuelerLernabschnittsdatenJahrgang>('de.svws_nrw.asd.validate.schueler.ValidatorSlj01SchuelerLernabschnittsdatenJahrgang');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(obj: unknown): ValidatorSlj01SchuelerLernabschnittsdatenJahrgang {
	return obj as ValidatorSlj01SchuelerLernabschnittsdatenJahrgang;
}
