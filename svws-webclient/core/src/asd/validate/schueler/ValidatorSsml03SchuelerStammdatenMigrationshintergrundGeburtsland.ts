import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/**
	 * Geburtsland
	 */
	private readonly _idGeburtsland: Supplier<number>;

	/**
	 * hat Migrationshintergrund
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Schülers: Das Feld 'Geburtsland' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtsland = idGeburtsland;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const idGeburtsland: number = this._idGeburtsland.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = hatMigrationshintergrundZwisch === null ? false : hatMigrationshintergrundZwisch;
		if (!hatMigrationshintergrund) {
			if (idGeburtsland !== -1) {
				this.addFehler(0, ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland.FEHLERTEXT);
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland>('de.svws_nrw.asd.validate.schueler.ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland(obj: unknown): ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland {
	return obj as ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland;
}
