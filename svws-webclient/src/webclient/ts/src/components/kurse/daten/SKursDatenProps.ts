import { KursDaten, JahrgangsListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";

export interface KursDatenProps {
		patch: (data : Partial<KursDaten>) => Promise<void>;
		data: KursDaten;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}