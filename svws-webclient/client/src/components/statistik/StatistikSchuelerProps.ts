import type { SchuelerListeEintrag, StatistikGesamt } from "@core";
import type { SchuelerIndividualdatenProps } from "../schueler/individualdaten/SchuelerIndividualdatenProps";

export interface StatistikSchuelerProps extends SchuelerIndividualdatenProps {
	statistikGesamt: StatistikGesamt;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	setAuswahl: (id: number) => Promise<void>;
	gotoSchueler: (lehrer: SchuelerListeEintrag) => Promise<void>;
}
