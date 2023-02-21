import { KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";

export interface KurseAppProps {
		auswahl: KursListeEintrag | undefined;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}