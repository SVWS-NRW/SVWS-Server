<template>
	<svws-ui-content-card class="h-full" overflow-scroll>
		<div class="flex flex-wrap justify-between mb-4">
			<h3 class="text-headline cursor-auto">
				<svws-ui-tooltip position="right" :indicator="(blockung_aktiv && !blockungsergebnis_aktiv) || blockungsergebnis_aktiv ? 'underline': false">
					{{ blockungsname }}
					<template #icon>
						<i-ri-error-warning-line v-if="blockung_aktiv && !blockungsergebnis_aktiv" class="ml-1 w-6 h-6" />
						<i-ri-pushpin-line v-if="blockungsergebnis_aktiv" class="ml-1 w-6 h-6" />
						<span v-else />
					</template>
					<template #content>
						<div v-if="blockungsergebnis_aktiv" class="text-lg font-bold">Dieses Blockungsergebnis ist aktiv.</div>
						<div v-if="blockung_aktiv && !blockungsergebnis_aktiv">Ein anderes Ergebnis dieser Blockung ist bereits aktiv.</div>
					</template>
				</svws-ui-tooltip>
			</h3>
			<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
				<svws-ui-sub-nav>
					<slot name="triggerRegeln" />
					<s-card-gost-kursansicht-blockung-aktivieren-modal :get-datenmanager="getDatenmanager" :ergebnis-aktivieren="ergebnisAktivieren" :blockungsname="blockungsname" v-slot="{ openModal }">
						<svws-ui-button :disabled="blockungsergebnis_aktiv || (blockung_aktiv && !blockungsergebnis_aktiv)" type="transparent" size="small" @click="openModal()">Aktivieren</svws-ui-button>
					</s-card-gost-kursansicht-blockung-aktivieren-modal>
					<s-card-gost-kursansicht-blockung-hochschreiben-modal :get-datenmanager="getDatenmanager" :ergebnis-hochschreiben="ergebnisHochschreiben" v-slot="{ openModal }">
						<svws-ui-button type="transparent" size="small" @click="openModal()">Hochschreiben</svws-ui-button>
					</s-card-gost-kursansicht-blockung-hochschreiben-modal>
				</svws-ui-sub-nav>
			</Teleport>
		</div>
		<svws-ui-data-table :items="GostKursart.values()"
			:columns="cols" disable-footer>
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-left" :class="allow_regeln ? 'col-span-6' : 'col-span-5'">
						<div class="flex items-center justify-between">
							<span>Schiene</span>
							<template v-if="allow_regeln">
								<svws-ui-button type="transparent" size="small" @click="add_schiene" title="Schiene hinzufügen" class="h-6 py-0 px-1">
									Hinzufügen <i-ri-add-circle-line />
								</svws-ui-button>
							</template>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center" v-for="s in schienen" :key="s.id">
						<div v-if="allow_regeln" class="flex justify-center text-center items-center w-auto">
							<template v-if="s.id === edit_schienenname">
								<svws-ui-text-input :model-value="s.bezeichnung" focus headless style="width: 6rem"
									@blur="edit_schienenname=undefined"
									@keyup.enter="edit_schienenname=undefined"
									@keyup.escape="edit_schienenname=undefined"
									@update:model-value="patch_schiene(s, $event.toString())" />
							</template>
							<template v-else>
								<span class="underline decoration-dotted underline-offset-2 cursor-text" title="Namen bearbeiten" @click="edit_schienenname = s.id">{{ s.nummer }}</span>
							</template>
							<svws-ui-icon v-if="allow_del_schiene(s)" class="opacity-25 hover:opacity-100 hover:text-error cursor-pointer -mr-2" @click="del_schiene(s)"><i-ri-delete-bin-line /></svws-ui-icon>
						</div>
						<template v-else>{{ s.nummer }}</template>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-left" :class="allow_regeln ? 'col-span-6' : 'col-span-5'">
						Schülerzahl
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center" v-for="s in schienen" :key="s.id">
						{{ getAnzahlSchuelerSchiene(s.id) }}
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-left" :class="allow_regeln ? 'col-span-6' : 'col-span-5'">
						Kollisionen
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center" v-for="s in schienen" :key="s.id">
						{{ getAnzahlKollisionenSchiene(s.id) }}
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th cursor-pointer" @click="sort_by = sort_by === 'kursart'? 'fach_id':'kursart'" :class="allow_regeln ? 'col-span-2' : 'col-span-1'">
						<div class="data-table__th-wrapper data-table__th-wrapper__sortable">
							<span class="data-table__th-title">Kurs</span>
							<span role="button"
								class="data-table__th-sorting"
								tabindex="-1">
								<span class="sorting-arrows sorting-arrows__column">
									<i-ri-arrow-up-down-line class="sorting-arrows__up"
										:class="{'sorting-arrows__active': sort_by === 'kursart'}" />
									<i-ri-arrow-up-down-line class="sorting-arrows__down"
										:class="{'sorting-arrows__active': sort_by === 'kursart'}" />
								</span>
							</span>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th">Lehrer</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center">Koop</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center">FW</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center">Diff</div>
					<!--Schienen-->
					<template v-if="allow_regeln">
						<s-gost-kursplanung-kursansicht-schiene-dragable v-for="s in schienen" :key="s.id" :schiene="s" :add-regel="addRegel" :drag-and-drop-data="dragAndDropData" @dnd="dragAndDropData=$event" />
					</template>
					<div v-else role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center normal-case font-normal text-black/50" :style="{'gridColumn': 'span ' + schienen.size()}">
						<span class="inline-flex items-center gap-1"><i-ri-information-line />Regeln können in diesem Ergebnis nicht erstellt werden.</span>
					</div>
				</div>
			</template>

			<template #body>
				<template v-if="sort_by==='fach_id'">
					<template v-for="fach in mapFachwahlStatistik.values()" :key="fach.id">
						<template v-for="kursart in GostKursart.values()" :key="kursart.id">
							<s-gost-kursplanung-kursansicht-fachwahl :config="config" :fach="fach" :kursart="kursart" :halbjahr="halbjahr.id"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
						</template>
					</template>
				</template>
				<template v-else>
					<template v-for="kursart in GostKursart.values()" :key="kursart.id">
						<template v-for="fach in mapFachwahlStatistik.values()" :key="fach.id">
							<s-gost-kursplanung-kursansicht-fachwahl :config="config" :fach="fach" :kursart="kursart" :halbjahr="halbjahr.id"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
						</template>
					</template>
				</template>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostFaecherManager, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List } from "@core";
	import { GostKursart } from "@core";
	import type { ComputedRef, Ref, WritableComputedRef } from "vue";
	import {computed, onMounted, ref} from "vue";
	import type { Config } from "~/components/Config";
	import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import type {DataTableColumn} from "@ui";

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
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		ergebnisHochschreiben: () => Promise<void>;
		ergebnisAktivieren: () => Promise<boolean>;
		config: Config;
		hatErgebnis: boolean;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapFachwahlStatistik: Map<number, GostStatistikFachwahl>;
	}>();

	const edit_schienenname: Ref<number|undefined> = ref()
	const dragAndDropData: Ref<{ schiene: GostBlockungSchiene | undefined, kurs?: undefined } | undefined> = ref(undefined);

	const sort_by: WritableComputedRef<string> = computed({
		get: () => props.config.getValue('gost.kursansicht.sortierung'),
		set: (value) => {
			if (value === undefined)
				value = 'kursart'
			void props.config.setValue('gost.kursansicht.sortierung', value);
		}
	});

	const blockungsname: ComputedRef<string> = computed(() => props.getDatenmanager().daten().name);

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.getDatenmanager().getMengeOfSchienen());

	const allow_regeln: ComputedRef<boolean> = computed(() => (props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1));

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const blockungsergebnis_aktiv: ComputedRef<boolean> = computed(() => props.hatErgebnis ? props.getErgebnismanager().getErgebnis().istVorlage : false);

	function calculateColumns(): DataTableColumn[] {
		const cols: Array<DataTableColumn> = [];

		if (allow_regeln.value) {
			cols.push({ key: "actions", label: "Actions", fixedWidth: 1.5, align: 'center' });
		}

		cols.push({ key: "kurs", label: "Kurs", span: 2, minWidth: 8 },
			{ key: "lehrer", label: "Lehrer", minWidth: 6 },
			{ key: "koop", label: "Kooperation", align: 'center' },
			{ key: "FW", label: "Fachwahl", align: 'center' },
			{ key: "Diff", label: "Diff", align: 'center' });

		for (let i = 0; i < schienen.value.size(); i++) {
			cols.push({ key: "schiene_" + (i+1), label: "schiene_" + (i+1), minWidth: 3.5, align: 'center' });
		}

		return cols;
	}

	const cols: ComputedRef<DataTableColumn[]> = computed(() => calculateColumns());

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchueler(idSchiene) : 0;
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		if (!props.hatErgebnis)
			return false;
		return props.getDatenmanager().removeSchieneAllowed(schiene.id) && props.getErgebnismanager().getOfSchieneRemoveAllowed(schiene.id);
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) : 0;
	}

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: string) {
		await props.patchSchiene({ bezeichnung: bezeichnung }, schiene.id);
	}

	async function add_schiene() {
		// TODO: Update cols value mit neuer Anzahl von Schienen
		return await props.addSchiene();
	}

	async function del_schiene(schiene: GostBlockungSchiene) {
		// TODO: Update cols value mit neuer Anzahl von Schienen
		return await props.removeSchiene(schiene);
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});
</script>
