import { ValidatorSle01SchuelerLernabschnittsdatenEpJahre } from '../../../asd/validate/schueler/ValidatorSle01SchuelerLernabschnittsdatenEpJahre';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSle00SchuelerLernabschnittsdatenEpJahre extends Validator {

	/**
	 * EP - Jahre
	 */
	private readonly _idEpJahre: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "EP-Jahr des Schülers: Das Feld 'EP-Jahr' darf nicht leer sein.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idEpJahre: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEpJahre = idEpJahre;
		this._validatoren.add(new ValidatorSle01SchuelerLernabschnittsdatenEpJahre(this.getNotNullSupplierLong(idEpJahre), kontext));
	}

	protected pruefe(): boolean {
		const idEpJahre: number | null = this._idEpJahre.get();
		if (idEpJahre === null) {
			this.addFehler(0, ValidatorSle00SchuelerLernabschnittsdatenEpJahre.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSle00SchuelerLernabschnittsdatenEpJahre';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSle00SchuelerLernabschnittsdatenEpJahre', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSle00SchuelerLernabschnittsdatenEpJahre>('de.svws_nrw.asd.validate.schueler.ValidatorSle00SchuelerLernabschnittsdatenEpJahre');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSle00SchuelerLernabschnittsdatenEpJahre(obj: unknown): ValidatorSle00SchuelerLernabschnittsdatenEpJahre {
	return obj as ValidatorSle00SchuelerLernabschnittsdatenEpJahre;
}
