import { shallowRef } from "vue";
import type { RouteLocationRaw } from "vue-router";

import { ArrayList } from "@core";
import type { GostHalbjahr, GostStatistikFachwahl, List } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeGostFachwahlenAllgemein } from "./RouteGostFachwahlenAllgemein";
import { routeGostFachwahlenAbitur } from "./RouteGostFachwahlenAbitur";
import { routeGostFachwahlenAbiturFach } from "./RouteGostFachwahlenAbiturFach";
import { routeGostFachwahlenFach } from "./RouteGostFachwahlenFach";
import { routeGostFachwahlenFachHalbjahr } from "./RouteGostFachwahlenFachHalbjahr";
import { routeGostFachwahlenHalbjahr } from "./RouteGostFachwahlenHalbjahr";
import { routeGostFachwahlenLeistungskurse } from "./RouteGostFachwahlenLeistungskurse";
import { routeGostFachwahlenZusatzkurse } from "./RouteGostFachwahlenZusatzkurse";

interface RouteStateDataGostFachwahlen {
	abiturjahr: number;
	fachwahlen: List<GostStatistikFachwahl>;
}

export class RouteDataGostFachwahlen  {

	private static _defaultState: RouteStateDataGostFachwahlen = {
		abiturjahr: -1,
		fachwahlen: new ArrayList<GostStatistikFachwahl>(),
	}

	private _state = shallowRef(RouteDataGostFachwahlen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... RouteDataGostFachwahlen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get abiturjahr(): number {
		if (this._state.value.abiturjahr < 0)
			throw new Error("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		return this._state.value.abiturjahr;
	}

	public get fachwahlen(): List<GostStatistikFachwahl> {
		return this._state.value.fachwahlen;
	}

	public async setEintrag(abiturjahr: number) {
		if (abiturjahr === -1) {
			this.setPatchedDefaultState({ abiturjahr });
			return;
		}
		const fachwahlen = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr);
		this.setPatchedDefaultState({ abiturjahr, fachwahlen });
	}

	doSelect = async (idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr) : Promise<void> => {
		// Ermittle die Route, die aufgrund der Auswahl genutzt werden soll
		let route : RouteLocationRaw | undefined;
		if (idFach === undefined) {
			if (bereich === undefined) {
				route = routeGostFachwahlenAllgemein.getRoute(this.abiturjahr);
			} else if ((bereich === "Halbjahr") && (halbjahr !== undefined)) {
				route = routeGostFachwahlenHalbjahr.getRoute(this.abiturjahr, halbjahr);
			} else if (bereich === "ZK") {
				route = routeGostFachwahlenZusatzkurse.getRoute(this.abiturjahr);
			} else if (bereich === "LK") {
				route = routeGostFachwahlenLeistungskurse.getRoute(this.abiturjahr);
			} else if (bereich === "Abi") {
				route = routeGostFachwahlenAbitur.getRoute(this.abiturjahr);
			}
		} else {
			if (bereich === undefined) {
				route = routeGostFachwahlenFach.getRoute(this.abiturjahr, idFach);
			} else if ((bereich === "Halbjahr") && (halbjahr !== undefined)) {
				route = routeGostFachwahlenFachHalbjahr.getRoute(this.abiturjahr, idFach, halbjahr);
			} else if (bereich === "Abi") {
				route = routeGostFachwahlenAbiturFach.getRoute(this.abiturjahr, idFach);
			}
		}
		// Wähle ggf. die Default-Route, um Fehler abzufangen...
		if (route === undefined)
			route = route = routeGostFachwahlenAllgemein.getRoute(this.abiturjahr);
		// Führe das Routing durch
		await RouteManager.doRoute(route);
	}

}

