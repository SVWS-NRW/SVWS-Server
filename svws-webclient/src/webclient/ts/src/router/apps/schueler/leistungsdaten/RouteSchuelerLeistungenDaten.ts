import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core-ts";
import { Ref, ref, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { computed } from "vue";
import { ListAbschnitte } from "~/apps/schueler/ListAbschnitte";
import { ListFaecher } from "~/apps/kataloge/faecher/ListFaecher";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/RouteApp";
import { App } from "~/apps/BaseApp";

export class RouteDataSchuelerLeistungenDaten {
	auswahl: ListAbschnitte = new ListAbschnitte();
	_daten: Ref<SchuelerLernabschnittsdaten | undefined> = ref(undefined);
	listFaecher: ListFaecher = new ListFaecher();
	mapFaecher: Map<number, FaecherListeEintrag> = new Map();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();

	public get daten(): SchuelerLernabschnittsdaten {
		if (this._daten.value === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._daten.value;
	}

	public async onSelect(item?: SchuelerLernabschnittListeEintrag) {
		if (((item === undefined) && (this._daten.value === undefined)) || ((this._daten.value !== undefined) && (this.daten.id === item?.id)))
			return;
		this.auswahl.ausgewaehlt = item;
		this._daten.value = (item?.id === undefined) ? undefined : await App.api.getSchuelerLernabschnittsdaten(App.schema, item.schuelerID, item.schuljahresabschnitt);
	}

	setLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value?.schuelerID, idLernabschnitt: value?.id } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		if (this._daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO await App.api.patchSchuelerLeistungsdaten(data, App.schema, id);
	}

}

const SSchuelerLeistungenDaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenDaten.vue");
const SSchuelerLeistungenAuswahl = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahl.vue")

export class RouteSchuelerLeistungenDaten extends RouteNodeListView<ListAbschnitte, SchuelerLernabschnittListeEintrag, RouteDataSchuelerLeistungenDaten, RouteSchuelerLeistungen> {

	public constructor() {
		super("schueler_leistungen_daten", ":idLernabschnitt(\\d+)?", SSchuelerLeistungenAuswahl, SSchuelerLeistungenDaten, new ListAbschnitte(), 'id', new RouteDataSchuelerLeistungenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id as string);
		if (to.name !== from?.name) {
			await this.data.listFaecher.update_list();
			this.data.mapFaecher.clear();
			this.data.listFaecher.liste.forEach(f => this.data.mapFaecher.set(f.id, f));
			await this.data.listLehrer.update_list();
			this.data.mapLehrer.clear();
			this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
		}
		if ((to.name !== from?.name) || (from_params.id === undefined) || (parseInt(from_params.id as string) != id))
			await this.data.auswahl.update_list(id);
		if ((to.name === this.name) && (to_params.idLernabschnitt === undefined)) {
			if (routeApp.data.schule.daten === undefined)
				throw new Error("Keine Daten für die Schule geladen!");
			const akt_schuljahresabschnitt = routeApp.data.schule.daten.idSchuljahresabschnitt;
			let lernabschnitt = this.data.auswahl.liste.find(l => l.schuljahresabschnitt === akt_schuljahresabschnitt);
			if (lernabschnitt === undefined)
				lernabschnitt = this.data.auswahl.liste[0];
			if (lernabschnitt === undefined)
				return false;
			return { name: this.name, params: { id: to_params.id, idLernabschnitt: lernabschnitt.id }};
		}
		return true;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.idLernabschnitt === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const idLernabschnitt = parseInt(to_params.idLernabschnitt as string);
			await this.data.onSelect(this.data.auswahl.liste.find(s => s.id === idLernabschnitt));
		}
	}

	// TODO deprecated
	protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> {
		return computed({
			get: () => undefined,
			set: (value) => {}
		});
	}

	public getRoute(id: number, idLernabschnitt: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idLernabschnitt }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			lernabschnitt: this.data.auswahl.ausgewaehlt,
			lernabschnitte: this.data.auswahl.liste,
			setLernabschnitt: this.data.setLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			lernabschnitt: this.data.auswahl.ausgewaehlt,
			data: this.data.daten,
			mapFaecher: this.data.mapFaecher,
			mapLehrer: this.data.mapLehrer,
			patchLeistung: this.data.patchLeistung
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

