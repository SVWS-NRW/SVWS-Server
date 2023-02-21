import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";

export interface KlassenAppProps {
	auswahl: KlassenListeEintrag | undefined,
	mapLehrer: Map<number, LehrerListeEintrag>,
}