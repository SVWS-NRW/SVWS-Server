import { shallowRef } from "vue";

import type { LehrerListeEintrag, LehrerPersonaldaten, LehrerStammdaten } from "@core";
import { ArrayList, DeveloperNotificationException, LehrerListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";

interface RouteStateLehrer {
	// Daten, allgemein
	idSchuljahresabschnitt: number,
	lehrerListeManager: LehrerListeManager;
	// später nachzuladende Daten (Routen-abhängig)
	personaldaten: LehrerPersonaldaten | null;
	// TODO Unterrichtsdaten
	view: RouteNode<any, any>;
}

export class RouteDataLehrer {

	private static _defaultState : RouteStateLehrer = {
		idSchuljahresabschnitt: -1,
		lehrerListeManager: new LehrerListeManager(null, new ArrayList()),
		personaldaten: null,
		view: routeLehrerIndividualdaten
	}

	private _state = shallowRef<RouteStateLehrer>(RouteDataLehrer._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateLehrer>) {
		this._state.value = Object.assign({ ... RouteDataLehrer._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateLehrer>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeLehrer.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für Lehrer gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
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
		const lehrerListeManager = new LehrerListeManager(api.schulform, listLehrer);
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
		let auswahl = this.lehrerListeManager.liste.get(lehrer.id);
		if (auswahl === null)
			auswahl = this.lehrerListeManager.filtered().isEmpty() ? null : this.lehrerListeManager.filtered().get(0);
		const daten = auswahl === null ? null : await api.server.getLehrerStammdaten(api.schema, auswahl.id);
		this.lehrerListeManager.setDaten(daten);
		const personaldaten = this.lehrerListeManager.hasDaten() && this.hatPersonaldaten
			? await this.ladePersonaldaten(this.lehrerListeManager.auswahl()) : null;
		this.setPatchedState({ personaldaten });
	}

	public get hatPersonaldaten(): boolean {
		return this._state.value.personaldaten !== null;
	}

	get personaldaten(): LehrerPersonaldaten {
		if (this._state.value.personaldaten === null)
			throw new Error("Unerwarteter Fehler: Lehrerpersonaldaten nicht initialisiert");
		return this._state.value.personaldaten;
	}


	public async loadPersonaldaten() {
		if (!this.lehrerListeManager.hasDaten())
			return;
		const personaldaten = await this.ladePersonaldaten(this.lehrerListeManager.auswahl());
		this.setPatchedState({ personaldaten, });
	}

	public async unloadPersonaldaten() {
		this.setPatchedState({ personaldaten: null });
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
		if (!this.hatPersonaldaten)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idLehrer =  this.personaldaten.id;
		await api.server.patchLehrerPersonaldaten(data, api.schema, idLehrer);
		Object.assign(this.personaldaten, data);
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

