import type { GostJahrgang, List, GostStatistikFachwahl } from "@svws-nrw/svws-core";

export interface GostFachwahlenProps {
	jahrgang: GostJahrgang;
	fachwahlen: List<GostStatistikFachwahl>;
}