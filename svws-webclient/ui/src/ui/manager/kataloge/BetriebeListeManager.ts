import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { Betrieb } from "@core/core/data/schule/Betrieb";
import type { BetriebeAnsprechpartner } from "@core/core/data/schule/BetriebeAnsprechpartner";
import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class BetriebeListeManager extends AuswahlManager<number, Betrieb, Betrieb> {

	private static readonly _betriebToId = (a: Betrieb) => a.id;
	private readonly _idsOfReferencedBetriebe: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private readonly _betriebsartenById: Map<number, Betriebsart> = new Map();
	private readonly _orteById: Map<number, OrtKatalogEintrag> = new Map();

	/**
	 * Ein Default-Comparator für den Vergleich von Betrieben.
	 */
	public static readonly comparator: Comparator<Betrieb> = {
		compare: (a: Betrieb, b: Betrieb) => {
			let cmp;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			if ((a.name !== null) && (b.name !== null)) {
				cmp = JavaString.compareTo(a.name, b.name);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		},
	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param betriebe							die Liste der Betriebe
	 * @param betriebsarten						die Liste der Betriebsarten
	 * @param orte								die Liste der Orte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, betriebe: List<Betrieb>, betriebsarten: List<Betriebsart>, orte: List<OrtKatalogEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, betriebe,
			BetriebeListeManager.comparator, BetriebeListeManager._betriebToId, BetriebeListeManager._betriebToId, []);
		this.mapBetriebsarten(betriebsarten);
		this.mapOrte(orte);
	}

	private mapBetriebsarten(betriebsarten: List<Betriebsart>) {
		for (const betriebsart of betriebsarten) {
			this._betriebsartenById.set(betriebsart.id, betriebsart);
		}
	}

	private mapOrte(orte: List<OrtKatalogEintrag>) {
		for (const ort of orte) {
			this._orteById.set(ort.id, ort);
		}
	}

	/**
	 * Vergleicht zwei Betriebe Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: Betrieb, b: Betrieb): number {
		return BetriebeListeManager.comparator.compare(a, b);
	}


	get idsOfReferencedBetriebe(): HashSet<number> {
		return this._idsOfReferencedBetriebe;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedBetriebe.clear();
		for (const b of this.liste.auswahl()) {
			if (b.referenziertInAnderenTabellen) {
				this._idsOfReferencedBetriebe.add(b.id);
			}
		}
	}

	protected checkFilter(eintrag: Betrieb): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Betrieb) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return ((eintrag.name !== null) && eintrag.name.toLocaleLowerCase().includes(searchTermLower));
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	// --- ansprechpartner ---

	/** Fügt einen Ansprechpartner der Liste hinzu -> Wird zur Anzeige neu angelegter Einträge in der Tabelle benötigt */
	public addAnsprechpartner(m: BetriebeAnsprechpartner) {
		this.daten().ansprechpartner.add(m);
	}

	/** Eintrag der Ansprechpartner löschen */
	public deleteAnsprechpartner(id: number) {
		const index = this.getIndexAnsprechpartnerById(id);
		if (index !== undefined) {
			this.daten().ansprechpartner.removeElementAt(index);
		}
	}

	/** Eintrag der Ansprechpartner patchen */
	public patchAnsprechpartner(data: Partial<BetriebeAnsprechpartner>) {
		if (data.id === undefined) {
			return;
		}
		const index = this.getIndexAnsprechpartnerById(data.id);
		if (index === undefined) {
			return;
		}
		const ansprechpartner = this.daten().ansprechpartner.get(index);
		Object.assign(ansprechpartner, data);
	}

	private getIndexAnsprechpartnerById(id: number): number | undefined {
		let index = 0;
		for (const s of this.daten().ansprechpartner) {
			if (s.id === id) {
				return index;
			}
			index++;
		}
	}

	get betriebsartenById(): Map<number, Betriebsart> {
		return this._betriebsartenById;
	}

	get orteById(): Map<number, OrtKatalogEintrag> {
		return this._orteById;
	}
}
