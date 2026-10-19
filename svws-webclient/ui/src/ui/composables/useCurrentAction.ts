import type { Ref } from "vue";
import { ref } from "vue";

/**
 * Verwaltet die geöffnete Card eines Bereichs, damit immer nur eine Card offen ist. Eine UiCard meldet ihr Schließen erst nach der Animation.
 * Dieses verspätete Schließen einer anderen Card darf die gerade geöffnete Card nicht zurücksetzen.
 *
 * @param currentAction   die geöffnete Card, zum Beispiel ein Model der Komponente. Ohne Angabe entsteht ein neues ref.
 *
 * @returns die geöffnete Card und die Funktion, die sie beim Öffnen oder Schließen einer Card setzt
 */
export function useCurrentAction(currentAction: Ref<string> = ref<string>("")) {

	function setCurrentAction(action: string, open: boolean) {
		if ((action !== currentAction.value) && !open) {
			return;
		}
		currentAction.value = open ? action : "";
	}

	return { currentAction, setCurrentAction };
}
