import type { FachDaten, List } from "@core";
import { ArrayList, DeveloperNotificationException, FachListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuleFachDaten } from "./RouteSchuleFachDaten";
import { routeSchuleFaecher } from "./RouteSchuleFaecher";
import { routeFachStundenplan } from "./stundenplan/RouteFachStundenplan";
import { RoutingStatus } from "~/router/RoutingStatus";
import { ViewType } from "@ui";
import { routeSchuleFachGruppenprozesse } from "./RouteSchuleFachGruppenprozesse";
import { routeSchuleFachNeu } from "./RouteSchuleFachNeu";

interface RouteStateSchuleFaecher extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	fachListeManager: FachListeManager;
}

const defaultState = <RouteStateSchuleFaecher> {
	idSchuljahresabschnitt: -1,
	fachListeManager: new FachListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSchuleFachDaten,
};

export class RouteDataSchuleFaecher extends RouteData<RouteStateSchuleFaecher> {

	public constructor() {
		super(defaultState);
	}

	get fachListeManager(): FachListeManager {
		return this._state.value.fachListeManager;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number, isEntering: boolean) {
		if (!isEntering && idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return null;
		return await this.ladeDatenZuSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}


	private async ladeDatenZuSchuljahresabschnitt(idSchuljahresabschnitt : number) {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		// Bestimme die Fachdaten vorher, um ggf. eine neue ID für das Routing zurückzugeben
		const hatteAuswahl = (this.fachListeManager.auswahlID() !== null) ? this.fachListeManager.auswahl() : null;
		const fachListeManager = new FachListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,	api.schulform, listKatalogeintraege);
		fachListeManager.setFilterAuswahlPermitted(true);
		// Wählen nun ein Fach aus, dabei wird sich ggf. an der alten Auswahl orientiert
		if (hatteAuswahl) {
			let auswahl = fachListeManager.getByKuerzelOrNull(hatteAuswahl.kuerzel);
			if ((auswahl === null) && (fachListeManager.liste.size() > 0))
				auswahl = fachListeManager.liste.list().get(0);
			if (auswahl !== null) {
				const daten = await this.getDatenInternal(auswahl);
				fachListeManager.setDaten(daten);
			}
		}
		this.setPatchedDefaultState({ fachListeManager, idSchuljahresabschnitt });
		return this.fachListeManager.auswahlID();
	}

	private async getDatenInternal(auswahl: FachDaten) {
		return await api.server.getFach(api.schema, auswahl.id);
	}

	setEintrag = async (fach: FachDaten | null) => {
		if ((fach === null) && (!this.fachListeManager.hasDaten()))
			return;
		if ((fach === null) || (this.fachListeManager.liste.list().isEmpty())) {
			this.fachListeManager.setDaten(null);
			this.setPatchedState({ fachListeManager: this.fachListeManager });
			return;
		}
		if ((this.fachListeManager.hasDaten() && (fach.id === this.fachListeManager.auswahl().id)))
			return;
		const daten = await this.getDatenInternal(fach);
		this.fachListeManager.setDaten(daten);
		this.setPatchedState({ fachListeManager: this.fachListeManager });
	}

	gotoEintrag = async (eintrag: FachDaten) => {
		await RouteManager.doRoute(routeSchuleFaecher.getRouteSelectedChild({ id: eintrag.id }));
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
		await this.ladeDatenZuSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(auswahl);
	}

	add = async (data: Partial<FachDaten>): Promise<void> => {
		api.status.start();
		const fachDaten = await api.server.addFach(data, api.schema);
		this.fachListeManager.liste.add(fachDaten)
		this.commit()
		api.status.stop()
	}

	private setDefaults() {
		this.activeViewType = ViewType.DEFAULT;
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : routeSchuleFachDaten;
	}

	gotoDefaultView = async (eintragId?: number | null) => {
		if ((eintragId !== null) && (eintragId !== undefined) && this.fachListeManager.liste.has(eintragId)) {
			const route = ((this.view === routeSchuleFachDaten) || (this.view === routeFachStundenplan)) ? this.view.getRoute({ id: eintragId }) : routeSchuleFaecher.getRoute({ id: eintragId })
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)
				await this.setEintrag(this.fachListeManager.liste.get(eintragId));

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.fachListeManager.filtered();
		if (!filtered.isEmpty()) {
			const fach = this.fachListeManager.filtered().getFirst();
			const route = routeSchuleFachDaten.getRoute({ id: fach.id });
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setEintrag(fach);
			this.commit();
		}
	}

	gotoHinzufuegenView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.HINZUFUEGEN || (this._state.value.view === routeSchuleFachNeu)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.HINZUFUEGEN;

		if (navigate) {
			const result = await RouteManager.doRoute(routeSchuleFachNeu.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.fachListeManager.liste.auswahlClear();
		}

		this._state.value.view = routeSchuleFachNeu;
		this.fachListeManager.setDaten(null);
		this.commit();
	}

	gotoGruppenprozessView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.GRUPPENPROZESSE || (this._state.value.view === routeSchuleFachGruppenprozesse)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate)
			await RouteManager.doRoute(routeSchuleFachGruppenprozesse.getRoute());

		this._state.value.view = routeSchuleFachGruppenprozesse;
		this.fachListeManager.setDaten(null);
		this.commit();
	}

	public deleteFaecherCheck = (): [boolean, List<string>] => {
		const errorLog: List<string> = new ArrayList<string>();
		return [false, errorLog];
	}

	public deleteFaecher = async (): Promise<[boolean, List<string>]> => {
		return [false, new ArrayList<string>()];
	}
}
