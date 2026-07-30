<template>
	<svws-ui-modal :show="isOpen"
		size="medium"
		@update:show="closeModal">
		<template #modalTitle>
			<span>Bild hochladen für: {{ currentLogo?.proxy.bezeichnung }}</span>
		</template>
		<template #modalContent>
			<span class="block text-headline-md pb-3 text-left">Bildanforderungen</span>
			<div class="grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-left mb-5">
				<span class="font-bold">Dateiformat:</span>
				<span>
					{{ SUPPORTED_IMAGE_TYPES.map(type => type.extensions.join(', ')).join(', ') }}
				</span>
				<span class="font-bold">Dateigröße:</span>
				<span>max. 2 MB</span>
				<span class="font-bold">Auflösung:</span>
				<span>min. 300dpi</span>
				<span class="font-bold">Zielgröße in Breite x Höhe:</span>
				<span>{{ bildVorgaben.breite }}mm x {{ bildVorgaben.hoehe }}mm ({{ bildVorgaben.seitenverhaeltnis }})</span>
			</div>
			<div class="my-4" :class="currentLogoHasImage ? 'grid grid-cols-2 gap-4' : 'flex justify-center'">
				<div v-if="currentLogo?.proxy.base64 !== ''" class="flex flex-col gap-2">
					<div class="flex justify-between">
						<span class="font-bold">Aktuelles Bild</span>
						<span>{{ getFormattedDate(currentLogo?.proxy.hinzugefuegtAm) }}</span>
					</div>
					<div class="flex items-center justify-center h-50 rounded-md border border-ui-50 bg-ui-neutral-25">
						<logo-image :logo-base64="currentLogo?.proxy.base64 ?? ''"
							:alt="`Aktuelles Bild '${currentLogo?.proxy.bezeichnung}'`"
							mode="inline" />
					</div>
				</div>
				<div class="flex flex-col gap-2" :class="{ 'max-w-sm w-full' : !currentLogoHasImage }">
					<span class="font-bold text-left">Neues Bild</span>
					<div class="flex items-center justify-center h-50 rounded-md border border-ui-50 bg-ui-neutral-25">
						<logo-image :logo-base64="uploadedFileSrc"
							alt="Vorschau des neuen Bildes"
							mode="inline" />
					</div>
					<input ref="fileInput" type="file" :accept="SUPPORTED_IMAGE_TYPES.map(type => type.mimeType).join(', ')" @change="onFileChanged">
				</div>
			</div>
		</template>
		<template #modalActions>
			<div class="mt-7 flex gap-4 justify-end">
				<svws-ui-button type="secondary" @click="closeModal">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button :disabled="uploadDisabled" @click="uploadLogoImage()">
					Hochladen
				</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, onBeforeUnmount, ref, watch } from "vue";
	import type { Logo } from "@core";
	import { DeveloperNotificationException, ReportingBildDefinition } from "@core";
	import { SUPPORTED_IMAGE_TYPES } from "../LogoUtils";
	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";
	import LogoImage from "../LogoImage.vue";

	type BildVorgaben = {
		hoehe: number;
		breite: number;
		seitenverhaeltnis: string;
	};

	const props = defineProps<{
		isOpen: boolean;
		logo: () => LogoModelProxy | undefined;
		add: (logo: Partial<Logo>) => Promise<Logo>;
	}>();

	const emit = defineEmits<{
		(e: "closeModal"): void;
	}>();

	const currentLogo = computed(() => props.logo());
	const currentLogoHasImage = computed(() => currentLogo.value?.proxy.base64 !== '');
	const file = ref<File | null>(null);
	const fileInput = ref<HTMLInputElement | null>(null);
	const objectUrl = ref<string | null>(null);
	const uploadedFileSrc = computed(() => objectUrl.value ?? "");

	const bildVorgaben = computed<BildVorgaben>(() => {
		const kennung = props.logo()?.proxy.kennung ?? null;
		const def = ReportingBildDefinition.getByKennung(kennung);
		if (def === null) {
			throw new DeveloperNotificationException(`Es existiert keine Definition für die Kennung ${kennung}`);
		}

		const hoehe = def.getHoehe();
		const breite = def.getBreite();

		const greatestCommonDivisor = (a: number, b: number): number => (b === 0) ? a : greatestCommonDivisor(b, a % b);
		const divisor = greatestCommonDivisor(breite, hoehe);

		return { hoehe, breite, seitenverhaeltnis: `${breite / divisor}:${hoehe / divisor}` };
	});

	const uploadDisabled = computed(() => {
		if (file.value === null) {
			return true;
		}

		const mimeTypeNotSupported = !SUPPORTED_IMAGE_TYPES.some(type => type.mimeType === file.value?.type);
		const fileTooBig = file.value.size > 2 * 1024 * 1024;
		return mimeTypeNotSupported || fileTooBig;
	});


	watch(file, (newFile) => {
		// alte URL freigeben
		if (objectUrl.value !== null) {
			URL.revokeObjectURL(objectUrl.value);
			objectUrl.value = null;
		}

		// neue URL erzeugen
		if (newFile !== null) {
			objectUrl.value = URL.createObjectURL(newFile);
		}
	});

	onBeforeUnmount(() => {
		if (objectUrl.value !== null) {
			URL.revokeObjectURL(objectUrl.value);
		}
	});

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
		}
	}

	function closeModal() {
		file.value = null;
		emit("closeModal");
	}

	async function uploadLogoImage() {
		if (file.value === null) {
			return;
		}
		const logo = props.logo();
		if (logo === undefined) {
			return;
		}
		const kennung = logo.proxy.kennung;
		const logoBase64 = await readFileAsBase64(file.value);

		if (logo.proxy.base64 === '') {
			await props.add({ kennung, logoBase64 });
		} else {
			logo.proxy.base64 = logoBase64;
			await logo.patch();
		}
		closeModal();
	}

	/**
	 * Liest ein File-Objekt und gibt dessen Inhalt als Base64-String zurück.
	 * Der zurückgegebene String enthält den Data-URL-Prefix (z. B. "data:image/png;base64,...").
	 */
	function readFileAsBase64(file: File): Promise<string> {
		return new Promise((resolve, reject) => {
			const reader = new FileReader();
			reader.onload = () => resolve(reader.result as string);
			reader.onerror = () => reject(new Error(reader.error?.message ?? "Fehler beim Lesen der Datei"));
			reader.readAsDataURL(file);
		});
	}

	function getFormattedDate(date: string | undefined): string {
		if (date === undefined) {
			return '';
		}
		return new Intl.DateTimeFormat("de-DE", {
			day: "2-digit",
			month: "long",
			year: "numeric",
			timeZone: "UTC",
		}).format(new Date(date));
	}

</script>
