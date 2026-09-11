package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * Die beim Kursimport neu anzulegenden UV-Daten. Negative IDs kennzeichnen dabei
 * Objekte, die innerhalb desselben Imports referenziert werden.
 */
@TranspilerDTO
public class UvKursImportDaten {

	/** Die anzulegenden Planungsabschnitt-Lehrer-Zuordnungen. */
	@Valid
	public @NotNull List<UvPlanungsabschnittLehrerCreateRequest> planungsabschnittlehrer = new ArrayList<>();

	/** Die anzulegenden Schienen. */
	@Valid
	public @NotNull List<UvSchieneCreateRequest> schienen = new ArrayList<>();

	/** Die anzulegenden Schülergruppen. */
	@Valid
	public @NotNull List<UvSchuelergruppeCreateRequest> schuelergruppen = new ArrayList<>();

	/** Die anzulegenden Kurse. */
	@Valid
	public @NotNull List<UvKursCreateRequest> kurse = new ArrayList<>();

	/** Die anzulegenden Lerngruppen. */
	@Valid
	public @NotNull List<UvLerngruppeCreateRequest> lerngruppen = new ArrayList<>();

	/** Die anzulegenden Lehrer-Lerngruppen-Zuordnungen. */
	@Valid
	public @NotNull List<UvLerngruppenLehrerCreateRequest> lerngruppenlehrer = new ArrayList<>();

	/** Die anzulegenden Schienen-Lerngruppen-Zuordnungen. */
	@Valid
	public @NotNull List<UvLerngruppenSchieneCreateRequest> lerngruppenschienen = new ArrayList<>();

	/** Die anzulegenden Schüler-Schülergruppen-Zuordnungen. */
	@Valid
	public @NotNull List<UvSchuelergruppeSchuelerCreateRequest> schuelergruppenschueler = new ArrayList<>();

	/** Erzeugt ein leeres Importdatenobjekt. */
	public UvKursImportDaten() {
		// leer
	}

	/**
	 * Prüft, ob alle beim Import neu anzulegenden Objekte negative temporäre IDs besitzen.
	 *
	 * @return {@code true}, wenn alle temporären IDs negativ sind
	 */
	@AssertTrue(message = "Die temporären IDs aller neu anzulegenden Kurse, Lerngruppen, Schülergruppen und Schienen müssen negativ sein.")
	public boolean isGueltigeTempIds() {
		for (final @NotNull UvKursCreateRequest kurs : kurse) {
			if ((kurs.id == null) || (kurs.id >= 0)) {
				return false;
			}
		}
		for (final @NotNull UvLerngruppeCreateRequest lerngruppe : lerngruppen) {
			if ((lerngruppe.id == null) || (lerngruppe.id >= 0)) {
				return false;
			}
		}
		for (final @NotNull UvSchuelergruppeCreateRequest schuelergruppe : schuelergruppen) {
			if ((schuelergruppe.id == null) || (schuelergruppe.id >= 0)) {
				return false;
			}
		}
		for (final @NotNull UvSchieneCreateRequest schiene : schienen) {
			if ((schiene.id == null) || (schiene.id >= 0)) {
				return false;
			}
		}
		return true;
	}
}
