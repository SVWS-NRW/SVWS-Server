import type { KatalogEntlassgrund, Merkmal, SchulEintrag, SchuelerSchulbesuchSchule, SchuelerSchulbesuchMerkmal, Kindergarten } from "@core";
import type { Schuljahresabschnitt, List } from "@core";
import { Einschulungsart, Herkunftsarten, Jahrgaenge, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Uebergangsempfehlung, Kindergartenbesuch,
	SchuelerSchulbesuchsdaten, SchuelerListeEintrag } from "@core";
import { StateManager } from "~/router/StateManager";


interface ManagerStateDataSchuelerSchulbesuch {
	auswahl: SchuelerListeEintrag;
	daten: SchuelerSchulbesuchsdaten;
}

const defaultState = <ManagerStateDataSchuelerSchulbesuch> {
	auswahl: new SchuelerListeEintrag(),
	daten: new SchuelerSchulbesuchsdaten(),
};

/** Ein Manager für die Verwaltung von Schulbesuchsdaten */
export class SchuelerSchulbesuchManager extends StateManager<ManagerStateDataSchuelerSchulbesuch> {

	/** Eine Map der Schuljahresabschnitte gemappt nach idSchuljahresAbschnitt */
	protected _schuljahresabschnitteById: Map<number, Schuljahresabschnitt> = new Map();

	/** Eine Map der Schulen gemappt nach Schulnummer */
	protected _schulenById: Map<number, SchulEintrag> = new Map();

	/** Eine Map der Merkmale gemappt nach id */
	protected _merkmaleById: Map<number, Merkmal> = new Map();

	/** Eine Map der Entlassgruende gemappt nach id */
	protected _entlassgruendeById: Map<number, KatalogEntlassgrund> = new Map();

	/** Eine Map der Kindergärten gemappt nach id */
	protected _kindergaertenById: Map<number, Kindergarten> = new Map();

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
	 * @param kindergaerten			   eine Liste der Kindergärten
	 * @param patch					   die PatchMethode der SchuelerSchulbesuchsdaten
	 */
	public constructor(daten: SchuelerSchulbesuchsdaten, auswahl: SchuelerListeEintrag, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulen: List<SchulEintrag>, merkmale: List<Merkmal>, entlassgruende: List<KatalogEntlassgrund>, kindergaerten: List<Kindergarten>,
		patch: (data: Partial<SchuelerSchulbesuchsdaten>) => Promise<void>) {
		super(defaultState)
		this._state.value.daten = daten;
		this._state.value.auswahl = auswahl;
		this._patch = patch;
		this.mapSchuljahresabschnitte(schuljahresabschnitte);
		this.mapSchulen(schulen);
		this.mapMerkmale(merkmale);
		this.mapEntlassgruende(entlassgruende);
		this.mapKindergaerten(kindergaerten);
		this._schuljahr = this.calcSchuljahr();
	}

	private mapKindergaerten(kindergaerten: List<Kindergarten>) {
		for (const kindergarten of kindergaerten)
			this._kindergaertenById.set(kindergarten.id, kindergarten);
	}

	private mapSchuljahresabschnitte(schuljahresabschnitte: List<Schuljahresabschnitt>) {
		for (const abschnitt of schuljahresabschnitte)
			this._schuljahresabschnitteById.set(abschnitt.id, abschnitt);
	}

	private mapSchulen(schulen : List<SchulEintrag>) {
		for (const schule of schulen)
			this._schulenById.set(schule.id, schule);
	}

	private mapMerkmale(merkmale : List<Merkmal>) {
		for (const merkmal of merkmale)
			this._merkmaleById.set(merkmal.id, merkmal);
	}

	private mapEntlassgruende(entlassgruende : List<KatalogEntlassgrund>) {
		for (const entlassgrund of entlassgruende)
			this._entlassgruendeById.set(entlassgrund.id, entlassgrund)
	}

	/** Gibt die aktuell im Manager gespeicherten SchuelerSchulbesuchsdaten zurück. */
	public get daten(): SchuelerSchulbesuchsdaten {
		return this._state.value.daten;
	}

