<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
					:model-value="manager().daten().bezeichnung"
					@change="patchBezeichnung"
					:valid="bezeichnungIsValid" :max-len="50" :min-len="1" :readonly="!hatKompetenzUpdate" />
				<svws-ui-text-input placeholder="Raum"
					:model-value="manager().daten().raum"
					@change="patchRaum"
					:valid="raumIsValid" :max-len="20" :readonly="!hatKompetenzUpdate" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
					:model-value="manager().daten().email"
					@change="patchEmail"
					:valid="v => emailIsValid(v, 100)" :max-len="100" :readonly="!hatKompetenzUpdate" />
				<svws-ui-text-input placeholder="Durchwahl" type="tel"
					:model-value="manager().daten().durchwahl"
					@change="patchDurchwahl"
					:valid="durchwahlIsValid" :max-len="20" :readonly="!hatKompetenzUpdate" />
				<ui-select label="Lehrer"
					:manager="lehrerSelectManager"
					v-model="selectedLehrer"
					:readonly="!hatKompetenzUpdate" />
				<div class="mt-2.5">
					<svws-ui-button type="secondary" class="rounded-md h-fit"
						:disabled="manager().daten().idAbteilungsleiter === null"
						@click="goToLehrer(manager().daten().idAbteilungsleiter ?? -1)">
						<span class="icon i-ri-link" /> Zum Lehrer
					</svws-ui-button>
				</div>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Ansicht & Sortierung">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="selectedIsSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zugeordnete Klassen">
			<svws-ui-table :columns
				:items="manager().getKlassenByAuswahl()"
				v-model="klassenToBeDeleted"
				:selectable="hatKompetenzUpdate" count scroll>
				<template #actions v-if="hatKompetenzUpdate">
					<div class="inline-flex gap-4">
						<svws-ui-button title="Klasse löschen" type="trash"
							@click="deleteSelectedKlassen"
							:disabled="klassenToBeDeleted.length === 0" />
						<svws-ui-button title="Klasse hinzufügen" type="icon"
							@click="openModal"
							:disabled="addableKlassen.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-modal :show="modalIsOpen"
			@update:show="closeModal"
			:auto-close="false">
			<template #modalTitle>Klassen hinzufügen</template>
			<template #modalContent>
				<svws-ui-table class="max-h-[400px]"
					:columns
					:items="addableKlassen"
					v-model="klassenToBeAdded"
					selectable scroll>
					<template #actions v-if="hatKompetenzUpdate">
						<div class="inline-flex gap-4">
							<div class="mt-7 flex flex-row gap-4 justify end">
								<svws-ui-button type="secondary"
									@click="closeModal">
									Abbrechen
								</svws-ui-button>
								<svws-ui-button @click="addKlassen"
									:disabled="klassenToBeAdded.length === 0">
									Speichern
								</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/AbteilungenDatenProps";
	import type { DataTableColumn } from "@ui";
	import type { KlassenDaten, LehrerListeEintrag, List } from "@core";
	import { SelectManager } from "@ui";
	import { computed, ref } from "vue";
	import { AbteilungKlassenzuordnung, ArrayList, BenutzerKompetenz, HashMap } from "@core";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";

	const props = defineProps<AbteilungenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const columns: DataTableColumn[] = [{ key: "kuerzel", label: "Klasse" }];

	const lehrer = computed(() => props.manager().lehrerById.values());
	const lehrerSelectManager = new SelectManager({
		options: lehrer, optionDisplayText: v => v.vorname + ' ' + v.nachname,
		selectionDisplayText: v => v.vorname + ' ' + v.nachname,
	});

	const selectedIsSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	const selectedLehrer = computed<LehrerListeEintrag | null>({
		get: () => props.manager().lehrerById.get(props.manager().daten().idAbteilungsleiter ?? -1) ?? null,
		set: (v: LehrerListeEintrag | null) => void props.patch({ idAbteilungsleiter: v?.id ?? null }),
	});

	/// --- Patch ---
	async function patchBezeichnung(v: string | null) {
		if (bezeichnungIsValid(v)) {
			await props.patch({ bezeichnung: v?.trim() ?? undefined });
		}
	}

	async function patchRaum(raum: string | null) {
		if (raumIsValid(raum)) {
			await props.patch({ raum });
		}
	}

	async function patchDurchwahl(durchwahl: string | null) {
		if (durchwahlIsValid(durchwahl)) {
			await props.patch({ durchwahl });
		}
	}

	async function patchEmail(email: string | null) {
		if (emailIsValid(email, 100)) {
			await props.patch({ email });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? -1 });
		}
	}

	/// --- validate ---
	function raumIsValid(v: string | null) {
		return optionalInputIsValid(v, 20);
	}

	function durchwahlIsValid(v: string | null) {
		return phoneNumberIsValid(v, 20);
	}

	function bezeichnungIsValid(name: string | null): boolean {
		return mandatoryInputIsValid(name, 50)
			&& isUniqueInList(name, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	// --- Klassenzuordnungen ---
	const klassenToBeDeleted = ref<KlassenDaten[]>([]);
	const klassenToBeAdded = ref<KlassenDaten[]>([]);
	const addableKlassen = computed(() => {
		const allValues = [...props.manager().klassenById.values()];
		const alreadyAdded = new Set(props.manager().getKlassenByAuswahl());
		return allValues.filter(v => !alreadyAdded.has(v));
	});
	const klassenzuordnungenByIdKlasse = computed(() => {
		const result = new HashMap<number, AbteilungKlassenzuordnung>();
		for (const k of props.manager().daten().klassenzuordnungen) {
			result.put(k.idKlasse, k);
		}
		return result;
	});

	async function addKlassen() {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		if (klassenToBeAdded.value.length === 0) {
			closeModal();
			return;
		}

		const zuordnungen = createKlassenzuordnungen();
		await props.addKlassenzuordnungen(zuordnungen, props.manager().daten().id);
		isLoading.value = false;
		closeModal();
	}

	function createKlassenzuordnungen(): List<AbteilungKlassenzuordnung> {
		const klassenzuordnungen = new ArrayList<AbteilungKlassenzuordnung>();
		for (const klasse of klassenToBeAdded.value) {
			const zuordnung = new AbteilungKlassenzuordnung();
			zuordnung.idAbteilung = props.manager().daten().id;
			zuordnung.idKlasse = klasse.id;
			const { id, ...partialData } = zuordnung;
			klassenzuordnungen.add(partialData as AbteilungKlassenzuordnung);
		}
		return klassenzuordnungen;
	}

	async function deleteSelectedKlassen() {
		if (klassenToBeDeleted.value.length === 0) {
			return;
		}

		const ids = new ArrayList<number>();
		for (const k of klassenToBeDeleted.value) {
			const zuordnung = klassenzuordnungenByIdKlasse.value.get(k.id);
			if (zuordnung !== null) {
				ids.add(zuordnung.id);
			}
		}
		await props.deleteKlassenzuordnungen(ids);
		klassenToBeDeleted.value = [];
	}

	// --- Modal ---
	const isLoading = ref<boolean>(false);
	const modalIsOpen = ref<boolean>(false);

	function closeModal() {
		klassenToBeAdded.value = [];
		modalIsOpen.value = false;
	}

	function openModal() {
		klassenToBeAdded.value = [];
		modalIsOpen.value = true;
	}

</script>
