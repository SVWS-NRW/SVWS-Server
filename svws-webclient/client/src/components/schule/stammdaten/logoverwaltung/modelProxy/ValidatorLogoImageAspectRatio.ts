import { BasicValidator, ValidatorFehlerart } from "@core";
import type { ImageInfo, ImageRestrictions } from "../LogoUtils";
import { getAspectRatio } from "../LogoUtils";


/**
 * Ein Validator, welcher prüft, ob ein Bild das vorgegebene Seitenverhältnis für ein Logo besitzt.
 */
export class ValidatorLogoImageAspectRatio extends BasicValidator {

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
	 * Die Prüfroutine, welche prüft, ob das Bild das richtige Seitenverhältnis besitzt.
	 *
	 * @returns true, wenn das Bild dem vorgegebenen Seitenverhältnis entspricht
	 */
	protected pruefe(): boolean {
		const imageInfo = this.imageInfo();
		const width = imageInfo.width;
		const height = imageInfo.height;

		if ((height === 0) || (width === 0)) {
			return true;
		}

		const imageRestrictions = this.imageRestrictions;
		const targetWidthInMM = imageRestrictions.breiteInMM;
		const targetHeightInMM = imageRestrictions.hoeheInMM;

		const actualRatio = width / height;
		const expectedRatio = targetWidthInMM / targetHeightInMM;
		if (Math.abs(actualRatio - expectedRatio) > 0.03) {
			this.addFehler(0, `Unpassendes Seitenverhältnisse: ${getAspectRatio(height, width)} statt ${imageRestrictions.seitenverhaeltnis}.
			Dies kann zu einer verzerrten Darstellung des Bildes führen.`);
			return false;
		}

		return true;
	}

}
