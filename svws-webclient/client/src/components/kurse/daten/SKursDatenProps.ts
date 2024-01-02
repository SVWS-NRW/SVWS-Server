import type { KursDaten, JahrgangsListeEintrag, LehrerListeEintrag, Schueler, FaecherListeEintrag, Schulform } from "@core";

export interface KursDatenProps {
	schulform: Schulform;
	patch: (data : Partial<KursDaten>) => Promise<void>;
	data: KursDaten;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapFaecher: Map<number, FaecherListeEintrag>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}
