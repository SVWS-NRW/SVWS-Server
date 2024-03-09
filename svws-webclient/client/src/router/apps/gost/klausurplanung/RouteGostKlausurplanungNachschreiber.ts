import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, GostKursklausurManager, Schulform, ArrayList, ServerMode, GostKlausurvorgabenManager } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";

import type { GostKlausurplanungNachschreiberProps } from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreiberProps";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungNachschreiber = () => import("~/components/gost/klausurplanung/SGostKlausurplanungNachschreiber.vue");

export class RouteGostKlausurplanungNachschreiber extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.nachschreiber", "nachschreiber", SGostKlausurplanungNachschreiber);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachschreiber";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : Number(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter dürfen keine Arrays sein"));
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			return routeError.getRoute(new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein."));
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungNachschreiberProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new GostKlausurvorgabenManager(new ArrayList()), new ArrayList(), null, null, null)},
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			erzeugeKlausurtermin: routeGostKlausurplanung.data.erzeugeKlausurtermin,
			loescheKlausurtermine: routeGostKlausurplanung.data.loescheKlausurtermine,
			blockenNachschreibklausuren: routeGostKlausurplanung.data.blockenNachschreiber,
			updateKlausurblockung: routeGostKlausurplanung.data.updateKlausurblockung,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungNachschreiber = new RouteGostKlausurplanungNachschreiber();


