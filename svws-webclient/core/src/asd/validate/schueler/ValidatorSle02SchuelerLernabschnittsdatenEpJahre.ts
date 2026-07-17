import { PrimarstufeSchuleingangsphaseBesuchsjahre } from '../../../asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSle02SchuelerLernabschnittsdatenEpJahre extends Validator {

	/**
	 * EP - Jahre
	 */
	private readonly _idEpJahre: Supplier<number>;

	private static readonly FEHLERTEXT: string = "EP-Jahr des Schülers: Der eingetragene Wert für das Feld 'EP-Jahr' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idEpJahre: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEpJahre = idEpJahre;
	}

	protected pruefe(): boolean {
		if (!PrimarstufeSchuleingangsphaseBesuchsjahre.data().isGueltig(this._idEpJahre.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorSle02SchuelerLernabschnittsdatenEpJahre.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSle02SchuelerLernabschnittsdatenEpJahre';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSle02SchuelerLernabschnittsdatenEpJahre', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSle02SchuelerLernabschnittsdatenEpJahre>('de.svws_nrw.asd.validate.schueler.ValidatorSle02SchuelerLernabschnittsdatenEpJahre');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSle02SchuelerLernabschnittsdatenEpJahre(obj: unknown): ValidatorSle02SchuelerLernabschnittsdatenEpJahre {
	return obj as ValidatorSle02SchuelerLernabschnittsdatenEpJahre;
}
