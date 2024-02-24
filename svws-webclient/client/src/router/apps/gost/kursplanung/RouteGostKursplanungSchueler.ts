import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostUmwahlansichtProps } from "~/components/gost/kursplanung/SCardGostUmwahlansichtProps";
import type { KursplanungSchuelerAuswahlProps } from "~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahlProps";

import { BenutzerKompetenz, GostHalbjahr, Schulform, ServerMode } from "@core";
import { ConfigElement } from "~/components/Config";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeGostKursplanung, type RouteGostKursplanung } from "~/router/apps/gost/kursplanung/RouteGostKursplanung";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";

const SCardGostUmwahlansicht = () => import("~/components/gost/kursplanung/SCardGostUmwahlansicht.vue");
const SGostKursplanungSchuelerAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahl.vue");

export class RouteGostKursplanungSchueler extends RouteNode<unknown, RouteGostKursplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.kursplanung.schueler", "schueler/:idschueler(\\d+)?", SCardGostUmwahlansicht);
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
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : Number(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { abiturjahr: abiturjahr }};
		return false;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		if (abiturjahr === undefined)
			return { name: routeGost.name, params: { } };
		if ((halbjahr === undefined) || (idBlockung === undefined))
			return routeGostKursplanung.getRouteHalbjahr(abiturjahr, (halbjahr === undefined) ? GostHalbjahr.EF1.id : halbjahr.id);
		if (idErgebnis === undefined)
			return routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr.id, idBlockung);
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array || to_params.idschueler instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung und des Ergebnisses
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
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
		const idSchueler = to_params.idschueler === undefined ? undefined : parseInt(to_params.idschueler);
		// ... wurde die ID des Schülers auf undefined setzt, so prüfe, ob die Schülerliste leer ist und wähle ggf. das erste Element aus
		if (idSchueler === undefined) {
			if (routeGostKursplanung.data.mapSchueler.size > 0) {
				const schueler = routeGostKursplanung.data.mapSchueler.values().next().value;
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis, schueler?.id);
			}
			return;
		}
		// ... wurde die ID des Schülers verändert, merke diesen Schüler
		if ((!routeGostKursplanung.data.hatSchueler) || (routeGostKursplanung.data.auswahlSchueler.id !== idSchueler)) {
			// Setze den neu ausgewählten Schüler-Eintrag
			const schuelerEintrag = routeGostKursplanung.data.mapSchueler.get(idSchueler);
			if (schuelerEintrag === undefined)
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis, undefined);
			await routeGostKursplanung.data.setAuswahlSchueler(schuelerEintrag);
		}
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined, idergebnis: number | undefined, idschueler: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idblockung === undefined) || (idergebnis === undefined))
			throw new Error("Abiturjahr, Halbjahr und die ID der Blockung und des Ergebnisses müssen für diese Route definiert sein.");
		if (idschueler === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis, idschueler : idschueler }};
	}

	gotoSchuelerIndividualdaten = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerIndividualdaten.getRoute(idSchueler));
	}

	gotoLaufbahnplanung = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute(idSchueler));
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

	public getProps(to: RouteLocationNormalized): GostUmwahlansichtProps {
		return {
			hatBlockung: routeGostKursplanung.data.hatBlockung && routeGostKursplanung.data.hatErgebnis,
			hatErgebnis: routeGostKursplanung.data.hatErgebnis,
			regelnUpdate: routeGostKursplanung.data.regelnUpdate,
			updateKursSchuelerZuordnung: routeGostKursplanung.data.updateKursSchuelerZuordnung,
			removeKursSchuelerZuordnung: routeGostKursplanung.data.removeKursSchuelerZuordnung,
			autoKursSchuelerZuordnung:  routeGostKursplanung.data.autoKursSchuelerZuordnung,
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
