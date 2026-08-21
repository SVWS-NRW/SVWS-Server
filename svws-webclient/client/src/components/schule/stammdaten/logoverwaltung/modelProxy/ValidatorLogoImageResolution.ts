import { BasicValidator, ValidatorFehlerart } from "@core";
import type { ImageInfo, ImageRestrictions } from "../LogoUtils";

/**
 * Ein Validator, welcher prüft, ob ein Bild die minimal erforderliche Auflösung für ein Logo besitzt.
 */
export class ValidatorLogoImageResolution extends BasicValidator {

	/** Die Werte des Bildes */
	private readonly imageInfo: () => ImageInfo;
	/** Die Vorgaben, die das Bild erfüllen muss */
	private readonly imageRestrictions: ImageRestrictions;

	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param imageInfo           die Werte des Bildes
	 * @param imageRestrictions   die Vorgaben, die das Bild erfüllen muss
	 */
	constructor(imageInfo: () => ImageInfo, imageRestrictions: ImageRestrictions) {
		super(ValidatorFehlerart.HINWEIS);
		this.imageInfo = imageInfo;
		this.imageRestrictions = imageRestrictions;
	}

	/**
	 * Die Prüfroutine, welche prüft, ob das Bild die richtige Auflösung besitzt.
	 *
	 * @returns true, wenn die Auflösung des Bildes den Vorgaben entspricht
	 */
	protected pruefe(): boolean {
		const imageInfo = this.imageInfo();
		const imageType = imageInfo.fileType;
		const imageWidth = imageInfo.width;
		const imageHeight = imageInfo.height;
		if ((imageHeight === 0) || (imageWidth === 0) || (imageType === "image/svg+xml")) {
			return true;
		}

		const imageRestrictions = this.imageRestrictions;
		const targetWidthInMM = imageRestrictions.breiteInMM;
		const targetHeightInMM = imageRestrictions.hoeheInMM;
		const targetDpi = imageRestrictions.aufloesungInDPI;

		const dpiBreite = (imageWidth * 25.4) / targetWidthInMM;
		const dpiHoehe = (imageHeight * 25.4) / targetHeightInMM;
		const aufloesung = Math.min(dpiBreite, dpiHoehe);
		if (aufloesung < targetDpi) {
			this.addFehler(0, `Zu geringe Auflösung: ${Math.round(aufloesung)} DPI statt ${imageRestrictions.aufloesungInDPI} DPI.
			Dies kann zu einer unscharfen Darstellung des Bildes führen.`);
			return false;
		}

		return true;
	}

}
