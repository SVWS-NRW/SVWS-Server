import { KlassenDaten, LehrerListeEintrag, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";

export interface KlassenDatenProps {
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	data: KlassenDaten,
	mapLehrer: Map<number, LehrerListeEintrag>,
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag>,
}