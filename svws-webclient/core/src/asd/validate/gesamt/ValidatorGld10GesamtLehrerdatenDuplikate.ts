import type { JavaSet } from '../../../java/util/JavaSet';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { HashSet } from '../../../java/util/HashSet';

export class ValidatorGld10GesamtLehrerdatenDuplikate extends Validator {

	private readonly listLehrer: Supplier<List<LehrerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste aller Lehrerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const list: List<LehrerStatistikGesamt> = this.listLehrer.get();
		if (list.isEmpty())
			return success;
		const ids: JavaSet<number> = new HashSet<number>();
		for (const lehrer of list) {
			const istNeu: boolean = ids.add(lehrer.id);
			if (!istNeu) {
				this.addFehler(0, "Lehrkäfte: Die ID " + lehrer.id + " kommt in der Liste mehrfach vor.");
				success = false;
			}
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGld10GesamtLehrerdatenDuplikate';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGld10GesamtLehrerdatenDuplikate', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGld10GesamtLehrerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGld10GesamtLehrerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGld10GesamtLehrerdatenDuplikate(obj: unknown): ValidatorGld10GesamtLehrerdatenDuplikate {
	return obj as ValidatorGld10GesamtLehrerdatenDuplikate;
}
