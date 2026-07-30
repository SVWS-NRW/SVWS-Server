<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Logos">
				<ui-table-grid name="Logos" :manager="() => gridManager">
					<template #header>
						<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
							<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
								<svws-ui-checkbox :model-value="bulkAllChecked"
									:indeterminate="bulkIntermediate"
									:disabled="bulkSelectable.length === 0"
									@update:model-value="toggleBulkAll" />
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
							<svws-ui-checkbox :model-value="bulkSelectedLogos.includes(logo)"
								:disabled="logo.proxy.base64 === ''"
								@update:model-value="(value) => toggleBulkSelection(logo, value)" />
						</td>
						<td class="flex justify-center text-left">
							{{ logo.proxy.bezeichnung }}
						</td>
						<td class="flex justify-center text-left">
							{{ logo.proxy.beschreibung }}
						</td>
						<td class="flex items-center justify-center">
							<div class="h-22 py-1">
								<logo-image mode="tooltip"
									:logo-base64="logo.proxy.base64"
									:alt="`Kleine Vorschau des Bildes für das Logo '${logo.proxy.bezeichnung}'`"
									aria-label="Öffnet eine größere Vorschau des Bildes neben der Tabelle"
									@click="selectPreviewLogo(logo)"
									clickable />
							</div>
						</td>
						<td>
							<ui-table-actions :actions="rowActions(logo)" :items="logo" />
						</td>
					</template>
					<template #footer>
						<td class="col-span-full my-1">
							<ui-table-actions :actions="bulkActions" :items="bulkSelectedLogos" always-visible />
						</td>
					</template>
				</ui-table-grid>
			</svws-ui-content-card>
			<svws-ui-content-card :title="previewLogo.proxy.bezeichnung" v-if="previewLogo !== null">
				<logo-image mode="tooltip"
					:logo-base64="previewLogo.proxy.base64"
					:alt="`Kleine Vorschau des Bildes für das Logo '${previewLogo.proxy.bezeichnung}'`" />
			</svws-ui-content-card>
		</div>
		<logo-image-upload-modal v-if="uploadModalIsOpen"
			:is-open="uploadModalIsOpen"
			:add="addLogo"
			:logo="() => logoForUpload"
			@close-modal="closeUploadModal()" />
		<logo-image-delete-modal :is-open="deleteModalIsOpen"
			:logos="logoImagesToDelete"
			@cancel="closeDeleteModal"
			@confirm="deleteLogoBilder" />
	</div>
