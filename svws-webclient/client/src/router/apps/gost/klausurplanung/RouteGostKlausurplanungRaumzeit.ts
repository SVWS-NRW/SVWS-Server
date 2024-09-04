import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, GostKlausurplanManager, Schulform, ServerMode, Vector } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungRaumzeitProps } from "~/components/gost/klausurplanung/SGostKlausurplanungRaumzeitProps";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";

const SGostKlausurplanungRaumzeit = () => import("~/components/gost/klausurplanung/SGostKlausurplanungRaumzeit.vue");

export class RouteGostKlausurplanungRaumzeit extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.raumzeit", "raumzeit/:idtermin(\\d+)?", SGostKlausurplanungRaumzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume und Startzeiten";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.manager.getStundenplanManagerOrNull())
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	public getRoute(abiturjahr: number, halbjahr: number, idtermin: number | undefined ) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, idtermin }};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idtermin instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
		const idTermin = !to_params.idtermin ? null : parseInt(to_params.idtermin);
		const terminList = routeGostKlausurplanung.data.manager.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(routeGostKlausurplanung.data.jahrgangsdaten.abiturjahr, routeGostKlausurplanung.data.halbjahr, routeGostKlausurplanung.data.quartalsauswahl.value);
		if (idTermin === null && !terminList.isEmpty()) {
			const termin = routeGostKlausurplanung.data.terminSelected.value !== undefined && terminList.contains(routeGostKlausurplanung.data.terminSelected.value) ? routeGostKlausurplanung.data.terminSelected.value : terminList.getFirst();
			return this.getRoute(abiturjahr, halbjahr.id, termin.id);
		}
		if (idTermin !== null) {
			const termin = routeGostKlausurplanung.data.manager.terminGetByIdOrException(idTermin);
			// routeGostKlausurplanung.data.terminSelected.value = termin;
			await routeGostKlausurplanung.data.setRaumTermin(termin);
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungRaumzeitProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			gotoTermin: routeGostKlausurplanung.data.gotoTermin,
			kMan: () => routeGostKlausurplanung.data.manager,
			createKlausurraum: routeGostKlausurplanung.data.createKlausurraum,
			loescheKlausurraum: routeGostKlausurplanung.data.loescheKlausurraum,
			patchKlausurraum: routeGostKlausurplanung.data.patchKlausurraum,
			setzeRaumZuSchuelerklausuren: routeGostKlausurplanung.data.setzeRaumZuSchuelerklausuren,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			setRaumTermin: routeGostKlausurplanung.data.setRaumTermin,
			terminSelected: routeGostKlausurplanung.data.terminSelected,
			zeigeAlleJahrgaenge: () => routeGostKlausurplanung.data.zeigeAlleJahrgaenge,
			setZeigeAlleJahrgaenge: routeGostKlausurplanung.data.setZeigeAlleJahrgaenge,
			setConfigValue: routeGostKlausurplanung.data.setConfigValue,
			getConfigValue: routeGostKlausurplanung.data.getConfigValue,
		}
	}

}

export const routeGostKlausurplanungRaumzeit = new RouteGostKlausurplanungRaumzeit();

