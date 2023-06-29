import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { FoerderschwerpunktEintrag, JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";

import type { SchuelerAbschnittAuswahlProps } from "~/components/schueler/abschnitt/SSchuelerAbschnittAuswahlProps";
import type { SchuelerAbschnittDatenProps } from "~/components/schueler/abschnitt/SSchuelerAbschnittDatenProps";

interface RouteStateDataSchuelerAbschnittDaten {
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapKlassen: Map<number, KlassenListeEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;

}
export class RouteDataSchuelerAbschnittDaten {

	private static _defaultState: RouteStateDataSchuelerAbschnittDaten = {
		auswahl: undefined,
		daten: undefined,
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
		mapKlassen: new Map(),
		mapFoerderschwerpunkte: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerAbschnittDaten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerAbschnittDaten>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerAbschnittDaten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerAbschnittDaten>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): SchuelerLernabschnittListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Lernabschnittseintrag nicht festgelegt, es können keine Informationen zu den Leistungsdaten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get daten(): SchuelerLernabschnittsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Leistungsdaten nicht initialisiert");
		return this._state.value.daten;
	}
	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get mapKlassen(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKlassen;
	}

	get mapFoerderschwerpunkte(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapFoerderschwerpunkte;
	}

	public async ladeListe() {
		const lehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of lehrer)
			mapLehrer.set(l.id, l);
		const jahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of jahrgaenge)
			mapJahrgaenge.set(j.id, j);
		const forderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapFoerderschwerpunkte = new Map<number, FoerderschwerpunktEintrag>();
		for (const fs of forderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		this.setPatchedDefaultState({ mapLehrer, mapJahrgaenge, mapFoerderschwerpunkte });
	}

	public async setEintrag(auswahl: SchuelerLernabschnittListeEintrag | undefined) {
		if (((auswahl === undefined) && (this._state.value.daten === undefined)) || ((this._state.value.daten !== undefined) && (this._state.value.daten.id === auswahl?.id)))
			return;
		let daten;
		const mapKlassen = new Map();
		if (auswahl !== undefined) {
			daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, auswahl.id);
			const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, auswahl.schuljahresabschnitt);
			for (const k of listKlassen)
				mapKlassen.set(k.id, k);
		}
		this.setPatchedState({ auswahl, daten, mapKlassen });
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerAbschnittDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patch = async (data : Partial<SchuelerLernabschnittsdaten>) => {
		console.log("TODO: Implementierung patch", data);
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

	patchBemerkungen = async (data : Partial<SchuelerLernabschnittBemerkungen>) => {
		console.log("TODO: Implementierung patchBemerkungen", data);
		// TODO await api.server.patchSchuelerLernabschnittsdatenBemerkungen(data, api.schema, this.daten.value.id);
	}

}

const SSchuelerAbschnittDaten = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittDaten.vue");
const SSchuelerAbschnittAuswahl = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittAuswahl.vue")

export class RouteSchuelerAbschnittDaten extends RouteNode<RouteDataSchuelerAbschnittDaten, RouteSchuelerAbschnitt> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.abschnitt.daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerAbschnittDaten, new RouteDataSchuelerAbschnittDaten());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abschnittsdaten";
		super.setView("lernabschnittauswahl", SSchuelerAbschnittAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return (to_params.id !== undefined);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id);
		if (to_params.abschnitt === undefined) {
			return routeSchuelerAbschnitt.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr);
			const eintrag = routeSchuelerAbschnitt.data.getEntry(abschnitt, wechselNr);
			await this.data.setEintrag(eintrag);
		}
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAbschnittAuswahlProps {
		return {
			lernabschnitt: this.data.auswahl,
			lernabschnitte: routeSchuelerAbschnitt.data.listAbschnitte,
			gotoLernabschnitt: this.data.gotoLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbschnittDatenProps {
		return {
			schule: api.schuleStammdaten,
			data: this.data.daten,
			mapLehrer: this.data.mapLehrer,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKlassen: this.data.mapKlassen,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			patch: this.data.patch,
			patchBemerkungen: this.data.patchBemerkungen
		};
	}

}

export const routeSchuelerAbschnittDaten = new RouteSchuelerAbschnittDaten();

