import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { GostHalbjahr, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { RouteGostKursplanungBlockung, routeGostKursplanungBlockung } from "./RouteGostKursplanungBlockung";
import { Ref, ref, ShallowRef, shallowRef } from "vue";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeLogin } from "~/router/RouteLogin";
import { GostKursplanungSchuelerFilter } from "~/components/gost/kursplanung/GostKursplanungSchuelerFilter";

export class RouteDataGostKursplanungSchueler {

	idErgebnis: number | undefined = undefined;
	schueler: ShallowRef<SchuelerListeEintrag | undefined> = shallowRef(undefined);
	mapSchueler: Ref<Map<number, SchuelerListeEintrag>> = ref(new Map());
	schuelerFilter: ShallowRef<GostKursplanungSchuelerFilter | undefined> = shallowRef(undefined);

	setSchueler = async (schueler: SchuelerListeEintrag) => {
		if (schueler.id !== this.schueler.value?.id)
			void RouteManager.doRoute(routeGostKursplanungSchueler.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr, routeGostKursplanung.data.halbjahr.value.id,
				routeGostKursplanungHalbjahr.data.dataKursblockung.daten?.id, routeGostKursplanungBlockung.data.ergebnis.value?.id, schueler.id));
	}

	gotoSchueler = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerIndividualdaten.getRoute(idSchueler));
	}

	gotoLaufbahnplanung = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute(idSchueler));
	}

}

const SCardGostUmwahlansicht = () => import("~/components/gost/kursplanung/SCardGostUmwahlansicht.vue");
const SGostKursplanungSchuelerAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahl.vue");

export class RouteGostKursplanungSchueler extends RouteNode<RouteDataGostKursplanungSchueler, RouteGostKursplanungBlockung> {

	public constructor() {
		super("gost.kursplanung.halbjahr.ergebnis.schueler", "schueler/:idschueler(\\d+)?", SCardGostUmwahlansicht, new RouteDataGostKursplanungSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_schueler_auswahl", SGostKursplanungSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung - Schüler";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		if ((abiturjahr === undefined) || (routeGost.data.item.value !== undefined) && (abiturjahr !== routeGost.data.item.value.abiturjahr))
			return { name: routeGost.name, params: { } };
		if ((halbjahr === undefined) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr?.id);
		if (idErgebnis === undefined)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idBlockung === undefined) || (idErgebnis === undefined))
			throw new Error("Fehler: Abiturjahr, Halbjahr und ID der Blockung und des Ergebnisses müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		// Lade die Schülerliste
		const listSchueler = await routeLogin.data.api.getGostAbiturjahrgangSchueler(routeLogin.data.schema, abiturjahr);
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listSchueler)
			mapSchueler.set(s.id, s);
		this.data.mapSchueler.value = mapSchueler;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array || to_params.idschueler instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung und des Ergebnisses
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		if ((abiturjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.abiturjahr) || (halbjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.halbjahr) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr.id);
		if (idBlockung !== routeGostKursplanungHalbjahr.data.listBlockungen.ausgewaehlt?.id)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		if (idErgebnis !== routeGostKursplanungBlockung.data.dataKursblockungsergebnis?.daten?.id)
			routeGostKursplanungBlockung.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis);
		const idSchueler = to_params.idschueler === undefined ? undefined : parseInt(to_params.idschueler);
		// ... wurde die ID des Schülers auf undefined setzt, so prüfe, ob die Schülerliste leer ist und wähle ggf. das erste Element aus
		if (idSchueler === undefined) {
			if (this.data.mapSchueler.value.size > 0) {
				const schueler = this.data.mapSchueler.value.values().next().value;
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis, schueler?.id);
			}
			return;
		}
		// ... wurde das Blockungsergebnis verändert, so muss der Schüler-Filter neu initialisier werden
		if (this.data.idErgebnis !== idErgebnis) {
			this.data.schuelerFilter.value = new GostKursplanungSchuelerFilter(routeGostKursplanungHalbjahr.data.dataKursblockung.datenmanager,
				routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnismanager, routeGost.data.faecherManager.value.toVector(), this.data.mapSchueler.value);
			this.data.idErgebnis = idErgebnis;
		}
		// ... wurde die ID des Schülers verändert, merke diesen Schüler
		if (this.data.schueler.value?.id !== idSchueler) {
			// Setze den neu ausgewählten Schüler-Eintrag
			const schuelerEintrag = this.data.mapSchueler.value.get(idSchueler);
			if (schuelerEintrag === undefined)
				throw new Error("Programmierfehler: Ein Eintrag für die Schüler-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
			this.data.schueler.value = schuelerEintrag;
		}
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined, idergebnis: number | undefined, idschueler: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idblockung === undefined) || (idergebnis === undefined))
			throw new Error("Abiturjahr, Halbjahr und die ID der Blockung und des Ergebnisses müssen für diese Route definiert sein.");
		if (idschueler === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis, idschueler : idschueler }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			setSchueler: this.data.setSchueler,
			schueler: this.data.schueler.value,
			schuelerFilter: this.data.schuelerFilter.value!,
			faecherManager: routeGost.data.faecherManager.value,
			blockung: routeGostKursplanungHalbjahr.data.dataKursblockung
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			updateKursSchuelerZuordnung: routeGostKursplanungBlockung.data.updateKursSchuelerZuordnung,
			removeKursSchuelerZuordnung: routeGostKursplanungBlockung.data.removeKursSchuelerZuordnung,
			autoKursSchuelerZuordnung:  routeGostKursplanungBlockung.data.autoKursSchuelerZuordnung,
			gotoSchueler: this.data.gotoSchueler,
			gotoLaufbahnplanung: this.data.gotoLaufbahnplanung,
			blockung: routeGostKursplanungHalbjahr.data.dataKursblockung,
			schueler: this.data.schueler.value,
			pending: routeGostKursplanungBlockung.data.dataKursblockungsergebnis.pending,
		}
	}

}

export const routeGostKursplanungSchueler = new RouteGostKursplanungSchueler();
