import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core";

export interface KlassenAppProps {
	auswahl: KlassenListeEintrag | undefined,
	mapLehrer: Map<number, LehrerListeEintrag>,
}