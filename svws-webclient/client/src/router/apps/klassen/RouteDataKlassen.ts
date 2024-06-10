
import type { KlassenDaten, Schueler} from "@core";
import { ArrayList, DeveloperNotificationException, KlassenListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import { type RouteNode } from "~/router/RouteNode";


interface RouteStateKlassen extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	klassenListeManager: KlassenListeManager;
	mapKlassenVorigerAbschnitt: Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: Map<number, KlassenDaten>;
	oldView?: RouteNode<any, any>;
	gruppenprozesseEnabled: boolean;
}

const defaultState = <RouteStateKlassen> {
	idSchuljahresabschnitt: -1,
	klassenListeManager: new KlassenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()),
	mapKlassenVorigerAbschnitt: new Map<number, KlassenDaten>(),
	mapKlassenFolgenderAbschnitt: new Map<number, KlassenDaten>(),
	view: routeKlasseDaten,
	oldView: undefined,
	gruppenprozesseEnabled: false,
};

export class RouteDataKlassen extends RouteData<RouteStateKlassen> {

	public constructor() {
		super(defaultState);
	}

	get klassenListeManager(): KlassenListeManager {
		return this._state.value.klassenListeManager;
	}

	get mapKlassenVorigerAbschnitt(): Map<number, KlassenDaten> {
		return this._state.value.mapKlassenVorigerAbschnitt;
	}

	get mapKlassenFolgenderAbschnitt(): Map<number, KlassenDaten> {
		return this._state.value.mapKlassenFolgenderAbschnitt;
	}

