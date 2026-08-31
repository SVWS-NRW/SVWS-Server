<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
					v-model="modelProxy.proxy.bezeichnung"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('bezeichnung')"
					required :max-len="50"
					:readonly="readonly || isAbteilungImZukuenftigenAbschnitt" />
				<svws-ui-text-input placeholder="Raum"
					v-model="modelProxy.proxy.raum"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('raum')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
					v-model="modelProxy.proxy.email"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('email')"
					:max-len="100"
					:readonly />
				<svws-ui-text-input placeholder="Durchwahl" type="tel"
					v-model="modelProxy.proxy.durchwahl"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('durchwahl')"
					:max-len="20"
					:readonly />
				<ui-select label="Abteilungsleitung"
					:manager="lehrerSelectManager"
					v-model="modelProxy.abteilungsleiter.value"
					:readonly />
				<div class="mt-2.5">
					<svws-ui-button v-if="modelProxy.abteilungsleiter.value !== null" class="rounded-md h-fit"
						type="secondary"
						@click="goToLehrer(modelProxy.abteilungsleiter.value?.id ?? -1)">
						<span class="icon i-ri-link me-1" />zum Profil
					</svws-ui-button>
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Ansicht & Sortierung">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="modelProxy.proxy.sortierung"
						@change="modelProxy.patch"
						:validation="() => modelProxy.getFehler('sortierung')"
						:readonly :min="0"
						:removeable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="modelProxy.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zugeordnete Klassen">
			<div class="flex flex-col max-h-125">
				<svws-ui-table class="max-h-125"
					:columns
					:items="manager().getKlassenByAuswahl()"
					v-model="klassenToDelete"
					:selectable="!readonly" count scroll>
					<template #actions v-if="!readonly">
						<div class="inline-flex gap-4">
							<svws-ui-tooltip position="bottom">
								<svws-ui-button type="trash"
									@click="deleteSelectedKlassen"
									:disabled="klassenToDelete.length === 0" />
								<template #content>
									{{ klassenToDelete.length === 0 ? 'Keine Klassen selektiert' : 'Selektierte Klassen entfernen' }}
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip position="bottom">
								<svws-ui-button type="icon"
									@click="openModal"
									:disabled="availableKlassenToAdd.isEmpty()">
									<span class="icon i-ri-add-line" />
								</svws-ui-button>
								<template #content>
									{{ availableKlassenToAdd.isEmpty() ? 'Bereits alle Klassen zugeordnet' : 'Klassen zuordnen' }}
								</template>
							</svws-ui-tooltip>
						</div>
					</template>
				</svws-ui-table>
			</div>
		</svws-ui-content-card>
		<svws-ui-modal :show="modalIsOpen"
			@update:show="closeModal"
			:auto-close="false" size="medium">
			<template #modalTitle>Klassen zuordnen</template>
			<template #modalContent>
				<div class="flex flex-col gap-4 max-h-150">
					<svws-ui-table class="max-h-150"
						:columns
						:items="availableKlassenToAdd"
						v-model="klassenToAdd"
						selectable scroll count />
					<div class="flex gap-4 justify-end items-center">
						<svws-ui-button type="secondary"
							@click="closeModal">
							Abbrechen
						</svws-ui-button>
						<svws-ui-button @click="addSelectedKlassen"
							:disabled="klassenToAdd.length === 0">
							Hinzufügen
						</svws-ui-button>
					</div>
				</div>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/AbteilungenDatenProps";
	import type { DataTableColumn } from "@ui";
	import { SelectManager, useBenutzerState, ViewType } from "@ui";
	import type { AbteilungKlassenzuordnung, KlassenDatenMinimal, List } from "@core";
	import { Arrays, BenutzerKompetenz, HashMap } from "@core";
	import { computed, ref } from "vue";
	import { AbteilungenModelProxy } from "~/components/schule/kataloge/abteilungen/modelproxy/AbteilungenModelProxy";

	const props = defineProps<AbteilungenDatenProps>();
	const benutzerState = useBenutzerState();

	const columns: DataTableColumn[] = [{ key: "kuerzel", label: "Kürzel" }, { key: "beschreibung", label: "Beschreibung", span: 3 }];

	const isLoading = ref<boolean>(false);
	const modalIsOpen = ref<boolean>(false);
	const klassenToDelete = ref<KlassenDatenMinimal[]>([]);
	const klassenToAdd = ref<KlassenDatenMinimal[]>([]);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const readonly = computed(() => props.isReadonly || !hatKompetenzUpdate.value);
	const availableKlassenToAdd = computed<List<KlassenDatenMinimal>>(() => props.manager().getAvailableKlassenToAdd());

	const modelProxy = new AbteilungenModelProxy(() => props.manager().daten(), props.manager, ViewType.DEFAULT, props.patch);

	const lehrer = computed(() => props.manager().lehrerById.values());
	const lehrerSelectManager = new SelectManager({
		options: lehrer, optionDisplayText: v => v.vorname + ' ' + v.nachname,
		selectionDisplayText: v => v.vorname + ' ' + v.nachname,
	});

	/// --- validate ---
	async function addSelectedKlassen() {
		if (isLoading.value || (klassenToAdd.value.length === 0)) {
			return;
		}
		isLoading.value = true;

		const idsKlassen = Arrays.asList(klassenToAdd.value.map(klasse => klasse.id));
		await props.addKlassenzuordnungen(props.manager().daten().id, idsKlassen);

		isLoading.value = false;
		closeModal();
	}

	async function deleteSelectedKlassen() {
		if (klassenToDelete.value.length === 0) {
			return;
		}

		const klassenzuordnungenByIdKlasse = getKlassenzuordnungenByIdKlasse();
		const klassenzuordnungen = Arrays.asList(klassenToDelete.value
			.map(klasse => klassenzuordnungenByIdKlasse.get(klasse.id))
			.filter(zuordnung => zuordnung !== null));
		await props.deleteKlassenzuordnungen(klassenzuordnungen);
		klassenToDelete.value = [];
	}

	function getKlassenzuordnungenByIdKlasse() {
		const result = new HashMap<number, AbteilungKlassenzuordnung>();
		for (const klassenzuordnung of props.manager().daten().klassenzuordnungen) {
			result.put(klassenzuordnung.idKlasse, klassenzuordnung);
		}
		return result;
	}

	function closeModal() {
		klassenToAdd.value = [];
		modalIsOpen.value = false;
	}

	function openModal() {
		klassenToAdd.value = [];
		modalIsOpen.value = true;
	}

</script>
