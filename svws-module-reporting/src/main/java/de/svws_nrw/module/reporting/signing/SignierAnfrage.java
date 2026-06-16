package de.svws_nrw.module.reporting.signing;

import java.util.UUID;

/**
 * Eine einzelne Signieranfrage für ein Dokument. Je Schüler wird genau eine Anfrage erzeugt und gesammelt über
 * {@link DokumentSignierer#signiereBatch(java.util.List)} an den Signierer übergeben.
 * Die {@code correlationId} ist anonym: Reporting hält die Zuordnung zur echten Schüler-ID lokal und gibt sie nicht nach außen.
 *
 * @param correlationId Anonyme Korrelations-ID zur Zuordnung von Anfrage und Ergebnis.
 * @param xml           Das zu signierende XSchule-XML als Bytes (kein Hash – die Hashbildung erfolgt im Signiermodul).
 */
// Anfragen werden ausschließlich über die correlationId zugeordnet, nie per Wertgleichheit auf dem Byte-Array verglichen.
@SuppressWarnings("java:S6218")
public record SignierAnfrage(UUID correlationId, byte[] xml) {
	// Reines Datenobjekt ohne weitere Logik.
}
