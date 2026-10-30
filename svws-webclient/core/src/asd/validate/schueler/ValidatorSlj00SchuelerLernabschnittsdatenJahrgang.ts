import { ValidatorSlj01SchuelerLernabschnittsdatenJahrgang } from '../../../asd/validate/schueler/ValidatorSlj01SchuelerLernabschnittsdatenJahrgang';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JahrgaengeStatistikGesamt } from '../../../asd/data/statistik/JahrgaengeStatistikGesamt';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlj00SchuelerLernabschnittsdatenJahrgang extends Validator {

	/**
	 * Die ID des Jahrgangs des Schülers
	 */
	private readonly _jahrgangID: Supplier<number | null>;


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
		this._validatoren.add(new ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(jahrgangID, jahrgaenge, kontext));
	}

	protected pruefe(): boolean {
		const jahrgangID: number | null = this._jahrgangID.get();
		if (jahrgangID === null) {
			this.addFehler(0, "Jahrgang des Schülers: Das Feld 'Jahrgang' darf nicht leer sein");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlj00SchuelerLernabschnittsdatenJahrgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlj00SchuelerLernabschnittsdatenJahrgang', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlj00SchuelerLernabschnittsdatenJahrgang>('de.svws_nrw.asd.validate.schueler.ValidatorSlj00SchuelerLernabschnittsdatenJahrgang');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlj00SchuelerLernabschnittsdatenJahrgang(obj: unknown): ValidatorSlj00SchuelerLernabschnittsdatenJahrgang {
	return obj as ValidatorSlj00SchuelerLernabschnittsdatenJahrgang;
}
