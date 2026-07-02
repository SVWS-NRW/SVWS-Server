import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { FormBilingualerUnterrichtKatalogEintrag } from '../../../asd/data/schule/FormBilingualerUnterrichtKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class FormBilingualerUnterricht extends CoreTypeSimple<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht> {


	/**
	 * Erstellt eine FormBilingualerUnterricht
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht>): void {
		CoreTypeDataManager.putManager(FormBilingualerUnterricht.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht> {
		return CoreTypeDataManager.getManager(FormBilingualerUnterricht.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<FormBilingualerUnterricht> {
		return CoreTypeSimple.valuesByClass(FormBilingualerUnterricht.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	public getInstance(): FormBilingualerUnterricht | null {
		return new FormBilingualerUnterricht();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public getLetzterEintrag(): FormBilingualerUnterrichtKatalogEintrag {
		return this.getManager().getHistorieByWert(this).getLast();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.FormBilingualerUnterricht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.FormBilingualerUnterricht', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<FormBilingualerUnterricht>('de.svws_nrw.asd.types.schule.FormBilingualerUnterricht');

}

export function cast_de_svws_nrw_asd_types_schule_FormBilingualerUnterricht(obj: unknown): FormBilingualerUnterricht {
	return obj as FormBilingualerUnterricht;
}
