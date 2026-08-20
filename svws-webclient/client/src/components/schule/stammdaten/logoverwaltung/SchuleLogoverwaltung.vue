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
							<th v-else :class="['flex justify-center', (col.kuerzel === 'Bild') ? 'items-center' : 'items-start']">
								{{ col.kuerzel }}
							</th>
						</template>
					</template>
					<template #default="{ row: logoModel }">
						<td class="flex items-start justify-center group-hover:bg-ui-selected" :class="{ 'bg-ui-selected': logoModel === previewLogoModel}">
							<svws-ui-checkbox :model-value="bulkSelectedLogoModels.includes(logoModel)"
								:disabled="logoModel.proxy.base64 === ''"
								@update:model-value="(value) => toggleBulkSelection(logoModel, value)" />
						</td>
						<td class="flex justify-center text-left group-hover:bg-ui-selected" :class="{ 'bg-ui-selected': logoModel === previewLogoModel}">
							{{ logoModel.proxy.bezeichnung }}
						</td>
						<td class="flex justify-center text-left group-hover:bg-ui-selected" :class="{ 'bg-ui-selected': logoModel === previewLogoModel}">
							{{ logoModel.proxy.beschreibung }}
						</td>
						<td class="flex items-center justify-center text-left max-w-full group-hover:bg-ui-selected" :class="{ 'bg-ui-selected': logoModel === previewLogoModel}">
							<div class="relative p-1"
								:style="{ height: `${LOGO_PREVIEW_HEIGHT_REM}rem`, aspectRatio: getCssAspectRatio(logoModel.proxy.kennung) }">
								<logo-image mode="tooltip"
									:logo-base64="logoModel.proxy.base64"
									:alt="`Kleine Vorschau des Bildes für das Logo '${logoModel.proxy.bezeichnung}'`"
									aria-label="Öffnet eine größere Vorschau des Bildes neben der Tabelle"
									@click="selectPreviewLogo(logoModel)"
									clickable />
								<ui-validation-tooltip v-if="(logoModel.proxy.base64 !== '') && logoModel.hatFehler()"
									:validation-result="getValidationResult(logoModel)"
									class="absolute right-0 top-0" />
							</div>
						</td>
						<td class="group-hover:bg-ui-selected" :class="{ 'bg-ui-selected': logoModel === previewLogoModel}">
							<ui-table-actions :actions="rowActions(logoModel)" :items="logoModel" />
						</td>
					</template>
					<template #footer>
						<td class="col-span-full my-1">
							<ui-table-actions :actions="bulkActions" :items="bulkSelectedLogoModels" always-visible />
						</td>
					</template>
				</ui-table-grid>
			</svws-ui-content-card>
			<svws-ui-content-card title="Vorschau" v-if="previewLogoModel !== null" class="w-[50%]">
				<div class="p-1"
					:style="previewStyle">
					<logo-image mode="tooltip"
						:logo-base64="previewLogoModel.proxy.base64"
						:alt="`Große Vorschau des Bildes für das Logo '${previewLogoModel.proxy.bezeichnung}'`" />
				</div>
			</svws-ui-content-card>
		</div>
		<logo-image-upload-modal v-if="uploadModalIsOpen"
			:is-open="uploadModalIsOpen"
			:add="addLogo"
			:logo-model="() => logoModelForUpload"
			@close-modal="closeUploadModal()" />
		<logo-image-delete-modal :is-open="deleteModalIsOpen"
			:logo-models="logoModelToDeleteImage"
			@cancel="closeDeleteModal"
			@confirm="deleteLogoImages" />
	</div>
