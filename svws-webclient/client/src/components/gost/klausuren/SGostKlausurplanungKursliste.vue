<template>
	<svws-ui-modal v-if="show" :show size="small">
		<template #modalTitle>
			Grund für Fehlen angeben
		</template>
		<template #modalContent>
			<svws-ui-text-input focus placeholder="z.B. Krankheit" @update:model-value="bemerkung => terminSelected.bemerkung = bemerkung" @keyup.enter="createTermin(true)" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="createTermin(false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="createTermin(true)"> Nachschreibtermin erstellen </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div class="flex flex-col text-left">
		<div v-if="bemerkungVorgabe !== null" class="mb-4 rounded-lg border border-ui-warning bg-ui-warning-weak px-4 py-3">
			<div class="text-base font-bold leading-tight">Bemerkung zur Vorgabe</div>
			<div class="mt-1.5 whitespace-pre-wrap leading-snug">{{ bemerkungVorgabe }}</div>
		</div>
		<header v-if="showHeader" class="border-b border-ui-25 pb-3">
			<h2 class="text-headline leading-tight">Klausurschreiber im Kurs {{ state.manager.kursKurzbezeichnungByKursklausur(kursklausur) }}</h2>
			<div class="mt-1.5 text-base leading-tight opacity-60">
				{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(kursklausur) }} Schüler
			</div>
		</header>
		<div v-else class="pb-2 text-base leading-tight opacity-60">
			{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(kursklausur) }} Schüler
		</div>
		<div class="max-h-[calc(100vh-18rem)] overflow-y-auto border-t border-ui-25">
			<div v-for="s in state.manager.schuelerklausurterminGetMengeByKursklausur(kursklausur)" :key="s.id"
				class="grid grid-cols-[minmax(0,1fr)_auto] items-center gap-3 border-b border-ui-10 py-2 text-base leading-tight last:border-b-0"
				:class="{'bg-ui-danger/5': !schreibtTermin(s)}">
				<div class="min-w-0">
					<div class="truncate" :class="{'text-ui-danger line-through': !schreibtTermin(s)}">
						{{ presenter.schuelerNameBySchuelerklausurtermin(s) }}
					</div>
					<div v-if="hatKompetenzUpdate && (termin !== undefined) && !schreibtTermin(s)" class="mt-1 grid grid-cols-[8.25rem_minmax(0,1fr)] items-center gap-2">
						<span class="self-end pb-1 text-base leading-tight opacity-60">Versäumnisgrund:</span>
						<svws-ui-text-input :disabled="!hatKompetenzUpdate" class="min-w-0 text-base" :model-value="s.bemerkung" @change="bemerkung => state.patchKlausur(s, {bemerkung})" />
					</div>
				</div>
				<svws-ui-tooltip v-if="hatKompetenzUpdate && (termin !== undefined) && schreibtTermin(s)">
					<template #content>
						Klausur nicht mitgeschrieben
					</template>
					<svws-ui-button type="icon" size="small" title="Klausur nicht mitgeschrieben" @click="terminSelected = s; show = true">
						<span class="icon i-ri-user-forbid-line" />
					</svws-ui-button>
				</svws-ui-tooltip>
			</div>
			<div class="grid grid-cols-[8.25rem_minmax(0,1fr)] items-start gap-2 py-3">
				<div class="self-start pt-2 text-base font-medium leading-tight opacity-70">Bemerkungen zum Kurs:</div>
				<svws-ui-textarea-input resizeable="none" autoresize :disabled="!hatKompetenzUpdate" :model-value="kursklausur.bemerkung" @change="bemerkung => state.patchKlausur(kursklausur, {bemerkung})" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';
	import type { GostKursklausur, GostKlausurtermin } from '@core';
	import { BenutzerKompetenz, GostSchuelerklausurtermin } from '@core';
	import { useBenutzerState, useGostKlausurplanungState } from '@ui';
	import { useKlausurplanungPresenter } from './SGostKlausurplanungPresenter';

	const props = withDefaults(defineProps<{
		kursklausur: GostKursklausur;
		termin?: GostKlausurtermin;
		showHeader?: boolean;
	}>(), {
		termin: undefined,
		showHeader: true,
	});

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const show = ref<boolean>(false);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));
	const bemerkungVorgabe = computed<string | null>(() => {
		const bemerkung = state.manager.vorgabeByKursklausur(props.kursklausur).bemerkungVorgabe;
		return ((bemerkung === null) || (bemerkung.trim().length === 0)) ? null : bemerkung;
	});

	const terminSelected = ref<GostSchuelerklausurtermin>(new GostSchuelerklausurtermin());

	function schreibtTermin(schuelerklausurtermin: GostSchuelerklausurtermin): boolean {
		if (props.termin === undefined) {
			return true;
		}
		const schueler = state.manager.schuelerGetBySchuelerklausurtermin(schuelerklausurtermin);
		return state.manager.schuelerSchreibtKlausurtermin(schueler.id, props.termin);
	}

	const createTermin = async (create: boolean) => {
		if (create) {
			await state.patchKlausur(terminSelected.value, { bemerkung: terminSelected.value.bemerkung });
			const sktNeu = new GostSchuelerklausurtermin();
			sktNeu.idSchuelerklausur = terminSelected.value.idSchuelerklausur;
			await state.createSchuelerklausurtermin(sktNeu);
		}
		show.value = false;
		terminSelected.value = new GostSchuelerklausurtermin();
	};

</script>
