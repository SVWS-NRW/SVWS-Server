import { GostJahrgang, List, GostStatistikFachwahl } from "@svws-nrw/svws-core-ts";

export interface GostFachwahlenProps {
	jahrgang: GostJahrgang;
	fachwahlen: List<GostStatistikFachwahl>;
}