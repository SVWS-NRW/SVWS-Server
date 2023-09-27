<template>
	<div class="svws-ui-td ml-9" role="cell">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert" class="leading-tight">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</div>
</template>

<script setup lang="ts">

	import type {BenutzergruppenManager, BenutzerKompetenz } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	}>();

	const aktiviert : ComputedRef<boolean> = computed(() => props.istAdmin);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.getBenutzergruppenManager().hatKompetenz(props.kompetenz),
		set: (value) => {
			const alt = props.getBenutzergruppenManager().hatKompetenz(props.kompetenz);
			if (alt === value)
				return;
			if (value)
				void props.addKompetenz(props.kompetenz);
			else
				void props.removeKompetenz(props.kompetenz);
		}
	});

</script>

