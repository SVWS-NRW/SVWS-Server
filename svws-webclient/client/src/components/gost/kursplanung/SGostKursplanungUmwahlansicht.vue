<template>
	<div v-if="hatBlockung && hatErgebnis && (schueler !== undefined)" class="min-w-[42rem] w-fit h-full flex flex-col">
		<div class="content-card--header mb-2 w-fit h-fit">
			<div class="flex flex-row">
				<svws-ui-button type="icon" @click="routeLaufbahnplanung()" :title="`Zur Laufbahnplanung von ${schueler.vorname} ${schueler.nachname}`" size="small" class="mr-0.5 mt-0.5">
					<span class="icon i-ri-link" />
				</svws-ui-button>
				<span class="text-headline-md">{{ schueler.vorname }} {{ schueler.nachname }}</span>
			</div>
		</div>
		<!-- Anzeige der Umwahlansicht, falls Fächer belegt wurden ... -->
		<div class="flex gap-12 pr-7 overflow-y-auto h-full" v-if="fachbelegungen.size() > 0" style="scrollbar-gutter: stable; scrollbar-width: thin; scrollbar-color: rgba(0,0,0,0.2) transparent;">
			<!-- Übersicht über die Fachwahlen des Schülers -->
			<div class="w-1/6 min-w-[10rem]">
				<!-- der Drop-Bereich für den Mülleimer von Kurs-Schülerzuordnung - dieser umfasst auch die Fachwahlliste -->
				<div class="mb-5" @dragover="$event.preventDefault()" @drop="drop_entferne_kurszuordnung">
					<div>
						<!-- Die Liste mit den Fachwahlen -->
						<svws-ui-table :items="[]" :no-data="false" :disable-header="true" type="navigation" has-background class="mt-1">
							<template #body>
								<div v-for="fach in fachbelegungen" :key="fach.fachID" role="row" class="svws-ui-tr !w-full" :class="{ 'font-medium': (fachwahlKurszuordnungen.get(fach.fachID) === undefined) }">
									<div role="cell"
										:draggable="hatUpdateKompetenz && (fachwahlKurszuordnungen.get(fach.fachID) === undefined)"
										@dragstart="drag_started(fachwahlKurszuordnungen.get(fach.fachID)?.id, fach.fachID, fachwahlKursarten.get(fach.fachID)!.id)"
										@dragend="drag_ended()"
										class="select-none svws-ui-td svws-no-padding group -my-1 !py-0.5"
										:class="hatUpdateKompetenz && (fachwahlKurszuordnungen.get(fach.fachID) === undefined) ? 'cursor-grab' : 'opacity-50'"
										:style="{ '--background-color': bgColorFachwahl(fach.fachID) }">
										<div class="svws-ui-badge w-auto flex-grow -mx-3 py-0.5 !my-0 !h-full items-center">
											<div class="flex flex-row flex-grow">
												<template v-if="(fachwahlKurszuordnungen.get(fach.fachID) === undefined) && hatUpdateKompetenz">
													<span class="icon-sm i-ri-draggable opacity-50 group-hover:opacity-100 rounded-sm group-hover:bg-white/50" />
												</template>
												<template v-else>
													<span class="w-3 text-sm">&nbsp;</span>
												</template>
												<span class="ml-0.5">
													{{ getFachwahlKursname(fach.fachID) }}
													<template v-if="getDatenmanager().getHalbjahr().istQualifikationsphase() && getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, fach.fachID).abiturfach !== null">
														<span class="text-sm ml-2 mr-2">—</span>
														<span>AB{{ getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, fach.fachID).abiturfach }}</span>
													</template>
												</span>
											</div>
											<span v-if="fach.istSchriftlich" class="icon i-ri-draft-line -my-0.5" />
											<span v-else class="icon i-ri-chat-1-line -my-0.5" />
										</div>
									</div>
								</div>
							</template>
						</svws-ui-table>
						<!-- Ein Knopf zum Verwerfen der alten Verteilung beim Schüler und für eine Neuzuordnung des Schülers zu den Kursen -->
						<template v-if="hatUpdateKompetenz">
							<svws-ui-button type="secondary" @click="auto_verteilen" :disabled="apiStatus.pending" title="Automatisch verteilen und aktuelle Zuordnung verwerfen" class="mt-2 w-full"><span class="icon i-ri-sparkling-line" />Verteilen</svws-ui-button>
							<!-- Der "Mülleimer für das Ablegen von Kursen, bei denen die Kurs-Schüler-Zuordnung aufgehoben werden soll. " -->
							<div class="mt-5 py-4 border-2 rounded-xl border-dashed border-black/10 dark:border-white/10" :class="[dragAndDropData === undefined ? 'border-black/10 dark:border-white/10' : 'border-error ring-4 ring-error/10']">
								<div class="flex items-center gap-2 justify-center" :class="[dragAndDropData === undefined ? 'opacity-25' : 'opacity-100']">
									<span class="icon i-ri-delete-bin-line text-headline flex-shrink-0" :class="[dragAndDropData === undefined ? 'opacity-50' : 'text-error']" />
									<span class="text-sm w-2/3">
										<template v-if="dragAndDropData === undefined">Kurse hier zum<br>Löschen ablegen</template>
										<template v-else>Kurs-Zuordnung<br>aufheben</template>
									</span>
								</div>
							</div>
						</template>
					</div>
				</div>
			</div>

			<!-- Übersicht über die Kurs-Schienen-Zuordnung für den Schüler -->
			<div class="flex-grow">
				<svws-ui-table :items="[]" :columns="cols" :disable-header="true" :no-data="false" class="border-t">
					<template #body>
						<div v-for="(schiene, index) in getErgebnismanager().getMengeAllerSchienen()" :key="index"
							role="row" class="svws-ui-tr">
							<!-- Informationen zu der Schiene und der Statistik dazu auf der linken Seite der Tabelle -->
							<div role="cell" class="svws-ui-td svws-divider">
								<div class="flex flex-col py-1" :title="getErgebnismanager().getSchieneG(schiene.id).bezeichnung">
									<div class="inline-flex items-center" :class="{'text-error': hatSchieneKollisionen(schiene.id).value}">
										<span class="icon icon-error i-ri-alert-line -mt-0.5" v-if="hatSchieneKollisionen(schiene.id).value" />
										<span class="mb-0.5 text-button">{{ getErgebnismanager().getSchieneG(schiene.id).bezeichnung }}</span>
									</div>
									<span class="text-sm font-medium opacity-50">{{ schiene.kurse.size() }} Kurs{{ schiene.kurse.size() === 1 ? '' : 'e' }}</span>
									<span class="text-sm font-medium opacity-50">{{ getErgebnismanager().getOfSchieneAnzahlSchueler(schiene.id) }} Schüler</span>
								</div>
							</div>

							<!-- Die Liste der Schüler-Kurse (von links nach rechts), welche der Schiene zugeordnet sind (stehen ggf. für drag und/oder drop zur Verfügung). -->
							<div role="cell" v-for="kurs of getErgebnismanager().getOfSchieneKursmengeSortiert(schiene.id)" :key="kurs.id"
								class="svws-ui-td svws-align-center svws-no-padding select-none group relative !p-[2px] svws-divider last:!border-r-0"
								:class="{ 'is-drop-zone': is_drop_zone(kurs).value, 'cursor-grab': is_draggable(kurs.id).value }"
								:draggable="is_draggable(kurs.id).value"
								@dragstart="drag_started(kurs.id, kurs.fachID, kurs.kursart)"
								@dragend="drag_ended()">
								<div class="w-full h-full flex flex-col justify-center items-center rounded border border-black/10 py-1 px-0.5"
									:style="{ 'background-color': hatSchieneKollisionen(schiene.id).value && getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id) ? 'rgb(var(--color-error))' : bgColor(kurs.id) }"
									:class="{ 'text-white' : hatSchieneKollisionen(schiene.id).value && getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id)}"
									@dragover="if (is_drop_zone(kurs).value) $event.preventDefault();" @drop="drop_aendere_kurszuordnung(kurs)">
									<span class="icon-sm i-ri-draggable mt-1 ml-1 opacity-50 group-hover:opacity-100 rounded-sm absolute top-0 left-0" v-if="is_draggable(kurs.id).value" :class="[hatSchieneKollisionen(schiene.id).value && is_draggable(kurs.id).value ? 'group-hover:bg-white/25 icon-white' : 'group-hover:bg-white/75 icon-black']" />
									<span class="text-sm opacity-50 relative" title="Schriftlich/Insgesamt im Kurs">
										{{ getErgebnismanager().getOfKursAnzahlSchuelerSchriftlich(kurs.id) }}/{{ kurs.schueler.size() }}
									</span>
									<span class="py-0.5 font-medium" :class="{'opacity-50': !getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id)}">{{ getErgebnismanager().getOfKursName(kurs.id) }}</span>
									<span class="inline-flex items-center gap-1">
										<span v-if="getDatenmanager().getHalbjahr().istQualifikationsphase() && getAbiturfach(kurs.id).value !== null"
											class="opacity-75 text-sm">
											AB{{ getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, kurs.fachID).abiturfach }}
										</span>
										<template v-if="fach_gewaehlt(kurs)">
											<span class="cursor-pointer" @click.stop="verbieten_regel_toggle(kurs.id)" :title="verbieten_regel(kurs.id) ? 'Verboten' : 'Verbieten'">
												<span class="icon i-ri-forbid-fill inline-block" v-if="verbieten_regel(kurs.id)" />
												<span class="icon i-ri-prohibited-line inline-block opacity-75" v-if="!verbieten_regel(kurs.id) && !fixier_regel(kurs.id) && !getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id)" />
											</span>
											<span class="cursor-pointer" @click.stop="fixieren_regel_toggle(kurs.id)" :title="fixier_regel(kurs.id) ? 'Fixiert' : 'Fixieren'">
												<span class="icon i-ri-pushpin-fill inline-block" v-if="fixier_regel(kurs.id)"
													:class="[hatSchieneKollisionen(schiene.id).value ? 'icon-white' : '']" />
												<span class="icon i-ri-pushpin-line inline-block opacity-75" v-if="!verbieten_regel(kurs.id) && !fixier_regel(kurs.id)"
													:class="[hatSchieneKollisionen(schiene.id).value ? 'icon-white' : '']" />
											</span>
										</template>
										<template v-else>
											<span class="icon inline-block i-ri-forbid-fill" v-if="verbieten_regel(kurs.id)" title="Verboten" />
											<span class="icon inline-block i-ri-pushpin-fill" v-if="fixier_regel(kurs.id)" title="Fixiert" />
										</template>
									</span>
								</div>
							</div>

							<!-- Auffüllen mit leeren Zellen, falls in der Schiene nicht die maximale Anzahl von Kursen vorliegt. -->
							<div v-for="n in leereZellen(schiene.id).value" :key="n" role="cell" class="svws-ui-td svws-divider last:!border-r-0" />
						</div>
					</template>
				</svws-ui-table>
			</div>
		</div>

		<!-- ... ansonsten keine Anzeige der Umwahlansicht, falls keine Fächer belegt wurden -->
		<div v-else class="opacity-50">
			Keine Fachbelegungen vorhanden.
		</div>
	</div>
