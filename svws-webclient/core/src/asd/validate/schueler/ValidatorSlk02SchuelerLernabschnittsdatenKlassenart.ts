import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlk02SchuelerLernabschnittsdatenKlassenart extends Validator {

	/**
	 * Klassenart
	 */
	private readonly _idKlassenart: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Schüler Klassenart: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Klassenart im Katalog.
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
	}

	protected pruefe(): boolean {
		if (!Klassenart.data().isGueltig(this._idKlassenart.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorSlk02SchuelerLernabschnittsdatenKlassenart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlk02SchuelerLernabschnittsdatenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlk02SchuelerLernabschnittsdatenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlk02SchuelerLernabschnittsdatenKlassenart>('de.svws_nrw.asd.validate.schueler.ValidatorSlk02SchuelerLernabschnittsdatenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlk02SchuelerLernabschnittsdatenKlassenart(obj: unknown): ValidatorSlk02SchuelerLernabschnittsdatenKlassenart {
	return obj as ValidatorSlk02SchuelerLernabschnittsdatenKlassenart;
}
