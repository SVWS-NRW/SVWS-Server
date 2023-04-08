import { BenutzerKompetenz, GostKlausurvorgabenManager, GostKursklausurManager, Schulform, ArrayList } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "../../RouteGost";
import { routeGostKlausurplanung, RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungDaten = () => import("~/components/gost/klausurplanung/SGostKlausurplanungDaten.vue");

export class RouteGostKlausurplanungKlausurdaten extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.klausurdaten", "klausurdaten", SGostKlausurplanungDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausurdaten";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new ArrayList(), new ArrayList())},
			klausurvorgabenmanager: () => { return routeGostKlausurplanung.data.hatKlausurvorgabenManager ? routeGostKlausurplanung.data.klausurvorgabenmanager : new GostKlausurvorgabenManager(new ArrayList())},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			erzeugeKlausurvorgabe: routeGostKlausurplanung.data.erzeugeKlausurvorgabe,
			patchKlausurvorgabe: routeGostKlausurplanung.data.patchKlausurvorgabe,
			loescheKlausurvorgabe: routeGostKlausurplanung.data.loescheKlausurvorgabe,
			erzeugeVorgabenAusVorlage: routeGostKlausurplanung.data.erzeugeVorgabenAusVorlage,
		}
	}

}

export const routeGostKlausurplanungKlausurdaten = new RouteGostKlausurplanungKlausurdaten();

