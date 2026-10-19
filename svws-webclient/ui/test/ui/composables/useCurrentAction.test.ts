import { describe, expect, test } from "vitest";
import { ref } from "vue";

import { useCurrentAction } from "@ui/ui/composables/useCurrentAction";

describe("useCurrentAction", () => {

	test("öffnet eine Card und schließt sie wieder", () => {
		const { currentAction, setCurrentAction } = useCurrentAction();
		setCurrentAction("delete", true);
		expect(currentAction.value).toBe("delete");
		setCurrentAction("delete", false);
		expect(currentAction.value).toBe("");
	});

	test("übernimmt beim Öffnen einer anderen Card diese", () => {
		const { currentAction, setCurrentAction } = useCurrentAction();
		setCurrentAction("delete", true);
		setCurrentAction("sort", true);
		expect(currentAction.value).toBe("sort");
	});

	test("ignoriert das Schließen einer Card, die nicht geöffnet ist", () => {
		const { currentAction, setCurrentAction } = useCurrentAction();
		setCurrentAction("delete", true);
		setCurrentAction("sort", true);
		setCurrentAction("delete", false);
		expect(currentAction.value).toBe("sort");
	});

	test("arbeitet auf einem übergebenen ref", () => {
		const model = ref<string>("");
		const { setCurrentAction } = useCurrentAction(model);
		setCurrentAction("delete", true);
		expect(model.value).toBe("delete");
	});

});
