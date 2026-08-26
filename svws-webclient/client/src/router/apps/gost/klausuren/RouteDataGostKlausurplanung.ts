import type { RouteParams } from "vue-router";

import type { GostHalbjahr, GostKlausurtermin } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeGostKlausurplanungKalender } from "./RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungNachschreiber } from "./RouteGostKlausurplanungNachschreiber";
import { routeGostKlausurplanungRaumzeit } from "./RouteGostKlausurplanungRaumzeit";
import { routeGostKlausurplanungSchienen } from "./RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungVorgaben } from "./RouteGostKlausurplanungVorgaben";
import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX } from "@ui";
import { gostKlausurplanungStateImpl } from "~/states/GostKlausurplanungStateImpl";

interface RouteStateGostKlausurplanung extends RouteStateInterface {
}

const defaultState = <RouteStateGostKlausurplanung> {
	view: routeGostKlausurplanungVorgaben,
};

export class RouteDataGostKlausurplanung extends RouteData<RouteStateGostKlausurplanung> {

	public constructor() {
		super(defaultState);
	}

	public getParamsKey(abiturjahr: number): string {
		const strAbiturjahr = (abiturjahr < 0) ? "vorlage" : ("abi" + abiturjahr);
		return CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "routeparams." + strAbiturjahr;
	}

	public getParams(abiturjahr: number) {
		const paramsJson = configStateImpl.config.getValue(this.getParamsKey(abiturjahr));
		if (paramsJson.length === 0) {
			return undefined;
		}
		const params = JSON.parse(paramsJson) as RouteParams;
		return params;
	}

	public setParams(abiturjahr: number, params: RouteParams): void {
		params.view = this.view.name;
		void configStateImpl.config.setValue(this.getParamsKey(abiturjahr), JSON.stringify(params));
	}

	gotoVorgaben = async () => {
		await RouteManager.doRoute(routeGostKlausurplanungVorgaben.getRoute({ abiturjahr: gostKlausurplanungStateImpl.abiturjahr, halbjahr: gostKlausurplanungStateImpl.halbjahr.id }));
	};

	gotoSchienen = async (termin: GostKlausurtermin | undefined) => {
		await RouteManager.doRoute(routeGostKlausurplanungSchienen.getRoute({ abiturjahr: gostKlausurplanungStateImpl.abiturjahr, halbjahr: gostKlausurplanungStateImpl.halbjahr.id, idtermin: termin ? termin.id : undefined }));
	};

	gotoKalenderdatum = async (datum: string | undefined, termin: GostKlausurtermin | undefined) => {
		const abiturjahr = termin?.abiturjahrgang ?? gostKlausurplanungStateImpl.abiturjahr;
		const halbjahr = termin?.halbjahr ?? gostKlausurplanungStateImpl.halbjahr.id;
		let datumRoute: string | number = -1;
		if (datum !== undefined) {
			datumRoute = datum.replaceAll("-", "");
		} else if ((termin !== undefined) && (termin.datum !== null)) {
			datumRoute = termin.datum.replaceAll("-", "");
		}
		await RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute({ abiturjahr, halbjahr, datum: datumRoute, idtermin: termin?.id }));
	};

	gotoRaumzeitTermin = async (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => {
		await RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute({ abiturjahr, halbjahr: halbjahr.id, idtermin }));
	};

	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(this.view.getRoute({ abiturjahr: gostKlausurplanungStateImpl.abiturjahr, halbjahr: value.id }));
	};

	gotoNachschreiber = async (abiturjahr: number, halbjahr: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKlausurplanungNachschreiber.getRoute({ abiturjahr, halbjahr: halbjahr.id }));
	};

	gotoStundenplan = async () => {
		await RouteManager.doRoute(routeStundenplan.getRoute({ idSchuljahresabschnitt: gostKlausurplanungStateImpl.abschnittOrException.id }));
	};

	gotoFach = async (idFach: number) => {
		await RouteManager.doRoute({ name: "schule.faecher.daten", params: { id: idFach } });
	};

}
