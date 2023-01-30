import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { GostBlockungListeneintrag, GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, useRouter } from "vue-router";
import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
import { RouteGostKursplanung, routeGostKursplanung } from "../RouteGostKursplanung";
import { routeGostKursplanungBlockung } from "./RouteGostKursplanungBlockung";
import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";
import { computed, WritableComputedRef } from "vue";

export class RouteDataGostKursplanungHalbjahr  {
	listBlockungen: ListKursblockungen = new ListKursblockungen();
	dataKursblockung: DataGostKursblockung = new DataGostKursblockung();
}

const SGostKursplanungEmptyErgebnis = () => import("~/components/gost/kursplanung/SGostKursplanungEmptyErgebnis.vue");
const SGostKursplanungBlockungAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungBlockungAuswahl.vue");

export class RouteGostKursplanungHalbjahr extends RouteNode<RouteDataGostKursplanungHalbjahr, RouteGostKursplanung> {

	public constructor() {
		super("gost_kursplanung_halbjahr", "blockung/:idblockung(\\d+)?", SGostKursplanungEmptyErgebnis, new RouteDataGostKursplanungHalbjahr());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_blockung_auswahl", SGostKursplanungBlockungAuswahl, (route) => this.getProps(route));
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
		await this.data.listBlockungen.update_list(abiturjahr, halbjahr);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe nochmals Abiturjahrgang und Halbjahr
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		if ((abiturjahr !== this.data.listBlockungen.abiturjahr) || (halbjahr !== this.data.listBlockungen.halbjahr)) {
			await this.data.listBlockungen.update_list(abiturjahr, halbjahr);
			if ((abiturjahr !== this.data.listBlockungen.abiturjahr) || (halbjahr !== this.data.listBlockungen.halbjahr))
				return false;
		}
		// Prüfe die ID der Blockung
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung as string);
		// ... wurde die ID der Blockung auf undefined setzt, so prüfe, ob die Blockungsliste leer ist und wähle ggf. das erste Element aus
		if (idBlockung === undefined) {
			if ((idBlockung === undefined) && (this.data.listBlockungen.liste.length > 0))
				return this.getRoute(abiturjahr, halbjahr.id, this.data.listBlockungen.liste.at(0)?.id);
			if (this.data.listBlockungen.ausgewaehlt !== undefined) {
				await this.data.dataKursblockung.unselect();
				this.data.listBlockungen.ausgewaehlt = undefined;
			}
			return;
		}
		// ... wurde die ID der Blockung verändert, so lade die neue Blockung aus der Datenbank
		if (this.data.listBlockungen.ausgewaehlt?.id !== idBlockung) {
			// Lade die Liste der Blockungen neu
			await this.data.listBlockungen.update_list(abiturjahr, halbjahr);
			// Setze den neu ausgewählten Blockungs-Eintrag
			const blockungsEintrag = this.data.listBlockungen.liste.find(b => b.id === idBlockung);
			if (blockungsEintrag === undefined)
				throw new Error("Programmierfehler: Ein Eintrag für die Blockungs-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
			this.data.listBlockungen.ausgewaehlt = blockungsEintrag;
			// Lade die neuen Blockungsdaten
			await this.data.dataKursblockung.select(blockungsEintrag);
			const blockung = this.data.dataKursblockung.daten;
			if (blockung === undefined)
				throw new Error("Fehler beim Laden der Blockungsdaten für die Blockungs-ID als Parameter der Route.");
		}
		// ... prüfe, wenn diese Route das aktuelle Zeil ist, ob Daten vorhanden sind und damit ein Ergebnis existiert, welches selektiert werden kann
		if ((this.name === to.name) && (this.data.dataKursblockung.daten !== undefined) && (this.data.dataKursblockung.daten.ergebnisse.size() > 0)) {
			const blid = this.data.listBlockungen.ausgewaehlt?.id;
			const ergebnis = this.data.dataKursblockung.ergebnisse().get(0);
			return routeGostKursplanungBlockung.getRoute(abiturjahr, halbjahr.id, blid, ergebnis.id);
		}
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Abiturjahr und Halbjahr müssen für diese Route definiert sein.");
		if (idblockung === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeGostKursplanung.getProps(to),
			listBlockungen: this.data.listBlockungen,
			blockung: this.data.dataKursblockung
		}
	}

	public getSelector(onSet?: (value : GostBlockungListeneintrag | undefined) => void) : WritableComputedRef<GostBlockungListeneintrag | undefined> {
		const router = useRouter();
		return computed({
			get: () => this.data.listBlockungen.ausgewaehlt,
			set: (value) => {
				if (onSet !== undefined)
					onSet(value);
				if (value !== this.data.listBlockungen.ausgewaehlt)
					void router.push(this.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr, routeGostKursplanung.data.halbjahr.value.id, value?.id));
			}
		});
	}

}

export const routeGostKursplanungHalbjahr = new RouteGostKursplanungHalbjahr();
