<template>
	<template v-if="collapsed">
		<tr v-if="collapsed" style="background-color:lightblue; ">
			<td> <svws-ui-icon><i-ri-arrow-right-s-line @click="setCollapse()" /></svws-ui-icon> </td>
			<td> <svws-ui-checkbox v-model="selected" :disabled="istAdmin" /> </td>
			<td colspan="2"> {{ kompetenzgruppe.daten.bezeichnung }} </td>
		</tr>
	</template>
	<template v-else>
		<tr style="background-color:lightblue;  ">
			<td style="vertical-align: top;" :rowspan="BenutzerKompetenz.getKompetenzen(kompetenzgruppe).size()+1">
				<svws-ui-icon><i-ri-arrow-down-s-line @click="collapsed = !collapsed" /></svws-ui-icon>
			</td>
			<td><svws-ui-checkbox v-model="selected" :disabled="istAdmin" /></td>
			<td colspan="2"> {{ kompetenzgruppe.daten.bezeichnung }} </td>
		</tr>
		<s-benutzer-kompetenz v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" :kompetenz="kompetenz" :ist-admin="istAdmin" />
	</template>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { ref, Ref, computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
	}>();

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const collapsed: Ref<boolean> = ref(true);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (app.dataBenutzer.manager === undefined) ? false : app.dataBenutzer.manager.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
		set: (value) => {
			if (value)
				app.dataBenutzer.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				app.dataBenutzer.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

	function setCollapse() {
		collapsed.value = !collapsed.value;
	}

</script>
