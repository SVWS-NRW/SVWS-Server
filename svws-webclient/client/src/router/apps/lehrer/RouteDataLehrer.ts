import type { LehrerFachrichtungAnerkennung, LehrerFachrichtungEintrag, LehrerLehramtAnerkennung, LehrerLehramtEintrag, LehrerLehrbefaehigungAnerkennung, LehrerLehrbefaehigungEintrag, LehrerListeEintrag, LehrerPersonalabschnittsdaten, LehrerPersonaldaten, LehrerStammdaten, List } from "@core";
import { ArrayList, DeveloperNotificationException, LehrerListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";

interface RouteStateLehrer extends RouteStateInterface {
	// Daten, allgemein
	idSchuljahresabschnitt: number,
	lehrerListeManager: LehrerListeManager;
	// TODO Unterrichtsdaten
}

const defaultState = <RouteStateLehrer> {
	idSchuljahresabschnitt: -1,
	lehrerListeManager: new LehrerListeManager(-1, null, new ArrayList()),
	view: routeLehrerIndividualdaten
};

export class RouteDataLehrer extends RouteData<RouteStateLehrer> {

	public constructor() {
		super(defaultState);
	}

	private async ladePersonaldaten(eintrag: LehrerListeEintrag | undefined): Promise<LehrerPersonaldaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await api.server.getLehrerPersonaldaten(api.schema, eintrag.id);
	}


	get lehrerListeManager(): LehrerListeManager {
		return this._state.value.lehrerListeManager;
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			 return;
		// TODO Lade die Lehrerliste in Abhängigkeit von dem angegebenen Schuljahresabschnitt, sobald die API-Methode dafür existiert
		const listLehrer = await api.server.getLehrer(api.schema);
		const lehrerListeManager = new LehrerListeManager(idSchuljahresabschnitt, api.schulform, listLehrer);
		this.setPatchedDefaultState({ idSchuljahresabschnitt, lehrerListeManager });
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
		if ((lehrer !== null) && (this.lehrerListeManager.hasDaten() && (lehrer.id === this.lehrerListeManager.auswahl().id)))
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

	patchStammdaten = async (data : Partial<LehrerStammdaten>) => {
		if (!this.lehrerListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idLehrer = this.lehrerListeManager.auswahl().id;
		const daten = this.lehrerListeManager.daten();
		if (daten === null)
			return;
		await api.server.patchLehrerStammdaten(data, api.schema, idLehrer);
		Object.assign(daten, data);
		this.lehrerListeManager.setDaten(daten);
		this.commit();
	}

	patchPersonaldaten = async (data : Partial<LehrerPersonaldaten>) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const personaldaten = this.lehrerListeManager.personalDaten();
		await api.server.patchLehrerPersonaldaten(data, api.schema, personaldaten.id);
		Object.assign(personaldaten, data);
		this.lehrerListeManager.setPersonalDaten(personaldaten);
		this.commit();
	}

	patchPersonalAbschnittsdaten = async (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const abschnittsdaten = this.lehrerListeManager.getAbschnittById(id);
		if (abschnittsdaten === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id + " geladen.");
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

	patchLehramtAnerkennung = async (eintrag: LehrerLehramtEintrag, anerkennung : LehrerLehramtAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrämtern noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten.id ?? null });
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

	patchLehrbefaehigungAnerkennung = async (eintrag: LehrerLehrbefaehigungEintrag, anerkennung : LehrerLehrbefaehigungAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Lehrbefähigungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten.id ?? null });
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

	patchFachrichtungAnerkennung = async (eintrag: LehrerFachrichtungEintrag, anerkennung : LehrerFachrichtungAnerkennung | null) => {
		if (!this.lehrerListeManager.hasPersonalDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO API-Aufruf mit idAnerkennungsgrund ...
		console.log("Anpassen von Fachrichtungen noch nicht implementiert");
		Object.assign(eintrag, { idAnerkennungsgrund: anerkennung?.daten.id ?? null });
		this.commit();
	}

	gotoEintrag = async (eintrag: LehrerListeEintrag) => {
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: eintrag.id } });
	}

	setFilter = async () => {
		if (!this.lehrerListeManager.hasDaten()) {
			const listFiltered = this.lehrerListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		this.commit();
	}

}

