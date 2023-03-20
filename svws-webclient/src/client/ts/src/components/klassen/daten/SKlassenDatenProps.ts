import { KlassenDaten, LehrerListeEintrag, JahrgangsListeEintrag, Schueler, List } from "@svws-nrw/svws-core";

export interface KlassenDatenProps {
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	data: KlassenDaten,
	mapLehrer: Map<number, LehrerListeEintrag>,
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>,
	listSchueler: List<Schueler>,
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}