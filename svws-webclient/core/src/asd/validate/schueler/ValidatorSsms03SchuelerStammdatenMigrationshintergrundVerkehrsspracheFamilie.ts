import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/**
	 * Die ID der Verkehrssprache der Familie des Schülers
	 */
	private readonly _idVerkehrsspracheFamilie: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Verkehrssprache: Das Feld 'Verkehrssprache' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idVerkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const idVerkehrsspracheFamilie: number | null = this._idVerkehrsspracheFamilie.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = (hatMigrationshintergrundZwisch !== null) && hatMigrationshintergrundZwisch;
		if (!hatMigrationshintergrund && (idVerkehrsspracheFamilie !== null)) {
			this.addFehler(0, ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie>('de.svws_nrw.asd.validate.schueler.ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(obj: unknown): ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {
	return obj as ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie;
}
