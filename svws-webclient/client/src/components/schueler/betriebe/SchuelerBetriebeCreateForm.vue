<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-modal :show="createModalIsOpen"
				:auto-close="false"
				:close-in-title="false"
				size="medium">
				<template #modalTitle>
					<span>Betrieb hinzufügen</span>
				</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Betrieb"
							v-model="model.betrieb.value"
							:manager="betriebeManager"
							:validation="() => model.getFehler('idBetrieb')"
							searchable required :removable="false" />
						<div class="flex">
							<svws-ui-text-input placeholder="Vertragsbeginn" type="date"
								v-model="model.proxy.vertragsbeginn" />
							<svws-ui-text-input placeholder="Vertragsende" type="date"
								v-model="model.proxy.vertragsende" />
						</div>
						<svws-ui-spacing :size="2" />
						<ui-select label="Betreuende Lehrkraft"
							v-model="model.betreuendeLehrkraft.value"
							:manager="lehrerManager"
							:deep-search-attributes="['kuerzel']"
							searchable />
						<ui-select label="Ansprechpartner im Betrieb"
							v-model="model.ansprechpartner.value"
							:manager="ansprechpartnerManager"
							searchable />
						<svws-ui-text-input placeholder="Betreuer/Ausbilder"
							v-model="model.proxy.nameAusbilder"
							:validation="() => model.getFehler('nameAusbilder')"
							:max-len="30" />
						<ui-select label="Beschäftigungsart" v-if="istBK"
							v-model="model.beschaeftigungsart.value"
							:manager="beschaeftigungsartenManager"
							searchable />
						<div v-if="!istBK" />
						<svws-ui-spacing :size="2" />
						<svws-ui-checkbox v-model="model.proxy.erhaeltAnschreiben" class="mt-3">
							Erhält Anschreiben
						</svws-ui-checkbox>
						<div />
						<svws-ui-checkbox v-model="model.proxy.istPraktikum">
							Praktikum
						</svws-ui-checkbox>
					</svws-ui-input-wrapper>
				</template>
				<template #modalActions>
					<div class="mt-7 flex gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModal">
							Abbrechen
						</svws-ui-button>
						<svws-ui-button @click="addSchuelerBetrieb" :disabled="!formIsValid">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from 'vue';
	import { SchuelerBetriebeModelProxy } from "./modelproxy/SchuelerBetriebeModelProxy";
	import { SchuelerBetrieb } from '@core/asd/data/schueler/SchuelerBetrieb';
	import { Schulform } from '@core/asd/types/schule/Schulform';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';
	import type { SchuelerBetriebeManager } from '@ui/ui/manager/schueler/SchuelerBetriebeManager';

	const props = defineProps<{
		add: (data: Partial<SchuelerBetrieb>) => Promise<SchuelerBetrieb>,
		manager: () => SchuelerBetriebeManager;
		createModalIsOpen: boolean;
	}>();
	const schuleState = useSchuleState();

	const istBK = computed(() => {
		const erlaubteSchulformen = [Schulform.BK, Schulform.SB, Schulform.WB];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});
	const emit = defineEmits<(e: 'closeModal') => void>();
	const idSchueler = computed(() => props.manager().idSchueler);
	const model = shallowRef<SchuelerBetriebeModelProxy>(createModel());
	const formIsValid = computed(() => model.value.getAlleFehler().isEmpty());
	const betriebe = computed(() => props.manager().betriebeById.values());
	const beschaeftigungsarten = computed(() => props.manager().beschaeftigungsartenById.values());
	const lehrer = computed(() => props.manager().lehrerById.values());
	const ansprechpartner = computed(() => props.manager().ansprechpartnerById.values());

	function createModel() {
		return new SchuelerBetriebeModelProxy(
			() => Object.assign(new SchuelerBetrieb(), { idSchueler: idSchueler.value, sortierung: 32000 }), props.manager
		);
	}

	async function addSchuelerBetrieb() {
		const { id, ...partialData } = model.value.proxy;
		await props.add(partialData);
		closeModal();
	}

	function closeModal() {
		model.value = createModel();
		emit("closeModal");
	}

	const betriebeManager = new SelectManager({
		options: betriebe,
		optionDisplayText: v => v.name ?? '—',
		selectionDisplayText: v => v.name ?? '—',
	});

	const beschaeftigungsartenManager = new SelectManager({
		options: beschaeftigungsarten,
		optionDisplayText: v => v.bezeichnung ?? '—',
		selectionDisplayText: v => v.bezeichnung ?? '—',
	});

	const lehrerManager = new SelectManager({
		options: lehrer,
		optionDisplayText: v => `${v.nachname}, ${v.vorname}`,
		selectionDisplayText: v => `${v.nachname}, ${v.vorname}`,
	});

	const ansprechpartnerManager = new SelectManager({
		options: ansprechpartner,
		optionDisplayText: v => `${v.name}, ${v.rufname}`,
		selectionDisplayText: v => `${v.name}, ${v.rufname}`,
	});

</script>
