import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { RouteDataGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteDataGostFachwahlen";

import { routeGostFachwahlenAllgemein } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAllgemein";
import { routeGostFachwahlenAbitur } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAbitur";
import { routeGostFachwahlenAbiturFach } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAbiturFach";
import { routeGostFachwahlenFach } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenFach";
import { routeGostFachwahlenFachHalbjahr } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenFachHalbjahr";
import { routeGostFachwahlenHalbjahr } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenHalbjahr";
import { routeGostFachwahlenLeistungskurse } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenLeistungskurse";
import { routeGostFachwahlenZusatzkurse } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenZusatzkurse";

import type { GostFachwahlenProps } from "~/components/gost/fachwahlen/SGostFachwahlenProps";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerLaufbahnplanung } from "../../schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";
import { routeGostFachwahlenZKFach } from "./RouteGostFachwahlenZKFach";
import { routeGostFachwahlenLKFach } from "./RouteGostFachwahlenLKFach";


const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<RouteDataGostFachwahlen, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostFachwahlen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
			routeGostFachwahlenAllgemein,
			routeGostFachwahlenAbitur,
			routeGostFachwahlenAbiturFach,
			routeGostFachwahlenFach,
			routeGostFachwahlenFachHalbjahr,
			routeGostFachwahlenHalbjahr,
			routeGostFachwahlenLeistungskurse,
			routeGostFachwahlenZusatzkurse,
			routeGostFachwahlenZKFach,
			routeGostFachwahlenLKFach,
		];
		super.defaultChild = routeGostFachwahlenAllgemein;
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params !== undefined ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			if ((abiturjahr === undefined) || (abiturjahr === -1))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch(e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (abiturjahr === undefined || abiturjahr === -1) {
				const [ jahrgang ] = routeGost.data.mapAbiturjahrgaenge.values();
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: jahrgang.abiturjahr }};
			}
			await this.data.setEintrag(abiturjahr);
			if (to.name === this.name)
				return this.getRouteDefaultChild();
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.setEintrag(-1);
	}

	gotoLaufbahnplanung = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute({ id: idSchueler }));
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenProps {
		return {
			fachwahlstatistik: this.data.fachwahlstatistik,
			faecherManager: routeGost.data.faecherManager,
			doSelect: this.data.doSelect,
			selected: () => this.data.auswahl,
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