	/** Gibt die aktuell im Manager gespeicherten Schulen zurück */
	public get schulenById() : Map<number, SchulEintrag> {
		return this._schulenById;
	}

	/** Gibt die aktuell im Manager gespeicherten Merkmal zurück */
	public get merkmaleById() : Map<number, Merkmal> {
		return this._merkmaleById;
	}

	/** Gibt die aktuell im Manager gespeicherten Entlassgründe zurück */
	public get entlassgruendeById() : Map<number, KatalogEntlassgrund> {
		return this._entlassgruendeById;
	}

	/** Gibt die aktuell im Manager gespeicherten Kindergärten zurück */
	public get kindergaertenById() : Map<number, Kindergarten> {
		return this._kindergaertenById;
	}

	/** Gibt das Schuljahr vom Schuljahresabschnitts des aktuell ausgewählten Schülers zurück */
	public get schuljahr() : number {
		return this._schuljahr;
	}

	/** Gibt die vorherige Schule des aktuell ausgewählten Schülers zurück */
	public getVorherigeSchule() : SchulEintrag | undefined {
		return this.schulenById.get(this.daten.idVorherigeSchule ?? -1);
	}

	/** Gibt die vorherige Versetzungsart des ausgewählten Schülers zurück */
	public getVorigeArtLetzteVersetzung() : Herkunftsarten | undefined {
		if (this.daten.vorigeArtLetzteVersetzung === null)
			return undefined;
		const artID = Number(this.daten.vorigeArtLetzteVersetzung);
		return Herkunftsarten.getByID(artID) || undefined;
	}


	/** Gibt die vorherige AllgHerkunft des ausgewählten Schülers zurück. Diese ist abhängig von der ausgewählten Schule */
	public getVorigeAllgHerkunft() : string | null {
		const value = this.getVorigeSchulform();
		if (value === null)
			return null;
		const entry = Schulform.data().getEintragBySchuljahrUndWert(this.schuljahr, value);
		return (entry !== null) ? entry.text : null;
	}

	/** Gibt eine Liste der Jahrgaenge zurück, die in der gegebenen Schulform möglich sind */
	public getJahrgaengeBySchulform(schulform : Schulform | null) : List<Jahrgaenge> {
		if (schulform === null)
			return Jahrgaenge.data().getWerte();
		return Jahrgaenge.data().getListBySchuljahrAndSchulform(this.schuljahr, schulform)
	}

	/** Gibt die Herkunftsarten der vorherigen Schulform des ausgewählten Schülers zurück */
	public getHerkunftsarten() : Herkunftsarten[] {
		return Herkunftsarten.values().filter(
			h => h.getBezeichnung(this._schuljahr, this.getVorigeSchulform() || Schulform.G) !== null);
	}

	/** Gibt die vorherige Schulform des ausgewählten Schülers zurück */
	public getVorigeSchulform() :Schulform | null {
		const schule = this.getVorherigeSchule();
		if (schule === undefined || schule.idSchulform === null)
			return null;
		return Schulform.data().getWertByID(schule.idSchulform);
	}

	/** Gibt den Entlassjahrgang des ausgewählten Schülers zurück */
	public getEntlassjahrgang(field: "vorigeEntlassjahrgang" | "entlassungJahrgang") {
		return Jahrgaenge.values().find(v => v.daten(this.schuljahr)?.kuerzel === this.daten[field]) ?? null;
	}

	/** Gibt den Entlassgrund des ausgewählten Schülers zurück */
	public getEntlassgrund(field: "entlassungGrundID" | "vorigeEntlassgrundID") {
		return this.entlassgruendeById.get(this.daten[field] ?? -1);
	}

	/** Gibt die Einschulungsart des ausgewählten Schülers zurück */
	public getEinschulungsart() : Einschulungsart | null {
		if (this.daten.grundschuleEinschulungsartID === null)
			return null;
		return Einschulungsart.data().getWertByID(this.daten.grundschuleEinschulungsartID);
	}

