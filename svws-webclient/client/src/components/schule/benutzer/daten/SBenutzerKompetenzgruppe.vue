<template>
	<div class="svws-ui-tr" role="row">
		<div class="svws-ui-td" role="cell" :class="{'col-span-2': getBenutzerManager().istAdmin()}">
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
		</div>
		<div v-if="!getBenutzerManager().istAdmin()" class="svws-ui-td text-black/50 dark:text-white/50" role="cell">
			<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="getTopLevelGruppen4Kompetenz">{{ getTopLevelGruppen4Kompetenz }}</span>
		</div>
		<div class="svws-ui-td svws-align-right !pr-3" role="cell">
			{{ kompetenzgruppe.daten.id }}
		</div>
		<template v-if="hatSubKompetenzen">
			<div v-for="kompetenz in benutzerKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" class="svws-ui-tr" v-show="!collapsed">
				<s-benutzer-kompetenz :kompetenz="kompetenz" :get-benutzer-manager="getBenutzerManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz" />
			</div>
		</template>
	</div>
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

	const getTopLevelGruppen4Kompetenz = computed(() => {
		if (!hatSubKompetenzen.value) {
			return props.getGruppen4Kompetenz(props.kompetenzgruppe);
		}

		const subKompetenzen = [...props.benutzerKompetenzen(props.kompetenzgruppe).toArray()];

		return [...new Set(subKompetenzen.map(k => props.getGruppen4Kompetenz(k)).filter(g => g !== ''))]?.join(', ');
	});

</script>

<style scoped lang="postcss">
.svws-ui-tr {
	grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 1fr) minmax(4rem, 0.25fr);
}
</style>
