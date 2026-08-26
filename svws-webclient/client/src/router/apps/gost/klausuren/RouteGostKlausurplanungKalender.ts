import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { gostKlausurplanungStateImpl } from "~/states/GostKlausurplanungStateImpl";
import { BenutzerKompetenz, ServerMode, DeveloperNotificationException, GostHalbjahr, DateUtils } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { checkHiddenKlausurplanungStundenplan, routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";
import { routeGostKlausurplanungVorgaben } from "./RouteGostKlausurplanungVorgaben";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausuren/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.kalender", String.raw`kalender/:datum(-1|\d{8})?/:idtermin(\d+)?`, SGostKlausurplanungKalender);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Kalender";
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

	protected async update(_to: RouteNode<any, any>, to_params: RouteParams, _from: RouteNode<any, any> | undefined, from_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			if (!gostKlausurplanungStateImpl.manager.stundenplanManagerExistsByAbschnitt(gostKlausurplanungStateImpl.abschnittOrException.id)) {
				return routeGostKlausurplanungVorgaben.getRoute();
			}
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const { datum: datumParam } = RouteNode.getStringParams(to_params, ["datum"]);
			const { datum: datumFromParam } = RouteNode.getStringParams(from_params, ["datum"]);
			const datum = this.parseDatumParam(datumParam);
			const datumFrom = this.parseDatumParam(datumFromParam);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			const termin = gostKlausurplanungStateImpl.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			gostKlausurplanungStateImpl.setSelectedTermin(termin);
			if ((abiturjahr === undefined) || (halbjahr === null)) {
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
			}
			if ((datum === undefined) && (datumFrom === undefined)) {
				return this.getRoute({ datum: this.getRouteDatumDerGueltigenKalenderwoche(), idtermin: termin === undefined ? undefined : termin.id });
			} else if ((datum === undefined) && (datumFrom !== undefined)) {
				return this.getRoute({ datum: this.toRouteDatum(datumFrom), idtermin: termin === undefined ? undefined : termin.id });
			} else if (datum !== undefined) {
				gostKlausurplanungStateImpl.setKalenderdatum(this.getDatumOderGueltigenStundenplanStart(datum));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	private parseDatumParam(datum: string | undefined): string | undefined {
		if ((datum === undefined) || (datum === "-1")) {
			return undefined;
		}
		if (datum.length !== 8) {
			throw new DeveloperNotificationException(`Fehler: Das Kalenderdatum '${datum}' muss im Format JJJJMMTT angegeben werden.`);
		}
		return datum.slice(0, 4) + "-" + datum.slice(4, 6) + "-" + datum.slice(6, 8);
	}

	private toRouteDatum(datum: string): string {
		return datum.replaceAll("-", "");
	}

	private getRouteDatumDerGueltigenKalenderwoche(): string {
		const datum = gostKlausurplanungStateImpl.kalenderdatum ?? new Date().toISOString().slice(0, 10);
		const stundenplan = gostKlausurplanungStateImpl.manager.stundenplanManagerGetByAbschnittAndDatumOrClosest(gostKlausurplanungStateImpl.abschnittOrException.id, datum);
		const kwClosest = stundenplan.kalenderwochenzuordnungGetByDatum(datum);
		return this.toRouteDatum(DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwClosest.jahr, kwClosest.kw));
	}

	private getDatumOderGueltigenStundenplanStart(datum: string): string {
		const idAbschnitt = gostKlausurplanungStateImpl.abschnittOrException.id;
		const stundenplan = gostKlausurplanungStateImpl.manager.stundenplanManagerGetByAbschnittAndDatumOrNull(idAbschnitt, datum);
		return stundenplan === null ? gostKlausurplanungStateImpl.manager.stundenplanManagerGetByAbschnittAndDatumOrClosest(idAbschnitt, datum).getGueltigAb() : datum;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const datum = gostKlausurplanungStateImpl.kalenderdatum?.replaceAll("-", "") ?? undefined;
		const idtermin = gostKlausurplanungStateImpl.selectedTermin?.id ?? undefined;
		return { datum, idtermin };
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();
