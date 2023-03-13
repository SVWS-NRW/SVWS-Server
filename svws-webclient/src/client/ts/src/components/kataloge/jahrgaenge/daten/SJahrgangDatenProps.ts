import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core";

export interface JahrgangDatenProps {
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
	data: JahrgangsDaten;
	mapKatalogeintraege: Map<number, JahrgangsListeEintrag>;
}