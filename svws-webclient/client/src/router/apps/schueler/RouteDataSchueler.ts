import type { SchuelerListeEintrag, SchuelerStammdaten } from "@core";
import { ArrayList, DeveloperNotificationException, SchuelerListe, SchuelerListeManager, SchuelerStatus } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import type { RouteNode } from "~/router/RouteNode";
import { RouteType } from "~/router/RouteType";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import { RoutingStatus } from "~/router/RoutingStatus";
import { routeSchuelerGruppenprozesse } from "~/router/apps/schueler/RouteSchuelerGruppenprozesse";


interface RouteStateSchueler extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	schuelerListeManager: SchuelerListeManager | undefined;
	activeRouteType: RouteType;
	oldView?: RouteNode<any, any>;
	gruppenprozesseEnabled: boolean;
	creationModeEnabled: boolean;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	schuelerListeManager: undefined,
	view: routeSchuelerIndividualdaten,
	activeRouteType: RouteType.DEFAULT,
	oldView: undefined,
	gruppenprozesseEnabled: false,
	creationModeEnabled: false,
};

export class RouteDataSchueler extends RouteData<RouteStateSchueler> {

	public constructor() {
		super(defaultState);
	}

	get gruppenprozesseEnabled(): boolean {
		return this._state.value.gruppenprozesseEnabled;
	}

	get creationModeEnabled(): boolean {
		return this._state.value.creationModeEnabled;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number, isEntering: boolean) : Promise<number | null> {
		if (!isEntering && idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return null;
		return await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Setzt die Daten zum ausgewählten Schuljahresabschnitt und Schülers und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param {number | undefined} idSchueler   die ID des Schülers
	 * @param {boolean} isEntering              gibt an, ob die Route neu betreten wird
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		// Erzeuge neuen SchuelerListeManager, wenn die Route neu betreten wird oder der Schuljahresabschnitt geändert wird
		const schuelerListe: SchuelerListe = await this.loadSchuelerListe(idSchuljahresabschnitt);

		const manager = new SchuelerListeManager(api.schulform, schuelerListe, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
		manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		// Problem: Filter müsste entsprechend der Schuljahresauswahl angepasst werden, sonst wird der Schüler nicht geufunden.
		// if (this._state.value.schuelerListeManager !== undefined)
		// 	manager.useFilter(this._state.value.schuelerListeManager);

		// Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist und dieser im neuen Manager vorhanden ist
		const vorherigeAuswahl = ((this._state.value.schuelerListeManager !== undefined) && this.schuelerListeManager.hasDaten()) ? this.schuelerListeManager.auswahl() : null;
		if (vorherigeAuswahl !== null) {
			const auswahl = this.schuelerListeManager.liste.get(vorherigeAuswahl.id);
			let schuelerStammdaten = await this.loadSchuelerStammdaten(auswahl);
			if (schuelerStammdaten === null)
				schuelerStammdaten = await this.loadSchuelerStammdaten(manager.liste.list().get(0));

			this.schuelerListeManager.setDaten(schuelerStammdaten);
		}

		const view = this.getCurrentViewOrDefault(manager);

		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			schuelerListeManager: manager,
			activeRouteType: this._state.value.activeRouteType,
			view: view,
		});

		return this.schuelerListeManager.auswahlID();
	}

