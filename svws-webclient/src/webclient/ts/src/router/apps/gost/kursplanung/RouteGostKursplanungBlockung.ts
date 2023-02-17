import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { GostBlockungsergebnisListeneintrag, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
import { RouteGostKursplanungHalbjahr, routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { Ref, ref } from "vue";
import { routeGostKursplanungSchueler } from "./RouteGostKursplanungSchueler";
import { RouteManager } from "~/router/RouteManager";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataGostKursplanungBlockung {
	ergebnisAuswahl: Ref<GostBlockungsergebnisListeneintrag | undefined> = ref(undefined);
	dataKursblockungsergebnis: DataGostKursblockungsergebnis = new DataGostKursblockungsergebnis();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	fachwahlen: List<GostStatistikFachwahl> = new Vector<GostStatistikFachwahl>();

	setAuswahlErgebnis = async (value: GostBlockungsergebnisListeneintrag | undefined) => {
		if ((value?.id !== this.ergebnisAuswahl.value?.id) && (!RouteManager.isActive())) {
			const idSchueler = routeGostKursplanungSchueler.data.schueler.value?.id;
			if (idSchueler === undefined)
				await RouteManager.doRoute(routeGostKursplanungBlockung.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr,
					routeGostKursplanung.data.halbjahr.id, value?.blockungID, value?.id));
			else
				await RouteManager.doRoute(routeGostKursplanungSchueler.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr,
					routeGostKursplanung.data.halbjahr.id, value?.blockungID, value?.id, idSchueler));
		}
	}

	updateKursSchienenZuordnung = async (idKurs: number, idSchieneAlt: number, idSchieneNeu: number): Promise<boolean> => {
		return await this.dataKursblockungsergebnis.assignKursSchiene(idKurs, idSchieneAlt, idSchieneNeu);
	}

	updateKursSchuelerZuordnung = async (idSchueler: number, idKursNeu: number, idKursAlt: number): Promise<boolean> => {
		return await this.dataKursblockungsergebnis.assignSchuelerKurs(idSchueler, idKursNeu, idKursAlt);
	}

	removeKursSchuelerZuordnung = async (idSchueler: number, idKurs: number): Promise<boolean> => {
		return await this.dataKursblockungsergebnis.removeSchuelerKurs(idSchueler, idKurs);
	}

	autoKursSchuelerZuordnung = async (idSchueler : number) => {
		return await this.dataKursblockungsergebnis.multiAssignSchuelerKurs(idSchueler);
	}

	removeErgebnisse = async (ergebnisse: GostBlockungsergebnisListeneintrag[]) => {
		if (ergebnisse.length <= 0)
			return;
		const reselect = ergebnisse.find(e => e.id === this.ergebnisAuswahl.value?.id);
		if (reselect) {
			// TODO Lade ein anderes, nicht ausgewähltes Ergebnis und lösche erst dann...
		}
		for (const ergebnis of ergebnisse) {
			await routeLogin.data.api.deleteGostBlockungsergebnis(routeLogin.data.schema, ergebnis.id);
		}
	}

	removeErgebnis = async (idErgebnis: number) => {
		await routeLogin.data.api.deleteGostBlockungsergebnis(routeLogin.data.schema, idErgebnis);
	}

	ergebnisZuNeueBlockung = async (idErgebnis: number) => {
		const result = await routeLogin.data.api.dupliziereGostBlockungMitErgebnis(routeLogin.data.schema, idErgebnis);
		await RouteManager.doRoute(routeGostKursplanungHalbjahr.getRoute(result.abijahrgang, result.gostHalbjahr, result.id));
	}

	ergebnisHochschreiben = async () => {
		if (!this.ergebnisAuswahl.value)
			throw new Error("Unerwarteter Fehler: Aktuell ist kein Ergebnis ausgewählt.");
		const abiturjahr = routeGostKursplanung.data.abiturjahr;
		const halbjahr = routeGostKursplanung.data.halbjahr.next()?.id || routeGostKursplanung.data.halbjahr.id;
		const result = await routeLogin.data.api.schreibeGostBlockungsErgebnisHoch(routeLogin.data.schema, this.ergebnisAuswahl.value.id);
		await RouteManager.doRoute(routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr, result.id));
	}

	ergebnisAktivieren = async () => {
		if ((!routeGostKursplanung.data.hatBlockung) ||
			(routeGostKursplanungHalbjahr.data.dataKursblockung.datenmanager === undefined) ||
			(routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnismanager === undefined) ||
			(this.ergebnisAuswahl.value === undefined))
			return false;
		const res = await this.dataKursblockungsergebnis.activate_blockungsergebnis();
		if (!res)
			return false;
		routeGostKursplanung.data.jahrgangsdaten.istBlockungFestgelegt[routeGostKursplanung.data.halbjahr.id] = true;
		routeGostKursplanung.data.auswahlBlockung.istAktiv = true;
		routeGostKursplanungHalbjahr.data.dataKursblockung.datenmanager.daten().istAktiv = true;
		routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnismanager.getErgebnis().istVorlage = true;
		this.ergebnisAuswahl.value.istVorlage = true;
		return true;
	}

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
		// notwendig, damit der Ergebnis-Manager initialisiert werden kann
		this.data.dataKursblockungsergebnis.dataKursblockung = routeGostKursplanungHalbjahr.data.dataKursblockung;
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
			if ((routeGostKursplanungHalbjahr.data.dataKursblockung.daten !== undefined) && (routeGostKursplanungHalbjahr.data.dataKursblockung.daten.ergebnisse.size() > 0)) {
				const ergebnis = routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnisse().get(0);
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, ergebnis.id);
			}
			if (this.data.ergebnisAuswahl.value !== undefined) {
				await this.data.dataKursblockungsergebnis.unselect();
				this.data.ergebnisAuswahl.value = undefined;
			}
			return;
		}
		// ... bestimme den Listen-Eintrag zu dem Ergebnis
		const ergebnisEintrag = (routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnisse().toArray() as GostBlockungsergebnisListeneintrag[]).find(e => e.id === idErgebnis);
		if (ergebnisEintrag === undefined)
			throw new Error("Programmierfehler: Ein Eintrag für die Ergebnis-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
		// ... wurde die ID des Ergebnisses verändert, so lade das neue Ergebnis aus der Datenbank
		if (this.data.ergebnisAuswahl.value?.id !== idBlockung) {
			// Setze den neu ausgewählten Ergebnis-Eintrag
			this.data.ergebnisAuswahl.value = ergebnisEintrag;
			// Lade die neuen Ergebnisdaten
			await this.data.dataKursblockungsergebnis.select(ergebnisEintrag);
			const ergebnis = this.data.dataKursblockungsergebnis.daten;
			if (ergebnis === undefined)
				throw new Error("Fehler beim Laden der Blockungs-Ergebnisdaten für die Ergebnis-ID als Parameter der Route.");
		}
		// ... setze die aktuelle Route auf die Schüler-Route, so dass die Auswahl geladen wird.
		if (this.name === to.name)
			return routeGostKursplanungSchueler.getRoute(abiturjahr, halbjahr.id, ergebnisEintrag.blockungID, ergebnisEintrag.id, undefined);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		this.data.ergebnisAuswahl.value = undefined;
		await this.data.dataKursblockungsergebnis.unselect();
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
			getDatenmanager: () => routeGostKursplanungHalbjahr.data.dataKursblockung.datenmanager,
			removeErgebnis: this.data.removeErgebnis,
			removeErgebnisse: this.data.removeErgebnisse,
			ergebnisZuNeueBlockung: this.data.ergebnisZuNeueBlockung,
			setAuswahlErgebnis: this.data.setAuswahlErgebnis,
			auswahlErgebnis: this.data.ergebnisAuswahl.value,
			jahrgangsdaten: routeGostKursplanung.data.jahrgangsdaten,
			halbjahr: routeGostKursplanung.data.halbjahr,
			blockung: routeGostKursplanungHalbjahr.data.dataKursblockung,
			pending: routeGostKursplanungHalbjahr.data.dataKursblockung.pending,
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			getDatenmanager: () => routeGostKursplanungHalbjahr.data.dataKursblockung.datenmanager,
			getErgebnismanager: () => routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnismanager,
			patchRegel: routeGostKursplanungHalbjahr.data.patchRegel,
			addRegel: routeGostKursplanungHalbjahr.data.addRegel,
			removeRegel: routeGostKursplanungHalbjahr.data.removeRegel,
			updateKursSchienenZuordnung: this.data.updateKursSchienenZuordnung,
			patchSchiene: routeGostKursplanungHalbjahr.data.patchSchiene,
			addSchiene: routeGostKursplanungHalbjahr.data.addSchiene,
			removeSchiene: routeGostKursplanungHalbjahr.data.removeSchiene,
			patchKurs: routeGostKursplanungHalbjahr.data.patchKurs,
			addKurs: routeGostKursplanungHalbjahr.data.addKurs,
			removeKurs: routeGostKursplanungHalbjahr.data.removeKurs,
			addKursLehrer: routeGostKursplanungHalbjahr.data.addKursLehrer,
			removeKursLehrer: routeGostKursplanungHalbjahr.data.removeKursLehrer,
			ergebnisHochschreiben: this.data.ergebnisHochschreiben,
			ergebnisAktivieren: this.data.ergebnisAktivieren,
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
