import { ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart } from '../../../asd/validate/schueler/ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart';
import { SchuelerSchulbesuchsdaten } from '../../../asd/data/schueler/SchuelerSchulbesuchsdaten';
import { Einschulungsart } from '../../../asd/types/schueler/Einschulungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private readonly _schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten   ein Supplier für die Schulbesuchsdaten des Schülers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._schulbesuchsdaten = schulbesuchsdaten;
		this._validatoren.add(new ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(schulbesuchsdaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: SchuelerSchulbesuchsdaten | null = this._schulbesuchsdaten.get();
		if ((daten === null) || (daten.idEinschulungsartGrundschule === null) || (daten.idEinschulungsartGrundschule < 0)) {
			return true;
		}
		if (Einschulungsart.data().getWertByIDOrNull(daten.idEinschulungsartGrundschule) === null) {
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
