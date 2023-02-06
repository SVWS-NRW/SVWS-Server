import { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, List, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { App } from "~/apps/BaseApp";
import { ref, Ref } from "vue";
import { routeSchuelerAbschnitt, RouteSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";

export class RouteDataSchuelerAbschnittDaten {

	auswahl: SchuelerLernabschnittListeEintrag | undefined = undefined;
	daten: Ref<SchuelerLernabschnittsdaten | undefined> = ref(undefined);
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map();
	mapKlassen: Ref<Map<number, KlassenListeEintrag>> = ref(new Map());

	public async onSelect(item?: SchuelerLernabschnittListeEintrag) {
		if (((item === undefined) && (this.daten.value === undefined)) || ((this.daten.value !== undefined) && (this.daten.value.id === item?.id)))
			return;
		this.auswahl = item;
		this.daten.value = (item?.id === undefined) ? undefined : await App.api.getSchuelerLernabschnittsdatenByID(App.schema, item.id);
		// Lade die Liste der Klassen als Katalog, der nur lesend genutzt wird
		if (item === undefined) {
			this.mapKlassen.value = new Map();
		} else {
			const klassen = await App.api.getKlassenFuerAbschnitt(App.schema, item.schuljahresabschnitt);
			const mapKlassen = new Map<number, KlassenListeEintrag>();
			for (const k of klassen)
				mapKlassen.set(k.id, k);
			this.mapKlassen.value = mapKlassen;
		}
	}

	setLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerAbschnittDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patch = async (data : Partial<SchuelerLernabschnittsdaten>) => {
		if (this.daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO await App.api.patchSchuelerLernabschnittsdaten(data, App.schema, this.daten.value.id);
	}

	patchBemerkungen = async (data : Partial<SchuelerLernabschnittBemerkungen>) => {
		if (this.daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		// TODO await App.api.patchSchuelerLernabschnittsdatenBemerkungen(data, App.schema, this.daten.value.id);
	}

}

const SSchuelerAbschnittDaten = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittDaten.vue");
const SSchuelerAbschnittAuswahl = () => import("~/components/schueler/abschnitt/SSchuelerAbschnittAuswahl.vue")

export class RouteSchuelerAbschnittDaten extends RouteNode<RouteDataSchuelerAbschnittDaten, RouteSchuelerAbschnitt> {

	public constructor() {
		super("schueler_abschnitt_daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerAbschnittDaten, new RouteDataSchuelerAbschnittDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abschnittsdaten";
		super.setView("lernabschnittauswahl", SSchuelerAbschnittAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return (to_params.id !== undefined);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		// Lade die Liste der Lehrer als Katalog, der nur lesend genutzt wird
		const lehrer = await App.api.getLehrer(App.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of lehrer)
			mapLehrer.set(l.id, l);
		this.data.mapLehrer = mapLehrer;
		// Lade die Liste der Jahrgaenge als Katalog, der nur lesend genutzt wird
		const jahrgaenge = await App.api.getJahrgaenge(App.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of jahrgaenge)
			mapJahrgaenge.set(j.id, j);
		this.data.mapJahrgaenge = mapJahrgaenge;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id as string);
		if (to_params.abschnitt === undefined) {
			await this.data.onSelect(undefined);
			return routeSchuelerAbschnitt.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt as string);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr as string);
			const entry = routeSchuelerAbschnitt.data.getEntry(abschnitt, wechselNr);
			await this.data.onSelect(entry);
		}
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			lernabschnitt: this.data.auswahl,
			lernabschnitte: routeSchuelerAbschnitt.data.listAbschnitte,
			setLernabschnitt: this.data.setLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			data: this.data.daten.value,
			mapLehrer: this.data.mapLehrer,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKlassen: this.data.mapKlassen.value,
			patch: this.data.patch,
			patchBemerkungen: this.data.patchBemerkungen
		};
	}

}

export const routeSchuelerAbschnittDaten = new RouteSchuelerAbschnittDaten();

