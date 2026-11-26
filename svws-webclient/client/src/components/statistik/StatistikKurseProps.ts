import type { KursStatistikGesamt } from "@core/asd/data/statistik/KursStatistikGesamt";

import type { KursDatenProps } from "../kurse/daten/KursDatenProps";

export interface StatistikKurseProps extends Omit<KursDatenProps, "manager"> {
	gotoKurs: (lehrer: KursStatistikGesamt) => Promise<void>;
	zeigeAlles: boolean;
}
