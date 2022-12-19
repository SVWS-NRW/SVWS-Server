<template>
	<svws-ui-content-card :title="`Übersicht über die Blockung ${allow_regeln ? '/ Festlegung von Regeln':''}`">
		<div class="flex flex-row">
			<div class="flex-none sm:-mx-6 lg:-mx-8">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="w-full flex gap-2 justify-between mb-2">
						<div class="flex gap-2 content-start">
							<svws-ui-button size="small" type="primary" @click="toggle_modal_hochschreiben">Hochschreiben</svws-ui-button>
							<svws-ui-button  v-if="!blockung_aktiv" type="secondary" @click="toggle_modal_aktivieren">Aktivieren</svws-ui-button>
						</div>
						<div v-if="app.listAbiturjahrgangSchueler.filter.kurs && allow_regeln" class="flex gap-2 text-lg">
							<div class="flex flex-col justify-end">
								<div class="flex flex-row gap-2">
									Lehrkraft:
											<svws-ui-multi-select v-model="kurslehrer" class="w-52" autocomplete :item-filter="lehrer_filter" :items="main.apps.lehrer.auswahl.liste" :item-text="(l: LehrerListeEintrag)=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`"/>
											<svws-ui-icon class="cursor-pointer text-red-400" @click="remove_kurslehrer"><i-ri-delete-bin-2-line /></svws-ui-icon>
								</div>
							</div>
						</div>
					</div>
					<div v-if="blockungsergebnis_aktiv" class="text-lg font-bold">Dieses Blockungsergebnis ist aktiv.</div>
					<div v-if="blockung_aktiv && !blockungsergebnis_aktiv" class="text-lg font-bold">Ein anderes Ergebnis dieser Blockung ist bereits aktiv.</div>
					<div class="rounded-lg shadow">
						<table class="w-full  border-collapse text-sm table-auto">
							<!-- Wenn sticky angewendet wird, verschwinden die  border border-[#7f7f7f]/20 s...  -->
							<thead class="sticky top-0 bg-slate-100">
								<tr>
									<td colspan="3">
										Schiene
									</td>
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										<div class="flex gap-1">
											<template v-if="s === edit_schienenname">
												<svws-ui-text-input
													:modelValue="s.bezeichnung"
													focus headless
													style="width: 6rem"
													@blur="edit_schienenname=undefined"
													@keyup.enter="edit_schienenname=undefined"
													@keyup.escape="edit_schienenname=undefined"
													@update:modelValue="patch_schiene(s, $event)"
												/>
											</template>
											<template v-else>
												<span class="px-3 underline decoration-dashed underline-offset-2 cursor-text" @click="edit_schienenname = s">{{s.nummer}}</span>
											</template>
											<svws-ui-icon v-if="allow_del_schiene(s)" class="text-red-500 cursor-pointer" @click="del_schiene(s)"><i-ri-delete-bin-2-line/></svws-ui-icon>
										</div>
									</td>
									<template v-if="allow_regeln">
										<td class="bg-[#329cd5] rounded-l-none rounded-lg border-none cursor-pointer" rowspan="4" @click="add_schiene">
											<div class="px-2" >+</div></td><td rowspan="4" class="bg-white">
										</td>
									</template>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20" colspan="3">
										Schülerzahl
									</td>
									<!-- Schülerzahlen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										{{ getAnzahlSchuelerSchiene(s.id) }}
									</td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20" colspan="3">
										Kollisionen
									</td>
									<!-- Kollisionen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										{{ getAnzahlKollisionenSchiene(s.id) }}
									</td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20 text-center cursor-pointer" @click="sort_by = sort_by === 'kursart'? 'fach_id':'kursart'">
										<div class="flex gap-1">Kurs (Lehrer)<svws-ui-icon><i-ri-arrow-up-down-line /></svws-ui-icon></div></td>
									<td class="border border-[#7f7f7f]/20 text-center">Koop</td>
									<td class="border border-[#7f7f7f]/20 text-center">Diff</td>
									<!--Schienen-->
									<s-drag-schiene
										v-if="allow_regeln"
										v-for="s in schienen"
										:key="s.id"
										:schiene="s"
									/>
									<td v-else :colspan="schienen.size()" class="text-center">Regeln können nicht in Ergebnissen erstellt werden</td>
								</tr>
							</thead>
							<tbody>
								<s-kurs-blockung
									v-for="kurs in sorted_kurse"
									:key="kurs.id"
									:kurs="kurs"
								></s-kurs-blockung>
								<template v-if="allow_regeln">
									<tr>
										<td class="border border-[#7f7f7f]/20 text-center bg-white" :colspan="schienen.size()+4">Fachwahlen ohne Kurse</td>
										<td class="bg-white"></td>
									</tr>
									<s-fach-kurs
										v-for="fach in faecher"
										:key="fach.id"
										:fach="fach"
										:halbjahr="app.blockungsauswahl.ausgewaehlt?.gostHalbjahr || 0"
									/>
								</template>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-modal ref="modal_aktivieren" size="small">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Soll das Blockungsergebnis aktiviert werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_modal_aktivieren">Abbrechen</svws-ui-button>
				<svws-ui-button @click="activate_ergebnis">Ja</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="modal_hochschreiben" size="small">
		<template #modalTitle>Blockungsergebnis hochschreiben</template>
		<template #modalContent>
			<p>Soll das Blockungsergebnis in das nächste Halbjahr ({{app.dataKursblockung.datenmanager?.getHalbjahr().next()?.kuerzel}}) hochgeschrieben werden?</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="toggle_modal_hochschreiben">Abbrechen</svws-ui-button>
			<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungKursLehrer,
		GostBlockungSchiene,
		GostBlockungsergebnisManager,
		GostStatistikFachwahl,
		LehrerListeEintrag,
		List,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main, mainApp } from "~/apps/Main";
	import { lehrer_filter } from "~/helfer"
