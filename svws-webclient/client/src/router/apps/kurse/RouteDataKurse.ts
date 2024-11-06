import type { KursDaten, List, Schueler } from "@core";
import { KursListeManager, DeveloperNotificationException, ArrayList } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { ViewType } from "@ui";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { RoutingStatus } from "~/router/RoutingStatus";
import { routeKurseNeu } from "./RouteKurseNeu";


interface RouteStateKurse extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	kursListeManager: KursListeManager | undefined;
}

const defaultState = <RouteStateKurse> {
	idSchuljahresabschnitt: -1,
	kursListeManager: undefined,
	view: routeKursDaten,
};

export class RouteDataKurse extends RouteData<RouteStateKurse> {

	public constructor() {
		super(defaultState);
	}

	get hatKursListeManagerManager(): boolean {
		return (this._state.value.kursListeManager !== undefined);
	}

	get kursListeManager(): KursListeManager {
		if (this._state.value.kursListeManager === undefined)
			throw new DeveloperNotificationException("Zugriff auf den Kurs-Liste-Manager, bevor dieser initialisiert wurde.");
		return this._state.value.kursListeManager;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			return null;

		// Lade die Kataloge und erstelle den Manager
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const listFaecher = await api.server.getFaecher(api.schema);
		const kursListeManager = new KursListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher);

		if (this._state.value.idSchuljahresabschnitt === -1) {
			// Initiales Setzen der Filter
			kursListeManager.setFilterAuswahlPermitted(true);
		} else {
			// Filter aus vorherigem Manager übernehmen
			// kursListeManager.useFilter(this._state.value.kursListeManager);
		}

		// Versuche den ausgewählten Kurs von vorher zu laden
		const vorherigeAuswahl = (this._state.value.kursListeManager !== undefined) && this.kursListeManager.hasDaten() ? this.kursListeManager.auswahl() : null;
		if (vorherigeAuswahl !== null) {
			const auswahl = kursListeManager.getByKuerzelAndJahrgangOrNull(vorherigeAuswahl.kuerzel, vorherigeAuswahl.idJahrgaenge.get(0));
			kursListeManager.setDaten(auswahl ?? kursListeManager.liste.list().get(0));
		}

		// stellt die ursprünglich gefilterte Liste wieder her
		kursListeManager.filtered();

		// Aktualisiere den State
		this.setPatchedDefaultState({ idSchuljahresabschnitt, kursListeManager, activeViewType: this.activeViewType });
		return this.kursListeManager.auswahlID();
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

	public async setEintrag(kurs: KursDaten | null) {
		if ((kurs === null) && (!this.kursListeManager.hasDaten()))
			return;
		if ((kurs === null) || (this.kursListeManager.liste.list().isEmpty())) {
			this.kursListeManager.setDaten(null);
			this.commit();
			return;
		}
		if ((this.kursListeManager.hasDaten() && (kurs.id === this.kursListeManager.auswahl().id)))
			return;
		let daten = this.kursListeManager.liste.get(kurs.id);
		if (daten === null)
			daten = this.kursListeManager.filtered().isEmpty() ? null : this.kursListeManager.filtered().get(0);
		this.kursListeManager.setDaten(daten);
		this.commit();
	}

	patch = async (data : Partial<KursDaten>) => {
		if (!this.kursListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idKurs = this.kursListeManager.auswahl().id;
		await api.server.patchKurs(data, api.schema, idKurs);
		const daten = this.kursListeManager.daten();
		Object.assign(daten, data);
		this.kursListeManager.setDaten(daten);
		this.commit();
	}

	gotoEintrag = async (eintrag: KursDaten) => {
		await RouteManager.doRoute(routeKurse.getRoute({ id: eintrag.id }));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	}

	setFilter = async () => {
		if (!this.kursListeManager.hasDaten()) {
			const listFiltered = this.kursListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		const kursListeManager = this.kursListeManager;
		this.setPatchedState({ kursListeManager });
	}

	add = async (partialKurs: Partial<KursDaten>): Promise<void> => {
		// TODO: Implementieren
	}

	private setDefaults() {
		this.activeViewType = ViewType.DEFAULT;
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : routeKursDaten;
	}

	gotoDefaultView = async (eintragId?: number | null) => {
		if ((eintragId !== null) && (eintragId !== undefined) && this.kursListeManager.liste.has(eintragId)) {
			const route = (this.view === routeKursDaten)
				? this.view.getRoute({ id: eintragId }) : routeKursDaten.getRoute({ id: eintragId });
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)
				await this.setEintrag(this.kursListeManager.liste.get(eintragId));

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.kursListeManager.filtered();
		if (!filtered.isEmpty()) {
			const kurs = this.kursListeManager.filtered().getFirst();
			const route = routeKursDaten.getRoute({ id: kurs.id });
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setEintrag(kurs);
			this.commit();
		}
	}

	gotoGruppenprozessView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.GRUPPENPROZESSE || (this._state.value.view === routeKurseGruppenprozesse)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate)
			await RouteManager.doRoute(routeKurseGruppenprozesse.getRoute());

		this._state.value.view = routeKurseGruppenprozesse;
		this.kursListeManager.setDaten(null);
		this.commit();
	}

	gotoHinzufuegenView = async (navigate: boolean) => {
		if (this.activeViewType === ViewType.HINZUFUEGEN || (this._state.value.view === routeKurseNeu)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.HINZUFUEGEN;

		if (navigate) {
			const result = await RouteManager.doRoute(routeKurseNeu.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.kursListeManager.liste.auswahlClear();
		}

		this._state.value.view = routeKurseNeu;
		this.kursListeManager.setDaten(null);
		this.commit();
	}

	deleteKurseCheck = (): [boolean, List<string>] => {
		const errorLog: List<string> = new ArrayList<string>();
		return [false, errorLog];
	}

	deleteKurse = async (): Promise<[boolean, List<string>]> => {
		return [false, new ArrayList<string>()];
	}

	/* TODO
	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.kursListeManager.auswahl().id;
		await api.server.setKursSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.kursListeManager.liste.get(auswahl_id));
	}
	*/

}