	/** Gibt die PrimarstufeSchuleingangsphaseBesuchsjahre des ausgewählten Schülers zurück */
	public getEPJahre() : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		if (this.daten.idGrundschuleJahreEingangsphase === null)
			return null;
		return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByID(this.daten.idGrundschuleJahreEingangsphase)
	}

	/** Gibt die Übergangsempfehlung des ausgewählten Schülers zurück */
	public getUebergangsempfehlung() : Uebergangsempfehlung | null {
		return Uebergangsempfehlung.data().getWertByKuerzel(this.daten.kuerzelGrundschuleUebergangsempfehlung ?? '')
	}

	/** Gibt die Sek1 Schulform des ausgewählten Schülers zurück */
	public getSchulformSek1() : Schulform | null {
		return Schulform.data().getWertByKuerzel(this.daten.sekIErsteSchulform?? '');
	}

	/** Gibt die Dauer des Kindergartenbesuchs zurück */
	public getDauerKindergartenbesuch() : Kindergartenbesuch | null {
		if (this.daten.idDauerKindergartenbesuch === null)
			return null;
		return Kindergartenbesuch.data().getWertByID(this.daten.idDauerKindergartenbesuch);
	}

	// --- patch ---

	/** Die allgemeine Patch-Methode der SchuelerSchulbesuchsdaten */
	public async doPatch(daten : Partial<SchuelerSchulbesuchsdaten>) {
		await this._patch(daten);
		const patchedDaten = Object.assign(this.daten, daten);
		this.setPatchedState({ daten: patchedDaten });
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
			patchData[key] = schule.id;
		else
			patchData[key] = null;
		void this.doPatch(patchData);

	}

	/** Die spezielle Patch-Methode der Entlassjahrgänge */
	public patchEntlassjahrgang(v: Jahrgaenge | undefined | null, field : "vorigeEntlassjahrgang" | "entlassungJahrgang") {
		void this.doPatch({ [field]: v?.daten(this.schuljahr)?.kuerzel ?? null });
	}

	/** Eintrag zu den bisher besuchten Schulen hinzufügen */
	public addSchuelerSchulbesuchSchule(s: SchuelerSchulbesuchSchule) {
		this.daten.alleSchulen.add(s);
	}

	/** Eintrag der bisher besuchten Schulen löschen */
	public deleteBisherigeSchuleById(id: number) {
		const index = this.getIndexBisherigeSchuleById(id);
		if (index !== undefined)
			this.daten.alleSchulen.removeElementAt(index);
	}

	/** Eintrag der bisher besuchten Schulen patchen */
	public patchBisherigeSchuleById(id: number, data: Partial<SchuelerSchulbesuchSchule>) {
		const index = this.getIndexBisherigeSchuleById(id);
		if (index === undefined)
			return;
		const schule = this.daten.alleSchulen.get(index);
		Object.assign(schule, data);
	}

	/** Eintrag zu den Merkmalen hinzufügen */
	public addSchuelerSchulbesuchMerkmal(m : SchuelerSchulbesuchMerkmal) {
		this.daten.merkmale.add(m);
	}

	/** Eintrag der Merkmale patchen */
	public patchSchuelerSchulbesuchMerkmalById(id: number, data: Partial<SchuelerSchulbesuchMerkmal>) {
		const index = this.getIndexMerkmalById(id);
		if (index === undefined)
			return;
		const merkmal = this.daten.merkmale.get(index);
		Object.assign(merkmal, data);
	}

	/** Eintrag der Merkmale löschen */
	public deleteSchuelerSchulbesuchMerkmal(id: number) {
		const index = this.getIndexMerkmalById(id);
		if (index !== undefined)
			this.daten.merkmale.removeElementAt(index);
	}

	// --- util ---

	private getIndexBisherigeSchuleById(id: number) : number | undefined {
		let index = 0;
		for (const s of this.daten.alleSchulen) {
			if (s.id === id)
				return index;
			index++;
		}
		return;
	}

	private getIndexMerkmalById(id: number) : number | undefined {
		let index = 0;
		for (const s of this.daten.merkmale) {
			if (s.id === id)
				return index;
			index++;
		}
		return;
	}

	private calcSchuljahr(): number {
		const abschnitt = this._schuljahresabschnitteById.get(this._state.value.auswahl.idSchuljahresabschnitt);
		return (abschnitt === undefined) ? -1 : abschnitt.schuljahr;
	}
}
