import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { GostSchuelerklausurTermin, List} from "@core";
import { ArrayList, BenutzerKompetenz, GostKlausurraumManager, GostKursklausurManager, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungNachschreibAnsichtProps } from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsichtProps";
import SGostKlausurplanungNachschreibAnsichtVue from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsicht.vue";
import { routeApp } from "../../RouteApp";

export class RouteGostKlausurplanungNachschreibAnsicht extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.nachschreibansicht", "nachschreibansicht", SGostKlausurplanungNachschreibAnsichtVue);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachschreibplan";
	}

	raummanager : GostKlausurraumManager = new GostKlausurraumManager();

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : Number(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			this.raummanager = await routeGostKlausurplanung.data.erzeugeKlausurraummanager(this.mapIDs(routeGostKlausurplanung.data.kursklausurmanager.schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(routeGostKlausurplanung.data.jahrgangsdaten.abiturjahr, routeGostKlausurplanung.data.halbjahr, routeGostKlausurplanung.data.quartalsauswahl.value)));
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungNachschreibAnsichtProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager()},
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			raummanager: this.raummanager,
		}
	}

	private mapIDs(skts: List<GostSchuelerklausurTermin>) {
		const numList = new ArrayList<number>();
		for (const skt of skts)
			numList.add(skt.id);
		return numList;
	}

}



export const routeGostKlausurplanungNachschreibAnsicht = new RouteGostKlausurplanungNachschreibAnsicht();

