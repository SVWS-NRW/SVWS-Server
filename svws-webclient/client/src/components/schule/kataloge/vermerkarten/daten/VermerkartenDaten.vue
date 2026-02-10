<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().daten().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="30" required :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
		<svws-ui-content-card class="w-full" :title="'Alle Schüler mit der Vermerkart `' + manager().auswahl().bezeichnung + '`'"
			v-if="manager().schuelerVermerkartZusammenfassungen.size() > 0">
			<svws-ui-table class="w-full" :columns :items="manager().schuelerVermerkartZusammenfassungen">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ value }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { VermerkartenDatenProps } from "./VermerkartenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<VermerkartenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	const columns: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Rufname", sortable: true },
		{ key: "anzahlVermerke", label: "Anzahl", fixedWidth: 8.75, sortable: true, span: 0.5 },
	];

	async function patchBezeichnung(bezeichnung: string | null): Promise<void> {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung });
		}
	}

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });

	}

	function bezeichnungIsValid(value: string | null): boolean {
		return mandatoryInputIsValid(value, 30)
			&& isUniqueInList(value, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID());
	}

	function sortierungIsValid(value: number | null): boolean {
		return numberIsValid(value, true, 0, 32000);
	}

</script>
