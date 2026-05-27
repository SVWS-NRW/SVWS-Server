<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Logos">
				<ui-table-grid name="Logos" :manager="() => gridManager">
					<template #header>
						<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
							<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
								<svws-ui-checkbox :model-value="bulkChecked"
									:indeterminate="bulkIntermediate"
									@update:model-value="toggleAll" />
							</th>
							<th v-else-if="col.kuerzel === 'RowActions'" />
							<th v-else-if="col.kuerzel === 'Bild'" class="flex items-center justify-center">
								{{ col.kuerzel }}
							</th>
							<th v-else class="flex items-start justify-center">
								{{ col.kuerzel }}
							</th>
						</template>
					</template>
					<template #default="{ row: logo }">
						<td class="flex items-start justify-center">
							<svws-ui-checkbox :model-value="auswahl.includes(logo)" @update:model-value="(value) => toggleSelection(logo, value)" />
						</td>
						<td class="flex items-start justify-center text-left">
							{{ logo.bezeichnung }}
						</td>
						<td class="flex items-start justify-center text-left">
							{{ logo.beschreibung }}
						</td>
						<td class="flex items-center justify-center">
							<div class="h-22 py-1">
								<button v-if="logoDisplayMode(logo) === 'image'"
									type="button"
									class="h-full hover:ring-2 focus:ring-2 focus:outline-hidden rounded-md ring-ui p-0.5"
									@click="selectLogo(logo)"
									:aria-label="`Logo '${logo.bezeichnung}' in Vorschau öffnen`">
									<img :src="getImgSrcString(logo.mimeType, logo.logoBase64)"
										class="h-full w-full"
										:alt="`Kleine Vorschau des Logos '${logo.bezeichnung}'`">
								</button>
								<svws-ui-tooltip v-else-if="logoDisplayMode(logo) === 'empty'" class="h-full" position="top">
									<span class="icon-xl i-ri-file-close-line icon-ui-caution" />
									<template #content>
										Für dieses Logo wurde noch kein Bild hochgeladen
									</template>
								</svws-ui-tooltip>
								<svws-ui-tooltip v-else class="h-full" position="top">
									<span class="icon-xl i-ri-eye-off-line icon-ui-warning" />
									<template #content>
										Für dieses Logo ist keine Vorschau möglich.
									</template>
								</svws-ui-tooltip>
							</div>
						</td>
						<td>
							<ui-table-actions :actions="rowActions(logo)" :items="logo" />
						</td>
					</template>
					<template #footer>
						<td class="col-span-full my-1">
							<ui-table-actions :actions="bulkActions" :items="auswahl" always-visible />
						</td>
					</template>
				</ui-table-grid>
			</svws-ui-content-card>
			<svws-ui-content-card :title="selectedLogo.bezeichnung" v-if="selectedLogo !== null">
				<img :src="getImgSrcString(selectedLogo.mimeType, selectedLogo.logoBase64)"
					class="max-w-full max-h-full"
					:alt="`Große Vorschau des Logos '${selectedLogo.bezeichnung}'`">
			</svws-ui-content-card>
		</div>
		<svws-ui-modal v-model:show="warningModalIsShown"
			:auto-close="false"
			size="medium" type="danger">
			<template #modalTitle>
				<slot name="title">Logos löschen</slot>
			</template>
			<template #modalDescription>
				<div class="text-left">
					<slot name="description">
						<div class="mb-4">Möchten Sie folgende Logos wirklich löschen:</div>
						<div v-for="logo in logosToDelete" :key="logo.id" class="p-2 grid grid-cols-2 border-b border-ui">
							<div class="flex items-center">{{ logo.bezeichnung }}</div>
							<img v-if="logoDisplayMode(logo) === 'image'" :src="getImgSrcString(logo.mimeType, logo.logoBase64)"
								class="h-20"
								:alt="`Kleine Vorschau des Logos '${logo.bezeichnung}'`">
							<svws-ui-tooltip v-else-if="logoDisplayMode(logo) === 'empty'" class="h-full w-fit" position="top">
								<span class="icon-xl i-ri-file-close-line icon-ui-caution" />
								<template #content>
									Für dieses Logo wurde noch kein Bild hochgeladen
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else class="h-full w-fit" position="top">
								<span class="icon-xl i-ri-eye-off-line icon-ui-warning" />
								<template #content>
									Für dieses Logo ist keine Vorschau möglich.
								</template>
							</svws-ui-tooltip>
						</div>
					</slot>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="closeWarningModal">Abbrechen</svws-ui-button>
				<svws-ui-button type="danger" @click="deleteLogos(logosToDelete)">Logos löschen</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>
