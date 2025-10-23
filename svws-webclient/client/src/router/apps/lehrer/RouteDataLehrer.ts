import type {
	ApiFile, LehrerFachrichtungEintrag, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerListeEintrag, LehrerPersonalabschnittsdatenAnrechnungsstunden,
	LehrerPersonaldaten, LehrerPersonalabschnittsdaten, LehrerStammdaten, List, SimpleOperationResponse, StundenplanListeEintrag,
	ReportingParameter, SchulEintrag } from "@core";
import { ArrayList, UserNotificationException, DeveloperNotificationException, BenutzerKompetenz, ServerMode } from "@core";
import { api } from "~/router/Api";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { type PendingStateManager, ViewType, LehrerListeManager } from "@ui";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeLehrerIndividualdatenGruppenprozesse } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdatenGruppenprozesse";
import { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerAllgemeinesGruppenprozesse } from "./allgemeines/RouteLehrerAllgemeinesGruppenprozesse";

interface RouteStateLehrer extends RouteStateAuswahlInterface<LehrerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	mapSchulen: Map<string, SchulEintrag>;
	pendingStateManager: PendingStateManagerLehrerIndividualdaten | undefined;
}

const defaultState = <RouteStateLehrer>{
	idSchuljahresabschnitt: -1,
	manager: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	activeViewType: ViewType.DEFAULT,
	view: routeLehrerIndividualdaten,
	gruppenprozesseView: routeLehrerIndividualdatenGruppenprozesse,
	mapStundenplaene: new Map(),
	mapSchulen: new Map(),
	pendingStateManager: undefined,
};

export class RouteDataLehrer extends RouteDataAuswahl<LehrerListeManager, RouteStateLehrer> {

	public constructor() {
		super(defaultState, { hinzufuegen: routeLehrerNeu });
	}

	get pendingStateManager(): PendingStateManagerLehrerIndividualdaten {
		if (this._state.value.pendingStateManager === undefined) {
			this._state.value.pendingStateManager = new PendingStateManagerLehrerIndividualdaten('id', () => routeLehrer.data.manager);
			routeLehrer.data.pendingStateManagerRegistry.addPendingStateManager(this._state.value.pendingStateManager);
		}
		return this._state.value.pendingStateManager;
	}

	get filterNurSichtbar(): boolean {
		return api.config.getValue("lehrer.auswahl.filterNurSichtbar") === 'true';
	}

	setFilterNurSichtbar = async (value: boolean) => {
		await api.config.setValue('lehrer.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	get filterNurStatistikrelevant(): boolean {
		return api.config.getValue("lehrer.auswahl.filterNurStatistikrelevant") === 'true';
	}

	setFilterNurStatistikrelevant = async (value: boolean) => {
		await api.config.setValue('lehrer.auswahl.filterNurStatistikrelevant', value ? "true" : "false");
	};

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.mapSchulen;
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateLehrer>> {
		// Lade die Daten von der API
		const listLehrer = await api.server.getLehrer(api.schema);

		// Erstelle den Lehrer-Liste-Manager
		const manager = new LehrerListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listLehrer);

		// Übernehme den Filter von dem vorigen Manager oder initialisiere ihn neu, falls kein voriger Manager vorhanden ist
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(this.filterNurSichtbar);
			manager.setFilterNurStatistikRelevant(this.filterNurStatistikrelevant);
		} else {
			manager.useFilter(this._state.value.manager);
		}

		// Hinweis: Dieses Nachträgliche Verändern des DefaultStates wurde gemacht, weil zum Zeitpunkt der Klassen initialisierung der ServerMode noch nicht
		// abgerufen wurde und somit die Bedingung, welche Route als Default für Gruppenprozesse genutzt werden soll, nicht geprüft werden kann
		// Diese Stelle eignet sich als Alternative, da sie noch vor dem ersten Betreten der Route aber bereits nach dem Abruf der ServerModes liegt
		// TODO: Ausbauen sobald die Route routeSchuelerIndividualdatenGruppenprozesse im "Stable" Mode bereitsteht
		if (api.mode !== ServerMode.DEV)
			this._defaultState = { ...defaultState, gruppenprozesseView: routeLehrerAllgemeinesGruppenprozesse };

