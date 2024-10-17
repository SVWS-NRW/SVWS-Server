import type { List, SchuelerListeEintrag, SchuelerStammdaten } from "@core";
import { BenutzerKompetenz, ArrayList, DeveloperNotificationException, SchuelerListe, SchuelerListeManager, SchuelerStatus } from "@core";

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
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	schuelerListeManager: undefined,
	view: routeSchuelerIndividualdaten,
	activeRouteType: RouteType.DEFAULT,
};

export class RouteDataSchueler extends RouteData<RouteStateSchueler> {

	public constructor() {
		super(defaultState);
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number, isEntering: boolean) : Promise<number | null> {
		if (!isEntering && idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return null;
		return await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Setzt die Daten zum ausgewählten Schuljahresabschnitt und Schülers und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		// Erzeuge neuen SchuelerListeManager, wenn die Route neu betreten wird oder der Schuljahresabschnitt geändert wird
		const schuelerListe: SchuelerListe = await this.loadSchuelerListe(idSchuljahresabschnitt);

		const manager = new SchuelerListeManager(api.schulform, schuelerListe, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
		if (this._state.value.schuelerListeManager === undefined) {
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		} else {
			manager.useFilter(this._state.value.schuelerListeManager);
		}

		// Funktioniert aktuell nicht, zudem muss überlegt werden anhand welcher eindeutigen Kriterien der Schüler im neuen Abschnitt
		//  zugeordnet/gefunden werden kann ggf. über Geburtsdatum + Name
		//  (Und wenn es dann tatsächlich immer noch > 1 zutreffenden Schüler geben sollte wird keiner selektiert)?

		// Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist und dieser im neuen Manager vorhanden ist
		// const vorherigeAuswahl = ((this._state.value.schuelerListeManager !== undefined) && this.schuelerListeManager.hasDaten()) ? this.schuelerListeManager.auswahl() : null;
		// if (vorherigeAuswahl !== null) {
		// 	const auswahl = this.schuelerListeManager.liste.get(vorherigeAuswahl.id);
		// 	let schuelerStammdaten = await this.loadSchuelerStammdaten(auswahl);
		// 	if (schuelerStammdaten === null)
		// 		schuelerStammdaten = await this.loadSchuelerStammdaten(manager.liste.list().get(0));
		//
		// 	this.schuelerListeManager.setDaten(schuelerStammdaten);
		// }

		// stellt die ursprünglich gefilterte Liste wieder her
		manager.filtered();

		const view = this.getCurrentViewOrDefault(manager);

		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			schuelerListeManager: manager,
			activeRouteType: this.activeRouteType,
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
		if (schuelerManager.hasDaten())
			return this._state.value.view;
		else
			return routeSchuelerIndividualdaten;
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
		for (const response of operationResponses)
			if (response.success && response.id !== null) {
				const schueler = this.schuelerListeManager.liste.get(response.id);
				logMessages.add(`Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${response.id.toString()}) wurde erfolgreich gelöscht.`);
				schuelerToRemove.add(schueler);
			} else {
				status = false;
				logMessages.addAll(response.log);
			}
		if (!schuelerToRemove.isEmpty()) {
			this.schuelerListeManager.liste.auswahlClear();
			this.schuelerListeManager.setDaten(null);
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [status, logMessages];
	}

	public deleteSchuelerCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');

		if (!this.schuelerListeManager.liste.auswahlExists())
			errorLog.add('Es wurde kein Schüler zum Löschen ausgewählt.');

		return [errorLog.isEmpty(), errorLog];
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
		if (!this.schuelerListeManager.hasDaten() && (this.activeRouteType === RouteType.DEFAULT)) {
			const listFiltered = this.schuelerListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				return await this.gotoDefaultRoute(listFiltered.get(0).id);
			}
		}
		this.commit();
	}

}
