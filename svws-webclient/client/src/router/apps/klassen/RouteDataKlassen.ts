
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
}

const defaultState = <RouteStateKlassen> {
	idSchuljahresabschnitt: -1,
	klassenListeManager: new KlassenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()),
	mapKlassenVorigerAbschnitt: new Map<number, KlassenDaten>(),
	mapKlassenFolgenderAbschnitt: new Map<number, KlassenDaten>(),
	view: routeKlasseDaten,
	oldView: undefined,
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

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			return null;
		// Bestimme die Klassendaten vorher, um ggf. eine neue ID für das Routing zurückzugeben
		const hatteAuswahl = (this.klassenListeManager.auswahlID() !== null) ? this.klassenListeManager.auswahl() : null;
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
		// Wählen nun eine Klasse aus, dabei wird sich ggf. an der alten Auswahl orientiert
		let result : number | null = null;
		if (hatteAuswahl && (hatteAuswahl.kuerzel !== null)) {
			let auswahl = klassenListeManager.getByKuerzelOrNull(hatteAuswahl.kuerzel);
			if ((auswahl === null) && (klassenListeManager.liste.size() > 0))
				auswahl = klassenListeManager.liste.list().get(0);
			if (auswahl !== null) {
				klassenListeManager.setDaten(auswahl);
				result = auswahl.id;
			}
		}
		// Aktualisiere den State
		this.setPatchedDefaultState({ idSchuljahresabschnitt, klassenListeManager, mapKlassenVorigerAbschnitt, mapKlassenFolgenderAbschnitt });
		return result;
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
		if ((klasse !== null) && (this.klassenListeManager.hasDaten() && (klasse.id === this.klassenListeManager.auswahl().id)))
			return;
		let daten = this.klassenListeManager.liste.get(klasse.id);
		if (daten === null)
			daten = this.klassenListeManager.filtered().isEmpty() ? null : this.klassenListeManager.filtered().get(0);
		this.klassenListeManager.setDaten(daten);
		this.commit();
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

	gotoEintrag = async (eintrag: KlassenDaten) => {
		await RouteManager.doRoute(this.view.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	setGruppenprozess = async (value: boolean) => {
		if (((value === true) && (this._state.value.oldView !== undefined))
			|| ((value === false) && (this._state.value.oldView === undefined))) {
			this.commit();
			return;
		}
		if (value) {
			this._state.value.oldView = this._state.value.view;
			await RouteManager.doRoute(routeKlasseGruppenprozesse.getRoute(this._state.value.klassenListeManager.auswahlID() ?? -1));
			return;
		}
		const view = this._state.value.oldView ?? routeKlasseDaten;
		this._state.value.oldView = undefined;
		await RouteManager.doRoute(view.getRoute(this._state.value.klassenListeManager.auswahlID() ?? -1));
	}

	setFilter = async () => {
		if (!this.klassenListeManager.hasDaten()) {
			const listFiltered = this.klassenListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
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
