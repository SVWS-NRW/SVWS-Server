import { KlassenDaten, LehrerListeEintrag, JahrgangsListeEintrag } from "@svws-nrw/svws-core";

export interface KlassenDatenProps {
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	data: KlassenDaten,
	mapLehrer: Map<number, LehrerListeEintrag>,
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>,
}