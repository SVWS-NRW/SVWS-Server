import type { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { LehrerIndividualdatenProps } from "../lehrer/individualdaten/LehrerIndividualdatenProps";
import type { LehrerPersonaldatenProps } from "../lehrer/personaldaten/LehrerPersonaldatenProps";

export interface StatistikLehrerProps extends LehrerPersonaldatenProps, LehrerIndividualdatenProps {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	setAuswahl: (id: number) => Promise<void>;
	gotoLehrer: (lehrer: LehrerListeEintrag) => Promise<void>;
}
