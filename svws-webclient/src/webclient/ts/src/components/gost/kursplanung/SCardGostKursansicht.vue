<template>
	<svws-ui-content-card>
		<div class="flex flex-wrap justify-between">
			<h3 class="text-headline opacity-50">Übersicht {{app?.dataKursblockung?.daten?.name ? app?.dataKursblockung?.daten?.name : 'Blockungsübersicht'}}</h3>
			<div class="flex items-center gap-2">
				<svws-ui-button v-if="!blockung_aktiv" type="secondary" @click="toggle_modal_aktivieren">Aktivieren</svws-ui-button>
				<svws-ui-button type="primary" @click="toggle_modal_hochschreiben">Hochschreiben</svws-ui-button>
			</div>
		</div>
		<div class="flex flex-row">
			<div class="flex-none">
				<div>
					<div class="w-full flex gap-2 justify-between mb-2">
						<div class="flex gap-2 content-start">
						</div>
					</div>
					<div v-if="blockungsergebnis_aktiv" class="text-lg font-bold">Dieses Blockungsergebnis ist aktiv.</div>
					<div v-if="blockung_aktiv && !blockungsergebnis_aktiv" class="text-lg font-bold">Ein anderes Ergebnis dieser Blockung ist bereits aktiv.</div>
					<table class="v-table--complex table--highlight-rows table-auto w-full border-collapse">
						<thead class="sticky top-0">
							<tr>
								<th colspan="5">
									Schiene </th>
								<th v-for="s in schienen" :key="s.id" class="text-center" >
									<div v-if="allow_regeln" class="flex justify-center">
										<template v-if="s === edit_schienenname">
											<svws-ui-text-input :modelValue="s.bezeichnung.toString()" focus headless style="width: 6rem"
												@blur="edit_schienenname=undefined"
												@keyup.enter="edit_schienenname=undefined"
												@keyup.escape="edit_schienenname=undefined"
												@update:modelValue="patch_schiene(s, $event.toString())" /> </template>
										<template v-else>
											<span class="px-3 underline decoration-dotted underline-offset-2 cursor-text" title="Namen bearbeiten" @click="edit_schienenname = s">{{s.nummer}}</span>
										</template>
										<svws-ui-icon v-if="allow_del_schiene(s)" class="text-red-500 cursor-pointer" @click="del_schiene(s)"><i-ri-delete-bin-2-line/></svws-ui-icon>
									</div>
									<template v-else>{{s.nummer}}</template>
								</th>
								<template v-if="allow_regeln">
									<th rowspan="4" @click="add_schiene" title="Schiene hinzufügen" class="p-2">
										<div class="p-2 cursor-pointer rounded bg-primary text-white">+</div></th>
									<th rowspan="4" class="hidden"/>
								</template>
							</tr>
							<tr>
								<th colspan="5">
									Schülerzahl </th>
								<!-- Schülerzahlen -->
								<th v-for="s in schienen" :key="s.id" class="text-center" >
									{{ getAnzahlSchuelerSchiene(s.id) }} </th>
							</tr>
							<tr>
								<th colspan="5">
									Kollisionen </th>
								<!-- Kollisionen -->
								<th v-for="s in schienen" :key="s.id" class="text-center" >
									{{ getAnzahlKollisionenSchiene(s.id) }} </th>
							</tr>
							<tr>
								<th class="text-center cursor-pointer" @click="sort_by = sort_by === 'kursart'? 'fach_id':'kursart'">
									<div class="flex gap-1">Kurs<svws-ui-icon><i-ri-arrow-up-down-line /></svws-ui-icon></div></th>
								<th>Lehrer</th>
								<th class="text-center">Koop</th>
								<th class="text-center">FW</th>
								<th class="text-center">Diff</th>
								<!--Schienen-->
								<s-drag-schiene v-if="allow_regeln" v-for="s in schienen" :key="s.id" :schiene="s" />
								<th v-else :colspan="schienen.size()" class="text-center">Regeln können nicht in Ergebnissen erstellt werden</th>
							</tr>
						</thead>
						<tbody>
							<s-fach-kurs v-for="fach in faecher" :key="fach.id" :fach="fach" :list-lehrer="listLehrer" :map-lehrer="mapLehrer"
								:halbjahr="app.blockungsauswahl.ausgewaehlt?.gostHalbjahr || 0"/>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-modal ref="modal_aktivieren" size="small">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Soll das Blockungsergebnis aktiviert werden? </div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_modal_aktivieren">Abbrechen</svws-ui-button>
				<svws-ui-button @click="activate_ergebnis">Ja</svws-ui-button> </div>
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
  import { GostBlockungSchiene, GostBlockungsergebnisManager, GostStatistikFachwahl, LehrerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main, mainApp } from "~/apps/Main";
	import { SvwsUiContentCard, SvwsUiButton, SvwsUiTextInput, SvwsUiIcon, SvwsUiModal } from "@svws-nrw/svws-ui";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";

	const { listLehrer, mapLehrer } = defineProps<{ 
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
	}>();

	const main: Main = injectMainApp();
	const app = main.apps.gost

	const edit_schienenname: Ref<GostBlockungSchiene|undefined> = ref()

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
