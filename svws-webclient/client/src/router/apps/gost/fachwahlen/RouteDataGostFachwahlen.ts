import type { RouteLocationRaw } from "vue-router";

import type { GostHalbjahr, GostStatistikFachwahl, List, SchuelerListeEintrag } from "@core";
import { ArrayList, SchuelerStatus, GostJahrgangsFachwahlenManager, GostJahrgangFachwahlen } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeGostFachwahlenAllgemein } from "./RouteGostFachwahlenAllgemein";
import { routeGostFachwahlenAbitur } from "./RouteGostFachwahlenAbitur";
import { routeGostFachwahlenAbiturFach } from "./RouteGostFachwahlenAbiturFach";
import { routeGostFachwahlenFach } from "./RouteGostFachwahlenFach";
import { routeGostFachwahlenFachHalbjahr } from "./RouteGostFachwahlenFachHalbjahr";
import { routeGostFachwahlenHalbjahr } from "./RouteGostFachwahlenHalbjahr";
import { routeGostFachwahlenLeistungskurse } from "./RouteGostFachwahlenLeistungskurse";
import { routeGostFachwahlenZusatzkurse } from "./RouteGostFachwahlenZusatzkurse";

interface RouteStateDataGostFachwahlen extends RouteStateInterface {
	abiturjahr: number;
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
}

const defaultState = <RouteStateDataGostFachwahlen> {
	abiturjahr: -1,
	fachwahlstatistik: new ArrayList<GostStatistikFachwahl>(),
	fachwahlenManager: new GostJahrgangsFachwahlenManager(new GostJahrgangFachwahlen()),
	mapSchueler: new Map<number, SchuelerListeEintrag>(),
};

export class RouteDataGostFachwahlen extends RouteData<RouteStateDataGostFachwahlen> {

	public constructor() {
		super(defaultState);
	}

	get abiturjahr(): number {
		if (this._state.value.abiturjahr < 0)
			throw new Error("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		return this._state.value.abiturjahr;
	}

	public get fachwahlstatistik(): List<GostStatistikFachwahl> {
		return this._state.value.fachwahlstatistik;
	}

	public get fachwahlenManager(): GostJahrgangsFachwahlenManager {
		return this._state.value.fachwahlenManager;
	}

	public get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this._state.value.mapSchueler;
	}

	public async setEintrag(abiturjahr: number) {
		if (abiturjahr === -1) {
			this.setPatchedDefaultState({ abiturjahr });
			return;
		}
		const fachwahlstatistik = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr);
		const fachwahlen = await api.server.getGostAbiturjahrgangFachwahlen(api.schema, abiturjahr);
		const fachwahlenManager = new GostJahrgangsFachwahlenManager(fachwahlen);
		const listSchueler = await api.server.getGostAbiturjahrgangSchueler(api.schema, abiturjahr);
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listSchueler) {
			const status = SchuelerStatus.fromID(s.status);
			if ((status !== null) && ([SchuelerStatus.AKTIV, SchuelerStatus.EXTERN, SchuelerStatus.ABSCHLUSS, SchuelerStatus.BEURLAUBT, SchuelerStatus.NEUAUFNAHME].includes(status)))
				mapSchueler.set(s.id, s);
		}
		this.setPatchedDefaultState({ abiturjahr, fachwahlstatistik, fachwahlenManager, mapSchueler });
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

