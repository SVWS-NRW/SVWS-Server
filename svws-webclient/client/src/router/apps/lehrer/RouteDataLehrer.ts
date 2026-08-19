import type { FachDaten, LehrerUnterrichtsfach, LehrerFachrichtungEintrag, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerListeEintrag,
	LehrerPersonalabschnittsdaten, LehrerPersonalabschnittsdatenAnrechnungsstunden, LehrerPersonaldaten, LehrerStammdaten, List, SchulEintrag,
	SimpleOperationResponse, StundenplanListeEintrag, Schulleitung } from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { LehrerListeManager, type PendingStateManager, ViewType } from "@ui";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeLehrerIndividualdatenGruppenprozesse } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdatenGruppenprozesse";
import { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerAllgemeinesGruppenprozesse } from "./allgemeines/RouteLehrerAllgemeinesGruppenprozesse";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { serverStateImpl } from "~/states/ServerStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

interface RouteStateLehrer extends RouteStateAuswahlInterface<LehrerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	mapSchulen: Map<string, SchulEintrag>;
	mapFaecher: Map<number, FachDaten>;
	lehrerUnterrichtsfaecher: List<LehrerUnterrichtsfach>;
	listLeitungsfunktionen: List<Schulleitung>;
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
	mapFaecher: new Map(),
	lehrerUnterrichtsfaecher: new ArrayList(),
	listLeitungsfunktionen: new ArrayList(),
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
		return configStateImpl.config.getValue("lehrer.auswahl.filterNurSichtbar") === 'true';
	}

	setFilterNurSichtbar = async (value: boolean) => {
		await configStateImpl.config.setValue('lehrer.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	get filterNurStatistikrelevant(): boolean {
		return configStateImpl.config.getValue("lehrer.auswahl.filterNurStatistikrelevant") === 'true';
	}

	setFilterNurStatistikrelevant = async (value: boolean) => {
		await configStateImpl.config.setValue('lehrer.auswahl.filterNurStatistikrelevant', value ? "true" : "false");
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
		const manager = new LehrerListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, listLehrer);

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
		if (!serverStateImpl.hasDev) {
			this._defaultState = { ...defaultState, gruppenprozesseView: routeLehrerAllgemeinesGruppenprozesse };
		}

		return { manager };
	}

	public async ladeDaten(auswahl: LehrerListeEintrag | null): Promise<LehrerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}

		if (this.manager.hasPersonalDaten()) {
			this._state.value.lehrerUnterrichtsfaecher = await api.server.getLehrerUnterrichtsfaecher(api.schema, auswahl.id);
		}

		const [stammdaten, listLeitungsfunktionen] = await Promise.all([
			api.server.getLehrerStammdaten(api.schema, auswahl.id),
			api.server.getAllSchulleitungenByLehrer(api.schema, auswahl.id),
		]);

		this._state.value.listLeitungsfunktionen = listLeitungsfunktionen;

		return stammdaten;
	}

	protected async updateManager(manager: LehrerListeManager, managerAlt: LehrerListeManager, daten: LehrerStammdaten) {
		if (managerAlt.hasPersonalDaten()) {
			manager.setPersonalDaten(await api.server.getLehrerPersonaldaten(api.schema, daten.id));
		}
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene) {
			mapStundenplaene.set(l.id, l);
		}
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
		if (auswahlList.isEmpty()) {
			return null;
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList) {
			ids.add(eintrag.id);
		}
		return await api.server.getLehrerStammdatenMultiple(ids, api.schema);
	}

	public async loadPersonaldaten() {
		if (!this.manager.hasDaten()) {
			return;
		}
		const listSchulen = await api.server.getSchulenMitKuerzel(api.schema);
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const s of listSchulen) {
			if (s.schulnummerStatistik !== null) {
				mapSchulen.set(s.schulnummerStatistik, s);
			}
		}
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id);
		if (personaldaten.abschnittsdaten.isEmpty()) {
			const result = await this.createPersonalabschnittsdaten(personaldaten.id);
			personaldaten.abschnittsdaten.add(result);
		}
		this.manager.setPersonalDaten(personaldaten);
		const faecher = await api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FachDaten>();
		for (const f of faecher) {
			mapFaecher.set(f.id, f);
		}
		this.setPatchedState({ mapSchulen, mapFaecher });
	}

	public async unloadPersonaldaten() {
		this.manager.setPersonalDaten(null);
		this._state.value.lehrerUnterrichtsfaecher = new ArrayList();
		this.commit();
	}

	protected async doPatch(data: Partial<LehrerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchLehrerStammdaten(data, api.schema, id);
		return true;
	}

	get getListLeitungsfunktionen(): List<Schulleitung> {
		const list = new ArrayList<Schulleitung>();
		list.addAll(this._state.value.listLeitungsfunktionen);
		return list;
	}

	addLeitungsfunktion = async (data: Partial<Schulleitung>, idLehrer: number): Promise<void> => {
		const eintrag = await api.server.addSchulleitung({ ...data, idLehrer }, api.schema);
		const listLeitungsfunktionen = this.getListLeitungsfunktionen;
		listLeitungsfunktionen.add(eintrag);
		this.setPatchedState({ listLeitungsfunktionen });
	};

	patchLeitungsfunktion = async (data: Partial<Schulleitung>, idEintrag: number): Promise<void> => {
		await api.server.patchSchulleitung(data, api.schema, idEintrag);
		const listLeitungsfunktionen = this.getListLeitungsfunktionen;
		for (const l of listLeitungsfunktionen) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ listLeitungsfunktionen });
	};

	deleteLeitungsfunktionen = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchulleitungen(idsEintraege, api.schema);
		const listLeitungsfunktionen = this.getListLeitungsfunktionen;
		for (const id of idsEintraege) {
			for (let i = 0; i < listLeitungsfunktionen.size(); i++) {
				if (listLeitungsfunktionen.get(i).id === id) {
					listLeitungsfunktionen.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listLeitungsfunktionen });
	};

	patchPersonaldaten = async (data: Partial<LehrerPersonaldaten>): Promise<boolean> => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		const personaldaten = this.manager.personalDaten();
		await api.server.patchLehrerPersonaldaten(data, api.schema, personaldaten.id);
		Object.assign(personaldaten, data);
		this.manager.setPersonalDaten(personaldaten);
		this.commit();
		return true;
	};

	patchPersonalAbschnittsdaten = async (data: Partial<LehrerPersonalabschnittsdaten>, id: number) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		const abschnittsdaten = this.manager.getAbschnittById(id);
		if (abschnittsdaten === null) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		}
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnittsdaten.id);
		Object.assign(abschnittsdaten, data);
		this.commit();
	};

	private getAbschnitt(id: number): LehrerPersonalabschnittsdaten {
		const abschnitt = this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt);
		if (abschnitt === null) {
			throw new DeveloperNotificationException("Es konnten keine gültigen Lehrerabschnittsdaten mit der ID " + id.toString() + " geladen werden.");
		}
		return abschnitt;
	};

	addMehrleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenMehrleistung(data, api.schema);
		abschnitt.mehrleistung.add(result);
		this.commit();
	};

	patchMehrleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => {
		await api.server.patchLehrerPersonalabschnittsdatenMehrleistung(data, api.schema, id);
		this.commit();
	};

	removeMehrleistung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenMehrleistung(api.schema, data.id);
		abschnitt.mehrleistung.remove(data);
		this.commit();
	};

	addMinderleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenMinderleistung(data, api.schema);
		abschnitt.minderleistung.add(result);
		this.commit();
	};

	patchMinderleistung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number) => {
		await api.server.patchLehrerPersonalabschnittsdatenMinderleistung(data, api.schema, id);
		this.commit();
	};

	removeMinderleistung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenMinderleistung(api.schema, data.id);
		abschnitt.minderleistung.remove(data);
		this.commit();
	};

	addAnrechnung = async (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data, api.schema);
		abschnitt.anrechnungen.add(result);
		this.commit();
	};

	patchAnrechnungen = async (data: List<Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>>) => {
		await api.server.patchLehrerPersonalabschnittsdatenAllgemeineAnrechnungen(data, api.schema);
		this.commit();
	};

	removeAnrechnung = async (data: LehrerPersonalabschnittsdatenAnrechnungsstunden) => {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(api.schema, data.id);
		abschnitt.anrechnungen.remove(data);
		this.commit();
	};

	addLehramt = async (eintrag: Partial<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const result = await api.server.addLehrerLehramt(eintrag, api.schema);
		this.manager.personalDaten().lehraemter.add(result);
		this.commit();
	};

	removeLehraemter = async (eintraege: List<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege) {
			await api.server.deleteLehrerLehramt(api.schema, eintrag.id);
		}
		this.manager.personalDaten().lehraemter.removeAll(eintraege);
		this.commit();
	};

	patchLehramt = async (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehramt(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	addLehrbefaehigung = async (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerLehrbefaehigung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.lehrbefaehigungen.add(result);
			}
		}
		this.commit();
	};

	removeLehrbefaehigungen = async (eintraege: List<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege) {
			await api.server.deleteLehrerLehrbefaehigung(api.schema, eintrag.id);
		}
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			lehramt.lehrbefaehigungen.removeAll(eintraege);
		}
		this.commit();
	};

	patchLehrbefaehigung = async (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehrbefaehigung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	addFachrichtung = async (eintrag: Partial<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Fachrichtungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerFachrichtung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.fachrichtungen.add(result);
			}
		}
		this.commit();
	};

	removeFachrichtungen = async (eintraege: List<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerFachrichtungen(ids, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			lehramt.fachrichtungen.removeAll(eintraege);
		}
		this.commit();
	};

	patchFachrichtung = async (eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerFachrichtung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	get mapFaecher(): Map<number, FachDaten> {
		return this._state.value.mapFaecher;
	}

	get lehrerUnterrichtsfaecher(): List<LehrerUnterrichtsfach> {
		return this._state.value.lehrerUnterrichtsfaecher;
	}

	addLehrerUnterrichtsfach = async (eintrag: Partial<LehrerUnterrichtsfach>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur hinzugefügt werden, wenn gültige Personaldaten geladen sind.");
		}
		const result = await api.server.addLehrerUnterrichtsfach(eintrag, api.schema);
		this._state.value.lehrerUnterrichtsfaecher.add(result);
		this.commit();
	};

	patchLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerUnterrichtsfach(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	removeLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach) => {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur entfernt werden, wenn gültige Personaldaten geladen sind.");
		}
		await api.server.deleteLehrerUnterrichtsfach(api.schema, eintrag.id);
		this._state.value.lehrerUnterrichtsfaecher.remove(eintrag);
		this.commit();
	};


	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLehrer(ids, api.schema);
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const list = new ArrayList<number>();
		for (const id of ids) {
			if (!this.manager.getIdsReferenzierterLehrer().contains(id)) {
				list.add(id);
			}
		}
		return list;
	}

	protected deleteMessage(id: number, lehrer: LehrerListeEintrag | null): string {
		return `Lehrer ${(lehrer?.vorname ?? '???') + ' ' + (lehrer?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Lehrern vor.');
		}

		for (const id of this.manager.getIdsReferenzierterLehrer()) {
			const lehrer = this.manager.liste.get(id);
			if (lehrer) {
				errorLog.add(`Die Lehrkraft ${lehrer.vorname} ${lehrer.nachname} ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
			}
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	add = async (data: Partial<LehrerStammdaten>): Promise<void> => {
		const lehrerStammdaten = await api.server.addLehrerStammdaten(data, api.schema);
		await this.createPersonalabschnittsdaten(lehrerStammdaten.id);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(lehrerStammdaten.id);
	};

	private async createPersonalabschnittsdaten(idLehrer: number): Promise<LehrerPersonalabschnittsdaten> {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		return await api.server.createLehrerPersonalabschnittsdaten({ idLehrer, idSchuljahresabschnitt }, api.schema);
	}

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

