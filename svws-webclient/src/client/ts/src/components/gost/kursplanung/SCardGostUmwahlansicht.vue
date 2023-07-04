<template>
	<svws-ui-content-card v-if="schueler !== undefined" title="Kurszuordnungen" class="min-w-[30rem]">
		<!-- Link für den Wechsel zur Laufbahnplanung -->
		<template #actions>
			<svws-ui-button type="secondary" @click="routeLaufbahnplanung()" :title="`Zur Laufbahnplanung von ${schueler.vorname + ' ' + schueler.nachname}`">
				<i-ri-group-line />
				{{ 'Laufbahnplanung von ' + schueler.vorname + ' ' + schueler.nachname }}
			</svws-ui-button>
		</template>

		<!-- Anzeige der Umwahlansicht, falls Fächer belegt wurden ... -->
		<div class="flex gap-4 -mt-2" v-if="fachbelegungen.size() > 0">
			<!-- Übersicht über die Fachwahlen des Schülers -->
			<div class="w-1/6 min-w-[9rem]">
				<!-- der Drop-Bereich für den Mülleimer von Kurs-Schülerzuordnung - dieser umfasst auch die Fachwahlliste -->
				<svws-ui-drop-data v-slot="{ active }" class="mb-4" @drop="drop_entferne_kurszuordnung">
					<div class="border-2 -m-[2px]" :class="{ 'border-dashed border-error': active, 'border-transparent': !active }">
						<!-- Die Liste mit den Fachwahlen -->
						<svws-ui-data-table :items="[]" :no-data="false" :disable-header="true">
							<template #body>
								<s-kurs-schueler-fachbelegung v-for="fach in fachbelegungen" :key="fach.fachID" :fach="fach" :kurse="blockungsergebnisse"
									:schueler-id="schueler.id" :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager" :drag-and-drop-data="dragAndDropData" @dnd="updateDragAndDropData" />
							</template>
						</svws-ui-data-table>

						<!-- Der "Mülleimer für das Ablegen von Kursen, bei denen die Kurs-Schüler-Zuordnung aufgehoben werden soll. " -->
						<template v-if="!blockung_aktiv">
							<div class="flex items-center py-2 px-3 m-1" :class="{'bg-error text-white': active}">
								<div v-if="active" class="flex gap-2 items-center w-full h-full">
									<i-ri-delete-bin-line class="w-6 h-6" :class="{ 'bg-error': is_dragging }" />
									<span>Entfernen</span>
								</div>
								<div v-else class="flex gap-2 items-center w-full h-full">
									<i-ri-delete-bin-line class="w-6 h-6 opacity-25" :class="{ 'bg-error': is_dragging }" />
									<span class="opacity-25">Entfernen</span>
								</div>
							</div>
						</template>
					</div>
				</svws-ui-drop-data>

				<!-- Ein Knopf zum Verwerfen der alten Verteilung beim Schüler und für eine Neuzuordnung des Schülers zu den Kursen -->
				<svws-ui-button class="w-full justify-center" type="secondary" @click="auto_verteilen" :disabled="apiStatus.pending" title="Automatisch verteilen">Verteilen<i-ri-sparkling-line /></svws-ui-button>
			</div>

			<!-- Übersicht über die Kurs-Schienen-Zuordnung für den Schüler -->
			<div class="flex-grow">
				<svws-ui-data-table :items="[]" :columns="cols" :disable-header="true" :disable-footer="true" :no-data="false">
					<template #header><div /></template>
					<template #body>
						<div v-for="(schiene, index) in getErgebnismanager().getMengeAllerSchienen()" :key="index"
							role="row" class="data-table__tr data-table__thead__tr" :class="{ 'border border-error': hatSchieneKollisionen(schiene.id, schueler.id).value }">
							<!-- Informationen zu der Schiene und der Statistik dazu auf der linken Seite der Tabelle -->
							<div role="cell" class="data-table__td" :class="{ 'text-error': hatSchieneKollisionen(schiene.id, schueler.id).value }">
								<div class="flex flex-col py-1" :title="getErgebnismanager().getSchieneG(schiene.id).bezeichnung">
									<span class="font-medium inline-flex items-center gap-1 text-base">
										<svws-ui-tooltip v-if="hatSchieneKollisionen(schiene.id, schueler.id).value">
											<i-ri-alert-line />
											<template #content>
												<span>Kollision in dieser Schiene</span>
											</template>
										</svws-ui-tooltip>
										<span class="line-clamp-1">{{ getErgebnismanager().getSchieneG(schiene.id).bezeichnung }}</span>
									</span>
									<span class="text-sm font-medium opacity-50">{{ schiene.kurse.size() }} Kurs{{ schiene.kurse.size() === 1 ? '' : 'e' }}</span>
									<span class="text-sm font-medium opacity-50">{{ getErgebnismanager().getOfSchieneAnzahlSchueler(schiene.id) }} Schüler</span>
								</div>
							</div>

							<!-- Die Liste der Schüler-Kurse (von links nach rechts), welche der Schiene zugeordnet sind (stehen ggf. für drag und/oder drop zur Verfügung). -->
							<svws-ui-drag-data v-for="kurs of schiene.kurse" :key="kurs.id" tag="div" role="cell" :data="{ id: kurs.id, fachID: kurs.fachID, kursart: kurs.kursart }"
								class="data-table__td data-table__td__align-center data-table__td__no-padding select-none group relative p-0.5"
								:class="{ 'is-drop-zone': is_drop_zone(kurs).value, 'cursor-grab': is_draggable(kurs.id, schueler.id).value }"
								:draggable="is_draggable(kurs.id, schueler.id).value" @drag-start="drag_started" @drag-end="drag_ended">
								<svws-ui-drop-data @drop="drop_aendere_kurszuordnung(kurs, schueler.id)" :drop-allowed="is_drop_zone(kurs).value"
									class="w-full h-full flex flex-col justify-center items-center rounded" :style="{ 'background-color': bgColor(kurs.id, schueler.id) }">
									<span class="group-hover:bg-white rounded w-3 absolute top-1 left-1" v-if="is_draggable(kurs.id, schueler.id).value">
										<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
									</span>
									<span class="text-sm opacity-50 relative" title="Schriftlich/Insgesamt im Kurs">
										{{ getErgebnismanager().getOfKursAnzahlSchuelerSchriftlich(kurs.id) }}/{{ kurs.schueler.size() }}
									</span>
									<span class="py-0.5 font-medium">{{ getErgebnismanager().getOfKursName(kurs.id) }}</span>
									<span class="inline-flex items-center gap-1">
										<span v-if="(allow_regeln && fach_gewaehlt(schueler.id, kurs).value && !getDatenmanager().daten().istAktiv)">
											<span class="icon cursor-pointer" @click.stop="verbieten_regel_toggle(kurs.id, schueler.id)" :title="verbieten_regel(kurs.id, schueler.id).value ? 'Verboten' : 'Verbieten'">
												<i-ri-forbid-fill v-if="verbieten_regel(kurs.id, schueler.id).value" class="inline-block" />
												<i-ri-forbid-line v-if="!verbieten_regel(kurs.id, schueler.id).value && !fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
											</span>
											<span class="icon cursor-pointer" @click.stop="fixieren_regel_toggle(kurs.id, schueler.id)" :title="fixier_regel(kurs.id, schueler.id).value ? 'Fixiert' : 'Fixieren'">
												<i-ri-pushpin-fill v-if="fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
												<i-ri-pushpin-line v-if="!verbieten_regel(kurs.id, schueler.id).value && !fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
											</span>
										</span>
										<span v-else>
											<span class="icon" title="Verboten"> <i-ri-forbid-fill v-if="verbieten_regel(kurs.id, schueler.id).value" class="inline-block" /> </span>
											<span class="icon" title="Fixiert"> <i-ri-pushpin-fill v-if="fixier_regel(kurs.id, schueler.id).value" class="inline-block" /> </span>
										</span>
									</span>
								</svws-ui-drop-data>
							</svws-ui-drag-data>

							<!-- Auffüllen mit leeren Zellen, falls in der Schiene nicht die maximale Anzahl von Kursen vorliegt. -->
							<div v-for="n in (maxKurseInSchienen - schiene.kurse.size())" :key="n" role="cell" class="data-table__td" />
						</div>
					</template>
				</svws-ui-data-table>
			</div>
		</div>

		<!-- ... ansonsten keine Anzeige der Umwahlansicht, falls keine Fächer belegt wurden -->
		<div v-else class="opacity-50">
			Keine Fachbelegungen vorhanden.
		</div>
	</svws-ui-content-card>
