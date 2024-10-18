import type { SchulEintrag } from "@core";

export interface SchuleDatenProps {
	schuljahr: number;
	patch: (data : Partial<SchulEintrag>) => Promise<void>;
	mapKatalogeintraege: Map<number, SchulEintrag>;
	auswahl: SchulEintrag|undefined;
}