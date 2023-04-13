<template>
	<div role="row"
		class="data-table__tr data-table__tbody__tr">
		<div role="row" class="data-table__tr data-table__tbody__tr col-span-full">
			<div role="cell" class="data-table__td">
				<div class="flex items-center gap-1">
					<svws-ui-button type="icon" size="small" @click="collapsed = !collapsed" :class="{'pointer-events-none': !hatSubKompetenzen}" :tabindex="!hatSubKompetenzen ? -1 : ''">
						<template v-if="hatSubKompetenzen">
							<i-ri-arrow-right-s-line v-if="collapsed" />
							<i-ri-arrow-down-s-line v-else />
						</template>
					</svws-ui-button>
					<!--TODO: Intermediate state wenn mindestens ein Unterpunkt true ist-->
					<svws-ui-checkbox v-model="selected" :disabled="getBenutzerManager().istAdmin()">
						{{ kompetenzgruppe.daten.bezeichnung }}
					</svws-ui-checkbox>
				</div>
			</div>
			<div role="cell" class="data-table__td text-black/50">
				–
			</div>
			<div role="cell" class="data-table__td" :title="getBenutzerManager().istAdmin() ? 'Administrator' : ''">
				<template v-if="getBenutzerManager().istAdmin()">
					<i-ri-shield-star-line class="opacity-50" />
				</template>
			</div>
		</div>
		<div role="row" class="data-table__tr data-table__tbody__tr" :class="{'data-table__tr__collapsed': collapsed, 'data-table__tr__expanded': !collapsed}"
			v-for="kompetenz in BenutzerKompetenz.getKompetenzenMitSchulform(kompetenzgruppe,api.schulform)" :key="kompetenz.daten.id">
			<s-benutzer-kompetenz :kompetenz="kompetenz" :get-benutzer-manager="getBenutzerManager"
				:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core";
	import { BenutzerKompetenz } from "@svws-nrw/svws-core";
	import type { Ref, WritableComputedRef } from "vue";
	import { ref, computed } from "vue";
	import { api } from "~/router/Api";

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

	const hatSubKompetenzen: WritableComputedRef<number> = computed(() => BenutzerKompetenz.getKompetenzenMitSchulform(props.kompetenzgruppe,api.schulform).size());

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

<style scoped lang="postcss">
.data-table__tr {
	grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 0.5fr) minmax(4rem, 1fr);
}

.checkbox--checked:not(.checkbox--disabled) {
	@apply text-primary;
}
</style>
