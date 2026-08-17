<template>
	<div class="w-full flex flex-col gap-2">
		<div class="overflow-hidden flex flex-col gap-2">
			<ui-select label="Schuljahresabschnitt" class="mt-2"
				v-model="model.selectedSchuljahresabschnitt.value"
				:manager="schuljahresabschnittManager"
				:readonly :removable="false" required />
			<ui-select v-if="model.proxy.idSchuljahresabschnitt !== -1"
				class="pl-8" label="Kategorie"
				v-model="model.selectedKategorie.value"
				:manager="kategorieManager"
				:validation="() => model.getFehler('idKategorie')"
				:readonly required />
			<ui-select v-if="model.proxy.idKategorie !== -1"
				class="pl-16" label="Merkmal"
				v-model="model.selectedMerkmal.value"
				:manager="merkmalManager"
				:validation="() => model.getFehler('idMerkmal')"
				:readonly required />
			<ui-select v-if="model.proxy.idMerkmal !== -1"
				class="pl-24" label="Zusatzmerkmal"
				v-model="model.selectedZusatzmerkmal.value"
				:manager="zusatzmerkmalManager"
				:validation="() => model.getFehler('idZusatzmerkmal')"
				:readonly required />
			<ui-select v-if="showEbene4"
				class="pl-32" label="KAoAEbene4"
				v-model="model.selectedEbene4.value"
				:manager="ebene4Manager"
				:validation="() => model.getFehler('idEbene4')"
				:readonly required />
			<ui-select v-if="showAnschlussoption"
				class="pl-32" label="KAoAAnschlussoption"
				v-model="model.selectedAnschlussoption.value"
				:manager="anschlussoptionManager"
				:validation="() => model.getFehler('idAnschlussoption')"
				:readonly required />
			<ui-select v-if="showBerufsfeld"
				class="pl-32" label="KAoABerufsfeld"
				v-model="model.selectedBerufsfeld.value"
				:manager="berufsfeldManager"
				:validation="() => model.getFehler('idBerufsfeld')"
				:readonly required />
			<div class="pl-32">
				<svws-ui-text-input v-if="showFreitext"
					placeholder="Bemerkung"
					v-model="model.proxy.bemerkung"
					:validation="() => model.getFehler('bemerkung')"
					:max-len="255" :readonly :rows="6" />
			</div>
			<slot />
		</div>
	</div>
</template>
<script setup lang="ts">
	import { computed, watch } from "vue";
	import type { SchuelerListeEintrag, Schuljahresabschnitt } from "@core";
	import { KAOAAnschlussoptionen, KAOABerufsfeld, KAOAEbene4, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal } from "@core";
	import { CoreTypeSelectManager, type SchuelerKAoAManager, SelectManager } from "@ui";
	import type { SchuelerKaoaModelProxy } from "./modelproxy/SchuelerKaoaModelProxy";

	const props = defineProps<{
		model: SchuelerKaoaModelProxy;
		manager: () => SchuelerKAoAManager;
		auswahl: () => SchuelerListeEintrag;
		readonly: boolean;
	}>();

	const schuljahresabschnitteFiltered = computed(() => props.manager().schuljahresabschnitteFiltered);
	const schuljahr = computed<number>(() => props.manager().schuljahresabschnitteById.get(props.model.proxy.idSchuljahresabschnitt)?.schuljahr ?? -1);
	const showEbene4 = computed<boolean>(() => props.model.selectedZusatzmerkmal.value?.optionsart === 'SBO_EBENE_4');
	const showAnschlussoption = computed<boolean>(() => props.model.selectedZusatzmerkmal.value?.optionsart === 'ANSCHLUSSOPTION');
	const showBerufsfeld = computed<boolean>(() => props.model.selectedZusatzmerkmal.value?.optionsart === 'BERUFSFELD');
	const showFreitext = computed<boolean>(() => (props.model.selectedZusatzmerkmal.value?.optionsart === 'FREITEXT') || (props.model.selectedZusatzmerkmal.value?.optionsart === 'FREITEXT_BERUF'));

	const schuljahresabschnittManager = new SelectManager({
		options: schuljahresabschnitteFiltered,
		optionDisplayText: v => schuljahresabschnittText(v),
		selectionDisplayText: v => schuljahresabschnittText(v),
	});

	const kategorieManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAKategorie.getEintraegeBySchuljahrAndIdJahrgang(schuljahr.value, props.model.proxy.idJahrgang),
		}],
		clazz: KAOAKategorie.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const merkmalManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAMerkmal.getEintraegeBySchuljahrAndIdKategorie(schuljahr.value, props.model.proxy.idKategorie),
		}],
		clazz: KAOAMerkmal.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const zusatzmerkmalManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAZusatzmerkmal.getEintraegeBySchuljahrAndIdMerkmal(schuljahr.value, props.model.proxy.idMerkmal),
		}],
		clazz: KAOAZusatzmerkmal.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const ebene4Manager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAEbene4.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr.value, props.model.selectedZusatzmerkmal.value?.id ?? -1),
		}],
		clazz: KAOAEbene4.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const anschlussoptionManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAAnschlussoptionen.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr.value, props.model.selectedZusatzmerkmal.value?.id ?? -1),
		}],
		clazz: KAOAAnschlussoptionen.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const berufsfeldManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOABerufsfeld.getEintraegeBySchuljahr(schuljahr.value),
		}],
		clazz: KAOABerufsfeld.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	watch(() => props.model.proxy, async () => {
		kategorieManager.updateFilteredOptions();
		merkmalManager.updateFilteredOptions();
		zusatzmerkmalManager.updateFilteredOptions();
		ebene4Manager.updateFilteredOptions();
		anschlussoptionManager.updateFilteredOptions();
		berufsfeldManager.updateFilteredOptions();
	}, { immediate: true, deep: true });

</script>

