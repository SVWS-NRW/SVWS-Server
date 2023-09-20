<template>
	<svws-ui-table-row>
		<svws-ui-table-cell>
			<div class="flex items-center gap-1">
				<svws-ui-button type="icon" size="small" @click="collapsed = !collapsed" :class="{'pointer-events-none': !hatSubKompetenzen}" :tabindex="!hatSubKompetenzen ? -1 : ''">
					<template v-if="hatSubKompetenzen">
						<i-ri-arrow-right-s-line v-if="collapsed" />
						<i-ri-arrow-down-s-line v-else />
					</template>
				</svws-ui-button>
				<svws-ui-checkbox v-model="selected" :value="selectedMindestensEine && !selected ? 'indeterminate' : selected" :disabled="getBenutzerManager().istAdmin()">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</div>
		</svws-ui-table-cell>
		<svws-ui-table-cell class="text-black/50 dark:text-white/50">
			<span class="inline-flex gap-1" v-if="getBenutzerManager().istAdmin()">
				<i-ri-shield-star-line class="opacity-50 -mt-0.5" />
				<span>Admin</span>
			</span>
		</svws-ui-table-cell>
		<svws-ui-table-cell class="font-mono" :class="{'text-black/50 dark:text-white/50': getBenutzerManager().istAdmin()}">
			{{ kompetenzgruppe.daten.id }}
		</svws-ui-table-cell>
	</svws-ui-table-row>
	<svws-ui-table-row :depth="1" :collapsed="collapsed" :expanded="!collapsed" v-for="kompetenz in benutzerKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id">
		<s-benutzer-kompetenz :kompetenz="kompetenz" :get-benutzer-manager="getBenutzerManager"
			:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz" />
	</svws-ui-table-row>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenzGruppe, BenutzerManager, List } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { ref, computed } from "vue";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		getBenutzerManager: () => BenutzerManager;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
		benutzerKompetenzen : ( kompetenz : BenutzerKompetenzGruppe ) => List<BenutzerKompetenz>;
	}>();

	const collapsed = ref(true);

	const hatSubKompetenzen = computed<number>(() => props.benutzerKompetenzen(props.kompetenzgruppe).size());

	const selectedMindestensEine = computed<boolean>(() => props.getBenutzerManager().hatKompetenzenMindestensEine(props.benutzerKompetenzen(props.kompetenzgruppe)));

	const selected = computed<'indeterminate' | boolean>({
		get: () => props.getBenutzerManager().hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)) || (selectedMindestensEine.value ? 'indeterminate' : false),
		set: (value) => {
			if (value)
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		}
	});

</script>

<style scoped lang="postcss">
.data-table__tr {
	grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 1fr) minmax(4rem, 0.25fr);
}
</style>
