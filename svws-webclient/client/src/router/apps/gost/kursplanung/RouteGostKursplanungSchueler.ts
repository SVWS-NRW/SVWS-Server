import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { GostKursplanungUmwahlansichtProps } from "~/components/gost/kursplanung/SGostKursplanungUmwahlansichtProps";
import type { KursplanungSchuelerAuswahlProps } from "~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahlProps";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";
import { ConfigElement } from "~/components/Config";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeGostKursplanung, type RouteGostKursplanung } from "~/router/apps/gost/kursplanung/RouteGostKursplanung";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeApp } from "../../RouteApp";
import { routeError } from "~/router/error/RouteError";

const SGostKursplanungUmwahlansicht = () => import("~/components/gost/kursplanung/SGostKursplanungUmwahlansicht.vue");
const SGostKursplanungSchuelerAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahl.vue");

export class RouteGostKursplanungSchueler extends RouteNode<any, RouteGostKursplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN
		], "gost.kursplanung.schueler", "schueler/:idschueler(\\d+)?", SGostKursplanungUmwahlansicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_schueler_auswahl", SGostKursplanungSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung - Schüler";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.kursplanung.schueler.auswahl.geschlecht", "user", "true"),
			new ConfigElement("gost.kursplanung.schueler.auswahl.filterOpen", "user", "true")
		]);
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: null };
			if ((abiturjahr === null) || (abiturjahr === -1))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idblockung: idBlockung, idergebnis: idErgebnis } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idblockung", "idergebnis"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if (abiturjahr === undefined)
				return { name: routeGost.name, params: { } };
			if ((halbjahr === null) || (idBlockung === undefined))
				return routeGostKursplanung.getRouteHalbjahr(abiturjahr, (halbjahr === null) ? GostHalbjahr.EF1.id : halbjahr.id);
			if (idErgebnis === undefined)
				return routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr.id, idBlockung);
			return true;
		} catch(e) {
			return routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idblockung: idBlockung, idergebnis: idErgebnis, idschueler: idSchueler } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idblockung", "idergebnis", "idschueler"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if ((abiturjahr === undefined) || (halbjahr === null))
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
			if ((abiturjahr !== routeGostKursplanung.data.abiturjahr) || (halbjahr !== routeGostKursplanung.data.halbjahr) || (idBlockung === undefined))
				return routeGostKursplanung.getRouteHalbjahr(abiturjahr, halbjahr.id);
			if (idBlockung !== routeGostKursplanung.data.auswahlBlockung.id)
				return routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr.id, idBlockung);
			if (idErgebnis !== routeGostKursplanung.data.auswahlErgebnis.id) {
				if (idErgebnis === undefined)
					routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr.id, idBlockung);
				else
					routeGostKursplanung.getRouteErgebnis(abiturjahr, halbjahr.id, idBlockung, idErgebnis);
			}
			// ... wurde die ID des Schülers auf undefined setzt, so prüfe, ob die Schülerliste leer ist und wähle ggf. das erste Element aus
			if (idSchueler === undefined) {
				if (routeGostKursplanung.data.datenmanager.schuelerGetAnzahl() > 0) {
					const schueler = routeGostKursplanung.data.datenmanager.schuelerGetListe().get(0);
					return this.getRoute({ idschueler: schueler.id });
				}
				return;
			}
			// ... wurde die ID des Schülers verändert, merke diesen Schüler
			if ((!routeGostKursplanung.data.hatSchueler) || (routeGostKursplanung.data.auswahlSchueler.id !== idSchueler)) {
			// Setze den neu ausgewählten Schüler-Eintrag
				const schueler = routeGostKursplanung.data.datenmanager.schuelerGetOrNull(idSchueler);
				if (schueler === null)
					return this.getRoute({ idschueler: undefined });
				await routeGostKursplanung.data.setAuswahlSchueler(schueler);
			}
		} catch(e) {
			return routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { idschueler: routeGostKursplanung.data.hatSchueler ? routeGostKursplanung.data.auswahlSchueler.id : undefined };
	}

	gotoSchuelerIndividualdaten = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerIndividualdaten.getRoute({ id: idSchueler }));
	}

	gotoLaufbahnplanung = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute({ id: idSchueler }));
	}

	public getAuswahlProps(to: RouteLocationNormalized): KursplanungSchuelerAuswahlProps {
		return {
			hatBlockung: routeGostKursplanung.data.hatBlockung && routeGostKursplanung.data.hatErgebnis,
			hatErgebnis: routeGostKursplanung.data.hatErgebnis,
			setSchueler: routeGostKursplanung.data.gotoSchueler,
			getErgebnismanager: () => routeGostKursplanung.data.ergebnismanager,
			getDatenmanager: () => routeGostKursplanung.data.datenmanager,
			schueler: routeGostKursplanung.data.hatSchueler ? routeGostKursplanung.data.auswahlSchueler : undefined,
			schuelerFilter: () => routeGostKursplanung.data.schuelerFilter,
			faecherManager: routeGost.data.faecherManager,
			regelnUpdate: routeGostKursplanung.data.regelnUpdate,
			isSchuelerFilterOpen: () => routeGostKursplanung.data.isSchuelerFilterOpen,
			setIsSchuelerFilterOpen: routeGostKursplanung.data.setIsSchuelerFilterOpen,
			showGeschlecht: () => routeGostKursplanung.data.showGeschlecht,
			setShowGeschlecht: routeGostKursplanung.data.setShowGeschlecht,
		}
	}

	public getProps(to: RouteLocationNormalized): GostKursplanungUmwahlansichtProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenAbiturjahrgaenge: api.benutzerKompetenzenAbiturjahrgaenge,
			hatBlockung: routeGostKursplanung.data.hatBlockung && routeGostKursplanung.data.hatErgebnis,
			hatErgebnis: routeGostKursplanung.data.hatErgebnis,
			regelnUpdate: routeGostKursplanung.data.regelnUpdate,
			updateKursSchuelerZuordnungen: routeGostKursplanung.data.updateKursSchuelerZuordnungen,
			autoKursSchuelerZuordnung: routeGostKursplanung.data.autoKursSchuelerZuordnung,
			gotoSchueler: this.gotoSchuelerIndividualdaten,
			gotoLaufbahnplanung: this.gotoLaufbahnplanung,
			getDatenmanager: () => routeGostKursplanung.data.datenmanager,
			getErgebnismanager: () => routeGostKursplanung.data.ergebnismanager,
			schueler: routeGostKursplanung.data.hatSchueler ? routeGostKursplanung.data.auswahlSchueler : undefined,
			apiStatus: api.status,
		}
	}

}

export const routeGostKursplanungSchueler = new RouteGostKursplanungSchueler();
