import { BasicValidator, ValidatorFehlerart } from "@core";
import type { ImageInfo, ImageRestrictions } from "../LogoUtils";
import { ValidatorLogoImageResolution } from "./ValidatorLogoImageResolution";
import { ValidatorLogoImageAspectRatio } from "./ValidatorLogoImageAspectRatio";
import { ValidatorLogoImageFileType } from "./ValidatorLogoImageFileType";
import { ValidatorLogoImageFileSize } from "./ValidatorLogoImageFileSize";


/**
 * Ein Validator, welcher prüft, ob das Logo-Bild gültig ist.
 */
export class ValidatorLogoImage extends BasicValidator {
	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param imageInfo           die Werte des Bildes
	 * @param imageRestrictions   die Vorgaben, die das Bild erfüllen muss
	 */
	constructor(imageInfo: () => ImageInfo, imageRestrictions: ImageRestrictions) {
		super(ValidatorFehlerart.KANN);
		this._validatoren.add(new ValidatorLogoImageFileType(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageFileSize(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageResolution(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageAspectRatio(imageInfo, imageRestrictions));
	}

	protected pruefe(): boolean {
		return true;
	}

}
