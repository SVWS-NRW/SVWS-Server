<template>
	<div class="page page-flex-row">
		<Teleport to=".svws-sub-nav-target" defer>
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<div class="ml-4 flex gap-0.5 items-center leading-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="i => i.kuerzel" autocomplete
						:item-filter="(i, text)=> i.filter(k => k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" focus-class-sub-nav />
					<template v-if="stundenplanManager().getWochenTypModell() > 0">
						<div class="text-button font-bold mr-1 -mt-px">Wochentyp:</div>
						<svws-ui-select headless title="Wochentyp" v-model="wochentypAnzeige" :items="wochentypen()" class="print:!hidden" type="transparent"
							:disabled="wochentypen().size() <= 0" :item-text="wt => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
					</template>
					<svws-ui-button v-if="hatUpdateKompetenz" type="transparent" @click.stop="doppelstundenModus = !doppelstundenModus" title="Doppelstundenmodus ein- und ausschalten" class="text-ui-100">
						{{ doppelstundenModus ? 'Doppelstundenmodus' : 'Einzelstundenmodus' }}
					</svws-ui-button>
					<template v-if="(stundenplanManager().unterrichtsgruppenMergeableGet().size() > 0) || (stundenplanManager().unterrichtGetMengeUngueltigAsList().size() > 0) && hatUpdateKompetenz">
						<span class="ml-4">Unterricht:</span>
						<s-stundenplan-klasse-modal-ungueltige v-if="stundenplanManager().unterrichtGetMengeUngueltigAsList().size() > 0" :stundenplan-manager :remove-unterrichte v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" class="ml-1" @click="openTheModal(openModal)" title="Unterricht, der entfernt werden kann, weil es keinen Klassenunterricht dazu gibt">
								<span class="icon icon-ui-danger i-ri-error-warning-line" />ungültige&nbsp;entfernen
							</svws-ui-button>
						</s-stundenplan-klasse-modal-ungueltige>
						<s-stundenplan-klasse-modal-merge v-if="stundenplanManager().unterrichtsgruppenMergeableGet().size() > 0" :stundenplan-manager :merge-unterrichte v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" class="ml-1" @click="openTheModal(openModal)" title="Unterricht, der zusammengelegt werden kann, weil es Doppelungen gibt">
								<span class="icon icon-ui-danger i-ri-error-warning-line" />Zusammenlegen
							</svws-ui-button>
						</s-stundenplan-klasse-modal-merge>
					</template>
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			<span>Für diesen Stundenplan ist keine Klasse vorhanden.</span>
		</template>
		<template v-else>
			<div v-if="hatUpdateKompetenz" @dragover="checkDropZone($event)" @drop="onDrop(undefined, -1)" class="min-w-fit flex flex-col gap-4 justify-start mb-auto svws-table-offset h-full overflow-y-scroll overflow-x-hidden pr-6 border-2 rounded-xl border-dashed"
				:class="[dragData === undefined ? 'border-transparent' : ' border-ui-danger ring-4 ring-ui-danger/10']">
				<svws-ui-table v-if="!stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id).isEmpty()" :items="stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKlassenunterricht">
					<template #body>
						<template v-for="ku in stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :key="ku.idKlasse + '/' + ku.idFach">
							<div role="row" class="svws-ui-tr tr-klassenunterricht" @dragstart="onDrag(ku, $event)" @dragend="onDrag(undefined)">
								<div role="cell" class="select-none svws-ui-td" :class="{
									'text-ui-danger font-bold': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) < 0,
									'font-bold': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) > 0 }">
									<div class="svws-ui-badge select-none group cursor-grab flex place-items-center" @click="auswahl !== ku ? auswahl = ku : auswahl = undefined"
										:style="stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) > 0 ? `color: var(--color-text-uistatic); background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(ku.idFach).kuerzelStatistik)}` : ''"
										:class="{ 'cursor-grabbing': dragData !== undefined, '!border-black/5': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) <= 0 }" draggable="true">
										<span class="icon i-ri-draggable icon-ui-75 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75" />
										<span :class="{'font-bold': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) > 0}">
											{{ stundenplanManager().fachGetByIdOrException(ku.idFach).bezeichnung }}
										</span>
									</div>
								</div>
								<div role="cell" class="select-none svws-ui-td svws-align-center">
									<span class="rounded-sm p-0.5 -m-0.5" :class="{
										'bg-ui-danger text-ui-ondanger font-bold': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) < 0,
										'bg-ui-75': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) > 0,
										'bg-ui-success text-ui-onsuccess': stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) === 0 }">
										{{ stundenplanManager().klassenunterrichtGetWochenstundenIST(ku.idKlasse, ku.idFach) }}/{{ stundenplanManager().klassenunterrichtGetWochenstundenSOLL(ku.idKlasse, ku.idFach) }}
									</span>
								</div>
							</div>
						</template>
					</template>
				</svws-ui-table>
				<div class="fixed flex items-center justify-center h-3/4 w-72 z-20 pointer-events-none"><span :class="dragData === undefined ? '':'icon-lg icon-ui-danger opacity-50 i-ri-delete-bin-line scale-[4]'" /></div>
				<svws-ui-checkbox v-if="!stundenplanManager().schieneGetMengeByKlasseId(klasse.id).isEmpty()" type="toggle" v-model="schienSortierung">Nach Schienen sortieren</svws-ui-checkbox>
				<svws-ui-table v-if="!stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id).isEmpty()" :items="stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKursunterricht">
					<template #body>
						<template v-if="(schienSortierung === true) && (!stundenplanManager().schieneGetMengeByKlasseId(klasse.id).isEmpty())">
							<div v-for="schiene of stundenplanManager().schieneGetMengeByKlasseId(klasse.id)" :key="schiene.id" @dragstart="onDrag(schiene, $event)" @dragend="onDrag(undefined)" draggable="true">
								<!-- Die Schienenzeile -->
								<div role="row" class="svws-ui-tr tr-kursunterricht bg-ui-75">
									<div role="cell" class="select-none svws-ui-td font-bold group" :class="{ 'cursor-grabbing': dragData !== undefined }">
										<div class="select-none group cursor-grab flex place-items-center">
											<span class="icon i-ri-draggable opacity-60 group-hover:opacity-100 group-hover:icon-ui-75" />
											<span>{{ schiene.bezeichnung }}</span>
										</div>
									</div>
									<div role="cell" class="select-none svws-ui-td" />
								</div>
								<!-- Die Kurszeilen -->
								<div v-for="kurs in stundenplanManager().kursGetMengeByKlasseIdAndSchieneId(klasse.id, schiene.id)" :key="kurs.id" role="row" class="svws-ui-tr tr-kursunterricht"
									@dragstart.stop="onDrag(kurs, $event)" @dragend.stop="onDrag(undefined)" @click="toRaw(auswahl) !== kurs ? auswahl = kurs : auswahl = undefined"
									:class="{ 'cursor-grabbing': dragData !== undefined, '!border-black/5': stundenplanManager().kursGetWochenstundenREST(kurs.id) <= 0 }">
									<div role="cell" class="select-none svws-ui-td" :class="{
										'text-ui-danger font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0,
										'font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0 }">
										<div class="svws-ui-badge select-none group cursor-grab flex place-items-center" draggable="true" :class="{ 'cursor-grabbing': dragData !== undefined, 'border-black/5!': stundenplanManager().kursGetWochenstundenREST(kurs.id) <= 0 }"
											:style="stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0 ? `color: var(--color-text-uistatic); background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(kurs.idFach).kuerzelStatistik)}` : ''">
											<span class="icon i-ri-draggable icon-ui-75 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75" />
											<span :class="{'font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0}">{{ kurs.bezeichnung }}</span>
										</div>
									</div>
									<div role="cell" class="select-none svws-ui-td svws-align-center">
										<span class="rounded-sm p-0.5" :class="{
											'bg-ui-danger text-ui-ondanger font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0,
											'bg-ui-75': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0,
											'bg-ui-success text-ui-onsuccess': stundenplanManager().kursGetWochenstundenREST(kurs.id) === 0 }">
											{{ stundenplanManager().kursGetWochenstundenIST(kurs.id) }}/{{ stundenplanManager().kursGetWochenstundenSOLL(kurs.id) }}
										</span>
									</div>
								</div>
							</div>
						</template>
						<template v-else>
							<div v-for="kurs in stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :key="kurs.id" role="row" class="svws-ui-tr tr-kursunterricht"
								@dragstart="onDrag(kurs, $event)" @dragend="onDrag(undefined)" @click="toRaw(auswahl) !== kurs ? auswahl = kurs : auswahl = undefined"
								:style="stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0 ? `color: var(--color-text-uistatic); background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(kurs.idFach).kuerzelStatistik)}` : ''">
								<div role="cell" class="select-none svws-ui-td" :class="{
									'text-ui-danger font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0,
									'font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0 }">
									<div class="svws-ui-badge select-none group cursor-grab flex place-items-center"
										:class="{ 'cursor-grabbing': dragData !== undefined, 'border-ui-10': stundenplanManager().kursGetWochenstundenREST(kurs.id) <= 0 }" draggable="true">
										<span class="icon i-ri-draggable icon-ui-75 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75" />
										<span :class="{'font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0}">{{ kurs.bezeichnung }}</span>
									</div>
								</div>
								<div role="cell" class="select-none svws-ui-td svws-align-center">
									<span class="rounded-sm p-0.5" :class="{
										'bg-ui-danger text-ui-ondanger font-bold': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0,
										'bg-ui-75': stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0,
										'bg-ui-success text-ui-onsuccess': stundenplanManager().kursGetWochenstundenREST(kurs.id) === 0 }">
										{{ stundenplanManager().kursGetWochenstundenIST(kurs.id) }}/{{ stundenplanManager().kursGetWochenstundenSOLL(kurs.id) }}
									</span>
								</div>
							</div>
						</template>
					</template>
				</svws-ui-table>
			</div>
			<!-- Das Zeitraster des Stundenplans, in welches von der linken Seite die Kurs-Unterrichte oder die Klassen-Unterricht hineingezogen werden können.-->
			<stundenplan-klassen mode-pausenaufsichten="tooltip" :id="klasse.id" :manager="stundenplanManager" :wochentyp="()=>wochentypAnzeige" :kalenderwoche="() => undefined"
				:use-drag-and-drop="hatUpdateKompetenz" :drag-data="() => dragData" :on-drag :on-drop class="h-full overflow-scroll pr-4" @update:click="u => toRaw(auswahl) !== u ? auswahl = u : auswahl = undefined" />
			<!-- Card für die zusätzlichen Einstellungen zum Unterricht -->
			<div class="flex flex-col gap-4 w-96 min-w-96">
				<template v-if="(auswahl !== undefined)">
					<div class="text-headline-md">Raumzuordnung {{ unterrichtBezeichnung }} ({{ schuelerzahl }} SuS)</div>
					<div>{{ auswahl.lehrer.size() > 1 ? 'Lehrkräfte' : 'Lehrkraft' }} {{ [...auswahl.lehrer].map(l => stundenplanManager().lehrerGetByIdOrException(l).kuerzel).join(', ') }}</div>
					<div class="text-headline-sm">Für alle Unterrichte setzen:</div>
					<div class="grid gap-2" style="grid-template-columns: 8rem auto;">
						<div />
						<svws-ui-select v-if="!disabled" label="Raumzuordnung" :items="raeumeAuswahl" :model-value="undefined" ref="refSelect"
							@update:model-value="item => auswahl && item && patchUnterrichtRaeume(unterrichteAuswahl, [item])" :item-text="raumInfo" />
					</div>
					<div class="text-headline-sm">Einzeln setzen:</div>
					<div class="grid items-center gap-2" style="grid-template-columns: 8rem auto;">
						<template v-for="u of unterrichteAuswahl" :key="u.id">
							<div>
								{{ Wochentag.fromIDorException(stundenplanManager().zeitrasterGetByIdOrException(u.idZeitraster).wochentag).kuerzel }}.,
								{{ stundenplanManager().zeitrasterGetByIdOrException(u.idZeitraster).unterrichtstunde }}. Std
								<span v-if="u.wochentyp > 0">({{ stundenplanManager().stundenplanGetWochenTypAsString(u.wochentyp) }})</span>
							</div>
							<svws-ui-multi-select label="Raumzuordnung" :items="stundenplanManager().raumGetMengeAsList()" :model-value="getRaeume(u)"
								@update:model-value="liste => patchUnterrichtRaeume(ListUtils.create1(u), liste)" :item-text="item => raumInfo(item, ListUtils.create1(u))" :disabled />
						</template>
					</div>
				</template>
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, toRaw, watch } from "vue";
	import { useRegionSwitch, type DataTableColumn } from "@ui";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "@ui";
	import type { StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import type { List, StundenplanRaum } from "@core";
	import { StundenplanKlasse } from "@core";
	import { ArrayList, StundenplanKurs, StundenplanKlassenunterricht, Fach, StundenplanUnterricht, StundenplanZeitraster, HashSet, StundenplanSchiene, BenutzerKompetenz, ListUtils, Wochentag } from "@core";

	const props = defineProps<StundenplanKlasseProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const _klasse = shallowRef<StundenplanKlasse | undefined>(undefined);
	const wochentypAnzeige = shallowRef<number>(0);
	const doppelstundenModus = shallowRef<boolean>(false);
	const schienSortierung = shallowRef<boolean>(true);
	const auswahl = ref<StundenplanKlassenunterricht|StundenplanUnterricht|StundenplanKurs|undefined>();
	const refSelect = ref();

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	const schuljahr = computed<number>(() => props.stundenplanManager().getSchuljahr());

	const klasse = computed<StundenplanKlasse>({
		get: () : StundenplanKlasse => {
			if (_klasse.value !== undefined)
				try {
					return props.stundenplanManager().klasseGetByIdOrException(_klasse.value.id);
				} catch (e) { /* empty */ }
			return props.stundenplanManager().klasseGetMengeAsList().isEmpty() ? new StundenplanKlasse() : props.stundenplanManager().klasseGetMengeAsList().get(0);
		},
		set: (value : StundenplanKlasse) => _klasse.value = value,
	});

	function getBgColor(fach: string): string {
		return Fach.getBySchluesselOrDefault(fach).getHMTLFarbeRGB(schuljahr.value);
	}

	const unterrichteAuswahl = ref<List<StundenplanUnterricht>>(new ArrayList());
	const raeumeAuswahl = ref<List<StundenplanRaum>>(new ArrayList());
	const schuelerzahl = ref<number>(0);
	const unterrichtBezeichnung = ref<string>("");

	watch(auswahl, () => {
		if (auswahl.value instanceof StundenplanKlassenunterricht) {
			unterrichteAuswahl.value = props.stundenplanManager().unterrichtGetMengeByKlasseIdAndFachId(klasse.value.id, auswahl.value.idFach);
			raeumeAuswahl.value = props.stundenplanManager().raumGetMengeAsList();
			schuelerzahl.value = props.stundenplanManager().schuelerGetAnzahlByKlasseIdOrException(klasse.value.id);
			unterrichtBezeichnung.value = props.stundenplanManager().fachGetByIdOrException(auswahl.value.idFach).bezeichnung;
		}
		else if (auswahl.value instanceof StundenplanUnterricht) {
			unterrichteAuswahl.value = props.stundenplanManager().unterrichtGetMengeByUnterrichtId(auswahl.value.id);
			raeumeAuswahl.value = props.stundenplanManager().raumGetMengeAsList();
			if (auswahl.value.idKurs === null) {
				schuelerzahl.value = props.stundenplanManager().schuelerGetAnzahlByKlasseIdOrException(klasse.value.id);
				unterrichtBezeichnung.value = props.stundenplanManager().fachGetByIdOrException(auswahl.value.idFach).bezeichnung;
			}
			else {
				schuelerzahl.value = props.stundenplanManager().schuelerGetAnzahlByKursIdAsListOrException(auswahl.value.idKurs);
				unterrichtBezeichnung.value = props.stundenplanManager().unterrichtGetByIDStringOfFachOderKurs(auswahl.value.id, true);
			}
		}
		else if (auswahl.value instanceof StundenplanKurs) {
			unterrichteAuswahl.value = props.stundenplanManager().unterrichtGetMengeByKurs(auswahl.value.id);
			raeumeAuswahl.value = props.stundenplanManager().raumGetMengeAsList();
			schuelerzahl.value = props.stundenplanManager().schuelerGetAnzahlByKursIdAsListOrException(auswahl.value.id);
			unterrichtBezeichnung.value = auswahl.value.bezeichnung;
		}
		else {
			unterrichteAuswahl.value = new ArrayList();
			raeumeAuswahl.value = new ArrayList();
			schuelerzahl.value = 0;
			unterrichtBezeichnung.value = "";
		}
	})

	function raumInfo(raum: StundenplanRaum, unterrichte: List<StundenplanUnterricht> = unterrichteAuswahl.value) {
		const ids = new ArrayList<number>();
		for (const u of unterrichte)
			ids.add(u.id);
		const beschreibung = raum.kuerzel;
		const groesse = raum.groesse;
		const kollisionen = props.stundenplanManager().raumGetAnzahlAnKollisionenFuerUnterrichte(raum.id, ids);

		const zuKlein = schuelerzahl.value > groesse;
		const belegt = kollisionen > 0;

		const status = (belegt && zuKlein)
			? "→ belegt und zu klein" : belegt
				? "→ belegt" : zuKlein
					? "→ zu klein" : "";

		return `${beschreibung} (${groesse}) ${status}`;
	}

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

	function getListOfUnterrichte(unterricht: StundenplanUnterricht) {
		const liste = new ArrayList<number>();
		for (const u of props.stundenplanManager().unterrichtGetMengeByUnterrichtId(unterricht.id))
			liste.add(u.id);
		return liste;
	}

	async function patchUnterrichtRaeume(liste: List<StundenplanUnterricht>, raeume: Iterable<StundenplanRaum>) {
		disabled.value = true;
		const ids = new ArrayList<number>();
		for (const r of raeume)
			ids.add(r.id);
		for (const u of liste)
			u.raeume = ids;
		await props.patchUnterrichte(liste);
		disabled.value = false;
	}

	const disabled = ref<boolean>(false);

	function getRaeume(u: StundenplanUnterricht) {
		const arr: StundenplanRaum[] = [];
		for (const r of u.raeume)
			arr.push(props.stundenplanManager().raumGetByIdOrException(r));
		return arr;
	}

	const dragData = shallowRef<StundenplanAnsichtDragData>(undefined);

	function onDrag(data: StundenplanAnsichtDragData, event?: DragEvent) {
		auswahl.value = undefined;
		dragData.value = data;
	}

	async function onDrop(zone: StundenplanAnsichtDropZone, wochentyp?: number) {
		// else if oder return der api-methode, sonst wird weiter geprüft
		if (dragData.value === undefined)
			return;
		// Fall StundenplanUnterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanUnterricht) && (zone instanceof StundenplanZeitraster) && (wochentyp !== undefined))
			return await props.patchUnterrichte([dragData.value], zone, wochentyp);
		// Fall List<StundenplanUnterricht> -> StundenplanZeitraster
		// Fall List<StundenplanKurs> -> StundenplanZeitraster
		// Fall List<StundenplanUnterricht> -> undefined
		if (dragData.value.isTranspiledInstanceOf("java.util.List")) {
			const listStundenplanUnterricht = new ArrayList<StundenplanUnterricht>();
			const listStundenplanKurs = new ArrayList<StundenplanKurs>();
			for (const item of dragData.value as List<unknown>)
				if (item instanceof StundenplanUnterricht)
					listStundenplanUnterricht.add(item);
				else if ((item instanceof StundenplanKurs) && (zone instanceof StundenplanZeitraster) && (wochentyp !== undefined) && props.stundenplanManager().kursDarfInZelle(item, zone.wochentag, zone.unterrichtstunde, wochentyp))
					listStundenplanKurs.add(item);
			if (listStundenplanKurs.size() > 0)
				return await props.addUnterrichte(listStundenplanKurs);
			if (listStundenplanUnterricht.size() > 0)
				if ((zone instanceof StundenplanZeitraster) && (wochentyp !== undefined))
					return await props.patchUnterrichte(listStundenplanUnterricht, zone, wochentyp);
				else if (zone === undefined)
					return await props.removeUnterrichte(listStundenplanUnterricht);
		}
		// Fall StundenplanKlassenunterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKlassenunterricht) && (zone instanceof StundenplanZeitraster) && (wochentyp !== undefined) && props.stundenplanManager().klassenunterrichtDarfInZelle(dragData.value, zone.wochentag, zone.unterrichtstunde, wochentyp)) {
			const klassen = new ArrayList<number>();
			klassen.add(dragData.value.idKlasse);
			const stunde = { idZeitraster: zone.id, wochentyp, idKurs: null, idFach: dragData.value.idFach, klassen, lehrer: dragData.value.lehrer, schienen: dragData.value.schienen };
			const arr = [];
			arr.push(stunde);
			if (doppelstundenModus.value === true && props.stundenplanManager().klassenunterrichtGetWochenstundenREST(klasse.value.id, dragData.value.idFach) >= 2) {
				const next = props.stundenplanManager().getZeitrasterNext(zone);
				if (next && props.stundenplanManager().klassenunterrichtDarfInZelle(dragData.value, zone.wochentag, next.unterrichtstunde, wochentyp))
					arr.push({ idZeitraster: next.id, wochentyp, idKurs: null, idFach: dragData.value.idFach, klassen, lehrer: dragData.value.lehrer, schienen: dragData.value.schienen });
			}
			await props.addUnterrichte(arr);
			return;
		}
		// Fall StundenplanUnterricht -> undefined
		if ((dragData.value instanceof StundenplanUnterricht) && (zone === undefined))
			return await props.removeUnterrichte([dragData.value]);
		// TODO Fall StundenplanKurs -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKurs) && (zone instanceof StundenplanZeitraster) && (wochentyp !== undefined) && props.stundenplanManager().kursDarfInZelle(dragData.value, zone.wochentag, zone.unterrichtstunde, wochentyp)) {
			const klassen = new HashSet<number>();
			const listSchueler = props.stundenplanManager().schuelerGetMengeByKursIdAsListOrException(dragData.value.id);
			for (const schueler of listSchueler)
				klassen.add(schueler.idKlasse);
			const stunde = { idZeitraster: zone.id, wochentyp, idKurs: dragData.value.id, idFach: dragData.value.idFach, klassen: new ArrayList(klassen), schienen: dragData.value.schienen, lehrer: dragData.value.lehrer };
			const arr = [];
			arr.push(stunde);
			//stundenplanManager().klassenunterrichtGetWochenstundenIST(ku.idKlasse, ku.idFach) }}/{{ stundenplanManager().klassenunterrichtGetWochenstundenSOLL(ku.idKlasse, ku.idFach)
			if (doppelstundenModus.value === true && props.stundenplanManager().kursGetWochenstundenREST(dragData.value.id) >= 2) {
				const next = props.stundenplanManager().getZeitrasterNext(zone);
				if (next && props.stundenplanManager().kursDarfInZelle(dragData.value, zone.wochentag, next.unterrichtstunde, wochentyp))
					arr.push({ idZeitraster: next.id, wochentyp, idKurs: dragData.value.id, idFach: dragData.value.idFach, klassen: new ArrayList(klassen), schienen: dragData.value.schienen, lehrer: dragData.value.lehrer });
			}
			return await props.addUnterrichte(arr);
		}
		// Fall StundenplanSchiene -> StundenplanZeitraster
		if (dragData.value instanceof StundenplanSchiene) {
			const listStundenplanKursRaw = props.stundenplanManager().kursGetMengeByKlasseIdAndSchieneId(klasse.value.id, dragData.value.id);
			const arr = [];
			for (const kurs of listStundenplanKursRaw) {
				if ((zone instanceof StundenplanZeitraster) && (wochentyp !== undefined) && props.stundenplanManager().kursDarfInZelle(kurs, zone.wochentag, zone.unterrichtstunde, wochentyp)) {
					const klassen = new HashSet<number>();
					const listSchueler = props.stundenplanManager().schuelerGetMengeByKursIdAsListOrException(kurs.id);
					for (const schueler of listSchueler)
						klassen.add(schueler.idKlasse);
					const stunde = { idZeitraster: zone.id, wochentyp, idKurs: kurs.id, idFach: kurs.idFach, klassen: new ArrayList(klassen), schienen: kurs.schienen, lehrer: kurs.lehrer };
					arr.push(stunde);
					if ((doppelstundenModus.value === true) && (props.stundenplanManager().kursGetWochenstundenREST(kurs.id) >= 2)) {
						const next = props.stundenplanManager().getZeitrasterNext(zone);
						if (next && (props.stundenplanManager().kursDarfInZelle(kurs, zone.wochentag, next.unterrichtstunde, wochentyp)))
							arr.push({ idZeitraster: next.id, wochentyp, idKurs: kurs.id, idFach: kurs.idFach, klassen: new ArrayList(klassen), schienen: kurs.schienen, lehrer: kurs.lehrer });
					}
				}
			}
			await props.addUnterrichte(arr);
			return;
		}
		// TODO Fall StundenplanZeitraster -> undefined
		// TODO Fall StundenplanPausenaufsicht -> StundenplanPausenzeit
		// TODO Fall StundenplanPausenaufsicht -> undefined
		// TODO Fall Lehrer -> StundenplanPausenzeit
	}

	function isDropZone() : boolean {
		if ((dragData.value === undefined) || (dragData.value instanceof StundenplanKurs) || (dragData.value instanceof StundenplanKlassenunterricht))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	function openTheModal(openModal: () => void) {
		openModal();
		auswahl.value = undefined;
	}

	const colsKlassenunterricht: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Klassenunterrichte", span: 1 },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", fixedWidth: 3, align: "center" },
	];

	const colsKursunterricht: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Kursunterrichte", span: 1 },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", fixedWidth: 5, align: "center" },
	];

</script>

<style scoped>

	.tr-klassenunterricht {
		grid-template-columns: minmax(4rem, 1fr) 3rem;
	}

	.tr-kursunterricht {
		grid-template-columns: minmax(4rem, 1fr) 5rem;
	}

</style>
