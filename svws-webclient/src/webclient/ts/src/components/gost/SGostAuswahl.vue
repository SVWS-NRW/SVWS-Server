<template>
	<svws-ui-secondary-menu>
		<template #headline>Abiturjahrgänge</template>
		<template #header></template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model="selected"
					:columns="cols"
					:data="rows"
					id="abiturjahr"
					:footer="true"
				>
					<template #cell-abiturjahr="{ row }">
						{{row.abiturjahr === -1 ? '':row.abiturjahr}}
						<span v-if="(pending && row.abiturjahr === selected?.abiturjahr)" class="loading-spinner-dimensions">
							<img src="/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation" ></span>
					</template>
					<template #footer>
						<svws-ui-dropdown variant="icon" class="">
							<template #dropdownButton>Abiturjahr hinzufügen</template>
							<template #dropdownItems>
								<svws-ui-dropdown-item
									v-for="jahrgang in jahrgaenge"
									:key="jahrgang.id"
									class="px-2"
									@click="abiturjahr_hinzufuegen(jahrgang)"
								>{{ jahrgang.kuerzel }}
								</svws-ui-dropdown-item>
							</template>
						</svws-ui-dropdown>
					</template>
				</svws-ui-table>
			</div>
			<div v-if="main.config.kursblockung_aktiv.size && abiturjahr > 0" class="mt-20">
				<h3 class="text-headline px-6 mb-3">Blockungen</h3>
				<svws-ui-table v-model="selected_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :data="halbjahre" class="mb-10">
					<template #body="{rows}">
						<template v-for="row in <GostHalbjahr[]>rows" :key="row.id">
							<tr :class="{'vt-clicked': row.id === selected_hj?.id}" @click="select_hj(row)">
								<td>
									{{row.kuerzel}}
									<span v-if="((pending || app.blockungsauswahl.pending) && row.id === selected_hj?.id)" class="loading-spinner-dimensions">
										<img src="/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation" ></span>
									<svws-ui-button type="transparent" v-if="allow_add_blockung(row)" @click.stop="blockung_hinzufuegen" :disabled="pending">Blockung hinzufügen</svws-ui-button>
								</td>
							</tr>
							<template v-if="row.id === selected_hj?.id" v-for="blockung in rows_blockungswahl" :key="blockung.hashCode">
								<tr :class="{'vt-clicked': blockung === selected_blockungauswahl}" class="table--row-indent" @click="select_blockungauswahl(blockung)">
									<td v-if=" blockung === selected_blockungauswahl ">
										<div class="flex">
											<span v-if="(!edit_blockungsname && blockung === selected_blockungauswahl)"
												class="text-input--inline"
												@click.stop="edit_blockungsname = true"
											>{{blockung.name}}</span>
											<svws-ui-text-input v-else
												:modelValue="blockung.name"
												style="width: 10rem"
												headless focus
												@keyup.enter="edit_blockungsname=false"
												@keyup.escape="edit_blockungsname=false"
												@update:modelValue="patch_blockung"/>
										</div>
										<svws-ui-icon v-if="blockung.istAktiv" > <i-ri-pushpin-fill /> </svws-ui-icon>
										<div v-if="allow_add_blockung(row)" class="flex gap-1">
											<svws-ui-button size="small" type="secondary" @click.stop="create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="pending">Berechnen</svws-ui-button >
											<svws-ui-button size="small" type="danger" @click.stop="toggle_remove_blockung_modal" title="Blockung löschen" :disabled="pending">
												<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
											</svws-ui-button>
										</div>
									</td>
									<td v-else>
										<div class="flex justify-between w-full">
											<span>{{blockung.name}}</span>
											<svws-ui-icon v-if="blockung.istAktiv" > <i-ri-pushpin-fill /> </svws-ui-icon>
										</div>
									</td>
								</tr>
								<auswahl-blockung-api-status :blockung="blockung"/>
							</template>
						</template>
					</template>
				</svws-ui-table>
				<svws-ui-table
					v-if="rows_ergebnisse.size()>0"
					v-model="selected_ergebnis"
					v-model:selection="selected_ergebnisse"
					is-multi-select
					:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: 15 }]"
					:data="rows_ergebnisse.toArray()"
					:footer="selected_ergebnisse.length > 0"
				>
					<template #cell-bewertung="{ row }: {row: GostBlockungsergebnisListeneintrag}">
						<span class="flex gap-1 cell--bewertung" >
							<span :style="{'background-color': color1(row)}">{{manager?.getOfBewertung1Wert(row.id)}}</span>
							<span :style="{'background-color': color2(row)}">{{manager?.getOfBewertung2Wert(row.id)}}</span>
							<span :style="{'background-color': color3(row)}">{{manager?.getOfBewertung3Wert(row.id)}}</span>
							<span :style="{'background-color': color4(row)}">{{manager?.getOfBewertung4Wert(row.id)}}</span>
						</span>
						<svws-ui-icon v-if="row.istVorlage" > <i-ri-pushpin-fill /></svws-ui-icon>
						<div v-if="(row.id === selected_ergebnis?.id && !selected_blockungauswahl?.istAktiv)" class="flex gap-1">
							<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="pending"> Ableiten </svws-ui-button>
							<svws-ui-button v-if="rows_ergebnisse.size() > 1" size="small" type="danger" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="pending">
								<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
							</svws-ui-button>
						</div>
					</template>
					<template #footer>
						<span v-if="selected_ergebnisse.length === rows_ergebnisse.size()">Mindestens ein Ergebnis behalten!</span>
						<svws-ui-button @click="remove_ergebnisse" type="danger" size="small" :disabled="selected_ergebnisse.length > rows_ergebnisse.size() - 1">
							Auswahl löschen
						</svws-ui-button>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal ref="modal_remove_blockung" size="small">
		<template #modalTitle>Blockung löschen</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Soll die Blockung mit allen Ergebnissen gelöscht werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_remove_blockung_modal()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
