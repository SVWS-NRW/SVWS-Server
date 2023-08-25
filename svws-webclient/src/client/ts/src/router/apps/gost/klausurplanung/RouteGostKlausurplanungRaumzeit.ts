import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ServerMode, Vector } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungRaumzeitProps } from "~/components/gost/klausurplanung/SGostKlausurplanungRaumzeitProps";

const SGostKlausurplanungRaumzeit = () => import("~/components/gost/klausurplanung/SGostKlausurplanungRaumzeit.vue");

export class RouteGostKlausurplanungRaumzeit extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.raumzeit", "raumzeit", SGostKlausurplanungRaumzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausurräume und -startzeiten";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungRaumzeitProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new Vector(), new Vector())},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			kursmanager: routeGostKlausurplanung.data.kursManager,
			stundenplanmanager: routeGostKlausurplanung.data.stundenplanmanager,
			erzeugeKlausurraum: routeGostKlausurplanung.data.erzeugeKlausurraum,
			loescheKlausurraum: routeGostKlausurplanung.data.loescheKlausurraum,
			patchKlausurraum: routeGostKlausurplanung.data.patchKlausurraum,
			erzeugeKlausurraummanager: routeGostKlausurplanung.data.erzeugeKlausurraummanager,
			setzeRaumZuSchuelerklausuren: routeGostKlausurplanung.data.setzeRaumZuSchuelerklausuren,
			patchKlausurUhrzeit: routeGostKlausurplanung.data.patchKlausurUhrzeit,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungRaumzeit = new RouteGostKlausurplanungRaumzeit();

