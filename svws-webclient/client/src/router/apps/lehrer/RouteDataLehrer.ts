import type { ApiFile, LehrerFachrichtungAnerkennung, LehrerFachrichtungEintrag, LehrerLehramtAnerkennung, LehrerLehramtEintrag, LehrerLehrbefaehigungAnerkennung, LehrerLehrbefaehigungEintrag, LehrerListeEintrag, LehrerPersonalabschnittsdaten, LehrerPersonaldaten, LehrerStammdaten, List ,SimpleOperationResponse, StundenplanListeEintrag, ReportingParameter} from "@core";
import { ArrayList, DeveloperNotificationException, LehrerListeManager, BenutzerKompetenz, ReportingReportvorlage } from "@core";

import { api } from "~/router/Api";

import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { ViewType } from "@ui";
import { routeLehrerGruppenprozesse } from "~/router/apps/lehrer/RouteLehrerGruppenprozesse";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";

interface RouteStateLehrer extends RouteStateAuswahlInterface<LehrerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
}

const defaultState = <RouteStateLehrer>{
	idSchuljahresabschnitt: -1,
	manager: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	activeViewType: ViewType.DEFAULT,
	view: routeLehrerIndividualdaten,
	mapStundenplaene: new Map(),
};

export class RouteDataLehrer extends RouteDataAuswahl<LehrerListeManager, RouteStateLehrer> {

	public constructor() {
		super(defaultState, routeLehrerGruppenprozesse, routeLehrerNeu);
	}

	get filterNurSichtbar(): boolean {
		return api.config.getValue("lehrer.auswahl.filterNurSichtbar") === 'true';
	}

	setFilterNurSichtbar = async (value: boolean) => {
		await api.config.setValue('lehrer.auswahl.filterNurSichtbar', value ? "true" : "false");
	}

	get filterNurStatistikrelevant(): boolean {
		return api.config.getValue("lehrer.auswahl.filterNurStatistikrelevant") === 'true';
	}

	setFilterNurStatistikrelevant = async (value: boolean) => {
		await api.config.setValue('lehrer.auswahl.filterNurStatistikrelevant', value ? "true" : "false");
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateLehrer>> {
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
		return { manager };
	}

	public async ladeDaten(auswahl: LehrerListeEintrag | null) : Promise<LehrerStammdaten | null> {
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


	public async loadPersonaldaten() {
		if (!this.manager.hasDaten())
			return;
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id)
		this.manager.setPersonalDaten(personaldaten);
		this.commit();
	}

	public async unloadPersonaldaten() {
		this.manager.setPersonalDaten(null);
		this.commit();
	}

	protected async doPatch(data : Partial<LehrerStammdaten>, id: number) : Promise<void> {
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
	}

	patchPersonalAbschnittsdaten = async (data: Partial<LehrerPersonalabschnittsdaten>, id: number) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const abschnittsdaten = this.manager.getAbschnittById(id);
		if (abschnittsdaten === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnittsdaten.id);
		Object.assign(abschnittsdaten, data);
		this.commit();
	}

	addLehramt = async (eintrag: LehrerLehramtEintrag) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Lehrämtern noch nicht implementiert");
		this.manager.personalDaten().lehraemter.add(eintrag);
		this.commit();
	}

	removeLehraemter = async (eintraege: List<LehrerLehramtEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Lehrämtern noch nicht implementiert");
		this.manager.personalDaten().lehraemter.removeAll(eintraege);
		this.commit();
	}

	patchLehramtAnerkennung = async (eintrag: LehrerLehramtEintrag, anerkennung: LehrerLehramtAnerkennung | null) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrämtern noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.manager.getSchuljahr())?.id ?? null });
		this.commit();
	}

	addLehrbefaehigung = async (eintrag: LehrerLehrbefaehigungEintrag) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Lehrbefähigungen noch nicht implementiert");
		this.manager.personalDaten().lehrbefaehigungen.add(eintrag);
		this.commit();
	}

	removeLehrbefaehigungen = async (eintraege: List<LehrerLehrbefaehigungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Lehrbefähigungen noch nicht implementiert");
		this.manager.personalDaten().lehrbefaehigungen.removeAll(eintraege);
		this.commit();
	}

	patchLehrbefaehigungAnerkennung = async (eintrag: LehrerLehrbefaehigungEintrag, anerkennung: LehrerLehrbefaehigungAnerkennung | null) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrbefähigungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.manager.getSchuljahr())?.id ?? null });
		this.commit();
	}

	addFachrichtung = async (eintrag: LehrerFachrichtungEintrag) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Fachrichtungen noch nicht implementiert");
		this.manager.personalDaten().fachrichtungen.add(eintrag);
		this.commit();
	}

	removeFachrichtungen = async (eintraege: List<LehrerFachrichtungEintrag>) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Fachrichtungen noch nicht implementiert");
		this.manager.personalDaten().fachrichtungen.removeAll(eintraege);
		this.commit();
	}

	patchFachrichtungAnerkennung = async (eintrag: LehrerFachrichtungEintrag, anerkennung: LehrerFachrichtungAnerkennung | null) => {
		if (!this.manager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Fachrichtungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.manager.getSchuljahr())?.id ?? null });
		this.commit();
	}


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

	protected deleteMessage(id: number, lehrer: LehrerListeEintrag | null) : string {
		return `Lehrer ${(lehrer?.vorname ?? '???') + ' ' + (lehrer?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	deleteLehrerCheck = (): [boolean, List<string>] => {
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

		return [errorLog.isEmpty(), errorLog];
	}

	add = async (data: Partial<LehrerStammdaten>): Promise<void> => {
		const lehrerStammdaten = await api.server.addLehrerStammdaten(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(lehrerStammdaten.id);
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Lehrer ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl())
			reportingParameter.idsDetaildaten.add(l.id);
		return await api.server.pdfReport(reportingParameter, api.schema);
	})
}

