import type { LehrerFachrichtungAnerkennung, LehrerFachrichtungEintrag, LehrerLehramtAnerkennung, LehrerLehramtEintrag, LehrerLehrbefaehigungAnerkennung, LehrerLehrbefaehigungEintrag, LehrerListeEintrag, LehrerPersonalabschnittsdaten, LehrerPersonaldaten, LehrerStammdaten, List } from "@core";
import { ArrayList, DeveloperNotificationException, LehrerListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { ViewType } from "@ui";
import { RoutingStatus } from "~/router/RoutingStatus";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplan";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { routeLehrerStundenplanDaten } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplanDaten";
import { routeLehrerGruppenprozesse } from "~/router/apps/lehrer/RouteLehrerGruppenprozesse";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";

interface RouteStateLehrer extends RouteStateInterface {
	// Daten, allgemein
	idSchuljahresabschnitt: number,
	lehrerListeManager: LehrerListeManager,
	activeViewType: ViewType,
	// TODO Unterrichtsdaten
}

const defaultState = <RouteStateLehrer>{
	idSchuljahresabschnitt: -1,
	lehrerListeManager: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	activeViewType: ViewType.DEFAULT,
	view: routeLehrerIndividualdaten,
};

export class RouteDataLehrer extends RouteData<RouteStateLehrer> {

	public constructor() {
		super(defaultState);
	}

	get lehrerListeManager(): LehrerListeManager {
		return this._state.value.lehrerListeManager;
	}

	get activeViewType() {
		return this._state.value.activeViewType;
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		// TODO Lade die Lehrerliste in Abhängigkeit von dem angegebenen Schuljahresabschnitt, sobald die API-Methode dafür existiert
		const listLehrer = await api.server.getLehrer(api.schema);
		const lehrerListeManager = new LehrerListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listLehrer);

		if (this._state.value.lehrerListeManager === undefined) {
			lehrerListeManager.setFilterAuswahlPermitted(true);
			lehrerListeManager.setFilterNurSichtbar(false);
		} else {
			// lehrerListeManager.useFilter(this._state.value.klassenListeManager);
		}

		// Versuche die ausgewählte Klasse von vorher zu laden
		const vorherigeAuswahlID = ((this._state.value.lehrerListeManager !== undefined) && this.lehrerListeManager.hasDaten()) ? this.lehrerListeManager.auswahlID() : null;
		if (vorherigeAuswahlID !== null) {
			lehrerListeManager.setDaten(await api.server.getLehrerStammdaten(api.schema, vorherigeAuswahlID));

			const hattePersonaldaten = this.lehrerListeManager.hasPersonalDaten();
			if (lehrerListeManager.hasDaten() && hattePersonaldaten)
				lehrerListeManager.setPersonalDaten(await api.server.getLehrerPersonaldaten(api.schema, vorherigeAuswahlID));
		}

		// stellt die ursprünglich gefilterte Liste wieder her
		lehrerListeManager.filtered();

		this.setPatchedDefaultState({ idSchuljahresabschnitt, lehrerListeManager, activeViewType: this.activeViewType, view: this._state.value.view });
		return this.lehrerListeManager.auswahlID();
	}


	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
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

	/**
	 * Setzt den ausgewählten Lehrer und lädt dessen Stammdaten.
	 *
	 * @param lehrer   der ausgewählte Lehrer
	 */
	public async setLehrer(lehrer: LehrerListeEintrag | null) {
		if ((lehrer === null) && (!this.lehrerListeManager.hasDaten()))
			return;
		if ((lehrer === null) || (this.lehrerListeManager.liste.list().isEmpty())) {
			this.lehrerListeManager.setDaten(null);
			this.commit();
			return;
		}
		if ((this.lehrerListeManager.hasDaten() && (lehrer.id === this.lehrerListeManager.auswahl().id)))
			return;
		const hattePersonaldaten = this.lehrerListeManager.hasPersonalDaten();
		let auswahl = this.lehrerListeManager.liste.get(lehrer.id);
		if (auswahl === null)
			auswahl = this.lehrerListeManager.filtered().isEmpty() ? null : this.lehrerListeManager.filtered().get(0);
		const daten = auswahl === null ? null : await api.server.getLehrerStammdaten(api.schema, auswahl.id);
		this.lehrerListeManager.setDaten(daten);
		const personaldaten = this.lehrerListeManager.hasDaten() && hattePersonaldaten
			? await api.server.getLehrerPersonaldaten(api.schema, this.lehrerListeManager.auswahl().id)
			: null;
		this.lehrerListeManager.setPersonalDaten(personaldaten);
		this.commit();
	}

	public async loadPersonaldaten() {
		if (!this.lehrerListeManager.hasDaten())
			return;
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, this.lehrerListeManager.auswahl().id)
		this.lehrerListeManager.setPersonalDaten(personaldaten);
		this.commit();
	}

	public async unloadPersonaldaten() {
		this.lehrerListeManager.setPersonalDaten(null);
		this.commit();
	}

	patchStammdaten = async (data: Partial<LehrerStammdaten>) => {
		if (!this.lehrerListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idLehrer = this.lehrerListeManager.auswahl().id;
		const daten = this.lehrerListeManager.daten();
		await api.server.patchLehrerStammdaten(data, api.schema, idLehrer);
		Object.assign(daten, data);
		this.lehrerListeManager.setDaten(daten);
		this.commit();
	}

	patchPersonaldaten = async (data: Partial<LehrerPersonaldaten>) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const personaldaten = this.lehrerListeManager.personalDaten();
		await api.server.patchLehrerPersonaldaten(data, api.schema, personaldaten.id);
		Object.assign(personaldaten, data);
		this.lehrerListeManager.setPersonalDaten(personaldaten);
		this.commit();
	}

	patchPersonalAbschnittsdaten = async (data: Partial<LehrerPersonalabschnittsdaten>, id: number) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const abschnittsdaten = this.lehrerListeManager.getAbschnittById(id);
		if (abschnittsdaten === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnittsdaten.id);
		Object.assign(abschnittsdaten, data);
		this.commit();
	}

	addLehramt = async (eintrag: LehrerLehramtEintrag) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Lehrämtern noch nicht implementiert");
		this.lehrerListeManager.personalDaten().lehraemter.add(eintrag);
		this.commit();
	}

	removeLehraemter = async (eintraege: List<LehrerLehramtEintrag>) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Lehrämtern noch nicht implementiert");
		this.lehrerListeManager.personalDaten().lehraemter.removeAll(eintraege);
		this.commit();
	}

	patchLehramtAnerkennung = async (eintrag: LehrerLehramtEintrag, anerkennung: LehrerLehramtAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrämtern noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.lehrerListeManager.getSchuljahr())?.id ?? null });
		this.commit();
	}

	addLehrbefaehigung = async (eintrag: LehrerLehrbefaehigungEintrag) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Lehrbefähigungen noch nicht implementiert");
		this.lehrerListeManager.personalDaten().lehrbefaehigungen.add(eintrag);
		this.commit();
	}

	removeLehrbefaehigungen = async (eintraege: List<LehrerLehrbefaehigungEintrag>) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Lehrbefähigungen noch nicht implementiert");
		this.lehrerListeManager.personalDaten().lehrbefaehigungen.removeAll(eintraege);
		this.commit();
	}

	patchLehrbefaehigungAnerkennung = async (eintrag: LehrerLehrbefaehigungEintrag, anerkennung: LehrerLehrbefaehigungAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrbefähigungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.lehrerListeManager.getSchuljahr())?.id ?? null });
		this.commit();
	}

	addFachrichtung = async (eintrag: LehrerFachrichtungEintrag) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Hinzufügen von Fachrichtungen noch nicht implementiert");
		this.lehrerListeManager.personalDaten().fachrichtungen.add(eintrag);
		this.commit();
	}

	removeFachrichtungen = async (eintraege: List<LehrerFachrichtungEintrag>) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		// TODO API-Aufruf ...
		console.log("Entfernen von Fachrichtungen noch nicht implementiert");
		this.lehrerListeManager.personalDaten().fachrichtungen.removeAll(eintraege);
		this.commit();
	}

	patchFachrichtungAnerkennung = async (eintrag: LehrerFachrichtungEintrag, anerkennung: LehrerFachrichtungAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Fachrichtungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten(this.lehrerListeManager.getSchuljahr())?.id ?? null });
		this.commit();
	}

	private setDefaults() {
		this._state.value.activeViewType = ViewType.DEFAULT;
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : routeLehrerIndividualdaten;
	}

	public async setEintrag(lehrer: LehrerListeEintrag | null) {
		if ((lehrer === null) && (!this.lehrerListeManager.hasDaten()))
			return;
		if ((lehrer === null) || (this.lehrerListeManager.liste.list().isEmpty())) {
			this.lehrerListeManager.setDaten(null);
			this.commit();
			return;
		}
		if (this.lehrerListeManager.hasDaten() && (lehrer.id === this.lehrerListeManager.auswahl().id))
			return;

		let daten = this.lehrerListeManager.liste.get(lehrer.id);
		if (daten === null)
			daten = this.lehrerListeManager.filtered().isEmpty() ? null : this.lehrerListeManager.filtered().get(0);

		if (daten !== null){
			const lehrerStammdaten = await api.server.getLehrerStammdaten(api.schema, daten.id)
			this.lehrerListeManager.setDaten(lehrerStammdaten);

			const personaldaten = this.lehrerListeManager.hasDaten() ? await api.server.getLehrerPersonaldaten(api.schema, this.lehrerListeManager.auswahl().id) : null;
			this.lehrerListeManager.setPersonalDaten(personaldaten);
		}

		this.commit();
	}

	gotoDefaultView = async (eintragId?: number | null) => {
		if ((eintragId !== null) && (eintragId !== undefined) && this.lehrerListeManager.liste.has(eintragId)) {
			const route = [routeLehrerPersonaldaten.name, routeLehrerStundenplan.name, routeLehrerUnterrichtsdaten.name, routeLehrerIndividualdaten.name, routeLehrerStundenplanDaten.name].includes(this.view.name)
				? this.view.getRoute(eintragId) : routeLehrerIndividualdaten.getRoute(eintragId)
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)
				await this.setEintrag(this.lehrerListeManager.liste.get(eintragId));

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.lehrerListeManager.filtered();
		if (!filtered.isEmpty()) {
			const klasse = this.lehrerListeManager.filtered().getFirst();
			const route = routeLehrerIndividualdaten.getRoute(klasse.id);
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setEintrag(klasse);
			this.commit();
		}
	}

	gotoGruppenprozessView = async (navigate: boolean) => {
		if (this._state.value.activeViewType === ViewType.GRUPPENPROZESSE || (this._state.value.view === routeLehrerGruppenprozesse)) {
			this.commit();
			return;
		}

		this._state.value.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate)
			await RouteManager.doRoute(routeLehrerGruppenprozesse.getRoute());

		this._state.value.view = routeLehrerGruppenprozesse;
		this.lehrerListeManager.setDaten(null);
		this.commit();
	}

	gotoHinzufuegenView = async (navigate: boolean) => {
		if (this._state.value.activeViewType === ViewType.HINZUFUEGEN || (this._state.value.view === routeLehrerNeu)) {
			this.commit();
			return;
		}

		this._state.value.activeViewType = ViewType.HINZUFUEGEN;

		if (navigate) {
			const result = await RouteManager.doRoute(routeLehrerNeu.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.lehrerListeManager.liste.auswahlClear();
		}

		this._state.value.view = routeLehrerNeu;
		this.lehrerListeManager.setDaten(null);
		this.commit();
	}

	deleteLehrer = async (): Promise<[boolean, List<string | null>]> => {
		return [false, new ArrayList()];
	}

	deleteLehrerCheck = (): [boolean, List<string>] => {
		return [false, new ArrayList()];
	}

	setFilter = async () => {
		if (!this.lehrerListeManager.hasDaten() && (this.activeViewType === ViewType.DEFAULT)) {
			const listFiltered = this.lehrerListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				return await this.gotoDefaultView(listFiltered.get(0).id);
			}
		}
		this.commit();
	}

}