	/**
	 * Lädt das {@link SchuelerListe} Objekt von der API, für die übergebene Schuljahresabschnitt ID
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @returns Gibt SchuelerListe zurück
	 */
	private async loadSchuelerListe(idSchuljahresabschnitt: number): Promise<SchuelerListe> {
		// Lese die grundlegenden Daten für den Schuljahresabschnitt ein und erstelle den SchülerListeManager
		const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		return SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());
	}

	/**
	 * Liefert die bisher ausgewählte View oder die Default View {@link RouteSchuelerIndividualdaten}
	 *
	 * @returns aktuelle View oder Default View
	 */
	private getCurrentViewOrDefault(schuelerManager: SchuelerListeManager): RouteNode<any, any> | undefined {
		if (schuelerManager.hasDaten()) {
			return this._state.value.view;
		} else {
			return routeSchuelerIndividualdaten;
		}
	}

	/**
	 * Liefert den aktuell ausgewählten {@link SchuelerListeEintrag} oder <code>null</code> falls kein Schüler ausgewählt ist.
	 *
	 * @param {number | undefined} idSchueler          ID des Schülers
	 * @param {SchuelerListeManager} schuelerManager   SchuelerListeManager
	 * @param {boolean} isEntering                     gibt an ob die Route das erste mal betreten wird
	 *
	 * @returns den ausgewählten {@link SchuelerListeEintrag} oder <code>null</code>
	 */
	private async getSchuelerAuswahl(idSchueler: number | undefined, schuelerManager: SchuelerListeManager, isEntering: boolean): Promise<SchuelerListeEintrag | null> {
		if (schuelerManager.filtered().isEmpty())
			return null;

		// Wenn keine Schüler-ID ausgewählt ist, dann gebe null zurück oder beim Einsteigen in die Route den ersten Schüler der gefilterten Liste
		if (idSchueler === undefined)
			return (isEntering) ? schuelerManager.filtered().get(0) : null;

		// Wenn ein Schüler ausgewählt ist, wird dieser zurückgegeben, falls keine Auswahl vorliegt, wird der erste Eintrag aus der gefilterten Schülerliste zurückgegeben
		return schuelerManager.liste.get(idSchueler) !== null ? schuelerManager.liste.get(idSchueler) : schuelerManager.filtered().get(0);
	}

	/**
	 * Lädt das {@link SchuelerStammdaten} Objekt zum übergebenen {@link SchuelerListeEintrag}.
	 * Die Methode liefert <code>null</code>, wenn der Parameter <code>schuelerEintrag</code> <code>null</code> ist.
	 *
	 * @param {SchuelerListeEintrag | null} schuelerEintrag     Eintrag des Schülers
	 *
	 * @returns Gibt Stammdaten des Schülers oder <code>null</code> zurück
	 */
	private async loadSchuelerStammdaten(schuelerEintrag: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
		if (schuelerEintrag === null)
			return null;

		return await api.server.getSchuelerStammdaten(api.schema, schuelerEintrag.id);
	}


	get schuelerListeManager(): SchuelerListeManager {
		if (this._state.value.schuelerListeManager === undefined)
			return new SchuelerListeManager(null, new SchuelerListe(), api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt); // Gib Dummy zurück
		return this._state.value.schuelerListeManager;
	}

	get activeRouteType(): RouteType {
		return this._state.value.activeRouteType;
	}

	patch = async (data : Partial<SchuelerStammdaten>) => {
		if (!this.schuelerListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idSchueler = this.schuelerListeManager.auswahl().id;
		const stammdaten = this.schuelerListeManager.daten();
		await api.server.patchSchuelerStammdaten(data, api.schema, idSchueler);
		Object.assign(stammdaten, data);
		this.schuelerListeManager.setDaten(stammdaten);
		this.commit();
	}

	public deleteSchueler = async (): Promise<[boolean, ArrayList<string | null>]> => {
		const ids = new ArrayList<number>();
		for (const schueler of this.schuelerListeManager.liste.auswahlSorted())
			ids.add(schueler.id);

		const operationResponses = await api.server.deleteSchueler(ids, api.schema);

		const schuelerToRemove = new ArrayList<SchuelerListeEintrag>();
		const logMessages = new ArrayList<string | null>();
		let status = true;
		for (const response of operationResponses) {
			if (response.success && response.id !== null) {
				const schueler = this.schuelerListeManager.liste.get(response.id);
				logMessages.add(`Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${response.id.toString()}) wurde erfolgreich gelöscht.`);
				schuelerToRemove.add(schueler);
			} else {
				status = false;
				logMessages.addAll(response.log);
			}
		}

		if (!schuelerToRemove.isEmpty()) {
			this.schuelerListeManager.liste.auswahlClear();
			this.schuelerListeManager.setDaten(null);
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [status, logMessages];
	}

	public deleteSchuelerCheck = (): [boolean, ArrayList<string>] => {
		return [true, new ArrayList()]; // aktuell gibt es keine Vorbedingungen um einen Schüler auf "gelöscht" zu setzen
	}

	public async setDaten(schueler: SchuelerListeEintrag | null) {
		const hatteDaten = this.schuelerListeManager.hasDaten();
		if ((schueler === null) && (!hatteDaten))
			return;

		if ((schueler === null) || (this.schuelerListeManager.liste.list().isEmpty())) {
			this.schuelerListeManager.setDaten(null);
			this.commit();
			return;
		}

		if (hatteDaten && (schueler.id === this.schuelerListeManager.auswahl().id))
			return;

		const schuelerEintrag = this.getEintragOrDefault(schueler.id);
		const schuelerStammdaten = await this.loadSchuelerStammdaten(schuelerEintrag);
		this.schuelerListeManager.setDaten(schuelerStammdaten);
		this.commit();
	}

	getEintragOrDefault = (idSchueler: number) => {
		if (this.schuelerListeManager.liste.has(idSchueler))
			return this.schuelerListeManager.liste.get(idSchueler);
		else
			return this.schuelerListeManager.filtered().isEmpty() ? null : this.schuelerListeManager.filtered().get(0);
	}

	gotoDefaultRoute = async (idSchueler?: number | null) => {
		this._state.value.oldView = this._state.value.view;
		if ((idSchueler !== null) && (idSchueler !== undefined) && this.schuelerListeManager.liste.has(idSchueler)) {
			const route = (this.view !== routeSchuelerNeu && this.view !== routeSchuelerGruppenprozesse) ? this.view.getRoute(idSchueler) : routeSchuelerIndividualdaten.getRoute(idSchueler);
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE){
				const schuelerEintrag = this.getEintragOrDefault(idSchueler);
				await this.setDaten(schuelerEintrag);
			}

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.schuelerListeManager.filtered();
		if (!filtered.isEmpty()) {
			const schueler = filtered.getFirst();
			const route = routeSchuelerIndividualdaten.getRoute(schueler.id);
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setDaten(schueler);
		}
	}

	private setDefaults() {
		this._state.value.activeRouteType = RouteType.DEFAULT
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : routeSchuelerIndividualdaten;
		this.commit();
	}

	gotoGruppenprozessRoute = async (navigate: boolean) => {
		if ((this._state.value.activeRouteType === RouteType.GRUPPENPROZESSE) || (this._state.value.view === routeSchuelerGruppenprozesse)) {
			this.commit();
			return;
		}

		this._state.value.activeRouteType = RouteType.GRUPPENPROZESSE;
		// this._state.value.oldView = this._state.value.view;

		if (navigate)
			await RouteManager.doRoute(routeSchuelerGruppenprozesse.getRoute());

		this._state.value.view = routeSchuelerGruppenprozesse;
		this.schuelerListeManager.setDaten(null);
		this.commit();
	}

	gotoHinzufuegenRoute = async (navigate: boolean) => {
		if ((this._state.value.activeRouteType === RouteType.HINZUFUEGEN) || (this._state.value.view === routeSchuelerNeu)) {
			this.commit();
			return;
		}

		this._state.value.activeRouteType = RouteType.HINZUFUEGEN;
		// this._state.value.oldView = this._state.value.view;

		if (navigate) {
			const result = await RouteManager.doRoute(routeSchuelerNeu.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.schuelerListeManager.liste.auswahlClear();
		}

		this._state.value.view = routeSchuelerNeu;
		this.schuelerListeManager.setDaten(null);
		this.commit();
	}

	setFilter = async () => {
		if (!this.schuelerListeManager.hasDaten()) {
			const listFiltered = this.schuelerListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoDefaultRoute(listFiltered.get(0).id);
				return;
			}
		}
		this.setPatchedState({ schuelerListeManager: this.schuelerListeManager });
	}

}
