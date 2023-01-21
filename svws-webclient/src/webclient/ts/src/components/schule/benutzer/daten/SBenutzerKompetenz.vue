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
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
	}>();

	const main : Main = injectMainApp();
	const app = main.apps.benutzer;

	// True wenn Benutzer Admin ist oder die Kompetenz von einer Gruppe geerbt wird.
	const aktiviert : ComputedRef<boolean | undefined> = computed(() =>
		app.dataBenutzer.manager?.istAdmin() || (app.dataBenutzer.manager?.getGruppen(props.kompetenz).size() !== 0)
	);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (app.dataBenutzer.manager === undefined) ? false : app.dataBenutzer.manager.hatKompetenz(props.kompetenz),
		set: (value) => {
			if (value)
				app.dataBenutzer.addKompetenz(props.kompetenz);
			else
				app.dataBenutzer.removeKompetenz(props.kompetenz);
		}
	});

	function getGruppen( kompetenz : BenutzerKompetenz ) : string {
		return app.dataBenutzer.getGruppen4Kompetenz(kompetenz);
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