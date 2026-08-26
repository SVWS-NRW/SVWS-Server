<template>
	<div class="h-full">
		<div class="rounded-lg p-2" :class="{'bg-ui-brand/10': highlight}">
			<div>
				<div class="text-headline-md leading-tight inline-flex gap-1">
					<template v-if="terminkonflikte.length > 0">
						<span class="icon i-ri-alert-fill icon-ui-danger" />
						<span>{{ terminkonflikte.length }} Kurse mit Überschneidungen{{ kontextSuffix }}</span>
					</template>
					<template v-else>
						<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
						<span>Keine Kursüberschneidungen{{ kontextSuffix }}</span>
					</template>
				</div>
				<ul v-if="terminkonflikte.length > 0" class="mt-5 flex flex-col gap-3">
					<li v-for="konflikt in terminkonflikte" :key="konflikt.key">
						<div class="flex items-center gap-2">
							<s-gost-klausurplanung-kurs-badge :kursklausur="konflikt.klausur" :tooltip="false" :show-bemerkungen="false" />
							<span v-if="(konflikt.termin !== undefined) && (kontext === 'woche')" class="text-sm opacity-50">{{ presenter.terminDatumKurzText(konflikt.termin) }}</span>
						</div>
						<ul class="mt-1 grid grid-cols-2 gap-x-3 gap-y-0.5 leading-tight">
							<li v-for="schueler in konflikt.schueler" :key="schueler.id">{{ presenter.schuelerName(schueler) }}</li>
						</ul>
					</li>
				</ul>
			</div>

			<div class="mt-6 border-t border-ui-10 pt-6">
				<div class="text-headline-md leading-tight flex flex-wrap items-center gap-x-2 gap-y-1">
					<template v-if="kwFehler.length > 0">
						<span class="icon i-ri-alert-fill icon-ui-caution" />
					</template>
					<template v-else>
						<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
					</template>
					<span>{{ kwFehler.length === 0 ? "Keine" : kwFehler.length }} Fehler für Schüler mit</span>
					<div class="w-20 shrink-0" @click.stop>
						<svws-ui-input-number class="w-full" headless :model-value="state.kwErrorLimit" @update:model-value="state.setKwErrorLimit" :min="3" :max="5" />
					</div>
					<span>oder mehr Klausuren in einer Woche</span>
				</div>
				<div v-if="kwFehler.length > maxVisibleRows" class="mt-2 flex justify-start">
					<svws-ui-checkbox type="toggle" v-model="showMoreKwFehler" @click.stop>Alle anzeigen</svws-ui-checkbox>
				</div>
				<ul v-if="kwFehler.length > 0" class="mt-5 flex flex-col gap-3"
					:class="{'[mask-image:linear-gradient(to_bottom,black_72%,transparent_100%)] [-webkit-mask-image:linear-gradient(to_bottom,black_72%,transparent_100%)]': !showMoreKwFehler && (kwFehler.length > maxVisibleRows)}">
					<li v-for="konflikt in sichtbareKwFehler" :key="'fehler-' + konflikt.a.id">
						<span class="font-bold">{{ presenter.schuelerName(konflikt.a) }}</span>
						<div class="mt-0.5 grid grid-cols-4 justify-items-start gap-x-1 gap-y-2">
							<span v-for="klausur in konflikt.b" :key="klausur.id" class="inline-flex flex-col items-center justify-center text-center"
								@mouseenter="emitMouseEnter(klausur)"
								@mouseleave="emit('kursklausurMouseLeave')">
								<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="klausur" :tooltip="false" :show-bemerkungen="false" />
								<span class="text-sm font-medium">{{ wochenkonfliktKlausurDatumText(klausur) }}</span>
							</span>
						</div>
					</li>
				</ul>
				<div v-if="!showMoreKwFehler && (kwFehler.length > maxVisibleRows)" class="mt-2 font-bold opacity-50">+ {{ kwFehler.length - maxVisibleRows }} weitere</div>
			</div>

			<div class="mt-6 border-t border-ui-10 pt-6">
				<div class="text-headline-md leading-tight flex flex-wrap items-center gap-x-2 gap-y-1">
					<template v-if="kwWarnungen.length > 0">
						<span class="icon i-ri-alert-line icon-ui-warning" />
					</template>
					<template v-else>
						<span class="icon i-ri-checkbox-circle-fill icon-ui-success" />
					</template>
					<span>{{ kwWarnungen.length === 0 ? "Keine" : kwWarnungen.length }} Warnungen für Schüler mit</span>
					<div class="w-20 shrink-0" @click.stop>
						<svws-ui-input-number class="w-full" headless :model-value="state.kwWarnLimit" @update:model-value="state.setKwWarnLimit" :min="2" :max="5" />
					</div>
					<span>Klausuren in einer Woche</span>
				</div>
				<div v-if="kwWarnungen.length > maxVisibleRows" class="mt-2 flex justify-start">
					<svws-ui-checkbox type="toggle" v-model="showMoreKwWarnungen" @click.stop>Alle anzeigen</svws-ui-checkbox>
				</div>
				<ul v-if="kwWarnungen.length > 0" class="mt-5 flex flex-col gap-3"
					:class="{'[mask-image:linear-gradient(to_bottom,black_72%,transparent_100%)] [-webkit-mask-image:linear-gradient(to_bottom,black_72%,transparent_100%)]': !showMoreKwWarnungen && (kwWarnungen.length > maxVisibleRows)}">
					<li v-for="konflikt in sichtbareKwWarnungen" :key="'warnung-' + konflikt.a.id">
						<span class="font-bold">{{ presenter.schuelerName(konflikt.a) }}</span>
						<div class="mt-0.5 grid grid-cols-4 justify-items-start gap-x-1 gap-y-2">
							<span v-for="klausur in konflikt.b" :key="klausur.id" class="inline-flex flex-col items-center justify-center text-center"
								@mouseenter="emitMouseEnter(klausur)"
								@mouseleave="emit('kursklausurMouseLeave')">
								<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="klausur" :tooltip="false" :show-bemerkungen="false" />
								<span class="text-sm font-medium">{{ wochenkonfliktKlausurDatumText(klausur) }}</span>
							</span>
						</div>
					</li>
				</ul>
				<div v-if="!showMoreKwWarnungen && (kwWarnungen.length > maxVisibleRows)" class="mt-2 font-bold opacity-50">+ {{ kwWarnungen.length - maxVisibleRows }} weitere</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import type { GostKursklausur, GostKlausurtermin, GostSchuelerklausurtermin, PairNN, List, SchuelerListeEintrag } from "@core";
	import { useGostKlausurplanungState } from "@ui";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	type KlausurplanungTerminkonflikt = {
		key: string;
		termin: GostKlausurtermin;
		klausur: GostKursklausur;
		schueler: SchuelerListeEintrag[];
	};

	type PendingKursklausurDrop = {
		termin: GostKlausurtermin;
		kursklausur: GostKursklausur;
	};

	type PendingTerminDrop = {
		termin: GostKlausurtermin;
		datum: string;
	};

	const props = withDefaults(defineProps<{
		termine?: GostKlausurtermin[];
		kontext: "termin" | "woche";
		kw?: number;
		highlight?: boolean;
		pendingKursklausurDrop?: PendingKursklausurDrop;
		pendingTerminDrop?: PendingTerminDrop;
	}>(), {
		termine: () => [],
		kw: undefined,
		highlight: false,
		pendingKursklausurDrop: undefined,
		pendingTerminDrop: undefined,
	});

	const emit = defineEmits<{
		kursklausurMouseEnter: [klausur: GostKursklausur];
		kursklausurMouseLeave: [];
	}>();

	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);
	const maxVisibleRows = 3;
	const showMoreKwFehler = ref(false);
	const showMoreKwWarnungen = ref(false);

	const kontextSuffix = computed<string>(() => props.kontext === "termin" ? " für den ausgewählten Termin" : " für die ausgewählte Woche");

	const terminkonflikte = computed<KlausurplanungTerminkonflikt[]>(() => props.termine.flatMap(terminkonflikteByTermin));

	const kwFehler = computed<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>[]>(() => kwKonfliktEntries(state.kwErrorLimit));
	const kwWarnungen = computed<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>[]>(() => kwKonfliktEntries(state.kwWarnLimit)
		.filter(konflikt => konflikt.b.size() < state.kwErrorLimit));

	const sichtbareKwFehler = computed<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>[]>(() => showMoreKwFehler.value ? kwFehler.value : kwFehler.value.slice(0, maxVisibleRows));
	const sichtbareKwWarnungen = computed<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>[]>(() => showMoreKwWarnungen.value ? kwWarnungen.value : kwWarnungen.value.slice(0, maxVisibleRows));

	function terminkonflikteByTermin(termin: GostKlausurtermin): KlausurplanungTerminkonflikt[] {
		const pending = props.pendingKursklausurDrop;
		const konflikte = ((pending !== undefined) && (pending.termin.id === termin.id))
			? state.manager.konflikteNeuKursklausurSchuelerByTerminAndKursklausur(termin, pending.kursklausur)
			: state.manager.konflikteKursklausurSchuelerByTermin(termin);
		return [...konflikte].map(konflikt => ({
			key: `${termin.id}-${konflikt.a.id}`,
			termin,
			klausur: konflikt.a,
			schueler: [...konflikt.b],
		}));
	}

	function kwKonfliktEntries(threshold: number): PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>[] {
		if (props.pendingTerminDrop !== undefined) {
			return [...state.manager.klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(props.pendingTerminDrop.termin, props.pendingTerminDrop.datum, threshold, false)];
		}
		if (props.pendingKursklausurDrop !== undefined) {
			return [...state.manager.klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(props.pendingKursklausurDrop.termin, props.pendingKursklausurDrop.kursklausur, threshold)];
		}
		if ((props.kontext === "woche") && (props.kw !== undefined)) {
			return [...state.manager.klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(props.kw, state.jahrgangsdaten.abiturjahr, threshold, false)];
		}
		return props.termine.length === 0
			? []
			: [...state.manager.klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(props.termine[0], threshold)];
	}

	function wochenkonfliktKlausurDatumText(klausur: GostSchuelerklausurtermin): string {
		if ((props.pendingKursklausurDrop !== undefined) && (state.manager.schuelerklausurBySchuelerklausurtermin(klausur).idKursklausur === props.pendingKursklausurDrop.kursklausur.id)) {
			return presenter.terminDatumKurzText(props.pendingKursklausurDrop.termin);
		}
		const termin = state.manager.terminOrNullBySchuelerklausurtermin(klausur);
		if (termin === null) {
			return "N.N.";
		}
		if ((props.pendingTerminDrop !== undefined) && (termin.id === props.pendingTerminDrop.termin.id)) {
			return presenter.datumKurzGermanOrNN(props.pendingTerminDrop.datum);
		}
		return termin.datum === null ? "N.N." : presenter.terminDatumKurzText(termin);
	}

	function emitMouseEnter(klausur: GostSchuelerklausurtermin): void {
		emit("kursklausurMouseEnter", state.manager.kursklausurBySchuelerklausurtermin(klausur));
	}
</script>
