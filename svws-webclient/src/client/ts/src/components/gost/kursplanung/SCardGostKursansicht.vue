<template>
	<svws-ui-content-card class="h-full table--with-background" overflow-scroll style="width: calc(100% + 2rem); padding-right: 2rem;">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<slot name="triggerRegeln" />
				<s-card-gost-kursansicht-blockung-aktivieren-modal :get-datenmanager="getDatenmanager" :ergebnis-aktivieren="ergebnisAktivieren" :blockungsname="blockungsname" v-slot="{ openModal }">
					<svws-ui-tooltip>
						<svws-ui-button :disabled="!aktivieren_moeglich" type="transparent" size="small" @click="openModal()">Aktivieren</svws-ui-button>
						<template #content>
							<span v-if="!existiertSchuljahresabschnitt"> Die Blockung kann nicht aktiviert werden, da noch kein Abschnitt für dieses Halbjahr angelegt ist. </span>
							<span v-if="bereits_aktiv"> Die Blockung kann nicht aktiviert werden, da bereits Kurse der gymnasialen Oberstufe für diesen Abschnitt angelegt sind. </span>
							<span v-else />
						</template>
					</svws-ui-tooltip>
				</s-card-gost-kursansicht-blockung-aktivieren-modal>
				<s-card-gost-kursansicht-blockung-hochschreiben-modal :get-datenmanager="getDatenmanager" :ergebnis-hochschreiben="ergebnisHochschreiben" v-slot="{ openModal }">
					<svws-ui-button type="transparent" size="small" @click="openModal()">Hochschreiben</svws-ui-button>
				</s-card-gost-kursansicht-blockung-hochschreiben-modal>
			</svws-ui-sub-nav>
		</Teleport>
		<svws-ui-data-table :items="GostKursart.values()" :columns="cols" disable-footer>
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
							<span v-if="allow_del_schiene(s)" class="icon opacity-25 hover:opacity-100 hover:text-error cursor-pointer -mr-2" @click="del_schiene(s)"><i-ri-delete-bin-line /></span>
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
						class="data-table__th data-table__thead__th data-table__th__align-center" v-for="s in schienen" :key="s.id" :class="{'text-error': getAnzahlKollisionenSchiene(s.id) > 0}">
						{{ getAnzahlKollisionenSchiene(s.id) }} <i-ri-alert-line v-if="getAnzahlKollisionenSchiene(s.id) > 0" />
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
					<svws-ui-table-cell thead align="center" tooltip="Kooperation">Koop</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" tooltip="Fachwahlen">FW</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" tooltip="Differenz">Diff</svws-ui-table-cell>
					<!--Schienen-->
					<template v-if="allow_regeln">
						<template v-for="schiene in schienen" :key="schiene.id">
							<div @dragover="if (istDropZoneSchiene(schiene).value) $event.preventDefault();" @drop="openModalRegelKursartSchiene(schiene)"
								class="data-table__th data-table__thead__th data-table__th__align-center text-black/25 hover:text-black relative group"
								:class="{ 'bg-primary/5 text-primary hover:text-primary': istDropZoneSchiene(schiene).value, }">
								<div :key="schiene.id" @click="openModalRegelKursartSchiene(schiene)"
									class="select-none cursor-grab text-center" :class="{'cursor-grabbing': dragDataKursartSchiene !== undefined}"
									:draggable="true" @dragstart="dragSchieneStarted(schiene)" @dragend="dragSchieneEnded">
									<span class="rounded w-3 absolute top-0 left-0">
										<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
									</span>
									<i-ri-lock-unlock-line class="inline-block" />
								</div>
							</div>
						</template>
					</template>
					<div v-else role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center normal-case font-normal text-black/50" :style="{'gridColumn': 'span ' + schienen.size()}">
						<span class="inline-flex items-center gap-1"><i-ri-information-line />Regeln können in diesem Ergebnis nicht erstellt werden.</span>
					</div>
				</div>
			</template>

			<template #body>
				<template v-if="sort_by==='fach_id'">
					<template v-for="fachwahlen in mapFachwahlStatistik.values()" :key="fachwahlen.id">
						<template v-for="kursart in GostKursart.values()" :key="kursart.id">
							<s-gost-kursplanung-kursansicht-fachwahl v-if="istFachwahlVorhanden(fachwahlen, kursart).value"
								:config="config" :fachwahlen="fachwahlen" :kursart="kursart"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:drag-data-kurs-schiene="() => dragDataKursSchiene" :on-drag-kurs-schiene="dragKursSchiene" :on-drop-kurs-schiene="dropKursSchiene" />
						</template>
					</template>
				</template>
				<template v-else>
					<template v-for="kursart in GostKursart.values()" :key="kursart.id">
						<template v-for="fachwahlen in mapFachwahlStatistik.values()" :key="fachwahlen.id">
							<s-gost-kursplanung-kursansicht-fachwahl v-if="istFachwahlVorhanden(fachwahlen, kursart).value"
								:config="config" :fachwahlen="fachwahlen" :kursart="kursart"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:drag-data-kurs-schiene="() => dragDataKursSchiene" :on-drag-kurs-schiene="dragKursSchiene" :on-drop-kurs-schiene="dropKursSchiene" />
						</template>
					</template>
				</template>
			</template>
		</svws-ui-data-table>
		<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" ref="modalRegelKursartSchienen" />
		<s-gost-kursplanung-kursansicht-modal-regel-kurse :get-datenmanager="getDatenmanager" :add-regel="addRegel" ref="modal_regel_kurse" />
		<s-gost-kursplanung-kursansicht-modal-combine-kurse :get-datenmanager="getDatenmanager" :combine-kurs="combineKurs" ref="modal_combine_kurse" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, Ref, WritableComputedRef } from "vue";
	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager,
		GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostFach, GostFaecherManager, GostHalbjahr, GostJahrgangsdaten,
		GostStatistikFachwahl, LehrerListeEintrag, List } from "@core";
	import { type SGostKursplanungKursansichtDragData } from "./kursansicht/SGostKursplanungKursansichtFachwahlProps";
	import type { Config } from "~/components/Config";
	import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import type { DataTableColumn } from "@ui";
	import { GostKursart, GostStatistikFachwahlHalbjahr, ZulaessigesFach } from "@core";
	import { computed, onMounted, ref } from "vue";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
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
		existiertSchuljahresabschnitt: boolean;
		config: Config;
		hatErgebnis: boolean;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapFachwahlStatistik: Map<number, GostStatistikFachwahl>;
	}>();

	const edit_schienenname: Ref<number|undefined> = ref()

	const sort_by: WritableComputedRef<string> = computed({
		get: () => props.config.getValue('gost.kursansicht.sortierung'),
		set: (value) => {
			if (value === undefined)
				value = 'kursart'
			void props.config.setValue('gost.kursansicht.sortierung', value);
		}
	});

	const istFachwahlVorhanden = (fachwahlen: GostStatistikFachwahl, kursart: GostKursart) : ComputedRef<boolean> => computed(() => {
		const anzahl = props.getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id).size();
		return (anzahl > 0) || (allow_regeln.value && getAnzahlFachwahlen(fachwahlen, kursart) > 0);
	});

	const blockungsname: ComputedRef<string> = computed(() => props.getDatenmanager().daten().name);

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.getDatenmanager().schieneGetListe());

	const allow_regeln: ComputedRef<boolean> = computed(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const blockungsergebnis_aktiv: ComputedRef<boolean> = computed(() => props.hatErgebnis ? props.getErgebnismanager().getErgebnis().istVorlage : false);

	const bereits_aktiv: ComputedRef<boolean> = computed(() => props.jahrgangsdaten.istBlockungFestgelegt[props.halbjahr.id] || blockungsergebnis_aktiv.value || blockung_aktiv.value);

	const aktivieren_moeglich : ComputedRef<boolean> = computed(() => props.existiertSchuljahresabschnitt && !bereits_aktiv.value);

	function calculateColumns() {
		const cols: DataTableColumn[] = [];

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

	const cols = computed(() => calculateColumns());

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchueler(idSchiene) : 0;
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		if (!props.hatErgebnis)
			return false;
		return props.getDatenmanager().schieneGetIsRemoveAllowed(schiene.id) && props.getErgebnismanager().getOfSchieneRemoveAllowed(schiene.id);
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

	function getAnzahlFachwahlen(fachwahlen: GostStatistikFachwahl, kursart: GostKursart) : number {
		const fach_halbjahr : GostStatistikFachwahlHalbjahr = fachwahlen.fachwahlen[props.halbjahr.id] || new GostStatistikFachwahlHalbjahr();
		const gostfach : GostFach | null = props.faecherManager.get(fachwahlen.id);
		if (gostfach === null)
			return 0;
		const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
		switch (kursart) {
			case GostKursart.LK: return fach_halbjahr.wahlenLK;
			case GostKursart.GK: return (zulFach === ZulaessigesFach.PX) || (zulFach === ZulaessigesFach.VX) ? 0 : fach_halbjahr.wahlenGK;
			case GostKursart.ZK: return fach_halbjahr.wahlenZK;
			case GostKursart.PJK: return (zulFach === ZulaessigesFach.PX) ? fach_halbjahr.wahlenGK : 0;
			case GostKursart.VTF: return (zulFach === ZulaessigesFach.VX) ? fach_halbjahr.wahlenGK : 0;
			default: return 0;
		}
	}


	const dragDataKursartSchiene = ref<{ schiene: GostBlockungSchiene } | undefined>(undefined);

	const modalRegelKursartSchienen = ref();

	const istDropZoneSchiene = (schiene: GostBlockungSchiene) : ComputedRef<boolean> => computed(() => {
		return (dragDataKursartSchiene.value !== undefined && (dragDataKursartSchiene.value.schiene.id !== schiene.id));
	});

	function openModalRegelKursartSchiene(schiene: GostBlockungSchiene) {
		const andereSchiene = (dragDataKursartSchiene.value === undefined) ? schiene : dragDataKursartSchiene.value.schiene;
		modalRegelKursartSchienen.value.openModal(andereSchiene, schiene);
	}

	function dragSchieneStarted(schiene: GostBlockungSchiene) {
		dragDataKursartSchiene.value = { schiene };
	}

	function dragSchieneEnded() {
		dragDataKursartSchiene.value = undefined;
	}


	const dragDataKursSchiene = ref<SGostKursplanungKursansichtDragData>(undefined);

	const modal_regel_kurse = ref();
	const modal_combine_kurse = ref();


	function dragKursSchiene(data: SGostKursplanungKursansichtDragData) {
		dragDataKursSchiene.value = data;
	}

	async function dropKursSchiene(zone: SGostKursplanungKursansichtDragData) {
		if ((zone === undefined) || (dragDataKursSchiene.value === undefined))
			return;
		if (dragDataKursSchiene.value.kurs.id !== zone.kurs.id) {
			const schienen = props.getErgebnismanager().getOfKursSchienenmenge(zone.kurs.id);
			if (schienen.contains(zone.schiene))
				modal_combine_kurse.value.openModal(dragDataKursSchiene.value.kurs, zone.kurs);
			else
				modal_regel_kurse.value.openModal(dragDataKursSchiene.value.kurs.id, zone.kurs.id);
			return;
		}
		if ((dragDataKursSchiene.value.kurs.id === zone.kurs.id) && (!props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(zone.kurs.id, zone.schiene.id)) ) {
			// Entferne potentielle Fixierung beim Verschieben.
			if (allow_regeln.value && props.getDatenmanager().kursGetHatFixierungInSchiene(dragDataKursSchiene.value.kurs.id, zone.schiene.id)) {
				const regel = props.getDatenmanager().kursGetRegelFixierungInSchiene(zone.kurs.id, zone.schiene.id);
				await props.removeRegel(regel.id);
			}
			await props.updateKursSchienenZuordnung(dragDataKursSchiene.value.kurs.id, dragDataKursSchiene.value.schiene.id, zone.schiene.id);
		}
	}


</script>
