import { JavaString } from "@core";

export function emailIsValid(value: string | null, maxLength: number) {
	if ((value === null) || JavaString.isBlank(value))
		return true;

	if (value.length > maxLength)
		return false;

	return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
}

export function mandatoryInputIsValid(value: string | null, maxLength: number) {
	if (value === null)
		return false;

	return (!JavaString.isBlank(value)) && (value.length <= maxLength);
}

export function optionalInputIsValid(input : string | null, maxLength: number) {
	if ((input === null) || (JavaString.isBlank(input)))
		return true;

	return input.length <= maxLength;
}

export function isUniqueInList<T>(value: string | null, list: Array<T>, key: keyof T): boolean {
	if ((value === null) || (value === ""))
		return true;

	return !list.some(entry => {
		const fieldValue = entry[key];
		return typeof fieldValue === "string" && fieldValue.trim() === value.trim();
	});
}
