<template>
	<svws-ui-secondary-menu>
		<template #headline> Abiturjahrgangsauswahl</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model:selected="selected" :cols="cols" :rows="rows" :footer="false" asc="false">
				</svws-ui-table>
			</div>
			<svws-ui-dropdown variant="secondary" class="float-right m-4">
				<template #dropdownButton>Abiturjahr hinzufügen</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item
						v-for="jahrgang in jahrgaenge"
						:key="jahrgang.id"
						class="px-2"
						@click="abiturjahr_hinzufuegen(jahrgang)"
					>{{ jahrgang.kuerzel }}</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown>
			<div v-if="main.config.kursblockung_aktiv.size && abiturjahr" class="container">
				<table class="table mt-8">
					<thead class="table--header">
						<tr>
							<td class="table--cell table--cell-padded">
								<div class="table--header-col">
									<span class="table--header-col--text">
										Halbjahr
									</span>
								</div>
							</td>
						</tr>
					</thead>
					<tbody>
						<template v-for="hj in halbjahre" :key="hj.id">
							<tr class="table--row">
								<td
									class="table--cell table--cell-padded table--border"
									:class="{ 'table--row-selected': hj === selected_hj }"
									@click="selected_hj = hj"
								>
									{{ hj.kuerzel }}
									<svws-ui-button
										v-if="hj === selected_hj"
										size="small"
										type="primary"
										class="float-right cursor-pointer"
										@click="blockung_hinzufuegen"
									>Blockung hinzufügen</svws-ui-button>
								</td>
							</tr>
							<template v-if="hj === selected_hj && !wait">
								<template v-for="blockung in rows_blockungsswahl" :key="blockung.id">
									<tr class="table--row">
										<td
											class="table--cell table--cell-padded table--border" 
											:class="{ 'table--row-selected': blockung === selected_blockungauswahl }"
											colspan="3"
											@click="selected_blockungauswahl = blockung"
											>
											<template v-if=" blockung === selected_blockungauswahl ">
												<div class="float-left flex">
													<span v-if="!edit_blockungsname" class="px-3 underline decoration-dashed underline-offset-2 cursor-pointer" @click="edit_blockungsname = true">{{blockung.name}}--{{blockung.id}}</span>
													<svws-ui-text-input v-else v-model="blockung.name" style="width: 10rem" headless focus @keyup.enter="edit_blockungsname=false" @input="patch_blockung(blockung)"/>
												</div>
												<div class="float-right flex gap-1">
													<svws-ui-button class="cursor-pointer" size="small" @click="create_blockung" >Ergebnisse berechnen</svws-ui-button >
													<svws-ui-button size="small" type="danger" class="cursor-pointer" @click="remove_blockung">Löschen </svws-ui-button>
												</div>
											</template>
											<template v-else>
												<span class="px-3">{{blockung.name}}</span>
											</template>
										</td>
									</tr>
									<!-- Api-Status für das Halbjahr -->
									<auswahl-blockung-api-status :blockung="blockung"/>
								</template>
							</template>
						</template>
					</tbody>
				</table>
				<table class="table mt-8">
					<thead class="table--header">
						<tr>
							<td class="table--cell table--cell-padded">
								<div class="table--header-col">
									<span class="table--header-col--text">
										ID
									</span>
								</div>
							</td>
							<td class="table--cell table--cell-padded">
								<div class="table--header-col">
									<span class="table--header-col--text">
										Kollisionen
									</span>
								</div>
							</td>
							<td class="table--cell table--cell-padded">
								<div class="table--header-col">
									<span class="table--header-col--text">
										Bewertungen
									</span>
								</div>
							</td>
						</tr>
					</thead>
					<tbody>
						<template v-for="ergebnis in rows_ergebnisse" :key="ergebnis.id">
							<tr 
								class="table--row"
								:class="{ 'table--row-selected': ergebnis === selected_ergebnis }"
								@click="selected_ergebnis = ergebnis"
							>
								<td class="table--cell table--cell-padded table--border" >
									{{ ergebnis.id }}
								</td>
								<td class="table--cell table--cell-padded table--border" >
									<div class="flex">
										<span class="inline-flex font-semibold" >
											<span :style="{'background-color': color1(ergebnis)}">{{manager?.getOfBewertung1Wert(ergebnis.id)}}</span>&nbsp
											<span :style="{'background-color': color2(ergebnis)}">{{manager?.getOfBewertung2Wert(ergebnis.id)}}</span>&nbsp
											<span :style="{'background-color': color3(ergebnis)}">{{manager?.getOfBewertung3Wert(ergebnis.id)}}</span>&nbsp
											<span :style="{'background-color': color4(ergebnis)}">{{manager?.getOfBewertung4Wert(ergebnis.id)}}</span>
										</span>
									</div>
								</td>
								<td class="table--cell table--cell-padded table--border" >
									<div v-if="ergebnis === selected_ergebnis" class="flex gap-1 float-right ">
										<svws-ui-button size="small" type="primary" class="cursor-pointer" @click="derive_blockung"> Ableiten </svws-ui-button>
										<svws-ui-button v-if="rows_ergebnisse.size() > 1" size="small" type="danger" class="cursor-pointer" @click="remove_ergebnis">
											<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
										</svws-ui-button>
									</div>
								</td>
							</tr>
						</template>
					</tbody>
				</table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
