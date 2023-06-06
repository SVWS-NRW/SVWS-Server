import type { SchuelerListeEintrag} from "@core";
import { BenutzerKompetenz, SchuelerKAoADaten, Schulform } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerKAoAProps } from "~/components/schueler/kaoa/SSchuelerKaoaProps";
import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler} from "~/router/apps/RouteSchueler";
import { routeSchueler } from "~/router/apps/RouteSchueler";

interface RouteStateSchuelerKAoA {
	auswahl: SchuelerListeEintrag | undefined;
	data: SchuelerKAoADaten;
}
export class RouteDataSchuelerKAoA {

	private static _defaultState : RouteStateSchuelerKAoA = {
		auswahl: undefined,
		data: new SchuelerKAoADaten(),
	}

	private _state = shallowRef<RouteStateSchuelerKAoA>(RouteDataSchuelerKAoA._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerKAoA>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerKAoA._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerKAoA>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get data(): SchuelerKAoADaten {
		// if (this._state.value.data === undefined)
		// 	throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.data
	}

	patch = async (data : Partial<SchuelerKAoADaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patch KAoA", data);
	}


	public async ladeDaten(auswahl?: SchuelerListeEintrag) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === undefined)
			this.setPatchedDefaultState({});
		else {
			try {
				// TODO Lade KAoA-Daten
			} catch(error) {
				throw new Error("Die KAoA-Daten konnten nicht eingeholt werden, sind für diesen Schüler KAoA-Daten möglich?")
			}
		}
	}
}

const SSchuelerKaoa = () => import("~/components/schueler/kaoa/SSchuelerKaoa.vue");

export class RouteSchuelerKAoA extends RouteNode<RouteDataSchuelerKAoA, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f=>!f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.kaoa", "kaoa", SSchuelerKaoa, new RouteDataSchuelerKAoA());
		super.propHandler = (route) => this.getProps(route);
		super.text = "KAoA";
		super.isHidden = (params?: RouteParams) => routeSchueler.data.auswahl === undefined;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.data.ladeDaten(undefined);
		} else {
			const id = parseInt(to_params.id);
			try {
				await this.data.ladeDaten(this.parent.data.mapSchueler.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerKAoAProps {
		return {
			data: this.data.data,
			patch: this.data.patch,
		 };
	}

}

export const routeSchuelerKAoA = new RouteSchuelerKAoA();

