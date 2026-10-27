import { Uebergangsempfehlung } from '../../../asd/types/schueler/Uebergangsempfehlung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/**
	 * Die Übergangsempfehlung-Grundschule-ID des Schülers
	 */
	private readonly _idUebergangsempfehlungGrundschule: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public constructor(idUebergangsempfehlungGrundschule: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;
	}

	protected pruefe(): boolean {
		if (!Uebergangsempfehlung.data().isGueltig(this._idUebergangsempfehlungGrundschule.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Übergangsempfehlung des Schülers: Der eingetragene Wert für das Feld 'Übergangsempfehlung Jg. 5' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung>('de.svws_nrw.asd.validate.schueler.ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung(obj: unknown): ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung {
	return obj as ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung;
}
