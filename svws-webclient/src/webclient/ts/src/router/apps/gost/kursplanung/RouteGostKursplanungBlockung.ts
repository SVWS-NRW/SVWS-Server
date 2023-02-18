import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteGostKursplanungHalbjahr, routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { routeGostKursplanungSchueler } from "./RouteGostKursplanungSchueler";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataGostKursplanungBlockung {
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	fachwahlen: List<GostStatistikFachwahl> = new Vector<GostStatistikFachwahl>();
}

const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");
const SGostKursplanungErgebnisAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungErgebnisAuswahl.vue");

export class RouteGostKursplanungBlockung extends RouteNode<RouteDataGostKursplanungBlockung, RouteGostKursplanungHalbjahr> {

	public constructor() {
		super("gost.kursplanung.halbjahr.ergebnis", "ergebnis/:idergebnis(\\d+)?", SGostKursplanung, new RouteDataGostKursplanungBlockung());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_ergebnis_auswahl", SGostKursplanungErgebnisAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung";
		super.children = [
			routeGostKursplanungSchueler
		];
		super.defaultChild = routeGostKursplanungSchueler;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr und Blockungsid dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		if ((abiturjahr === undefined) || (routeGost.data.item.value !== undefined) && (abiturjahr !== routeGost.data.item.value.abiturjahr))
			return { name: routeGost.name, params: { } };
		if ((halbjahr === undefined) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr?.id);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr und Blockungsid dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idBlockung === undefined))
			throw new Error("Fehler: Abiturjahr, Halbjahr und ID der Blockung müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(k => this.data.mapLehrer.set(k.id, k));
		this.data.fachwahlen = await routeLogin.data.api.getGostAbiturjahrgangFachwahlstatistik(routeLogin.data.schema, abiturjahr);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		if ((abiturjahr !== routeGostKursplanung.data.abiturjahr) || (halbjahr !== routeGostKursplanung.data.halbjahr) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr.id);
		if (idBlockung !== routeGostKursplanung.data.auswahlBlockung.id)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		// Prüfe die ID der Ergebnisses
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		// ... wurde die ID des Ergebnisses auf undefined setzt, so prüfe, ob die Ergebnisliste leer ist und wähle ggf. das erste Element aus
		if (idErgebnis === undefined) {
			if ((routeGostKursplanung.data.hatBlockung) && (routeGostKursplanung.data.hatErgebnis)) {
				const ergebnis = routeGostKursplanung.data.datenmanager.getErgebnisseSortiertNachBewertung().get(0);
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, ergebnis.id);
			}
			if ((routeGostKursplanung.data.hatBlockung) && (!routeGostKursplanung.data.hatErgebnis))
				return;
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr.id);
		}
		// ... bestimme den Listen-Eintrag zu dem Ergebnis
		const ergebnisEintrag = routeGostKursplanung.data.datenmanager.getErgebnis(idErgebnis);
		if (ergebnisEintrag === undefined)
			throw new Error("Programmierfehler: Ein Eintrag für die Ergebnis-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
		// ... wurde die ID des Ergebnisses verändert, so setze den neu ausgewählten Ergebnis-Eintrag
		if (routeGostKursplanung.data.auswahlErgebnis.id !== ergebnisEintrag.id)
			await routeGostKursplanung.data.setAuswahlErgebnis(ergebnisEintrag);
		// ... setze die aktuelle Route auf die Schüler-Route, so dass die Auswahl geladen wird.
		if (this.name === to.name)
			return routeGostKursplanungSchueler.getRoute(abiturjahr, halbjahr.id, ergebnisEintrag.blockungID, ergebnisEintrag.id, undefined);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeGostKursplanung.data.setAuswahlErgebnis(undefined);
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined, idergebnis: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idblockung === undefined))
			throw new Error("Abiturjahr, Halbjahr und die ID der Blockung müssen für diese Route definiert sein.");
		if (idergebnis === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			getDatenmanager: () => routeGostKursplanung.data.datenmanager,
			removeErgebnis: routeGostKursplanung.data.removeErgebnis,
			removeErgebnisse: routeGostKursplanung.data.removeErgebnisse,
			ergebnisZuNeueBlockung: routeGostKursplanung.data.ergebnisZuNeueBlockung,
			setAuswahlErgebnis: routeGostKursplanung.data.setAuswahlErgebnis,
			auswahlErgebnis: routeGostKursplanung.data.auswahlErgebnis,
			jahrgangsdaten: routeGostKursplanung.data.jahrgangsdaten,
			halbjahr: routeGostKursplanung.data.halbjahr,
			apiStatus: routeGostKursplanung.data.apiStatus,
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			getDatenmanager: () => routeGostKursplanung.data.datenmanager,
			getErgebnismanager: () => routeGostKursplanung.data.ergebnismanager,
			patchRegel: routeGostKursplanung.data.patchRegel,
			addRegel: routeGostKursplanung.data.addRegel,
			removeRegel: routeGostKursplanung.data.removeRegel,
			updateKursSchienenZuordnung: routeGostKursplanung.data.updateKursSchienenZuordnung,
			patchSchiene: routeGostKursplanung.data.patchSchiene,
			addSchiene: routeGostKursplanung.data.addSchiene,
			removeSchiene: routeGostKursplanung.data.removeSchiene,
			patchKurs: routeGostKursplanung.data.patchKurs,
			addKurs: routeGostKursplanung.data.addKurs,
			removeKurs: routeGostKursplanung.data.removeKurs,
			addKursLehrer: routeGostKursplanung.data.addKursLehrer,
			removeKursLehrer: routeGostKursplanung.data.removeKursLehrer,
			ergebnisHochschreiben: routeGostKursplanung.data.ergebnisHochschreiben,
			ergebnisAktivieren: routeGostKursplanung.data.ergebnisAktivieren,
			schuelerFilter: routeGostKursplanungSchueler.data.schuelerFilter.value,
			faecherManager: routeGost.data.faecherManager.value,
			halbjahr: routeGostKursplanung.data.halbjahr,
			mapLehrer: this.data.mapLehrer,
			fachwahlen: this.data.fachwahlen,
			mapSchueler: routeGostKursplanung.data.mapSchueler
		}
	}

}

export const routeGostKursplanungBlockung = new RouteGostKursplanungBlockung();
