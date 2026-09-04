import type { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SchuelerIndividualdatenProps } from "../schueler/individualdaten/SchuelerIndividualdatenProps";

export interface StatistikSchuelerProps extends SchuelerIndividualdatenProps {
	statistikGesamt: StatistikGesamt;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	setAuswahl: (id: number) => Promise<void>;
	gotoSchueler: (lehrer: SchuelerListeEintrag) => Promise<void>;
}
