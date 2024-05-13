import { ArrayList, DeveloperNotificationException, FachListeManager, type FachDaten  } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogFachDaten } from "./RouteKatalogFachDaten";
import { routeKatalogFaecher } from "./RouteKatalogFaecher";

interface RouteStateKatalogFaecher extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	fachListeManager: FachListeManager;
}

const defaultState = <RouteStateKatalogFaecher> {
	idSchuljahresabschnitt: -1,
	fachListeManager: new FachListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKatalogFachDaten,
};

export class RouteDataKatalogFaecher extends RouteData<RouteStateKatalogFaecher> {

	public constructor() {
		super(defaultState);
	}

	get fachListeManager(): FachListeManager {
		return this._state.value.fachListeManager;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			 return null;
		return await this.ladeListe(idSchuljahresabschnitt);
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}


	private async ladeListeIntern(idSchuljahresabschnitt : number) {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		// Bestimme die Fachdaten vorher, um ggf. eine neue ID für das Routing zurückzugeben
		const hatteAuswahl = (this.fachListeManager.auswahlID() !== null) ? this.fachListeManager.auswahl() : null;
		const fachListeManager = new FachListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,	api.schulform, listKatalogeintraege);
		// Wählen nun ein Fach aus, dabei wird sich ggf. an der alten Auswahl orientiert
		if (hatteAuswahl && (hatteAuswahl.kuerzel !== null)) {
			let auswahl = fachListeManager.getByKuerzelOrNull(hatteAuswahl.kuerzel);
			if ((auswahl === null) && (fachListeManager.liste.size() > 0))
				auswahl = fachListeManager.liste.list().get(0);
			if (auswahl !== null)
				fachListeManager.setDaten(auswahl);
		}
		return {fachListeManager, idSchuljahresabschnitt};
	}

	public async ladeListe(idSchuljahresabschnitt: number) {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return null;
		const o = await this.ladeListeIntern(idSchuljahresabschnitt);
		this.setPatchedDefaultState(o);
		return o.fachListeManager.auswahlID();
	}

	private async getDatenInternal(auswahl: FachDaten) {
		return await api.server.getFach(api.schema, auswahl.id);
	}

	setEintrag = async (fach: FachDaten | null) => {
		if ((fach === null) && (!this.fachListeManager.hasDaten()))
			return;
		const fachListeManager = this.fachListeManager;
		if ((fach === null) || (fachListeManager.liste.list().isEmpty())) {
			fachListeManager.setDaten(null);
			this.setPatchedState({ fachListeManager });
			return;
		}
		if ((fach !== null) && (fachListeManager.hasDaten() && (fach.id === fachListeManager.auswahl().id)))
			return;
		const daten = await this.getDatenInternal(fach);
		fachListeManager.setDaten(daten);
		this.setPatchedState({ fachListeManager });
	}

	gotoEintrag = async (eintrag: FachDaten) => {
		await RouteManager.doRoute(routeKatalogFaecher.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FachDaten>) => {
		if (!this.fachListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idFach = this.fachListeManager.auswahl().id;
		const daten = this.fachListeManager.daten();
		await api.server.patchFach(data, api.schema, idFach);
		Object.assign(daten, data);
		const eintrag = this.fachListeManager.liste.get(idFach);
		if (eintrag !== null) {
			if (data.sortierung !== undefined)
				eintrag.sortierung = data.sortierung;
			if (data.kuerzel !== undefined)
				eintrag.kuerzel = data.kuerzel;
			if (data.bezeichnung !== undefined)
				eintrag.bezeichnung = data.bezeichnung;
		}
		this.fachListeManager.setDaten(daten);
		this.commit();
	}

	setFilter = async () => {
		if (!this.fachListeManager.hasDaten()) {
			const listFiltered = this.fachListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		const fachListeManager = this.fachListeManager;
		this.setPatchedState({ fachListeManager });
	}

	setzeDefaultSortierungSekII = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl = this.fachListeManager.auswahl();
		await api.server.setFaecherSortierungSekII(api.schema);
		const { fachListeManager } = await this.ladeListeIntern(idSchuljahresabschnitt);
		this._state.value.fachListeManager = fachListeManager;
		await this.setEintrag(auswahl);
	}

}