	get gruppenprozesseEnabled(): boolean {
		return this._state.value.gruppenprozesseEnabled;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			return null;

		// Lade die Kataloge und erstelle den Manager
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapKlassenVorigerAbschnitt = schuljahresabschnitt.idVorigerAbschnitt === null
			? new Map<number, KlassenDaten>()
			: await api.getKlassenListe(schuljahresabschnitt.idVorigerAbschnitt);
		const mapKlassenFolgenderAbschnitt = schuljahresabschnitt.idFolgeAbschnitt === null
			? new Map<number, KlassenDaten>()
			: await api.getKlassenListe(schuljahresabschnitt.idFolgeAbschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const klassenListeManager = new KlassenListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listKlassen, listSchueler, listJahrgaenge, listLehrer);

		// Versuche die ausgewählte Klasse von vorher zu laden
		const vorherigeAuswahl = this.klassenListeManager.hasDaten() ? this.klassenListeManager.auswahl() : null;
		if ((vorherigeAuswahl !== null) && (vorherigeAuswahl.kuerzel !== null)) {
			const auswahl = klassenListeManager.getByKuerzelOrNull(vorherigeAuswahl.kuerzel);
			klassenListeManager.setDaten(auswahl ?? klassenListeManager.liste.list().get(0));
		}

		// Aktualisiere den State
		this.setPatchedDefaultState({ idSchuljahresabschnitt, klassenListeManager, mapKlassenVorigerAbschnitt, mapKlassenFolgenderAbschnitt, gruppenprozesseEnabled: this._state.value.gruppenprozesseEnabled});
		return klassenListeManager.auswahlID();
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			 return null;
		return await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	public async setEintrag(klasse: KlassenDaten | null) {
		if ((klasse === null) && (!this.klassenListeManager.hasDaten()))
			return;

		if ((klasse === null) || (this.klassenListeManager.liste.list().isEmpty())) {
			this.klassenListeManager.setDaten(null);
			this.commit();
			return;
		}

		if (this.klassenListeManager.hasDaten() && (klasse.id === this.klassenListeManager.auswahl().id))
			return;

		let daten = this.klassenListeManager.liste.get(klasse.id);
		if (daten === null)
			daten = this.klassenListeManager.filtered().isEmpty() ? null : this.klassenListeManager.filtered().get(0);

		this.klassenListeManager.setDaten(daten);
		this.commit();
	}

	public deleteKlassenCheck = (): [boolean, ArrayList<string>] => {
		const errorLog: ArrayList<string> = new ArrayList();
		if (!this.klassenListeManager.liste.auswahlExists())
			errorLog.add('Es wurde keine Klasse zum Löschen ausgewählt.')
		for (const klasse of this.klassenListeManager.liste.auswahlSorted())
			if (klasse.schueler.size() > 0)
				errorLog.add(`Klasse ${klasse.kuerzel} (ID: ${klasse.id}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
		return [errorLog.isEmpty(), errorLog];
	}

	public deleteKlassen = async (): Promise<[boolean, ArrayList<string | null>]>  => {
		const ids = new ArrayList<number>();
		for (const klasse of this.klassenListeManager.liste.auswahlSorted())
			ids.add(klasse.id);

		const operationResponses = await api.server.deleteKlassen(ids, api.schema);

		const klassenToRemove = new ArrayList<KlassenDaten>();
		const logMessages = new ArrayList<string | null>();
		let status = true;
		for (const response of operationResponses) {
			if (response.success && response.id) {
				const klasse = this.klassenListeManager.liste.get(response.id);
				logMessages.add(`Klasse ${klasse?.kuerzel} (ID: ${response.id}) wurde erfolgreich gelöscht.`);
				klassenToRemove.add(klasse);
			} else {
				status = false;
				logMessages.addAll(response.log);
			}
		}

		if (!klassenToRemove.isEmpty()) {
			this._state.value.klassenListeManager.liste.auswahlClear();
			this._state.value.klassenListeManager.setDaten(null);
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [status, logMessages];
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (!this.klassenListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idKlasse = this.klassenListeManager.auswahl().id;
		const daten = this.klassenListeManager.daten();
		if (daten === null)
			return;
		await api.server.patchKlasse(data, api.schema, idKlasse);
		Object.assign(daten, data);
		const eintrag = this.klassenListeManager.liste.get(idKlasse);
		if (eintrag !== null) {
			if (data.kuerzel !== undefined)
				eintrag.kuerzel = data.kuerzel;
			if (data.sortierung !== undefined)
				eintrag.sortierung = data.sortierung;
		}
		this.klassenListeManager.setDaten(daten);
		this.commit();
	}

	gotoEintrag = async (eintragId?: number | null) => {
		if ((eintragId !== null) && (eintragId !== undefined)) {
			// Deaktivieren des Gruppenprozess Modus falls noch aktiv
			this._state.value.klassenListeManager.liste.auswahlClear()
			this._state.value.gruppenprozesseEnabled = false;

			const view = this._state.value.view === routeKlasseGruppenprozesse ? routeKlasseDaten : this.view;
			if (this._state.value.klassenListeManager.liste.has(eintragId)){
				await RouteManager.doRoute(view.getRoute(eintragId));
				await this.setEintrag(this._state.value.klassenListeManager.liste.get(eintragId))
				return
			}
		}

		const filtered = this._state.value.klassenListeManager.filtered();
		if (!filtered.isEmpty()) {
			const klasse = this._state.value.klassenListeManager.filtered().getFirst();
			await RouteManager.doRoute(routeKlasseDaten.getRoute(klasse.id));
			await this.setEintrag(klasse);
		}
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	gotoGruppenprozess = async () => {
		if (this._state.value.gruppenprozesseEnabled || this._state.value.view === routeKlasseGruppenprozesse && this._state.value.gruppenprozesseEnabled) {
			this.commit();
			return;
		}

		this._state.value.gruppenprozesseEnabled = true;
		this._state.value.oldView = this._state.value.view;

		// Aktiviere Gruppenprozess View
		await RouteManager.doRoute(routeKlasseGruppenprozesse.getRoute(-1));
		this._state.value.view = routeKlasseGruppenprozesse;
		this._state.value.klassenListeManager.setDaten(null);
	}

	setFilter = async () => {
		if (!this.klassenListeManager.hasDaten()) {
			const listFiltered = this.klassenListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0).id);
				return;
			}
		}
		const klassenListeManager = this.klassenListeManager;
		this.setPatchedState({ klassenListeManager });
	}

	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.klassenListeManager.auswahl().id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.klassenListeManager.liste.get(auswahl_id));
	}

}
