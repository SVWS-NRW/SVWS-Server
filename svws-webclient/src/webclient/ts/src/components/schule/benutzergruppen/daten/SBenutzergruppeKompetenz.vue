<template>
	<tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }">
		<td> <svws-ui-checkbox v-model="selected" :disabled="aktiviert" /> </td>
		<td> {{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }} </td>
	</tr>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
	}>();

	const main : Main = injectMainApp();
	const app =  main.apps.benutzergruppe;

	const aktiviert : ComputedRef<boolean> = computed(() => props.istAdmin);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (app.dataBenutzergruppe.manager === undefined) ? false : app.dataBenutzergruppe.manager.hatKompetenz(props.kompetenz),
		set: (value) => {
			const alt : boolean = (app.dataBenutzergruppe.manager === undefined) ? false : app.dataBenutzergruppe.manager.hatKompetenz(props.kompetenz);
			if (alt === value)
				return;
			if (value)
				app.dataBenutzergruppe.addKompetenz(props.kompetenz);
			else
				app.dataBenutzergruppe.removeKompetenz(props.kompetenz);
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