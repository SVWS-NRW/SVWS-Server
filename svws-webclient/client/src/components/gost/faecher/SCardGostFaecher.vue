<template>
	<ui-table-grid name="Fächer der gymnasialen Oberstufe" :header-count="2" :footer-count="0" :manager="() => gridManager" hide-selection>
		<template #header="params">
			<template v-if="params.i === 1">
				<th class="ui-divider col-span-4 text-left">
					Angebotene Fächer
				</th>
				<th class="ui-divider col-span-2">
					{{ textPJKLeitfaecher }}
				</th>
				<th class="ui-divider col-span-6">
					Wählbar
				</th>
				<th class="col-span-2">
					Abitur
				</th>
			</template>
			<template v-else>
				<th class="text-left">
					Kürzel
				</th>
				<th class="text-left">
					Bezeichnung
				</th>
				<th>
					Neu
				</th>
				<th class="ui-divider">
					<svws-ui-tooltip>
						<span>WS</span>
						<template #content>
							Wochenstunden
						</template>
					</svws-ui-tooltip>
				</th>
				<th>
					1
				</th>
				<th class="ui-divider">
					2
				</th>
				<th>
					EF.1
				</th>
				<th class="ui-divider">
					EF.2
				</th>
				<th>
					Q1.1
				</th>
				<th class="ui-divider">
					Q1.2
				</th>
				<th>
					Q2.1
				</th>
				<th class="ui-divider">
					Q2.2
				</th>
				<th>
					GK
				</th>
				<th>
					LK
				</th>
			</template>
		</template>
		<template #default="{ row: fach }">
			<td class="text-left text-uistatic" :style="{ 'background-color': getFachfarbe(fach) }">
				<span :title="fach.kuerzelAnzeige ?? undefined">
					{{ fach.kuerzelAnzeige }}
				</span>
			</td>
			<td class="text-left text-uistatic" :title="fach?.bezeichnung ?? undefined" :style="{ 'background-color': getFachfarbe(fach) }">
				<span class="line-clamp-1 break-all">{{ fach.bezeichnung }}</span>
			</td>
			<td class="flex flex-row justify-center text-uistatic" :style="{ 'background-color': getFachfarbe(fach) }">
				<input disabled type="checkbox" class="svws-ui-checkbox svws-headless" v-model="fach.istFremdSpracheNeuEinsetzend">
			</td>
			<td class="flex flex-row justify-center text-uistatic ui-divider" :class="{ 'cursor-pointer': hatWahlProjektkursStunden(fach) && hatUpdateKompetenz }"
				@click="setProjektkursStunden(fach)" :style="{ 'background-color': getFachfarbe(fach) }">
				<div v-if="hatWahlProjektkursStunden(fach) && hatUpdateKompetenz" class="flex gap-x-0.5 px-1 border border-ui-25 hover:border-ui-50 border-dashed hover:border-solid hover:bg-ui-50 p-y-[0.1rem] rounded"
					@keydown.enter="setProjektkursStunden(fach)" tabindex="0">
					<span :class="{ 'opacity-100 font-bold': fach.wochenstundenQualifikationsphase === 2, 'opacity-25 hover:opacity-100': fach.wochenstundenQualifikationsphase === 3}">2</span>
					<span class="opacity-50">/</span>
					<span :class="{ 'opacity-100 font-bold': fach.wochenstundenQualifikationsphase === 3, 'opacity-25 hover:opacity-100': fach.wochenstundenQualifikationsphase === 2}">3</span>
				</div>
				<span v-else>{{ fach.wochenstundenQualifikationsphase }}</span>
			</td>
			<td class="flex flex-row justify-center text-uistatic" :style="{ 'background-color': getFachfarbe(fach) }">
				<svws-ui-select v-if="istJahrgangAllgemein && hatLeitfach1(fach) && hatUpdateKompetenz" removable headless
					:model-value="getLeitfach1(fach)" @update:model-value="value => void patchFach({ projektKursLeitfach1ID: value?.id ?? null }, fach.id)"
					:items="getLeitfaecher1(fach)" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? '—'" />
				<span v-else class="px-2 text-center w-full" :class="{'opacity-25': !fach.projektKursLeitfach1Kuerzel}">{{ fach.projektKursLeitfach1Kuerzel || '—' }}</span>
			</td>
			<td class="flex flex-row justify-center text-uistatic ui-divider" :style="{ 'background-color': getFachfarbe(fach) }">
				<svws-ui-select v-if="istJahrgangAllgemein && istPJK(fach) && hatUpdateKompetenz" removable headless
					:model-value="getLeitfach2(fach)" @update:model-value="value => void patchFach({ projektKursLeitfach2ID: value?.id ?? null }, fach.id)"
					:items="getLeitfaecher2(fach)" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? '—'" />
				<span v-else class="px-2 text-center w-full" :class="{'opacity-25': !fach.projektKursLeitfach2Kuerzel}">{{ fach.projektKursLeitfach2Kuerzel || '—' }}</span>
			</td>
			<td class="flex flex-row justify-center text-uistatic" :class="{ 'brightness-65': !istMoeglichEF(fach) }" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichEF(fach)" :disabled="!hatUpdateKompetenz" type="checkbox" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichEF1" @change="patchFach({ istMoeglichEF1: !fach.istMoeglichEF1 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic ui-divider" :class="{'brightness-65': !istMoeglichEF(fach)}" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichEF(fach)" :disabled="!hatUpdateKompetenz" type="checkbox" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichEF2" @change="patchFach({ istMoeglichEF2: !fach.istMoeglichEF2 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic" :class="{'brightness-65': !istMoeglichQ1(fach)}" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichQ1(fach)" type="checkbox" :disabled="!hatUpdateKompetenz" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichQ11" @change="patchFach({ istMoeglichQ11: !fach.istMoeglichQ11 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic ui-divider" :class="{'brightness-65': !istMoeglichQ1(fach)}" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichQ1(fach)" type="checkbox" :disabled="!hatUpdateKompetenz" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichQ12" @change="patchFach({ istMoeglichQ12: !fach.istMoeglichQ12 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic" :style="{ 'background-color': getFachfarbe(fach) }">
				<input type="checkbox" :disabled="!hatUpdateKompetenz" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichQ21" @change="patchFach({ istMoeglichQ21: !fach.istMoeglichQ21 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic ui-divider" :style="{ 'background-color': getFachfarbe(fach) }">
				<input type="checkbox" :disabled="!hatUpdateKompetenz" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichQ22" @change="patchFach({ istMoeglichQ22: !fach.istMoeglichQ22 }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic" :class="{'brightness-65': !istMoeglichAbiGK(fach)}" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichAbiGK(fach)" :disabled="!hatUpdateKompetenz" type="checkbox" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichAbiGK" @change="patchFach({ istMoeglichAbiGK: !fach.istMoeglichAbiGK }, fach.id)">
			</td>
			<td class="flex flex-row justify-center text-uistatic" :class="{'brightness-65': !istMoeglichAbiLK(fach)}" :style="{ 'background-color': getFachfarbe(fach) }">
				<input v-if="istMoeglichAbiLK(fach)" :disabled="!hatUpdateKompetenz" type="checkbox" class="svws-ui-checkbox svws-headless"
					:checked="fach.istMoeglichAbiLK" @change="patchFach({ istMoeglichAbiLK: !fach.istMoeglichAbiLK }, fach.id)">
			</td>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	/**
	 * Die Implementierung enthält Teile von experimentellem Code. Für diesen gilt folgendes:
	 *
	 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
	 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
	 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
	 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
	 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
	 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
	 * umgesetzt werden.
	 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
	 * erlaubt.
	 */

	import { computed } from "vue";
	import { AbiturdatenManager, ArrayList, Fach, Fachgruppe, Jahrgaenge } from "@core";
	import type { GostFach, GostFaecherManager, List, ServerMode } from "@core";
	import { GridManager } from "@ui";

	const props = defineProps<{
		serverMode: ServerMode;
		faecherManager: () => GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<void>;
		abiturjahr: number;
		hatUpdateKompetenz: boolean;
	}>();

	const schuljahr = computed<number>(() => props.faecherManager().getSchuljahr());

	function getFachfarbe(fach: GostFach): string {
		return Fach.getBySchluesselOrDefault(fach.kuerzel).getHMTLFarbeRGB(schuljahr.value);
	}

	function istPJK(fach: GostFach): boolean {
		return Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value) === Fachgruppe.FG_PX;
	}

	const istJahrgangAllgemein = computed<boolean>(() => props.abiturjahr < 0);

	const textPJKLeitfaecher = computed<string>(() => {
		// experimenteller Code
		if (AbiturdatenManager.nutzeExperimentellenCode(props.serverMode, props.abiturjahr)) {
			return "Referenzfächer";
		}
		return "Leitfächer";
	});

	const gridManager = new GridManager<string, GostFach, List<GostFach>>({
		daten: computed<List<GostFach>>(() => props.faecherManager().faecher()),
		getRowKey: fach => "" + fach.id,
		columns: [
			{ kuerzel: "Kürzel", name: "Fachkürzel", width: "minmax(5rem, 0.25fr)", hideable: false },
			{ kuerzel: "Bezeichnung", name: "Fach", width: "minmax(12rem, 1fr)", hideable: false },
			{ kuerzel: "Neu", name: "Kursart", width: "minmax(2.5rem, 0.1fr)", hideable: false },
			{ kuerzel: "WS", name: "Wochenstunden", width: "minmax(3.5rem, 0.25fr)", hideable: false },
			{ kuerzel: "1", name: "1", width: "minmax(6rem, 0.25fr)", hideable: false },
			{ kuerzel: "2", name: "2", width: "minmax(6rem, 0.25fr)", hideable: false },
			{ kuerzel: "EF.1", name: "EF.1", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "EF.2", name: "EF.2", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "Q1.1", name: "Q1.1", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "Q1.2", name: "Q1.2", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "Q2.1", name: "Q2.1", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "Q2.2", name: "Q2.2", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "GK", name: "GK", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "LK", name: "LK", width: "minmax(3rem, 0.25fr)", hideable: false },
			{ kuerzel: "", name: "", width: "1.25rem", hideable: false },
		],
	});

	function istMoeglichEF(fach: GostFach): boolean {
		const fg = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value);
		return !((fg === Fachgruppe.FG_ME) || (fg === Fachgruppe.FG_PX));
	}

	function istMoeglichQ1(fach: GostFach): boolean {
		const fg = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value);
		// experimenteller Code
		if (AbiturdatenManager.nutzeExperimentellenCode(props.serverMode, props.abiturjahr)) {
			return (fg !== Fachgruppe.FG_PX);
		}
		return true;
	}

	function istMoeglichAbiGK(fach: GostFach): boolean {
		const fg = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value);
		// experimenteller Code
		if (AbiturdatenManager.nutzeExperimentellenCode(props.serverMode, props.abiturjahr)) {
			return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX);
		}
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	}

	function istMoeglichAbiLK(fach: GostFach): boolean {
		const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
		if ((f.getJahrgangAb(schuljahr.value) === Jahrgaenge.EF) ||
			((fach.biliSprache !== null) && (fach.biliSprache !== "D"))) {
			return false;
		}
		const fg = f.getFachgruppe(schuljahr.value);
		return (fg !== Fachgruppe.FG_ME) && (fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX);
	}

	function hatLeitfach1(fach: GostFach): boolean {
		const fg = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(schuljahr.value);
		return (fg === Fachgruppe.FG_VX) || (fg === Fachgruppe.FG_PX);
	}

	function getLeitfach1(fach: GostFach): GostFach | undefined {
		if (fach.projektKursLeitfach1ID === null) {
			return undefined;
		}
		return props.faecherManager().get(fach.projektKursLeitfach1ID) ?? undefined;
	}

	function getLeitfach2(fach: GostFach): GostFach | undefined {
		if (fach.projektKursLeitfach2ID === null) {
			return undefined;
		}
		return props.faecherManager().get(fach.projektKursLeitfach2ID) ?? undefined;
	}

	function getLeitfaecher1(fach: GostFach): List<GostFach> {
		const leitfaecher = props.faecherManager().getLeitfaecher();
		const leitfach2 = getLeitfach2(fach);
		if (leitfach2 === undefined) {
			return leitfaecher;
		}
		const result = new ArrayList<GostFach>(leitfaecher);
		result.removeElementAt(result.indexOf(leitfach2));
		return result;
	}

	function getLeitfaecher2(fach: GostFach): List<GostFach> {
		const leitfaecher = props.faecherManager().getLeitfaecher();
		const leitfach1 = getLeitfach1(fach);
		if (leitfach1 === undefined) {
			return leitfaecher;
		}
		const result = new ArrayList<GostFach>(leitfaecher);
		result.removeElementAt(result.indexOf(leitfach1));
		return result;
	}

	function hatWahlProjektkursStunden(fach: GostFach): boolean {
		// experimenteller Code
		if (AbiturdatenManager.nutzeExperimentellenCode(props.serverMode, props.abiturjahr)) {
			return false;
		}
		return istPJK(fach);
	}

	async function setProjektkursStunden(fach: GostFach) {
		if (!istPJK(fach)) {
			return;
		}
		await props.patchFach({ wochenstundenQualifikationsphase: fach.wochenstundenQualifikationsphase === 2 ? 3 : 2 }, fach.id);
	}

</script>
