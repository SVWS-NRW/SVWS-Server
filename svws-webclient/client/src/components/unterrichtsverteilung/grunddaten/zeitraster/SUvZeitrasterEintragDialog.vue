<template>
	<svws-ui-modal v-model:show="show" :auto-close="!dialogLoading" :close-in-title="!dialogLoading">
		<template #modalTitle>Zeiten bearbeiten</template>
		<template #modalContent>
			<p class="mb-3">{{ eintrag === null ? '' : Wochentag.fromIDorException(eintrag.wochentag).beschreibung + ', ' + eintrag.stunde + '. Stunde' }}</p>
			<svws-ui-notification v-if="dialogError" type="error" class="mb-3">{{ dialogError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="zeitBeginn" type="time" placeholder="Beginn" required :disabled="dialogLoading" />
				<svws-ui-text-input v-model="zeitEnde" type="text" placeholder="Ende (HH:MM)" required :disabled="dialogLoading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" :disabled="dialogLoading" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button :disabled="dialogLoading || !darfSpeichern" @click="saveZeitDialog">Speichern</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref, watch } from "vue";

	import type { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
	import { Wochentag } from "@core/core/types/Wochentag";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { UvManager } from "@core/core/utils/uv/UvManager";

	const props = defineProps<{
		eintrag: UvZeitrasterEintrag | null;
		darfSpeichern: boolean;
		speichern: (eintrag: UvZeitrasterEintrag, beginn: number, ende: number) => Promise<void>;
	}>();

	const show = defineModel<boolean>('show', { required: true });
	const dialogLoading = ref(false);
	const dialogError = ref('');
	const zeitBeginn = ref<string | null>('');
	const zeitEnde = ref<string | null>('');

	watch(show, offen => {
		const eintrag = props.eintrag;
		if (!offen || eintrag === null) {
			return;
		}
		dialogError.value = '';
		zeitBeginn.value = DateUtils.gibZeitStringOfMinuten(eintrag.beginn);
		zeitEnde.value = DateUtils.gibZeitStringOfMinuten(eintrag.ende);
	});

	function uhrzeitAlsMinuten(value: string | null): number | null {
		if (value === null || !/^([01]\d|2[0-3]):[0-5]\d$/.test(value)) {
			return null;
		}
		return DateUtils.gibMinutenOfZeitAsString(value);
	}

	/** Prüft die Eingabe und lässt den Entwurf bei einem Speicherfehler geöffnet. */
	async function saveZeitDialog(): Promise<void> {
		const eintrag = props.eintrag;
		if (dialogLoading.value || !props.darfSpeichern || eintrag === null) {
			return;
		}
		const beginn = uhrzeitAlsMinuten(zeitBeginn.value);
		const ende = uhrzeitAlsMinuten(zeitEnde.value);
		if (beginn === null || ende === null || !UvManager.zeitrasterEintragZeitenGueltig(beginn, ende)) {
			dialogError.value = 'Bitte gültige Zeiten zwischen 00:00 und 23:59 eingeben. Das Ende muss nach dem Beginn liegen.';
			return;
		}
		dialogLoading.value = true;
		dialogError.value = '';
		try {
			await props.speichern(eintrag, beginn, ende);
			if (props.eintrag === eintrag) {
				show.value = false;
			}
		} catch {
			if (props.eintrag === eintrag) {
				dialogError.value = 'Speichern fehlgeschlagen. Die Eingaben bleiben erhalten.';
			}
		} finally {
			dialogLoading.value = false;
		}
	}
</script>
