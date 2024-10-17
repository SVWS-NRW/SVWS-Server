<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl :halbjahr :zeige-alle-jahrgaenge :set-zeige-alle-jahrgaenge />
	</Teleport>

	<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="big">
		<template #modalTitle>
			Nachschreiber in folgenden Hauptterminen zulassen:
		</template>
		<template #modalContent>
			<svws-ui-table :columns="cols">
				<template #noData>
					<slot name="noData">
				&nbsp;
					</slot>
				</template>

				<template #body>
					<div v-for="termin in kMan().terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value)"
						:key="termin.id"
						class="svws-ui-tr" role="row">
						<div class="svws-ui-td" role="cell">
							<svws-ui-checkbox :title="kMan().schuelerklausurterminNtGetMengeByTermin(termin).size() > 0 ? 'Termin enthält Nachschreiber' : ''" :disabled="kMan().schuelerklausurterminNtGetMengeByTermin(termin).size() > 0" v-model="termin.nachschreiberZugelassen" @update:model-value="patchKlausurtermin(termin.id, { 'nachschreiberZugelassen': termin.nachschreiberZugelassen } )" />
						</div>
						<div class="svws-ui-td" role="cell">
							{{ termin.datum !== null ? DateUtils.gibDatumGermanFormat(termin.datum) : "N.N." }}
						</div>
						<div class="svws-ui-td" role="cell">
							{{ kMan().schuelerklausurterminGetMengeByTermin(termin).size() }}
						</div>
						<div class="svws-ui-td" role="cell">
							{{ [...kMan().kursklausurGetMengeByTermin(termin)].map(k => kMan().kursKurzbezeichnungByKursklausur(k)).join(", ") }}
						</div>
					</div>
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="primary" @click="_showModalTerminGrund = false"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal :show="showModalAutomatischBlocken" size="small">
		<template #modalTitle>
			Automatisch blocken
		</template>
		<template #modalContent>
			<svws-ui-checkbox type="toggle" :disabled="gleiche_fachart_auf_selbe_termine" v-model="nachschreiber_der_selben_klausur_auf_selbe_termine" class="text-left">
				Nachschreiber aus gleichem Kurs zusammenfassen
			</svws-ui-checkbox>
			<svws-ui-checkbox type="toggle" v-model="gleiche_fachart_auf_selbe_termine" @update:model-value="value => nachschreiber_der_selben_klausur_auf_selbe_termine = value ? false : nachschreiber_der_selben_klausur_auf_selbe_termine" class="text-left">
				Nachschreiber mit gleichem Fach und gleicher Kursart zusammenfassen
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div class="page--content page--content--full relative">
		<div class="content-card h-full flex flex-col">
			<div class="content-card--headline mb-4">In Planung</div>
			<div class="content-card--content flex flex-col p-3" @drop="onDrop(undefined)" @dragover="$event.preventDefault()" :class="[(dragData !== undefined && dragData instanceof GostSchuelerklausurTermin && dragData.idTermin !== null) ? 'border-error ring-4 ring-error/10 border-2 rounded-xl border-dashed' : '']">
				<s-gost-klausurplanung-schuelerklausur-table :k-man
					:schuelerklausuren="kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value)"
					:on-drag
					:draggable="() => hatKompetenzUpdate"
					:selected-items="selectedNachschreiber">
					<template #noData>
						<div class="leading-tight flex flex-col gap-0.5">
							<span>Aktuell keine Nachschreibklausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Klausuren können hier zurückgelegt werden.</span>
						</div>
					</template>
				</s-gost-klausurplanung-schuelerklausur-table>
			</div>
		</div>
		<svws-ui-content-card>
			<svws-ui-content-card class="rounded-lg" v-if="multijahrgang()" :has-background="true">
				<template #title>
					<span class="leading-tight text-headline-md gap-1">
						<span v-if="(!zeigeAlleJahrgaenge() && kMan().terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value, true))" class="icon i-ri-alert-fill icon-error px-4" />
						<span>Jahrgangsübergreifende Planung</span>
						<span v-if="(!zeigeAlleJahrgaenge() && kMan().terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value, true))"> aktiviert, da jahrgangsgemischte Termine existieren</span>
					</span>
				</template>
				<ul>
					<li class="flex font-bold">
						<span>{{ kMan().schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value).size() }} Nachschreiber im akutellen Jahrgang,&nbsp;</span>
						<span v-if="kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value).size() == 0" class="text-green-500">alle zugewiesen.</span>
						<span v-else class="text-red-500">nicht alle zugewiesen.</span>
					</li>
					<template v-for="pair in GostKlausurplanManager.halbjahreParallelUndAktivGetMenge(jahrgangsdaten.abiturjahr, halbjahr, false)" :key="pair.a">
						<li class="flex" v-if="kMan().schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(pair.a, pair.b, quartalsauswahl.value).size() > 0">
							<span>{{ kMan().schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(pair.a, pair.b, quartalsauswahl.value).size() }} Nachschreiber im Jahrgang {{ pair.b.jahrgang }},&nbsp;</span>
							<span v-if="kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(pair.a, pair.b, quartalsauswahl.value).size() == 0" class="text-green-500">alle zugewiesen.</span>
							<span v-else class="text-red-500">nicht alle zugewiesen.</span>
							<svws-ui-button type="icon" @click="gotoNachschreiber(pair.a, pair.b)" :title="`Zur Planung des Jahrgangs`" size="small"><span class="icon i-ri-link" /></svws-ui-button>
						</li>
					</template>
				</ul>
			</svws-ui-content-card>
			<div class="flex justify-between items-start mt-5 mb-5">
				<div class="flex flex-wrap items-center gap-2 w-full">
					<svws-ui-button :disabled="!hatKompetenzUpdate" @click="erzeugeKlausurtermin(quartalsauswahl.value, false)"><span class="icon i-ri-add-line -ml-1" />Neuer Nachschreibtermin</svws-ui-button>
					<svws-ui-button :disabled="!hatKompetenzUpdate" type="secondary" @click="_showModalTerminGrund = true"><span class="icon i-ri-checkbox-circle-line -ml-1" />Haupttermin zulassen</svws-ui-button>
					<svws-ui-button type="secondary" :disabled="!hatKompetenzUpdate || selectedNachschreiber.isEmpty()" @click="showModalAutomatischBlocken().value = true"><span class="icon i-ri-sparkling-line" />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
				</div>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(45rem,1fr))] gap-4 pt-2 -mt-2">
				<template v-if="termine.size()">
					<s-gost-klausurplanung-nachschreiber-termin v-for="termin of termine" :key="termin.id"
						:benutzer-kompetenzen
						:termin="() => termin"
						:class="{'is-drop-zone': dragData !== undefined && kMan().konfliktPaarGetMengeTerminAndSchuelerklausurtermin(termin, dragData as GostSchuelerklausurTermin).isEmpty()}"
						:k-man
						:drag-data
						:on-drag
						:on-drop
						:draggable
						:termin-selected="terminSelected?.id===termin.id"
						@click="terminSelected=(terminSelected?.id===termin.id?undefined:termin);$event.stopPropagation()"
						:loesche-klausurtermine
						:patch-klausurtermin
						:klausur-css-classes
						:patch-klausur
						:update-klausurblockung
						:show-schuelerklausuren="true"
						:goto-kalenderdatum
						:goto-raumzeit-termin />
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { JavaSet} from "@core";
	import { GostKlausurplanManager, GostKursklausur, DateUtils, GostKlausurtermin, GostSchuelerklausurTermin, GostNachschreibterminblockungKonfiguration, HashSet, ArrayList, BenutzerKompetenz } from "@core";
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostKlausurplanungNachschreiberProps } from "./SGostKlausurplanungNachschreiberProps";
	import type { DataTableColumn } from "@ui";

	const _showModalTerminGrund = ref<boolean>(false);
	const showModalTerminGrund = () => _showModalTerminGrund;
	const _showModalAutomatischBlocken = ref<boolean>(false);
	const showModalAutomatischBlocken = () => _showModalAutomatischBlocken;
	const nachschreiber_der_selben_klausur_auf_selbe_termine = ref(false);
	const gleiche_fachart_auf_selbe_termine = ref(false);

	const multijahrgang = () => props.zeigeAlleJahrgaenge() || props.kMan().terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, true);

	const selectedNachschreiber = ref<JavaSet<GostSchuelerklausurTermin>>(new HashSet<GostSchuelerklausurTermin>());

	const loading = ref<boolean>(false);

	const props = defineProps<GostKlausurplanungNachschreiberProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const terminSelected = ref<GostKlausurtermin | undefined>(undefined);

	const onDrag = (data: GostKlausurplanungDragData) => {
		terminSelected.value = undefined;
		dragData.value = data;
	};

	async function blocken() {
		showModalAutomatischBlocken().value = false
		loading.value = true;
		const config = new GostNachschreibterminblockungKonfiguration();
		config.termine = termine.value;
		selectedNachschreiber.value.retainAll(props.kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value));
		config.schuelerklausurtermine = new ArrayList<GostSchuelerklausurTermin>(selectedNachschreiber.value);
		config._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = nachschreiber_der_selben_klausur_auf_selbe_termine.value;
		config._regel_gleiche_fachart_auf_selbe_termine_verteilen = gleiche_fachart_auf_selbe_termine.value;
		await props.blockenNachschreibklausuren(config);
		selectedNachschreiber.value.clear();
		loading.value = false;
	}

	function draggable(data: GostKlausurplanungDragData) {
		return hatKompetenzUpdate.value && data instanceof GostSchuelerklausurTermin;
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur || dragData.value instanceof GostSchuelerklausurTermin) {
			if (zone === undefined && dragData.value.idTermin !== null)
				await props.patchKlausur(dragData.value, {idTermin: null});
			else if (zone instanceof GostKlausurtermin) {
				if (zone.id !== dragData.value.idTermin) {
					await props.patchKlausur(dragData.value, {idTermin: zone.id});
					terminSelected.value = zone;
				}
			}
		}
	};

	const termine = computed(() => props.kMan().terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, multijahrgang()));

	const klausurCssClasses = (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		if (klausur instanceof GostKursklausur && dragData.value !== undefined)
			return {
				"bg-red-500": props.kMan().konfliktZuKursklausurBySchuelerklausur((dragData.value as GostSchuelerklausurTermin), klausur),
			}
		else if (klausur instanceof GostSchuelerklausurTermin && dragData.value !== undefined)
			return {
				"bg-red-500": props.kMan().schuelerklausurGetByIdOrException(klausur.idSchuelerklausur).idSchueler === props.kMan().schuelerklausurGetByIdOrException((dragData.value as GostSchuelerklausurTermin).idSchuelerklausur).idSchueler,
			}
		else if (klausur instanceof GostSchuelerklausurTermin && termin !== undefined) {
			return {
				"bg-red-200": props.kMan().konfliktPaarGetMengeTerminAndSchuelerklausurtermin(termin, klausur).size() > 0,
			}
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "id", label: " ", fixedWidth: 2 },
			{ key: "datum", label: "Datum", fixedWidth: 8 },
			{ key: "size", label: "#SuS", fixedWidth: 4 },
			{ key: "faecher", label: "Kurse" },
		];

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>

<style lang="postcss" scoped>
.page--content {
  	@apply grid;
  	grid-template-columns: minmax(44rem, .3fr) 1fr;
}

.svws-ui-tab-content {
	@apply overflow-y-hidden items-start;

	.page--content {
		@apply h-full py-0 auto-rows-auto;

		.content-card {
			@apply max-h-full pt-8 pb-8 px-4 -mx-4 overflow-y-auto h-[unset];
			scrollbar-gutter: stable;
		}
	}
}

.is-drop-zone {
	@apply relative bg-primary/5;

	&:before {
		content: '';
		@apply absolute inset-1 border border-svws pointer-events-none rounded-lg ring-4 ring-svws/25;
	}
}

</style>
