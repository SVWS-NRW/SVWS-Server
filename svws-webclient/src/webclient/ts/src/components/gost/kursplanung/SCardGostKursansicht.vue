<template>
	<svws-ui-content-card style="flex: 1 0 40%;">
		<div class="flex flex-wrap justify-between mb-4">
			<h3 class="text-headline">{{ blockung.daten?.name ? blockung?.daten?.name : 'Blockungsübersicht' }}</h3>
			<div class="flex items-center gap-2">
				<svws-ui-button v-if="!blockung_aktiv" type="secondary" @click="toggle_modal_aktivieren">Aktivieren</svws-ui-button>
				<svws-ui-button type="primary" @click="toggle_modal_hochschreiben">Hochschreiben</svws-ui-button>
			</div>
		</div>
		<div v-if="blockungsergebnis_aktiv" class="text-lg font-bold">Dieses Blockungsergebnis ist aktiv.</div>
		<div v-if="blockung_aktiv && !blockungsergebnis_aktiv" class="text-lg font-bold">Ein anderes Ergebnis dieser Blockung ist bereits aktiv.</div>
		<div class="v-table--container">
			<table class="v-table--complex table--highlight-rows table-auto w-full">
				<thead>
					<tr>
						<th colspan="5">
							Schiene
						</th>
						<th v-for="s in schienen" :key="s.id" class="text-center">
							<div v-if="allow_regeln" class="flex justify-center">
								<template v-if="s === edit_schienenname">
									<svws-ui-text-input :model-value="s.bezeichnung.toString()" focus headless style="width: 6rem"
										@blur="edit_schienenname=undefined"
										@keyup.enter="edit_schienenname=undefined"
										@keyup.escape="edit_schienenname=undefined"
										@update:model-value="patch_schiene(s, $event.toString())" />
								</template>
								<template v-else>
									<span class="px-3 underline decoration-dotted underline-offset-2 cursor-text" title="Namen bearbeiten" @click="edit_schienenname = s">{{ s.nummer }}</span>
								</template>
								<svws-ui-icon v-if="allow_del_schiene(s)" class="text-red-500 cursor-pointer" @click="del_schiene(s)"><i-ri-delete-bin-2-line /></svws-ui-icon>
							</div>
							<template v-else>{{ s.nummer }}</template>
						</th>
						<template v-if="allow_regeln">
							<th rowspan="4" @click="add_schiene" title="Schiene hinzufügen" class="p-2">
								<div class="p-2 cursor-pointer rounded bg-primary text-white">+</div>
							</th>
							<th rowspan="4" class="hidden" />
						</template>
					</tr>
					<tr>
						<th colspan="5">
							Schülerzahl
						</th>
						<!-- Schülerzahlen -->
						<th v-for="s in schienen" :key="s.id" class="text-center">
							{{ getAnzahlSchuelerSchiene(s.id) }}
						</th>
					</tr>
					<tr>
						<th colspan="5">
							Kollisionen
						</th>
						<!-- Kollisionen -->
						<th v-for="s in schienen" :key="s.id" class="text-center">
							{{ getAnzahlKollisionenSchiene(s.id) }}
						</th>
					</tr>
					<tr>
						<th class="text-center cursor-pointer" @click="sort_by = sort_by === 'kursart'? 'fach_id':'kursart'">
							<div class="flex gap-1">Kurs<svws-ui-icon><i-ri-arrow-up-down-line /></svws-ui-icon></div>
						</th>
						<th>Lehrer</th>
						<th class="text-center">Koop</th>
						<th class="text-center">FW</th>
						<th class="text-center">Diff</th>
						<!--Schienen-->
						<!--eslint-disable-next-line vue/no-use-v-if-with-v-for-->
						<s-drag-schiene v-if="allow_regeln" v-for="s in schienen" :key="s.id" :schiene="s" :blockung="blockung" />
						<th v-else :colspan="schienen.size()" class="text-center">Regeln können nicht in Ergebnissen erstellt werden</th>
					</tr>
				</thead>
				<tbody>
					<template v-if="sort_by==='fach_id'">
						<template v-for="fach in fachwahlen" :key="fach.id">
							<template v-for="kursart in GostKursart.values()" :key="kursart.id">
								<s-fach-kurs :fach="fach" :kursart="kursart" :data-faecher="dataFaecher" :halbjahr="halbjahr.id" :blockung="blockung" :ergebnis="ergebnis"
									:list-lehrer="listLehrer" :map-lehrer="mapLehrer" :allow-regeln="allow_regeln" />
							</template>
						</template>
					</template>
					<template v-else>
						<template v-for="kursart in GostKursart.values()" :key="kursart.id">
							<template v-for="fach in fachwahlen" :key="fach.id">
								<s-fach-kurs :fach="fach" :kursart="kursart" :data-faecher="dataFaecher" :halbjahr="halbjahr.id" :blockung="blockung" :ergebnis="ergebnis"
									:list-lehrer="listLehrer" :map-lehrer="mapLehrer" :allow-regeln="allow_regeln" />
							</template>
						</template>
					</template>
				</tbody>
			</table>
		</div>
	</svws-ui-content-card>
	<div class="hidden">
	<svws-ui-modal ref="modal_aktivieren" size="small">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			Soll {{ blockung.daten?.name ? blockung.daten?.name : 'das Blockungsergebnis' }} aktiviert werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="toggle_modal_aktivieren">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="modal_hochschreiben" size="small">
		<template #modalTitle>Blockungsergebnis hochschreiben</template>
		<template #modalContent>
			<p>Soll das Blockungsergebnis in das nächste Halbjahr ({{ blockung.datenmanager?.getHalbjahr().next()?.kuerzel }}) hochgeschrieben werden?</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="toggle_modal_hochschreiben">Abbrechen</svws-ui-button>
			<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal></div>
