<template>
	<div v-if="jahrgangsdaten !== undefined" class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button size="small" :type="istManuellerModus ? 'error' : 'transparent'" @click="switchManuellerModus" :title="istManuellerModus ? 'Manuellen Modus deaktivieren' : 'Manuellen Modus aktivieren'">
					Manueller Modus
					<template v-if="istManuellerModus">
						<svg width="1.2em" height="1.2em" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 16V5.75C3 5.06 3.56 4.5 4.25 4.5S5.5 5.06 5.5 5.75V12H6.5V2.75C6.5 2.06 7.06 1.5 7.75 1.5C8.44 1.5 9 2.06 9 2.75V12H10V1.25C10 .56 10.56 0 11.25 0S12.5 .56 12.5 1.25V12H13.5V3.25C13.5 2.56 14.06 2 14.75 2S16 2.56 16 3.25V15H16.75L18.16 11.47C18.38 10.92 18.84 10.5 19.4 10.31L20.19 10.05C21 9.79 21.74 10.58 21.43 11.37L18.4 19C17.19 22 14.26 24 11 24C6.58 24 3 20.42 3 16Z" /></svg>
					</template>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="resetFachwahlen" />
				<svws-ui-modal-hilfe class="ml-auto"> <hilfe-gost-beratung /> </svws-ui-modal-hilfe>
			</svws-ui-sub-nav>
		</Teleport>
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung :abiturdaten-manager="abiturdatenManager" :manueller-modus="istManuellerModus"
				:gost-jahrgangsdaten="jahrgangsdaten()" :set-wahl="setWahl" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-12">
				<svws-ui-content-card title="Vorlage für Sprachbelegungen anpassen">
					<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
						<template #body>
							<div v-for="belegung in belegungen" :key="belegung.sprache" role="row" class="data-table__tr data-table__tbody__tr">
								<div role="cell" class="data-table__td"> {{ belegung.sprache }} </div>
								<div role="cell" class="data-table__td"> {{ belegung.reihenfolge }} </div>
								<div role="cell" class="data-table__td"> {{ belegung.belegungVonJahrgang }} </div>
								<div role="cell" class="data-table__td"> <svws-ui-button type="trash" @click="deleteSprachbelegung(belegung.sprache)" /> </div>
							</div>
						</template>
						<template #footer>
							<div v-if="!sprachen.isEmpty()" role="row" class="data-table__tr data-table__tbody__tr bg-light">
								<div role="cell" class="data-table__td">
									<svws-ui-multi-select :items="sprachen" :item-text="(i : string) => i"
										title="Sprache" v-model="sprache" class="placeholder--visible" headless />
								</div>
								<div role="cell" class="data-table__td"> {{ reihenfolge }} </div>
								<div role="cell" class="data-table__td">
									<svws-ui-multi-select :items="jahrgaenge" :item-text="(i : Jahrgaenge) => i.daten.kuerzel"
										title="Ab Jahrgang" v-model="jahrgang" class="placeholder--visible" headless />
								</div>
								<div role="cell" class="data-table__td">
									<svws-ui-button v-if="sprache !== undefined" type="icon" @click="addSprachbelegung()" size="small">
										<i-ri-add-circle-line />
									</svws-ui-button>
								</div>
							</div>
						</template>
					</svws-ui-data-table>
				</svws-ui-content-card>
				<svws-ui-content-card title="Textvorlagen für die Laufbahnplanung">
					<div class="flex flex-col gap-4">
						<svws-ui-textarea-input placeholder="Beratungsbögen" :model-value="jahrgangsdaten().textBeratungsbogen"
							@update:model-value="doPatch({ textBeratungsbogen: String($event) })" resizeable="vertical" autoresize />
						<svws-ui-textarea-input placeholder="Mailversand" :model-value="jahrgangsdaten().textMailversand"
							@update:model-value="doPatch({ textMailversand: String($event) })" resizeable="vertical" autoresize />
					</div>
				</svws-ui-content-card>
				<svws-ui-content-card v-if="istAbiturjahrgang" title="Beratungslehrer" class="opacity-50">
					<span>Hier kommen die Beratungslehrer hin.</span>
				</svws-ui-content-card>
				<s-laufbahnplanung-card-status :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, computed, ref, type Ref, type ComputedRef } from "vue";
	import type { GostBeratungProps } from "./SGostBeratungProps";
	import { Jahrgaenge, type GostJahrgangsdaten, type List, type Sprachbelegung, ArrayList } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<GostBeratungProps>();

	const cols: Array<DataTableColumn> = [
		{ key: "Sprache", label: "Sprache", span: 0.5, minWidth: 2 },
		{ key: "Folge", label: "Folge", span: 1.5, minWidth: 2},
		{ key: "Beginn", label: "Beginn", align: 'center', span: 0.25, minWidth: 2 },
		{ key: "", label: "", align: 'center', span: 0.25, minWidth: 2 },
	];

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.jahrgangsdaten().abiturjahr > 0));

	const istManuellerModus = ref(false)
	function switchManuellerModus() {
		istManuellerModus.value = istManuellerModus.value ? false : true;
	}

	async function doPatch(data: Partial<GostJahrgangsdaten>) {
		return await props.patchJahrgangsdaten(data, props.jahrgangsdaten().abiturjahr);
	}

	const belegungen: ComputedRef<List<Sprachbelegung>> = computed(() => {
		const result : List<Sprachbelegung> = new ArrayList(props.abiturdatenManager().getSprachendaten().belegungen);
		result.sort({ compare(a : Sprachbelegung, b : Sprachbelegung) { return ((a.reihenfolge === null) ? 0 : a.reihenfolge) - ((b.reihenfolge === null) ? 0 : b.reihenfolge); } });
		return result;
	});

	const sprachen: ComputedRef<List<string>> = computed(() => {
		const sprachen = props.abiturdatenManager().faecher().getFremdsprachenkuerzel();
		for (const belegung of props.abiturdatenManager().getSprachendaten().belegungen)
			sprachen.removeElementAt(sprachen.indexOf(belegung.sprache));
		return sprachen;
	});

	const sprache: Ref<string | undefined> = ref(undefined);

	const reihenfolge: ComputedRef<number> = computed(() => {
		const set : Set<number> = new Set();
		for (const belegung of props.abiturdatenManager().getSprachendaten().belegungen)
			if (belegung.reihenfolge !== null)
				set.add(belegung.reihenfolge);
		for (let i = 1; i < set.size + 2; i++)
			if (!set.has(i))
				return i;
		return 1;
	});

	const jahrgang: Ref<Jahrgaenge> = ref(Jahrgaenge.JG_05);

	const jahrgaenge: Jahrgaenge[] = [ Jahrgaenge.JG_05, Jahrgaenge.JG_06, Jahrgaenge.JG_07, Jahrgaenge.JG_08, Jahrgaenge.JG_09, Jahrgaenge.JG_10, Jahrgaenge.JG_EF];

	async function addSprachbelegung() {
		if (sprache.value !== undefined)
			await props.setSprachbelegung(sprache.value, { reihenfolge: reihenfolge.value, belegungVonJahrgang: jahrgang.value.daten.kuerzel });
	}

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
