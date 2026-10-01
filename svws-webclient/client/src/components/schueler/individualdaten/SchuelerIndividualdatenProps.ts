import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";

export interface SchuelerIndividualdatenProps {
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	zeigeAlles: boolean;
}
