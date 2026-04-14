import { SchuelerStatistikGesamt } from '../../../asd/data/statistik/SchuelerStatistikGesamt';
import type { JavaSet } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { HashSet } from '../../../java/util/HashSet';

export class ValidatorGsd10GesamtSchuelerdatenDuplikate extends Validator {

	private readonly listSchueler: Supplier<List<SchuelerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listSchueler        die Liste aller Schuelerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listSchueler: Supplier<List<SchuelerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this.listSchueler = listSchueler;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const schuelerListe: List<SchuelerStatistikGesamt> = this.listSchueler.get();
		if (schuelerListe.isEmpty()) {
			return true;
		}
		const ids: JavaSet<number> = new HashSet<number>();
		for (const schueler of schuelerListe) {
			const istNeu: boolean = ids.add(schueler.id);
			if (!istNeu) {
				this.addFehler(0, "Schüler: Die ID " + schueler.id + " kommt mehrfach vor.");
				success = false;
			}
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGsd10GesamtSchuelerdatenDuplikate';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGsd10GesamtSchuelerdatenDuplikate', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGsd10GesamtSchuelerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGsd10GesamtSchuelerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGsd10GesamtSchuelerdatenDuplikate(obj: unknown): ValidatorGsd10GesamtSchuelerdatenDuplikate {
	return obj as ValidatorGsd10GesamtSchuelerdatenDuplikate;
}
