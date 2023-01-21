<template>
	<template v-if="collapsed">
		<tr v-if="collapsed" style="background-color:lightblue; ">
			<td> <svws-ui-icon><i-ri-arrow-right-s-line @click="setCollapse()" /></svws-ui-icon> </td>
			<td> <svws-ui-checkbox v-model="selected" :disabled="istAdmin" /> </td>
			<td> {{ kompetenzgruppe.daten.bezeichnung }} </td>
		</tr>
	</template>
	<template v-else>
		<tr style="background-color:lightblue;  ">
			<td style="vertical-align: top;" :rowspan="BenutzerKompetenz.getKompetenzen(kompetenzgruppe).size()+1">
				<svws-ui-icon><i-ri-arrow-down-s-line @click="collapsed = !collapsed" /></svws-ui-icon>
			</td>
			<td><svws-ui-checkbox v-model="selected" :disabled="istAdmin" /></td>
			<td> {{ kompetenzgruppe.daten.bezeichnung }} </td>
		</tr>
		<s-benutzergruppe-kompetenz v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" :kompetenz="kompetenz" :ist-admin="istAdmin" />
	</template>
</template>

<script setup lang="ts">

	import { BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { ref, Ref, computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
	}>();

	const main: Main = injectMainApp();
	const app =  main.apps.benutzergruppe;

	const collapsed: Ref<boolean> = ref(true);

	const manager: ComputedRef<BenutzergruppenManager | BenutzerManager| undefined> = computed(() => {
		return app.dataBenutzergruppe.manager;
	});

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (manager.value === undefined) ? false : manager.value.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
		set: (value) => {
			if (value)
				app.dataBenutzergruppe.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				app.dataBenutzergruppe.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

	function setCollapse(){
		collapsed.value = !collapsed.value;
	}

</script>
