import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { GostBlockungsergebnisListeneintrag, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, useRouter } from "vue-router";
import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
import { RouteGostKursplanungHalbjahr, routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { computed, Ref, ref, WritableComputedRef } from "vue";
import { routeGostKursplanungSchueler } from "./RouteGostKursplanungSchueler";
import { RouteManager } from "~/router/RouteManager";
import { App } from "~/apps/BaseApp";

export class RouteDataGostKursplanungBlockung {
	ergebnis: Ref<GostBlockungsergebnisListeneintrag | undefined> = ref(undefined);
	dataKursblockungsergebnis: DataGostKursblockungsergebnis = new DataGostKursblockungsergebnis();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	fachwahlen: List<GostStatistikFachwahl> = new Vector<GostStatistikFachwahl>();

	updateKursSchienenZuordnung = async (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => {
		await this.dataKursblockungsergebnis.assignKursSchiene(idKurs, idSchieneAlt, idSchieneNeu);
	}

}

const SGostKursplanung = () => import("~/components/gost/kursplanung/SGostKursplanung.vue");
const SGostKursplanungErgebnisAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungErgebnisAuswahl.vue");

export class RouteGostKursplanungBlockung extends RouteNode<RouteDataGostKursplanungBlockung, RouteGostKursplanungHalbjahr> {

	public constructor() {
		super("gost_kursplanung_halbjahr_ergebnis", "ergebnis/:idergebnis(\\d+)?", SGostKursplanung, new RouteDataGostKursplanungBlockung());
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
		this.data.fachwahlen = await App.api.getGostAbiturjahrgangFachwahlstatistik(App.schema, abiturjahr);
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
		if ((abiturjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.abiturjahr) || (halbjahr !== routeGostKursplanungHalbjahr.data.listBlockungen.halbjahr) || (idBlockung === undefined))
			return routeGostKursplanung.getRoute(abiturjahr, halbjahr.id);
		if (idBlockung !== routeGostKursplanungHalbjahr.data.listBlockungen.ausgewaehlt?.id)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, idBlockung);
		// Prüfe die ID der Ergebnisses
		const idErgebnis = to_params.idergebnis === undefined ? undefined : parseInt(to_params.idergebnis);
		// ... wurde die ID des Ergebnisses auf undefined setzt, so prüfe, ob die Ergebnisliste leer ist und wähle ggf. das erste Element aus
		if (idErgebnis === undefined) {
			if ((routeGostKursplanungHalbjahr.data.dataKursblockung.daten !== undefined) && (routeGostKursplanungHalbjahr.data.dataKursblockung.daten.ergebnisse.size() > 0)) {
				const ergebnis = routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnisse().get(0);
				return this.getRoute(abiturjahr, halbjahr.id, idBlockung, ergebnis.id);
			}
			if (this.data.ergebnis.value !== undefined) {
				await this.data.dataKursblockungsergebnis.unselect();
				this.data.ergebnis.value = undefined;
			}
			return;
		}
		// ... bestimme den Listen-Eintrag zu dem Ergebnis
		const ergebnisEintrag = (routeGostKursplanungHalbjahr.data.dataKursblockung.ergebnisse().toArray() as GostBlockungsergebnisListeneintrag[]).find(e => e.id === idErgebnis);
		if (ergebnisEintrag === undefined)
			throw new Error("Programmierfehler: Ein Eintrag für die Ergebnis-ID als Parameter der Route muss an dieser Stelle vorhanden sein.");
		// ... wurde die ID des Ergebnisses verändert, so lade das neue Ergebnis aus der Datenbank
		if (this.data.ergebnis.value?.id !== idBlockung) {
			// Setze den neu ausgewählten Ergebnis-Eintrag
			this.data.ergebnis.value = ergebnisEintrag;
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
		this.data.ergebnis.value = undefined;
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
			...routeGostKursplanungHalbjahr.getProps(to),
			ergebnis: this.data.dataKursblockungsergebnis,
			listLehrer: this.data.listLehrer,
			mapLehrer: this.data.mapLehrer,
			fachwahlen: this.data.fachwahlen
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const faecherManager = routeGost.data.dataFaecher.manager;
		return {
			addRegel: routeGostKursplanungHalbjahr.data.addRegel,
			removeRegel: routeGostKursplanungHalbjahr.data.removeRegel,
			updateKursSchienenZuordnung: this.data.updateKursSchienenZuordnung,
			mapSchueler: routeGostKursplanungSchueler.data.mapSchueler.value,
			schuelerFilter: routeGostKursplanungSchueler.data.schuelerFilter.value,
			...routeGostKursplanungHalbjahr.getProps(to),
			ergebnis: this.data.dataKursblockungsergebnis,
			listLehrer: this.data.listLehrer,
			mapLehrer: this.data.mapLehrer,
			fachwahlen: this.data.fachwahlen
		}
	}

	public getSelector() : WritableComputedRef<GostBlockungsergebnisListeneintrag | undefined> {
		const router = useRouter();
		return computed({
			get: () => this.data.ergebnis.value,
			set: (value) => {
				if ((value?.id !== this.data.ergebnis.value?.id) && (!RouteManager.isActive())) {
					const idSchueler = routeGostKursplanungSchueler.data.schueler.value?.id;
					if (idSchueler === undefined)
						void router.push(this.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr, routeGostKursplanung.data.halbjahr.value.id, value?.blockungID, value?.id));
					else
						void router.push(routeGostKursplanungSchueler.getRoute(routeGost.liste.ausgewaehlt?.abiturjahr, routeGostKursplanung.data.halbjahr.value.id,
							value?.blockungID, value?.id, idSchueler));
				}
			}
		});
	}

}

export const routeGostKursplanungBlockung = new RouteGostKursplanungBlockung();
