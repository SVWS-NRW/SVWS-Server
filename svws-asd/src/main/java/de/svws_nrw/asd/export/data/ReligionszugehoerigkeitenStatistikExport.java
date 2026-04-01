package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Für die Amtlichen Schuldaten die aufsummierten Daten der Schüler zu den Religionszugehörigkeiten (S42)
 */

@XmlRootElement
@Schema(description = "Für die Amtlichen Schuldaten die aufsummierten Daten der Schüler zu den Religionszugehörigkeiten (S42)")
@TranspilerDTO
public class ReligionszugehoerigkeitenStatistikExport {

	/** Satzschlüssel: Ein Schüler-Jahrgang, der an der Schule unterrichtet wird. */
	@Schema(description = "satzschlüssel: ein Schüler-Jahrgang, der an der Schule unterrichtet wird", example = "05")
	public String jahrgang = "";

	/** Satzschlüssel: Ein Bildungsgang, der an der Schule unterrichtet wird. */
	@Schema(description = "satzschlüssel: ein Bildungsgang, der an der Schule unterrichtet wird", example = "H")
	public String schulgliederung = "";

	/** Satzschlüssel: Ein Bildungsbereich, der an der Schule unterrichtet wird. */
	@Schema(description = "satzschlüssel: ein Bildungsbereich, der an der Schule unterrichtet wird", example = "A")
	public String bildungsbereich = "";

	/** Satzschlüssel: Ein Förderschwerpunkt, der an der Schule vorkommt. */
	@Schema(description = "satzschlüssel: ein Förderschwerpunkt, der an der Schule vorkommt", example = "LB")
	public String foerderschwerpunkt = "";

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch", example = "13")
	public int evZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Evangelisch Weiblich", example = "12")
	public int evWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Katholisch. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Katholisch", example = "13")
	public int kathZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Katholisch Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Katholisch Weiblich", example = "12")
	public int kathWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch", example = "13")
	public int juedischZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Jüdisch Weiblich", example = "12")
	public int juedischWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe", example = "13")
	public int sonstOrthZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Sonstige Orthodoxe Weiblich", example = "12")
	public int sonstOrthWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe", example = "13")
	public int syrOrthZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Syrisch Orthodoxe Weiblich", example = "12")
	public int syrOrthWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Islamisch. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Islamisch", example = "13")
	public int islamischZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Islamisch Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Islamisch Weiblich", example = "12")
	public int islamischWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch", example = "13")
	public int alevitischZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch Weiblich", example = "12")
	public int alevitischWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Mennoniten BG.NRW. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Mennoniten", example = "13")
	public int mennonitenZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Mennoniten BG.NRW Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Alevitisch Weiblich", example = "12")
	public int mennonitenWeiblich = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Andere.*/
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Andere", example = "13")
	public int andereZusammen = 0;

	/** Die Summe aller Schüler mit der Religionszugehörigkeit Andere Weiblich. */
	@Schema(description = "die Summe aller Schüler mit der Religionszugehörigkeit Andere Weiblich", example = "12")
	public int andereWeiblich = 0;

	/** Die Summe aller Schüler ohne Religionszugehörigkeit.*/
	@Schema(description = "die Summe aller Schüler ohne Religionszugehörigkeit", example = "13")
	public int ohneZusammen = 0;

	/** Die Summe aller Schüler ohne Religionszugehörigkeit Weiblich. */
	@Schema(description = "die Summe aller Schüler ohne Religionszugehörigkeit Weiblich", example = "12")
	public int ohneWeiblich = 0;

	/** Die Summe aller Schüler dieses Satzes insgesamt. */
	@Schema(description = "die Summe aller Schüler dieses Satzes ingesamt", example = "500")
	public int insgesamtZusammen = 0;

	/** Die Summe aller Schüler dieses Satzes insgesamt Weiblich. */
	@Schema(description = "die Summe aller Schüler dieses Satzes ingesamt Weiblich", example = "490")
	public int insgesamtWeiblich = 0;

	/** Die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben. */
	@Schema(description = "die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben", example = "20")
	public int abmeldungenEvZusammen = 0;

	/** Die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich. */
	@Schema(description = "die Summe aller evangelischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich", example = "15")
	public int abmeldungenEvWeiblich = 0;

	/** Die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben. */
	@Schema(description = "die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben", example = "20")
	public int abmeldungenKathZusammen = 0;

	/** Die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich. */
	@Schema(description = "die Summe aller katholischen Schüler, die sich vom Religionsunterricht abgemeldet haben Weiblich", example = "15")
	public int abmeldungenKathWeiblich = 0;

	/** Die Summe aller evangelischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten. */
	@Schema(description = "die Summe aller evanglischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten", example = "20")
	public int ohneUnterrichtEvangelischZusammen = 0;

	/** Die Summe aller evangelischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich. */
	@Schema(description = "die Summe aller evanglischer Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich",
			example = "15")
	public int ohneUnterrichtEvangelischWeiblich = 0;

	/** Die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten. */
	@Schema(description = "die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten", example = "20")
	public int ohneUnterrichtKatholischZusammen = 0;

	/** Die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich. */
	@Schema(description = "die Summe aller katholischen Schüler, die aus organisatorischen Gründen keinen Religionsunterricht erhalten Weiblich",
			example = "15")
	public int ohneUnterrichtKatholischWeiblich = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ReligionszugehoerigkeitenStatistikExport() {
		// leer
	}

}
