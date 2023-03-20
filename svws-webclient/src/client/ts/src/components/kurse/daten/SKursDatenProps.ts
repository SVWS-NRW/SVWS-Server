import { KursDaten, JahrgangsListeEintrag, LehrerListeEintrag, List, Schueler } from "@svws-nrw/svws-core";

export interface KursDatenProps {
		patch: (data : Partial<KursDaten>) => Promise<void>;
		data: KursDaten;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}