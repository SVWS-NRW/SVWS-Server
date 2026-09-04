<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-erziehungsberechtigte /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<schueler-erziehungsberechtigte-table v-bind="props" size="2"
				:hat-kompetenz-update
				v-model:erzieher="erzieher"
				v-model:selected-erz="selectedErz"
				@delete-erzieher="deleteErzieherRequest"
				@add-modal="addModal"
				@open-modal-for-pos2="openModalForPos2" />
			<schueler-erziehungsberechtigte-patch-form v-if="erzieher !== undefined" :key="erzieher.id"
				v-bind="props"
				:erzieher
				:schuljahr
				:hat-kompetenz-update
				:patch="props.patchErzieher" />
			<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
			<schueler-erziehungsberechtigte-patch-pos2-modal :key="pos2SourceId"
				v-bind="props"
				:show="showPatchPosModal"
				:zweiter-erz
				:pos2-source-id
				:schuljahr
				:hat-kompetenz-update
				@close-modal="closeModal" />

			<schueler-erziehungsberechtigte-create-form v-bind="props"
				:schuljahr
				:create-modal-is-open
				@close-modal="closeCreateModal" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { SchuelerErziehungsberechtigteProps } from "./SchuelerErziehungsberechtigteProps";
	import { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const schuljahr = computed<number>(() => abschnittState.auswahl.schuljahr);
	const erzieher = ref<ErzieherStammdaten | undefined>();
	const selectedErz = ref<ErzieherStammdaten[]>([]);
	const zweiterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());
	const createModalIsOpen = ref(false);
	const showPatchPosModal = ref(false);
	const pos2SourceId = ref(0);

	function addModal() {
		createModalIsOpen.value = true;
	}

	function closeCreateModal() {
		createModalIsOpen.value = false;
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
		showPatchPosModal.value = true;
	}

	function closeModal() {
		zweiterErz.value = new ErzieherStammdaten();
		showPatchPosModal.value = false;
	}

	async function deleteErzieherRequest() {
		if (selectedErz.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedErz.value) {
			ids.add(s.id);
		}
		await props.deleteErzieher(ids);
		selectedErz.value = [];
	}

	watch(() => props.data(), (neu) => {
		if (neu.isEmpty()) {
			erzieher.value = undefined;
		} else if (erzieher.value === undefined) {
			erzieher.value = neu.getFirst();
		} else {
			const current = Array.from(neu).find(e => e.id === erzieher.value?.id);
			erzieher.value = current ?? neu.getFirst();
		}
	}, { immediate: true });

</script>
