<template>
	<span class="inline-flex items-center gap-1 whitespace-nowrap">
		<button v-if="tooltip" type="button" class="inline-flex whitespace-nowrap rounded focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ui-brand/50"
			title="Klausurschreiber anzeigen" @click.stop="showKursliste = true">
			<span class="svws-ui-badge whitespace-nowrap hover:opacity-75" :class="badgeClass" :style="badgeStyle">{{ badge.text }}</span>
		</button>
		<span v-else class="svws-ui-badge whitespace-nowrap" :class="badgeClass" :style="badgeStyle">{{ badge.text }}</span>
		<svws-ui-tooltip v-if="showBemerkungen && hatBemerkung">
			<template #content>
				<div v-if="bemerkungVorgabe !== null">
					<h3 class="border-b text-headline-md">Bemerkung zur Vorgabe</h3>
					<p>{{ bemerkungVorgabe }}</p>
				</div>
				<div v-if="bemerkungKlausur !== null">
					<h3 class="border-b text-headline-md">Bemerkung zur Kursklausur</h3>
					<p>{{ bemerkungKlausur }}</p>
				</div>
			</template>
			<span class="icon i-ri-edit-2-line icon-ui-brand" />
		</svws-ui-tooltip>
		<svws-ui-modal v-if="showKursliste" v-model:show="showKursliste" size="medium" no-scroll>
			<template #modalTitle>
				<span class="!text-headline-md leading-tight">Klausurschreiber im Kurs {{ kursKurzbezeichnung }}</span>
			</template>
			<template #modalContent>
				<s-gost-klausurplanung-kursliste :kursklausur="kursklausurResolved" :termin :show-header="false" />
			</template>
		</svws-ui-modal>
	</span>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKursklausur, GostSchuelerklausurtermin } from "@core";
	import { DeveloperNotificationException } from "@core";
	import { computed, ref } from "vue";
	import { useGostKlausurplanungState } from "@ui";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const props = withDefaults(defineProps<{
		kursklausur?: GostKursklausur;
		schuelerklausurtermin?: GostSchuelerklausurtermin;
		termin?: GostKlausurtermin;
		tooltip?: boolean;
		showBemerkungen?: boolean;
		light?: boolean;
	}>(), {
		kursklausur: undefined,
		schuelerklausurtermin: undefined,
		termin: undefined,
		tooltip: true,
		showBemerkungen: true,
		light: false,
	});

	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);
	const showKursliste = ref<boolean>(false);
	const kursklausurResolved = computed<GostKursklausur>(() => {
		if (props.kursklausur !== undefined) {
			return props.kursklausur;
		}
		if (props.schuelerklausurtermin === undefined) {
			throw new DeveloperNotificationException("Für die Darstellung des Kurs-Badges wurde weder eine Kursklausur noch ein Schülerklausurtermin übergeben.");
		}
		return state.manager.kursklausurBySchuelerklausurtermin(props.schuelerklausurtermin);
	});
	const kursKurzbezeichnung = computed<string>(() => state.manager.kursKurzbezeichnungByKursklausur(kursklausurResolved.value));
	const badge = computed(() => presenter.kursBadge(kursklausurResolved.value));
	const badgeStyle = computed<string>(() => {
		if (!props.light) {
			return presenter.kursBadgeStyle(kursklausurResolved.value);
		}
		if (badge.value.farbe === null) {
			return "";
		}
		return `color: var(--color-text); background-color: color-mix(in srgb, ${badge.value.farbe} 22%, white); border-color: color-mix(in srgb, ${badge.value.farbe} 55%, white);`;
	});
	const badgeClass = computed(() => props.light ? "border border-ui-25" : "");

	const bemerkungVorgabe = computed(() => {
		const bemerkung = state.manager.vorgabeByKursklausur(kursklausurResolved.value).bemerkungVorgabe;
		return ((bemerkung === null) || (bemerkung.trim().length === 0)) ? null : bemerkung;
	});
	const bemerkungKlausur = computed(() => {
		const bemerkung = kursklausurResolved.value.bemerkung;
		return ((bemerkung === null) || (bemerkung.trim().length === 0)) ? null : bemerkung;
	});
	const hatBemerkung = computed(() => (bemerkungVorgabe.value !== null) || (bemerkungKlausur.value !== null));
</script>
