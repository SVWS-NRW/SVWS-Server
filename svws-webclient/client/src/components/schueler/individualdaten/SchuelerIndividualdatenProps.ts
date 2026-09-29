import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";

export interface SchuelerIndividualdatenProps {
	mapSchulen: Map<string, SchulEintrag>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	zeigeAlles: boolean;
}
