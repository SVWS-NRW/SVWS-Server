<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Konfession ASD-Kürzel"
						v-model="selectedKonfession"
						:manager="konfessionKuerzelSelectManager"
						searchable statistics :readonly="!hatKompetenzUpdate" :removable="false" />
					<ui-select label="Konfession ASD-Text"
						v-model="selectedKonfession"
						:manager="konfessionTextSelectManager"
						searchable statistics :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-text-input placeholder="Interne Bezeichnung"
						:model-value="manager().daten().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="30" :readonly="!hatKompetenzUpdate"
						required />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						:model-value="manager().daten().bezeichnungZeugnis"
						@change="patchBezeichnungZeugnis"
						:valid="bezeichnungZeugnisIsValid" :max-len="50" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :readonly="!hatKompetenzUpdate" :min="0" :max="32000" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { KonfessionenDatenProps } from "./KonfessionenDatenProps";
	import { BenutzerKompetenz, Religion } from "@core";
	import type { CoreTypeData } from "@core";
	import { CoreTypeSelectManager } from "@ui";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<KonfessionenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());

	const konfessionKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuljahr.value,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const konfessionTextSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuljahr.value,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedKonfession = computed<CoreTypeData | null>({
		get: () => Religion.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, props.manager().daten().kuerzel ?? ""),
		set: (value: CoreTypeData | null) => void props.patch({ kuerzel: value?.schluessel ?? "" }),
	});

	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	// patch

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung?.trim() ?? "" });
		}
	}

	async function patchBezeichnungZeugnis(bezeichnungZeugnis: string | null) {
		if (bezeichnungZeugnisIsValid(bezeichnungZeugnis)) {
			await props.patch({ bezeichnungZeugnis: bezeichnungZeugnis?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? undefined });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	// Validierung

	function bezeichnungIsValid(bezeichnung: string | null) {
		return mandatoryInputIsValid(bezeichnung, 30)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function bezeichnungZeugnisIsValid(bezeichnungZeugnis: string | null) {
		return optionalInputIsValid(bezeichnungZeugnis, 50);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

</script>