</template>


<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { GostKursplanungUmwahlansichtProps } from "./SGostKursplanungUmwahlansichtProps";
	import type { GostBlockungRegel, GostBlockungsergebnisKurs, GostFachwahl, GostKursart, List } from "@core";
	import { BenutzerKompetenz, DTOUtils, GostBlockungRegelUpdate, SetUtils, Fach } from "@core";

	type DndData = { id: number | undefined, fachID: number, kursart: number };

	const props = defineProps<GostKursplanungUmwahlansichtProps>();

	const schuljahr = computed<number>(() => props.getDatenmanager().faecherManager().getSchuljahr());

	const idSchueler = computed<number>(() => props.schueler === undefined ? -1 : props.schueler.id);

	const hatUpdateKompetenz = computed<boolean>(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)
				&& props.benutzerKompetenzenAbiturjahrgaenge.has(props.getDatenmanager().daten().abijahrgang))
	});

	const dragAndDropData = ref<DndData | undefined>(undefined);

	const leereZellen = (id: number) => computed<number>(() => {
		const diff = props.getErgebnismanager().getOfSchieneMaxKursanzahl() - props.getErgebnismanager().getOfSchieneKursmengeSortiert(id).size();
		return diff < 0 ? 0 : diff;
	})

	async function auto_verteilen() {
		await props.autoKursSchuelerZuordnung(idSchueler.value);
	}

	const fachbelegungen = computed<List<GostFachwahl>>(() => {
		return props.getDatenmanager().schuelerGetListeOfFachwahlen(idSchueler.value);
	});

	function routeLaufbahnplanung() {
		void props.gotoLaufbahnplanung(idSchueler.value);
	}

	const cols = computed(() => {
		const cols: DataTableColumn[] = [{ key: "schiene", label: "Schiene", minWidth: 9, span: 0.1, align: 'left' }];
		for (let i = 0; i < props.getErgebnismanager().getOfSchieneMaxKursanzahl(); i++)
			cols.push({ key: "kurs_" + (i+1), label: "Kurs " + (i+1), align: 'center', minWidth: 6, span: 1 });
		return cols;
	})

	const hatSchieneKollisionen = (idSchiene: number) => computed<boolean>(() =>
		props.getErgebnismanager().getOfSchuelerOfSchieneHatKollision(idSchueler.value, idSchiene)
	);

	const getAbiturfach = (idKurs: number) => computed<number | null>(() => {
		if (!props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler.value, idKurs))
			return null;
		if (props.getErgebnismanager().getOfSchuelerOfKursIstUngueltig(idSchueler.value, idKurs))
			return null;
		const fachwahl = props.getErgebnismanager().getOfSchuelerOfKursFachwahl(idSchueler.value, idKurs);
		return fachwahl.abiturfach;
	});

	const is_draggable = (idKurs: number) => computed<boolean>(() => {
		if (props.apiStatus.pending || !hatUpdateKompetenz.value)
			return false;
		return props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler.value, idKurs) && !props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler.value, idKurs);
	});

	const is_drop_zone = (kurs: GostBlockungsergebnisKurs) => computed<boolean>(() => {
		if (dragAndDropData.value === undefined)
			return false
		const { id, fachID, kursart } = dragAndDropData.value;
		if ((id !== undefined) && (id === kurs.id))
			return false;
		if ((fachID !== kurs.fachID) || (kursart !== kurs.kursart))
			return false;
		if (props.getErgebnismanager().getOfSchuelerOfKursIstGesperrt(idSchueler.value, kurs.id))
			return false;
		return true;
	});

	function drag_started(id : number | undefined, fachID: number, kursart: number) {
		dragAndDropData.value = { id, fachID, kursart };
	}

	function drag_ended() {
		dragAndDropData.value = undefined;
	}

	async function drop_aendere_kurszuordnung(kurs_neu: GostBlockungsergebnisKurs) {
		const kurs_alt = dragAndDropData.value;
		if ((kurs_alt === undefined) || (!is_drop_zone(kurs_neu).value))
			return;
		const zuordnung = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs_neu.id, idSchueler.value);
		const update = props.getErgebnismanager().kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(SetUtils.create1(zuordnung));
		await props.updateKursSchuelerZuordnungen(update);
	}

	async function drop_entferne_kurszuordnung(e: DragEvent) {
		const obj = dragAndDropData.value;
		if ((obj === undefined) || (obj.id === undefined))
			return;
		const zuordnung = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(obj.id, idSchueler.value);
		const update = props.getErgebnismanager().kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(SetUtils.create1(zuordnung));
		await props.updateKursSchuelerZuordnungen(update);
	}

	function bgColor(idKurs : number) : string {
		if (props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler.value, idKurs)) {
			const k = props.getDatenmanager().kursGet(idKurs);
			const f = props.getDatenmanager().faecherManager().get(k.fach_id);
			const zf = (f === null) ? null : Fach.getBySchluesselOrDefault(f.kuerzel);
			return zf?.getHMTLFarbeRGB(schuljahr.value) ?? 'rgb(220,220,220)';
		}
		return "";
	}

	function fach_gewaehlt(kurs: GostBlockungsergebnisKurs) : boolean {
		return props.getErgebnismanager().getOfSchuelerHatFachwahl(idSchueler.value, kurs.fachID, kurs.kursart);
	}

	function fixier_regel(idKurs: number) : GostBlockungRegel | null {
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler.value, idKurs))
			return props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler.value, idKurs);
		return null;
	}

	function verbieten_regel(idKurs: number) : GostBlockungRegel | null {
		if (props.getDatenmanager().schuelerGetIstVerbotenInKurs(idSchueler.value, idKurs))
			return props.getDatenmanager().schuelerGetRegelVerbotenInKurs(idSchueler.value, idKurs);
		return null;
	}

	async function fixieren_regel_toggle(idKurs: number) {
		let update = new GostBlockungRegelUpdate();
		const regel = fixier_regel(idKurs);
		if (regel !== null)
			update.listEntfernen.add(regel);
		else
			update = props.getErgebnismanager().regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(SetUtils.create1(idSchueler.value), SetUtils.create1(idKurs));
		await props.regelnUpdate(update);
	}

	async function verbieten_regel_toggle(idKurs: number) {
		let update = new GostBlockungRegelUpdate();
		const regel = verbieten_regel(idKurs);
		if (regel !== null)
			update.listEntfernen.add(regel);
		else
			update = props.getErgebnismanager().regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(SetUtils.create1(idSchueler.value), SetUtils.create1(idKurs));
		await props.regelnUpdate(update);
	}

	const fachwahlKurszuordnungen = computed<Map<number, GostBlockungsergebnisKurs>>(() => {
		const zuordnungen = new Map<number, GostBlockungsergebnisKurs>();
		if (props.schueler === undefined)
			return zuordnungen;
		for (const belegung of fachbelegungen.value) {
			const z = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler.value, belegung.fachID);
			if (z !== null)
				zuordnungen.set(belegung.fachID, z);
		}
		return zuordnungen;
	});

	const fachwahlKursarten = computed<Map<number, GostKursart>>(() => {
		const kursarten = new Map<number, GostKursart>();
		if (props.schueler === undefined)
			return kursarten;
		for (const belegung of fachbelegungen.value) {
			const z = props.getErgebnismanager().getOfSchuelerOfFachKursart(idSchueler.value, belegung.fachID);
			kursarten.set(belegung.fachID, z);
		}
		return kursarten;
	});

	function bgColorFachwahl(idFach: number) : string {
		const fwKurszuordnung = fachwahlKurszuordnungen.value.get(idFach);
		if (fwKurszuordnung !== undefined)
			return "white";
		const f = props.getErgebnismanager().getFach(idFach);
		return Fach.getBySchluesselOrDefault(f.kuerzel).getHMTLFarbeRGB(schuljahr.value);
	}

	function getFachwahlKursname(idFach: number): string {
		const fw = fachwahlKurszuordnungen.value.get(idFach);
		if (fw === undefined) {
			const f = props.getErgebnismanager().getFach(idFach);
			const fwKursart = fachwahlKursarten.value.get(idFach);
			return `${f.kuerzelAnzeige ?? '??'}-${fwKursart?.kuerzel ?? '??'}`;
		}
		return props.getErgebnismanager().getOfKursName(fw.id);
	}

</script>


<style lang="postcss" scoped>

	.is-drop-zone {
		@apply relative bg-primary/5;

		&:before {
			content: '';
			@apply absolute inset-1 border border-svws pointer-events-none rounded-lg ring-4 ring-svws/25;
		}
	}

</style>
