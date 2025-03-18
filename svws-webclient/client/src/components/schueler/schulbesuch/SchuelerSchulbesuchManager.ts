import { Einschulungsart, Herkunftsarten, Jahrgaenge, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Schulgliederung, Uebergangsempfehlung,
	SchuelerSchulbesuchsdaten, SchuelerListeEintrag } from "@core";
import type { KatalogEntlassgrund, Merkmal, SchulEintrag } from "@core";
import type { Schuljahresabschnitt, List } from "@core";
import { shallowRef } from "vue";
import type { ShallowRef } from "vue";


interface ManagerStateDataSchuelerSchulbesuch {
	auswahl: SchuelerListeEintrag;
	daten: SchuelerSchulbesuchsdaten;
}

const defaultState = <ManagerStateDataSchuelerSchulbesuch> {
	auswahl: new SchuelerListeEintrag(),
	daten: new SchuelerSchulbesuchsdaten(),
};

/** Ein Manager für die Verwaltung von Schulbesuchsdaten */
export class SchuelerSchulbesuchManager {

	/** Der Default-State, welcher über den Konstruktor gesetzt wird */
	protected _defaultState : ManagerStateDataSchuelerSchulbesuch;

	/** Der aktuelle State */
	protected _state : ShallowRef<ManagerStateDataSchuelerSchulbesuch>;

	/** Eine Map der Schuljahresabschnitte gemappt nach idSchuljahresAbschnitt */
	protected _schuljahresabschnitte: Map<number, Schuljahresabschnitt> = new Map();

	/** Eine Map der Schulen gemappt nach Schulnummer */
	protected _schulen: Map<string, SchulEintrag> = new Map();

	/** Eine Map der Merkmale gemappt nach id */
	protected _merkmale: Map<number, Merkmal> = new Map();

	/** Eine Map der Entlassgruende gemappt nach id */
	protected _entlassgruende: Map<number, KatalogEntlassgrund> = new Map();

	/** Das Schuljahr vom Schuljahresabschnitts des aktuell ausgewählten Schülers */
	protected _schuljahr : number;

	/** Die Patch-Methode der SchuelerSchulbesuchsdaten */
	protected _patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>


	/**
	 * Erstellt einen neue SchuelerSchulbesuchManager für die übergebenen SchuelerSchulbesuchsdaten
	 *
	 * @param daten					   die SchuelerSchulbesuchsdaten
	 * @param auswahl				   der aktuell, ausgewählte Schüler
	 * @param schuljahresabschnitte    eine Liste der Schuljahresabschnitte
	 * @param schulen				   eine Liste der Schulen
	 * @param merkmale				   eine Liste der Merkmale
	 * @param entlassgruende		   eine Liste der Entlassgruende
	 * @param patch					   die PatchMethode der SchuelerSchulbesuchsdaten
	 */
	public constructor(daten: SchuelerSchulbesuchsdaten, auswahl : SchuelerListeEintrag, schuljahresabschnitte : List<Schuljahresabschnitt>,
		schulen : List<SchulEintrag>, merkmale : List<Merkmal>, entlassgruende : List<KatalogEntlassgrund>,
		patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>) {
		this._defaultState = defaultState;
		this._state = shallowRef<ManagerStateDataSchuelerSchulbesuch>(this._defaultState);
		this._state.value.daten = daten;
		this._state.value.auswahl = auswahl;
		this._patch = patch;
		this.mapSchuljahresabschnitte(schuljahresabschnitte);
		this.mapSchulen(schulen);
		this.mapMerkmale(merkmale);
		this.mapEntlassgruende(entlassgruende);
		this._schuljahr = this.calcSchuljahr();
	}

	private mapSchuljahresabschnitte(schuljahresabschnitte: List<Schuljahresabschnitt>) {
		for (const abschnitt of schuljahresabschnitte)
			this._schuljahresabschnitte.set(abschnitt.id, abschnitt);
	}

	private mapSchulen(schulen : List<SchulEintrag>) {
		for (const schule of schulen)
			this._schulen.set(schule.schulnummer, schule);
	}

	private mapMerkmale(merkmale : List<Merkmal>) {
		for (const merkmal of merkmale)
			this._merkmale.set(merkmal.id, merkmal);
	}

	private mapEntlassgruende(entlassgruende : List<KatalogEntlassgrund>) {
		for (const entlassgrund of entlassgruende)
			this._entlassgruende.set(entlassgrund.id, entlassgrund)
	}

	/** Gibt die aktuell im Manager gespeicherten SchuelerSchulbesuchsdaten zurück. */
	public get daten(): SchuelerSchulbesuchsdaten {
		return this._state.value.daten;
	}

	/** Gibt die aktuell im Manager gespeicherten Schulen zurück */
	public get schulen() : Map<string, SchulEintrag> {
		return this._schulen;
	}

	/** Gibt die aktuell im Manager gespeicherten Merkmal zurück */
	public get merkmale() : Map<number, Merkmal> {
		return this._merkmale;
	}

	/** Gibt die aktuell im Manager gespeicherten Entlassgründe zurück */
	public get entlassgruende() : Map<number, KatalogEntlassgrund> {
		return this._entlassgruende;
	}

	/** Gibt das Schuljahr vom Schuljahresabschnitts des aktuell ausgewählten Schülers zurück */
	public get schuljahr() : number {
		return this._schuljahr;
	}

