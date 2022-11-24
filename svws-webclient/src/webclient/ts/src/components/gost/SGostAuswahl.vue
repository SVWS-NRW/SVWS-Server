<template>
	<svws-ui-secondary-menu>
		<template #headline> Abiturjahrgangsauswahl</template>
		<template #header></template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model="selected"
					:columns="cols"
					:data="rows"
					:footer="false"
				/>
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
				<svws-ui-table v-model="selected_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :data="halbjahre">
					<template #body="{rows}">
						<template v-for="row in rows" :key="row.id">
							<tr :class="{'vt-clicked': row.id === selected_hj.id}" @click="selected_hj = halbjahre.find(hj=>hj.id===row.id)!">
								<td>
									{{row.kuerzel}}
									<svws-ui-button
										v-if="row.id === selected_hj.id"
										size="small" type="primary" class="float-right cursor-pointer" @click="blockung_hinzufuegen"
										>Blockung hinzufügen</svws-ui-button>
								</td>
							</tr>
							<template v-if="row.id === selected_hj.id && !wait" v-for="blockung in rows_blockungsswahl" :key="blockung.id">
								<tr :class="{'vt-clicked': blockung === selected_blockungauswahl}" @click="selected_blockungauswahl = blockung">
									<td v-if=" blockung === selected_blockungauswahl ">
										<div class="float-left flex">
											<span v-if="!edit_blockungsname"
												class="px-4 underline decoration-dashed underline-offset-2 cursor-pointer"
												@click="edit_blockungsname = true"
											>{{blockung.name}}--{{blockung.id}}</span>
											<svws-ui-text-input v-else v-model="blockung.name"
												style="width: 10rem"
												headless focus
												@keyup.enter="edit_blockungsname=false"
												@input="patch_blockung(blockung)"/>
										</div>
										<div class="float-right flex gap-2">
											<svws-ui-button class="cursor-pointer" size="small" @click="create_blockung">Ergebnisse berechnen</svws-ui-button >
											<svws-ui-button size="small" type="danger" class="cursor-pointer" @click="remove_blockung">
												<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
											</svws-ui-button>
										</div>
									</td>
									<td v-else>
										<span class="px-4">{{blockung.name}}</span>
									</td>
								</tr>
								<auswahl-blockung-api-status :blockung="blockung"/>
							</template>
						</template>
					</template>
				</svws-ui-table>
				<svws-ui-table
					v-model="selected_ergebnis"
					:columns="[{ key: 'id', label: 'ID' }, { key: 'bewertung', label: 'Bewertungen'}]"
					:data="rows_ergebnisse.toArray()"
				>
					<template #cell-bewertung="{ row }">
						<div class="flex justify-between">
							<span class="inline-flex font-semibold" >
								<span :style="{'background-color': color1(row)}">{{manager?.getOfBewertung1Wert(row.id)}}</span>&nbsp
								<span :style="{'background-color': color2(row)}">{{manager?.getOfBewertung2Wert(row.id)}}</span>&nbsp
								<span :style="{'background-color': color3(row)}">{{manager?.getOfBewertung3Wert(row.id)}}</span>&nbsp
								<span :style="{'background-color': color4(row)}">{{manager?.getOfBewertung4Wert(row.id)}}</span>
							</span>
							<div v-if="row.id === selected_ergebnis?.id" class="flex gap-2">
								<svws-ui-button size="small" type="primary" class="cursor-pointer" @click="derive_blockung"> Ableiten </svws-ui-button>
								<svws-ui-button v-if="rows_ergebnisse.size() > 1" size="small" type="danger" class="cursor-pointer" @click="remove_ergebnis">
									<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
								</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-table>
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
	ref,
	Ref,
	WritableComputedRef
} from "vue";
import { App } from "~/apps/BaseApp";
import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';
import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";
import { injectMainApp, Main } from "~/apps/Main";

const selected = useAuswahlViaRoute('gost')
const main: Main = injectMainApp();
const app = main.apps.gost;
const appJahrgaenge = main.apps.jahrgaenge;
const cols = ref([
	{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	{ key: "abiturjahr", label: "Abiturjahr", sortable: true },
	{ key: "jahrgang", label: "Stufe", sortable: true }]);

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