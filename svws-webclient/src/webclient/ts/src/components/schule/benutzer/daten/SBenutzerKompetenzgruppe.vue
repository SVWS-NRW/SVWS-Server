<template>
	<template v-if="collapsed">
		<tr v-if="collapsed" style="background-color:lightblue; ">
			<td> <svws-ui-icon><i-ri-arrow-right-s-line @click="setCollapse()" /></svws-ui-icon> </td>
			<td colspan="3">
				<svws-ui-checkbox v-model="selected" :disabled="manager.istAdmin()">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</td>
		</tr>
	</template>
	<template v-else>
		<tr style="background-color:lightblue;  ">
			<td style="vertical-align: top;" :rowspan="BenutzerKompetenz.getKompetenzen(kompetenzgruppe).size()+1">
				<svws-ui-icon><i-ri-arrow-down-s-line @click="collapsed = !collapsed" /></svws-ui-icon>
			</td>
			<td colspan="3">
				<svws-ui-checkbox v-model="selected" :disabled="manager.istAdmin()">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</td>
		</tr>
		<template v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
			<s-benutzer-kompetenz :kompetenz="kompetenz" :manager="manager"
				:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz" />
		</template>
	</template>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { ref, Ref, computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		manager : BenutzerManager;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	const collapsed: Ref<boolean> = ref(true);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.manager.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
		set: (value) => {
			if (value)
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

	function setCollapse() {
		collapsed.value = !collapsed.value;
	}

</script>