</template>


<script setup lang="ts">

	import { ArrayList, ZulaessigesFach, type GostBlockungKurs, type GostBlockungsergebnisKurs, type GostFachwahl, type List, GostKursblockungRegelTyp, GostBlockungRegel } from "@core";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import type { GostUmwahlansichtProps } from "./SCardGostUmwahlansichtProps";
	import type {DataTableColumn} from "@ui";

	type DndData = { id: number, fachID: number, kursart: number };

	const props = defineProps<GostUmwahlansichtProps>();

	const is_dragging: Ref<boolean> = ref(false);

	const dragAndDropData: Ref<DndData | undefined> = ref(undefined);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1);

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer());

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const blockungsergebnisse: ComputedRef<Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>> = computed(() => {
		const map = new Map();
		const schienen = props.getErgebnismanager().getMengeAllerSchienen();
		if (schienen.isEmpty())
			return map;
		for (const k of kurse.value)
			for (const s of schienen) {
				const arr = []
				for (const kk of s.kurse)
					kk.id === k.id ? arr.push(kk) : arr.push(undefined)
				map.set(k, arr)
			}
		return map;
	});

	async function drop_entferne_kurszuordnung(kurs: any) {
		const schuelerid = props.schueler?.id;
		if (schuelerid === undefined || kurs === undefined || kurs.id === undefined)
			return;
		await props.removeKursSchuelerZuordnung(schuelerid, kurs.id);
	}

	async function auto_verteilen() {
		if (props.schueler !== undefined)
			await props.autoKursSchuelerZuordnung(props.schueler.id);
	}

	const fachbelegungen: ComputedRef<List<GostFachwahl>> = computed(() => {
		if (props.schueler === undefined)
			return new ArrayList<GostFachwahl>();
		return props.getDatenmanager().schuelerGetListeOfFachwahlen(props.schueler.id);
	});

	function routeLaufbahnplanung() {
		if (props.schueler !== undefined)
			void props.gotoLaufbahnplanung(props.schueler.id);
	}

	function updateDragAndDropData(data: DndData | undefined) {
		dragAndDropData.value = data;
	}

	const maxKurseInSchienen: ComputedRef<number> = computed(() => {
		let max = 0;
		const schienen = props.getErgebnismanager().getMengeAllerSchienen();
		for (let i = 0; i < schienen.size(); i++)
			max = Math.max(max, schienen.get(i).kurse.size());
		return max;
	});

	function calculateColumns(): DataTableColumn[] {
		const cols: Array<DataTableColumn> = [{ key: "schiene", label: "Schiene", minWidth: 9, span: 0.1 }];
		for (let i = 0; i < maxKurseInSchienen?.value; i++)
			cols.push({ key: "kurs_" + (i+1), label: "Kurs " + (i+1), align: 'center', minWidth: 6 });
		return cols;
	}

	const cols: ComputedRef<DataTableColumn[]> = computed(() => calculateColumns());

	const hatSchieneKollisionen = (idSchiene: number, idSchueler: number) : ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchieneSchuelermengeMitKollisionen(idSchiene).contains(idSchueler)
	);


	const is_draggable = (idKurs: number, idSchueler: number) : ComputedRef<boolean> => computed(() => {
		if (props.apiStatus.pending || props.getDatenmanager().daten().istAktiv)
			return false;
		return props.getErgebnismanager().getOfKursSchuelerIDmenge(idKurs).contains(idSchueler);
	});

	const is_drop_zone = (kurs: GostBlockungsergebnisKurs) : ComputedRef<boolean> => computed(() => {
		if (dragAndDropData.value === undefined)
			return false
		const { id, fachID, kursart } = dragAndDropData.value;
		if ((id != undefined) && (id === kurs.id))
			return false;
		if ((fachID !== kurs.fachID) || (kursart !== kurs.kursart))
			return false;
		return true;
	});

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as DndData;
		if (!data)
			return;
		dragAndDropData.value = data;
	}

	function drag_ended() {
		dragAndDropData.value = undefined;
	}

	async function drop_aendere_kurszuordnung(kurs_neu: GostBlockungsergebnisKurs, idSchueler : number) {
		const kurs_alt = dragAndDropData.value;
		if (kurs_alt === undefined)
			return;
		if (!is_drop_zone(kurs_neu).value)
			return;
		await props.updateKursSchuelerZuordnung(idSchueler, kurs_neu.id, kurs_alt.id);
		drag_ended();
	}

	function bgColor(idKurs : number, idSchueler : number) : string {
		if (props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			const k = props.getDatenmanager().kursGet(idKurs);
			const f = props.getDatenmanager().faecherManager().get(k.fach_id);
			const zf = ZulaessigesFach.getByKuerzelASD(f?.kuerzel || null)
			return zf.getHMTLFarbeRGB();
		}
		return "";
	}

	const fach_gewaehlt = (idSchueler: number, kurs: GostBlockungsergebnisKurs): ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchuelerHatFachwahl(idSchueler, kurs.fachID, kurs.kursart)
	);

	const fixier_regel = (idKurs: number, idSchueler: number) : ComputedRef<number | undefined> => computed(() => {
		// TODO ersetzen falls die folgende Methode im Manager effektiv implementiert ist: props.getErgebnismanager().getOfSchuelerOfKursIstFixiert(idSchueler, idKurs)
		for (const regel of props.getDatenmanager().regelGetListe())
			if ((regel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) && (regel.parameter.get(0) === idSchueler) && (regel.parameter.get(1) === idKurs))
				return regel.id;
		return undefined;
	});

	const verbieten_regel = (idKurs: number, idSchueler: number) : ComputedRef<number | undefined> => computed(() => {
		// TODO ersetzen falls die folgende Methode im Manager effektiv implementiert ist: props.getErgebnismanager().getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs)
		for (const regel of props.getDatenmanager().regelGetListe())
			if ((regel.typ === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ) && (regel.parameter.get(0) === idSchueler) && (regel.parameter.get(1) === idKurs))
				return regel.id;
		return undefined;
	});

	const fixieren_regel_toggle = (idKurs: number, idSchueler: number) => fixier_regel(idKurs, idSchueler).value ? fixieren_regel_entfernen(idKurs, idSchueler) : fixieren_regel_hinzufuegen(idKurs, idSchueler);

	const verbieten_regel_toggle = (idKurs: number, idSchueler: number) => verbieten_regel(idKurs, idSchueler).value ? verbieten_regel_entfernen(idKurs, idSchueler) : verbieten_regel_hinzufuegen(idKurs, idSchueler);

	const regel_speichern = async (regel: GostBlockungRegel, idKurs: number, idSchueler: number) => {
		regel.parameter.add(idSchueler);
		regel.parameter.add(idKurs);
		await props.addRegel(regel);
	}

	const fixieren_regel_hinzufuegen = async (idKurs: number, idSchueler: number) => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	const fixieren_regel_entfernen = async (idKurs: number, idSchueler: number) => {
		const idRegel = fixier_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return
		await props.removeRegel(idRegel);
	}

	const verbieten_regel_hinzufuegen = async (idKurs: number, idSchueler: number) => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	const verbieten_regel_entfernen = async (idKurs: number, idSchueler: number) => {
		const idRegel = verbieten_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return
		await props.removeRegel(idRegel);
	}

</script>


<style lang="postcss" scoped>

	.is-drop-zone {
		@apply relative bg-primary/5;

		&:before {
			content: '';
			@apply absolute inset-1 border-2 border-dashed border-primary pointer-events-none;
		}
	}

</style>
