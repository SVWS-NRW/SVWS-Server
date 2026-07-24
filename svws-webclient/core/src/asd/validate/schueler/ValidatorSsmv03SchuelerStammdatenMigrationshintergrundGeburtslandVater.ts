import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/**
	 * Geburtsland des Vaters
	 */
	private readonly _idGeburtslandVater: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Vaters: Das Feld 'Geburtsland Vater' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandVater = idGeburtslandVater;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const idGeburtslandVater: number | null = this._idGeburtslandVater.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = (hatMigrationshintergrundZwisch !== null) && hatMigrationshintergrundZwisch;
		if (!hatMigrationshintergrund && (idGeburtslandVater !== null)) {
			this.addFehler(0, ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater>('de.svws_nrw.asd.validate.schueler.ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater(obj: unknown): ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater {
	return obj as ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater;
}
