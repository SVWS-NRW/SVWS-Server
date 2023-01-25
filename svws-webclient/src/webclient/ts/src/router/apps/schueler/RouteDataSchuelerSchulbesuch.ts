import { Herkunftsarten, SchuelerSchulbesuchMerkmal, SchuelerSchulbesuchSchule, SchuelerSchulbesuchsdaten, Schulform, Schulgliederung } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { App } from "~/apps/BaseApp";
import { PropDataSchulbesuch } from "~/components/schueler/schulbesuch/PropDataSchulbesuch";
import { routeSchuelerSchulbesuch } from "./RouteSchuelerSchulbesuch";

export class RouteDataSchuelerSchulbesuch implements PropDataSchulbesuch {

	_daten: ShallowRef<SchuelerSchulbesuchsdaten | undefined>;

	public constructor() {
		this._daten = shallowRef(undefined);
	}

	public set daten(value: SchuelerSchulbesuchsdaten | undefined) {
		this._daten.value = value;
	}

	public get daten(): SchuelerSchulbesuchsdaten | undefined {
		return this._daten.value;
	}

	public get visible(): boolean {
		return !(routeSchuelerSchulbesuch.hidden()) && (this._daten !== undefined);
	}

	public async onSelect(id?: number) {
		if (((id === undefined) && (this.daten === undefined)) ||
            ((this.daten !== undefined) && (this.daten.id === id)))
			return;
		this.daten = (id === undefined) ? undefined : await App.api.getSchuelerSchulbesuch(App.schema, id);
	}

	private async patch(partial : Partial<SchuelerSchulbesuchsdaten>) {
		if (this.daten === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		return App.api.patchSchuelerSchulbesuch(partial, App.schema, this.daten.id);
	}


	// Informationen zur Herkunft / Vorigen Schule des Schülers

	vorigeSchulnummer: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeSchulnummer?.toString(),
		set: (value) => void this.patch({ vorigeSchulnummer:  value })
	});

	vorigeSchulform: () => ComputedRef<Schulform | undefined> = () => computed(() => {
		const vorigeAllgHerkunft = this.daten?.vorigeAllgHerkunft;
		if (vorigeAllgHerkunft === undefined)
			return undefined;
		const sgl = Schulgliederung.getByKuerzel(vorigeAllgHerkunft);
		if (sgl !== null)
			return Schulform.BK;
		return Schulform.getByKuerzel(vorigeAllgHerkunft) || undefined;
	});

	vorigeAllgHerkunft: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeAllgHerkunft?.toString(),
		set: (value) => void this.patch({ vorigeAllgHerkunft:  value })
	});

	vorigeEntlassDatum: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeEntlassdatum?.toString(),
		set: (value) => void this.patch({ vorigeEntlassdatum:  value })
	});

	vorigeEntlassjahrgang: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeEntlassjahrgang?.toString(),
		set: (value) => void this.patch({ vorigeEntlassjahrgang:  value })
	});

	vorigeArtLetzteVersetzung: () => WritableComputedRef<Herkunftsarten | undefined> = () => computed({
		get: () => {
			if ((this.daten === undefined) || (this.daten.vorigeArtLetzteVersetzung === null))
				return undefined;
			const artID = parseInt(this.daten.vorigeArtLetzteVersetzung.valueOf());
			return Herkunftsarten.getByID(artID) || undefined;
		},
		set: (value) => void this.patch({ vorigeArtLetzteVersetzung:  value === undefined || value === null ? null : "" + value.daten.id.valueOf() })
	});

	vorigeBemerkung: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeBemerkung?.toString(),
		set: (value) => void this.patch({ vorigeBemerkung:  value })
	});

	vorigeEntlassgrundID: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.vorigeEntlassgrundID?.valueOf(),
		set: (value) => void this.patch({ vorigeEntlassgrundID: value })
	});

	vorigeAbschlussartID: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.vorigeAbschlussartID?.toString(),
		set: (value) => void this.patch({ vorigeAbschlussartID:  value })
	});


	// Informationen zum Grundschulbesuch

	grundschuleEinschulungsjahr: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.grundschuleEinschulungsjahr?.valueOf(),
		set: (value) => void this.patch({ grundschuleEinschulungsjahr: value })
	});

	grundschuleEinschulungsartID: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.grundschuleEinschulungsartID?.valueOf(),
		set: (value) => void this.patch({ grundschuleEinschulungsartID: value })
	});

	grundschuleJahreEingangsphase: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.grundschuleJahreEingangsphase?.valueOf(),
		set: (value) => void this.patch({ grundschuleJahreEingangsphase: value })
	});

	grundschuleUebergangsempfehlungID: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.grundschuleUebergangsempfehlungID?.valueOf(),
		set: (value) => void this.patch({ grundschuleUebergangsempfehlungID: value })
	});


	// Informationen zur Sekundarstufe I

	sekIWechsel: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.sekIWechsel?.valueOf(),
		set: (value) => void this.patch({ sekIWechsel: value })
	});

	sekIErsteSchulform: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.sekIErsteSchulform?.toString(),
		set: (value) => void this.patch({ sekIErsteSchulform: value })
	});

	sekIIWechsel: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.sekIIWechsel?.valueOf(),
		set: (value) => void this.patch({ sekIIWechsel: value })
	});


	// Informationen zur Entlassung von der Schule

	entlassungDatum: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.entlassungDatum?.toString(),
		set: (value) => void this.patch({ entlassungDatum:  value })
	});

	entlassungJahrgang: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.entlassungJahrgang?.toString(),
		set: (value) => void this.patch({ entlassungJahrgang:  value })
	});

	entlassungGrundID: () => WritableComputedRef<number | undefined> = () => computed({
		get: () => this.daten?.entlassungGrundID?.valueOf(),
		set: (value) => void this.patch({ entlassungGrundID: value })
	});

	entlassungAbschlussartID: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.entlassungAbschlussartID?.toString(),
		set: (value) => void this.patch({ entlassungAbschlussartID: value })
	});


	// Wechsel zu aufnehmender Schule

	aufnehmdendSchulnummer: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.aufnehmdendSchulnummer?.toString(),
		set: (value) => void this.patch({ aufnehmdendSchulnummer: value })
	});

	aufnehmdendWechseldatum: () => WritableComputedRef<string | undefined> = () => computed({
		get: () => this.daten?.aufnehmdendWechseldatum?.toString(),
		set: (value) => void this.patch({ aufnehmdendWechseldatum: value })
	});

	aufnehmdendBestaetigt: () => WritableComputedRef<boolean | undefined> = () => computed({
		get: () => this.daten?.aufnehmdendBestaetigt?.valueOf(),
		set: (value) => void this.patch({ aufnehmdendBestaetigt: value })
	});


	// Informationen zu weiteren Schulen

	alleSchulen: () => ComputedRef<SchuelerSchulbesuchSchule[]> = () => computed(() => {
		return this.daten === undefined ? [] : (this.daten.alleSchulen.toArray() as SchuelerSchulbesuchSchule[]);
	});


	// Merkmale

	merkmale: () => ComputedRef<SchuelerSchulbesuchMerkmal[]> = () => computed(() => {
		return this.daten === undefined ? [] : (this.daten.merkmale.toArray() as SchuelerSchulbesuchMerkmal[]);
	});

}
