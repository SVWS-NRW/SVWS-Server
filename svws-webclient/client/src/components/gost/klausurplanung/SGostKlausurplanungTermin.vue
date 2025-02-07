<template>
	<div :id="'gost-klausurtermin-' + termin.id" class="svws-ui-termin h-full flex flex-col group bg-ui-contrast-0 rounded-lg"
		:class="{
			'border shadow-md shadow-ui-contrast-10': !inTooltip,
			'border-ui-contrast-10': !inTooltip && !terminSelected,
			'-mx-2 -my-1': inTooltip,
			'border-r-2 border-r-ui-brand border-l-transparent': inTooltip && !termin.istHaupttermin,
			'border-ui-contrast-500 ring-4 ring-ui-contrast-10': termin.istHaupttermin && !inTooltip && terminSelected,
			'border-ui-brand/50 ring-4 ring-ui-brand/10': !termin.istHaupttermin && !inTooltip && terminSelected,
			'border-l-brand border-l-2': !termin.istHaupttermin,
		}">
		<slot name="header">
			<section class="text-headline-md leading-none px-3 pt-3" :class="{'pb-2': !$slots.tableTitle}">
				<template v-if="!$slots.tableTitle">
					<slot name="title">
						<span class="leading-tight inline-flex gap-0.5" :class="{'text-base': compact || compactWithDate}">
							<span v-if="dragIcon && !compact" class="group-hover:bg-ui-contrast-10 -ml-1 mr-0.5 rounded">
								<span class="icon i-ri-draggable" :class="{'-mx-0.5': !compact}" />
							</span>
							<span class="line-clamp-1 break-all">{{ terminBezeichnung() }}</span>
						</span>
						<div v-if="compactWithDate && termin.datum" class="mb-1 -mt-0.5 opacity-50 text-base">{{ DateUtils.gibDatumGermanFormat(termin.datum) }}</div>
						<div v-if="compact || compactWithDate" class="svws-compact-data text-sm font-medium flex flex-wrap mt-0.5">
							<span class="font-bold">{{ kMan().schuelerklausurterminAktuellGetMengeByTermin(termin).size() }} Schüler,&nbsp;</span>
							<span><span v-if="kMan().minKlausurdauerGetByTermin(termin, true) < kMan().maxKlausurdauerGetByTermin(termin, true)">{{ kMan().minKlausurdauerGetByTermin(termin, true) }} - </span>{{ kMan().maxKlausurdauerGetByTermin(termin, true) }} Minuten</span>

							<span v-if="quartalsauswahl && quartalsauswahl.value === 0">, {{ termin.quartal ? termin.quartal + ' . Quartal' : 'Beide Quartale' }}</span>
						</div>
					</slot>
				</template>
				<div class="flex justify-between w-full gap-1 items-center">
					<div v-if="!compact && !compactWithDate">
						<slot name="datum">
							<template v-if="termin.datum === null">
								<span class="opacity-25 inline-flex items-center gap-1">
									<span class="icon i-ri-calendar-2-line" />
									<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="gotoKalenderdatum(undefined, termin);$event.stopPropagation()" :title="`Datum setzen`" size="small"><span class="icon i-ri-link" /> Datum setzen</svws-ui-button>
								</span>
							</template>
							<template v-else>
								<span class="opacity-50 inline-flex items-center gap-1">
									<span>{{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
									<svws-ui-button v-if="!hideButtonRaeumePlanen" :disabled="!hatKompetenzUpdate" type="transparent" @click="gotoRaumzeitTermin(termin.abijahr, GostHalbjahr.fromIDorException(termin.halbjahr), termin.id);$event.stopPropagation()" :title="`Räume planen`" size="small"><span class="icon i-ri-link" /> Räume planen</svws-ui-button>
								</span>
							</template>
						</slot>
					</div>
					<div v-if="$slots.actions" class="flex gap-0.5 items-center -mr-2 -my-1">
						<slot name="actions" />
					</div>
				</div>
			</section>
		</slot>
		<slot name="main" v-if="!compact">
			<section class="flex flex-col grow" :class="{'mt-2': !$slots.tableTitle, 'px-3': !inTooltip}">
				<slot name="klausuren">
					<div v-if="kursklausuren().size() === 0 && (schuelerklausurtermine().size() === 0)">
						Keine Klausuren
					</div>
					<slot name="kursklausuren" v-if="kursklausuren().size()">
						<svws-ui-table :disable-header="!$slots.tableTitle" :class="{'border-t border-ui-contrast-25': !$slots.tableTitle}">
							<template #header>
								<div class="svws-ui-tr" :style="tableRowStyle" role="row">
									<div class="svws-ui-td col-span-full" role="columnheader">
										<slot name="tableTitle" />
									</div>
								</div>
							</template>
							<template #body>
								<div v-for="klausur in kursklausuren()"
									:key="klausur.id"
									:data="klausur"
									:draggable="onDrag !== undefined && draggable(klausur, termin)"
									@dragstart="onDrag && onDrag(klausur);$event.stopPropagation()"
									@dragend="onDrag && onDrag(undefined);$event.stopPropagation()"
									class="svws-ui-tr" :style="tableRowStyle" role="row"
									:class="[
										props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur, termin),
										{
											'cursor-grab active:cursor-grabbing group': onDrag !== undefined && (draggable === undefined || draggable(klausur, termin))
										}
									]">
									<div class="svws-ui-td" role="cell">
										<span class="icon i-ri-draggable -m-0.5 -ml-3" v-if="onDrag !== undefined && (draggable === undefined || draggable(klausur, termin))" />
									</div>
									<div class="svws-ui-td" :class="{'-ml-2': inTooltip}" role="cell">
										{{ GostHalbjahr.fromIDorException(kMan().vorgabeByKursklausur(klausur).halbjahr).jahrgang }}
									</div>
									<div class="svws-ui-td" role="cell">
										<svws-ui-tooltip :hover="false" :indicator="false" :keep-open autosize>
											<template #content>
												<s-gost-klausurplanung-kursliste :k-man :kursklausur="klausur" :termin :patch-klausur :create-schuelerklausur-termin @modal="keepOpen = $event" :benutzer-kompetenzen />
											</template>
											<span class="svws-ui-badge hover:opacity-75" :style="`background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(klausur) };`">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</span>
											<svws-ui-tooltip>
												<template #content>
													<div v-if="kMan().vorgabeByKursklausur(klausur).bemerkungVorgabe !== null && kMan().vorgabeByKursklausur(klausur).bemerkungVorgabe!.trim().length > 0">
														<h3 class="border-b text-headline-md">Bemerkung zur Vorgabe</h3>
														<p>{{ kMan().vorgabeByKursklausur(klausur).bemerkungVorgabe }}</p>
													</div>
													<div v-if="klausur.bemerkung !== null && klausur.bemerkung.trim().length > 0">
														<h3 class="border-b text-headline-md">Bemerkung zur Kursklausur</h3>
														<p>{{ klausur.bemerkung }}</p>
													</div>
												</template>
												<span class="icon i-ri-edit-2-line icon-primary" v-if="(klausur.bemerkung !== null && klausur.bemerkung.trim().length > 0) || (kMan().vorgabeByKursklausur(klausur).bemerkungVorgabe !== null && kMan().vorgabeByKursklausur(klausur).bemerkungVorgabe!.trim().length > 0)" />
											</svws-ui-tooltip>
										</svws-ui-tooltip>
									</div>
									<div class="svws-ui-td" role="cell">{{ kMan().kursLehrerKuerzelByKursklausur(klausur) }}</div>
									<div class="svws-ui-td flex" role="cell">
										<div>
											<span v-if="kMan().schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(termin, klausur).size() !== kMan().kursAnzahlKlausurschreiberByKursklausur(klausur)" class="font-bold">{{ kMan().schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(termin, klausur).size() }}/</span>
											<span :class="kMan().schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(termin, klausur).size() !== kMan().kursAnzahlKlausurschreiberByKursklausur(klausur) ? 'line-through' : ''">{{ kMan().kursAnzahlKlausurschreiberByKursklausur(klausur) }}/</span>
											<span class="">{{ kMan().kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</span>
										</div>
										<svws-ui-tooltip :hover="true" :indicator="false">
											<template #content>
												Kurs enthält externe Schüler
											</template>
											<svws-ui-badge v-if="kMan().kursklausurMitExternenS(klausur)" type="highlight" size="normal">E</svws-ui-badge>
										</svws-ui-tooltip>
									</div>
									<div class="svws-ui-td svws-align-right" :class="{'pr-3': inTooltip}" role="cell">{{ kMan().vorgabeByKursklausur(klausur).dauer }}</div>
									<div v-if="showKursschiene === true" class="svws-ui-td svws-align-right"><span class="opacity-50">{{ kMan().kursSchieneByKursklausur(klausur).isEmpty() ? "-" : kMan().kursSchieneByKursklausur(klausur).get(0) }}</span></div>
									<div v-if="kMan().quartalGetByTermin(termin) === -1" class="svws-ui-td svws-align-right" role="cell"><span class="opacity-50">{{ kMan().vorgabeByKursklausur(klausur).quartal }}.</span></div>
									<div v-if="showLastKlausurtermin === true" class="svws-ui-td svws-align-right" role="cell"><span class="opacity-50">{{ datumVorklausur(klausur) }}</span></div>
								</div>
							</template>
						</svws-ui-table>
					</slot>
					<slot name="schuelerklausuren" v-if="showSchuelerklausuren && schuelerklausurtermine().size()">
						<s-gost-klausurplanung-schuelerklausur-table :schuelerklausuren="schuelerklausurtermine()"
							:k-man
							:termin
							:on-drag
							:draggable
							:patch-klausur
							:klausur-css-classes />
					</slot>
					<div class="mt-3">
						<svws-ui-textarea-input class="text-sm" :headless="termin.bemerkung === null || termin.bemerkung.trim().length === 0" :rows="1"
							resizeable="none" autoresize placeholder="Bemerkungen zum Termin" :disabled="!hatKompetenzUpdate" :model-value="termin.bemerkung"
							@change="bemerkung => patchKlausurtermin(termin.id, {bemerkung})" @click="$event.stopPropagation()" />
					</div>
					<span class="flex w-full justify-between items-center gap-1 text-sm mt-auto pr-2" :class="{'pl-3': inTooltip}">
						<div class="py-3" :class="{'opacity-50': !kursklausuren().size() && (showSchuelerklausuren && !schuelerklausurtermine().size())}">
							<span class="font-bold">{{ kMan().schuelerklausurterminAktuellGetMengeByTermin(termin).size() }} Schüler, </span>
							<span><span v-if="kMan().minKlausurdauerGetByTermin(termin, true) < kMan().maxKlausurdauerGetByTermin(termin, true)">{{ kMan().minKlausurdauerGetByTermin(termin, true) }} - </span>{{ kMan().maxKlausurdauerGetByTermin(termin, true) }} Minuten</span>
						</div>
						<slot name="loeschen" />
					</span>
				</slot>
			</section>
		</slot>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type {DataTableColumn} from "@ui";
	import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";
	import type { GostKlausurplanManager, GostKursklausur, GostKlausurtermin, GostSchuelerklausurTermin, GostKlausurenCollectionSkrsKrsData} from "@core";
	import { GostHalbjahr, BenutzerKompetenz, DateUtils } from "@core";

	const props = withDefaults(defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>,
		termin: GostKlausurtermin;
		kMan: () => GostKlausurplanManager;
		klausurCssClasses?: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => void;
		onDrag?: (data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData, termin: GostKlausurtermin) => boolean;
		//onDrop?: (zone: GostKlausurplanungDropZone) => void;
		compact?: boolean;
		compactWithDate?: boolean;
		quartalsauswahl?: {value: number};
		dragIcon?: boolean;
		terminSelected?: boolean;
		showKursschiene? : boolean;
		showLastKlausurtermin? : boolean;
		showSchuelerklausuren?: boolean;
		showKursklausurenNachschreiber?: boolean;
		showKlausurenSelbesDatum?: boolean;
		hideButtonRaeumePlanen?: boolean;
		createSchuelerklausurTermin?: (id: number) => Promise<void>;
		patchKlausur?: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		inTooltip?: boolean;
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	}>(), {
		klausurCssClasses: undefined,
		onDrag: undefined,
		draggable: () => false,
		//onDrop: undefined,
		quartalsauswahl: undefined,
		showSchuelerklausuren: false,
		createSchuelerklausurTermin: undefined,
		patchKlausur: undefined,
		showKlausurenSelbesDatum: false,
		showKursklausurenNachschreiber: false,
		hideButtonRaeumePlanen: false,
		inTooltip: false,
	});

	const keepOpen = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const kursklausuren = () => props.kMan().kursklausurMitNachschreibernGetMengeByTermin(props.termin, props.showKursklausurenNachschreiber);
	const schuelerklausurtermine = () => props.kMan().schuelerklausurterminNtGetMengeByTermin(props.termin);

	const datumVorklausur = (klausur: GostKursklausur) => {
		const vorklausur = props.kMan().kursklausurVorterminByKursklausur(klausur);
		if (vorklausur === null)
			return "-";
		const termin = props.kMan().terminOrNullByKursklausur(vorklausur);
		return termin === null || termin.datum === null ? "-" : DateUtils.gibDatumGermanFormat(termin.datum).substring(0,6);
	};

	const terminBezeichnung = () => {
		if (props.termin.bezeichnung !== null && props.termin.bezeichnung.length > 0)
			return props.termin.bezeichnung;
		if (!props.termin.istHaupttermin)
			return "Nachschreibtermin";
		if (kursklausuren().size() > 0)
			return [...props.kMan().kursklausurGetMengeByTermin(props.termin)].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k)).join(", ")
		return "Klausurtermin";
	}

	const tableRowStyle = computed<string>(() => {
		let result = "grid-template-columns: 1rem 2rem minmax(4rem, 1.25fr) 4rem minmax(4.5rem, 0.5fr) minmax(3.25rem, 0.5fr)";
		if (props.showKursschiene === true)
			result += " minmax(1.75rem, 0.25fr)";
		if (props.kMan().quartalGetByTermin(props.termin) === -1)
			result += " minmax(1.75rem, 0.25fr)";
		if (props.showLastKlausurtermin === true)
			result += " minmax(4.75rem, 0.25fr)";
		return result;
	});

</script>

<style lang="postcss" scoped>

	@reference "../../../../../ui/src/assets/styles/index.css"

	.svws-warning {
		.i-ri-draggable {
			opacity: 10%;
		}

		&:hover {
			.i-ri-draggable {
				@apply opacity-100 text-ui-contrast-100;
			}
		}
	}

	.svws-ui-termin {
		.text-input--headless {
			@apply text-headline-md;

			&:not(:focus) {
				&::placeholder {
					@apply text-ui-contrast-100;
				}
			}

			&::placeholder {
				@apply font-bold;
			}
		}

		.svws-klausurplanung-schienen-termin & {
			@apply border-0 rounded-xl;
		}

		.svws-selected & {
			.text-input--headless {
				&:not(:focus) {
					&::placeholder {
						@apply text-ui-brand;
					}
				}

				&:focus {
					&::placeholder {
						@apply text-ui-brand/50;
					}
				}
			}
		}
	}

	.svws-ui-stundenplan--unterricht .svws-ui-termin {
		@apply z-10;

		.px-3 {
			@apply my-auto;
			padding: 0 0.25rem;
		}

		.svws-compact-data {
			@apply justify-center;
		}
	}

</style>
