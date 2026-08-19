import { ValidatorSsdSchuelerStammdatenGeburtsdatum } from '../../../asd/validate/schueler/ValidatorSsdSchuelerStammdatenGeburtsdatum';
import { ValidatorSsgSchuelerStammdatenGeschlecht } from '../../../asd/validate/schueler/ValidatorSsgSchuelerStammdatenGeschlecht';
import { ValidatorSsmSchuelerStammdatenMigrationshintergrund } from '../../../asd/validate/schueler/ValidatorSsmSchuelerStammdatenMigrationshintergrund';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit';
import { ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit';

export class ValidatorSsSchuelerStammdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geschlecht                das geschlecht des Schuelers
	 * @param geburtsdatum              das geburtsdatum des Schuelers
	 * @param idGeburtsland             die ID des Geburtslandes
	 * @param idGeburtslandMutter       die ID des Geburtslandes der Mutter
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param idStaatsangehoerigkeit    Staatsangehörigkeit
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(geschlecht: Supplier<number | null>, geburtsdatum: Supplier<string | null>, idGeburtsland: Supplier<number | null>, idGeburtslandMutter: Supplier<number | null>, idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, idStaatsangehoerigkeit: Supplier<number | null>, idStaatsangehoerigkeit2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsgSchuelerStammdatenGeschlecht(geschlecht, kontext));
		this._validatoren.add(new ValidatorSsdSchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
		this._validatoren.add(new ValidatorSsmSchuelerStammdatenMigrationshintergrund(idGeburtsland, idGeburtslandMutter, idGeburtslandVater, hatMigrationshintergrund, kontext));
		this._validatoren.add(new ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit(idStaatsangehoerigkeit, kontext));
		this._validatoren.add(new ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, idStaatsangehoerigkeit, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsSchuelerStammdaten>('de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsSchuelerStammdaten(obj: unknown): ValidatorSsSchuelerStammdaten {
	return obj as ValidatorSsSchuelerStammdaten;
}
