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
			<div v-if="main.config.kursblockung_aktiv.size && abiturjahr" class="mt-20">
				<h3 class="text-headline px-6 mb-3">Blockungen</h3>
				<svws-ui-table v-model="selected_hj" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :data="halbjahre" class="mb-10">
					<template #body="{rows}">
						<template v-for="row in <GostHalbjahr[]>rows" :key="row.id">
							<tr :class="{'vt-clicked': row.id === selected_hj.id}" @click="select_hj(row)">
								<td>
									{{row.kuerzel}}
									<span v-if="((pending || app.blockungsauswahl.pending) && row.id === selected_hj.id)" class="loading-spinner-dimensions">
										<img src="/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation" ></span>
									<svws-ui-button type="transparent" v-if="allow_add_blockung(row)" @click.stop="blockung_hinzufuegen" :disabled="pending">Blockung hinzufügen</svws-ui-button>
								</td>
							</tr>
							<template v-if="row.id === selected_hj.id" v-for="blockung in rows_blockungswahl" :key="blockung.hashCode">
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
										<div v-if="allow_add_blockung(row)" class="flex items-center gap-1">
											<svws-ui-button size="small" type="secondary" @click.stop="create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="pending">Berechnen</svws-ui-button >
											<svws-ui-button size="small" type="danger" @click.stop="toggle_remove_blockung_modal" title="Blockung löschen" :disabled="pending">
												<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
											</svws-ui-button>
										</div>
									</td>
									<td v-else>
										<span>{{blockung.name}}</span>
									</td>
								</tr>
								<auswahl-blockung-api-status :blockung="blockung"/>
							</template>
						</template>
					</template>
				</svws-ui-table>
				<svws-ui-table
					v-model="selected_ergebnis"
					:columns="[{ key: 'id', label: 'ID'}, { key: 'bewertung', label: 'Bewertungen', span: '15'}]"
					:data="rows_ergebnisse.toArray()"
				>
					<template #cell-bewertung="{ row }: {row: GostBlockungsergebnisListeneintrag}">
						<span class="flex gap-1 font-semibold" >
							<span :style="{'background-color': color1(row)}">{{manager?.getOfBewertung1Wert(row.id)}}</span>
							<span :style="{'background-color': color2(row)}">{{manager?.getOfBewertung2Wert(row.id)}}</span>
							<span :style="{'background-color': color3(row)}">{{manager?.getOfBewertung3Wert(row.id)}}</span>
							<span :style="{'background-color': color4(row)}">{{manager?.getOfBewertung4Wert(row.id)}}</span>
						</span>
						<svws-ui-icon v-if="row.istVorlage" > <i-ri-pushpin-fill /></svws-ui-icon>
						<div v-if="(row.id === selected_ergebnis?.id && !selected_blockungauswahl?.istAktiv)" class="flex gap-2">
							<svws-ui-button v-if="selected_hj !== GostHalbjahr.Q22" size="small" type="secondary" class="cursor-pointer" @click.stop="toggle_modal_hochschreiben" :disabled="pending"> Hochschreiben </svws-ui-button>
							<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="toggle_modal_aktivieren" :disabled="pending"> Aktivieren </svws-ui-button>
							<svws-ui-button size="small" type="secondary" class="cursor-pointer" @click.stop="derive_blockung" :disabled="pending"> Ableiten </svws-ui-button>
							<svws-ui-button v-if="rows_ergebnisse.size() > 1" size="small" type="danger" class="cursor-pointer" @click.stop="remove_ergebnis" :disabled="pending">
								<svws-ui-icon><i-ri-delete-bin-2-line/></svws-ui-icon>
							</svws-ui-button>
						</div>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal ref="modal_aktivieren" size="small">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Soll das Blockungsergebnis aktiviert werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_modal_aktivieren()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="activate_ergebnis">Ja</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="modal_hochschreiben" size="small">
		<template #modalTitle>Blockungsergebnis hochschreiben</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Soll das Blockungsergebnis in das nächste Halbjahr ({{selected_hj.next()?.kuerzel}}) hochgeschrieben werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_modal_hochschreiben()">Abbrechen</svws-ui-button>
				<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
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
import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
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
const hj_memo: Ref<GostHalbjahr | undefined> = ref(undefined)
const edit_blockungsname: Ref<boolean> = ref(false)

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

const selected_hj: WritableComputedRef<GostHalbjahr> =
	computed({
		get(): GostHalbjahr {
			manager.value?.getHalbjahr()
			const hj = hj_memo.value || manager.value?.getHalbjahr()
			return hj
				|| GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr.value, App.akt_abschnitt.schuljahr, App.akt_abschnitt.abschnitt)
				|| GostHalbjahr.EF1
		},
		set(hj: GostHalbjahr) {
			if (hj === selected_hj.value || !hj) 
				return;
			hj_memo.value = hj
			if ((selected.value?.abiturjahr) && (selected.value?.abiturjahr !== -1)) {
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
	const curr_hj = row.id === selected_hj.value.id;
	if (!curr_hj || app.dataJahrgang.daten === undefined)
		return false;
	return app.dataJahrgang.daten.istBlockungFestgelegt[row.id] ? false : true
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
	if (!selected.value?.abiturjahr)
		return;
	await App.api.createGostAbiturjahrgangBlockung(App.schema, selected.value.abiturjahr.valueOf(), selected_hj.value.id);
	const abiturjahr = selected.value.abiturjahr.valueOf();
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}

async function remove_blockung() {
	modal_remove_blockung.value.closeModal()
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

async function activate_ergebnis() {
	modal_aktivieren.value.closeModal();
	if (!selected_ergebnis.value || !app.dataJahrgang.daten)
		return;
	await App.api.activateGostBlockungsergebnis(App.schema, selected_ergebnis.value.id);
	app.dataJahrgang.daten.istBlockungFestgelegt[selected_hj.value.id] = true;
}

async function derive_blockung() {
	if (!selected_ergebnis.value)
		return;
	await App.api.dupliziereGostBlockungMitErgebnis(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.id, true);
}

async function hochschreiben_ergebnis() {
	modal_hochschreiben.value.closeModal();
	if (!selected_ergebnis.value || !app.dataJahrgang.daten)
		return;
	await App.api.schreibeGostBlockungsErgebnisHoch(App.schema, selected_ergebnis.value.id);
	const abiturjahr = selected.value?.abiturjahr?.valueOf() || -1;
	await app.blockungsauswahl.update_list(abiturjahr, selected_hj.value.next()?.id || selected_hj.value.id, true);
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

const modal_aktivieren: Ref<any> = ref(null);
function toggle_modal_aktivieren() {
	modal_aktivieren.value.isOpen ? modal_aktivieren.value.closeModal() : modal_aktivieren.value.openModal();
};

const modal_hochschreiben: Ref<any> = ref(null);
function toggle_modal_hochschreiben() {
	modal_hochschreiben.value.isOpen ? modal_hochschreiben.value.closeModal() : modal_hochschreiben.value.openModal();
};

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
