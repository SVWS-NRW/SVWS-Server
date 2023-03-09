<template>
	<tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }">
		<td> <svws-ui-checkbox v-model="selected" :disabled="aktiviert" /> </td>
		<td> {{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }} </td>
	</tr>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzergruppenManager, BenutzerKompetenz } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	const props = defineProps<{
		data: BenutzergruppeDaten;
		getBenutzergruppenManager: () => BenutzergruppenManager;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
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

<style scoped>
	.vorhanden{
		background-color: aquamarine;
	}

	.nichtvorhanden{
		background-color: white;
	}
	.deaktiviert{
		background-color: rgb(220, 220, 220);
	}
</style>