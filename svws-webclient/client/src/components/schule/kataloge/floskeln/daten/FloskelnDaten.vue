<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="10" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-textarea-input placeholder="Text" span="full"
						v-model="model.proxy.text"
						:validation="() => model.getFehler('text')"
						@change="model.patch" @keydown.enter.prevent
						:readonly="!hatKompetenzUpdate" required autoresize resizeable="none" />
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Floskelgruppe"
							v-model="model.selectedFloskelgruppe.value"
							:manager="floskelgruppenManager"
							:removable="false" required :readonly="!hatKompetenzUpdate" />
						<ui-select v-if="model.hatFloskelgruppeArtFach.value" label="Fach"
							v-model="model.selectedFach.value"
							:manager="faecherManager"
							searchable :readonly="!hatKompetenzUpdate" />
						<div v-else />
						<ui-select label="Jahrgang"
							v-model="model.selectedJahrgang.value"
							:manager="jahrgaengeManager"
							:readonly="!hatKompetenzUpdate" removable />
						<ui-select label="Niveau"
							v-model="model.selectedNiveau.value"
							:manager="niveauManager"
							:readonly="!hatKompetenzUpdate" />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sortierung -->
			<svws-ui-content-card title="Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000" required />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { computed } from "vue";
	import { FloskelModelProxy } from "~/components/schule/kataloge/floskeln/modelproxy/FloskelModelProxy";
	import type { FloskelnDatenProps } from "./FloskelnDatenProps";

	const props = defineProps<FloskelnDatenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new FloskelModelProxy(
		() => props.manager().daten(),
		() => props.manager().liste.list(),
		props.manager,
		props.patch
	);

	// --- manager ---

	const floskelgruppenManager = new SelectManager<Floskelgruppe>({
		options: computed(() => props.manager().floskelgruppenById.values()),
		optionDisplayText: (v: Floskelgruppe) => v.bezeichnung,
		selectionDisplayText: (v: Floskelgruppe) => v.bezeichnung,
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: computed<FachDaten[]>(() => [...props.manager().faecherById.values()]),
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: computed<JahrgangsDaten[]>(() => [...props.manager().jahrgaengeById.values()]),
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});

	const niveauManager = new SelectManager<number>({
		options: computed(() => props.manager().niveaus),
		optionDisplayText: String,
		selectionDisplayText: String,
	});
</script>
