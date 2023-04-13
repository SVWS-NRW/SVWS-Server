import { RouteNode } from "~/router/RouteNode";
import type { RouteGost} from "~/router/apps/RouteGost";
import { routeGost } from "~/router/apps/RouteGost";
import { BenutzerKompetenz, GostHalbjahr, Schulform } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeApp } from "~/router/RouteApp";
import { RouteDataGostKursplanung } from "./kursplanung/RouteDataGostKursplanung";
import { routeGostKursplanungSchueler } from "./kursplanung/RouteGostKursplanungSchueler";
import { api } from "~/router/Api";
import type { GostKursplanungProps } from "~/components/gost/kursplanung/SGostKursplanungProps";
import type { GostKursplanungAuswahlProps } from "~/components/gost/kursplanung/SGostKursplanungAuswahlProps";
import { ConfigElement } from "~/components/Config";


const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");
const SGostKursplanungAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungAuswahl.vue");

export class RouteGostKursplanung extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.kursplanung", "kursplanung/:halbjahr([0-5])?/:idblockung(\\d+)?/:idergebnis(\\d+)?", SGostKursplanung, new RouteDataGostKursplanung());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_child_auswahl", SGostKursplanungAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung";
		super.children = [
			routeGostKursplanungSchueler
		];
		super.defaultChild = routeGostKursplanungSchueler;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.kursansicht.sortierung", "user", "kursart")
		]);
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? undefined : parseInt(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = !to_params.idblockung ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = !to_params.idergebnis ? undefined : parseInt(to_params.idergebnis);
		if (abiturjahr === undefined || abiturjahr === -1)
			return this.getRoute();
		if (halbjahr === undefined) {
			let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt);
			if (hj === null) // In zwei Fällen existiert kein Planungshalbjahr, z.B. weil der Abiturjahrgang (fast) abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
			return this.getRouteHalbjahr(abiturjahr, hj.id);
		}
		if ((idBlockung === undefined) && (idErgebnis !== undefined))
			return this.getRouteHalbjahr(abiturjahr, halbjahr.id);
		return true;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = !to_params.idblockung ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = !to_params.idergebnis ? undefined : parseInt(to_params.idergebnis);
		// Prüfe den Abiturjahrgang und setze diesen ggf.
		if (abiturjahr === undefined)
			throw new Error("Fehler: Der Abiturjahrgang darf an dieser Stelle nicht undefined sein.");
		await this.data.setAbiturjahr(abiturjahr);
		// Prüfe das Halbjahr und setzte dieses ggf.
		if (halbjahr === undefined) {
			let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt);
			if (hj === null) // In zwei Fällen existiert kein Planungshalbjahr, z.B. weil der Abiturjahrgang (fast) abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
			return this.getRouteHalbjahr(abiturjahr, hj.id);
		}
		await this.data.setHalbjahr(halbjahr);
		const changedHalbjahr: boolean = await this.data.setHalbjahr(halbjahr);
		if (changedHalbjahr)
			return this.getRouteHalbjahr(abiturjahr, halbjahr.id);
		// Prüfe die Blockung und setzte diese ggf.
		if (idBlockung === undefined) {
			// ... wurde die ID der Blockung auf undefined gesetzt, so prüfe, ob die Blockungsliste leer ist und wähle ggf. das erste Element aus
			if ((idBlockung === undefined) && (this.data.mapBlockungen.size > 0)) {
				const blockungsEintrag = this.data.mapBlockungen.values().next().value;
				return this.getRouteBlockung(abiturjahr, halbjahr.id, blockungsEintrag.id);
			}
			if (this.data.hatBlockung)
				await this.data.setAuswahlBlockung(undefined);
			return; // akzeptiere die Route, da keine Blockung für den Abiturjahrgang und das Halbjahr vorhanden ist.
		}
		const blockungsEintrag = this.data.mapBlockungen.get(idBlockung);
		if (blockungsEintrag === undefined) {
			// ... eine Blockung mit der ID ist nicht vorhanden. Die Route wird abgelehnt und es findet eine Umleitung statt
			// TODO sollte z.B. nach Anlegen einer Ableitung die neue ID trotzdem erreichen können
			return this.getRouteHalbjahr(abiturjahr, halbjahr.id);
		}
		if (!this.data.hatBlockung || (this.data.auswahlBlockung.id !== blockungsEintrag.id)) {
			// ... wurde die ID der Blockung verändert, so setze den neu ausgewählten Blockungs-Eintrag und aktualisiere ggf. die Route
			await this.data.setAuswahlBlockung(blockungsEintrag);
			if (idErgebnis === undefined) {
				if (this.data.ergebnisse.size() <= 0)
					throw new Error("Fehler bei der Blockung. Es muss bei einer Blockung immer mindestens das Vorlagen-Ergebnis vorhanden sein.");
				// ...wenn kein Ergebnis in der Route gesetzt wurde, aber ein Ergebnis existiert, dann setze die Route neu auf das Vorlagen-Ergebnis und ggf. auf den aktuellen Schüler
				if (this.data.hatSchueler)
					return this.getRouteSchueler(abiturjahr, halbjahr.id, blockungsEintrag.id, this.data.auswahlErgebnis.id, this.data.auswahlSchueler.id);
				return this.getRouteErgebnis(abiturjahr, halbjahr.id, blockungsEintrag.id, this.data.auswahlErgebnis.id);
			}
		}
		// Prüfe das Blockungsergebnis und setzte dieses ggf.
		if (idErgebnis === undefined) {
			// ... wurde die ID des Ergebnisses auf undefined setzt, so prüfe, ob die Ergebnisliste leer ist und wähle ggf. das erste Element aus
			if ((this.data.hatBlockung) && (this.data.ergebnisse.size() > 0)) {
				const ergebnis = this.data.datenmanager.getErgebnisseSortiertNachBewertung().get(0);
				return this.getRouteErgebnis(abiturjahr, halbjahr.id, idBlockung, ergebnis.id);
			}
			if ((this.data.hatBlockung) && (this.data.ergebnisse.size() <= 0))
				return;   // akzeptiere die Route, da kein Ergebnis vorhanden ist - sollt eigentlich nicht vorkommen, da ein Vorlagenergebnis notwendig ist
			return this.getRouteHalbjahr(abiturjahr, halbjahr.id); // Es existiert keine Blockung, also route zu der Halbjahresauswahl
		}
		let ergebnisEintrag;
		try {
			ergebnisEintrag = routeGostKursplanung.data.datenmanager.getErgebnis(idErgebnis);
		} catch (e) {
			// ...wenn die Ergebnis-ID ungültig ist, dann setze ggf. das erste Ergebnis und route dahin
			if (this.data.ergebnisse.size() <= 0)
				throw new Error("Fehler bei der Blockung. Es muss bei einer Blockung immer mindestens das Vorlagen-Ergebnis vorhanden sein.");
			const ergebnis = this.data.datenmanager.getErgebnisseSortiertNachBewertung().get(0);
			return this.getRouteErgebnis(abiturjahr, halbjahr.id, idBlockung, ergebnis.id);
		}
		if (routeGostKursplanung.data.auswahlErgebnis.id !== ergebnisEintrag.id) {
			// ... wurde die ID des Ergebnisses verändert, so setze den neu ausgewählten Ergebnis-Eintrag
			await routeGostKursplanung.data.setAuswahlErgebnis(ergebnisEintrag);
			if (this.data.hatSchueler)
				return this.getRouteSchueler(abiturjahr, halbjahr.id, blockungsEintrag.id, this.data.auswahlErgebnis.id, this.data.auswahlSchueler.id);
			return this.getRouteErgebnis(abiturjahr, halbjahr.id, blockungsEintrag.id, this.data.auswahlErgebnis.id);
		}
		// Setze die aktuelle Route auf die Schüler-Route, so dass die Auswahl geladen wird.
		if (this.name === to.name)
			return routeGostKursplanungSchueler.getRoute(abiturjahr, halbjahr.id, ergebnisEintrag.blockungID, ergebnisEintrag.id, undefined);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		routeGost.data.params = from_params;
	}

	public getRoute() : RouteLocationRaw {
		return { name: routeGost.name, params: { abiturjahr: -1 }};
	}

	public getRouteHalbjahr(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getRouteBlockung(abiturjahr: number, halbjahr: number, idblockung: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung }};
	}

	public getRouteErgebnis(abiturjahr: number, halbjahr: number, idblockung: number, idergebnis: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis }};
	}

	public getRouteSchueler(abiturjahr: number, halbjahr: number, idblockung: number, idergebnis: number, idschueler: number) : RouteLocationRaw {
		return { name: routeGostKursplanungSchueler.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis, idschueler : idschueler }};
	}


	public getAuswahlProps(to: RouteLocationNormalized): GostKursplanungAuswahlProps {
		return {
			// Für die Halbjahresauswahl
			setHalbjahr: this.data.gotoHalbjahr,
			jahrgangsdaten: this.data.jahrgangsdaten,
			halbjahr: this.data.halbjahr,
			// ... und zusätzlich für die Blockungsauswahl
			addBlockung: this.data.addBlockung,
			removeBlockung: this.data.removeBlockung,
			patchBlockung: this.data.patchBlockung,
			setAuswahlBlockung: this.data.gotoBlockung,
			auswahlBlockung: this.data.hatBlockung ? this.data.auswahlBlockung : undefined,
			mapBlockungen: () => this.data.mapBlockungen,
			apiStatus: api.status,
			// ... und zusätzlich für die Ergebnisauswahl
			getDatenmanager: () => this.data.datenmanager,
			rechneGostBlockung: this.data.rechneGostBlockung,
			removeErgebnisse: this.data.removeErgebnisse,
			ergebnisZuNeueBlockung: this.data.ergebnisZuNeueBlockung,
			setAuswahlErgebnis: this.data.setAuswahlErgebnis,
			hatBlockung: this.data.hatBlockung,
			auswahlErgebnis: this.data.hatErgebnis ? this.data.auswahlErgebnis : undefined,
		}
	}

	public getProps(to: RouteLocationNormalized): GostKursplanungProps {
		return {
			config: api.config,
			hatBlockung: this.data.hatBlockung && this.data.hatErgebnis,
			getDatenmanager: () => this.data.datenmanager,
			hatErgebnis: this.data.hatErgebnis,
			getErgebnismanager: () => this.data.ergebnismanager,
			patchRegel: this.data.patchRegel,
			addRegel: this.data.addRegel,
			removeRegel: this.data.removeRegel,
			updateKursSchienenZuordnung: this.data.updateKursSchienenZuordnung,
			patchSchiene: this.data.patchSchiene,
			addSchiene: this.data.addSchiene,
			removeSchiene: this.data.removeSchiene,
			patchKurs: this.data.patchKurs,
			addKurs: this.data.addKurs,
			removeKurs: this.data.removeKurs,
			addKursLehrer: this.data.addKursLehrer,
			removeKursLehrer: this.data.removeKursLehrer,
			addSchieneKurs: this.data.addSchieneKurs,
			removeSchieneKurs: this.data.removeSchieneKurs,
			ergebnisHochschreiben: this.data.ergebnisHochschreiben,
			ergebnisAktivieren: this.data.ergebnisAktivieren,
			schuelerFilter: this.data.hatErgebnis ? this.data.schuelerFilter : undefined,
			faecherManager: routeGost.data.faecherManager,
			halbjahr: this.data.halbjahr,
			mapLehrer: this.data.mapLehrer,
			mapFachwahlStatistik: this.data.mapFachwahlStatistik,
			mapSchueler: this.data.mapSchueler
		}
	}

}

export const routeGostKursplanung = new RouteGostKursplanung();
