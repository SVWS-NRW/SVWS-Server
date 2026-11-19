import { ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart } from '../../../asd/validate/schueler/ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart';
import { Einschulungsart } from '../../../asd/types/schueler/Einschulungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/**
	 * Die ID der Einschulungsart an der Grundschule
	 */
	private readonly _idEinschulungsartGrundschule: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public constructor(idEinschulungsartGrundschule: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEinschulungsartGrundschule = idEinschulungsartGrundschule;
		this._validatoren.add(new ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	protected pruefe(): boolean {
		const idEinschulungsartGrundschule: number | null = this._idEinschulungsartGrundschule.get();
		if ((idEinschulungsartGrundschule === null) || (idEinschulungsartGrundschule < 0)) {
			return true;
		}
		if (Einschulungsart.data().getWertByIDOrNull(idEinschulungsartGrundschule) === null) {
			this.addFehler(0, "Einschulungsart des Schülers: Das Feld 'Einschulungsart' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart>('de.svws_nrw.asd.validate.schueler.ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(obj: unknown): ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart {
	return obj as ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart;
}