</template>
<script setup lang="ts">

	import { computed, ref, shallowRef, type ShallowRef } from "vue";
	import { ArrayList, DeveloperNotificationException, type List, type Logo, ReportingBildDefinition } from "@core";
	import { GridManager, useSchuleState, type TableActions } from "@ui";
	import type { SchuleLogoverwaltungProps } from "./SchuleLogoverwaltungProps";
	import { SUPPORTED_IMAGE_TYPES, type TableLogo } from "./LogoUtils";
	import { LogoModelProxy } from "./modelProxy/LogoModelProxy";
	import LogoImage from "./LogoImage.vue";
	import LogoImageUploadModal from "./modals/LogoImageUploadModal.vue";
	import LogoImageDeleteModal from "./modals/LogoImageDeleteModal.vue";

	const props = defineProps<SchuleLogoverwaltungProps>();
	const schuleState = useSchuleState();

	/**
	 * Tabellendaten
	 */

	const logoModels = computed(() => {
		const definitions = ReportingBildDefinition.getBySchulform(schuleState.schulform);
		const dbLogos = [...props.logos()];
		const models = new ArrayList<LogoModelProxy>();
		for (const definition of definitions) {
			const dbLogo = dbLogos.find(dbLogo => dbLogo.kennung === definition.getKennung());
			models.add(createModel(definition, dbLogo));
		}
		return models;
	});

	function createModel(definition: ReportingBildDefinition, dbLogo: Logo | undefined): LogoModelProxy {
		const id = dbLogo?.id ?? -1;
		const kennung = definition.getKennung() ?? '';
		const bezeichnung = definition.getBezeichnung() ?? '';
		const beschreibung = definition.getBeschreibung() ?? '';
		const base64 = dbLogo?.logoBase64 ?? '';
		const hinzugefuegtAm = dbLogo?.hinzugefuegtAm ?? '';
		return new LogoModelProxy(
			(): TableLogo => ({ id, kennung, bezeichnung, beschreibung, base64, hinzugefuegtAm }),
			(data: Partial<TableLogo>) => patchTableLogo(data.base64 ?? '', id)
		);
	}

	async function patchTableLogo(base64: string, id: number): Promise<boolean> {
		const patchData: Partial<Logo> = { logoBase64: base64 };
		return props.patchLogo(patchData, id);
	}

	const gridManager = shallowRef(new GridManager<string, LogoModelProxy, List<LogoModelProxy>>({
		daten: logoModels,
		getRowKey: row => row.proxy.kennung,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Bezeichnung", name: "Bezeichnung", width: "1fr" },
			{ kuerzel: "Beschreibung", name: "Beschreibung", width: '1fr' },
			{ kuerzel: "Bild", name: "Bild", width: "1fr" },
			{ kuerzel: "RowActions", name: "Row-Actions", width: '8.5em' },
		],
	}));

	function rowActions(logo: LogoModelProxy): TableActions<LogoModelProxy>[] {
		const hasImage = logo.proxy.base64 !== "";
		return [
			{
				label: hasImage ? "Bild aktualisieren" : "Bild hochladen",
				action: () => openUploadModal(logo),
				iconClasses: hasImage ? "i-ri-edit-2-line" : "i-ri-add-line",
			},
			{ label: "Bild exportieren", action: () => exportImage(logo), iconClasses: "i-ri-download-2-line", disabled: !hasImage },
			{ label: "Bild löschen", action: () => openDeleteModal([logo]), iconClasses: "i-ri-close-line icon-ui-danger", disabled: !hasImage },
		];
	}

	/**
	 * Große Bildvorschau
	 */

	const previewLogo = shallowRef<LogoModelProxy | null>(null);

	function selectPreviewLogo(logo: LogoModelProxy): void {
		if (previewLogo.value?.proxy.kennung === logo.proxy.kennung) {
			// Zweiter Klick auf dasselbe Bild → schließt die Preview
			previewLogo.value = null;
			return;
		}
		previewLogo.value = logo;
	}

	/**
	 * Bulk-Selektion
	 */

	const bulkSelectedLogos: ShallowRef<LogoModelProxy[]> = shallowRef([]);
	const bulkSelectable = computed(() => [...gridManager.value.daten].filter(logo => logo.proxy.base64 !== ''));
	const bulkAllChecked = computed(() => (bulkSelectedLogos.value.length === bulkSelectable.value.length) && (bulkSelectedLogos.value.length > 0));
	const bulkIntermediate = computed(() => (bulkSelectedLogos.value.length < bulkSelectable.value.length) && (bulkSelectedLogos.value.length > 0));
	const bulkActions = computed(() => {
		return [
			{ label: "Bilder exportieren", action: () => void exportImagesAsZip(), iconClasses: "i-ri-download-2-line", disabled: bulkSelectedLogos.value.length === 0 },
			{ label: "Bilder entfernen", action: () => openDeleteModal(bulkSelectedLogos.value), iconClasses: "i-ri-close-line icon-ui-danger", disabled: bulkSelectedLogos.value.length === 0 },
		];
	});

	function toggleBulkSelection(logo: LogoModelProxy, value: boolean): void {
		if (value) {
			bulkSelectedLogos.value = [...bulkSelectedLogos.value, logo];
		} else {
			const idx = bulkSelectedLogos.value.indexOf(logo);
			if (idx !== -1) {
				bulkSelectedLogos.value = bulkSelectedLogos.value.filter((_, i) => i !== idx);
			}
		}
	}

	function toggleBulkAll(value: boolean): void {
		bulkSelectedLogos.value = value ? [...bulkSelectable.value] : [];
	}

	/**
	 * Export
	 */

	function exportImage(logo: LogoModelProxy): void {
		const extension = getExtension(logo.proxy.base64);
		const filename = `${logo.proxy.kennung}${extension}`;

		triggerExport(logo.proxy.base64, filename);
	}

	async function exportImagesAsZip(): Promise<void> {
		throw new DeveloperNotificationException("Zip-Export ist noch nicht implementiert. Bitte Bilder einzeln exportieren");
	}

	function base64ToBlob(base64: string): Blob {
		const parsed = parseBase64(base64);
		if (parsed === null) {
			throw new Error("Ungültiger Base64 String. Es fehlt die DataUrl: data:[mimeType];base64;...");
		}

		const binary = atob(parsed.data);
		const bytes = Uint8Array.from(binary, c => c.charCodeAt(0));
		return new Blob([bytes], { type: parsed.mimeType });
	}

	function triggerExport(base64: string, filename: string): void {
		const blob = base64ToBlob(base64);
		const url = URL.createObjectURL(blob);

		const link = document.createElement("a");
		link.href = url;
		link.download = filename;
		link.click();

		URL.revokeObjectURL(url);
	}

	function getExtension(base64: string): string {
		const mimeType = parseBase64(base64)?.mimeType;
		return SUPPORTED_IMAGE_TYPES.find(t => t.mimeType === mimeType)?.extensions[0] ?? '.bin';
	}


	/**
	 * Ermittelt den MimeType und den Daten-String aus einem Base64-String
	 *
	 * @param base64   vollständiger Base64-String mit DataURL
	 *
	 * @returns ein Objekt mit dem berechneten MimeType und dem Datenstring
	 */
	function parseBase64(base64: string): { mimeType: string, data: string } | null {
		const match = /^data:([^;]+);base64,(.+)$/.exec(base64);
		if (match === null) {
			return null;
		}
		return {
			mimeType: match[1],
			data: match[2],
		};
	}

	/**
	 * Upload - Modal
	 */

	const uploadModalIsOpen = ref(false);
	const logoForUpload = shallowRef<LogoModelProxy>();

	function closeUploadModal() {
		uploadModalIsOpen.value = false;
		bulkSelectedLogos.value = [];
	}

	function openUploadModal(logo: LogoModelProxy) {
		previewLogo.value = null;
		uploadModalIsOpen.value = true;
		logoForUpload.value = logo;
	}

	/**
	 * Delete - Modal
	 */

	const deleteModalIsOpen = ref<boolean>(false);
	const logoImagesToDelete: ShallowRef<LogoModelProxy[]> = shallowRef([]);

	function openDeleteModal(logos: LogoModelProxy[]): void {
		deleteModalIsOpen.value = true;
		logoImagesToDelete.value = logos;
	}

	function closeDeleteModal(): void {
		deleteModalIsOpen.value = false;
		logoImagesToDelete.value = [];
	}

	async function deleteLogoBilder(logos: LogoModelProxy[]): Promise<void> {
		const kennungen = new Set<string>(logos.map(l => l.proxy.kennung));
		const dbLogosToDelete = [...props.logos()].filter(dbLogo => kennungen.has(dbLogo.kennung));
		await props.deleteLogo(dbLogosToDelete);
		bulkSelectedLogos.value = [];
		previewLogo.value = null;
		closeDeleteModal();
	}

</script>
