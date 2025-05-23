<template>
	<div class="svws-ui-tr kompetenz-tr" role="row">
		<div class="svws-ui-td" role="cell" :class="{'col-span-2': getBenutzerManager().istAdmin()}">
			<div class="flex items-center gap-1">
				<div type="icon" size="small" @click="collapsed = !collapsed" :class="hatSubKompetenzen ? 'cursor-pointer' : 'pointer-events-none'" :tabindex="!hatSubKompetenzen ? -1 : ''">
					<template v-if="hatSubKompetenzen">
						<span class="icon i-ri-arrow-right-s-line" v-if="collapsed" />
						<span class="icon i-ri-arrow-down-s-line" v-else />
					</template>
				</div>
				<svws-ui-checkbox v-model="selected" :indeterminate="indeterminate" :disabled="getBenutzerManager().istAdmin()">
					{{ kompetenzgruppe.daten.bezeichnung }}
				</svws-ui-checkbox>
			</div>
		</div>
		<div class="svws-ui-td" role="cell" />
		<div v-if="!getBenutzerManager().istAdmin()" class="svws-ui-td text-ui-50" role="cell">
			<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="getTopLevelGruppen4Kompetenz">{{ getTopLevelGruppen4Kompetenz }}</span>
		</div>
		<template v-if="hatSubKompetenzen">
			<div v-for="kompetenz in benutzerKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" class="svws-ui-tr kompetenz-tr" v-show="!collapsed">
				<s-benutzer-kompetenz :kompetenz :show-info :get-benutzer-manager :add-kompetenz :remove-kompetenz />
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { BenutzerKompetenzGruppe, BenutzerManager, List } from "@core";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<{
		kompetenzgruppe: BenutzerKompetenzGruppe;
		showInfo: boolean;
		getBenutzerManager: () => BenutzerManager;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		benutzerKompetenzen : ( kompetenz : BenutzerKompetenzGruppe ) => List<BenutzerKompetenz>;
	}>();

	const collapsed = ref(true);

	const hatSubKompetenzen = computed<number>(() => props.benutzerKompetenzen(props.kompetenzgruppe).size());

	const selectedHatAlle = computed<boolean>(() => {
		const kompetenzen = BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe);
		if (kompetenzen.isEmpty())
			return false;
		return props.getBenutzerManager().hatKompetenzen(kompetenzen)
	});
	const selectedMindestensEine = computed<boolean>(() => props.getBenutzerManager().hatKompetenzenMindestensEine(props.benutzerKompetenzen(props.kompetenzgruppe)));
	const indeterminate = computed<boolean>(() => !selectedHatAlle.value && selectedMindestensEine.value);

	const selected = computed<boolean>({
		get: () => selectedHatAlle.value,
		set: (value) => {
			if (value)
				void props.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
			else
				void props.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
		},
	});

	const getTopLevelGruppen4Kompetenz = computed<string>(() => {
		if (hatSubKompetenzen.value === 0)
			return props.getBenutzerManager().getBenutzerGruppenStringForKompetenzgruppe(props.kompetenzgruppe);
		const subKompetenzen = props.benutzerKompetenzen(props.kompetenzgruppe).toArray() as BenutzerKompetenz[];
		const tmp = new Set<string>();
		for (const k of subKompetenzen)
			for (const benutzergruppe of props.getBenutzerManager().getGruppen(k))
				tmp.add(benutzergruppe.bezeichnung);
		return [...tmp].join(', ');
	});

</script>

<style scoped>

	.kompetenz-tr {
		grid-template-columns: minmax(4rem, 2fr) 0.15fr minmax(4rem, 1fr);
	}

</style>
