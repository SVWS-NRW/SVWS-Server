import { LehrerListeEintrag, LehrerPersonaldaten, LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "../RouteLehrer";
import { routeLehrerIndividualdaten } from "./RouteLehrerIndividualdaten";

interface RouteStateLehrer {
	// Daten, allgemein
	idSchuljahresabschnitt: number,
	mapLehrer: Map<number, LehrerListeEintrag>;
	// Daten abhängig von der Lehrer-ID
	auswahl: LehrerListeEintrag | undefined;
	stammdaten: LehrerStammdaten | undefined;
	// später nachzuladende Daten (Routen-abhängig)
	personaldaten: LehrerPersonaldaten | undefined;
	// TODO Unterrichtsdaten
	view: RouteNode<any, any>;
}

export class RouteDataLehrer {

	private static _defaultState : RouteStateLehrer = {
		idSchuljahresabschnitt: -1,
		mapLehrer: new Map(),
		auswahl: undefined,
		stammdaten: undefined,
		personaldaten: undefined,
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


	private firstLehrer(mapLehrer: Map<number, LehrerListeEintrag>): LehrerListeEintrag | undefined {
		if (mapLehrer.size === 0)
			return undefined;
		return mapLehrer.values().next().value;
	}


	private async ladeStammdaten(eintrag: LehrerListeEintrag | undefined): Promise<LehrerStammdaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await api.server.getLehrerStammdaten(api.schema, eintrag.id);
	}


	private async ladePersonaldaten(eintrag: LehrerListeEintrag | undefined): Promise<LehrerPersonaldaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await api.server.getLehrerPersonaldaten(api.schema, eintrag.id);
	}


	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		// TODO Lade die Lehrerliste in Abhängigkeit von dem angegebenen Schuljahresabschnitt, sobald die API-Methode dafür existiert
		const mapLehrer = await api.getLehrerListeAktuell();
		const auswahl = this.firstLehrer(mapLehrer);
		const stammdaten = await this.ladeStammdaten(auswahl);
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			mapLehrer: mapLehrer,
			auswahl: auswahl,
			stammdaten: stammdaten,
			view: this._state.value.view
		});
	}


	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	/**
	 * Setzt den ausgewählten Lehrer und lädt dessen Stammddaten.
	 *
	 * @param lehrer   der ausgewählte Lehrer
	 */
	public async setLehrer(lehrer: LehrerListeEintrag | undefined) {
		if (lehrer?.id === this._state.value.auswahl?.id)
			return;
		if ((lehrer === undefined) || (this.mapLehrer.size === 0)) {
			this.setPatchedDefaultState({
				idSchuljahresabschnitt: this._state.value.idSchuljahresabschnitt,
				mapLehrer: this._state.value.mapLehrer,
			});
			return;
		}
		const neueAuswahl = (this.mapLehrer.get(lehrer.id) === undefined) ? this.firstLehrer(this.mapLehrer) : lehrer;
		const stammdaten = await this.ladeStammdaten(neueAuswahl);
		const personaldaten = this.hatPersonaldaten ? await this.ladePersonaldaten(neueAuswahl) : undefined;
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: this._state.value.idSchuljahresabschnitt,
			mapLehrer: this._state.value.mapLehrer,
			auswahl: neueAuswahl,
			stammdaten: stammdaten,
			personaldaten: personaldaten,
			view: this._state.value.view,
		});
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


	get auswahl(): LehrerListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	public get hatStammdaten(): boolean {
		return this._state.value.stammdaten !== undefined;
	}

	get stammdaten(): LehrerStammdaten {
		if (this._state.value.stammdaten === undefined)
			throw new Error("Unerwarteter Fehler: Lehrerstammdaten nicht initialisiert");
		return this._state.value.stammdaten;
	}

	public get hatPersonaldaten(): boolean {
		return this._state.value.personaldaten !== undefined;
	}

	get personaldaten(): LehrerPersonaldaten {
		if (this._state.value.personaldaten === undefined)
			throw new Error("Unerwarteter Fehler: Lehrerpersonaldaten nicht initialisiert");
		return this._state.value.personaldaten;
	}


	public async loadPersonaldaten() {
		if (this.auswahl === undefined)
			return;
		const personaldaten = await this.ladePersonaldaten(this.auswahl);
		this.setPatchedState({
			personaldaten: personaldaten,
		});
	}

	public async unloadPersonaldaten() {
		this.setPatchedState({
			personaldaten: undefined,
		});
	}

	patchStammdaten = async (data : Partial<LehrerStammdaten>) => {
		await api.server.patchLehrerStammdaten(data, api.schema, this.stammdaten.id);
		Object.assign(this.stammdaten, data);
		// TODO Bei Anpassungen von nachname, vorname, kürzel -> Lehrerliste aktualisieren...
		this.commit();
	}

	patchPersonaldaten = async (data : Partial<LehrerPersonaldaten>) => {
		await api.server.patchLehrerPersonaldaten(data, api.schema, this.personaldaten.id);
		Object.assign(this.personaldaten, data);
		this.commit();
	}

	gotoLehrer = async (value: LehrerListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeLehrer.name, params: { } });
			return;
		}
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

}

