import type { KlassenListeManager } from "@ui";

export interface SKlassenDatenLehrerZuweisungModalProps {
	manager: () => KlassenListeManager,
	addKlassenleitung: (idLehrer : number, idKlasse : number) => Promise<void>;
}
