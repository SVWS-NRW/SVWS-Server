export function genId() {
	return window.btoa(Math.round(Math.random() * 1_000_000) + "");
}
