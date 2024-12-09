import type { KlassenListeManager } from "@core";

export interface SKlassenDatenLehrerZuweisungModalProps {
	manager: () => KlassenListeManager,
	addKlassenleitung: (idLehrer : number, idKlasse : number) => Promise<void>;
}
