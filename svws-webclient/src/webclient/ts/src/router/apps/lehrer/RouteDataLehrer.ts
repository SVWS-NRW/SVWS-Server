import { LehrerListeEintrag, LehrerPersonaldaten, LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { shallowRef } from "vue";
import { routeLogin } from "~/router/RouteLogin";
import { RouteManager } from "~/router/RouteManager";
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
}

export class RouteDataLehrer {

	private static _defaultState : RouteStateLehrer = {
		idSchuljahresabschnitt: -1,
		mapLehrer: new Map(),
		auswahl: undefined,
		stammdaten: undefined,
		personaldaten: undefined,
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


	private async ladeListe(): Promise<Map<number, LehrerListeEintrag>> {
		const listLehrer = await routeLogin.data.api.getLehrer(routeLogin.data.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		return mapLehrer;
	}


	private ersterLehrer(mapLehrer: Map<number, LehrerListeEintrag>): LehrerListeEintrag | undefined {
		if (mapLehrer.size === 0)
			return undefined;
		return mapLehrer.values().next().value;
	}


	private async ladeStammdaten(eintrag: LehrerListeEintrag | undefined): Promise<LehrerStammdaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await routeLogin.data.api.getLehrerStammdaten(routeLogin.data.schema, eintrag.id);
	}


	private async ladePersonaldaten(eintrag: LehrerListeEintrag | undefined): Promise<LehrerPersonaldaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await routeLogin.data.api.getLehrerPersonaldaten(routeLogin.data.schema, eintrag.id);
	}


	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		// TODO Lade die Lehrerliste in Abhängigkeit von dem angegebenen Schuljahresabschnitt, sobald die API-Methode dafür existiert
		const mapLehrer = await this.ladeListe();
		const auswahl = this.ersterLehrer(mapLehrer);
		const stammdaten = await this.ladeStammdaten(auswahl);
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			mapLehrer: mapLehrer,
			auswahl: auswahl,
			stammdaten: stammdaten
		});
	}


	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}


	public get firstLehrer(): LehrerListeEintrag | undefined {
		if (this.mapLehrer.size === 0)
			return undefined;
		return this.mapLehrer.values().next().value;
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
		const neueAuswahl = (this.mapLehrer.get(lehrer.id) === undefined) ? this.ersterLehrer(this.mapLehrer) : lehrer;
		const stammdaten = await this.ladeStammdaten(neueAuswahl);
		const personaldaten = this.hatPersonaldaten ? await this.ladePersonaldaten(neueAuswahl) : undefined;
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: this._state.value.idSchuljahresabschnitt,
			mapLehrer: this._state.value.mapLehrer,
			auswahl: neueAuswahl,
			stammdaten: stammdaten,
			personaldaten: personaldaten,
		});
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
		await routeLogin.data.api.patchLehrerStammdaten(data, routeLogin.data.schema, this.stammdaten.id);
		Object.assign(this.stammdaten, data);
		// TODO Bei Anpassungen von nachname, vorname, kürzel -> Lehrerliste aktualisieren...
		this.commit();
	}

	patchPersonaldaten = async (data : Partial<LehrerPersonaldaten>) => {
		await routeLogin.data.api.patchLehrerPersonaldaten(data, routeLogin.data.schema, this.personaldaten.id);
		Object.assign(this.personaldaten, data);
		this.commit();
	}

	gotoLehrer = async (value: LehrerListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeLehrer.name, params: { } });
			return;
		}
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

}

