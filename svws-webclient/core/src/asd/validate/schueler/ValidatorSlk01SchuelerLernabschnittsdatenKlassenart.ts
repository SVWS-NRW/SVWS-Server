import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSlk02SchuelerLernabschnittsdatenKlassenart } from '../../../asd/validate/schueler/ValidatorSlk02SchuelerLernabschnittsdatenKlassenart';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlk01SchuelerLernabschnittsdatenKlassenart extends Validator {

	/**
	 * Das Geburtsland
	 */
	private readonly _idKlassenart: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Klassenart des Schülers: Das Feld 'Klassenart' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Klassenart im Katalog.
	 *
	 * @param idKlassenart   die Klassenart ID
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._validatoren.add(new ValidatorSlk02SchuelerLernabschnittsdatenKlassenart(idKlassenart, kontext));
	}

	protected pruefe(): boolean {
		const idKlassenart: number = this._idKlassenart.get();
		const kArt: Klassenart | null = Klassenart.data().getWertByIDOrNull(idKlassenart);
		if (kArt === null) {
			this.addFehler(0, ValidatorSlk01SchuelerLernabschnittsdatenKlassenart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlk01SchuelerLernabschnittsdatenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlk01SchuelerLernabschnittsdatenKlassenart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlk01SchuelerLernabschnittsdatenKlassenart>('de.svws_nrw.asd.validate.schueler.ValidatorSlk01SchuelerLernabschnittsdatenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlk01SchuelerLernabschnittsdatenKlassenart(obj: unknown): ValidatorSlk01SchuelerLernabschnittsdatenKlassenart {
	return obj as ValidatorSlk01SchuelerLernabschnittsdatenKlassenart;
}
