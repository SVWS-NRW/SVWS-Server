import { ValidatorSlk00SchuelerLernabschnittsdatenKlassenart } from '../../../asd/validate/schueler/ValidatorSlk00SchuelerLernabschnittsdatenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlkSchuelerLernabschnittsdatenKlassenart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlk00SchuelerLernabschnittsdatenKlassenart(idKlassenart, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlkSchuelerLernabschnittsdatenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlkSchuelerLernabschnittsdatenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlkSchuelerLernabschnittsdatenKlassenart>('de.svws_nrw.asd.validate.schueler.ValidatorSlkSchuelerLernabschnittsdatenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlkSchuelerLernabschnittsdatenKlassenart(obj: unknown): ValidatorSlkSchuelerLernabschnittsdatenKlassenart {
	return obj as ValidatorSlkSchuelerLernabschnittsdatenKlassenart;
}
