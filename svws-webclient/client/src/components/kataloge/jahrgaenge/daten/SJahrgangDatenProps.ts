import type { JahrgangsDaten, JahrgangsListeEintrag, Schulform } from "@core";

export interface JahrgangDatenProps {
	schulform: Schulform;
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
	data: () => JahrgangsDaten;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
}