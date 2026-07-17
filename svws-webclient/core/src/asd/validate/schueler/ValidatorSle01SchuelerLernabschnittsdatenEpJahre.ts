import { PrimarstufeSchuleingangsphaseBesuchsjahre } from '../../../asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre';
import { ValidatorSle02SchuelerLernabschnittsdatenEpJahre } from '../../../asd/validate/schueler/ValidatorSle02SchuelerLernabschnittsdatenEpJahre';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSle01SchuelerLernabschnittsdatenEpJahre extends Validator {

	/**
	 * EP - Jahre
	 */
	private readonly _idEpJahre: Supplier<number>;

	private static readonly FEHLERTEXT: string = "EP-Jahr des Schülers: Das Feld 'EP-Jahr' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idEpJahre: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEpJahre = idEpJahre;
		this._validatoren.add(new ValidatorSle02SchuelerLernabschnittsdatenEpJahre(idEpJahre, kontext));
	}

	protected pruefe(): boolean {
		const idEpJahre: number = this._idEpJahre.get();
		const epJahre: PrimarstufeSchuleingangsphaseBesuchsjahre | null = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(idEpJahre);
		if (epJahre === null) {
			this.addFehler(0, ValidatorSle01SchuelerLernabschnittsdatenEpJahre.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSle01SchuelerLernabschnittsdatenEpJahre';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSle01SchuelerLernabschnittsdatenEpJahre', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSle01SchuelerLernabschnittsdatenEpJahre>('de.svws_nrw.asd.validate.schueler.ValidatorSle01SchuelerLernabschnittsdatenEpJahre');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSle01SchuelerLernabschnittsdatenEpJahre(obj: unknown): ValidatorSle01SchuelerLernabschnittsdatenEpJahre {
	return obj as ValidatorSle01SchuelerLernabschnittsdatenEpJahre;
}
