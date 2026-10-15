package de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung;

import java.util.List;

import de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung;

/**
 * Ergebnis einer Synchronisation von erfolgreichen Bewerbungen.
 *
 * @param erstellt      die neu angelegten erfolgreichen Bewerbungen, in der Reihenfolge des Remote-Ergebnisses
 * @param uebersprungen die schulkindIds der bereits bekannten und daher übersprungenen Bewerbungen
 */
public record SyncResult(
		List<SchulwechselErfolgreicheBewerbung> erstellt,
		List<String> uebersprungen
) {
}
