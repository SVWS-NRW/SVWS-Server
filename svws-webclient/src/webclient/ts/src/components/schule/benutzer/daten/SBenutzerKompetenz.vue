<template>
	<tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }">
		<td colspan="2">
			<svws-ui-checkbox v-model="selected" :disabled="aktiviert">
				{{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }}
			</svws-ui-checkbox>
		</td>
		<!-- <td> {{ kompetenz.daten.id }}-{{ kompetenz.daten.bezeichnung }} </td> -->
		<!-- TODO Die Methode in Manager auslagern. -->
		<td> {{ props.getGruppen4Kompetenz(kompetenz) }} </td>
	</tr>
</template>

<script setup lang="ts">
	import { BenutzerKompetenz, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	const props = defineProps<{
		manager : BenutzerManager;
		kompetenz: BenutzerKompetenz;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	// True wenn Benutzer Admin ist oder die Kompetenz von einer Gruppe geerbt wird.
	const aktiviert : ComputedRef<boolean | undefined> = computed(() => {
		return props.manager.istAdmin() || (props.manager.getGruppen(props.kompetenz).size() !== 0)
	});

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (props.manager === undefined) ? false : props.manager.hatKompetenz(props.kompetenz),
		set: (value) => {
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