import { Lehrer } from "~/apps/lehrer/Lehrer";

	const main: Main = injectMainApp();
	const app = main.apps.gost

	const edit_schienenname: Ref<GostBlockungSchiene|undefined> = ref()
	const new_kurs_lehrer: Ref<boolean> = ref(false);

	const sort_by: WritableComputedRef<UserConfigKeys['gost.kursansicht.sortierung']> =
		computed({
			get(): UserConfigKeys['gost.kursansicht.sortierung'] {
				return main.config.user_config.get('gost.kursansicht.sortierung')
					|| 'kursart'
			},
			set(value: UserConfigKeys['gost.kursansicht.sortierung']) {
				if (value === undefined)
					value = 'kursart'
				App.api.setClientConfigUserKey(value, App.schema, 'SVWS-Client', 'gost.kursansicht.sortierung')
				mainApp.config.user_config.set('gost.kursansicht.sortierung', value)
			}
		});

	const faecher: ComputedRef<List<GostStatistikFachwahl>> =
		computed(() => app.dataFachwahlen.daten || new Vector<GostStatistikFachwahl>());

	const sorted_kurse: ComputedRef<List<GostBlockungKurs>> =
		computed(() => {
			if (app.dataKursblockung.datenmanager === undefined)
				return new Vector<GostBlockungKurs>();
			if (sort_by.value === 'kursart')
				return app.dataKursblockung.datenmanager.getKursmengeSortiertNachKursartFachNummer()
			else return app.dataKursblockung.datenmanager.getKursmengeSortiertNachFachKursartNummer()
		})

	const schienen: ComputedRef<List<GostBlockungSchiene>> =
		computed(() => app.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>());

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const allow_regeln: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.liste.length === 1);

	const blockung_aktiv: ComputedRef<boolean> =
		computed(()=> app.blockungsauswahl.ausgewaehlt?.istAktiv || false)

	const blockungsergebnis_aktiv: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.ausgewaehlt?.istVorlage || false)

	const kurslehrer: WritableComputedRef<LehrerListeEintrag|undefined> =
		computed({
			get(): LehrerListeEintrag | undefined {
				if (!app.dataKursblockung.datenmanager || !app.listAbiturjahrgangSchueler.filter.kurs)
					return;
				const liste = app.dataKursblockung.datenmanager.getOfKursLehrkraefteSortiert(app.listAbiturjahrgangSchueler.filter.kurs.id);
				if (liste.size()) {
					const lehrer = liste.get(0);
					return App.apps.lehrer.auswahl.liste.find(l=>l.id === lehrer.id);
				}
				else
					return;

			},
			set(value: LehrerListeEintrag | undefined) {
				if (!app.listAbiturjahrgangSchueler.filter.kurs)
					return;
				if (value !== undefined) {
					app.dataKursblockung.add_blockung_lehrer(app.listAbiturjahrgangSchueler.filter.kurs.id, value.id)
						.then(lehrer => {
							if (!lehrer || !app.dataKursblockung.datenmanager || !app.listAbiturjahrgangSchueler.filter.kurs)
								throw new Error("Fehler beim Anlegen des Kurslehrers");
							app.dataKursblockung.datenmanager.patchOfKursAddLehrkraft(app.listAbiturjahrgangSchueler.filter.kurs.id, lehrer);
						})

				}
			}
		})
	

	function remove_kurslehrer() {
		if (!app.dataKursblockung.datenmanager || !app.listAbiturjahrgangSchueler.filter.kurs || !kurslehrer.value)
			return;
		app.dataKursblockung.del_blockung_lehrer(app.listAbiturjahrgangSchueler.filter.kurs.id, kurslehrer.value.id);
		app.dataKursblockung.datenmanager.patchOfKursRemoveLehrkraft(app.listAbiturjahrgangSchueler.filter.kurs.id, kurslehrer.value.id);
	}
	
	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchueler(idSchiene) || 0;
	};

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		return app.dataKursblockung.datenmanager?.removeSchieneAllowed(schiene.id)
						&& manager.value?.getOfSchieneRemoveAllowed(schiene.id)
			? true
			:false
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) || 0;
	};

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: String) {
		schiene.bezeichnung = bezeichnung;
		await app.dataKursblockung.patch_schiene(schiene);
	}

	async function add_schiene() {
		await app.dataKursblockung.add_blockung_schiene()
	}

	async function del_schiene(s: GostBlockungSchiene) {
		await app.dataKursblockung.del_blockung_schiene(s)
	}

	const modal_aktivieren: Ref<any> = ref(null);
	function toggle_modal_aktivieren() {
		modal_aktivieren.value.isOpen ? modal_aktivieren.value.closeModal() : modal_aktivieren.value.openModal();
	};

	const modal_hochschreiben: Ref<any> = ref(null);
	function toggle_modal_hochschreiben() {
		modal_hochschreiben.value.isOpen ? modal_hochschreiben.value.closeModal() : modal_hochschreiben.value.openModal();
	};

	async function activate_ergebnis() {
		modal_aktivieren.value.closeModal();
		const selected_hj = app.dataKursblockung.datenmanager?.getHalbjahr();
		if (!app.blockungsauswahl.ausgewaehlt || !app.blockungsergebnisauswahl.ausgewaehlt || !app.dataJahrgang.daten || !selected_hj)
			return;
		const res = await app.dataKursblockungsergebnis.activate_blockungsergebnis();
		if (!res)
			return;
		app.dataJahrgang.daten.istBlockungFestgelegt[selected_hj.id] = true;
		app.blockungsauswahl.ausgewaehlt.istAktiv = true;
		app.blockungsergebnisauswahl.ausgewaehlt.istVorlage = true;
	}

	async function hochschreiben_ergebnis() {
		modal_hochschreiben.value.closeModal();
		const selected_hj = app.dataKursblockung.datenmanager?.getHalbjahr();
		if (!app.blockungsergebnisauswahl.ausgewaehlt || !app.dataJahrgang.daten || !selected_hj)
		return;
		const abiturjahr = app.auswahl.ausgewaehlt?.abiturjahr || -1
		await App.api.schreibeGostBlockungsErgebnisHoch(App.schema, app.blockungsergebnisauswahl.ausgewaehlt.id);
		await app.blockungsauswahl.update_list(abiturjahr, selected_hj.next()?.id || selected_hj.id, true);
	}

</script>
