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
		<template v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
			<s-benutzergruppe-kompetenz :kompetenz="kompetenz" :ist-admin="istAdmin" :data="data"
				:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" />
		</template>
	</template>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core";
	import { ref, Ref, computed,WritableComputedRef } from "vue";

	const props = defineProps<{
		data: BenutzergruppeDaten;
		getBenutzergruppenManager: () => BenutzergruppenManager;
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>
	}>();

	const collapsed: Ref<boolean> = ref(true);


	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.getBenutzergruppenManager().hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
		set: (value) => {
			if (value)
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

	function setCollapse(){
		collapsed.value = !collapsed.value;
	}

</script>
