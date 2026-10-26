import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie } from '../../../asd/validate/schueler/ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';

export class ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/**
	 * Das Schuljahr des Schülers
	 */
	readonly _schuljahr: Supplier<number>;

	/**
	 * Die Verkehrssprache der Familie des Schülers
	 */
	private readonly _idVerkehrsspracheFamilie: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Verkehrssprache: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Verkehrssprache' besetzt sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                 Schuljahr
	 * @param verkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, verkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idVerkehrsspracheFamilie = verkehrsspracheFamilie;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const verkehrsspracheFamilie: number | null = this._idVerkehrsspracheFamilie.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = hatMigrationshintergrundZwisch === null ? false : hatMigrationshintergrundZwisch;
		this._validatoren.clear();
		if (hatMigrationshintergrund) {
			if (verkehrsspracheFamilie === null) {
				this.addFehler(0, ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie.FEHLERTEXT);
				return false;
			}
			this._validatoren.add(new ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(this._schuljahr, this.getNotNullSupplierLong(this._idVerkehrsspracheFamilie), { get: () => hatMigrationshintergrund }, this.kontext()));
		} else {
			// empty block
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie>('de.svws_nrw.asd.validate.schueler.ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(obj: unknown): ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {
	return obj as ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie;
}
