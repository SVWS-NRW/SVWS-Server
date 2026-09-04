<template>
	<ui-card v-if="show" :icon :title :subtitle :fehler :is-open :collapsible @update:is-open="updateIsOpen">
		<template v-if="$slots.title" #title>
			<slot name="title" />
		</template>
		<template v-if="$slots.subtitle" #subtitle>
			<slot name="subtitle" />
		</template>
		<slot />
		<template v-if="$slots.buttonFooterLeft" #buttonFooterLeft>
			<slot name="buttonFooterLeft" />
		</template>
	</ui-card>
</template>

<script lang="ts">
	export type KlausurplanungProblemId =
		| "stundenplan_fehlend"
		| "vorgaben_fehlend"
		| "schuelerklausurmenge_abweichend"
		| "kursklausuren_fehlend"
		| "kursklausuren_nicht_verteilt"
		| "termine_ohne_stundenplan"
		| "klausurtermine_mit_schuelerkonflikten"
		| "termine_ohne_datum"
		| "termine_ohne_raumplanung"
		| "termine_raumkapazität"
		| "nachschreibklausuren_nicht_zugewiesen"
		| "konflikt_drei_wochenklausuren"
		| "konflikt_vier_wochenklausuren";
</script>

<script setup lang="ts">
	import type { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import { computed } from "vue";

	const props = withDefaults(defineProps<{
		show?: boolean;
		icon: string;
		title?: string;
		subtitle?: string;
		fehler: ValidatorFehlerart;
		problemId: KlausurplanungProblemId;
		currentAction: KlausurplanungProblemId | "";
		canOpen?: boolean;
		collapsible?: boolean;
	}>(), {
		show: true,
		title: undefined,
		subtitle: undefined,
		canOpen: true,
		collapsible: undefined,
	});

	const emit = defineEmits<{
		"update:currentAction": [value: KlausurplanungProblemId | ""];
	}>();

	const isOpen = computed<boolean>(() => props.canOpen && (props.currentAction === props.problemId));

	function updateIsOpen(open: boolean): void {
		if (open && props.canOpen) {
			emit("update:currentAction", props.problemId);
			return;
		}
		if (!open && (props.currentAction === props.problemId)) {
			emit("update:currentAction", "");
		}
	}
</script>
