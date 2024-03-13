import type { KursDaten, JahrgangsDaten, LehrerListeEintrag, Schueler, FachDaten, Schulform } from "@core";

export interface KursDatenProps {
	schulform: Schulform;
	patch: (data : Partial<KursDaten>) => Promise<void>;
	data: () => KursDaten;
	mapJahrgaenge: Map<number, JahrgangsDaten>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapFaecher: Map<number, FachDaten>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}
