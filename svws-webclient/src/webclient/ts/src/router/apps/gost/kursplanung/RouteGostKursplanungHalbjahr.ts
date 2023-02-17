import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { GostBlockungKurs, GostBlockungListeneintrag, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdaten, GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
import { RouteGostKursplanung, routeGostKursplanung } from "../RouteGostKursplanung";
import { routeGostKursplanungBlockung } from "./RouteGostKursplanungBlockung";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataGostKursplanungHalbjahr  {

	dataKursblockung: DataGostKursblockung = new DataGostKursblockung();

	setAuswahlBlockung = async (auswahl: GostBlockungListeneintrag | undefined): Promise<void> => {
		if (auswahl !== undefined)
			await routeGostKursplanung.data.gotoBlockung(auswahl?.id);
	}

	patchBlockung = async (data: Partial<GostBlockungsdaten>, idBlockung: number): Promise<boolean> => {
		if (this.dataKursblockung.daten === undefined)
			return false;
		await routeLogin.data.api.patchGostBlockung(data, routeLogin.data.schema, idBlockung);
		return true;
	}

	addRegel = async (regel: GostBlockungRegel) => {
		return await this.dataKursblockung.add_blockung_regel(regel);
	}

	removeRegel = async (id: number) => {
		return await this.dataKursblockung.del_blockung_regel(id);
	}

	patchRegel = async (data: Partial<GostBlockungRegel>, idRegel: number) => {
		this.dataKursblockung.pending = true;
		await routeLogin.data.api.patchGostBlockungRegel(data, routeLogin.data.schema, idRegel);
		this.dataKursblockung.pending = false;
	}

	patchKurs = async(data: Partial<GostBlockungKurs>, kurs_id: number) => {
		await this.dataKursblockung.patch_kurs(kurs_id, data);
	}

	addKurs = async (fach_id : number, kursart_id : number) => {
		return await this.dataKursblockung.add_blockung_kurse(fach_id, kursart_id);
	}

	removeKurs = async (fach_id : number, kursart_id : number) => {
		return await this.dataKursblockung.del_blockung_kurse(fach_id, kursart_id);
	}

	addKursLehrer = async(kurs_id: number, lehrer_id: number) => {
		return await this.dataKursblockung.add_blockung_lehrer(kurs_id, lehrer_id);
	}

	removeKursLehrer = async(kurs_id: number, lehrer_id: number) => {
		await this.dataKursblockung.del_blockung_lehrer(kurs_id, lehrer_id);
	}

	patchSchiene = async (data: Partial<GostBlockungSchiene>, id : number) => {
		await this.dataKursblockung.patch_schiene(data, id);
	}

	addSchiene = async () => {
		return await this.dataKursblockung.add_blockung_schiene();
	}

	removeSchiene = async (s: GostBlockungSchiene) => {
		return await this.dataKursblockung.del_blockung_schiene(s);
	}

}

const SGostKursplanungEmptyErgebnis = () => import("~/components/gost/kursplanung/SGostKursplanungEmptyErgebnis.vue");
const SGostKursplanungBlockungAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungBlockungAuswahl.vue");

export class RouteGostKursplanungHalbjahr extends RouteNode<RouteDataGostKursplanungHalbjahr, RouteGostKursplanung> {

	public constructor() {
		super("gost.kursplanung.halbjahr", "blockung/:idblockung(\\d+)?", SGostKursplanungEmptyErgebnis, new RouteDataGostKursplanungHalbjahr());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_blockung_auswahl", SGostKursplanungBlockungAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung Halbjahresauswahl";
		super.children = [
			routeGostKursplanungBlockung
		];
		super.defaultChild = routeGostKursplanungBlockung;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (routeGost.data.item.value !== undefined) && (abiturjahr !== routeGost.data.item.value.abiturjahr))
			return { name: routeGost.name, params: { } };
		if (halbjahr === undefined)
			return routeGostKursplanung.getRoute(abiturjahr, undefined);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe nochmals Abiturjahrgang und Halbjahr
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		await routeGostKursplanung.data.setAbiturjahr(abiturjahr);
		await routeGostKursplanung.data.setHalbjahr(halbjahr);
		// Prüfe die ID der Blockung
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung as string);
		// ... wurde die ID der Blockung auf undefined gesetzt, so prüfe, ob die Blockungsliste leer ist und wähle ggf. das erste Element aus
		if (idBlockung === undefined) {
			if ((idBlockung === undefined) && (routeGostKursplanung.data.mapBlockungen.size > 0)) {
				const blockungsEintrag = routeGostKursplanung.data.mapBlockungen.values().next().value;
				return this.getRoute(abiturjahr, halbjahr.id, blockungsEintrag.id);
			}
			if (routeGostKursplanung.data.hatBlockung) {
				await this.data.dataKursblockung.unselect();
				await routeGostKursplanung.data.setAuswahlBlockung(undefined);
			}
			return;
		}
		// ... exisitiert die Blockung mit der ID überhaupt? Wenn nicht, muss die Route abgelehnt werden und umgeleitet werden
		if (routeGostKursplanung.data.mapBlockungen.size === 0)
			return routeGostKursplanung.data.gotoHalbjahr(routeGostKursplanung.data.halbjahr);
		// ... wurde die ID der Blockung verändert, so lade die neue Blockung aus der Datenbank
		if (routeGostKursplanung.data.auswahlBlockung.id !== idBlockung) {
			// Setze den neu ausgewählten Blockungs-Eintrag
			const blockungsEintrag = routeGostKursplanung.data.mapBlockungen.get(idBlockung);
			if (blockungsEintrag === undefined)
				throw new Error("Programmierfehler: Ein Eintrag für die Blockungs-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
			await routeGostKursplanung.data.setAuswahlBlockung(blockungsEintrag);
			// Lade die neuen Blockungsdaten
			await this.data.dataKursblockung.select(blockungsEintrag);
			const blockung = this.data.dataKursblockung.daten;
			if (blockung === undefined)
				throw new Error("Fehler beim Laden der Blockungsdaten für die Blockungs-ID als Parameter der Route.");
		}
		// ... prüfe, wenn diese Route das aktuelle Zeil ist, ob Daten vorhanden sind und damit ein Ergebnis existiert, welches selektiert werden kann
		if ((this.name === to.name) && (this.data.dataKursblockung.daten !== undefined) && (this.data.dataKursblockung.daten.ergebnisse.size() > 0)) {
			const blid = routeGostKursplanung.data.auswahlBlockung.id;
			const ergebnis = this.data.dataKursblockung.ergebnisse().get(0);
			return routeGostKursplanungBlockung.getRoute(abiturjahr, halbjahr.id, blid, ergebnis.id);
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeGostKursplanung.data.setAuswahlBlockung(undefined);
		await this.data.dataKursblockung.unselect();
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Abiturjahr und Halbjahr müssen für diese Route definiert sein.");
		if (idblockung === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			removeBlockung: routeGostKursplanung.data.removeBlockung,
			patchBlockung: this.data.patchBlockung,
			setAuswahlBlockung: this.data.setAuswahlBlockung,
			auswahlBlockung: routeGostKursplanung.data.auswahlBlockung,
			mapBlockungen: routeGostKursplanung.data.mapBlockungen,
			jahrgangsdaten: routeGost.data.jahrgangsdaten.value,
			halbjahr: routeGostKursplanung.data.halbjahr,
			pending: this.data.dataKursblockung.pending
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			hatBlockung: routeGostKursplanung.data.mapBlockungen.size > 0
		}
	}

}

export const routeGostKursplanungHalbjahr = new RouteGostKursplanungHalbjahr();
