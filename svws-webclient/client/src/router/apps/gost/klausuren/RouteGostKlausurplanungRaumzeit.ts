import type { RouteLocationRaw, RouteParams } from "vue-router";
import { gostKlausurplanungStateImpl } from "~/states/GostKlausurplanungStateImpl";
import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { checkHiddenKlausurplanungStundenplan, routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungRaumzeit = () => import("~/components/gost/klausuren/SGostKlausurplanungRaumzeit.vue");

export class RouteGostKlausurplanungRaumzeit extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.raumzeit", String.raw`raumzeit/:idtermin(\d+)?`, SGostKlausurplanungRaumzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Räume und Startzeiten";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	public getProps() {
		return {
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
		};
	}

	public checkHidden(params?: RouteParams) {
		return checkHiddenKlausurplanungStundenplan(params);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if ((abiturjahr === undefined) || (halbjahr === null)) {
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
			}
			const terminList = gostKlausurplanungStateImpl.manager.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(gostKlausurplanungStateImpl.jahrgangsdaten.abiturjahr, gostKlausurplanungStateImpl.halbjahr, gostKlausurplanungStateImpl.quartal);
			if ((idtermin === undefined) && !terminList.isEmpty()) {
				const termin = (gostKlausurplanungStateImpl.selectedTermin !== undefined) && terminList.contains(gostKlausurplanungStateImpl.selectedTermin) ? gostKlausurplanungStateImpl.selectedTermin : terminList.getFirst();
				return this.getRoute({ idtermin: termin.id });
			}
			const termin = gostKlausurplanungStateImpl.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			if (termin !== undefined && termin.datum !== null) {
				if (gostKlausurplanungStateImpl.manager.stundenplanManagerExistsByAbschnittAndDatum(gostKlausurplanungStateImpl.abschnittOrException.id, termin.datum)) {
					gostKlausurplanungStateImpl.setRaumTermin(termin);
				}
			}
		} catch (e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

}

export const routeGostKlausurplanungRaumzeit = new RouteGostKlausurplanungRaumzeit();
