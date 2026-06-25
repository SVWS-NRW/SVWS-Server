import { ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/**
	 * Das Geburtsland
	 */
	private readonly _idGeburtsland: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Schülers: Das Feld muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtsland = idGeburtsland;
		this._validatoren.add(new ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland(idGeburtsland, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtsland: number = this._idGeburtsland.get();
		const nat: Nationalitaeten | null = Nationalitaeten.data().getWertByIDOrNull(idGeburtsland);
		if (nat === null) {
			this.addFehler(0, ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland>('de.svws_nrw.asd.validate.schueler.ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland(obj: unknown): ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland {
	return obj as ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland;
}
