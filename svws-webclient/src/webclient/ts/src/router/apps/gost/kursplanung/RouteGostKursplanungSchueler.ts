import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { GostHalbjahr, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, useRouter } from "vue-router";
import { routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { RouteGostKursplanungBlockung, routeGostKursplanungBlockung } from "./RouteGostKursplanungBlockung";
import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";
import { DataSchuelerLaufbahndaten } from "~/apps/gost/DataSchuelerLaufbahnplanung";
import { computed, WritableComputedRef } from "vue";

export class RouteDataGostKursplanungSchueler {
	listSchueler: ListAbiturjahrgangSchueler = new ListAbiturjahrgangSchueler();
	dataSchueler: DataSchuelerLaufbahndaten = new DataSchuelerLaufbahndaten();
}

const SCardGostUmwahlansicht = () => import("~/components/gost/kursplanung/SCardGostUmwahlansicht.vue");
const SGostKursplanungSchuelerAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungSchuelerAuswahl.vue");

export class RouteGostKursplanungSchueler extends RouteNode<RouteDataGostKursplanungSchueler, RouteGostKursplanungBlockung> {

	public constructor() {
		super("gost_kursplanung_halbjahr_ergebnis_schueler", "schueler/:idschueler(\\d+)?", SCardGostUmwahlansicht, new RouteDataGostKursplanungSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_kursplanung_schueler_auswahl", SGostKursplanungSchuelerAuswahl, (route) => this.getProps(route));
		super.text = "Kursplanung - Schüler";
		this.isHidden = (params: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params: RouteParams) {
		const abiturjahr = params.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr as string);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(Number(parseInt(to_params.halbjahr as string))) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung as string);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis as string);
		if ((abiturjahr === undefined) || (abiturjahr !== routeGost.data.item.value?.abiturjahr))
			return { name: routeGost.name, params: { } };
		if ((halbjahr === undefined) || (halbjahr.id !== routeGostKursplanung.data.halbjahr.value.id) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr?.id);
		if (idErgebnis === undefined)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr as string);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(Number(parseInt(to_params.halbjahr as string))) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung as string);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis as string);
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idBlockung === undefined) || (idErgebnis === undefined))
			throw new Error("Fehler: Abiturjahr, Halbjahr und ID der Blockung und des Ergebnisses müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		// notwendig, damit der Filter für die Schülerliste genutzt werden kann
		this.data.listSchueler.dataKursblockung = routeGostKursplanungHalbjahr.data.dataKursblockung;
		// Lade die Schülerliste
		await this.data.listSchueler.update_list(abiturjahr);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung und des Ergebnisses
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr as string);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(Number(parseInt(to_params.halbjahr as string))) || undefined;
		const idBlockung = to_params.idblockung === undefined ? undefined : parseInt(to_params.idblockung as string);
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis as string);
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		if ((abiturjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.abiturjahr) || (halbjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.halbjahr) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr.id);
		if (idBlockung !== routeGostKursplanungHalbjahr.data.listBlockungen.ausgewaehlt?.id)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		if (idErgebnis !== routeGostKursplanungBlockung.data.dataKursblockungsergebnis?.daten?.id)
			routeGostKursplanungBlockung.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis);
		const idSchueler = to_params.idschueler === undefined ? undefined : parseInt(to_params.idschueler as string);
		// ... wurde die ID des Schülers auf undefined setzt, so prüfe, ob die Schülerliste leer ist und wähle ggf. das erste Element aus
		if (idSchueler === undefined) {
			if (this.data.listSchueler.liste.length > 0) {
				const schueler = this.data.listSchueler.liste.at(0);
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, idErgebnis, schueler?.id);
			}
			if (this.data.dataSchueler.daten !== undefined)
				this.data.dataSchueler.unselect();
			return;
		}
		// ... wurde die ID des Schülers verändert, so lade die Laufbahn-Daten des Schülers aus der Datenbank
		if (this.data.listSchueler.ausgewaehlt?.id !== idSchueler) {
			// Setze den neu ausgewählten Schüler-Eintrag
			const schuelerEintrag = this.data.listSchueler.liste.find(s => s.id === idSchueler);
			if (schuelerEintrag === undefined)
				throw new Error("Programmierfehler: Ein Eintrag für die Schüler-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
			this.data.listSchueler.ausgewaehlt = schuelerEintrag;
			// Lade die neuen Laufbahndaten
			await this.data.dataSchueler.select(schuelerEintrag);
			const schueler = this.data.dataSchueler.daten;
			if (schueler === undefined)
				throw new Error("Fehler beim Laden der Schüler-Laufbahndaten für die Schüler-ID als Parameter der Route.");
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeGostKursplanungBlockung.getProps(to),
			listSchueler: this.data.listSchueler,
			dataSchueler: this.data.dataSchueler,
		}
	}

	public getRoute(abiturjahr: number | undefined, halbjahr: number | undefined, idblockung: number | undefined, idergebnis: number | undefined, idschueler: number | undefined) : RouteLocationRaw {
		if ((abiturjahr === undefined) || (halbjahr === undefined) || (idblockung === undefined) || (idergebnis === undefined))
			throw new Error("Abiturjahr, Halbjahr und die ID der Blockung und des Ergebnisses müssen für diese Route definiert sein.");
		if (idschueler === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr, idblockung: idblockung, idergebnis: idergebnis, idschueler : idschueler }};
	}

	public getSelector() : WritableComputedRef<SchuelerListeEintrag | undefined> {
		const router = useRouter();
		return computed({
			get: () => this.data.listSchueler.ausgewaehlt,
			set: (value) => {
				if (this.data.listSchueler.ausgewaehlt !== value)
					router.push(routeGostKursplanungSchueler.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr, routeGostKursplanung.data.halbjahr.value.id,
						routeGostKursplanungHalbjahr.data.dataKursblockung.daten?.id, routeGostKursplanungBlockung.data.ergebnis.value?.id, value?.id));
			}
		});
	}

}

export const routeGostKursplanungSchueler = new RouteGostKursplanungSchueler();
