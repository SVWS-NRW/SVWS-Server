import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/**
	 * Das Geburtsland
	 */
	private readonly _idGeburtsland: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Schülers: Der eingetragene Wert für das Feld 'Geburtsland' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idGeburtsland     die ID des Geburtslands
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtsland = idGeburtsland;
	}

	protected pruefe(): boolean {
		if (!Nationalitaeten.data().isGueltig(this._idGeburtsland.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland>('de.svws_nrw.asd.validate.schueler.ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland(obj: unknown): ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland {
	return obj as ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland;
}