</template>

<script setup lang="ts">

	import { GostBlockungSchiene, GostBlockungsergebnisManager, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, Vector, GostKursart } from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main, mainApp } from "~/apps/Main";
	import { SvwsUiContentCard, SvwsUiButton, SvwsUiTextInput, SvwsUiIcon, SvwsUiModal } from "@svws-nrw/svws-ui";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";
	import { router } from "~/router";
	import { routeGostKursplanungHalbjahr } from "~/router/apps/gost/kursplanung/RouteGostKursplanungHalbjahr";
	import type { UserConfigKeys } from "~/utils/userconfig/keys"

	const props = defineProps<{
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: GostHalbjahr;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

	const main: Main = injectMainApp();

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
				void App.api.setClientConfigUserKey(value, App.schema, 'SVWS-Client', 'gost.kursansicht.sortierung')
				mainApp.config.user_config.set('gost.kursansicht.sortierung', value)
			}
		});

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() =>
		props.blockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>()
	);

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const allow_regeln: ComputedRef<boolean> = computed(() =>
		(props.blockung.datenmanager !== undefined) && (props.blockung.datenmanager.getErgebnisseSortiertNachBewertung().size() === 1)
	);

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.blockung.daten?.istAktiv || false)

	const blockungsergebnis_aktiv: ComputedRef<boolean> = computed(() => props.ergebnis.daten?.istVorlage || false)

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchueler(idSchiene) || 0;
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		if ((props.blockung.datenmanager === undefined) || (manager.value === undefined))
			return false;
		return props.blockung.datenmanager.removeSchieneAllowed(schiene.id) && manager.value.getOfSchieneRemoveAllowed(schiene.id);
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) || 0;
	}

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: String) {
		schiene.bezeichnung = bezeichnung;
		await props.blockung.patch_schiene(schiene);
	}

	async function add_schiene() {
		await props.blockung.add_blockung_schiene();
	}

	async function del_schiene(s: GostBlockungSchiene) {
		await props.blockung.del_blockung_schiene(s);
	}

	const modal_aktivieren: Ref<any> = ref(null);
	function toggle_modal_aktivieren() {
		modal_aktivieren.value.isOpen ? modal_aktivieren.value.closeModal() : modal_aktivieren.value.openModal();
	}

	const modal_hochschreiben: Ref<any> = ref(null);
	function toggle_modal_hochschreiben() {
		modal_hochschreiben.value.isOpen ? modal_hochschreiben.value.closeModal() : modal_hochschreiben.value.openModal();
	}

	async function activate_ergebnis() {
		modal_aktivieren.value.closeModal();
		if (props.jahrgangsdaten.daten === undefined)
			throw new Error("Unerwarteter Fehler: Daten zum Abiturjahrgang nicht vorhanden.");
		if ((props.listBlockungen.ausgewaehlt === undefined) || (props.blockung.daten === undefined))
			throw new Error("Unerwarteter Fehler: Aktuell ist keine Blockung ausgewählt.");
		if (props.blockung.ergebnismanager === undefined)
			throw new Error("Unerwarteter Fehler: Aktuell ist kein Ergebnismanager vorhanden.");
		if (props.ergebnis.daten === undefined)
			throw new Error("Unerwarteter Fehler: Aktuell ist kein Ergebnis ausgewählt.");
		const res = await props.ergebnis.activate_blockungsergebnis();
		if (!res)
			return;
		props.jahrgangsdaten.daten.istBlockungFestgelegt[props.halbjahr.id] = true;
		props.listBlockungen.ausgewaehlt.istAktiv = true;
		props.blockung.daten.istAktiv = true;
		props.blockung.ergebnismanager.getErgebnis().istVorlage = true;
		props.ergebnis.daten.istVorlage = true;
	}

	async function hochschreiben_ergebnis() {
		modal_hochschreiben.value.closeModal();
		if (!props.ergebnis.daten)
			throw new Error("Unerwarteter Fehler: Aktuell ist kein Ergebnis ausgewählt.");
		const abiturjahr = props.blockung.daten?.abijahrgang;
		if (abiturjahr === undefined)
			throw new Error("Unerwarteter Fehler: Kein gültiger Abiturjahrgang ausgewählt.");
		const result = await App.api.schreibeGostBlockungsErgebnisHoch(App.schema, props.ergebnis.daten.id);
		await 	router.push({ name: routeGostKursplanungHalbjahr.name, params: { abiturjahr: abiturjahr, halbjahr: props.halbjahr.next()?.id || props.halbjahr.id, idblockung: result.id } });
	}

</script>