		return { manager };
	}

	public async ladeDaten(auswahl: LehrerListeEintrag | null): Promise<LehrerStammdaten | null> {
		return (auswahl === null) ? null : await api.server.getLehrerStammdaten(api.schema, auswahl.id);
	}

	protected async updateManager(manager: LehrerListeManager, managerAlt: LehrerListeManager, daten: LehrerStammdaten) {
		if (managerAlt.hasPersonalDaten())
			manager.setPersonalDaten(await api.server.getLehrerPersonaldaten(api.schema, daten.id));
		await this.updateMapStundenplaene();
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedState({ mapStundenplaene });
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	/**
	 * Aktualisiere beim Manager bei den neuen Daten ggf. auch die Personaldaten
	 *
	 * @param daten   die neuen Daten des Lehrers
	 */
	protected async updateDaten(daten: LehrerStammdaten | null) {
		const hattePersonaldaten = this.manager.hasPersonalDaten();
		this.manager.setDaten(daten);
		const personaldaten = (this.manager.hasDaten() && hattePersonaldaten)
			? await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id)
			: null;
		this.manager.setPersonalDaten(personaldaten);
	}

	public async ladeDatenMultiple(auswahlList: List<LehrerListeEintrag>, state: Partial<RouteStateLehrer>): Promise<List<LehrerStammdaten> | null> {
		if (auswahlList.isEmpty())
			return null;
		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList)
			ids.add(eintrag.id);
		return await api.server.getLehrerStammdatenMultiple(ids, api.schema);
	}

	public async loadPersonaldaten() {
		if (!this.manager.hasDaten())
			return;
		const listSchulen = await api.server.getSchulenMitKuerzel(api.schema);
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const s of listSchulen)
			if (s.schulnummerStatistik !== null)
				mapSchulen.set(s.schulnummerStatistik, s);
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id);
		this.manager.setPersonalDaten(personaldaten);
		this.setPatchedState({ mapSchulen });
	}

	public async unloadPersonaldaten() {
		this.manager.setPersonalDaten(null);
		this.commit();
	}

	protected async doPatch(data: Partial<LehrerStammdaten>, id: number): Promise<void> {
		await api.server.patchLehrerStammdaten(data, api.schema, id);
	}

	patchPersonaldaten = async (data: Partial<LehrerPersonaldaten>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const personaldaten = this.manager.personalDaten();
		await api.server.patchLehrerPersonaldaten(data, api.schema, personaldaten.id);
		Object.assign(personaldaten, data);
		this.manager.setPersonalDaten(personaldaten);
		this.commit();
	};

	patchPersonalAbschnittsdaten = async (data: Partial<LehrerPersonalabschnittsdaten>, id: number) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const abschnittsdaten = this.manager.getAbschnittById(id);
		if (abschnittsdaten === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnittsdaten.id);
		Object.assign(abschnittsdaten, data);
		this.commit();
	};
	addMehrleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const result = await api.server.addLehrerPersonalabschnittsdatenMehrleistung(data, api.schema);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.mehrleistung.add(result);
		this.commit();
	};

	patchMehrleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => {
		await api.server.patchLehrerPersonalabschnittsdatenMehrleistung(data, api.schema, id);
		this.commit();
	};

	removeMehrleistung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		await api.server.deleteLehrerPersonalabschnittsdatenMehrleistung(api.schema, data.id);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.mehrleistung.remove(data);
		this.commit();
	};

	addMinderleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const result = await api.server.addLehrerPersonalabschnittsdatenMinderleistung(data, api.schema);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.minderleistung.add(result);
		this.commit();
	};

	patchMinderleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => {
		await api.server.patchLehrerPersonalabschnittsdatenMinderleistung(data, api.schema, id);
		this.commit();
	};

	removeMinderleistung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		await api.server.deleteLehrerPersonalabschnittsdatenMinderleistung(api.schema, data.id);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.minderleistung.remove(data);
		this.commit();
	};

	addAnrechnung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const result = await api.server.addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data, api.schema);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.anrechnungen.add(result);
		this.commit();
	};

	patchAnrechnung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => {
		await api.server.patchLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data, api.schema, id);
		this.commit();
	};

	removeAnrechnung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		await api.server.deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(api.schema, data.id);
		this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt)?.anrechnungen.remove(data);
		this.commit();
	};

	addLehramt = async (eintrag: Partial<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		const result = await api.server.addLehrerLehramt(eintrag, api.schema);
		this.manager.personalDaten().lehraemter.add(result);
		this.commit();
	};

	removeLehraemter = async (eintraege: List<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege)
			await api.server.deleteLehrerLehramt(api.schema, eintrag.id);
		this.manager.personalDaten().lehraemter.removeAll(eintraege);
		this.commit();
	};

	patchLehramt = async (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchLehrerLehramt(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	addLehrbefaehigung = async (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		if (eintrag.idLehramt === undefined)
			throw new DeveloperNotificationException("Lehrbefähigungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		const result = await api.server.addLehrerLehrbefaehigung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter)
			if (lehramt.id === eintrag.idLehramt)
				lehramt.lehrbefaehigungen.add(result);
		this.commit();
	};

	removeLehrbefaehigungen = async (eintraege: List<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege)
			await api.server.deleteLehrerLehrbefaehigung(api.schema, eintrag.id);
		for (const lehramt of this.manager.personalDaten().lehraemter)
			lehramt.lehrbefaehigungen.removeAll(eintraege);
		this.commit();
	};

	patchLehrbefaehigung = async (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchLehrerLehrbefaehigung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	addFachrichtung = async (eintrag: Partial<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		if (eintrag.idLehramt === undefined)
			throw new DeveloperNotificationException("Fachrichtungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		const result = await api.server.addLehrerFachrichtung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter)
			if (lehramt.id === eintrag.idLehramt)
				lehramt.fachrichtungen.add(result);
		this.commit();
	};

	removeFachrichtungen = async (eintraege: List<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege)
			await api.server.deleteLehrerFachrichtung(api.schema, eintrag.id);
		for (const lehramt of this.manager.personalDaten().lehraemter)
			lehramt.fachrichtungen.removeAll(eintraege);
		this.commit();
	};

	patchFachrichtung = async (eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchLehrerFachrichtung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};


	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLehrer(ids, api.schema);
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const list = new ArrayList<number>();
		for (const id of ids)
			if (!this.manager.getIdsReferenzierterLehrer().contains(id))
				list.add(id);
		return list;
	}

	protected deleteMessage(id: number, lehrer: LehrerListeEintrag | null): string {
		return `Lehrer ${(lehrer?.vorname ?? '???') + ' ' + (lehrer?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	deleteLehrerCheck = (): { success: boolean, logs: List<string> } => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Lehrern vor.');

		if (!this.manager.liste.auswahlExists())
			errorLog.add('Es wurde kein Lehrer zum Löschen ausgewählt.');

		for (const id of this.manager.getIdsReferenzierterLehrer()) {
			const lehrer = this.manager.liste.get(id);
			if (lehrer)
				errorLog.add(`Die Lehrkraft ${lehrer.vorname} ${lehrer.nachname} ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	add = async (data: Partial<LehrerStammdaten>): Promise<void> => {
		const lehrerStammdaten = await api.server.addLehrerStammdaten(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(lehrerStammdaten.id);
	};

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Lehrer ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl())
			reportingParameter.idsDetaildaten.add(l.id);
		return await api.server.pdfReport(reportingParameter, api.schema);
	});

	sendEMail = api.call(async (reportingParameter: ReportingParameter): Promise<SimpleOperationResponse> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
			return await api.server.emailReport(reportingParameter, api.schema);
		}
		throw new UserNotificationException("Dieser Report kann nur versendet werden, wenn mindestens ein Lehrer ausgewählt ist.");
	});

	patchMultiple = async (pendingStateManager: PendingStateManager<any>): Promise<void> => {
		api.status.start();

		const partialsToPatch = pendingStateManager.partials;
		// TODO einbauen
		// await api.server.patchLehrerStammdatenMultiple(partialsToPatch, api.schema);

		// Übernehme nur geänderte LehrerStammdaten Objekte in den AuswahlManager, damit nicht alle Stammdaten neugeladen werden müssen
		for (const partialToPatch of partialsToPatch) {
			if (partialToPatch.id !== undefined) {
				const patchId = (partialToPatch as Record<string, any>)[pendingStateManager.idFieldName];
				const currentStammdaten = this._state.value.manager?.getListeDaten().get(patchId);
				this._state.value.manager?.getListeDaten().put(patchId, Object.assign(Object.assign({}, currentStammdaten), partialToPatch));
			}
		}

		pendingStateManager.resetPendingState();
		this.commit();
		api.status.stop();
	};
}

