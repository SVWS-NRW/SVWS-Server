import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";

import type { LehrerIndividualdatenProps } from "../lehrer/individualdaten/LehrerIndividualdatenProps";

export interface StatistikLehrerProps extends LehrerIndividualdatenProps {
	gotoLehrer: (lehrer: LehrerListeEintrag) => Promise<void>;
}
