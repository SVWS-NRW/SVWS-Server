import { ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination } from '../../../asd/validate/schueler/ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination';
import { NationalitaetenKatalogEintrag } from '../../../asd/data/schule/NationalitaetenKatalogEintrag';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { Verkehrssprache } from '../../../asd/types/schule/Verkehrssprache';
import { VerkehrsspracheKatalogEintrag } from '../../../asd/data/schule/VerkehrsspracheKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslandes
	 * @param idGeburtslandMutter       die ID des Geburtslandes der Mutter
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param idVerkehrsspracheFamilie  die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number | null>, idGeburtslandMutter: Supplier<number | null>, idGeburtslandVater: Supplier<number | null>, idVerkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		let geburtsland: string;
		let nationalitaetEintrag: NationalitaetenKatalogEintrag | null = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag !== null) {
			geburtsland = nationalitaetEintrag.kuerzel;
		} else {
			geburtsland = "DEU";
		}
		let geburtslandMutter: string;
		nationalitaetEintrag = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag !== null) {
			geburtslandMutter = nationalitaetEintrag.kuerzel;
		} else {
			geburtslandMutter = "DEU";
		}
		let geburtslandVater: string;
		nationalitaetEintrag = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag !== null) {
			geburtslandVater = nationalitaetEintrag.kuerzel;
		} else {
			geburtslandVater = "DEU";
		}
		let verkehrsspracheFamilie: string;
		let verkehrsspracheEintrag: VerkehrsspracheKatalogEintrag | null = Verkehrssprache.data().getEintragByID(idVerkehrsspracheFamilie.get());
		if (verkehrsspracheEintrag !== null) {
			verkehrsspracheFamilie = verkehrsspracheEintrag.kuerzel;
		} else {
			verkehrsspracheFamilie = "de";
		}
		this._validatoren.add(new ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination({ get: () => geburtsland }, { get: () => geburtslandMutter }, { get: () => geburtslandVater }, { get: () => verkehrsspracheFamilie }, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination>('de.svws_nrw.asd.validate.schueler.ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination(obj: unknown): ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination {
	return obj as ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination;
}
