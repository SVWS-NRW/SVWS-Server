import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { KlassenStatistikGesamt } from "@core/asd/data/statistik/KlassenStatistikGesamt";

export interface StatistikKlassenProps {
	gotoKlasse: (klasse: KlassenStatistikGesamt | KlassenListeEintrag) => Promise<void>;
}
