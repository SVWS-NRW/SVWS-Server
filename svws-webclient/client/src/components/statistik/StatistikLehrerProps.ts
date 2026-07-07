import type { LehrerListeEintrag, StatistikGesamt } from "@core";
import type { LehrerIndividualdatenProps } from "../lehrer/individualdaten/LehrerIndividualdatenProps";
import type { LehrerPersonaldatenProps } from "../lehrer/personaldaten/LehrerPersonaldatenProps";

export interface StatistikLehrerProps extends LehrerPersonaldatenProps, LehrerIndividualdatenProps {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	setAuswahl: (id: number) => Promise<void>;
	gotoLehrer: (lehrer: LehrerListeEintrag) => Promise<void>;
}
