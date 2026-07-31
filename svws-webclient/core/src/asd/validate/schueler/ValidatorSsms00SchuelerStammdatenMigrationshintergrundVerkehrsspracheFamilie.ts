import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie } from '../../../asd/validate/schueler/ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';

export class ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

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
	 * @param verkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(verkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idVerkehrsspracheFamilie = verkehrsspracheFamilie;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
		this._validatoren.add(new ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(verkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const verkehrsspracheFamilie: number | null = this._idVerkehrsspracheFamilie.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = (hatMigrationshintergrundZwisch !== null) && hatMigrationshintergrundZwisch;
		if (hatMigrationshintergrund && (verkehrsspracheFamilie === null)) {
			this.addFehler(0, ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie.FEHLERTEXT);
			return false;
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