<script setup lang="ts">

	import { computed, ref } from "vue";
	import { ArrayList, HashMap, type List, Logo, ReportingBildDefinition } from "@core";
	import { GridManager, UiTableActions, useSchuleState } from "@ui";
	import type { SchuleLogoverwaltungProps } from "./SchuleLogoverwaltungProps";
	import type { TableActions } from "../../../../../../ui/src/ui/controls/tablegrid/UiTableActions.vue";

	const props = defineProps<SchuleLogoverwaltungProps>();
	const schuleState = useSchuleState();
	const warningModalIsShown = ref<boolean>(false);
	const logosToDelete = ref<Logo[]>([]);

	const SUPPORTED_IMAGE_TYPES = [
		{ mimeType: 'image/png', extension: 'png' },
		{ mimeType: 'image/jpeg', extension: 'jpg' },
		{ mimeType: 'image/gif', extension: 'gif' },
		{ mimeType: 'image/webp', extension: 'webp' },
		{ mimeType: 'image/svg+xml', extension: 'svg' },
		{ mimeType: 'image/avif', extension: 'avif' },
		{ mimeType: 'image/bmp', extension: 'bmp' },
	] as const;


	function logoDisplayMode(logo: Logo): 'image' | 'empty' | 'unsupported' {
		if (logo.logoBase64 === '') {
			return 'empty';
		}
		if (!SUPPORTED_IMAGE_TYPES.some(t => t.mimeType === logo.mimeType)) {
			return 'unsupported';
		}
		return 'image';
	}

	/**
	 * Tabellendaten
	 */
	const mergedLogos = computed(() => {
		const logoByKennung = mapLogosByKennung(props.logos());

		const result = new ArrayList<Logo>();
		for (const logo of logoByKennung.values()) {
			result.add(logo);
		}
		return result;
	});

	function mapLogosByKennung(definedLogos: List<Logo>): HashMap<string, Logo> {
		const allLogosDefinitions = ReportingBildDefinition.getBySchulform(schuleState.schulform) ?? new ArrayList<ReportingBildDefinition>();
		const logoByKennung = new HashMap<string, Logo>();
		for (const definition of allLogosDefinitions) {
			const logo = new Logo();
			logo.kennung = definition.getKennung() ?? "";
			logo.bezeichnung = definition.getBezeichnung() ?? "";
			logo.beschreibung = definition.getBeschreibung() ?? "";
			logoByKennung.put(logo.kennung, logo);
		}
		mergeDefinedLogos(logoByKennung, definedLogos);
		return logoByKennung;
	}

	function mergeDefinedLogos(logoByKennung: HashMap<string, Logo>, definedLogos: List<Logo>) {
		for (const logo of definedLogos) {
			if (logoByKennung.containsKey(logo.kennung)) {
				logoByKennung.put(logo.kennung, logo);
			}
		}
	}

	const gridManager = computed(() => new GridManager<string, Logo, List<Logo>>({
		daten: mergedLogos,
		getRowKey: row => row.kennung,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Bezeichnung", name: "Bezeichnung", width: "1fr" },
			{ kuerzel: "Beschreibung", name: "Beschreibung", width: '1fr' },
			{ kuerzel: "Bild", name: "Bild", width: "1fr" },
			{ kuerzel: "RowActions", name: "Row-Actions", width: '8.5em' },
		],
	}));

	function rowActions(logo: Logo): TableActions<Logo>[] {
		return [
			{ label: "Logo aktualisieren", action: () => alert("Hier fehlt noch das Upload Modal"), iconClasses: "i-ri-loop-right-line" },
			{ label: "Logo exportieren", action: () => exportLogo(logo), iconClasses: "i-ri-download-2-line", disabled: logo.logoBase64 === "" },
			{ label: "Logo entfernen", action: () => openWarningModal([logo]), iconClasses: "i-ri-close-line icon-ui-danger", disabled: logo.logoBase64 === "" },
		];
	}

	const bulkChecked = computed(() => (auswahl.value.length === gridManager.value.daten.size()) && (auswahl.value.length > 0));
	const bulkIntermediate = computed(() => (auswahl.value.length < gridManager.value.daten.size()) && (auswahl.value.length > 0));
	const bulkActions = computed(() => {
		return [
			{ label: "Logos exportieren", action: () => void exportZip(), iconClasses: "i-ri-download-2-line", disabled: validBulkSelection.value.length === 0 },
			{ label: "Logos entfernen", action: () => openWarningModal(validBulkSelection.value), iconClasses: "i-ri-close-line icon-ui-danger", disabled: validBulkSelection.value.length === 0 },
		];
	});

	const validBulkSelection = computed(() => {
		return auswahl.value.filter(logo => logo.logoBase64 !== "");
	});

	function getImgSrcString(mimeType: string, base64: string): string {
		return `data:${mimeType};base64,${base64}`;
	}


	/**
	 * Selektion
	 */

	const auswahl = ref<Logo[]>([]);

	function toggleSelection(logo: Logo, value: boolean): void {
		if (value) {
			auswahl.value.push(logo);
			logosToDelete.value.push(logo);
		} else {
			const idx = auswahl.value.indexOf(logo);
			if (idx !== -1) {
				auswahl.value.splice(idx, 1);
				logosToDelete.value.splice(idx, 1);
			}
		}
	}

	function toggleAll(value: boolean): void {
		auswahl.value = value ? [...gridManager.value.daten] : [];
	}

	const selectedLogo = ref<Logo | null>(null);

	function selectLogo(logo: Logo): void {
		if (selectedLogo.value?.kennung === logo.kennung) {
			// Zweiter Klick auf dasselbe Logo → schließt die Preview
			selectedLogo.value = null;
			return;
		}
		if (logoDisplayMode(logo) !== 'image') {
			return;
		}
		selectedLogo.value = logo;
	}

	/**
	 * Export
	 */

	function exportLogo(logo: Logo): void {
		const extension = getExtension(logo.mimeType);
		const filename = `${logo.kennung}.${extension}`;

		triggerExport(getImgSrcString(logo.mimeType, logo.logoBase64), filename);
	}

	async function exportZip(): Promise<void> {
		console.log("Zip-Export ist noch nicht implementiert");
		/* const blob = await props.zipLogos(auswahl.value.map(logo => logo.id));
		const url = URL.createObjectURL(blob);

		triggerExport(url, "schullogos.zip");

		URL.revokeObjectURL(url);*/
	}

	function triggerExport(url: string, filename: string) {
		const link = document.createElement('a');
		link.href = url;
		link.download = filename;
		link.click();
	}

	function getExtension(mimeType: string): string {
		return SUPPORTED_IMAGE_TYPES.find(t => t.mimeType === mimeType)?.extension ?? 'bin';
	}

	/**
	 * Delete - Modal
	 */

	function openWarningModal(logos: Logo[]): void {
		warningModalIsShown.value = true;
		logosToDelete.value = logos;
	}

	function closeWarningModal(): void {
		warningModalIsShown.value = false;
	}

	async function deleteLogos(logos: Logo[]): Promise<void> {
		await props.deleteLogo(logos);
		closeWarningModal();
	}

</script>
