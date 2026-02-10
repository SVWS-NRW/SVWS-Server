import { JavaObject } from '../../../../../java/lang/JavaObject';
import { BKGymAbiturMarkierungsalgorithmusErgebnis } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusErgebnis';
import { BKGymAbiturdatenManager } from '../../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { HashMap } from '../../../../../java/util/HashMap';
import { ArrayList } from '../../../../../java/util/ArrayList';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsVarianten extends JavaObject {

	/**
	 * Der Manager für die Fächer des beruflichen Gymnasiums
	 */
	public readonly abiturdatenManager: BKGymAbiturdatenManager;

	/**
	 * Die verschiedenen Markierungsergebnisse, aus denen das beste Ergebnis gewählt wird.
	 */
	private readonly ergebnisse: List<BKGymAbiturMarkierungsVariante> = new ArrayList<BKGymAbiturMarkierungsVariante>();

	/**
	 * Eine Map, welche von der Nummer des Abiturfaches auf die FachID verweist.
	 */
	private readonly mapAbiturfachbelegungen: HashMap<number, number> = new HashMap<number, number>();


	/**
	 * Konstruktor der die Root-Variante der Markierungen aus den Abiturdaten erzeugt.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager: BKGymAbiturdatenManager) {
		super();
		this.abiturdatenManager = manager;
		this.init();
	}

	/**
	 * Erzeugt die root-Variante und trägt sie als einzige in die Liste der Varianten ein.
	 * Über Markierungsregeln können weitere Varianten später hinzugefügt werden.
	 */
	private init(): void {
		const root: BKGymAbiturMarkierungsVariante = new BKGymAbiturMarkierungsVariante(this);
		this.ergebnisse.add(root);
		this.reportDoppelteFaecher(root);
		this.reportFehlerFacharbeit(root);
	}

	/**
	 * gibt die im Fächermanager ermittelten doppelten Fächer ins Log aus.
	 *
	 * @param root   die Markierungsvariante
	 */
	private reportDoppelteFaecher(root: BKGymAbiturMarkierungsVariante): void {
		for (const fach of this.abiturdatenManager.getFaecherManager().getDoppelteFaecher())
			root.addLogEintrag(0, "Hinweis: Das Fach " + fach + " ist im Fächerkatalog nicht eindeutig bestimmbar. Bitte die Bezeichnungen der Fächer eindeutig festlegen.");
	}

	/**
	 * gibt den Hinweis aus, wenn das der Facharbeit zugeordnete Fach kein Leistungskurs ist
	 *
	 * @param root   die Markierungsvariante
	 */
	private reportFehlerFacharbeit(root: BKGymAbiturMarkierungsVariante): void {
		if (!this.abiturdatenManager.getFachbelegungManager().getIstFacharbeitLK())
			root.addLogEintrag(0, "Hinweis: Die Facharbeit ist nicht einem der beiden Leistungkursfächer zugeordnet.");
	}

	/**
	 * Getter auf die Ergebnisse
	 *
	 * @return die Liste mit den Markierungsvarianten
	 */
	public getErgebnisse(): List<BKGymAbiturMarkierungsVariante> {
		return this.ergebnisse;
	}

	/**
	 * Fügt eine neue Markierungsvariante hinzu
	 *
	 * @param variante   die Variante, die hinzugefügt wird
	 */
	public addVariante(variante: BKGymAbiturMarkierungsVariante): void {
		this.ergebnisse.add(variante);
	}

	/**
	 * Liefert das beste Ergebnis als DTO zurück
	 *
	 * @return das Ergebnis
	 */
	public getBestesErgebnis(): BKGymAbiturMarkierungsalgorithmusErgebnis | null {
		if (this.ergebnisse.isEmpty())
			return null;
		this.ergebnisse.sort(BKGymAbiturMarkierungsVariante.comparator);
		return this.ergebnisse.getFirst().getErgebnis();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVarianten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVarianten'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsVarianten>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsVarianten');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsVarianten(obj: unknown): BKGymAbiturMarkierungsVarianten {
	return obj as BKGymAbiturMarkierungsVarianten;
}
