<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-kaoa />
		</svws-ui-modal-hilfe>
	</Teleport>


	<div class="w-full overflow-y-auto">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-content-card class="col-span-full">
					<!-- Anlegen Button !-->
					<div class="grid justify-items-end mr-2 pb-4">
						<svws-ui-button title="AddButton" class="contentFocusField min-h-8"
							@click="createAddModel"
							:disabled="!hatKompetenzUpdate || (createKaoaModel !== undefined)">
							<span class="icon i-ri-chat-new-line" />
							<span class="ml-2">Neuen Eintrag anlegen</span>
						</svws-ui-button>
					</div>

					<!-- UI Card zum Erstellen eines neuen Eintrags  !-->
					<div v-if="createKaoaModel !== undefined" class="pb-4">
						<ui-card :title="cardTitle(createKaoaModel)"
							:is-open="true"
							:collapsible="false">
							<template #info v-if="(abschnittState.auswahl)">
								{{ schuljahresabschnittText(abschnittState.auswahl) }}
							</template>
							<kaoa-form :model="createKaoaModel"
								:manager
								:auswahl
								:readonly="!hatKompetenzUpdate" />
							<template #buttonFooterRight>
								<div class="mt-7 flex flex-row gap-4 justify-end">
									<svws-ui-button type="secondary"
										@click="resetCreateModel">
										Abbrechen
									</svws-ui-button>
									<svws-ui-button @click="addEntry"
										:disabled="createKaoaModel.hatBlockierendeFehler() || !hatKompetenzUpdate">
										Speichern
									</svws-ui-button>
								</div>
							</template>
						</ui-card>
					</div>

					<!-- UI Cards zum Anzeigen und Patchen !-->
					<div class="space-y-2!">
						<ui-card v-for="kaoaModel of patchKaoaModels" :key="kaoaModel.proxy.id"
							:title="cardTitle(kaoaModel)"
							:info="schuljahresabschnittText(kaoaModel.selectedSchuljahresabschnitt.value)">
							<kaoa-form :model="kaoaModel"
								:manager
								:auswahl
								:readonly="!hatKompetenzUpdate" />
							<template #buttonFooterRight>
								<div class="mt-7 flex flex-row gap-4 justify-end">
									<svws-ui-button v-if="Object.keys(kaoaModel.pending).length > 0"
										@click="kaoaModel.patch"
										:disabled="kaoaModel.hatBlockierendeFehler() || !hatKompetenzUpdate">
										Änderung speichern
									</svws-ui-button>
									<svws-ui-button v-if="Object.keys(kaoaModel.pending).length > 0"
										type="secondary"
										@click="kaoaModel.pending = {}">
										Abbrechen
									</svws-ui-button>
									<svws-ui-button type="danger"
										@click="props.delete(auswahl().id, kaoaModel.proxy.id)" :disabled="!hatKompetenzUpdate">
										Löschen
									</svws-ui-button>
								</div>
							</template>
						</ui-card>
					</div>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from 'vue';
	import type { Schuljahresabschnitt } from "@core";
	import { BenutzerKompetenz, Jahrgaenge, SchuelerKAoADaten } from "@core";
	import { useBenutzerState, useModelProxyList, useAbschnittState } from '@ui';
	import { SchuelerKaoaModelProxy } from "./modelproxy/SchuelerKaoaModelProxy";
	import type { SchuelerKAoAProps } from './SchuelerKaoaProps';

	const props = defineProps<SchuelerKAoAProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN));

	const createKaoaModel = shallowRef<SchuelerKaoaModelProxy>();
	const patchKaoaModels = useModelProxyList(
		() => props.manager().kAoADatenById.values(),
		(kaoa) => kaoa.id,
		(kaoa) => new SchuelerKaoaModelProxy(() => kaoa, props.manager, (data) => props.patch(data, kaoa.id))
	);

	function createAddModel() {
		const kaoaDaten = new SchuelerKAoADaten();
		const kuerzelJahrgang = props.manager().lernabschnitteBySchuljahr.get(abschnittState.auswahl.schuljahr)?.jahrgang ?? '';
		kaoaDaten.idJahrgang = Jahrgaenge.data().getWertByKuerzel(kuerzelJahrgang)?.daten(abschnittState.auswahl.schuljahr)?.id ?? -1;
		kaoaDaten.idSchuljahresabschnitt = abschnittState.auswahl.id;

		createKaoaModel.value = new SchuelerKaoaModelProxy(() => kaoaDaten, () => props.manager());
	}

	// --- header ---

	function schuljahresabschnittText(value: Schuljahresabschnitt | null) {
		if (value === null) {
			return "-";
		}
		return (value.schuljahr > 0) ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	function cardTitle(kaoaDaten: SchuelerKaoaModelProxy) {
		return getKuerzel(kaoaDaten) + " " + getBezeichnung(kaoaDaten);
	}

	function getKuerzel(kaoaDaten: SchuelerKaoaModelProxy | undefined): string {
		const eintrag = kaoaDaten?.selectedZusatzmerkmal.value
			?? kaoaDaten?.selectedMerkmal.value
			?? kaoaDaten?.selectedKategorie.value;
		return eintrag?.kuerzel ?? "SBO";
	}

	function getBezeichnung(kaoaDaten: SchuelerKaoaModelProxy) {
		return kaoaDaten.selectedKategorie.value?.text ?? "";
	}

	// --- api ---

	async function addEntry() {
		if (createKaoaModel.value === undefined) {
			return;
		}
		const { id, ...partialData } = createKaoaModel.value.proxy;
		await props.add(partialData, props.auswahl().id);
		resetCreateModel();
	}

	function resetCreateModel() {
		createKaoaModel.value = undefined;
	}

</script>
