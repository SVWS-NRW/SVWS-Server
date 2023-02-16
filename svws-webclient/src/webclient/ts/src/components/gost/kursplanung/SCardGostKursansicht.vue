<template>
	<svws-ui-content-card style="flex: 1 0 40%; height: auto;">
		<div class="sticky h-4 lg:h-6 3xl:h-8 -mt-4 lg:-mt-6 3xl:-mt-8 -top-4 lg:-top-6 3xl:-top-8 bg-white z-10" />
		<div class="flex flex-wrap justify-between mb-4">
			<h3 class="text-headline">{{ blockungsname }}</h3>
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
									<svws-ui-text-input :model-value="s.bezeichnung" focus headless style="width: 6rem"
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
						<template v-if="allow_regeln">
							<s-gost-kursplanung-kursansicht-schiene-dragable v-for="s in schienen" :key="s.id" :schiene="s" :add-regel="addRegel" />
						</template>
						<th v-else :colspan="schienen.size()" class="text-center normal-case font-normal text-black/50">Regeln können nicht in Ergebnissen erstellt werden</th>
					</tr>
				</thead>
				<tbody>
					<template v-if="sort_by==='fach_id'">
						<template v-for="fach in fachwahlen" :key="fach.id">
							<template v-for="kursart in GostKursart.values()" :key="kursart.id">
								<s-gost-kursplanung-kursansicht-fachwahl :fach="fach" :kursart="kursart" :halbjahr="halbjahr.id"
									:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
									:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
									:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
									:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
									:remove-kurs-lehrer="removeKursLehrer" />
							</template>
						</template>
					</template>
					<template v-else>
						<template v-for="kursart in GostKursart.values()" :key="kursart.id">
							<template v-for="fach in fachwahlen" :key="fach.id">
								<s-gost-kursplanung-kursansicht-fachwahl :fach="fach" :kursart="kursart" :halbjahr="halbjahr.id"
									:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
									:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
									:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
									:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
									:remove-kurs-lehrer="removeKursLehrer" />
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
				Soll {{ blockungsname }} aktiviert werden?
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="toggle_modal_aktivieren">Abbrechen</svws-ui-button>
				<svws-ui-button type="primary" @click="activate_ergebnis">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
		<svws-ui-modal ref="modal_hochschreiben" size="small">
			<template #modalTitle>Blockungsergebnis hochschreiben</template>
			<template #modalContent>
				<p>Soll das Blockungsergebnis in das nächste Halbjahr ({{ getDatenmanager().getHalbjahr().next()?.kuerzel }}) hochgeschrieben werden?</p>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="toggle_modal_hochschreiben">Abbrechen</svws-ui-button>
				<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungSchiene, GostBlockungsergebnisManager, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, Vector,
		GostKursart, GostBlockungRegel, GostFaecherManager, GostBlockungKurs, GostBlockungKursLehrer, GostBlockungsdatenManager } from "@svws-nrw/svws-core-ts";
	import { routeLogin } from "~/router/RouteLogin";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { SvwsUiContentCard, SvwsUiButton, SvwsUiTextInput, SvwsUiIcon, SvwsUiModal } from "@svws-nrw/svws-ui";
	import type { UserConfigKeys } from "~/utils/userconfig/keys"
	import { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import { routeApp } from "~/router/RouteApp";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
		addSchiene: () => Promise<GostBlockungSchiene | undefined>;
		removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		ergebnisHochschreiben: () => Promise<void>;
		ergebnisAktivieren: () => Promise<boolean>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

	const edit_schienenname: Ref<GostBlockungSchiene|undefined> = ref()

	const sort_by: WritableComputedRef<UserConfigKeys['gost.kursansicht.sortierung']> =
		computed({
			get(): UserConfigKeys['gost.kursansicht.sortierung'] {
				return (routeApp.data.user_config.value.get('gost.kursansicht.sortierung')
					|| 'kursart') as UserConfigKeys['gost.kursansicht.sortierung'];
			},
			set(value: UserConfigKeys['gost.kursansicht.sortierung']) {
				if (value === undefined)
					value = 'kursart'
				void routeLogin.data.api.setClientConfigUserKey(value, routeLogin.data.schema, 'SVWS-Client', 'gost.kursansicht.sortierung')
				routeApp.data.user_config.value.set('gost.kursansicht.sortierung', value)
			}
		});

	const blockungsname: ComputedRef<string> = computed(() => props.getDatenmanager().daten().name);

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.getDatenmanager().getMengeOfSchienen());

	const allow_regeln: ComputedRef<boolean> = computed(() => (props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1));

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const blockungsergebnis_aktiv: ComputedRef<boolean> = computed(() => props.getErgebnismanager().getErgebnis().istVorlage || false);

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return props.getErgebnismanager().getOfSchieneAnzahlSchueler(idSchiene) || 0;
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		return props.getDatenmanager().removeSchieneAllowed(schiene.id) && props.getErgebnismanager().getOfSchieneRemoveAllowed(schiene.id);
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return props.getErgebnismanager().getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) || 0;
	}

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: string) {
		await props.patchSchiene({ bezeichnung: bezeichnung }, schiene.id);
	}

	async function add_schiene() {
		return await props.addSchiene();
	}

	async function del_schiene(schiene: GostBlockungSchiene) {
		return await props.removeSchiene(schiene);
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
		await props.ergebnisAktivieren();
	}

	async function hochschreiben_ergebnis() {
		modal_hochschreiben.value.closeModal();
		await props.ergebnisHochschreiben();
	}

</script>
