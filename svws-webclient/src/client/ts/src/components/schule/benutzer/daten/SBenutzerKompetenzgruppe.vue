<template>
	<div role="row"
		 class="data-table__tr data-table__tbody__tr">
		<div role="cell" class="data-table__td">
			<div class="flex items-center gap-1">
				<svws-ui-button type="icon" size="small" @click="collapsed = !collapsed">
					<i-ri-arrow-right-s-line v-if="collapsed"/>
					<i-ri-arrow-down-s-line v-else />
				</svws-ui-button>
				<svws-ui-checkbox v-model="selected" :disabled="getBenutzerManager().istAdmin()">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</div>
		</div>
		<div role="row" class="data-table__tr data-table__tbody__tr">
			<template v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
				<s-benutzer-kompetenz :kompetenz="kompetenz" :get-benutzer-manager="getBenutzerManager"
									  :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz" />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core";
	import { ref, Ref, computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		getBenutzerManager: () => BenutzerManager;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	const collapsed: Ref<boolean> = ref(true);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.getBenutzerManager().hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)),
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