import {
	GostBlockungListeneintrag,
	GostBlockungsdatenManager,
	GostBlockungsergebnisListeneintrag,
	GostHalbjahr,
	GostJahrgang,
	JahrgangsListeEintrag,
	List,
	Vector
} from "@svws-nrw/svws-core-ts";
import {
	computed,
	ComputedRef,
	reactive,
	ref,
	Ref,
	WritableComputedRef
} from "vue";
import { App } from "~/apps/BaseApp";
import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;
const appJahrgaenge = main.apps.jahrgaenge;
const cols = [
	{
		id: "bezeichnung",
		title: "Bezeichnung",
		width: "10em",
		sortable: true
	},
	{
		id: "abiturjahr",
		title: "Abiturjahr",
		sortable: true
	},
	{
		id: "jahrgang",
		title: "Stufe",
		sortable: true
	}
];

// const manager = reactive(app.dataKursblockung.datenmanager);
const wait: Ref<boolean> = ref(false);
const halbjahre = GostHalbjahr.values();
const hj_memo: Ref<GostHalbjahr | undefined> = ref(undefined)
const edit_blockungsname: Ref<boolean> = ref(false)

const rows: ComputedRef<GostJahrgang[]> = 
	computed(() => {
	const list = [...app.auswahl.liste];
	return list.sort((a, b) =>
		(a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1
	)});

const manager: ComputedRef<GostBlockungsdatenManager | undefined> =
	computed(()=> app.dataKursblockung.manager_container ? app.dataKursblockung.datenmanager : undefined);
const rows_blockungsswahl: ComputedRef<GostBlockungListeneintrag[]> =
	computed(() => app.blockungsauswahl.liste);
const rows_ergebnisse: ComputedRef<List<GostBlockungsergebnisListeneintrag>> =
	computed(() => manager.value?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>());
const abiturjahr: ComputedRef<number> =
	computed(() => Number(app.dataJahrgang.daten?.abiturjahr));
const jahrgaenge: ComputedRef<JahrgangsListeEintrag[]> =
	computed( () => appJahrgaenge.auswahl.liste);

const selected_hj: WritableComputedRef<GostHalbjahr> = computed({
	get(): GostHalbjahr {
		manager.value?.getHalbjahr()
		const hj = hj_memo.value || manager.value?.getHalbjahr()
		return hj
			|| GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(
				abiturjahr.value,
				App.akt_abschnitt.schuljahr,
				App.akt_abschnitt.abschnitt
			)
			|| GostHalbjahr.EF1
	},
	set(hj: GostHalbjahr) {
		if (hj === selected_hj.value || !hj) return;
		hj_memo.value = hj
		wait.value = true;
		if (selected.value?.abiturjahr)
			app.blockungsauswahl
				.update_list(
					selected.value.abiturjahr.valueOf(),
					hj.id
				)
				.then(() => {
					wait.value = false;
				});
		else wait.value = false;
	}});

const selected_blockungauswahl: WritableComputedRef<GostBlockungListeneintrag | undefined> =
	computed({
	get(): GostBlockungListeneintrag | undefined {
		return app.blockungsauswahl.ausgewaehlt;
	},
	set: (value: GostBlockungListeneintrag | undefined) => {
		if (value !== app.blockungsauswahl.ausgewaehlt) {
			app.blockungsauswahl.ausgewaehlt = value;
			edit_blockungsname.value = false
		}
	}
});

const selected_ergebnis: WritableComputedRef<GostBlockungsergebnisListeneintrag | undefined> =
	computed({
	get(): GostBlockungsergebnisListeneintrag | undefined {
		return app.blockungsergebnisauswahl.ausgewaehlt;
	},
	set(value: GostBlockungsergebnisListeneintrag | undefined) {
		if (app.blockungsergebnisauswahl) {
			app.blockungsergebnisauswahl.ausgewaehlt = value;
		}
	}
});

const selected: WritableComputedRef<GostJahrgang | undefined> =
	computed({
	get(): GostJahrgang | undefined {
		return app.auswahl.ausgewaehlt;
	},
	set(value: GostJahrgang | undefined) {
		if (app.auswahl) app.auswahl.ausgewaehlt = value
	}
});

async function abiturjahr_hinzufuegen(jahrgang: JahrgangsListeEintrag) {
	try {
		const abiturjahr = await app.dataJahrgang.post_jahrgang(jahrgang.id);
		await app.auswahl.update_list();
		const jahr = app.auswahl.liste?.find(j => j.abiturjahr === abiturjahr);
		selected.value = jahr;
	} catch (e) {
		console.log("Fehler: ", e);
	}
}
const create_blockung = () => {
	const halbjahresHashCode: number = app.blockungsauswahl.ausgewaehlt?.hashCode() ? app.blockungsauswahl.ausgewaehlt.hashCode() : -1;
	const id = app.blockungsauswahl.ausgewaehlt?.id;
	if (!id)
		return;
	const apiCall = app.create_blockung(id, halbjahresHashCode);
	main.config.apiLoadingStatus.addStatusByPromise(apiCall, {message: 'Blockung wird berechnet...', caller: 'Kursplanung (Gost)', categories: [GOST_CREATE_BLOCKUNG_SYMBOL]});
};

async function blockung_hinzufuegen() {
	if (!selected.value?.abiturjahr)
		return;
	await App.api.createGostAbiturjahrgangBlockung(App.schema, selected.value.abiturjahr.valueOf(), selected_hj.value.id);
	const abiturjahr = selected.value.abiturjahr.valueOf();
	if (!abiturjahr)
		return;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}

async function remove_blockung() {
	if (!selected_blockungauswahl.value)
		return;
	await App.api.deleteGostBlockung(App.schema, selected_blockungauswahl.value?.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf();
	if (!abiturjahr)
		return;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id);
}

async function remove_ergebnis() {
	if (!selected_ergebnis.value)
		return;
	await App.api.deleteGostBlockungsergebnis(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id)
}

async function derive_blockung() {
	if (!selected_ergebnis.value)
		return;
	await App.api.dupliziereGostBlockungMitErgebnis(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}
async function patch_blockung(blockung: GostBlockungListeneintrag) {
	await app.dataKursblockung.patch({name: blockung.name});
}

function color1(ergebnis: GostBlockungsergebnisListeneintrag): string {
	return `hsl(${Math.round((1 - (manager.value?.getOfBewertung1Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
}
function color2(ergebnis: GostBlockungsergebnisListeneintrag): string {
	return `hsl(${Math.round((1 - (manager.value?.getOfBewertung2Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
}
function color3(ergebnis: GostBlockungsergebnisListeneintrag): string {
	return `hsl(${Math.round((1 - (manager.value?.getOfBewertung3Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
}
function color4(ergebnis: GostBlockungsergebnisListeneintrag): string {
	return `hsl(${Math.round((1 - (manager.value?.getOfBewertung4Intervall(ergebnis.id)||0)) * 120)},100%,80%)`
}
</script>

<style>
.loading-disclaimer {
	background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
	--tw-bg-opacity: 1;
	--tw-border-opacity: 1;
	border-width: 1px;
	padding: .25rem .75rem;
	line-height: 1.125;
}

.loading-spinner-dimensions {
	height: 1rem;
	width: 1rem;
}

.loading-display {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

.loading-rotation {
	display: block;
	position: relative;
	-webkit-animation: spin 6s steps(11, end) infinite;
	-moz-animation: spin 6s steps(11, end) infinite;
	animation: spin 6s steps(11, end) infinite;
}

.api-error-text {
	color: rgb(var(--svws-ui-color-error));
	font-weight: 700;
}

@-webkit-keyframes spin {
	0% {
		-webkit-transform: rotate(0deg);
	}

	100% {
		-webkit-transform: rotate(360deg);
	}
}

@-moz-keyframes spin {
	0% {
		-webkit-transform: rotate(0deg);
	}

	100% {
		-webkit-transform: rotate(360deg);
	}
}

@keyframes spin {
	0% {
		-webkit-transform: rotate(0deg);
	}

	100% {
		-webkit-transform: rotate(360deg);
	}
}
</style>