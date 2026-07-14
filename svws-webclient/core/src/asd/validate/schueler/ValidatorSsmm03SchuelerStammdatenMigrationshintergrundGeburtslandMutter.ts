import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/**
	 * Geburtsland der Mutter
	 */
	private readonly _idGeburtslandMutter: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland der Mutter: Das Feld 'Geburtsland Mutter' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandMutter: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandMutter = idGeburtslandMutter;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const idGeburtslandMutter: number | null = this._idGeburtslandMutter.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = (hatMigrationshintergrundZwisch !== null) && hatMigrationshintergrundZwisch;
		if (!hatMigrationshintergrund && (idGeburtslandMutter !== null)) {
			this.addFehler(0, ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter>('de.svws_nrw.asd.validate.schueler.ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter(obj: unknown): ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter {
	return obj as ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter;
}
