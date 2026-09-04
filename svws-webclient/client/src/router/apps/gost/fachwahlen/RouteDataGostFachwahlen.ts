import type { RouteLocationRaw } from "vue-router";
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
import { routeGostFachwahlenZKFach } from "./RouteGostFachwahlenZKFach";
import { routeGostFachwahlenLKFach } from "./RouteGostFachwahlenLKFach";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { GostJahrgangFachwahlen } from "@core/core/data/gost/GostJahrgangFachwahlen";
import type { GostStatistikFachwahl } from "@core/core/data/gost/GostStatistikFachwahl";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostJahrgangsFachwahlenManager } from "@core/core/utils/gost/GostJahrgangsFachwahlenManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

interface RouteStateDataGostFachwahlen extends RouteStateInterface {
	abiturjahr: number;
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	auswahl: { idFach?: number, bereich: string };
}

const defaultState = <RouteStateDataGostFachwahlen> {
	abiturjahr: -1,
	fachwahlstatistik: new ArrayList<GostStatistikFachwahl>(),
	fachwahlenManager: new GostJahrgangsFachwahlenManager(new GostJahrgangFachwahlen()),
	mapSchueler: new Map<number, SchuelerListeEintrag>(),
	auswahl: { bereich: 'Fach' },
};

export class RouteDataGostFachwahlen extends RouteData<RouteStateDataGostFachwahlen> {

	public constructor() {
		super(defaultState);
	}

	get abiturjahr(): number {
		if (this._state.value.abiturjahr < 0) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		}
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

	public get auswahl(): { idFach?: number, bereich: string } {
		return this._state.value.auswahl;
	}

	public set auswahl(auswahl: { idFach?: number, bereich: string }) {
		this.setPatchedState({ auswahl });
	}

	public async setEintrag(abiturjahr: number, idFach?: number) {
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
			const status = SchuelerStatus.data().getWertByKuerzel("" + s.status);
			if ((status !== null) && ([SchuelerStatus.AKTIV, SchuelerStatus.EXTERN, SchuelerStatus.ABSCHLUSS, SchuelerStatus.BEURLAUBT, SchuelerStatus.NEUAUFNAHME].includes(status))) {
				mapSchueler.set(s.id, s);
			}
		}
		const auswahl = this.auswahl;
		if (idFach !== undefined) {
			auswahl.idFach = idFach;
		}
		this.setPatchedDefaultState({ abiturjahr, fachwahlstatistik, fachwahlenManager, mapSchueler, auswahl });
	}

	doSelect = async (idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr): Promise<void> => {
		// Setze die Auswahl
		this.auswahl = { idFach, bereich: bereich ?? 'Fach' };
		// Ermittle die Route, die aufgrund der Auswahl genutzt werden soll
		let route: RouteLocationRaw | undefined;
		if (idFach === undefined) {
			if (bereich === undefined) {
				route = routeGostFachwahlenAllgemein.getRoute();
			} else if ((bereich === "Halbjahr") && (halbjahr !== undefined)) {
				route = routeGostFachwahlenHalbjahr.getRoute({ idHalbjahr: halbjahr.id });
			} else if (bereich === "ZK") {
				route = routeGostFachwahlenZusatzkurse.getRoute();
			} else if (bereich === "LK") {
				route = routeGostFachwahlenLeistungskurse.getRoute();
			} else if (bereich === "Abi") {
				route = routeGostFachwahlenAbitur.getRoute();
			}
		} else {
			if (bereich === undefined) {
				route = routeGostFachwahlenFach.getRoute({ idFach });
			} else if ((bereich === "Halbjahr") && (halbjahr !== undefined)) {
				route = routeGostFachwahlenFachHalbjahr.getRoute({ idFach, idHalbjahr: halbjahr.id });
			} else if (bereich === "ZK") {
				route = routeGostFachwahlenZKFach.getRoute({ idFach });
			} else if (bereich === "LK") {
				route = routeGostFachwahlenLKFach.getRoute({ idFach });
			} else if (bereich === "Abi") {
				route = routeGostFachwahlenAbiturFach.getRoute({ idFach });
			}
		}
		// Wähle ggf. die Default-Route, um Fehler abzufangen...
		route ??= routeGostFachwahlenAllgemein.getRoute();
		// Führe das Routing durch
		await RouteManager.doRoute(route);
	};
}

