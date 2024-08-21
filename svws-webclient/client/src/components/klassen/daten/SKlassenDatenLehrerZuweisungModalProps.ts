import type { KlassenListeManager } from "@core";

export interface SKlassenDatenLehrerZuweisungModalProps {
	klassenListeManager: () => KlassenListeManager,
	addKlassenleitung: (idLehrer : number, idKlasse : number) => Promise<void>;
}
