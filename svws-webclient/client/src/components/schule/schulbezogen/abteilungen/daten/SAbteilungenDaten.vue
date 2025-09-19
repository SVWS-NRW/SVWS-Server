<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					@change="v => patch({ bezeichnung: v ?? undefined })" :max-len="50" :min-len="1" :readonly />
				<svws-ui-text-input placeholder="Raum" :max-len="20" :model-value="manager().daten().raum" @change="raum => patch({ raum })" :readonly />
				<svws-ui-text-input placeholder="Email" type="email" :max-len="100" :model-value="manager().daten().email" :readonly
					@change="email => patch({ email })" />
				<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" :model-value="manager().daten().durchwahl" :readonly
					@change="durchwahl => patch({ durchwahl })" />
				<svws-ui-spacing />
				<ui-select label="Lehrer" :manager="lehrerSelectManager" :model-value="manager().getLehrer().get(manager().daten().idAbteilungsleiter)"
					:readonly @update:model-value="v => patch({ idAbteilungsleiter: v?.id ?? null })" />
				<svws-ui-button :disabled="manager().daten().idAbteilungsleiter === null" type="transparent"
					@click="goToLehrer(manager().daten().idAbteilungsleiter ?? -1)">
					<span class="icon i-ri-link" /> Zum Lehrer
				</svws-ui-button>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zugeordnete Klassen">
			<svws-ui-table :items="manager().getKlassenByAuswahl()" :columns :selectable="hatKompetenzUpdate" v-model="klassenToBeDeleted" clickable>
				<template #actions v-if="hatKompetenzUpdate">
					<div class="inline-flex gap-4">
						<svws-ui-button @click="deleteSelectedKlassen" type="trash" :disabled="klassenToBeDeleted.length === 0" />
						<svws-ui-button @click="toggleModal(true)" type="icon" title="Klasse hinzufügen">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-modal :show="showModal" @update:show="toggleModal(false)">
			<template #modalTitle>Klassen hinzufügen</template>
			<template #modalContent>
				<svws-ui-table :items="addableKlassen" :columns :selectable="true" v-model="klassenToBeAdded" :scroll="true" class="max-h-[400px]">
					<template #actions v-if="hatKompetenzUpdate">
						<div class="inline-flex gap-4">
							<div class="mt-7 flex flex-row gap-4 justify end">
								<svws-ui-button type="secondary" @click="toggleModal(false)">Abbrechen</svws-ui-button>
								<svws-ui-button @click="addKlassen" :disabled="klassenToBeAdded.length === 0">Speichern</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenDatenProps } from "~/components/schule/schulbezogen/abteilungen/daten/SAbteilungenDatenProps";
	import type { DataTableColumn} from "@ui";
	import type { KlassenDaten, List } from "@core";
	import { SelectManager } from "@ui";
	import { computed, ref } from "vue";
	import { AbteilungKlassenzuordnung, ArrayList, BenutzerKompetenz, HashMap } from "@core";

	const props = defineProps<AbteilungenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);
	const klassenToBeDeleted = ref<KlassenDaten[]>([]);
	const klassenToBeAdded = ref<KlassenDaten[]>([]);
	const lehrer = computed(() => props.manager().getLehrer().values());

	const lehrerSelectManager = new SelectManager({
		options: lehrer, optionDisplayText: v => v.vorname + ' ' + v.nachname,
		selectionDisplayText: v => v.vorname + ' ' + v.nachname,
	});
	const columns: DataTableColumn[] = [ { key: "kuerzel", label: "Klasse"} ];

	const klassenzuordnungenByIdKlasse = computed(() => {
		const result = new HashMap<number, AbteilungKlassenzuordnung>();
		for (const k of props.manager().daten().klassenzuordnungen)
			result.put(k.idKlasse, k)
		return result;
	});

	const addableKlassen = computed(() => {
		const allValues = [...props.manager().getKlassen().values()];
		const alreadyAdded = [...props.manager().getKlassenByAuswahl()];
		return allValues.filter(v => !alreadyAdded.includes(v));
	})

	async function deleteSelectedKlassen() {
		if (klassenToBeDeleted.value.length === 0)
			return;

		const ids = new ArrayList<number>();
		for (const k of klassenToBeDeleted.value) {
			const zuordnung = klassenzuordnungenByIdKlasse.value.get(k.id);
			if (zuordnung !== null)
				ids.add(zuordnung.id)
		}
		await props.deleteKlassenzuordnungen(ids);
		klassenToBeDeleted.value = [];
	}

	const isLoading = ref<boolean>(false);
	const showModal = ref<boolean>(false);
	function toggleModal(isOpen: boolean) {
		klassenToBeAdded.value = [];
		showModal.value = isOpen;
	}

	async function addKlassen() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		const zuordnungen = createKlassenzuordnungen();
		await props.addKlassenzuordnungen(zuordnungen, props.manager().daten().id);
		isLoading.value = false;
		toggleModal(false);
	}

	function createKlassenzuordnungen(): List<AbteilungKlassenzuordnung> {
		if (klassenToBeAdded.value.length === 0)
			return new ArrayList<AbteilungKlassenzuordnung>();

		const klassenzuordnungen = new ArrayList<AbteilungKlassenzuordnung>();
		for (const klasse of klassenToBeAdded.value ) {
			const zuordnung = new AbteilungKlassenzuordnung()
			zuordnung.idAbteilung = props.manager().daten().id;
			zuordnung.idKlasse = klasse.id;
			const { id, ...partialData } = zuordnung;
			klassenzuordnungen.add(partialData as AbteilungKlassenzuordnung);
		}
		return klassenzuordnungen;
	}

</script>
