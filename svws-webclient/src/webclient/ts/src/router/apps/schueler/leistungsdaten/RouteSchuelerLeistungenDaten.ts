import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ListFaecher } from "~/apps/kataloge/faecher/ListFaecher";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLeistungen, RouteSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { RouteManager } from "~/router/RouteManager";
import { routeLogin } from "~/router/RouteLogin";
import { ref, Ref } from "vue";

export class RouteDataSchuelerLeistungenDaten {

	auswahl: SchuelerLernabschnittListeEintrag | undefined = undefined;
	daten: Ref<SchuelerLernabschnittsdaten | undefined> = ref(undefined);
	listFaecher: ListFaecher = new ListFaecher();
	mapFaecher: Map<number, FaecherListeEintrag> = new Map();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();

	public async onSelect(item?: SchuelerLernabschnittListeEintrag) {
		if (((item === undefined) && (this.daten.value === undefined)) || ((this.daten.value !== undefined) && (this.daten.value.id === item?.id)))
			return;
		this.auswahl = item;
		this.daten.value = (item?.id === undefined) ? undefined : await routeLogin.data.api.getSchuelerLernabschnittsdatenByID(routeLogin.data.schema, item.id);
	}

	setLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		if (this.daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO await routeLogin.data.api.patchSchuelerLeistungsdaten(data, routeLogin.data.schema, id);
	}

}

const SSchuelerLeistungenDaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenDaten.vue");
const SSchuelerLeistungenAuswahl = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahl.vue")

export class RouteSchuelerLeistungenDaten extends RouteNode<RouteDataSchuelerLeistungenDaten, RouteSchuelerLeistungen> {

	public constructor() {
		super("schueler_leistungen_daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerLeistungenDaten, new RouteDataSchuelerLeistungenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return (to_params.id !== undefined);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		await this.data.listFaecher.update_list();
		this.data.mapFaecher.clear();
		this.data.listFaecher.liste.forEach(f => this.data.mapFaecher.set(f.id, f));
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id as string);
		if (to_params.abschnitt === undefined) {
			await this.data.onSelect(undefined);
			return routeSchuelerLeistungen.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt as string);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr as string);
			const entry = routeSchuelerLeistungen.data.getEntry(abschnitt, wechselNr);
			await this.data.onSelect(entry);
		}
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			lernabschnitt: this.data.auswahl,
			lernabschnitte: routeSchuelerLeistungen.data.listAbschnitte,
			setLernabschnitt: this.data.setLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			data: this.data.daten.value,
			mapFaecher: this.data.mapFaecher,
			mapLehrer: this.data.mapLehrer,
			patchLeistung: this.data.patchLeistung
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

