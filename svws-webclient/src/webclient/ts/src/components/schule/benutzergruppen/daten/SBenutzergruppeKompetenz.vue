<template>
	<tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }">
		<td> <svws-ui-checkbox v-model="selected" :disabled="aktiviert" /> </td>
		<td> {{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }} </td>
	</tr>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
	}>();

	const aktiviert : ComputedRef<boolean> = computed(() => props.istAdmin);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (props.data.manager === undefined) ? false : props.data.manager.hatKompetenz(props.kompetenz),
		set: (value) => {
			const alt : boolean = (props.data.manager === undefined) ? false : props.data.manager.hatKompetenz(props.kompetenz);
			if (alt === value)
				return;
			if (value)
				props.data.addKompetenz(props.kompetenz);
			else
				props.data.removeKompetenz(props.kompetenz);
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