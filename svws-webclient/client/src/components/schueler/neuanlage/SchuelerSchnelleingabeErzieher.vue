<template>
	<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
		<schueler-erziehungsberechtigte-table :data="props.getErzieher"
			:erzieherarten-by-id="manager().erzieherartenById"
			:hat-kompetenz-update="updateKompetenz"
			v-model:erzieher="data"
			v-model:selected-erz="selectedData"
			@delete-erzieher="deleteEntry"
			@add-modal="add"
			@open-modal-for-pos2="openModalForPos2" />
		<schueler-erziehungsberechtigte-patch-form v-if="data !== undefined"
			:key="data.id"
			:erzieher="data"
			:erzieherarten-by-id="manager().erzieherartenById"
			:schuljahr
			:hat-kompetenz-update="updateKompetenz"
			:patch="props.patchErzieher" />
		<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
		<svws-ui-modal :show="patchPosModalErzIsShown" @update:show="closeModal">
			<template #modalTitle>Einen zweiten Erziehungsberechtigten hinzufügen</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2" class="text-left">
					<svws-ui-text-input placeholder="Anrede"
						v-model="zweiterErz.anrede" />
					<svws-ui-text-input placeholder="Titel"
						v-model="zweiterErz.titel" />
					<svws-ui-text-input placeholder="Vorname"
						v-model="zweiterErz.vorname"
						required />
					<svws-ui-text-input placeholder="Nachname"
						v-model="zweiterErz.nachname"
						required />
					<svws-ui-text-input placeholder="E-Mail Adresse" type="email"
						v-model="zweiterErz.eMail"
						verify-email />
					<ui-select label="Staatsangehörigkeit"
						v-model="zweiteErzStaatsangehoerigkeit"
						:manager="staatsangehoerigkeitenManager"
						:removable="false" />
				</svws-ui-input-wrapper>
				<div v-if="updateKompetenz" class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="addSecondErzieher"
						:disabled="(!mandatoryInputIsValid(zweiterErz.vorname, 80))
							|| (!mandatoryInputIsValid(zweiterErz.nachname, 120))">
						Zweiten Erzieher speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
		<schueler-erziehungsberechtigte-create-form :add-erzieher="addErzieherWrapper"
			:patch-erzieher-an-position="patchErzieherAnPositionWrapper"
			:erzieherarten-by-id="manager().erzieherartenById"
			:schuljahr
			:create-modal-is-open
			@close-modal="() => createModalIsOpen = false" />
	</svws-ui-content-card>
</template>
<script setup lang="ts">

	import type { List, NationalitaetenKatalogEintrag } from "@core";
	import { ArrayList, ErzieherStammdaten, Nationalitaeten } from "@core";
	import type { SchuelerSchnelleingabeManager } from "@ui";
	import { CoreTypeSelectManager } from "@ui";
	import { computed, ref, watch } from "vue";
	import { mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		getErzieher: () => List<ErzieherStammdaten>;
		addErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number) => Promise<ErzieherStammdaten>;
		patchErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
		patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => Promise<void>;
		deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
		schuljahr: number;
		updateKompetenz: boolean;
	}>();

	const manager = () => props.manager();
	const data = ref<ErzieherStammdaten | undefined>();
	const selectedData = ref<ErzieherStammdaten[]>([]);
	const zweiterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());
	const createModalIsOpen = ref(false);
	const pos2SourceId = ref(0);
	const patchPosModalErzIsShown = ref(false);

	const zweiteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.getByISO3(zweiterErz.value.staatsangehoerigkeitID ?? null)?.daten(props.schuljahr) ?? null,
		set: (value: NationalitaetenKatalogEintrag | null) => {
			zweiterErz.value.staatsangehoerigkeitID = value?.iso3 ?? null;
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: props.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const addErzieherWrapper = (data: Partial<ErzieherStammdaten>, pos: number) =>
		props.addErzieher(data, manager().stammdaten.id, pos);
	const patchErzieherAnPositionWrapper = (data: Partial<ErzieherStammdaten>, id: number, pos: number) =>
		props.patchErzieherAnPosition(data, id, manager().stammdaten.id, pos);

	function add() {
		createModalIsOpen.value = true;
	}

	async function deleteEntry() {
		if (selectedData.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedData.value) {
			ids.add(s.id);
		}
		await props.deleteErzieher(ids);
		selectedData.value = [];
		data.value = undefined;
	}

	async function addSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...partialDataWithoutId } = zweiterErz.value;
		const schuelerId = manager().stammdaten.id;
		await props.patchErzieherAnPosition(partialDataWithoutId, pos2SourceId.value, schuelerId, 2);
		patchPosModalErzIsShown.value = false;
	}

	function closeModal() {
		zweiterErz.value = new ErzieherStammdaten();
		patchPosModalErzIsShown.value = false;
	}

	async function openModalForPos2(item: ErzieherStammdaten) {
		pos2SourceId.value = item.id;
		zweiterErz.value = new ErzieherStammdaten();
		zweiterErz.value.idErzieherArt = item.idErzieherArt;
		zweiterErz.value.wohnortID = item.wohnortID;
		zweiterErz.value.ortsteilID = item.ortsteilID;
		zweiterErz.value.bemerkungen = item.bemerkungen;
		zweiterErz.value.strassenname = item.strassenname;
		zweiterErz.value.hausnummer = item.hausnummer;
		zweiterErz.value.hausnummerZusatz = item.hausnummerZusatz;
		patchPosModalErzIsShown.value = true;
	}

	watch(() => props.getErzieher(), (neu) => {
		if (neu.isEmpty()) {
			data.value = undefined;
		} else if (data.value === undefined) {
			data.value = neu.getFirst();
		} else {
			const current = Array.from(neu).find(e => e.id === data.value?.id);
			data.value = current ?? neu.getFirst();
		}
	}, { immediate: true });

</script>
