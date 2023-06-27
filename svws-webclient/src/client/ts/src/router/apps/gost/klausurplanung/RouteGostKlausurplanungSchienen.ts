import { RouteNode } from "~/router/RouteNode";
import type { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { routeGostKlausurplanung } from "../RouteGostKlausurplanung";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, GostHalbjahr, GostKursklausurManager, Schulform, ArrayList, ServerMode } from "@core";
import type { GostKlausurplanungSchienenProps } from "~/components/gost/klausurplanung/SGostKlausurplanungSchienenProps";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.schienen", "schienen", SGostKlausurplanungSchienen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schienen";
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			return new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			return new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungSchienenProps {
		return {
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new ArrayList(), new ArrayList())},
			setTerminToKursklausur: routeGostKlausurplanung.data.setTerminToKursklausur,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			erzeugeKlausurtermin: routeGostKlausurplanung.data.erzeugeKlausurtermin,
			loescheKlausurtermin: routeGostKlausurplanung.data.loescheKlausurtermin,
			erzeugeKursklausurenAusVorgaben: routeGostKlausurplanung.data.erzeugeKursklausurenAusVorgaben,
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			mapSchueler: routeGostKlausurplanung.data.mapSchueler,
			kursmanager: routeGostKlausurplanung.data.kursManager,
		}
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


