<template>
	<tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }">
		<td> <svws-ui-checkbox v-model="selected" :disabled="aktiviert" /> </td>
		<td> {{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }} </td>
		<td> {{ getGruppen(kompetenz) }} </td>
	</tr>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";

	const props = defineProps<{
		data: DataBenutzer;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
	}>();

	// True wenn Benutzer Admin ist oder die Kompetenz von einer Gruppe geerbt wird.
	const aktiviert : ComputedRef<boolean | undefined> = computed(() =>
		props.data.manager?.istAdmin() || (props.data.manager?.getGruppen(props.kompetenz).size() !== 0)
	);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (props.data.manager === undefined) ? false : props.data.manager.hatKompetenz(props.kompetenz),
		set: (value) => {
			if (value)
				void props.data.addKompetenz(props.kompetenz);
			else
				void props.data.removeKompetenz(props.kompetenz);
		}
	});

	function getGruppen( kompetenz : BenutzerKompetenz ) : string {
		return props.data.getGruppen4Kompetenz(kompetenz);
	}

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