</template>
<script setup lang="ts">

	import { computed, onMounted, ref, shallowRef, type ShallowRef } from "vue";
	import { DeveloperNotificationException, type Logo, ReportingBildDefinition } from "@core";
	import { GridManager, type TableActions, useModelProxyList, useSchuleState, ValidationResult } from "@ui";
	import type { SchuleLogoverwaltungProps } from "./SchuleLogoverwaltungProps";
	import { base64ToBlob, getCssAspectRatio, getExtension, parseBase64, setModelImageInfo, type TableLogo } from "./LogoUtils";
	import LogoImage from "./LogoImage.vue";
	import { LogoModelProxy } from "./modelProxy/LogoModelProxy";
	import LogoImageUploadModal from "./modals/LogoImageUploadModal.vue";
	import LogoImageDeleteModal from "./modals/LogoImageDeleteModal.vue";

	const LOGO_PREVIEW_HEIGHT_REM = 5.5;
	const props = defineProps<SchuleLogoverwaltungProps>();
	const schuleState = useSchuleState();

	/**
	 * Tabellendaten
	 */

	const tableLogos = computed(() => {
		const dbLogos = [...props.logos()];
		const tableLogos: TableLogo[] = [];
		const bildDefinitions = ReportingBildDefinition.getBySchulform(schuleState.schulform);
		for (const definition of bildDefinitions) {
			const dbLogo = dbLogos.find(dbLogo => dbLogo.kennung === definition.getKennung());
			tableLogos.push(getTableLogo(definition, dbLogo));
		}
		return tableLogos;
	});

	const logoModels = useModelProxyList(
		tableLogos,
		(tableLogo) => tableLogo.kennung,
		(tableLogo) => new LogoModelProxy(
			() => tableLogo,
			(data: Partial<TableLogo>) => patchTableLogo(data.base64 ?? '', tableLogo.id)
		)
	);

	/**
	 * Initiale Berechnung der Bilddimensionen zur Validierung.
	 * Die Dateigröße kann aus base64 nicht konkret bestimmt werden, daher wird diese hier nicht validiert
	 */
	onMounted(async () => {
		for (const logoModel of logoModels.value) {
			const base64 = logoModel.proxy.base64;
			if (base64 === '') {
				continue;
			}
			const fileType = parseBase64(base64)?.mimeType ?? null;
			await setModelImageInfo(logoModel, base64, fileType, null);
			// Revalidierung nach dem Setzen der Werte wieder notwendig
			logoModel.validate();
		}
	});

	function getImageColumnWitdh(): string {
		const bildDefinitions = [...ReportingBildDefinition.getBySchulform(schuleState.schulform)];
		const maxRatio = Math.max(
			1,
			...bildDefinitions.map(definition => {
				const hoehe = definition.getHoehe();
				const breite = definition.getBreite();

				return (hoehe > 0) ? breite / hoehe : 1;
			})
		);

		return `${Math.ceil(LOGO_PREVIEW_HEIGHT_REM * maxRatio)}rem`;
	}

	function getTableLogo(definition: ReportingBildDefinition, dbLogo: Logo | undefined): TableLogo {
		const id = dbLogo?.id ?? -1;
		const kennung = definition.getKennung() ?? '';
		const bezeichnung = definition.getBezeichnung() ?? '';
		const beschreibung = definition.getBeschreibung() ?? '';
		const base64 = dbLogo?.logoBase64 ?? '';
		const hinzugefuegtAm = dbLogo?.hinzugefuegtAm ?? '';
		return { id, kennung, bezeichnung, beschreibung, base64, hinzugefuegtAm };
	}

	function patchTableLogo(base64: string, id: number): Promise<boolean> {
		const patchData: Partial<Logo> = { logoBase64: base64 };
		return props.patchLogo(patchData, id);
	}

	function getValidationResult(logoModel: LogoModelProxy): ValidationResult {
		return new ValidationResult(logoModel.getAlleFehler());
	}

	const gridManager = shallowRef(new GridManager<string, LogoModelProxy, LogoModelProxy[]>({
		daten: logoModels,
		getRowKey: row => row.proxy.kennung,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Bezeichnung", name: "Bezeichnung", width: "1fr" },
			{ kuerzel: "Beschreibung", name: "Beschreibung", width: '1fr' },
			{ kuerzel: "Bild", name: "Bild", width: getImageColumnWitdh() },
			{ kuerzel: "", name: "Row-Actions", width: '8.5em' },
		],
	}));

	function rowActions(logoModel: LogoModelProxy): TableActions<LogoModelProxy>[] {
		const hasImage = logoModel.proxy.base64 !== "";
		return [
			{
				label: hasImage ? "Bild aktualisieren" : "Bild hochladen",
				action: () => openUploadModal(logoModel),
				iconClasses: hasImage ? "i-ri-edit-2-line" : "i-ri-add-line",
			},
			{ label: "Bild exportieren", action: () => exportImage(logoModel), iconClasses: "i-ri-download-2-line", disabled: !hasImage },
			{ label: "Bild löschen", action: () => openDeleteModal([logoModel]), iconClasses: "i-ri-close-line icon-ui-danger", disabled: !hasImage },
		];
	}

	/**
	 * Große Bildvorschau
	 */

	const previewLogoModel = shallowRef<LogoModelProxy | null>(null);

	function selectPreviewLogo(logoModel: LogoModelProxy): void {
		if (previewLogoModel.value?.proxy.kennung === logoModel.proxy.kennung) {
			// Zweiter Klick auf dasselbe Bild → schließt die Preview
			previewLogoModel.value = null;
			return;
		}
		previewLogoModel.value = logoModel;
	}

	const previewStyle = computed((): { aspectRatio: string } | undefined => {
		if (previewLogoModel.value === null) {
			return undefined;
		}
		return (previewLogoModel.value.proxy.base64 !== '')
			? { aspectRatio: getCssAspectRatio(previewLogoModel.value.proxy.kennung) }
			: undefined;
	});

	/**
	 * Bulk-Selektion
	 */

	const bulkSelectedLogoModels: ShallowRef<LogoModelProxy[]> = shallowRef([]);
	const bulkSelectable = computed(() => [...gridManager.value.daten].filter(logo => logo.proxy.base64 !== ''));
	const bulkAllChecked = computed(() => (bulkSelectedLogoModels.value.length === bulkSelectable.value.length) && (bulkSelectedLogoModels.value.length > 0));
	const bulkIntermediate = computed(() => (bulkSelectedLogoModels.value.length < bulkSelectable.value.length) && (bulkSelectedLogoModels.value.length > 0));
	const bulkActions = computed(() => {
		return [
			{ label: "Bilder exportieren", action: () => void exportImagesAsZip(), iconClasses: "i-ri-download-2-line", disabled: bulkSelectedLogoModels.value.length === 0 },
			{ label: "Bilder entfernen", action: () => openDeleteModal(bulkSelectedLogoModels.value), iconClasses: "i-ri-close-line icon-ui-danger", disabled: bulkSelectedLogoModels.value.length === 0 },
		];
	});

	function toggleBulkSelection(logoModel: LogoModelProxy, value: boolean): void {
		if (value) {
			bulkSelectedLogoModels.value = [...bulkSelectedLogoModels.value, logoModel];
		} else {
			const idx = bulkSelectedLogoModels.value.indexOf(logoModel);
			if (idx !== -1) {
				bulkSelectedLogoModels.value = bulkSelectedLogoModels.value.filter((_, i) => i !== idx);
			}
		}
	}

	function toggleBulkAll(value: boolean): void {
		bulkSelectedLogoModels.value = value ? [...bulkSelectable.value] : [];
	}

	/**
	 * Export
	 */

	function exportImage(logoModel: LogoModelProxy): void {
		const extension = getExtension(logoModel.proxy.base64);
		const filename = `${logoModel.proxy.kennung}${extension}`;

		triggerExport(logoModel.proxy.base64, filename);
	}

	async function exportImagesAsZip(): Promise<void> {
		throw new DeveloperNotificationException("Zip-Export ist noch nicht implementiert. Bitte Bilder einzeln exportieren");
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

	/**
	 * Upload - Modal
	 */

	const uploadModalIsOpen = ref(false);
	const logoModelForUpload = shallowRef<LogoModelProxy>();

	function closeUploadModal() {
		uploadModalIsOpen.value = false;
		bulkSelectedLogoModels.value = [];
		logoModelForUpload.value = undefined;
	}

	function openUploadModal(logoModel: LogoModelProxy) {
		previewLogoModel.value = null;
		uploadModalIsOpen.value = true;
		logoModelForUpload.value = logoModel;
	}

	/**
	 * Delete - Modal
	 */

	const deleteModalIsOpen = ref<boolean>(false);
	const logoModelToDeleteImage: ShallowRef<LogoModelProxy[]> = shallowRef([]);

	function openDeleteModal(logos: LogoModelProxy[]): void {
		deleteModalIsOpen.value = true;
		logoModelToDeleteImage.value = logos;
	}

	function closeDeleteModal(): void {
		deleteModalIsOpen.value = false;
		logoModelToDeleteImage.value = [];
	}

	async function deleteLogoImages(logos: LogoModelProxy[]): Promise<void> {
		const kennungen = new Set<string>(logos.map(l => l.proxy.kennung));
		const dbLogosToDelete = [...props.logos()].filter(dbLogo => kennungen.has(dbLogo.kennung));
		for (const logo of dbLogosToDelete) {
			const logoModel = logoModels.value.find(model => model.proxy.kennung === logo.kennung);
			await setModelImageInfo(logoModel, "", null, null);
		}
		await props.deleteLogo(dbLogosToDelete);
		bulkSelectedLogoModels.value = [];
		previewLogoModel.value = null;
		closeDeleteModal();
	}

</script>
