import { ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung } from '../../../asd/validate/schueler/ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idSchulgliederung        die ID der Schulgliederung
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idSchulgliederung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(schuljahr, idSchulgliederung, kontext));
	}

	protected pruefe(): boolean {
		const sja: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.getNotNullSupplierLong(this._idSchuljahresabschnitt).get());
		if (sja === null) {
			this.addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung>('de.svws_nrw.asd.validate.schueler.ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung(obj: unknown): ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung {
	return obj as ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung;
}
