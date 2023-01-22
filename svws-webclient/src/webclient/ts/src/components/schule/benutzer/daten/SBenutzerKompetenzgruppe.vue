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
		<template v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
			<s-benutzer-kompetenz :kompetenz="kompetenz" :ist-admin="istAdmin" :data="data" />
		</template>
	</template>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { ref, Ref, computed, WritableComputedRef } from "vue";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";

	const props = defineProps<{
		data: DataBenutzer;
		kompetenzgruppe: BenutzerKompetenzGruppe;
		istAdmin: boolean;
	}>();

	const collapsed: Ref<boolean> = ref(true);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => (props.data.manager === undefined) ? false : props.data.manager.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
		set: (value) => {
			if (value)
				props.data.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				props.data.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

	function setCollapse() {
		collapsed.value = !collapsed.value;
	}

</script>
