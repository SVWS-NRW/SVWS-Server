import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";

export interface JahrgangDatenProps {
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
	data: JahrgangsDaten;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
}