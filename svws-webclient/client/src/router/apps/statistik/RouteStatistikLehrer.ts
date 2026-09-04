import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { StatistikLehrerProps } from "~/components/statistik/StatistikLehrerProps";
import { routeStatistik, type RouteStatistik } from "./RouteStatistik";
import { routeLehrer } from "../lehrer/RouteLehrer";
import { routeApp } from "../RouteApp";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const StatistikLehrer = () => import("~/components/statistik/StatistikLehrer.vue");
export class RouteStatistikLehrer extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.lehrer", "lehrer", StatistikLehrer);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrer";
	}

	public getProps(to: RouteLocationNormalized): StatistikLehrerProps {
		return {
			// statistik
			zeigeAlles: false,
			statistikGesamt: routeStatistik.data.statistikGesamt,
			mapLehrer: routeStatistik.data.mapLehrer,
			lehrerListeManager: () => routeStatistik.data.managerLehrer,
			setAuswahl: routeStatistik.data.updateDatenLehrer,
			gotoLehrer: routeStatistik.data.gotoLehrer,
			// lehrer
			patch: routeLehrer.data.patch,
			// lehrer: leitungsfunktionen
			mapLeitungsfunktionen: routeApp.cache.kataloge.leitungsfunktionenById,
			getListLeitungsfunktionen: () => routeLehrer.data.getListLeitungsfunktionen,
			addLeitungsfunktion: routeLehrer.data.addLeitungsfunktion,
			patchLeitungsfunktion: routeLehrer.data.patchLeitungsfunktion,
			deleteLeitungsfunktionen: routeLehrer.data.deleteLeitungsfunktionen,

			mapSchulen: () => routeLehrer.data.mapSchulen,
			patchPersonaldaten: routeLehrer.data.patchPersonaldaten,
			patchAbschnittsdaten: routeLehrer.data.patchPersonalAbschnittsdaten,
			patchLehramt: routeLehrer.data.patchLehramt,
			addLehramt: routeLehrer.data.addLehramt,
			removeLehraemter: routeLehrer.data.removeLehraemter,
			patchLehrbefaehigung: routeLehrer.data.patchLehrbefaehigung,
			addLehrbefaehigung: routeLehrer.data.addLehrbefaehigung,
			removeLehrbefaehigungen: routeLehrer.data.removeLehrbefaehigungen,
			patchFachrichtung: routeLehrer.data.patchFachrichtung,
			addFachrichtung: routeLehrer.data.addFachrichtung,
			removeFachrichtungen: routeLehrer.data.removeFachrichtungen,
			addMehrleistung: routeLehrer.data.addMehrleistung,
			patchMehrleistung: routeLehrer.data.patchMehrleistung,
			removeMehrleistung: routeLehrer.data.removeMehrleistung,
			addMinderleistung: routeLehrer.data.addMinderleistung,
			patchMinderleistung: routeLehrer.data.patchMinderleistung,
			removeMinderleistung: routeLehrer.data.removeMinderleistung,
			addAnrechnung: routeLehrer.data.addAnrechnung,
			patchAnrechnungen: routeLehrer.data.patchAnrechnungen,
			removeAnrechnung: routeLehrer.data.removeAnrechnung,
			mapFaecher: () => routeLehrer.data.mapFaecher,
			lehrerUnterrichtsfaecher: () => routeLehrer.data.lehrerUnterrichtsfaecher,
			addLehrerUnterrichtsfach: routeLehrer.data.addLehrerUnterrichtsfach,
			patchLehrerUnterrichtsfach: routeLehrer.data.patchLehrerUnterrichtsfach,
			removeLehrerUnterrichtsfach: routeLehrer.data.removeLehrerUnterrichtsfach,
		};
	}
}

export const routeStatistikLehrer = new RouteStatistikLehrer();
