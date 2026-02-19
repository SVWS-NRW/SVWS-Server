<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						:model-value="manager().daten().kuerzel"
						@change="patchKuerzel"
						:valid="kuerzelIsValid"
						:min-len="1" :max-len="10" :readonly required />
					<svws-ui-textarea-input placeholder="Text" span="full"
						:model-value="manager().daten().text"
						@change="patchText" @keydown.enter.prevent
						:valid="textIsValid"
						:disabled="readonly" required autoresize resizeable="none" />
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Floskelgruppe"
							v-model="selectedFloskelgruppe"
							:manager="floskelgruppenManager"
							:removable="false" searchable required :readonly />
						<ui-select v-if="hatFloskelgruppeArtFach" label="Fach"
							v-model="selectedFach"
							:manager="faecherManager"
							searchable :readonly />
						<div v-else />
						<ui-select label="Jahrgang"
							v-model="selectedJahrgang"
							:manager="jahrgaengeManager"
							searchable :readonly />
						<ui-select label="Niveau"
							v-model="selectedNiveau"
							:manager="niveauManager"
							:readonly />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Ansicht & Sortierung -->
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:readonly :min="0" :max="32000" />
					<svws-ui-spacing />
					<svws-ui-checkbox :model-value="manager().daten().istSichtbar">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FachDaten, Floskelgruppe, JahrgangsDaten } from "@core";
	import { ArrayList, BenutzerKompetenz, Floskelgruppenart } from "@core";
	import type { FloskelnDatenProps } from "./FloskelnDatenProps";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";
	import { FloskelnListeManager, SelectManager } from "@ui";

	const props = defineProps<FloskelnDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	const floskelgruppen = computed(() => props.manager().floskelgruppenById.values());
	const selectedFloskelgruppe = computed<Floskelgruppe | null>({
		get: () => props.manager().floskelgruppenById.get(props.manager().auswahl().idFloskelgruppe ?? -1) ?? null,
		set: (value: Floskelgruppe | null) => void props.patch({ idFloskelgruppe: value?.id ?? null }),
	});

	const faecher = computed<FachDaten[]>(() => [...props.manager().faecherById.values()]);
	const selectedFach = computed<FachDaten | null>({
		get: () => props.manager().faecherById.get(props.manager().auswahl().idFach ?? -1) ?? null,
		set: (value: FachDaten | null) => void props.patch({ idFach: value?.id ?? null }),
	});
	const hatFloskelgruppeArtFach = computed<boolean>(() => {
		const fg = selectedFloskelgruppe.value;
		if (fg === null) {
			return false;
		}
		const eintragByID = Floskelgruppenart.data().getEintragByID(fg.idFloskelgruppenart ?? -1);
		return eintragByID?.schluessel === 'FACH';
	});


	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().jahrgaengeById.values()]);
	const selectedJahrgang = computed<JahrgangsDaten | null>({
		get: () => {
			const ids = props.manager().auswahl().idsJahrgaenge;
			if (ids === null || ids.isEmpty()) {
				return null;
			}
			return props.manager().jahrgaengeById.get(ids.get(0)) ?? null;
		},
		set: (value: JahrgangsDaten | null) => {
			const list = new ArrayList<number>();
			if (value !== null) {
				list.add(value.id);
			}
			void props.patch({ idsJahrgaenge: list });
		},
	});

	const selectedNiveau = computed<number | null>({
		get: () => props.manager().auswahl().niveau,
		set: (value: number | null) => void props.patch({ niveau: value }),
	});

	// --- manager ---

	const floskelgruppenManager = new SelectManager<Floskelgruppe>({
		options: floskelgruppen,
		optionDisplayText: (v: Floskelgruppe) => v.bezeichnung,
		selectionDisplayText: (v: Floskelgruppe) => v.bezeichnung,
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: faecher,
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});

	const niveauManager = new SelectManager<number>({
		options: FloskelnListeManager.NIVEAUS,
		optionDisplayText: String,
		selectionDisplayText: String,
	});

	// --- patch --

	async function patchKuerzel(value: string | null) {
		if (kuerzelIsValid(value)) {
			await props.patch({ kuerzel: value?.trim() });
		}
	}


	async function patchText(value: string | null) {
		if (textIsValid(value)) {
			await props.patch({ text: value?.trim() });
		}
	}
	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung });
		}
	}

	// --- validate ---

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10) &&
			isUniqueInList(value, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID()));
	}

	function textIsValid(value: string | null): boolean {
		return (value !== null) && (value !== "");
	}

	function sortierungIsValid(value: number | null): value is number {
		return !numberHasDecimals(value) && numberIsValid(value, true, 0, 32000);
	}

</script>
