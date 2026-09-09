
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { routeApp } from "../RouteApp";
import { routeSchuelerIndividualdaten } from "../schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchueler } from "../schueler/RouteSchueler";
import type { StatistikSchuelerProps } from "~/components/statistik/StatistikSchuelerProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteStatistik, routeStatistik } from "./RouteStatistik";
const StatistikSchueler = () => import("~/components/statistik/StatistikSchueler.vue");

export class RouteStatistikSchueler extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.schueler", "schueler", StatistikSchueler);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Schüler";
	}

	public getProps(): StatistikSchuelerProps {
		return {
			// statistik
			gotoSchueler: routeStatistik.data.gotoSchueler,
			zeigeAlles: false,
			// schueler
			patch: routeSchueler.data.patch,
			fahrschuelerartenById: routeApp.cache.kataloge.fahrschuelerartenById,
			foerderschwerpunkteById: routeApp.cache.kataloge.foerderschwerpunkteById,
			haltestellenById: routeApp.cache.kataloge.haltestellenById,
			religionenById: routeApp.cache.kataloge.religionenById,
			mapTelefonArten: routeApp.cache.kataloge.telefonartenById,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.getListSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			mapSchulen: routeSchuelerIndividualdaten.data.mapSchulen,
			autofocus: routeSchueler.data.autofocus,
		};
	}
}

export const routeStatistikSchueler = new RouteStatistikSchueler();