	/** Gibt die vorherige Schule des aktuell ausgewählten Schülers zurück */
	public getVorigeSchule() : SchulEintrag | undefined {
		return this.schulen.get(this.daten.vorigeSchulnummer ?? '');
	}

	/** Gibt die vorherige Versetzungsart des ausgewählten Schülers zurück */
	public getVorigeArtLetzteVersetzung() : Herkunftsarten | undefined {
		if (this.daten.vorigeArtLetzteVersetzung === null)
			return undefined;
		const artID = Number(this.daten.vorigeArtLetzteVersetzung);
		return Herkunftsarten.getByID(artID) || undefined;
	}

	/** Gibt die vorherige AllgHerkunft des ausgewählten Schülers zurück */
	public getVorigeAllgHerkunft() {
		if (this.daten.vorigeAllgHerkunft === null)
			return "";
		const value = Schulform.data().getWertByKuerzel(this.daten.vorigeAllgHerkunft);
		if (value === null)
			return "";
		const entry = Schulform.data().getEintragBySchuljahrUndWert(this.schuljahr, value);
		return (entry !== null) ? entry.text : "";
	}

	/** Gibt die Herkunftsarten der vorherigen Schulform des ausgewählten Schülers zurück */
	public getHerkunftsarten() : Herkunftsarten[] {
		return Herkunftsarten.values().filter(
			h => h.getBezeichnung(this._schuljahr, this.getVorigeSchulform() || Schulform.G) !== null);
	}

	/** Gibt die vorherige Schulform des ausgewählten Schülers zurück */
	public getVorigeSchulform() :Schulform | undefined {
		const vorigeAllgHerkunft = this.daten.vorigeAllgHerkunft;
		if (vorigeAllgHerkunft === null)
			return undefined;
		const sgl = Schulgliederung.data().getWertByKuerzel(vorigeAllgHerkunft);
		if (sgl !== null)
			return Schulform.BK;
		return Schulform.data().getWertByKuerzel(vorigeAllgHerkunft) || undefined;
	}

	/** Gibt den Entlassjahrgang des ausgewählten Schülers zurück */
	public getEntlassjahrgang(field: "vorigeEntlassjahrgang" | "entlassungJahrgang") {
		return Jahrgaenge.values().find(v => v.daten(this.schuljahr)?.kuerzel === this.daten[field]) ?? null;
	}

	/** Gibt den Entlassgrund des ausgewählten Schülers zurück */
	public getEntlassgrund(field: "entlassungGrundID" | "vorigeEntlassgrundID") {
		return this.entlassgruende.get(this.daten[field] ?? -1);
	}

	/** Gibt die Einschulungsart des ausgewählten Schülers zurück */
	public getEinschulungsart() : Einschulungsart | undefined {
		return Einschulungsart.values().find(v => v.daten.id === this.daten.grundschuleEinschulungsartID);
	}

	/** Gibt die PrimarstufeSchuleingangsphaseBesuchsjahre des ausgewählten Schülers zurück */
	public getEPJahre() : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertBySchluessel(this.daten.grundschuleJahreEingangsphase?.toString() ?? '')
	}

	/** Gibt die Übergangsempfehlung des ausgewählten Schülers zurück */
	public getUebergangsempfehlung() : Uebergangsempfehlung | null {
		return Uebergangsempfehlung.data().getWertByKuerzel(this.daten.kuerzelGrundschuleUebergangsempfehlung ?? '')
	}

	/** Gibt die Sek1 Schulform des ausgewählten Schülers zurück */
	public getSchulformSek1() : Schulform | null {
		return Schulform.data().getWertByKuerzel(this.daten.sekIErsteSchulform?? '');
	}

	// --- patch ---

	/** Die allgemeine Patch-Methode der SchuelerSchulbesuchsdaten */
	public async doPatch(daten : Partial<SchuelerSchulbesuchsdaten>) {
		await this._patch(daten)
		const patchedDaten = Object.assign(this.daten, daten);
		this._state.value = Object.assign({ ...this._state.value}, { daten: patchedDaten });
	}

	/** Die spezielle Patch-Methode der Entlassgründe */
	public patchEntlassgrund(v: any, field: "entlassungGrundID" | "vorigeEntlassgrundID") {
		void this.doPatch({ [field]: v?.id ?? null });
	}

	/** Die spezielle Patch-Methode der vorigeArtLetzteVersetzung */
	public patchVorigeArtLetzteVersetzung(value : Herkunftsarten | undefined | null) {
		void this.doPatch({ vorigeArtLetzteVersetzung: ((value === null) || (value === undefined)) ? null : value.daten.id.toString() });
	}

	/** Die spezielle Patch-Methode der Schuleinträge */
	public patchSchule(schule: SchulEintrag | undefined | null, key: string) {
		const patchData: Record<string, any> = {};
		if (schule !== undefined && schule !== null)
			patchData[key] = schule.schulnummer;
		else
			patchData[key] = null;
		void this.doPatch(patchData);

	}

	/** Die spezielle Patch-Methode der Entlassjahrgänge */
	public patchEntlassjahrgang(v: Jahrgaenge | undefined | null, field : "vorigeEntlassjahrgang" | "entlassungJahrgang") {
		void this.doPatch({ [field]: v?.daten(this.schuljahr)?.kuerzel ?? null });
	}

	// --- util ---

	private calcSchuljahr(): number {
		const abschnitt = this._schuljahresabschnitte.get(this._state.value.auswahl.idSchuljahresabschnitt?? -1);
		return (abschnitt === undefined) ? -1 : abschnitt.schuljahr;
	}
}