import { List, Vector, GostBlockungListeneintrag, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag,
	GostHalbjahr, GostJahrgang, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, ref, Ref, watch, WritableComputedRef } from "vue";
import { App } from "~/apps/BaseApp";
import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';
import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";
import { injectMainApp, Main } from "~/apps/Main";

const selected = useAuswahlViaRoute('gost')
const main: Main = injectMainApp();
const app = main.apps.gost;
const appJahrgaenge = main.apps.jahrgaenge;
const cols = ref([
	{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
	{ key: "abiturjahr", label: "Abiturjahr", sortable: true },
	{ key: "jahrgang", label: "Stufe", sortable: true }]);

const halbjahre = GostHalbjahr.values();
const hj_memo: Ref<GostHalbjahr | undefined> = ref(undefined);
const edit_blockungsname: Ref<boolean> = ref(false);
const selected_ergebnisse: Ref<GostBlockungsergebnisListeneintrag[]> = ref([]);

const rows: ComputedRef<GostJahrgang[]> =
	computed(() => {
		const list = [...app.auswahl.liste];
		return list.sort((a, b) =>
			(a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
	});

const manager: ComputedRef<GostBlockungsdatenManager | undefined> =
	computed(()=> app.dataKursblockung.datenmanager);
const rows_blockungswahl: ComputedRef<GostBlockungListeneintrag[]> =
	computed(() => app.blockungsauswahl.liste);
const rows_ergebnisse: ComputedRef<List<GostBlockungsergebnisListeneintrag>> =
	computed(() => manager.value?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>());
const abiturjahr: ComputedRef<number> =
	computed(() => Number(app.dataJahrgang.daten?.abiturjahr));
const jahrgaenge: ComputedRef<JahrgangsListeEintrag[]> =
	computed( () => {
		const set = new Set(app.auswahl.liste.map(r=>r.jahrgang))
		return appJahrgaenge.auswahl.liste.filter(j=>!set.has(j.kuerzel))
	});

watch(()=>selected.value, (new_jahrgang)=> {
	if (new_jahrgang?.abiturjahr && new_jahrgang.abiturjahr !== -1) {
		let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr( new_jahrgang.abiturjahr, App.akt_abschnitt.schuljahr, App.akt_abschnitt.abschnitt);
		// manchmal gibt es kein Planungshalbjahr, z.B. weil die Q2 fast durch ist oder der Abiturjahrgang noch in der Sek1 ist.
		if (!hj)
			if (new_jahrgang.abiturjahr < App.akt_abschnitt.schuljahr + App.akt_abschnitt.abschnitt)
				hj = GostHalbjahr.Q22;
			else
				hj = GostHalbjahr.EF1;
		selected_hj.value = hj;
	} else
		selected_hj.value = undefined;
})

const selected_hj: WritableComputedRef<GostHalbjahr | undefined > =
	computed({
		get(): GostHalbjahr | undefined {
			return hj_memo.value;
		},
		set(hj: GostHalbjahr | undefined) {
			if (hj === selected_hj.value)
				return;
			hj_memo.value = hj
			if (hj === undefined) {
				app.blockungsauswahl.ausgewaehlt = undefined;
				app.blockungsauswahl.liste = [];
			}
			else if ((selected.value?.abiturjahr) && (selected.value?.abiturjahr !== -1)) {
				app.blockungsauswahl.ausgewaehlt = undefined;
				app.blockungsauswahl.update_list(selected.value.abiturjahr, hj.id)
			}
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
		}});

const selected_ergebnis: WritableComputedRef<GostBlockungsergebnisListeneintrag | undefined> =
	computed({
		get(): GostBlockungsergebnisListeneintrag | undefined {
			return app.blockungsergebnisauswahl.ausgewaehlt;
		},
		set(value: GostBlockungsergebnisListeneintrag | undefined) {
			if (app.blockungsergebnisauswahl) {
				app.blockungsergebnisauswahl.ausgewaehlt = value;
			}
		}});

const pending_jahrgang: ComputedRef<boolean> =
	computed(()=> app.dataJahrgang.pending);

const pending: ComputedRef<boolean> =
	computed(()=> app.dataKursblockung.pending);

const allow_add_blockung = (row: GostHalbjahr): boolean => {
	const curr_hj = row.id === selected_hj.value?.id;
	if (!curr_hj || app.dataJahrgang.daten === undefined)
		return false;
	return app.dataJahrgang.daten.istBlockungFestgelegt[row.id] ? false : true
}

async function remove_ergebnisse() {
	if (!selected_hj.value)
		return;
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	if (selected_ergebnisse.value.length > 0 && abiturjahr) {
		for (const ergebnis of selected_ergebnisse.value) {
			await App.api.deleteGostBlockungsergebnis(App.schema, ergebnis.id);
		}
		selected_ergebnisse.value = [];
		await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value?.id);
	}
}

function select_hj(halbjahr: GostHalbjahr) {
	if (pending.value || app.blockungsauswahl.pending)
		return;
	const selection = halbjahre.find(hj=>hj.id===halbjahr.id);
	if (selection)
		selected_hj.value = selection;
}

function select_blockungauswahl(blockung: GostBlockungListeneintrag) {
	if (!pending.value)
		selected_blockungauswahl.value = blockung;
}

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

const create_blockungsergebnisse = () => {
	const halbjahresHashCode: number = app.blockungsauswahl.ausgewaehlt?.hashCode() ? app.blockungsauswahl.ausgewaehlt.hashCode() : -1;
	const id = app.blockungsauswahl.ausgewaehlt?.id;
	if (!id)
		return;
	const apiCall = app.create_blockungsergebnisse(id, halbjahresHashCode);
	main.config.apiLoadingStatus.addStatusByPromise(apiCall, {message: 'Blockung wird berechnet...', caller: 'Kursplanung (Gost)', categories: [GOST_CREATE_BLOCKUNG_SYMBOL]});
};

async function blockung_hinzufuegen() {
	if (!selected.value?.abiturjahr || !selected_hj.value)
		return;
	await App.api.createGostAbiturjahrgangBlockung(App.schema, selected.value.abiturjahr.valueOf(), selected_hj.value.id);
	const abiturjahr = selected.value.abiturjahr.valueOf();
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}

async function remove_blockung() {
	modal_remove_blockung.value.closeModal()
	if (!selected_blockungauswahl.value || !selected_hj.value)
		return;
	await App.api.deleteGostBlockung(App.schema, selected_blockungauswahl.value?.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf();
	if (!abiturjahr)
		return;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id);
}

async function remove_ergebnis() {
	if (!selected_ergebnis.value || !selected_hj.value)
		return;
	await App.api.deleteGostBlockungsergebnis(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id)
}

async function derive_blockung() {
	if (!selected_ergebnis.value || !selected_hj.value)
		return;
	await App.api.dupliziereGostBlockungMitErgebnis(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}

async function patch_blockung(name: string) {
	const res = await app.dataKursblockung.patch({name});
	if (res && selected_blockungauswahl.value)
		selected_blockungauswahl.value.name = name;
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

const modal_remove_blockung: Ref<any> = ref(null);
function toggle_remove_blockung_modal() {
	modal_remove_blockung.value.isOpen ? modal_remove_blockung.value.closeModal() : modal_remove_blockung.value.openModal();
};

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
<style lang="postcss" scoped>
.cell--bewertung span {
	@apply inline-block text-center text-black rounded font-normal;
	min-width: 5ex;
	padding: 0.05em 0.2em;
}

.vt-clicked .cell--bewertung span {
	filter: brightness(0.8) saturate(200%);
}
</style>
