import { ArrayList, DeveloperNotificationException, JahrgangListeManager, type JahrgangsDaten, type List } from "@core";

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuleJahrgaenge } from "./RouteSchuleJahrgaenge";
import { routeSchuleJahrgaengeDaten } from "./RouteSchuleJahrgaengeDaten";
import { RoutingStatus } from "~/router/RoutingStatus";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeSchuleJahrgangNeu } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangNeu";
import { routeSchuleJahrgangGruppenprozesse } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangGruppenprozesse";

interface RouteStateSchuleJahrgaenge extends RouteStateInterface {
	jahrgangListeManager: JahrgangListeManager;
}

const defaultState = <RouteStateSchuleJahrgaenge> {
	jahrgangListeManager: new JahrgangListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSchuleJahrgaengeDaten,
};


export class RouteDataSchuleJahrgaenge extends RouteData<RouteStateSchuleJahrgaenge> {

	public constructor() {
		super(defaultState);
	}

	get jahrgangListeManager(): JahrgangListeManager {
		return this._state.value.jahrgangListeManager;
	}

	public async ladeDaten(isEntering: boolean) {
		if (!isEntering)
			return null;

		const jahrgaenge = await api.server.getJahrgaenge(api.schema);
		const jahrgangListeManager = new JahrgangListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.abschnitte, api.schulform, jahrgaenge);

		const auswahl = jahrgangListeManager.liste.list().isEmpty() ? null : jahrgangListeManager.liste.list().get(0);
		jahrgangListeManager.setDaten(auswahl);

		this.setPatchedDefaultState({ jahrgangListeManager: jahrgangListeManager });
		return this.jahrgangListeManager.auswahlID();
	}

	add = async (data: Partial<JahrgangsDaten>): Promise<void> => {
		// Muss implementiert werden
	}

	deleteJahrgaengeCheck = (): [boolean, List<string>] => {
		// Muss implementiert werden
		return [false, new ArrayList()];
	}

	deleteJahrgaenge = async (): Promise<[boolean, List<string>]> => {
		// Muss implementiert werden
		return [false, new ArrayList()];
	}

	setEintrag = async (jahrgangsDaten: JahrgangsDaten | null) => {
		if ((jahrgangsDaten === null) && (!this.jahrgangListeManager.hasDaten()))
			return;
		if ((jahrgangsDaten === null) || (this.jahrgangListeManager.liste.list().isEmpty())) {
			this.jahrgangListeManager.setDaten(null);
			this.commit();
			return;
		}

		if ((this.jahrgangListeManager.hasDaten() && (jahrgangsDaten.id === this.jahrgangListeManager.auswahl().id)))
			return;

		this.jahrgangListeManager.setDaten(jahrgangsDaten);
		this.commit();
	}

	private setDefaults() {
		this.activeViewType = ViewType.DEFAULT;
		this._state.value.view = routeSchuleJahrgaengeDaten;
	}

	gotoDefaultView = async (eintragId?: number | null) => {
		if ((eintragId !== null) && (eintragId !== undefined) && this.jahrgangListeManager.liste.has(eintragId)) {
			const route = (this.view === routeSchuleJahrgaengeDaten) ? this.view.getRoute({ id: eintragId }) : routeSchuleJahrgaenge.getRoute({ id: eintragId })
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)
				await this.setEintrag(this.jahrgangListeManager.liste.get(eintragId));

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.jahrgangListeManager.filtered();
		if (!filtered.isEmpty()) {
			const jahrgang = this.jahrgangListeManager.filtered().getFirst();
			const route = routeSchuleJahrgaengeDaten.getRoute({ id: jahrgang.id });
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setEintrag(jahrgang);
			this.commit();
		}
	}

	gotoHinzufuegenView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.HINZUFUEGEN || (this._state.value.view === routeSchuleJahrgangNeu)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.HINZUFUEGEN;

		if (navigate) {
			const result = await RouteManager.doRoute(routeSchuleJahrgangNeu.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.jahrgangListeManager.liste.auswahlClear();
		}

		this._state.value.view = routeSchuleJahrgangNeu;
		this.jahrgangListeManager.setDaten(null);
		this.commit();
	}

	gotoGruppenprozessView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.GRUPPENPROZESSE || (this._state.value.view === routeSchuleJahrgangGruppenprozesse)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate)
			await RouteManager.doRoute(routeSchuleJahrgangGruppenprozesse.getRoute());

		this._state.value.view = routeSchuleJahrgangGruppenprozesse;
		this.jahrgangListeManager.setDaten(null);
		this.commit();
	}

	patch = async (dataToPatch : Partial<JahrgangsDaten>) => {
		if (!this.jahrgangListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");

		const jahrgangsdaten = this.jahrgangListeManager.daten();
		await api.server.patchJahrgang(dataToPatch, api.schema, jahrgangsdaten.id);
		Object.assign(jahrgangsdaten, dataToPatch);

		this.jahrgangListeManager.setDaten(jahrgangsdaten);
		this.commit();
	}

}
