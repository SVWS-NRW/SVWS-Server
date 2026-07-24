import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater } from '../../../asd/validate/schueler/ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/**
	 * Geburtsland des Vaters
	 */
	private readonly _idGeburtslandVater: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Vaters: Das Feld muss zulässig sein.";


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
		this._validatoren.add(new ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtslandVater: number | null = this._idGeburtslandVater.get();
		if (idGeburtslandVater === null) {
			return true;
		}
		if (Nationalitaeten.data().getWertByIDOrNull(idGeburtslandVater) === null) {
			this.addFehler(0, ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater>('de.svws_nrw.asd.validate.schueler.ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater(obj: unknown): ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater {
	return obj as ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater;
}
