import { Herkunftsarten, SchuelerSchulbesuchMerkmal, SchuelerSchulbesuchSchule, Schulform } from "@svws-nrw/svws-core-ts";
import { ComputedRef, WritableComputedRef } from "vue";

export interface PropDataSchulbesuch {

    get visible(): boolean;

	// Informationen zur Herkunft / Vorigen Schule des Schülers
	vorigeSchulnummer: () => WritableComputedRef<string | undefined>;
	vorigeSchulform: () => ComputedRef<Schulform | undefined>;
	vorigeAllgHerkunft: () => WritableComputedRef<string | undefined>;
	vorigeEntlassDatum: () => WritableComputedRef<string | undefined>;
	vorigeEntlassjahrgang: () => WritableComputedRef<string | undefined>;
	vorigeArtLetzteVersetzung: () => WritableComputedRef<Herkunftsarten | undefined>;
	vorigeBemerkung: () => WritableComputedRef<string | undefined>;
	vorigeEntlassgrundID: () => WritableComputedRef<number | undefined>;
	vorigeAbschlussartID: () => WritableComputedRef<string | undefined>;

	// Informationen zum Grundschulbesuch
	grundschuleEinschulungsjahr: () => WritableComputedRef<number | undefined>;
	grundschuleEinschulungsartID: () => WritableComputedRef<number | undefined>;
	grundschuleJahreEingangsphase: () => WritableComputedRef<number | undefined>;
	grundschuleUebergangsempfehlungID: () => WritableComputedRef<number | undefined>;

	// Informationen zur Sekundarstufe I
	sekIWechsel: () => WritableComputedRef<number | undefined>;
	sekIErsteSchulform: () => WritableComputedRef<string | undefined>;
	sekIIWechsel: () => WritableComputedRef<number | undefined>;

	// Informationen zur Entlassung von der Schule
	entlassungDatum: () => WritableComputedRef<string | undefined>;
	entlassungJahrgang: () => WritableComputedRef<string | undefined>;
	entlassungGrundID: () => WritableComputedRef<number | undefined>;
	entlassungAbschlussartID: () => WritableComputedRef<string | undefined>;

    // Wechsel zu aufnehmender Schule
    aufnehmdendSchulnummer: () => WritableComputedRef<string | undefined>;
	aufnehmdendWechseldatum: () =>WritableComputedRef<string | undefined>;
	aufnehmdendBestaetigt: () =>WritableComputedRef<boolean | undefined>;

	// Informationen zu weiteren Schulen
	alleSchulen: () => ComputedRef<SchuelerSchulbesuchSchule[]>;

	// Merkmale
	merkmale: () => ComputedRef<SchuelerSchulbesuchMerkmal[]>;

}